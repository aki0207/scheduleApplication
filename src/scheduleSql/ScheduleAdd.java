package scheduleSql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.*;

@WebServlet("/ScheduleAdd")
public class ScheduleAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("LOGINUSER");

		// ログインしてるか確認
		if (user == null) {

			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
			dispatcher.forward(request, response);

		}

		// パラメータ格納用
		String year;
		String month;
		String day;
		String shour;
		String sminute;
		String ehour;
		String eminute;
		String plan;
		String memo;
		String parameter;

		// parameterCHeckを呼び出したいがためだけにmonthインスタンスを生成.よき方法求
		Month m = new Month();

		// パラメータが不正な値じゃないかチェック×2
		request.setCharacterEncoding("UTF-8");
		parameter = request.getParameter("YEAR");
		year = m.stringParameterCheck(parameter);
		parameter = request.getParameter("MONTH");
		month = m.stringParameterCheck(parameter);
		parameter = request.getParameter("DAY");
		day = m.stringParameterCheck(parameter);
		parameter = request.getParameter("SHOUR");
		shour = m.stringParameterCheck(parameter);
		parameter = request.getParameter("SMINUTE");
		sminute = m.stringParameterCheck(parameter);
		parameter = request.getParameter("EHOUR");
		ehour = m.stringParameterCheck(parameter);
		parameter = request.getParameter("EMINUTE");
		eminute = m.stringParameterCheck(parameter);
		parameter = request.getParameter("PLAN");
		plan = m.stringParameterCheck(parameter);
		parameter = request.getParameter("MEMO");
		memo = m.stringParameterCheck(parameter);

		// 日付が不正な値な時、パラメータ無しでCalendar.jspにリダイレクト
		if (year.equals("") || month.equals("") || day.equals("")) {
			response.sendRedirect("/CalendarJsp/schedule/calendar.jsp");
		}

		// sqlのクエリー達
		int id = Integer.parseInt(user.getId());
		String date_format;
		String date_query;
		String start_time_query;
		String end_time_query;

		// 0埋めで整形
		month = String.format("%02d", Integer.parseInt(month));
		day = String.format("%02d", Integer.parseInt(day));
		shour = String.format("%02d", Integer.parseInt(shour));
		sminute = String.format("%02d", Integer.parseInt(sminute));
		ehour = String.format("%02d", Integer.parseInt(ehour));
		eminute = String.format("%02d", Integer.parseInt(eminute));

		// insert文のwhere旬に代入する値を準備
		// sql実行のために整形
		date_format = year + "-" + month + "-" + day;
		date_query = date_format + " 00:00:00";
		start_time_query = date_format + " " + shour + ":" + sminute + ":00";
		end_time_query = date_format + " " + ehour + ":" + eminute + ":00";

		response.setContentType("text/html; charset=UTF-8");
		Connection conn = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// select文で時間に重複がないか調べる
			String sql = "select schedule from schedule where id = ? and scheduledate = to_date(?,'YYYY-MM-DD HH24:MI:SS') and (starttime between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS') or endtime between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS'))";
			PreparedStatement stmt = conn.prepareStatement(sql);

			// sql文の値をセット
			stmt.setInt(1, id);
			stmt.setString(2, date_query);
			System.out.println(date_query);
			stmt.setString(3, start_time_query);
			System.out.println(start_time_query);
			stmt.setString(4, end_time_query);
			System.out.println(end_time_query);
			stmt.setString(5, start_time_query);
			stmt.setString(6, end_time_query);

			// selectを実行し、結果票を取得
			System.out.println("実行するよん");
			;
			ResultSet rs = stmt.executeQuery();

			// 検索結果が存在しない場合のみ追加を行う
			if (!(rs.isBeforeFirst())) {

				// insert文を準備
				sql = "insert  into schedule (id, scheduledate, starttime, endtime, schedule, schedulememo) values ( ?,to_date( ?,'YYYY-MM-DD HH24:MI:SS'), to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),? ,?)";
				stmt = conn.prepareStatement(sql);

				// sql文の値をセット
				stmt.setInt(1, id);
				stmt.setString(2, date_query);
				stmt.setString(3, start_time_query);
				stmt.setString(4, end_time_query);
				stmt.setString(5, plan);
				stmt.setString(6, memo);

				// 実行
				int num = stmt.executeUpdate();

				stmt.close();

			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();

		} catch (SQLException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				if (conn != null) {

					conn.close();

				}

			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		StringBuffer sb = new StringBuffer();
		sb.append("/CalendarJsp/schedule/calendar.jsp");
		sb.append("?YEAR=");
		sb.append(year);
		sb.append("&MONTH=");
		sb.append(month);
		sb.append("&ID=");
		sb.append(id);
		response.sendRedirect(new String(sb));

	}
}