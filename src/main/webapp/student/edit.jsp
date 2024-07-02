<%--
  Created by IntelliJ IDEA.
  User: quyen
  Date: 7/2/2024
  Time: 8:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Student</title>
</head>
<body>
<h1>Edit Student</h1>
<p>
    <c:if test='${requestScope["message"] != null}'>
        <span class="message">${requestScope["message"]}</span>
    </c:if>
</p>
<p>
    <a href="/student">Back to student list</a>
</p>
<form method="post" >
    <fieldset>
        <legend>Student information</legend>
        <table>
            <tr>
                <td>Name: </td>
                <td><input type="text" name="name" id="name" value="${requestScope["student"].name}"></td>
            </tr>
            <tr>
                <td>Address: </td>
                <td><input type="text" name="address" id="address" value="${requestScope["student"].address}"></td>
            </tr>
            <tr>
                <td>Point: </td>
                <td><input type="text" name="point" id="point" value="${requestScope["student"].point}"></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Update Student"></td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>
