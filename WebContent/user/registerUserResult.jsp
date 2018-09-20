<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%
	//セッションスコープからユーザー情報を取得
	User user = (User) session.getAttribute("User");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8">
<title>ユーザー登録</title>
</head>
<body>
	<h1>ユーザー登録</h1>
	<%
		if (user != null) {
	%>

	<p>登録が完了しました！</p>
	<p>
		ようこそ<%=user.getName()%>さん
	</p>

	<p>以下のID及びパスワードはログインの際、必要となります。</p>

	<p>
		ID:<%=user.getId()%>
		パスワード:<%=user.getPass()%>
	</p>

	<%
		//セッション殺す
		session = request.getSession();
		session.invalidate();
		
	%>
	<a href="/CalendarJsp/index.jsp">ログインへ</a>

	<%
		} else {
	%>

	<p>既に使用されています。ログインをお試し下さい</p>
	<a href="/CalendarJsp/index.jsp">ログインへ</a>

	<%
		}
	%>


</body>
</html>