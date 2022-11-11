import processing.core.PApplet;
public class Window extends PApplet{
  public static void main(String[] args) {
    String[] processingArgs = {"processingWindow"};
    Window processingWindow = new Window();
    PApplet.runSketch(processingArgs, processingWindow);

    System.out.println("hello");
  }
}
