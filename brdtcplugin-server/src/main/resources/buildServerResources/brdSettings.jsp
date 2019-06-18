<%@ include file="/include-internal.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ page import="brdtcplugin.Constants" %>
<jsp:useBean id="buildForm" type="jetbrains.buildServer.controllers.admin.projects.BuildTypeForm" scope="request"/>

<tr>
  <td colspan="2">
    <em>This build feature adds a custom HTML to the build results page.</em>
  </td>
</tr>
<tr>
<tr>
  <th>
    <label for="<%= Constants.ARTIFACT_FILENAME%>">Artifact filename:</label>
  </th>
  <td>
    <props:textProperty name="<%= Constants.ARTIFACT_FILENAME%>" className="longField textProperty_max-width js_max-width"/>
    <span class="smallNote">Filename containing the HTML to be displayed. To select a file from a zip archive use the archive.zip!filename.html syntax.</span>
  </td>
</tr>
