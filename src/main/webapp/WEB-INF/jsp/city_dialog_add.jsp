<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="ru">
<head>
</head>
<body>
<h3>Добавление города</h3>
<form:form method="post" action="${pageContext.request.contextPath}/city/set" modelAttribute="cityModel">
    <table>
        <tr>
            <td><form:label path = "name">Название города</form:label></td>
            <td><form:input autocomplete="off" path = "name"/></td>
            <td><span style="color: red;"> <form:errors path = "name"/></span></td>
        </tr>
        <tr>
            <td colspan = "2">
                <input type = "submit" value = "Добавить"/>
            </td>
        </tr>
    </table>
    <c:choose>
    <c:when test="${status == 200}">
        <span style="color: green;"> <c:out value="${statusText}"/>
                </span>
        <script>setTimeout(function() {window.close();}, 2000)</script>
    </c:when>
    </c:choose>
</form:form>
</body>
</html>