import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Window extends PApplet{
  // create tempPos and tempDir for now
  protected static final int WALL = 1;
  protected static final int CIRCLE = 2;
  protected static final int RECT = 3;

  protected PVector tempDir = new PVector(1f, -1f).normalize();

  protected int width = 1280;
  protected int height = 720;


  // turn = false means player1's turn.
  // turn = true means player2's turn.
  protected boolean turn = false;
  //  protected boolean turn = true;
  protected boolean title = true;
  PImage img;
  protected int dashboardHeight = this.height - 130;

  protected String player1Name;
  protected String player2Name;

  protected Player leftPlayer = new Player(new PVector(50,this.height - 200), this);
  protected Player rightPlayer = new Player(new PVector(width - 100,this.height - 200), this);
  protected Player currentPlayer = leftPlayer;
  protected CannonBall ball = new CannonBall(currentPlayer.position, tempDir,this);
  protected PVector wallPosition = new PVector((float)this.width / 2, this.dashboardHeight);

  protected int numberOfObstacles = 40;
  protected Obstacle wall = new Obstacle(1, false, wallPosition);

  protected ArrayList<ICollidable> collidables = new ArrayList<>();

  protected ArrayList<PVector> obstacleVector = new ArrayList<>();
  private OnEventListner mListener;

  double obstacleSize;

  boolean ballMove = false;

  protected int turnCnt = 0;
  protected boolean cheatMode = false;

  // default : 0
  // player 1 won: 1
  // player 2 won: 2
  protected int winner = 0;

  // MongoDB for leaderboard,
  private final String conStr = "mongodb+srv://admin1:123@cluster0.dmvywtj.mongodb.net/?retryWrites=true&w=majority";

  private final MongoClient mongoClient = MongoClients.create(
          MongoClientSettings.builder()
                  .applyConnectionString(
                          new ConnectionString(
                                  conStr
                          )
                  )
                  .retryWrites(true)
                  .build()
  );
  private final MongoCollection collection = mongoClient.
          getDatabase("app").
          getCollection("ranking");



  public void registerOnEventListener(OnEventListner mListener) {
    this.mListener = mListener;
  }

  public void afterFire() {
    System.out.println("Fire button pushed!");

    if(this.mListener != null) {
      mListener.onEvent();
    }
  }

  public void draw() {
    if (this.title) {
      showTitle();
    }
    if (!this.turn) {
      currentPlayer = leftPlayer;
    } else {
      currentPlayer = rightPlayer;
    }
    background(10);
    leftPlayer.draw(this);
    rightPlayer.draw(this);
    ball.draw(ball.position, this);
    if(ballMove) {
      ball.move(currentPlayer, this);
    }
    drawAngle(currentPlayer, currentPlayer.angleDirection);
    drawDashboard();
    drawHp();
    drawFuel();
    wall.draw(WALL, wallPosition, obstacleSize, this);
    for (int i = 0; i < numberOfObstacles / 2; i++) {
      this.obstacleSize = 10;
      Obstacle curr = (Obstacle) collidables.get(i);
      curr.draw(CIRCLE, curr.position, obstacleSize ,this);
    }
    for (int i = numberOfObstacles / 2; i < numberOfObstacles; i++) {
      this.obstacleSize = 5;
      Obstacle curr = (Obstacle) collidables.get(i);
      curr.draw(RECT, curr.position, obstacleSize ,this);
    }
    if ((leftPlayer.getHp() == 0) || (rightPlayer.getHp() == 0)) {
      gameOver();
    }
  }

  public void gameOver() {
    String winnerName = "";
    if (leftPlayer.getHp() == 0) {
      gameOverMsg(player2Name);
      this.winner = 2;
      winnerName = player2Name;
    } else if (rightPlayer.getHp() == 0) {
      this.winner = 1;
      winnerName = player1Name;
    }

    int finalScore = calculateTotalScore(this.winner);

    // record winner to the db
    collection.insertOne(new Document()
            .append("userName", winnerName)
            .append("score", finalScore)
    );

    // draw game over message for winner with the score if in the top 5
    gameOverMsg(winnerName);
    noLoop();
  }

  public void gameOverMsg(String player) {
    textSize(60);
    rect(200, this.height - 700, 900, this.height - 500);
    fill(3, 253, 247);
    text(player + " won!", 350, this.height - 650);
    text("Press ESC key to quit.", 500, this.height - 600);

    // query the 5th highest score (or at least the range)
    List<Document> col = new ArrayList<>();
    collection.find().sort(Sorts.descending("score")).limit(5).into(col);
    for (int i = 0; i < col.size(); i++) {
      String name = (String) col.get(i).get("userName");
      int playerScore = Integer.parseInt(col.get(i).get("score").toString());
      System.out.println("name: " + name + " score: " + playerScore);
    }

  }

  public void showTitle() {
    image(this.img, 35, 0);
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    this.title = !this.title;
  }

  public void drawDashboard() {
    textSize(20);
    fill(8, 190, 27);
    rect(0, dashboardHeight, this.width, 2);
    fill(3, 253, 247);
    text(player1Name, 50, this.height - 100);
    text(player2Name, this.width - 300, this.height - 100);
    textSize(18);
    fill(3, 140, 253);
    // for player1
    text("HP", 50, this.height - 60);
    text("Fuel", 50, this.height - 30);
    // for player2
    text("HP", this.width - 300, this.height - 60);
    text("Fuel", this.width - 300, this.height - 30);
    updateScore();
    updateMsgBox();
    if (this.cheatMode) {
      cheatModeOn(1);
    } else {
      cheatModeOn(0);
    }
  }

  void cheatModeOn(int option) {
    switch (option) {
      case 0 -> {
        fill(0, 0, 0);
        text("<Test Mode>", this.width - 600, this.height - 100);
      }
      case 1 -> {
        fill(127, 127, 127);
        text("<Test Mode>", this.width - 600, this.height - 100);
      }
    }
  }

  void updateScore() {
    String lscore = "Score: " + leftPlayer.score;
    String rscore = "Score: " + rightPlayer.score;
    fill(255, 255, 204);
    text(lscore, 150, this.height - 100);
    text(rscore, this.width - 200, this.height - 100);

  }

  // turn = false means player1's turn.
  // turn = true means player2's turn.
  boolean getTurn() {
    return this.turn;
  }

  int calculateTotalScore(int winner) {
    // + 50 : if one player's cannonball hits other player
    // + 5 : if one player's cannonball breaks obstacle in the air
    // final score calculation logic
    // 1) remained turns : + (100 - current turn) * 10
    // 2) difference from other players' HP : (my HP - other's HP) *20
    // 3) plus current score
    int finalScore = 0;

    switch (winner) {
      case 1 -> {
        leftPlayer.score += (100 - this.turnCnt);
        leftPlayer.score += (leftPlayer.getHp() - rightPlayer.getHp());
        finalScore = leftPlayer.score;
      }
      case 2 -> {
        rightPlayer.score += (100 - this.turnCnt);
        rightPlayer.score += (rightPlayer.getHp() - leftPlayer.getHp());
        finalScore = rightPlayer.score;
      }
      default -> {
      }
    }

    return finalScore;
  }

  void updateMsgBox() {
    String rnd = "<Round: " + this.turnCnt + ">";
    if (!this.turn) {
      showGuide(rnd, player1Name);
    } else {
      showGuide(rnd, player2Name);
    }

    if (currentPlayer.getHp() < 30) {
      fill(255, 80, 1);
      if (!this.turn) {
        text("Warning! low HP!", 200, this.height - 50);
      } else {
        text("Warning! low HP!", this.width - 500, this.height - 50);
      }
    }

    if (currentPlayer.getFuel() == 0) {
      fill(255, 80, 1);
      if (!this.turn) {
        text("Fuel is empty!", 100, this.height - 30);
        this.currentPlayer.changeTurn(this.currentPlayer, this);
        if (!this.turn) {
          this.currentPlayer = this.leftPlayer;
        } else {
          this.currentPlayer = this.rightPlayer;
        }
        ball.position = this.currentPlayer.position;
      } else {
        text("Fuel is empty!", this.width - 250, this.height - 30);
        this.currentPlayer.changeTurn(this.currentPlayer, this);
        if (!this.turn) {
          this.currentPlayer = this.leftPlayer;
        } else {
          this.currentPlayer = this.rightPlayer;
        }
        ball.position = this.currentPlayer.position;
      }
    } else if (currentPlayer.getFuel() < 30) {
      fill(255, 80, 1);
      if (!this.turn) {
        text("Warning! Fuel shortage.", 200, this.height - 30);
      } else {
        text("Warning! Fuel shortage.", this.width - 500, this.height - 30);
      }
    }
  }

  public void showGuide(String rnd, String player) {
    fill(206, 254, 238);
    text(rnd, this.width - 800 ,this.height - 100);
    text(player + "'s turn",this.width - 800 ,this.height - 80 );
    fill(255, 255, 204);
    text("Move your tank by LEFT/RIGHT key",this.width - 800 ,this.height - 60 );
    text("Set the angle with by UP/DOWN key",this.width - 800 ,this.height - 40 );
    text("Fire the cannon ball by ENTER key",this.width - 800 ,this.height - 20 );
  }

  public void drawHp() {
    // for player1
    drawHpForPlayer(leftPlayer);
    this.rect(100, this.height - 70, leftPlayer.getHp(), 10);

    // for player2
    drawHpForPlayer(rightPlayer);
    this.rect(this.width - 240, this.height - 70, rightPlayer.getHp(), 10);
  }

  public void drawHpForPlayer(Player player) {
    if (player.getHp() >= 100) {
      this.fill(0, 204, 0);
    } else if ((player.getHp() < 100) && (player.getHp() >= 30)) {
      this.fill(249, 227, 41);
    } else if (player.getHp() < 30) {
      this.fill(246, 0, 0);
    }
  }

  public void drawFuel() {
    // for player1
    drawFuelForPlayer(leftPlayer);
    this.rect(100, this.height - 40, leftPlayer.getFuel(), 10);

    // for player2
    drawFuelForPlayer(rightPlayer);
    this.rect(this.width - 240, this.height - 40, rightPlayer.getFuel(), 10);
  }

  public void drawFuelForPlayer(Player player) {
    if (player.getFuel() >= 70) {
      this.fill(118, 59, 0);
    } else if ((player.getFuel() < 70) && (player.getFuel() >= 30)) {
      this.fill(164, 82, 0);
    } else if (player.getFuel() < 30) {
      this.fill(246, 0, 0);
    }
    this.rect(this.width - 240, this.height - 40, rightPlayer.getFuel(), 10);
  }

  public void drawAngle(Player player, PVector angleDirection) {
    float xPos;
    float yPos;
    xPos = player.getPosition().x;
    yPos = player.getPosition().y;
    this.stroke(255,179,179);
    if (!this.turn) {
      currentPlayer = leftPlayer;
    } else {
      currentPlayer = rightPlayer;
    }

    if (player ==leftPlayer) {
      this.line(xPos, yPos, xPos + (angleDirection.x * 50), yPos - (angleDirection.y) * 50);
    } else {
      this.line(xPos + player.width, yPos, xPos - (angleDirection.x * 50), yPos - (angleDirection.y) * 80);
    }
    this.stroke(0);
  }

  public void settings() {
    size(this.width, this.height);
    img = loadImage("title.jpg");
    Scanner myObj = new Scanner(System.in);
    System.out.println("\n\nPlease Enter player 1's name: ");
    player1Name = myObj.nextLine();
    System.out.println("Please Enter player 2's name: ");
    player2Name = myObj.nextLine();
    myObj.close();

    // Initialize obstacles
    for (int i = 0; i < numberOfObstacles / 2; i++) {
      collidables.add(new Obstacle(
              1,
              true,
              new PVector((random(width)), random(height - 250))
      ));
      obstacleVector.add(new PVector(random(width), random(height - 250)));
    }
    for (int i = numberOfObstacles / 2; i < numberOfObstacles; i++) {
      collidables.add(new Obstacle(
              1,
              false,
              new PVector((random(width)), random(height - 250))
      ));
      obstacleVector.add(new PVector(random(width), random(height - 250)));
    }
    collidables.add(leftPlayer);
    collidables.add(rightPlayer);
    collidables.add(wall);
  }
  @Override
  public void keyPressed(KeyEvent event) {
    super.keyPressed(event);
//    Player currentPlayer;
    if (!this.turn) {
      currentPlayer = leftPlayer;
    } else {
      currentPlayer = rightPlayer;
    }

    if ((currentPlayer == null) || (currentPlayer.fuel == 0)){
      if(event.getKeyCode() == ENTER) {
        currentPlayer.fire(currentPlayer, ball, this);
      }
      return;
    }
    if (keyPressed) {
      if ((key == 'z' || key == 'Z') && (cheatMode)){
        currentPlayer.setHp(-10);
      }
      if ((key == 'x' || key == 'X') && (cheatMode)){
        currentPlayer.decreaseFuel(-10);
      }
      if ((key == 's' || key == 'S') && (cheatMode)){
        currentPlayer.decreaseFuel(10);
      }
      if ((key == 'a' || key == 'A') && (cheatMode)){
        currentPlayer.setHp(10);
      }
    }
    switch (event.getKeyCode()) {
      case RIGHT:
        if (!ballMove) {
          currentPlayer.move(5);
          currentPlayer.decreaseFuel(2);
          ball.position.x += 5;
        }
        break;
      case LEFT:
        if (!ballMove) {
          currentPlayer.move(-5);
          currentPlayer.decreaseFuel(2);
          ball.position.x -= 5;
        }
        break;
      case UP:
        currentPlayer.setAngle(currentPlayer, 0.006F);
        currentPlayer.decreaseFuel(0.1F);
        break;
      case DOWN:
        currentPlayer.setAngle(currentPlayer, -0.006F);
        currentPlayer.decreaseFuel(0.1F);
        break;
      case ENTER:
        ball.setDirection(currentPlayer.angleDirection);
        this.ballMove = true;
        currentPlayer.fire(currentPlayer, ball, this);
        break;
      case TAB:
        if (!ballMove) {
          currentPlayer.changeTurn(currentPlayer, this);
          if (!this.turn) {
            currentPlayer = leftPlayer;
          } else {
            currentPlayer = rightPlayer;
          }
          ball.position = currentPlayer.position;
        }
        break;
      case CONTROL:
        this.cheatMode = !this.cheatMode;
        break;
      default:
        break;
    }
  }

  public static void main(String[] args) {
    String[] processingArgs = {"processingWindow"};
    Window processingWindow = new Window();
    PApplet.runSketch(processingArgs, processingWindow);
  }
}