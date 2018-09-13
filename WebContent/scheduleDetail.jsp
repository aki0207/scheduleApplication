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
		//なんかjspからjspに移動するときセッションの値消えるらしいから再度セッションに保存
		session.setAttribute("YEAR", session.getAttribute("YEAR"));
		session.setAttribute("MONTH", session.getAttribute("MONTH"));
		session.setAttribute("DAY", session.getAttribute("DAY"));
		session.setAttribute("SCHEDULEARRAY", session.getAttribute("SCHEDULEARRAY"));
		session.setAttribute("SCHEDULEMEMOARRAY", session.getAttribute("SCHEDULEMEMOARRAY"));
		//セッションから値を取得
		int year_now = Integer.parseInt((String) session.getAttribute("YEAR"));
		int month_now = Integer.parseInt((String) session.getAttribute("MONTH"));
		int day_now = Integer.parseInt((String) session.getAttribute("DAY"));
		String[] schedule_array = ((String[]) session.getAttribute("SCHEDULEARRAY"));
		String[] schedule_memo_array = ((String[]) session.getAttribute("SCHEDULEMEMOARRAY"));
		//パラメータを取得
		String totale_time = request.getParameter("TOTALETIME");
		int index_number = Integer.parseInt(request.getParameter("INDEXNO"));
	%>


	スケジュール詳細ページ&nbsp;
	<a
		href="/CalendarJsp/scheduleIndex.jsp?YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=day_now%>">戻る</a>
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
			<td width="800" height="30"><a
				href="/CalendarJsp/scheduleDetail.jsp?YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=day_now%>&INDEXNO=<%=i%>&TOTALETIME=<%=schedule_array[i].substring(0)%>"><%=schedule_array[i]%></a></td>
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
				<td width="400" height="30">
				
				<%=schedule_array[index_number].substring(11)%></td>

			</tr>
			<tr>
				<td>メモ</td>
				<td width="400" height="30"><%=schedule_memo_array[index_number]%></td>

			</tr>



		</table>



	</div>
</body>
</html>