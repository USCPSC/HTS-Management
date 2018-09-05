package gov.cpsc.itds.common.properties;

import gov.cpsc.itds.common.cli.CommandLineHandler;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class FeatureHandlerTest {

  public FeatureHandlerTest() {
    String[] args = {"-p", "EXAMPLE"};
    try {
      new CommandLineHandler(args);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void isEnabledTest() {
    boolean enabled = FeatureHandler.isEnabled(Feature.TEST_ENABLED);
    assert (enabled);
  }

  @Test
  public void isDisbaledTest() {
    boolean enabled = FeatureHandler.isEnabled(Feature.TEST_DISABLED);
    assert (!enabled);
  }

  @Test
  public void isNullTest() {
    boolean enabled = FeatureHandler.isEnabled(null);
    assert (!enabled);
  }

  @Test
  public void cacheTest() {
    boolean enabled = FeatureHandler.isEnabled(Feature.TEST_DISABLED);
    enabled = FeatureHandler.isEnabled(Feature.TEST_DISABLED);
    try {
      Thread.sleep(35000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    enabled = FeatureHandler.isEnabled(Feature.TEST_DISABLED);
  }
}
