
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta charset=UTF-8">
<title>ユーザー登録</title>
</head>
<body>
	<h1>ユーザー登録ページ</h1>
	<form action="/CalendarJsp/RegisterUser" method="post">
		名前&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:<input type="text" required name="NAME"><br>
		パスワード:<input type="text"  required name="PASS" ><br> <input
			type="submit" value="登録"> <a href="/index">ログインページへ戻る</a>
	</form>


</body>
</html>