import processing.core.PVector;

import java.util.Date;

abstract public class AbstractPlayer {
  protected PVector position;
  protected Window window;
  protected int hp;
  protected int fuel;

  private String name;

  public abstract void move();

  public abstract void setAngle();
  public abstract void setPower();
  public abstract void fire();
  public abstract void collide();

  public abstract void draw(Window window);

  public AbstractPlayer(PVector position, Window window) {
    this.position = position;
    this.window = window;
  }

}
