package brdtcplugin;

import jetbrains.buildServer.controllers.BuildDataExtensionUtil;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifactsViewMode;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class BrdBuildPageExtension extends SimplePageExtension {

    private SBuildServer buildServer;

    public BrdBuildPageExtension(PagePlaces pagePlaces, PluginDescriptor pluginDescriptor, SBuildServer buildServer) {
        super(pagePlaces, PlaceId.BUILD_RESULTS_FRAGMENT, "brdtcpluginBuild", pluginDescriptor.getPluginResourcesPath("Brd.jsp"));
        register();
        this.buildServer = buildServer;
    }

    @Override
    public boolean isAvailable(@NotNull HttpServletRequest request) {
        SBuild build = getBuild(request);
        return build != null && build.isFinished();
    }

    @Override
    public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {

        SBuild build = getBuild(request);

        final BuildArtifact artifact = build.getArtifacts(BuildArtifactsViewMode.VIEW_DEFAULT).getArtifact("buildoverview.html");
        if (artifact != null) {
            try {
                model.put("message", readData(artifact));
            } catch (IOException e) {
                model.put("brd_IOException", e);
            }
        }
    }

    @NotNull
    private String readData(BuildArtifact artifact) throws IOException {
        String data;InputStream inputStream = null;
        try {
            inputStream = artifact.getInputStream();
            data = readDataFromStream(inputStream);
        } finally {
            inputStream.close();
        }
        return data;
    }

    @NotNull
    protected String readDataFromStream(@NotNull final InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_16LE));
        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private SBuild getBuild(HttpServletRequest request) {
        return BuildDataExtensionUtil.retrieveBuild(request, buildServer);
    }
}
