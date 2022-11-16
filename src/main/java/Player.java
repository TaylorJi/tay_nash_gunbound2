import processing.core.PVector;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static processing.core.PApplet.println;

public class Player extends AbstractPlayer implements ICollidable, IMovable {
  protected int fillColour = 100;
  protected int width = 30;
  protected int height = 30;

  protected int hp = 100;

  protected float fuel = 100.0F;

  protected PVector angleDirection = new PVector(1,1).normalize();

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
  public void setPower() {

  }

  @Override
  public void fire(Player currentPlayer) {
    println("Fired!");
    currentPlayer.setFuel(100.0F);
    window.turn = !window.turn;
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
  public float getFuel() {
    return this.fuel;
  }

  @Override
  public void setFuel(float fuel) {
    this.fuel = fuel;
  }

  @Override
  public void decreaseFuel(float fuel) {
    this.fuel -= fuel;
    if (this.fuel <= 0) this.fuel = 0;
  }

  @Override
  public void setAngle(Player currentPlayer, float degree) {
//    Player player = new Player(currentPlayer.angleDirection, window);
//    player.angleDirection.rotate(degree);
//    if (!window.turn) {
//      if ((getAngle(abs(player.angleDirection.x), abs(player.angleDirection.y)) > 0.9)
//              || (getAngle(abs(player.angleDirection.x), abs(player.angleDirection.y)) < 0)) {
//        return;
//      }
//    }

//    println(getAngle(abs(player.angleDirection.x), abs(player.angleDirection.y)));
//    } else {
//      currentPlayer.angleDirection.x += degree;
//      currentPlayer.angleDirection.y -= degree;
//    }
//    println(this.getAngle(currentPlayer.angleDirection.x, currentPlayer.angleDirection.y));

    currentPlayer.angleDirection.rotate(degree);
//    println(getAngle(currentPlayer.angleDirection.x, currentPlayer.angleDirection.y));
  }

  double getAngle(float x, float y) {
    return atan(y/x);
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
