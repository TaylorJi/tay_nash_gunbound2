import processing.core.PVector;

abstract public class AbstractPlayer {
  protected PVector position;
  protected Window window;
  protected int hp;
  protected int fuel;

  private String name;

  public abstract void move(int direction);
  public abstract void setAngle(Player currentPlayer, float degree);
  public abstract void setPower();
  public abstract void fire(Player currentPlayer, CannonBall ball,Window window);
  public abstract void collide();
  public abstract int getHp();
  public abstract void setHp(int hp);
  public abstract float getFuel();
  public abstract void setFuel(float fuel);
  public abstract void decreaseFuel(float fuel);
  public abstract void setPosition(PVector position);
  public abstract PVector getPosition();

  public abstract void draw(Window window);

  public AbstractPlayer(PVector position, Window window) {
    this.position = position;
    this.window = window;
  }

}
