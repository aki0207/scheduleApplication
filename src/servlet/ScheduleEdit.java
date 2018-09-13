package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.xml.crypto.Data;
import model.*;

import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

@WebServlet("/ScheduleEdit")
public class ScheduleEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

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

		// parameterCHeckを呼び出したいがためだけにmonthインスタンスを生成.よき方法求む
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
			response.sendRedirect("/CalendarJsp/Calendar.jsp");
		}

		String date_format;
		String date_query;
		String start_time_query;
		String end_time_query;

		// 0埋め
		month = String.format("%02d", Integer.parseInt(month));
		day = String.format("%02d", Integer.parseInt(day));
		shour = String.format("%02d", Integer.parseInt(shour));
		sminute = String.format("%02d", Integer.parseInt(sminute));
		ehour = String.format("%02d", Integer.parseInt(ehour));
		eminute = String.format("%02d", Integer.parseInt(eminute));

		// update文のwhere旬に代入する値を準備
		// sql実行のためにフォーマットを整る
		date_format = year + "-" + month + "-" + day;
		date_query = date_format + " 00:00:00";
		start_time_query = date_format + " " + shour + ":" + sminute + ":00";
		end_time_query = date_format + " " + ehour + ":" + eminute + ":00";

		// セッション及びパラメータから値を持って来て、where旬で更新元のデータを指定するための値を用意
		HttpSession session = request.getSession();
		String edit_target_year = (String) session.getAttribute("YEAR");
		String edit_target_month = (String) session.getAttribute("MONTH");
		String edit_target_day = (String) session.getAttribute("DAY");
		String edit_target_start_time = request.getParameter("TOTALETIME").substring(0, 5) + ":00";

		// 0埋め
		edit_target_year = String.format("%02d", Integer.parseInt(edit_target_year));
		edit_target_month = String.format("%02d", Integer.parseInt(edit_target_month));
		edit_target_day = String.format("%02d", Integer.parseInt(edit_target_day));

		// 整形して実際に流されるのものがこちら
		String edit_target_search_query = edit_target_year + "-" + edit_target_month + "-" + edit_target_day + " "
				+ edit_target_start_time;
		int edit_id = 1;

		response.setContentType("text/html; charset=UTF-8");

		Connection conn = null;

		try {
			// JDBCドライバを読み込み
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// データベースへ接続
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// update文を準備
			String sql = "update schedule set scheduledate = case id when ? then to_date(?,'YYYY-MM-DD HH24:MI:SS') end, starttime = case id when ? then to_date(?,'YYYY-MM-DD HH24:MI:SS') end,endtime = case id when ? then to_date(?,'YYYY-MM-DD HH24:MI:SS') end, schedule = case id when ? then ? END, schedulememo = case id when ? then ? end where  id = ? and starttime = to_date(?,'YYYY-MM-DD HH24:MI:SS')";
			
			// ?にセット
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, edit_id);
			stmt.setString(2, date_query);
			stmt.setInt(3, edit_id);
			stmt.setString(4, start_time_query);
			stmt.setInt(5, edit_id);
			stmt.setString(6, end_time_query);
			stmt.setInt(7, edit_id);
			stmt.setString(8, plan);
			stmt.setInt(9, edit_id);
			stmt.setString(10, memo);
			stmt.setInt(11, edit_id);
			stmt.setString(12, edit_target_search_query);
			
			
		
			

			// UPDATE文を実行する
			int num = stmt.executeUpdate();

			stmt.close();

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

		// ただmonthから1ひきたかっただけなんだ…
		// int month_numeric_value = Integer.parseInt(month) - 1;

		StringBuffer sb = new StringBuffer();
		sb.append("/CalendarJsp/Calendar.jsp");
		sb.append("?YEAR=");
		sb.append(year);
		sb.append("&MONTH=");
		sb.append(month);
		response.sendRedirect(new String(sb));
	}
}