import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

public class Window extends PApplet{
  private CannonBall ball = new CannonBall((float) 30, (float) 30, this);

  private int width = 640;
  private int height = 360;

  private Player leftPlayer = new Player(new PVector(30,this.height - 50), this);
  private Player rightPlayer = new Player(new PVector(width - 50,this.height - 50), this);

  public void draw() {
    background(10);
    ball.draw(this);
    leftPlayer.draw(this);
    rightPlayer.draw(this);
  }

  public void settings() {
    size(this.width, this.height);
  }

  public static void main(String[] args) {
    String[] processingArgs = {"processingWindow"};
    Window processingWindow = new Window();
    PApplet.runSketch(processingArgs, processingWindow);
  }
}
