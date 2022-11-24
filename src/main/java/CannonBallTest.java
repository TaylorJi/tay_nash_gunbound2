import processing.core.PVector;

import static org.junit.jupiter.api.Assertions.*;

class CannonBallTest {
  CannonBall ball;
  Player leftPlayer;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    Window window = new Window();
    PVector tempPos = new PVector(50, window.height + 500);
    PVector tempDir = new PVector(1f, -1f).normalize();
    ball = new CannonBall(tempPos, tempDir,window);
    leftPlayer = new Player(new PVector(50,window.height - 200), window);
  }


  @org.junit.jupiter.api.Test
  void move() {

  }

  @org.junit.jupiter.api.Test
  void getWidth() {
    float testWidth = 20f;
    assertEquals(testWidth, ball.getWidth());
  }

  @org.junit.jupiter.api.Test
  void getHeight() {
    float testHeight = 30f;
    assertEquals(testHeight, ball.getHeight());
  }

  @org.junit.jupiter.api.Test
  void getDirection() {
  }

  @org.junit.jupiter.api.Test
  void setDirection() {
    PVector updatedDir = new PVector(2,2);
    ball.setDirection(updatedDir);
    assertEquals(updatedDir.x, ball.getDirection().x);
    assertEquals(updatedDir.y, ball.getDirection().y);
  }

  @org.junit.jupiter.api.Test
  void getPosition() {

  }

  @org.junit.jupiter.api.Test
  void setPosition() {

  }

  @org.junit.jupiter.api.Test
  void getVelocity() {
  }

  @org.junit.jupiter.api.Test
  void getPower() {
  }
}