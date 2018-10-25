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
        }

        th, td {
            padding: 8px 16px;
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
<h3>Текущие города</h3>

<button onclick="window.open('city_dialog',null,'height=200,width=500,status=yes,toolbar=no,menubar=no,location=no');">Добавить город</button>
<table>
    <thead>
    <tr>
        <th>id</th>
        <th>Название</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${cities}" var="cityModel">
        <tr>
            <td><c:out value="${cityModel.id}"/></td>
            <td><c:out value="${cityModel.name}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
<script>
</script>
</html>