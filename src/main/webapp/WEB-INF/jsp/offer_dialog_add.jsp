<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.datetimepicker.css">
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.datetimepicker.full.min.js"></script>
</head>
<body>
<h3>Новая акция</h3>
<form:form method="post" action="${pageContext.request.contextPath}/offer/set" modelAttribute="offerModel">
    <fmt:formatDate value="${offerModel.startDate}" var="startDate" pattern="dd.MM.yyyy HH:mm" />
    <fmt:formatDate value="${offerModel.endDate}" var="endDate" pattern="dd.MM.yyyy HH:mm" />
    <table>
        <tr>
            <td><form:label path = "startDate">Дата начала акции</form:label></td>
            <td><form:input autocomplete="off" id="startdatetimepicker" path = "startDate" value="${startDate}"/></td>
            <td><span style="color: red;"> <form:errors path = "startDate"/></span></td>
        </tr>
        <tr>
            <td><form:label path = "endDate">Дата конца акции</form:label></td>
            <td><form:input autocomplete="off" id="enddatetimepicker" path = "endDate" value="${endDate}"/></td>
            <td><span style="color: red;"> <form:errors path = "endDate"/></span></td>
        </tr>
        <tr>
            <td><form:label path = "win">Сумма выигрыша</form:label></td>
            <td><form:input autocomplete="off" path = "win"/></td>
            <td><span style="color: red;"> <form:errors path = "win"/></span></td>
        </tr>
        <tr>
            <td><form:label path = "payment">Сумма участия в акции</form:label></td>
            <td><form:input autocomplete="off" path = "payment"/></td>
            <td><span style="color: red;"> <form:errors path = "payment"/></span></td>
        </tr>
        <tr>
            <td><form:label path = "city">Город проведения акции</form:label></td>
            <td><form:select path="city">
                <form:option value="0">Выберите город</form:option>
                <c:forEach items="${cities}" var="cityModel">
                    <form:option value="${cityModel.id}">${cityModel.name}</form:option>
                </c:forEach>
            </form:select></td>
            <td><span style="color: red;"> <form:errors path = "city"/></span></td>
        </tr>
        <tr>
            <td colspan = "2">
                <input type = "submit" value = "Создать"/>
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
<script>
    jQuery('#startdatetimepicker').datetimepicker({ format:'d.m.Y H:i', step:10});
    $.datetimepicker.setLocale('ru');
</script>
<script>
    jQuery('#enddatetimepicker').datetimepicker({ format:'d.m.Y H:i', step:10});
    $.datetimepicker.setLocale('ru');
</script>
</body>
</html>