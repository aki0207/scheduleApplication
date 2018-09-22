
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>

<meta charset=UTF-8">
<title>スケジュール管理</title>
</head>
<body>
	<h1>スケジュール管理</h1>
	<form action="/CalendarJsp/Login" method="post">
		ユーザーID:<input type="text" name="ID" required><br> パスワード:<input
			type="text" name="PASS" required><br> <input
			type="submit" value="ログイン">
	</form>

	<p>ユーザー登録はこちら</p>
	<a href="/CalendarJsp/user/registerUserInputForm.jsp">登録へ</a>
	
	<!-- ブラウザバックを認めない -->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script>
		// History API が使えるブラウザかどうかをチェック
		if (window.history && window.history.pushState) {
			//. ブラウザ履歴に１つ追加
			history.pushState("nohb", null, "");
			$(window).on("popstate", function(event) {
				//. このページで「戻る」を実行
				if (!event.originalEvent.state) {
					//. もう一度履歴を操作して終了
					history.pushState("nohb", null, "");
					return;
				}
			});
		}
	</script>
</body>
</html>