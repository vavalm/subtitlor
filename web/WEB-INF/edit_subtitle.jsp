<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Editer les sous-titres</title>
</head>
<body>
<h1>
    <c:out value="${pageTitle}" default="Bonjour"/>
</h1>
<h2>
    <c:out value="Edition de : ${filename}"/>
</h2>
<form method="post" action="/home" enctype="multipart/form-data">
    <input type="submit" style="position:fixed; top: 10px; right: 10px;"/>
    <table>
        <c:forEach items="${ subtitles }" var="sub" varStatus="status">
            <tr>
                <td style="text-align:right;"><c:out value="${ sub.getText() }"/></td>
                <td>
                        <input type="text" name="line${ status.index }" id="line${ status.index }" size="35"
                               value="${sub.getTranslatedText()}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
    <input type="hidden" name="filename" value="${filename}"/>
    <input type="hidden" name="idform" value="edit"/>
</form>
</body>
</html>