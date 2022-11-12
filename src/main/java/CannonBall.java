import processing.core.PVector;

public class CannonBall {

  PVector position;
  PVector direction;
  //type creates a new class?

  Window window;
  private float xpos;
  private float ypos;

  private float width = (float) 30;

  private float height = (float) 30;
  private float vx = 0;
  private float vy = 0;
  int fillColour = 255;


  public CannonBall(float xin, float yin, Window window) {
    this.xpos = xin;
    this.ypos = yin;
    this.window = window;

  }


  public boolean isHit() {
    // if the cannonball hits, obstacle the move to the next turn
    // if the cannonball hits the other opponent, then loses the opponent's hp
    return false;
  }

  public void move() {
    //this method will be implemented from IMovable interface
    // cannonball moves toward in certain direction
  }

  public void draw(Window window) {
    window.fill(this.fillColour);
    window.ellipse(this.xpos,
            this.ypos,
            this.width,
            this.height);
  }


}
