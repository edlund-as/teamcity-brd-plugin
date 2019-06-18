package brdtcplugin;

import jetbrains.buildServer.controllers.BuildDataExtensionUtil;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifactsViewMode;
import jetbrains.buildServer.util.ArchiveUtil;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PlaceId;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.SimplePageExtension;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipInputStream;


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
    @NotNull
    public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {

        boolean isOnBuildoverviewTab = model.get("cameFromUrl").toString().endsWith("buildResultsDiv");
        if (!isOnBuildoverviewTab) { return; }

        SBuild build = getBuild(request);

        ZipInputStream zipInputStream;
        final BuildArtifact artifact = build.getArtifacts(BuildArtifactsViewMode.VIEW_DEFAULT).getArtifact("test.zip");
        try {
            zipInputStream = new ZipInputStream(artifact.getInputStream());
            model.put("message", readDataFromStream(ArchiveUtil.extractEntry(zipInputStream, "test.html")));
            zipInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        final BuildArtifact artifact = build.getArtifacts(BuildArtifactsViewMode.VIEW_DEFAULT).getArtifact("test.html");
//        if (artifact != null) {
//            try {
//                model.put("message", readData(artifact));
//            } catch (IOException e) {
//                model.put("brd_IOException", e);
//            }
//        }
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
