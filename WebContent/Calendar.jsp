<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Month"%>

<html>
<head>
<meta charset=UTF-8">
<title>カレンダーの表示</title>
</head>
<body>

	<!-- 現在の年度及び月を取得する -->
	<%
		Month month = new Month();
	%>
	<%
		int year_now = month.cal.get(Calendar.YEAR);
	%>
	<%
		int month_now = month.cal.get(Calendar.MONTH) + 1;
	%>
	

	<%
		month.cal.set(Calendar.YEAR, year_now);
	%>
	<%
		month.cal.set(Calendar.MONTH, month_now - 1);
	%>
	<%
		month.cal.set(Calendar.DATE, 1);
	%>

	<p><%=year_now%>年<%=month_now%>月
	</p>


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
			%>





			<%
				for (int i = 1; i <= max_day; i++) {
			%>


			<td><%=i%></td>


			<%
				month.cal.set(Calendar.DATE, i);
			%>


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
			if (month.cal.get(Calendar.DAY_OF_WEEK) != 7) {
		%>

		<td>

		<%
			for (int count = month.cal.get(Calendar.DAY_OF_WEEK); count < 6; count++) {
		%>

		&nbsp;

		<%
			}
		%>

		</td>
		<%
			}
		%>

	</table>
	</form>

</body>
</html>