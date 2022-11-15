import processing.core.PFont;
import processing.core.PVector;
public class Player extends AbstractPlayer implements ICollidable, IMovable {
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

  @Override
  public boolean collided(ICollidable c) {
    return false;
  }

  @Override
  public PVector getPosition() {
    return null;
  }

  @Override
  public PVector getVelocity() {
    return null;
  }

  @Override
  public PVector getRadius() {
    return null;
  }

  @Override
  public PVector getPower() {
    return null;
  }

  @Override
  public float getWidth() {
    return 0;
  }

  @Override
  public float getHeight() {
    return 0;
  }

  @Override
  public void collideBehaviour(ICollidable c) {

  }
}
