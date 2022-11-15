import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.KeyEvent;

import java.awt.*;

public class Window extends PApplet{
  // create tempPos and tempDir for now
  private PVector tempPos = new PVector(2f, 2f);
  private PVector tempDir = new PVector(1f, 1f);

  private CannonBall ball = new CannonBall( tempPos, tempDir,this);
  protected int width = 1280;
  protected int height = 720;

  // turn = false means player1's turn.
  // turn = true means player1's turn.
  protected boolean turn = false;

  PFont font;

  private Player leftPlayer = new Player(new PVector(50,this.height - 200), this);
  private Player rightPlayer = new Player(new PVector(width - 100,this.height - 200), this);

  public void draw() {
    background(10);
    ball.move(this);
    ball.draw(this);
    leftPlayer.draw(this);
    rightPlayer.draw(this);
    drawDashboard();
    drawHp();
    drawFuel();

  }

  public void drawDashboard() {
    textSize(20);
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
    this.fill(146, 208, 80);
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

  public void settings() {
    size(this.width, this.height);
  }
  @Override
  public void keyPressed(KeyEvent event) {
    super.keyPressed(event);
    Player CurrentPlayer;
    if (!this.turn) {
      CurrentPlayer = leftPlayer;
    } else {
      CurrentPlayer = rightPlayer;
    }

    if (CurrentPlayer == null) {
      return;
    }
    switch (event.getKeyCode()) {
      case RIGHT:
        CurrentPlayer.move(10);
        break;
      case LEFT:
        CurrentPlayer.move(-10);
        break;
      default:
        break;
    }
    if (!this.turn) {
      leftPlayer = CurrentPlayer;
    } else {
      rightPlayer = CurrentPlayer;
    }
  }

  public static void main(String[] args) {
    String[] processingArgs = {"processingWindow"};
    Window processingWindow = new Window();
    PApplet.runSketch(processingArgs, processingWindow);
  }
}
