import java.awt.Color;
import java.util.ArrayList;
import processing.core.PVector;

/**
 * IMoveable is anything that is movable onscreen.
 *
 * @author Nash Baek
 * @version 1.0
 */
public interface IMovable {
  PVector getPosition();
  PVector getVelocity();
  PVector getRadius();
  PVector getPower();
}
