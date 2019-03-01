<%--
  Created by IntelliJ IDEA.
  User: ltian
  Date: 2019/1/15
  Time: 下午 07:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
</head>
<body>
    <center>
        <h1>请登录</h1>
        <%--<<form action="${pageContext.request.contextPath}/sso/doLogin" method="post">--%>
        <%--<form action="http://check.x.com:8080/ssof/doLogin" method="post">--%>
        <form action="${pageContext.request.contextPath}${path}/doLogin" method="post">
            <span>用户名：</span><input type="text" name="username"/>
            <span>密码：</span><input type="password" name="password"/>
            <input type="hidden" name="gotoUrl" value="${gotoUrl}"/>
            <input type="submit"/>
            <span style="color: red">${msg}</span>
        </form>
    </center>
</body>
</html>
