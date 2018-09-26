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
        tr:hover {
            background-color: navajowhite;
        }
    </style>
</head>
<body>
<h3>Главная</h3>
<button type="button" onclick="window.location.href='/city/get_list'">Список городов</button>
<button type="button" onclick="window.location.href='/user/get_list'">Список пользователей</button>
<button type="button" onclick="window.location.href='/history/get_list_admin'">История транзакций</button>
<h2>Текущие акции</h2>
<button type="button" onclick="">Новая акция</button>
<br>
<table>
    <tr>
        <td>id</td>
        <td>Начало акции</td>
        <td>Конец акции</td>
        <td>Сумма выигрыша</td>
        <td>Сумма участия</td>
        <td>Город</td>
        <td>Статус</td>
        <td>Кол-во участников</td>
    </tr>
    <c:forEach items="${offers}" var="offerModel">
        <tr onclick="window.location.href='/participant/participant_form?id=${offerModel.id}'">
            <td><c:out value="${offerModel.id}"/></td>
            <td><fmt:formatDate value="${offerModel.startDate}" pattern="dd.MM.yyyy HH:mm:ss"/></td>
            <td><fmt:formatDate value="${offerModel.endDate}" pattern="dd.MM.yyyy HH:mm:ss"/></td>
            <td><c:out value="${offerModel.win}"/></td>
            <td><c:out value="${offerModel.payment}"/></td>
            <td><c:out value="${offerModel.city.name}"/></td>
            <td><c:out value="${offerModel.active}"/></td>
            <td><c:out value="${offerModel.participants}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
<script>
</script>
</html>