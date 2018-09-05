package gov.cpsc.itds.common.connectionpool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Unit test for simple App. */
public class ConnectionPoolTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public ConnectionPoolTest(String testName) {
    super(testName);
  }

  /** @return the suite of tests being tested */
  public static Test suite() {
    return new TestSuite(ConnectionPoolTest.class);
  }

  /** Happy Case */
  public void testGetConnection() throws SQLException {
    Connection connection = ITDSConnectionPool.getConnection();
    Statement statement = connection.createStatement();
    String sql =
        "EXAMPLE";
    ResultSet resultSet = statement.executeQuery(sql);
    while (resultSet.next()) {
      long unscored = resultSet.getInt("unscored");
      System.out.println("unscored=" + unscored);
    }
    assertTrue(connection != null);
  }

  public void testIsDatabaseUp() {
    // This assumes the test is run during the time when the database is up
    // if somebody does a build between ... the build should fail
    assertTrue(ITDSConnectionPool.isDatabaseUp());
  }
}
