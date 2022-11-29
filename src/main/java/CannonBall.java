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

  protected float speed = 1f;

  protected float width = (float) 20;

  protected float height = (float) 20;

  protected float radious = (float) 20 / 2;


  protected float xSpeed;

  protected float ySpeed;

  public final double g = -0.98;

  boolean collided = false;

  int fillColour = 255;


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

  public void didHitObstacle() {
    for (ICollidable each : window.collidables) {
      if (collided(each)) {
        collideBehaviour(each);
      }
    }
  }

  public void ballHoldPos() {
    if ((window.currentPlayer.getPosition().x + window.currentPlayer.width <= window.wallPosition.x)
            ||window.currentPlayer.getPosition().x - window.currentPlayer.width >= window.wallPosition.x) {
      window.ballMove = false;
    }
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



  public boolean OutOfBounds(Window window) {
    return this.relativePosition.y >= 81; // difference between dashboardHeight and player.y

  }

  public void bounce(float b) {
    direction.rotate(b);
  }


  public void move(Player currentPlayer, Window window) {
    if (currentPlayer == window.leftPlayer) {
      ballOutOfBound(window);
      this.relativePosition.x = this.relativePosition.x + 2* direction.mult(speed).x;
    } else {
      ballOutOfBound(window);
      this.relativePosition.x = this.relativePosition.x - 2 * direction.mult(speed).x;
    }
    this.relativePosition.y = this.relativePosition.y - direction.mult(speed).y;
    direction.y -= 0.0018f;

    didHitObstacle();
  }

  private void ballOutOfBound(Window window) {
    if(OutOfBounds(window)) {
      resetBall();
      window.currentPlayer.changeTurn(window.currentPlayer, window);
      if (!window.turn) {
        window.currentPlayer = window.leftPlayer;
      } else {
        window.currentPlayer = window.rightPlayer;
      }
      this.position = window.currentPlayer.position;
    }

  }

  public void draw(PVector position, Window window) {
    window.fill(this.fillColour);
    window.ellipse(position.x + this.getRadius() + this.relativePosition.x,
            position.y - this.getRadius() + this.relativePosition.y,
            this.width,
            this.height);
  }



  public float getRadius() {
    return this.radious;
  }

  @Override
  public boolean collided(ICollidable c) {
    float xPos = this.position.x + this.getRadius() + this.relativePosition.x;
    float yPos = this.position.y - this.getRadius() + this.relativePosition.y;

    PVector cannonBallCenter = new PVector(
            xPos,
            yPos
    );
    // obstacle collision
    if (c instanceof Obstacle) {
      Obstacle obs = (Obstacle) c;
      PVector obstacleCenter = null;
      if (obs.getSize() == 10) {
        obstacleCenter = new PVector(
                obs.getPosition().x + obs.getSize()/2,
                obs.getPosition().y + obs.getSize()/2
        );
      } else {
        obstacleCenter = new PVector(
                obs.getPosition().x,
                obs.getPosition().y
        );
      }

      if (cannonBallCenter.dist(obstacleCenter) <= this.getRadius() + obs.getSize()/2) {
        return true;
      }
    }

    // player collision
    if (c instanceof Player) {
      Player ply = (Player) c;
      float edge = (float) Math.sqrt(Math.pow(ply.getHeight()/2, 2) + Math.pow(ply.getWidth()/2, 2));
      PVector playerCenter = new PVector(
              ply.getPosition().x + edge,
              ply.getPosition().y + edge
      );

      if (cannonBallCenter.dist(playerCenter) <= this.getRadius() + edge) {
        return true;
      }
    }

    // wall collision
    if (yPos >= window.height - 130 - (float) window.height / 4) {
      if ((float) window.width / 2 - this.getRadius()/2 <= xPos &&
              (float) window.width / 2 + this.getRadius()/2 >= xPos) {
        System.out.println("wall");
        return true;
      }
    }

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

  public PVector getDirection(){
    return this.direction;
  }

  public void  setDirection(PVector direction){
    this.direction.x = direction.x;
    this.direction.y = direction.y;
  }

  @Override
  public void collideBehaviour(ICollidable c) {
    System.out.println("collided");

    if (c instanceof Obstacle) {
      System.out.println("obstacle");
      window.currentPlayer.changeTurn(window.currentPlayer, window);
      if (!window.turn) {
        window.currentPlayer.setScore(5);
        window.currentPlayer = window.leftPlayer;
      } else {
        window.currentPlayer.setScore(5);
        window.currentPlayer = window.rightPlayer;;
      }
      this.position = window.currentPlayer.position;
    }
    if (c instanceof Player) {
      System.out.println("player");
      if (!window.turn) {
        window.currentPlayer.setScore(50);
        window.rightPlayer.setHp(10);
      } else {
        window.currentPlayer.setScore(50);
        window.leftPlayer.setHp(10);
      }
      window.currentPlayer.changeTurn(window.currentPlayer, window);
      if (!window.turn) {
        window.currentPlayer = window.leftPlayer;
      } else {
        window.currentPlayer = window.rightPlayer;
      }
      this.position = window.currentPlayer.position;
    }

    resetBall();
  }

  public void setRelativeXPos (float f) {this.relativePosition.x = f;}
  public void setRelativeYPos (float f) {this.relativePosition.y = f;}


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
