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
        tbody tr:hover {
            background-color: navajowhite;
        }
        table.sortable thead {
            background-color: #f5eee2;
            color: #504f4d;
            font-weight: bold;
            cursor: default;
        }
    </style>
    <script src="${pageContext.request.contextPath}/js/sorttable.js"></script>
</head>
<body>
<h3>Главная</h3>
<button type="button" onclick="window.location.href='${pageContext.request.contextPath}/city/city_form'">Список городов</button>
<button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/get_list'">Список пользователей</button>
<button type="button" onclick="window.location.href='${pageContext.request.contextPath}/history/history_form'">История транзакций</button>
<h2>Текущие акции</h2>
<form:form method="get" action="${pageContext.request.contextPath}/offer/main_form" modelAttribute="offerRequest">
    <form:radiobutton id="all" path="active" onclick="this.form.submit()"/>
    <label for="all">Все</label>
    <form:radiobutton id="working" path="active" value="true" onclick="this.form.submit()"/>
    <label for="working">Активные</label>
    <form:radiobutton id="finished" path="active" value="false" onclick="this.form.submit()"/>
    <label for="finished">Оконченные</label>
    <br>
    <p>Город: </p>
    <form:select path="cityId" onchange="this.form.submit()">
        <form:option value="0">Все</form:option>
        <c:forEach items="${cities}" var="cityModel">
            <form:option value="${cityModel.id}">${cityModel.name}</form:option>
        </c:forEach>
    </form:select>
</form:form>
<%--<form method="get" action="${pageContext.request.contextPath}/offer/main_form">
<input type="radio" id="all" name="active"><label for="all">Все</label>
<input type="radio" id="active" name="active" value="true"><label for="active">Активные</label>
<input type="radio" id="finished" name="active" value="false"><label for="finished">Оконченные</label>
<input type="submit" value="Sunmiy">
</form>--%>
<br>
<button type="button" onclick="window.open('${pageContext.request.contextPath}/offer/offer_dialog',null,'height=500,width=500,status=yes,toolbar=no,menubar=no,location=no');">Новая акция</button>
<br>
<table class="sortable">
    <thead>
    <tr>
        <th>id</th>
        <th>Начало акции</th>
        <th>Конец акции</th>
        <th>Сумма выигрыша</th>
        <th>Сумма участия</th>
        <th>Город</th>
        <th>Статус</th>
        <th>Кол-во участников</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${offers}" var="offerModel">
        <fmt:formatDate value="${offerModel.startDate}" var="startDateSort" pattern="yyyyMMddHHmmss" />
        <fmt:formatDate value="${offerModel.endDate}" var="endDateSort" pattern="yyyyMMddHHmmss" />
        <tr onclick="window.location.href='${pageContext.request.contextPath}/participant/participant_form?id=${offerModel.id}'">
            <td><c:out value="${offerModel.id}"/></td>
            <td sorttable_customkey="${startDateSort}"><fmt:formatDate value="${offerModel.startDate}" pattern="dd.MM.yyyy HH:mm:ss"/></td>
            <td sorttable_customkey="${endDateSort}"><fmt:formatDate value="${offerModel.endDate}" pattern="dd.MM.yyyy HH:mm:ss"/></td>
            <td><c:out value="${offerModel.win}"/></td>
            <td><c:out value="${offerModel.payment}"/></td>
            <td><c:out value="${offerModel.city.name}"/></td>
            <td><c:choose>
                <c:when test="${offerModel.active}">
                <span style="color: green;">Активна</span></c:when>
            <c:otherwise>
                <span style="color: red;">Закончена</span>
            </c:otherwise>
            </c:choose>
            </td>
            <td><c:out value="${offerModel.participants}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
<script>
</script>
</html>