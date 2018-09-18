<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Month"%>
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
		String index_number_conversion_before = request.getParameter("INDEXNO");

		//不正な値チェック
		Month month = new Month();
		totale_time = month.totaleTimeParameterCheck(totale_time);
		index_number_conversion_before = month.indexNumberParameterCheck(index_number_conversion_before);

		if (totale_time.equals("") || index_number_conversion_before.equals("")) {

			// ユーザーのスケジュール表示画面へフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/Calendar.jsp");
			dispatcher.forward(request, response);
		}
		
		int index_number = Integer.parseInt(index_number_conversion_before);
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
			<td width="800" height="30">
				<%
					if (schedule_array[i].length() != 0) {
				%> <a
				href="/CalendarJsp/scheduleDetail.jsp?YEAR=<%=year_now%>&MONTH=<%=month_now%>&DAY=<%=day_now%>&INDEXNO=<%=i%>&TOTALETIME=<%=schedule_array[i].substring(0, 11)%>"><%=schedule_array[i]%></a>
			</td>
		</tr>

		<%
			}
		%>





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
					<%
						//スケジュールに値が入ってるときだけ表示
						if (schedule_array[index_number].length() != 0) {
					%> <%=schedule_array[index_number].substring(11)%></td>

				<%
					} else {
				%>

				<%
					// 存在しないindexnumberを指定された場合、トップページへ
						RequestDispatcher dispatcher = request.getRequestDispatcher("/Calendar.jsp");
						dispatcher.forward(request, response);

					}
				%>

			</tr>
			<tr>
				<td>メモ</td>
				<td width="400" height="30"><%=schedule_memo_array[index_number]%></td>

			</tr>




		</table>

		<a
			href="/CalendarJsp/scheduleEdit.jsp?TOTALETIME=<%=totale_time%>&INDEXNO=<%=index_number%>">編集へ</a>
		<a
			href="/CalendarJsp/scheduleDelete.jsp?TOTALETIME=<%=totale_time%>&INDEXNO=<%=index_number%>">削除へ</a>



	</div>
</body>
</html>