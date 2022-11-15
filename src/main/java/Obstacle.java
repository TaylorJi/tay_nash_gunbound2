import processing.core.PVector;

public class Obstacle implements ICollidable {
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
