<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="ru">
<head></head>
<body>
    <h3>Авторизация</h3>
    <form:form method="post" action="${pageContext.request.contextPath}/auth/login_form/send" modelAttribute="loginRequest">
    <table>
        <tr>
            <td><form:label path = "phoneNumber">Логин</form:label></td>
            <td><form:input path = "phoneNumber"/></td>
            <td><form:errors path = "phoneNumber"/></td>
        </tr>
        <tr>
            <td><form:label path = "password">Пароль</form:label></td>
            <td><form:input type="password" path = "password"/></td>
            <td><form:errors path = "password"/></td>
        </tr>
        <tr>
            <td colspan = "2">
                <input type = "submit" value = "Отправить"/>
            </td>
        </tr>
        <c:if test="${not empty statusText}">
            <span style="color: red;"> <c:out value="${statusText}"/>
            </span>
        </c:if>
    </table>
    </form:form>
</body>
</html>
