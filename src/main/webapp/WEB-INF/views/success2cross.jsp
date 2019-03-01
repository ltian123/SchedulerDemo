<%--
  Created by IntelliJ IDEA.
  User: ltian
  Date: 2019/1/15
  Time: 下午 07:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>demo2</title>
</head>
<body>
    <h1>欢迎登录demo2，您已经登录成功</h1>
    <c:forEach var="url" items="${hiddenUrl}">
        <iframe src="${url}" width="0px" height="0px"></iframe>
    </c:forEach>
</body>
</html>
