<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: vavalm
  Date: 15/08/2017
  Time: 11:16
  To change this template use File | Settings | File Templates.
--%>
<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Accueil</title>
</head>
<body>
<h1>
    <c:out value="${pageTitle}" default="Bienvenue sur la plateforme d'édition de sous-titres." />
</h1>
<p> Importer votre fichier pour traduire les sous-titres
<form method="post" action="/edit" enctype="multipart/form-data">
    <label for="${subtitlesFileName}">Fichier de sous-titres : </label>
    <input type="file" name="${subtitlesFileName}" id="${subtitlesFileName}">
    <input type="hidden" name="idform" value="upload"/>
    <input type="submit" name="edit">
</form>
</p>
<br><br>
<p> Vous pouvez exporter ou modifier un fichier de sous-titres déjà présent en base de données
<form method="post" action="/edit" enctype="multipart/form-data">
    <label>Choisissez un fichier de sous-titres en base de données : </label>
    <select name="fileInBdd">
        <c:forEach items="${subFiles}" var="subFile" varStatus="status">
            <option value="${subFile}">${subFile}</option>
        </c:forEach>
    </select>
    <%--<input type="submit" name="export" id="export" value="exporter">--%>
    <input type="hidden" name="idform" value="edit"/>
    <input type="submit" name="edit" id="edit" value="Modifier">
</form>
</p>

<p>
    ${path}
</p>
</body>
</html>