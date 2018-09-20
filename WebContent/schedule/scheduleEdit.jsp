<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
div.inputForm {
	position: absolute;
	top: 10px;
	right: 600px;
}
</style>
</head>
<body>
	<%@ page session="true"%>



	<%
		//なんかjspからjspに移動するときセッションの値消えるらしいから再度セッションに保存
		session.setAttribute("LOGINUSER", session.getAttribute("LOGINUSER"));
		session.setAttribute("YEAR", session.getAttribute("YEAR"));
		session.setAttribute("MONTH", session.getAttribute("MONTH"));
		session.setAttribute("DAY", session.getAttribute("DAY"));
		session.setAttribute("SCHEDULEARRAY", session.getAttribute("SCHEDULEARRAY"));
		session.setAttribute("SCHEDULEMEMOARRAY", session.getAttribute("SCHEDULEMEMOARRAY"));

		//セッションから値を取得
		User user = (User) session.getAttribute("LOGINUSER");
		int year_now = Integer.parseInt((String) session.getAttribute("YEAR"));
		int month_now = Integer.parseInt((String) session.getAttribute("MONTH"));
		int day_now = Integer.parseInt((String) session.getAttribute("DAY"));
		String[] schedule_array = ((String[]) session.getAttribute("SCHEDULEARRAY"));
		String[] schedule_memo_array = ((String[]) session.getAttribute("SCHEDULEMEMOARRAY"));

		//パラメータを取得
		String id_parameter = request.getParameter("ID");
		String totale_time = request.getParameter("TOTALETIME");
		String index_number_conversion_before = request.getParameter("INDEXNO");

		//不正な値チェック
		Month month = new Month();
		totale_time = month.totaleTimeParameterCheck(totale_time);
		index_number_conversion_before = month.indexNumberParameterCheck(index_number_conversion_before);
		int id_now = month.idParameterCheck(id_parameter);

		if (totale_time.equals("") || index_number_conversion_before.equals("") || id_now == -999 || user == null) {

			// ユーザーのスケジュール表示画面へフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
			dispatcher.forward(request, response);
		}

		//ログインしているか確認
		user.login_status = user.loginUser(id_now, user);

		if (user.login_status == false) {

			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
			dispatcher.forward(request, response);

		}

		int index_number = Integer.parseInt(index_number_conversion_before);
	%>







	スケジュール編集ページ
	<br>


	<a
		href="/CalendarJsp/schedule/scheduleDetail.jsp?ID=<%=id_now%>&YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=day_now%>&INDEXNO=<%=index_number%>&TOTALETIME=<%=schedule_array[index_number].substring(0, 11)%>">戻る</a>
	<table border="1">
		<tr>
			<td>時刻</td>
			<td>予定</td>
		</tr>

		<!-- 0時から23時までを表示 -->
		<%
			for (int i = 0; i < 24; i++) {
		%>
		<tr>
			<td><%=i%>:00</td>
			<!-- 時分をDetailに送るため -->

			<%
				//Detailに送る用のtotale_timeパラメータ
					String detail_totale_time;

					if (schedule_array[i].length() != 0) {
						detail_totale_time = schedule_array[i].substring(0, 11);
			%>
			<td width="800" height="30"><a
				href="/CalendarJsp/schedule/scheduleDetail.jsp?ID=<%=id_now%>&TOTALETIME=<%=detail_totale_time%>&INDEXNO=<%=i%>"><%=schedule_array[i]%></a></td>



			<%
				} else {
			%>
			<td width="800" height="30"><a
				href="/CalendarJsp/schedule/scheduleDetail.jsp?ID=<%=id_now%>"><%=schedule_array[i]%></a></td>

			<%
				}
			%>

		</tr>





		<%
			}
		%>

	</table>



	<!--セレクトボックスを作っていく-->

	<div class="inputForm">

		
		<form
			action="/CalendarJsp/ScheduleEdit?TOTALETIME=<%=totale_time%>&INDEXNO=<%=index_number%>"
			method="post">
			<table>
				<tr>
					<td nowrap>日付</td>
					<td><select name="YEAR">

							<%
								for (int i = year_now; i < year_now + 5; i++) {
							%>

							<%
								//パラメータをプルダウンメニューの初期値にする
									if (i == year_now) {
							%>

							<option value=<%=year_now%> selected><%=i%>年

								<%
								}
							%>
							
							<option value=<%=year_now%>><%=i%>年

								<%
															}
														%>
							
					</select> <select name="MONTH">


							<%
								for (int j = 1; j < 13; j++) {
							%>

							<%
								if (j == month_now) {
							%>

							<option value=<%=j%> selected><%=j%>日

								<%
								}
							%>
							
							<option value=<%=j%>><%=j%>月

								<%
															}
														%>
							
					</select> <select name="DAY">

							<%
								for (int k = 1; k < 32; k++) {
							%>

							<%
								if (k == day_now) {
							%>

							<option value=<%=k%> selected><%=k%>日

								<%
								}
							%>

								<%
									if (k != day_now) {
								%>
							
							<option value=<%=k%>><%=k%>日

								<%
															}
														%>


								<%
									}
								%>
							
					</select></td>
				</tr>


				<tr>
					<td nowrap>時刻</td>
					<td><select name="SHOUR">

							<%
								for (int i = 0; i < 24; i++) {
							%>

							<option value=<%=i%>><%=i%>時

								<%
								}
							%>
							
					</select> <select name="SMINUTE">

							<option value="0">00分
							<option value="30">30分
					</select> ～ <select name="EHOUR">

							<%
								for (int i = 0; i < 24; i++) {
							%>

							<option value=<%=i%>><%=i%>時

								<%
								}
							%>
							
					</select> <select name="EMINUTE">




							<option value="0">00分
							<option value="30">30分
					</select></td>
				</tr>

				<tr>
					<td nowrap>予定</td>
					<%
						//存在しないindex_numberを入力されたときはトップページへ
						if (schedule_array[index_number].length() == 0) {

							RequestDispatcher dispatcher = request.getRequestDispatcher("/Calendar.jsp");
							dispatcher.forward(request, response);

						}
					%>
					<td><input type="text" name="PLAN"
						value="<%=schedule_array[index_number].substring(11)%>" size="30"
						maxlength="255" required></td>
				</tr>


				<tr>
					<td valign="top" nowrap>メモ</td>
					<td><textarea name="MEMO" cols="30" rows="10" wrap="virtual"
							required><%=schedule_memo_array[index_number]%></textarea></td>
				</tr>
			</table>



			<p>
				<input type="submit" value="変更する"> <input type="reset"
					value="入力し直す">
			<p>
		</form>


	</div>
</body>
</html>