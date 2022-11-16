import processing.core.PVector;

public class Obstacle implements ICollidable {
  protected boolean isBreakable = false;
  protected int numberOfObstacles = 0;

  public Obstacle(int numberOfObstacles, boolean isBreakable) {
    this.numberOfObstacles = numberOfObstacles;
    this.isBreakable = isBreakable;
  }

  public void draw(Window window) {
    window.fill(118, 113, 113);
    window.rect(window.width / 2, window.dashboardHeight, 20, -window.height / 4);
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
