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

        thead {
            background-color: #f5eee2;
            color: #504f4d;
            font-weight: bold;
            cursor: default;
        }
    </style>
</head>
<body>
<button type="button" name="back" onclick="window.history.back()"><- Назад</button>
<h3>Пользователи в системе</h3>
<table>
    <thead>
    <tr>
        <th>id</th>
        <th>№ телефона</th>
        <th>Имя</th>
        <th>Фамилия</th>
        <th>Отчество</th>
        <th>Город</th>
        <th>Дата рождения</th>
        <th>Номер машины</th>
        <th>Дата регистрации</th>
        <th>Баланс</th>
    </tr>
    </thead>
    <tbody>
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
    </tbody>
</table>
</body>
<script>
</script>
</html>