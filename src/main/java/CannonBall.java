
import processing.core.PVector;

import static processing.core.PApplet.println;

public class CannonBall implements IMovable, ICollidable{

//  protected PVector tempPos = new PVector(50, this.height + 500);
//  protected PVector tempDir = new PVector(1f, -1f).normalize();


  PVector position;
  PVector relativePosition;
  PVector direction;
  //type creates a new class?

  PVector acc;

  float friction = 1f;

  Window window;

  protected Player player;

  protected Obstacle obstacle;

  protected float speed = 1f;

  protected float width = (float) 20;

  protected float height = (float) 20;

  protected float radious = (float) 30 / 2;


  protected float xSpeed;

  protected float ySpeed;

  public final double g = -0.98;

  int fillColour = 255;


//  private static CannonBall singleBall;
//
//  private CannonBall(PVector tempPos, PVector tempDir, Window window) {
//    CannonBall.position = tempPos;
//    CannonBall.direction = tempDir;
//    CannonBall.window = window;
//    this.relativePosition = new PVector(0,0);
//  }
//
//
//  public static CannonBall getInstance() {
//    if (singleBall == null) {
//      singleBall = new CannonBall(position, direction, window);
//    }
//    return singleBall;
//  }


  public CannonBall(PVector position, PVector direction, Window window) {
    this.position = position;
    this.direction = direction;
    this.window = window;
    this.relativePosition = new PVector(0,0);
  }

  public void resetBall() {
    setSpeed(0);
    setXPos(0);
    setYPos(0);

  }


//  public boolean isHit (Player player, Obstacle obstacle) {
//    // if the cannonball hits the other opponent, then loses the opponent's hp, and ends turn
//    // if not, ends the turn
//
////    if ((this.relativePosition.dist(player.position) <= 0 ) {
////
////    }
//
//    if (this.position.dist(player.getPosition()) == 0) {
//      System.out.println("touches player");
//      return true;
//    }
//
//    return false;
//  }



  public boolean OutOfBoundsRight(Window window) {
    return this.relativePosition.x < 0 || this.relativePosition.x > window.width
            ||this.relativePosition.y >= 81; // difference between dashboardHeight and player.y

  }

  public boolean OutOfBoundsLeft(Window window) {
    return this.relativePosition.y >= 81; // difference between dashboardHeight and player.y

  }

  public void bounce(float b) {
    direction.rotate(b);
  }


  public void moveRight(Player currentPlayer, Window window) {
    //this method will be implemented from IMovable interface
    // cannonball moves toward in certain direction

//    if (isHitPlayer(player, window)) {
//      player.setHp(10);
//      System.out.println(player.getHp());
//    }

    if (OutOfBoundsRight(window)) {
      System.out.println("ahahaha");
      resetBall();
      return;
    }
    this.relativePosition.x = this.relativePosition.x + direction.mult(speed).x;
    this.relativePosition.y = this.relativePosition.y - direction.mult(speed).y;
    direction.y -= 0.0018f;
    System.out.println(this.relativePosition.x);
    System.out.println(this.relativePosition.y);


  }

  public void moveLeft(Player currentPlayer, Window window) {
    //this method will be implemented from IMovable interface
    // cannonball moves toward in certain direction

//    if (isHitPlayer(player, window)) {
//      player.setHp(10);
//      System.out.println(player.getHp());
//    }

    if (OutOfBoundsLeft(window)) {
      System.out.println("ahahaha");
      resetBall();
    }
    this.relativePosition.x = this.relativePosition.x - direction.mult(speed).x;
    this.relativePosition.y = this.relativePosition.y - direction.mult(speed).y;
    direction.y -= 0.0018f;


  }

  public void draw(PVector position, Window window) {
    window.fill(this.fillColour);
    window.ellipse(position.x + this.getRadius() + this.relativePosition.x,
            position.y - this.radious + this.relativePosition.y,
            this.width,
            this.height);
  }

  public PVector getDirection(){
    return direction;
  }

  public void  setDirection(PVector direction){
    this.direction.x = direction.x;
    this.direction.y = direction.y;
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

  public void setXPos(float f) {
    this.relativePosition.x = f;
  }

  public float getYPos() {return position.y;}

  public void setYPos(float f) {
    this.relativePosition.y = f;
  }

  public void setSpeed(float f) {this.speed = f;}

  @Override
  public void collideBehaviour(ICollidable c) {

  }

  @Override
  public PVector getPosition() {
    return position;
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
