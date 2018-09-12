<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		session.setAttribute("YEAR", request.getAttribute("YEAR"));
		session.setAttribute("MONTH", request.getAttribute("MONTH"));
		session.setAttribute("DAY", request.getAttribute("DAY"));
		session.setAttribute("SCHEDULEARRAY", request.getAttribute("SCHEDULEARRAY"));
		session.setAttribute("SCHEDULEMEMOARRAY", request.getAttribute("SCHEDULEMEMOARRAY"));
		//パラメータで送る用
		String total_time = "";
		String schedule = "";
		String schedule_memo = "";
		
		

		//セッションから値を取得
		int year_now = Integer.parseInt((String) request.getAttribute("YEAR"));
		int month_now = Integer.parseInt((String) request.getAttribute("MONTH"));
		int day_now = Integer.parseInt((String) request.getAttribute("DAY"));
		String[] schedule_array = ((String[]) request.getAttribute("SCHEDULEARRAY"));
		String[] schedule_memo_array = ((String[]) request.getAttribute("SCHEDULEMEMOARRAY"));
	%>








	スケジュール詳細ページ&nbsp;
	<br>

	<a
		href="/CalendarJsp/Calendar.jsp?YEAR=<%=year_now%>&MONTH=<%=month_now%>">戻る</a>
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
				if (schedule_array[i].length() != 0) {

						total_time = schedule_array[i].substring(0, 11);
						schedule = schedule_array[i];
						schedule_memo = schedule_memo_array[i];
			%>
			<td width="800" height="30"><a
				href="/CalendarJsp/scheduleDetail.jsp?TOTALETIME= <%=total_time%>&SCHEDULE=<%= schedule %>&SCHEDULEMEMO=<%= schedule_memo %>&INDEXNO=<%= i%>"><%=schedule_array[i]%></a></td>
				
			<%
				} else {
			%>
			<td width="800" height="30"><a
				href="/CalendarJsp/scheduleDetail.jsp"><%=schedule_array[i]%></a></td>

			<%
				}
			%>
			
		</tr>





		<%
			}
		%>

	</table>

	<!--セレクトボックスを作っていく
		まずは年度から			-->

	<div class="inputForm">

		<form action="/CalendarJsp/AddSchedule" method="post">
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
					<td><input type="text" name="PLAN" value="" size="30"
						maxlength="100"></td>
				</tr>


				<tr>
					<td valign="top" nowrap>メモ</td>
					<td><textarea name="MEMO" cols="30" rows="10" wrap="virtual"></textarea></td>
				</tr>
			</table>



			<p>
				<input type="submit" value="登録する"> <input type="reset"
					value="入力し直す">
			<p>
		</form>


	</div>
</body>
</html>