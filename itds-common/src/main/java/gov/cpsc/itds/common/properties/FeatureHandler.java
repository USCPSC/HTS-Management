package gov.cpsc.itds.common.properties;

public class FeatureHandler {

  private FeatureHandler() {}

  public static boolean isEnabled(Enum feature) {
    return SysPropertiesHandler.getBooleanProperty(feature);
  }
}
