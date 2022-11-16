import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;

public class Window extends PApplet{
  // create tempPos and tempDir for now
  protected PVector tempPos = new PVector(50, this.height + 500);
  protected PVector tempDir = new PVector(1f, 1f).normalize();

  protected CannonBall ball = new CannonBall(tempPos, tempDir,this);
  protected PVector angleDirection = new PVector(3,1).normalize();
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
  protected Obstacle wall = new Obstacle(1,false);

  public void draw() {
//    if (this.title) {
//      showTitle();
//    }
    Player currentPlayer;
    if (!this.turn) {
      currentPlayer = leftPlayer;
    } else {
      currentPlayer = rightPlayer;
    }
    background(10);
    leftPlayer.draw(this);
    rightPlayer.draw(this);
    ball.draw(currentPlayer.position, this);
    ball.move(this);
    drawAngle(currentPlayer, angleDirection);
    drawDashboard();
    drawHp();
    drawFuel();
    wall.draw(this);
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
  }

  public void drawHp() {
    // for player1
    this.fill(249, 227, 41);
    this.rect(100, this.height - 70, leftPlayer.getHp(), 10);

    // for player2
    this.rect(this.width - 240, this.height - 70, rightPlayer.getHp(), 10);
  }

  public void drawFuel() {
    // for player1
    this.fill(164, 82, 0);
    this.rect(100, this.height - 40, leftPlayer.getFuel(), 10);

    // for player2
    this.rect(this.width - 240, this.height - 40, rightPlayer.getFuel(), 10);
  }

  public void drawAngle(Player player, PVector angleDirection) {
    float xPos;
    float yPos;
    xPos = player.getPosition().x;
    yPos = player.getPosition().y;
    this.stroke(255,179,179);
    this.line(xPos + player.width, yPos, xPos + (angleDirection.x) * 50, yPos - (angleDirection.y) * 50);
    this.stroke(0);
  }

  public void setAngleDirection(double degree) {
    PVector direction = new PVector(this.angleDirection.x,
                      this.angleDirection.y);
//    PVector direction = new PVector(this.angleDirection.x + (float)degree,
//            this.angleDirection.y - (float)degree).normalize();
    if ((getAngle(this.angleDirection.x + degree, this.angleDirection.y - degree) > 0.9)
      || (getAngle(this.angleDirection.x + degree, this.angleDirection.y - degree) < 0)) {
      return;
    } else {
      this.angleDirection.x += degree;
      this.angleDirection.y -= degree;
    }
  }

  double getAngle(double x, double y) {
    return atan((float)y/(float)x);
  }

  public void settings() {
    size(this.width, this.height);
    img = loadImage("title.jpg");
  }
  @Override
  public void keyPressed(KeyEvent event) {
    super.keyPressed(event);
    Player currentPlayer;
    if (!this.turn) {
      currentPlayer = leftPlayer;
    } else {
      currentPlayer = rightPlayer;
    }

    if ((currentPlayer == null) || (currentPlayer.fuel == 0)){
      return;
    }
    switch (event.getKeyCode()) {
      case RIGHT:
        currentPlayer.move(10);
        currentPlayer.setFuel(10);
        break;
      case LEFT:
        currentPlayer.move(-10);
        currentPlayer.setFuel(10);
        break;
      case UP:
        setAngleDirection(-0.01);
        break;
      case DOWN:
        setAngleDirection(0.01);
        break;
      default:
        break;
    }
    if (!this.turn) {
      leftPlayer = currentPlayer;
    } else {
      rightPlayer = currentPlayer;
    }
  }

  public static void main(String[] args) {
    String[] processingArgs = {"processingWindow"};
    Window processingWindow = new Window();
    PApplet.runSketch(processingArgs, processingWindow);
  }
}
