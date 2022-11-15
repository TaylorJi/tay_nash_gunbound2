import processing.core.PApplet;
import processing.core.PVector;

public class Window extends PApplet{
  // create tempPos and tempDir for now
  private PVector tempPos = new PVector(2f, 2f);
  private PVector tempDir = new PVector(1f, 1f);

  private CannonBall ball = new CannonBall( tempPos, tempDir,this);

  public void draw() {
    background(10);
    ball.move(this);
    ball.draw(this);
  }

  public void settings() {
    size(640, 360);
  }

  public static void main(String[] args) {
    String[] processingArgs = {"processingWindow"};
    Window processingWindow = new Window();
    PApplet.runSketch(processingArgs, processingWindow);
  }
}
