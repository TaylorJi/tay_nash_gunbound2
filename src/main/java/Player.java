import processing.core.PVector;
public class Player extends AbstractPlayer {
  protected int fillColour = 100;
  protected float width = 30.0F;
  protected float height = 30.0F;

  public Player(PVector position, Window window) {
    super(position, window);
  }

  @Override
  public void move() {

  }

  @Override
  public void setAngle() {

  }

  @Override
  public void setPower() {

  }

  @Override
  public void fire() {

  }

  @Override
  public void collide() {

  }

  @Override
  public void draw(Window window) {
    window.fill(this.fillColour);
    window.rect(this.position.x, this.position.y, this.width, this.height);

  }
}
