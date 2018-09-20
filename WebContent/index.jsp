
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>

<meta charset=UTF-8">
<title>スケジュール管理</title>
</head>
<body>
	<h1>
		スケジュール管理
	</h1>
	<form action="/CalendarJsp/Login" method="post">
		ユーザーID:<input type="text" name="ID"><br> パスワード:<input
			type="text" name="PASS"><br> <input type="submit"
			value="ログイン">
	</form>

	<p>ユーザー登録はこちら</p>
	<a href="/CalendarJsp/registerUserInputForm.jsp">登録へ</a>
</body>
</html>