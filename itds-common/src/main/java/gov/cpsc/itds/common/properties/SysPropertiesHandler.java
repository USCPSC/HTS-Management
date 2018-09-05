package gov.cpsc.itds.common.properties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author jglickman */
public class SysPropertiesHandler {

  private static final Logger logger = LoggerFactory.getLogger(SysPropertiesHandler.class);
  private static CacheManager cm = CacheManager.getInstance();
  private static Cache cache = null;
  private static final String CACHE_NAME = "sysprops";
  static {
    cache = cm.getCache(CACHE_NAME);
  }

  public static String getProperty(Enum key) {
    if (key == null) {
      return null;
    }
    String result=null;

    Element element = cache.get(key);
    if (element == null) {
      logger.debug("{} not found in sysprops cache", key);
      String sql = "select value from itds_exam.sys_property where name='" + key + "'";
      logger.debug(sql);
      try (Connection connection = gov.cpsc.itds.common.connectionpool.ITDSConnectionPool.getConnection();
          PreparedStatement statement = connection.prepareStatement(sql);
          ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          result = resultSet.getString("value");
        }
        element = new Element(key, result);
        cache.put(element);
      } catch (SQLException e) {
        logger.error("defaulting to false", e);
      }
    } else {
      logger.debug("found in cache");
      result = (String) element.getObjectValue();
    }
    logger.debug("feature {}={}", key, result);
    return result;
  }

  public static boolean getBooleanProperty(Enum key) {
    boolean result=Boolean.parseBoolean(getProperty(key));
    return result;
  }

  public static int getIntProperty(Enum key) {
    int result=Integer.parseInt(getProperty(key));
    return result;
  }

/*
//Are we still supporting this?
  public boolean saveSysProperty(Enum key, String value) {

  }
*/

}
