<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%
	//セッションスコープからユーザー情報を取得
	User user = (User) session.getAttribute("LOGINUSER");
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
		if (user != null) {
	%>

	<p>ログインに成功しました</p>
	<p>
		ようこそ<%=user.getName()%>さん
	</p>
	<%
		// セッションにuser情報を保存
			session.setAttribute("LOGINUSER", user);

			//セッションスコープの情報を破棄(テスト用)
			//session = request.getSession();
			//session.invalidate();
	%>
	<a href="/CalendarJsp/schedule/calendar.jsp?ID=<%=user.getId()%>">月一覧へ</a>

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