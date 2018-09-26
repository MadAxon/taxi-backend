<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            padding: 8px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }
    </style>
</head>
<body>
<button type="button" name="back" onclick="window.history.back()"><- Назад</button>
<h3>Пользователи в системе</h3>
<table>
    <tr>
        <td>id</td>
        <td>№ телефона</td>
        <td>Имя</td>
        <td>Фамилия</td>
        <td>Отчество</td>
        <td>Город</td>
        <td>Дата рождения</td>
        <td>Номер машины</td>
        <td>Дата регистрации</td>
        <td>Баланс</td>
    </tr>
    <c:forEach items="${users}" var="userModel">
        <tr>
            <td><c:out value="${userModel.id}"/></td>
            <td><c:out value="${userModel.phoneNumber}"/></td>
            <td><c:out value="${userModel.firstName}"/></td>
            <td><c:out value="${userModel.lastName}"/></td>
            <td><c:out value="${userModel.patronymic}"/></td>
            <td><c:out value="${userModel.city.name}"/></td>
            <td><fmt:formatDate value="${userModel.birthDate}" pattern="dd.MM.yyyy"/></td>
            <td><c:out value="${userModel.carNumber}"/></td>
            <td><fmt:formatDate value="${userModel.registerDate}" pattern="dd.MM.yyyy HH:mm:ss"/></td>
            <td><c:out value="${userModel.balance}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
<script>
</script>
</html>