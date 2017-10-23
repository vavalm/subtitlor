<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editer les sous-titres</title>
    <meta name="description" content="A description of your website">
    <link href="<c:url value="/css/style.css"/> " rel="stylesheet" type="text/css">
</head>
<body>
<h1 style="text-align: center">
    <c:out value="${pageTitle}" default="Bonjour"/>
</h1>
<h2>
    <c:out value="Edition de : ${subtitleFile.getName()}"/>
</h2>
<form method="post" action="/home" enctype="multipart/form-data">
    <input type="submit" style="position:fixed; top: 10px; right: 10px;"/>
    <table>
        <c:forEach items="${ subtitleFile.getSubtitles() }" var="sub" varStatus="status">
            <tr>
                <td style="text-align:right;"><c:out value="${ sub.getText() }"/></td>
                <td style="text-align:right;"><c:out value="${ sub.getStartTime() } --> ${ sub.getEndTime() }"/></td>
                <td>
                    <input type="text" name="line${ status.index }" id="line${ status.index }" size="35"
                           value="${sub.getTranslatedText()}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
    <c:set var="subtitleFile" value="${subtitleFile}" scope="request"/>
    <input type="hidden" name="filmId" value="${subtitleFile.getIdFilm()}"/>
    <input type="hidden" name="idform" value="edit"/>
</form>
</body>
</html>