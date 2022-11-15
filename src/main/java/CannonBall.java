import processing.core.PVector;

public class CannonBall {

  PVector position;
  PVector direction;
  //type creates a new class?

  Window window;
  private float xpos;
  private float ypos;
  private float speed = 1f;

  private float width = (float) 30;

  private float height = (float) 30;
  private float vx = 0;
  private float vy = 0;
  int fillColour = 255;


  public CannonBall(PVector position, PVector direction, Window window) {
    this.position = position;
    this.direction = direction;
    this.window = window;
  }


  public boolean isHitPlayer(Player player, Window window) {
    // if the cannonball hits the other opponent, then loses the opponent's hp, and ends turn
    // if not, ends the turn
    return false;
  }


  public boolean outOfBounds(Window window) {
    if ((this.position.x > window.width
            || this.position.x < 0)
            || (this.position.y > window.height
            || this.position.y < 0)) {
      return true;
    } else {
      return false;
    }
  }

  public void bounce(float b) {
    this.direction.rotate(b);
  }
  public void move(Window window) {
    //this method will be implemented from IMovable interface
    // cannonball moves toward in certain direction

    this.position = this.position.add(this.direction.mult(speed));
    if (outOfBounds(window)) {
      bounce((float) Math.PI / 4f);
    }

  }

  public void draw(Window window) {
    window.fill(this.fillColour);
    window.ellipse(this.position.x,
            this.position.y,
            this.width,
            this.height);
  }


}
