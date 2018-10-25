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
<h2>История всех транзакций</h2>
<table>
    <thead>
    <tr>
        <th>id</th>
        <th>id участника</th>
        <th>ФИО пользователя</th>
        <th>№ телефона пользователя</th>
        <th>Сумма оплаты/вывода/выигрыша</th>
        <th>Наименование провайдера/№ акции</th>
        <th>№ счета/чека</th>
        <th>Комиссия</th>
        <th>Дата проведение транзакции</th>
        <th>Статус операции</th>
        <th>Тип операции</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${histories}" var="historyModel">
        <tr>
            <td><c:out value="${historyModel.id}"/></td>
            <td><c:out value="${historyModel.user.id}"/></td>
            <td><c:out value="${historyModel.user.firstName} ${historyModel.user.lastName} ${historyModel.user.patronymic}"/></td>
            <td><c:out value="${historyModel.user.phoneNumber}"/></td>
            <td><c:out value="${historyModel.amount}"/></td>
            <td><c:out value="${historyModel.name}"/></td>
            <td><c:out value="${historyModel.number}"/></td>
            <td><c:out value="${historyModel.commission}"/></td>
            <td><fmt:formatDate value="${historyModel.date}" pattern="dd.MM.yyyy HH:mm:ss"/></td>
            <td><c:out value="${historyModel.transactionStatus}"/></td>
            <td><c:out value="${historyModel.transactionType}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
<script>
</script>
</html>