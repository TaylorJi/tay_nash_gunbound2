
import processing.core.PVector;

import static processing.core.PApplet.println;
import static processing.core.PApplet.radians;

public class CannonBall implements IMovable, ICollidable{

  PVector position;
  PVector relativePosition;
  PVector direction;
  //type creates a new class?

  PVector acc;

  float friction = 1f;

  Window window;
  protected float speed = 1f;

  protected float width = (float) 20;

  protected float height = (float) 20;

  protected float radious = (float) 30 / 2;

  protected Player player;

  protected float xSpeed;

  protected float ySpeed;

  public final double g = -0.98;

  int fillColour = 255;


  public CannonBall(PVector position, PVector direction, Window window) {
    this.position = position;
    this.direction = direction;
    this.window = window;
    this.relativePosition = new PVector(0,0);
  }




  public boolean isHitPlayer(Player player) {
    // if the cannonball hits the other opponent, then loses the opponent's hp, and ends turn
    // if not, ends the turn
    if (this.position.dist(player.getPosition()) == 0) {
      System.out.println("touches player");
      return true;
    }

    return false;
  }

  public float getXSpeed() {
    return this.xSpeed;
  }

  public float setXSpeed(float f) {
    return this.xSpeed = f;
  }

  public float getYSpeed() {
    return this.ySpeed;
  }

  public float setYSpeed(float y) {
    return this.ySpeed = y;
  }


  public boolean OutOfBounds(Window window) {
    player = new Player(new PVector(50,this.height - 200), window);
    return this.relativePosition.x < 0 || this.relativePosition.x > window.width
            ||this.relativePosition.y >= 99; // difference between dashboardHeight and player.y

  }


  public void removeBall (CannonBall this) {
    this.draw(getPosition(), window);
  }

  public void bounce(float b) {
    this.direction.rotate(b);
  }

  // if the ball is out of bound then remove the ball
  public void move(Player currentPlayer, Window window) {
    //this method will be implemented from IMovable interface
    // cannonball moves toward in certain direction

//    if (isHitPlayer(player, window)) {
//      player.setHp(10);
//      System.out.println(player.getHp());
//    }



    if (OutOfBounds(window)) {
      System.out.println("ahahaha");
    }
    this.relativePosition.x = this.relativePosition.x + this.direction.mult(speed).x;
    this.relativePosition.y = this.relativePosition.y - this.direction.mult(speed).y;
    this.direction.y -= 0.0018f;
    System.out.println("x:" + this.relativePosition.x);
    System.out.println("y:" + this.relativePosition.y);


  }

  public void draw(PVector position, Window window) {
    window.fill(this.fillColour);
    window.ellipse(position.x + this.getRadius() + this.relativePosition.x,
            position.y - this.radious + this.relativePosition.y,
            this.width,
            this.height);
  }

  public float getRadius() {
    return this.radious;
  }

  @Override
  public boolean collided(ICollidable c) {
    return false;
  }

  @Override
  public float getWidth() {
    return this.width;
  }

  @Override
  public float getHeight() {
    return this.height;
  }

  public float getXPos() {return this.relativePosition.x;}

  public float setXPos(float f) {return this.relativePosition.x = f;}

  public float getYPos() {return this.position.y;}

  public float setYPos(float f) {return this.relativePosition.y = f;}



  public PVector getDirection(){
    return this.direction;
  }

  public void  setDirection(PVector direction){
    this.direction.x = direction.x;
    this.direction.y = direction.y;
  }

  @Override
  public void collideBehaviour(ICollidable c) {

  }

  @Override
  public PVector getPosition() {
    return this.position;
  }

  @Override
  public void setPosition(PVector position) {
    this.position = position;
  }

  @Override
  public PVector getVelocity() {
    return null;
  }

  @Override
  public PVector getPower() {
    return null;
  }
}
