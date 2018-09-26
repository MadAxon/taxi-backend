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
<h2>История всех транзакций</h2>
<table>
    <tr>
        <td>id</td>
        <td>id участника</td>
        <td>ФИО пользователя</td>
        <td>№ телефона пользователя</td>
        <td>Сумма оплаты/вывода/выигрыша</td>
        <td>Наименование провайдера/№ акции</td>
        <td>№ счета/чека</td>
        <td>Комиссия</td>
        <td>Дата проведение транзакции</td>
        <td>Статус операции</td>
    </tr>
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
            <td><c:out value="${historyModel.historyStatus}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
<script>
</script>
</html>