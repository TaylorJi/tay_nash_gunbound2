import processing.core.PApplet;
public class Window extends PApplet{
  private CannonBall ball = new CannonBall((float) 30, (float) 30, this);

  public void draw() {
    background(10);
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
