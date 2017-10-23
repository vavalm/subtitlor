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
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bienvenue sur subtitle-editor</title>
    <meta name="description" content="A description of your website">
    <link href="<c:url value="/css/style.css"/> " rel="stylesheet" type="text/css">
</head>
<body>

<div id="wrapper">

    <div id="header">
        <div class="top_banner">
            <h1 style="text-align: center"><c:out value="${pageTitle}"
                                                  default="Bienvenue sur la plateforme d'édition de sous-titres."/></h1>
        </div>

    </div>

    <div id="page_content">
        <p><b style="font-size: large;">Importez votre fichier pour traduire les sous-titres</b>
        <form method="post" action="<c:url value="edit"/>" enctype="multipart/form-data" acceptcharset="UTF-8">
            <input type="text" name="filmName" id="filmName" minlength="3" placeholder="Nom du film" required/>
            <input type="file" name="${subtitlesFileName}" id="${subtitlesFileName}">
            <br>
            <input type="hidden" name="idform" value="upload"/>
            <input type="submit" name="edit">
        </form>
        </p>
        <br><br>
        <p><b style="font-size: large;">Exporter/Modifiez un fichier existant</b>
        <form method="post" action="<c:url value="edit"/>" enctype="multipart/form-data" id="filmsInBdd">
            <select name="filmId" id="filmNameChoice">
                <c:forEach items="${films}" var="film" varStatus="status">
                    <option value="${film.getIdFilm()}">${film}</option>
                </c:forEach>
            </select>
            <%--<input type="submit" name="export" id="export" value="exporter">--%>
            <input type="hidden" name="idform" value="edit"/>
            <input type="submit" name="edit" id="edit" value="Modifier">
            <input type="button" name="export" id="edit" value="Exporter l'original"
                   onclick="javascript:window.location.href='/download/'+ document.getElementById('filmNameChoice').value"/>
            <input type="button" name="export" id="edit" value="Exporter la traduction"
                   onclick="javascript:window.location.href='/download/translated/'+ document.getElementById('filmNameChoice').value"/>
        </form>
        </p>
    </div>

</div>

</body>
</html>