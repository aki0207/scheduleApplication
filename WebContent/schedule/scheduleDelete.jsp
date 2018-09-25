<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
a:visited {
	color: #0000ff;
	text-decoration: none
}
</style>
</head>
<body>




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


	スケジュール削除確認ページ&nbsp;
	<a
		href="/CalendarJsp/schedule/scheduleIndex.jsp?ID=<%=id_now %>&YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=day_now%>">戻る</a>


	<!--スケジュールの詳細表示部分-->
	<div class="dispSchedule">
		<table border="1">
			<tr>
				<td>日付</td>
				<td width="400" height="30"><%=year_now%>年<%=month_now%>月<%=day_now%>日</td>
			</tr>
			<tr>
				<td>時間</td>
				<td width="400" height="30">
					<%
						//整形
						if (totale_time.length() > 11) {
							totale_time = totale_time.substring(0, 12);
						}
					%> <%=totale_time%></td>
			</tr>
			<tr>
				<td>スケジュール</td>
				<%
					if (schedule_array[index_number].length() == 0) {

						RequestDispatcher dispatcher = request.getRequestDispatcher("/calendar.jsp");
						dispatcher.forward(request, response);

					}
				%>

				<td width="400" height="30"><%=schedule_array[index_number].substring(11)%></td>

			</tr>
			<tr>
				<td>メモ</td>
				<td width="400" height="30"><%=schedule_memo_array[index_number]%></td>

			</tr>




		</table>

		スケジュールを削除します。一度削除すると元には戻せません。<br> 削除しますか?,<br> <a
			href="/CalendarJsp/ScheduleDelete?YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=day_now%>&TOTALETIME=<%=totale_time%>">削除する</a>
		<a
			href="/CalendarJsp/shedule/scheduleDetail.jsp?ID=<%=id_now %>&YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=day_now%>&TOTALETIME=<%=totale_time%>&INDEXNO=<%=index_number%>">キャンセルして詳細に戻る</a>



	</div>
</body>
</html>