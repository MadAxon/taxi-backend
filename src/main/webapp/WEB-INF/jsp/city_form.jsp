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
    </style>
    <script>
        function loadModalWindow() {
            // open your window here
            window.open("city_dialog_add.jsp");
        }
    </script>
</head>
<body>
<button type="button" name="back" onclick="window.history.back()"><- Назад</button>
<h3>Текущие города</h3>
<button onclick="loadModalWindow()">Добавить город</button>
<table>
    <tr>
        <td>id</td>

        <td>Название</td>
    </tr>
    <c:forEach items="${cities}" var="cityModel">
        <tr>
            <td><c:out value="${cityModel.id}"/></td>
            <td><c:out value="${cityModel.name}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
<script>
    $(function() {

        $("#dialog").dialog({
            autoOpen: false,
            modal: true
        });

        $("#myButton").on("click", function(e) {
            e.preventDefault();
            $("#dialog").dialog("open");
        });

    });
</script>
</html>