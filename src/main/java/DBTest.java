import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBTest {
  public DBTest () {


    Connection connection = null;
    try {
      // below two lines are used for connectivity.
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/java_game",
              "root", "");

      Statement statement;
      statement = connection.createStatement();
      ResultSet resultSet;
      resultSet = statement.executeQuery("select * from Score");
      String userName;


      while (resultSet.next()) {
        userName = resultSet.getString("userName");
        System.out.println("userName : " + userName);
      }
      resultSet.close();
      statement.close();
      connection.close();
    } catch (Exception exception) {
      System.out.println(exception);
    }
  }

}
