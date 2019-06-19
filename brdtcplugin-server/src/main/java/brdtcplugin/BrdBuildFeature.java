package brdtcplugin;

import java.util.Map;
import jetbrains.buildServer.serverSide.BuildFeature;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrdBuildFeature extends BuildFeature {
  private final String myEditUrl;

  public BrdBuildFeature(@NotNull final PluginDescriptor descriptor) {
    myEditUrl = descriptor.getPluginResourcesPath(BrdBuildFeatureController.CONTROLLER_URL);
  }

  @NotNull
  @Override
  public String getType() {
    return Constants.BUILD_FEATURE_TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return Constants.BUILD_FEATURE_DISPLAY_NAME;
  }

  @Nullable
  @Override
  public String getEditParametersUrl() {
    return myEditUrl;
  }

  @NotNull
  @Override
  public String describeParameters(@NotNull Map<String, String> params) {
    final String artifactFilename = params.get(Constants.ARTIFACT_FILENAME);

    StringBuilder sb = new StringBuilder();
    if (StringUtil.isNotEmpty(artifactFilename)) {
      sb.append("Artifact filename: ").append(artifactFilename).append("\n");
    }
    return sb.toString().trim();
  }

  @Override
  public boolean isMultipleFeaturesPerBuildTypeAllowed() {
    return true;
  }

  @Override
  public boolean isRequiresAgent() { return false; }
}
