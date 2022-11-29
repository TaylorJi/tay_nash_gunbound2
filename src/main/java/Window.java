import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Window extends PApplet{
  // create tempPos and tempDir for now
  protected static final int WALL = 1;
  protected static final int CIRCLE = 2;
  protected static final int RECT = 3;

  protected PVector tempPos = new PVector(50, this.height + 500);
  protected PVector tempDir = new PVector(1f, -1f).normalize();

  protected CannonBall ball = new CannonBall(tempPos, tempDir,this);
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

  PFont font;

  protected Player leftPlayer = new Player(new PVector(50,this.height - 200), this);
  protected Player rightPlayer = new Player(new PVector(width - 100,this.height - 200), this);
  protected Player currentPlayer;
  protected PVector wallPosition = new PVector(this.width / 2, this.dashboardHeight);

  protected int numberOfObstacles = 40;
  protected Obstacle wall = new Obstacle(1,false);
//  protected Obstacle obstacle = new Obstacle(10, true);

  protected ArrayList<Obstacle> obstacle = new ArrayList<>();
//  PVector circleVector = new PVector(random(this.height), random(this.width));

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
//    if (this.title) {
//      showTitle();
//    }
    if (!this.turn) {
      currentPlayer = leftPlayer;
    } else {
      currentPlayer = rightPlayer;
    }
    background(10);
    leftPlayer.draw(this);
    rightPlayer.draw(this);
    ball.draw(currentPlayer.position, this);
    if(ballMove) {
      ball.move(currentPlayer,this);
    }
    drawAngle(currentPlayer, currentPlayer.angleDirection);
    drawDashboard();
    drawHp();
    drawFuel();
    wall.draw(WALL, wallPosition, obstacleSize, this);
    for (int i = 0; i <numberOfObstacles / 2; i++) {
      this.obstacleSize = 30;
      obstacle.get(i).draw(CIRCLE, obstacleVector.get(i),obstacleSize ,this);
    }
    for (int i = numberOfObstacles / 2; i <numberOfObstacles; i++) {
      this.obstacleSize = 10;
      obstacle.get(i).draw(RECT, obstacleVector.get(i),obstacleSize ,this);
    }
    if ((leftPlayer.getHp() == 0) || (rightPlayer.getHp() == 0)) {
      gameOver();
    }
  }

  public void gameOver() {
    if (leftPlayer.getHp() == 0) {
      textSize(100);
      rect(200, this.height - 500, 900, this.height - 500);
      fill(3, 253, 247);
      text(player2Name + " won!", 200, this.height - 400);
      text("Press ESC key to quit.", 200, this.height - 300);
      this.winner = 2;
    } else if (rightPlayer.getHp() == 0) {
      textSize(100);
      rect(200, this.height - 500, 900, this.height - 500);
      fill(3, 253, 247);
      text(player1Name + " won!", 200, this.height - 400);
      text("Press ESC key to quit.", 200, this.height - 300);
      this.winner = 1;
    }
    calculateTotalScore(this.winner);
  }

  public void showTitle() {
    image(img, 35, 0);
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
    if (this.cheatMode == true) {
      cheatModeOn(1);
    }
  }

  void cheatModeOn(int option) {
    switch (option) {
      case 0:
        fill(0, 0, 0);
        text("<Cheat Mode>", this.width - 600, this.height - 200);
        text("BACKSPACE: increase my HP", this.width - 600, this.height - 180);
        text("DELETE: increase my FUEL", this.width - 600, this.height - 160);
      break;
      case 1:
        fill(127, 127, 127);
        text("<Cheat Mode>", this.width - 600, this.height - 200);
        text("BACKSPACE: increase my HP", this.width - 600, this.height - 180);
        text("DELETE: increase my FUEL", this.width - 600, this.height - 160);
      break;
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
    int turnScore = 0;
    int hpScore = 0;
    int finalScore = 0;

    switch(winner) {
      case 1:
        leftPlayer.score += (100 - this.turnCnt);
        leftPlayer.score += (leftPlayer.getHp() - rightPlayer.getHp());
        finalScore = leftPlayer.score;
        break;
      case 2:
        rightPlayer.score += (100 - this.turnCnt);
        rightPlayer.score += (rightPlayer.getHp() - leftPlayer.getHp());
        finalScore = rightPlayer.score;
        break;
      default:
        break;
    }

    return 0;
  }

  void updateMsgBox() {
    String rnd = "<Round: " + this.turnCnt + ">";
    if (!this.turn) {
      fill(206, 254, 238);
      text(rnd, this.width - 800 ,this.height - 100);
      text(player1Name + "'s turn",this.width - 800 ,this.height - 80 );
      fill(255, 255, 204);
      text("Move your tank by LEFT/RIGHT key",this.width - 800 ,this.height - 60 );
      text("Set the angle with by UP/DOWN key",this.width - 800 ,this.height - 40 );
      text("Fire the cannon ball by ENTER key",this.width - 800 ,this.height - 20 );
    } else {
      fill(206, 254, 238);
      text(rnd, this.width - 800 ,this.height - 100);
      text(player2Name + "'s turn",this.width - 800 ,this.height - 80 );
      fill(255, 255, 204);
      text("Move your tank by LEFT/RIGHT key",this.width - 800 ,this.height - 60 );
      text("Set the angle with by UP/DOWN key",this.width - 800 ,this.height - 40 );
      text("Fire the cannon ball by ENTER key",this.width - 800 ,this.height - 20 );
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
        currentPlayer.changeTurn(currentPlayer, this);
      } else {
        text("Fuel is empty!", this.width - 250, this.height - 30);
        currentPlayer.changeTurn(currentPlayer, this);
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

  public void drawHp() {
    // for player1
    if (leftPlayer.getHp() >= 100) {
      this.fill(0, 204, 0);
    } else if ((leftPlayer.getFuel() < 100) && (leftPlayer.getFuel() >= 30)) {
      this.fill(249, 227, 41);
    } else if (leftPlayer.getFuel() < 30) {
      this.fill(246, 0, 0);
    }
    this.rect(100, this.height - 70, leftPlayer.getHp(), 10);

    // for player2
    if (rightPlayer.getHp() >= 100) {
      this.fill(0, 204, 0);
    } else if ((rightPlayer.getFuel() < 100) && (rightPlayer.getFuel() >= 30)) {
      this.fill(249, 227, 41);
    } else if (rightPlayer.getFuel() < 30) {
      this.fill(246, 0, 0);
    }
    this.rect(this.width - 240, this.height - 70, rightPlayer.getHp(), 10);
  }

  public void drawFuel() {
    // for player1
    if (leftPlayer.getFuel() >= 70) {
      this.fill(118, 59, 0);
    } else if ((leftPlayer.getFuel() < 70) && (leftPlayer.getFuel() >= 30)) {
      this.fill(164, 82, 0);
    } else if (leftPlayer.getFuel() < 30) {
      this.fill(246, 0, 0);
    }
    this.rect(100, this.height - 40, leftPlayer.getFuel(), 10);

    // for player2
    if (rightPlayer.getFuel() >= 70) {
      this.fill(118, 59, 0);
    } else if ((rightPlayer.getFuel() < 70) && (rightPlayer.getFuel() >= 30)) {
      this.fill(164, 82, 0);
    } else if (rightPlayer.getFuel() < 30) {
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
      this.line(xPos + player.width, yPos, xPos + (angleDirection.x) * 50, yPos - (angleDirection.y) * 50);
    } else {
      this.line(xPos, yPos, xPos - angleDirection.x * 40, yPos - angleDirection.y * 40);
    }
    this.stroke(0);
  }

  public void settings() {
    size(this.width, this.height);
    img = loadImage("title.jpg");
    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
    System.out.println("\n\nPlease Enter player 1's name: ");
    player1Name = myObj.nextLine();
    System.out.println("Please Enter player 2's name: ");
    player2Name = myObj.nextLine();
    myObj.close();

    // Initialize obstacles
    for (int i = 0; i < numberOfObstacles / 2; i++) {
      obstacle.add(new Obstacle(1,true));
      obstacleVector.add(new PVector(random(width), random(height - 250)));
    }
    for (int i = numberOfObstacles / 2; i < numberOfObstacles; i++) {
      obstacle.add(new Obstacle(1,false));
      obstacleVector.add(new PVector(random(width), random(height - 250)));
    }

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
//    if (keyPressed) {
//      if ((key == 'z' || key == 'Z') && (cheatMode)){
//        println("B pressed");
//      }
//    }
    switch (event.getKeyCode()) {
      case RIGHT:
        currentPlayer.move(5);
        currentPlayer.decreaseFuel(2);
        break;
      case LEFT:
        currentPlayer.move(-5);
        currentPlayer.decreaseFuel(2);
        break;
      case UP:
        currentPlayer.setAngle(currentPlayer, 0.005F);
        currentPlayer.decreaseFuel(0.1F);
        break;
      case DOWN:
        currentPlayer.setAngle(currentPlayer, -0.005F);
        currentPlayer.decreaseFuel(0.1F);
        break;
      case ENTER:
        this.ballMove = true;
        currentPlayer.fire(currentPlayer, ball, this);
        break;
      case BACKSPACE:
        if (cheatMode)
          currentPlayer.setHp(-10);
        break;
      case DELETE:
        if (cheatMode)
          currentPlayer.decreaseFuel(-10);
        break;
      case TAB:
        currentPlayer.changeTurn(currentPlayer, this);
        break;
      case ALT:
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
