package brdtcplugin;

import com.intellij.openapi.diagnostic.Logger ;
import jetbrains.buildServer.ArtifactsConstants;

public class Constants {
  // Plugin's ids
  public static final String BUILD_FEATURE_TYPE = "BuildResultDecorator";
  public static final String BUILD_FEATURE_DISPLAY_NAME = "Build Result Decorator";

  // Build feature parameters
  public static final String ARTIFACT_FILENAME = "artifactFilename";

  public static final Logger LOGGER = Logger.getInstance("BuildResultDecorator");
}
