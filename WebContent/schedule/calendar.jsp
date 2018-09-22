<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.*"%>


<html>
<head>
<meta charset=UTF-8">
<title>カレンダーの表示</title>
<style>
table th {
	padding: 30px 70px;
}

table td {
	padding-bottom: 50px;
}

a {
	text-decoration: none;
}

/* .logout {
	align="right";
} */
</style>
</head>
<body>





	<%
		Month month = new Month();
		User user = (User) session.getAttribute("LOGINUSER");

		//パラメータ確認
		String year_parameter = request.getParameter("YEAR");
		String month_parameter = request.getParameter("MONTH");
		String id_parameter = request.getParameter("ID");

		//値がない、不正な場合-999をセット
		int year_now = month.yearParameterCheck(year_parameter);
		int month_now = month.monthParameterCheck(month_parameter);
		int id_now = month.idParameterCheck(id_parameter);

		//セッションにuser情報がないか、idが不正な値ならログインページへ
		if (user == null || id_now == -999) {

			RequestDispatcher dispatcher = request.getRequestDispatcher("/ErrorClose");
			dispatcher.forward(request, response);
			return;

		}

		//ログインしているか確認
		user.login_status = user.loginUser(id_now, user);

		if (user.login_status == false) {

			RequestDispatcher dispatcher = request.getRequestDispatcher("/ErrorClose");
			dispatcher.forward(request, response);

		}

		//不正な値なら現在の年、月をセット
		if (year_now == -999 || month_now == -999) {

			year_now = month.cal.get(Calendar.YEAR);
			month_now = month.cal.get(Calendar.MONTH) + 1;

		}

		//12月の次は1月かつ次年度
		if (month_now > 12) {

			month_now = 1;
			year_now++;

		}

		//逆もまた然り
		if (month_now < 1) {

			month_now = 12;
			year_now--;

		}

		int date_now = month.cal.get(Calendar.DATE);

		//monthインスタンスになんとなく現在の日時をセット
		month.cal.set(Calendar.YEAR, year_now);
		month.cal.set(Calendar.MONTH, month_now - 1);
		month.cal.set(Calendar.DATE, 1);
	%>

	<a
		href="/CalendarJsp/schedule/calendar.jsp?ID=<%=id_now%>&YEAR=<%=year_now%>&MONTH=<%=month_now + 1%>">翌月</a>&nbsp;<%=year_now%>年<%=month_now%>月&nbsp;
	<a
		href="/CalendarJsp/schedule/calendar.jsp?ID=<%=id_now%>&YEAR=<%=year_now%>&MONTH=<%=month_now - 1%>">前月</a>

	<div style="position: absolute; top: 0; right: 0;">
		<a href="/CalendarJsp/Logout">ログアウト</a>
	</div>






	<table border="1">
		<tr>
			<th>日</th>
			<th>月</th>
			<th>火</th>
			<th>水</th>
			<th>木</th>
			<th>金</th>
			<th>土</th>
		</tr>

		<!-- その月の最終日 -->
		<%
			int max_day = month.cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		%>

		<!--1日の曜日  -->
		<%
			int start_index = month.cal.get(Calendar.DAY_OF_WEEK);
		%>


		<tr>

			<!-- 1週目の1日までを空白で埋める -->

			<%
				for (int i = 1; i < start_index; i++) {
			%>


			<td>&nbsp;</td>



			<%
				}

				//当月の最終日まで日付けを入れていく
				for (int i = 1; i <= max_day; i++) {
			%>


			<td align="left"><a
				href="/CalendarJsp/ScheduleToday?YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=i%>&ID=<%=id_now%>"><%=i%></a></td>


			<%
				month.cal.set(Calendar.DATE, i);
			%>



			<!-- 土曜日なら次の列へ -->
			<%
				if (Calendar.SATURDAY == month.cal.get(Calendar.DAY_OF_WEEK)) {
			%>

		</tr>

		<%
			}
		%>



		<%
			}
		%>

		<!-- 当月の最終日が土曜日じゃない時、土曜日まで余白を埋める -->
		<%
			if (month.cal.get(month.cal.DAY_OF_WEEK) < 7) {
		%>

		<td>
			<%
				for (int count = month.cal.get(month.cal.DAY_OF_WEEK); count < 6; count++) {
			%>
		
		<td>&nbsp;</td>
		<%
			}
		%>

		</td>
		<%
			}
		%>

	</table>



	</form>
	</ body></ html>