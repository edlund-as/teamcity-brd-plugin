package brdtcplugin;

import com.intellij.openapi.util.Pair;
import jetbrains.buildServer.controllers.BuildDataExtensionUtil;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildFeatureDescriptor;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    public void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request) {
        ArrayList<String> urllist = new ArrayList<>();
        SBuild build = getBuild(request);
        for (SBuildFeatureDescriptor bf: build.getBuildFeaturesOfType(Constants.BUILD_FEATURE_TYPE)) {
            String artifactFilename = bf.getParameters().get(Constants.ARTIFACT_FILENAME);
            if (artifactFilename != null) {
                urllist.add(getArtifactUrl(build, artifactFilename));
            }
        }

        model.put("urllist", urllist);
        model.put("baseurl", buildServer.getRootUrl());
    }

    private String getArtifactUrl(SBuild build, String artifactFilename) {
        if (isZipArtifact(artifactFilename)) {
            Pair<String, String> pair = getRelativeAndInsideZipPaths(artifactFilename);
            final BuildArtifact artifact = build.getArtifacts(BuildArtifactsViewMode.VIEW_DEFAULT).getArtifact(pair.first);
            if (artifact == null) { return ""; }
            return String.format("%s/%s:id/%s", getProjectExternalId(build), build.getBuildId(), artifactFilename.replace("!", "!/"));
        } else {
            final BuildArtifact artifact = build.getArtifacts(BuildArtifactsViewMode.VIEW_DEFAULT).getArtifact(artifactFilename);
            if (artifact == null) { return ""; }
            return String.format("%s/%s:id/%s", getProjectExternalId(build), build.getBuildId(), artifactFilename);
        }
    }

    private String getProjectExternalId(SBuild build) {
        SBuildType buildType = build.getBuildType();
        if (buildType == null) {
            return "";
        } else {
            return buildType.getExternalId();
        }
    }

    private boolean isZipArtifact(String artifactFilename) {
        return artifactFilename.contains("!");
    }

    // Returns a pair where first element is name of zip archive and second element is name of file within the archive
    // The separator character is "!"
    private Pair<String, String> getRelativeAndInsideZipPaths(String artifactFilename) {
        int sep = artifactFilename.indexOf("!");
        String archiveFilename = artifactFilename.substring(0, sep);
        String filenameInArchive = artifactFilename.substring(sep + 1);
        return new Pair<>(archiveFilename, filenameInArchive);
    }

    private String readData(BuildArtifact artifact) {
        String data;
        InputStream inputStream;

        try {
            inputStream = artifact.getInputStream();
            data = readDataFromStream(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
        } catch (IOException e) {
            return "Error reading artifact data: " + e.getMessage();
        }
        return data;
    }

    private String readZipData(BuildArtifact artifact, String filename) {
        String data = "";
        ZipInputStream zipInputStream;

        try {
            zipInputStream = new ZipInputStream(artifact.getInputStream());
            InputStream inputStream = ArchiveUtil.extractEntry(zipInputStream, filename);
            if (inputStream != null ) {
                data = readDataFromStream(inputStream, StandardCharsets.UTF_8);
            }
            zipInputStream.close();
        } catch (IOException e) {
            return "Error reading artifact data from zip file: " + e.getMessage();
        }
        return data;
    }

    private String readDataFromStream(@NotNull final InputStream inputStream, Charset charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
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
