<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%
	//セッションスコープからユーザー情報を取得
	User login_user = (User) session.getAttribute("loginUser");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8">
<title>スケジュール</title>
</head>
<body>
	<h1>スケジュール管理ログイン</h1>
	<%
		if (login_user != null) {
	%>

	<p>ログインに成功しました</p>
	<p>
		ようこそ-<%=login_user.getName()%>さん
	</p>
	<%
		//セッションスコープの情報を破棄
			//session = request.getSession();
			//session.invalidate();
	%>
	<a href="/CalendarJsp/Calendar.jsp?ID=<%=login_user.getId()%>">月一覧へ</a>

	<%
		} else {
	%>

	<p>ログインに失敗しました</p>
	<a href="/CalendarJsp/index.jsp">Topへ</a>

	<%
		}
	%>


</body>
</html>