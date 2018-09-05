package gov.cpsc.itds.common.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.cpsc.itds.common.ITDSTimeUtils;
import gov.cpsc.itds.common.properties.PropertiesHandler;

public class ITDSConnectionPool {

  private static BasicDataSource dataSource;
  private static final Logger logger = LoggerFactory.getLogger(ITDSConnectionPool.class);

  private ITDSConnectionPool() {
    throw new IllegalStateException();
  }

  public static Connection getConnection() throws SQLException {
    dataSource = getDataSource();
    return dataSource.getConnection();
  }

  public static BasicDataSource getDataSource() {
    logger.debug("dataSource={}", dataSource);
    if (dataSource == null) {
      BasicDataSource ds = new BasicDataSource();
      try {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      } catch (ClassNotFoundException e) {

        logger.error("Error creating DriverClass {}", e.getMessage());
      }
      ds.setUrl(PropertiesHandler.getProperty("jdbcConnection"));
      ds.setUsername(PropertiesHandler.getProperty("dbUser"));
      ds.setPassword(PropertiesHandler.getProperty("dbPassword"));
      ds.setMinIdle(5);
      ds.setMaxIdle(10);
      ds.setMaxOpenPreparedStatements(100);
      ds.setValidationQuery("select 1");

      dataSource = ds;
      logger.debug("dataSource={}", dataSource);
    }
    return dataSource;
  }

  public static boolean isDatabaseUp() {
    // This assumes there is a range during the day where the database is down span doesn't span
    // midnight
    boolean result = false;
    result =
        !(ITDSTimeUtils.isNowBetween(
            PropertiesHandler.getLocalTimeProperty("database_down_time"),
            PropertiesHandler.getLocalTimeProperty("database_up_time")));
    return result;
  }
}
