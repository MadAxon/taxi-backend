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
<h3>Участники в акции</h3>
<h5 style="color:slategray">Акция № ${offerModel.id} с ${offerModel.startDate} по ${offerModel.endDate}</h5>
<table>
    <tr>
        <td>id</td>
        <td>Дата вступления в акцию</td>
        <td>id участника</td>
        <td>ФИО участника</td>
        <td>№ телефона участника</td>
        <td>Статус выигрыша</td>
    </tr>
    <c:forEach items="${participants}" var="participantModel">
        <tr>
            <td><c:out value="${participantModel.id}"/></td>
            <td><fmt:formatDate value="${participantModel.date}" pattern="dd.MM.yyyy HH:mm:ss"/></td>
            <td><c:out value="${participantModel.user.id}"/></td>
            <td><c:out value="${participantModel.user.lastName}
             ${participantModel.user.firstName}
             ${participantModel.user.patronymic}"/></td>
            <td><c:out value="${participantModel.user.phoneNumber}"/></td>
            <td><c:out value="${participantModel.winner}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
<script>
</script>
</html>