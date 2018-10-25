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
<h3>Участники в акции</h3>
<h5 style="color:slategray">
    Акция № ${offerModel.id} с <fmt:formatDate value="${offerModel.startDate}" pattern="dd.MM.yyyy HH:mm"/>
     по <fmt:formatDate value="${offerModel.endDate}" pattern="dd.MM.yyyy HH:mm"/></h5>
<table>
    <thead>
    <tr>
        <th>id</th>
        <th>Дата вступления в акцию</th>
        <th>id участника</th>
        <th>ФИО участника</th>
        <th>№ телефона участника</th>
        <th>Статус участника</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${participants}" var="participantModel">
        <tr>
            <td><c:out value="${participantModel.id}"/></td>
            <td><fmt:formatDate value="${participantModel.date}" pattern="dd.MM.yyyy HH:mm:ss"/></td>
            <td><c:out value="${participantModel.user.id}"/></td>
            <td><c:out value="${participantModel.user.lastName}
             ${participantModel.user.firstName}
             ${participantModel.user.patronymic}"/></td>
            <td><c:out value="${participantModel.user.phoneNumber}"/></td>
            <td><c:choose>
                <c:when test="${participantModel.winner}">
                    <span style="color: green;">Победитель</span></c:when>
                <c:otherwise>
                    <span style="color: red;">Проигравший</span>
                </c:otherwise>
            </c:choose></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
<script>
</script>
</html>