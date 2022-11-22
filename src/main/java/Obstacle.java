import processing.core.PVector;

public class Obstacle implements ICollidable {
  protected static final int WALL = 1;
  protected static final int CIRCLE = 2;
  protected static final int RECT = 3;

  protected boolean isBreakable = false;
  protected int numberOfObstacles = 0;

  public Obstacle(int numberOfObstacles, boolean isBreakable) {
    this.numberOfObstacles = numberOfObstacles;
    this.isBreakable = isBreakable;
  }

  public void draw(int type, PVector position, double size, Window window) {
    switch (type) {
      case WALL:
        window.fill(118, 113, 113);
        window.rect(position.x, position.y, 20, -window.height / 4);
//        window.rect(100, 100, 20, 20);
        break;
      case CIRCLE:
        window.fill(100, 115, 150);
        window.ellipse(position.x, position.y, (float)size, (float)size);
        break;
      case RECT:
        window.fill(250, 50, 69);
        window.rect(position.x, position.y, (float)size, (float)size);
        break;
      default:
        break;
    }
//    window.fill(118, 113, 113);
//    window.rect(window.width / 2, window.dashboardHeight, 20, -window.height / 4);
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
