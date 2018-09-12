<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
div.dispSchedule {
	position: absolute;
	top: 10px;
	right: 500px;
}
</style>
</head>
<body>




	<%
		//セッションから値を取得
		int year_now = Integer.parseInt((String)session.getAttribute("YEAR"));
		int month_now = Integer.parseInt((String)session.getAttribute("MONTH"));
		int day_now = Integer.parseInt((String)session.getAttribute("DAY"));
		String[] schedule_array  = ((String[]) session.getAttribute("SCHEDULEARRAY"));
		String[] schedule_memo_array  = ((String[]) session.getAttribute("SCHEDULEMEMOARRAY"));
		//パラメータも取得
		String totale_time = request.getParameter("TOTALETIME");
		int index_number = Integer.parseInt(request.getParameter("INDEXNO"));
	%>


	スケジュール詳細ページ&nbsp;
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
			<td width="800" height="30"><a href="/CalendarJsp/ScheduleDetail.jsp?YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=day_now%>"><%= schedule_array[i] %></a></td>
		</tr>


				


		<%
			}
		%>

	</table>

	<!--スケジュールの詳細表示部分-->
	<div class="dispSchedule">
		<table border="1">
			<tr>
				<td>日付</td>
				<td width="400" height="30"><%= year_now %>年<%= month_now %>月<%= day_now %>日</td>
			</tr>
			<tr>
				<td>時間</td>
				<td width="400" height="30"><%= totale_time%>時</td>
			</tr>
			<tr>
				<td>スケジュール</td>
				<td width="400" height="30"><%= schedule_array[index_number]%></td>
				
			</tr>
			<tr>
				<td>メモ</td>
				<td width="400" height="30"><%= schedule_memo_array[index_number]%></td>
				
			</tr>
				
		
		
		</table>
		

	
	</div>
</body>
</html>