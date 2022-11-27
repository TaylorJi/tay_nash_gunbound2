
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

  protected float radious = (float) 20 / 2;


  protected float xSpeed;

  protected float ySpeed;

  public final double g = -0.98;

  int fillColour = 255;

  PVector ballTopRight;
  PVector ballTopLeft;
  PVector ballBottomRight;
  PVector ballBottomLeft;


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
    window.ballMove = false;
    setRelativeXPos(0);
    setRelativeYPos(0);

  }


 public void didHitObstacle () {
    for (int i = 0; i < window.obstacles.size(); i++) {
      this.obstacle = window.obstacles.get(i);
      if (collided(this.obstacle)) {
        collideBehaviour(this.obstacle);
      }
    }
 }

 public void isHitPlayer (Player p) {
    if (this.relativePosition.dist(p.position) <= 0) {
      System.out.println("player is hit");
    }
 }


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


  public void move(Player currentPlayer, Window window) {
    System.out.println(this.relativePosition.x);
    System.out.println(this.relativePosition.y);


    currentPlayer = window.currentPlayer;
    if (currentPlayer == window.leftPlayer) {
      if(OutOfBoundsRight(window)) {
        System.out.println("ahahaha");
        resetBall();
      }
      this.relativePosition.x = this.relativePosition.x + direction.mult(speed).x;
    } else {
      if(OutOfBoundsLeft(window)) {
        System.out.println("ahahaha");
        resetBall();
      }
      this.relativePosition.x = this.relativePosition.x - direction.mult(speed).x;
    }
    this.relativePosition.y = this.relativePosition.y - direction.mult(speed).y;
    direction.y -= 0.0018f;

    didHitObstacle();


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
    PVector ballCenter = this.relativePosition.add(this.radious, this.radious);
    for (Obstacle each : window.obstacles) {
//      each.setObsTopRight(new PVector(each.getPosition().x + each.size / 2f, each.getPosition().y - each.size / 2f));
//      each.setObsTopLeft(new PVector(each.getPosition().x - each.size / 2f, each.getPosition().y - each.size / 2f));
//      each.setObsBottomLeft(new PVector(each.getPosition().x - each.size / 2f, each.getPosition().y + each.size / 2f));
//      each.setObsBottomRight(new PVector(each.getPosition().x - each.size / 2f, each.getPosition().y + each.size / 2f));


//      ballTopRight = new PVector(this.relativePosition.x + this.radious, this.relativePosition.y - this.radious);
//      ballTopLeft = new PVector(this.relativePosition.x - this.radious, this.relativePosition.y - this.radious);
//      ballBottomRight = new PVector(this.relativePosition.x + this.radious, this.relativePosition.y + this.radious);
//      ballBottomLeft = new PVector(this.relativePosition.x - this.radious, this.relativePosition.y + this.radious);
//
//      float topLeftDist = ballTopLeft.x - each.getObsBottomLeft().x;
//      float topRightDist = ballTopRight.x - each.getObsTopRight().x;
//      float bottom
//
//      return topLeftDist <=
//
//
//      boolean collided = !(
//              this.getBottom() < b.getTop()
//                      || this.getTop() > b.getBottom()
//                      || this.getRight() < b.getLeft()
//                      || this.getLeft() > b.getRight()
//      );


      PVector obsCenter = each.getPosition().add(each.size/2, each.size/2);
      float dist = ballCenter.dist(obsCenter);
      if (dist <= this.radious + each.size/2) {
        return true;
      }
    }
    return false;
  }

//  public float getBallTopLeft() {
//
//  }

  @Override
  public float getWidth() {
    return this.width;
  }

  @Override
  public float getHeight() {
    return this.height;
  }

  public float getXPos() {return this.relativePosition.x;}

  public void setRelativeXPos(float f) {
    this.relativePosition.x = f;
  }

  public float getYPos() {return position.y;}

  public void setRelativeYPos(float f) {
    this.relativePosition.y = f;
  }

  public void setSpeed(float f) {this.speed = f;}

  @Override
  public void collideBehaviour(ICollidable c) {
    System.out.println("collied");
    resetBall();
//    window.currentPlayer.changeTurn(window.currentPlayer, window);
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
