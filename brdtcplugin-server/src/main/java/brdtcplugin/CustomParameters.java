package brdtcplugin;

import java.util.*;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildFeatureDescriptor;
import org.jetbrains.annotations.Nullable;

public class CustomParameters {
  @Nullable
  public static String getArtifactFilename(final SBuild build) {
    final SBuildFeatureDescriptor sBuildFeature = getBuildFeatureDescriptor(build);
    if (sBuildFeature == null) return null;
    return sBuildFeature.getParameters().get(Constants.ARTIFACT_FILENAME);
  }

  @Nullable
  private static SBuildFeatureDescriptor getBuildFeatureDescriptor(final SBuild build) {
    Collection<SBuildFeatureDescriptor> descriptors = build.getBuildFeaturesOfType(Constants.BUILD_FEATURE_TYPE);
    if (descriptors.isEmpty()) return null;
    return descriptors.iterator().next();
  }
}
