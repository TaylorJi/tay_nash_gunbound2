import processing.core.PVector;
public interface ICollidable {
  boolean collided(ICollidable c);
  PVector getPosition();
  float getWidth();
  float getHeight();
  void collideBehaviour(ICollidable c);
}
