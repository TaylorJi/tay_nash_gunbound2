import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.util.concurrent.TimeUnit;

import java.util.ArrayList;

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

  PFont font;

  protected Player leftPlayer = new Player(new PVector(50,this.height - 200), this);
  protected Player rightPlayer = new Player(new PVector(width - 100,this.height - 200), this);
  protected Player currentPlayer = leftPlayer;
  protected CannonBall ball = new CannonBall(currentPlayer.position, tempDir,this);
  protected PVector wallPosition = new PVector(this.width / 2, this.dashboardHeight);

  protected int numberOfObstacles = 40;
  protected Obstacle wall = new Obstacle(1,false, wallPosition);
//  protected Obstacle obstacle = new Obstacle(10, true);

  protected ArrayList<ICollidable> collidables = new ArrayList<>();
//  PVector circleVector = new PVector(random(this.height), random(this.width));

  protected ArrayList<PVector> obstacleVector = new ArrayList<>();
  private OnEventListner mListener;

  double obstacleSize;

  boolean ballMove = false;

  public void registerOnEventListener(OnEventListner mListener) {
    this.mListener = mListener;
  }


  public void draw() {
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
      this.obstacleSize = 30;
      Obstacle curr = (Obstacle) collidables.get(i);
      curr.draw(CIRCLE, curr.position, obstacleSize ,this);
    }
    for (int i = numberOfObstacles / 2; i < numberOfObstacles; i++) {
      this.obstacleSize = 10;
      Obstacle curr = (Obstacle) collidables.get(i);
      curr.draw(RECT, curr.position, obstacleSize ,this);
    }
    if ((leftPlayer.getHp() == 0) || (rightPlayer.getHp() == 0)) {
      gameOver();
    }
  }

  public void afterFire() {
    System.out.println("Fire button pushed!");
    if(this.mListener != null) {
      mListener.onEvent();
    }
  }



  public void gameOver() {// write sql query ex) currentPlayer name,
    if (leftPlayer.getHp() == 0) {
      textSize(100);
      rect(200, this.height - 500, 900, this.height - 500);
      fill(3, 253, 247);
      text("Player2 won!", 200, this.height - 400);
      text("Press TAB key to quit.", 200, this.height - 300);
    } else if (rightPlayer.getHp() == 0) {
      textSize(100);
      rect(200, this.height - 500, 900, this.height - 500);
      fill(3, 253, 247);
      text("Player1 won!", 200, this.height - 400);
      text("Press TAB key to quit.", 200, this.height - 300);
    }
  }

  public void showTitle() {
    image(img, 0, 0);
    try {
      TimeUnit.SECONDS.sleep(3);
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
    text("Player1", 50, this.height - 100);
    text("Player2", this.width - 300, this.height - 100);
    textSize(18);
    fill(3, 140, 253);
    // for player1
    text("HP", 50, this.height - 60);
    text("Fuel", 50, this.height - 30);
    // for player2
    text("HP", this.width - 300, this.height - 60);
    text("Fuel", this.width - 300, this.height - 30);
    updateMsgBox();
  }

  void updateMsgBox() {
    if (!this.turn) {
      fill(206, 254, 238);
      text("Player1's turn",this.width - 800 ,this.height - 80 );
      fill(255, 255, 204);
      text("Move your tank by ← → key",this.width - 800 ,this.height - 60 );
      text("Set the angle with by ↑ ↓ key",this.width - 800 ,this.height - 40 );
      text("Fire the cannon ball by ENTER key",this.width - 800 ,this.height - 20 );
    } else {
      fill(206, 254, 238);
      text("Player2's turn",this.width - 800 ,this.height - 80 );
      fill(255, 255, 204);
      text("Move your tank by ← → key",this.width - 800 ,this.height - 60 );
      text("Set the angle with by ↑ ↓ key",this.width - 800 ,this.height - 40 );
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
        text("Press ENTER to turn over.", 100, this.height - 10);
      } else {
        text("Fuel is empty!", this.width - 250, this.height - 30);
        text("Press ENTER to turn over.", this.width - 250, this.height - 10);
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
    if (leftPlayer.getHp() == 100) {
      this.fill(0, 204, 0);
    } else if ((leftPlayer.getFuel() < 100) && (leftPlayer.getFuel() >= 30)) {
      this.fill(249, 227, 41);
    } else if (leftPlayer.getFuel() < 30) {
      this.fill(246, 0, 0);
    }
    this.rect(100, this.height - 70, leftPlayer.getHp(), 10);

    // for player2
    if (rightPlayer.getHp() == 100) {
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
    collidables.add(wall);
    collidables.add(leftPlayer);
    collidables.add(rightPlayer);
  }
  @Override
  public void keyPressed(KeyEvent event) {
    super.keyPressed(event);
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
      case BACKSPACE:
        currentPlayer.setHp(10);
        break;
      case CONTROL:
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
      case TAB:
        exit();
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