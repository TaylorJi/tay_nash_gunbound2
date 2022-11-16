import processing.core.PFont;
import processing.core.PVector;

import static java.lang.Math.atan;

public class Player extends AbstractPlayer implements ICollidable, IMovable {
  protected int fillColour = 100;
  protected int width = 30;
  protected int height = 30;

  protected int hp = 100;

  protected int fuel = 100;

  protected PVector angleDirection = new PVector(3,1).normalize();

  public Player(PVector position, Window window) {
    super(position, window);
  }

  @Override
  public void move(int direction) {
    float xPos = getPosition().x;
    float yPos = getPosition().y;
    if ((xPos + direction <= 0) || (xPos + this.width + direction >= window.width)) {
      xPos = getPosition().x;
    } else {
      xPos = getPosition().x + direction;
    }
    PVector temp = new PVector(xPos, yPos);
    this.setPosition(temp);
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
  public int getHp() {
    return this.hp;
  }

  @Override
  public void setHp(int hp) {
    this.hp -= hp;
  }

  @Override
  public int getFuel() {
    return this.fuel;
  }

  @Override
  public void decreaseFuel(int fuel) {
    this.fuel -= fuel;
    if (this.fuel <= 0) this.fuel = 0;
  }

  public void setAngleDirection(double degree) {
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

  @Override
  public void setPosition(PVector position) {
    this.position = position;
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
    return this.position;
  }

  @Override
  public PVector getVelocity() {
    return null;
  }

  @Override
  public PVector getPower() {
    return null;
  }

  @Override
  public float getWidth() {
    return this.width;
  }

  @Override
  public float getHeight() {
    return this.height;
  }

  @Override
  public void collideBehaviour(ICollidable c) {

  }
}
