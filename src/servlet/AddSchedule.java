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
import javax.xml.crypto.Data;
import model.*;

import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

@WebServlet("/AddSchedule")
public class AddSchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

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

		// sqlのクエリー達
		String date_format;
		String date_query;
		String start_time_query;
		String end_time_query;
		// また考える
		int id = 1;

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

		// 日付が指定されていない場合開始時間及び終了時間をnullで登録
		if (shour.equals("") || sminute.equals("") || eminute.equals("") || ehour.equals("")) {

			start_time_query = null;
			end_time_query = null;

		}

		response.setContentType("text/html; charset=UTF-8");
		Connection conn = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// insert文を準備
			String sql = "insert  into schedule (id, scheduledate, starttime, endtime, schedule, schedulememo) values ( ?,to_date( ?,'YYYY-MM-DD HH24:MI:SS'), to_date(?,'YYYY-MM-DD HH24:MI:SS'),to_date(?,'YYYY-MM-DD HH24:MI:SS'),? ,?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setString(2, date_query);
			stmt.setString(3, start_time_query);
			stmt.setString(4, end_time_query);
			stmt.setString(5, plan);
			stmt.setString(6, memo);

			// 実行
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

		StringBuffer sb = new StringBuffer();
		sb.append("/CalendarJsp/Calendar.jsp");
		sb.append("?YEAR=");
		sb.append(year);
		sb.append("&MONTH=");
		sb.append(month);
		response.sendRedirect(new String(sb));

	}
}