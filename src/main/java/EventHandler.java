public class EventHandler implements OnEventListner {

  @Override
  public void onEvent() {
    System.out.println("Performing callback after synchronous Task");
  }
}
