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

import com.sun.xml.internal.bind.v2.model.core.ID;

import model.*;

@WebServlet("/ScheduleAdd")
public class ScheduleAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("LOGINUSER");
		System.out.println(user.getId());

		// ログインしてるか確認
		if (user.getId() == null) {

			RequestDispatcher dispatcher = request.getRequestDispatcher("/ErrorClose");
			dispatcher.forward(request, response);

			return;

		}

		// パラメータ格納用
		String year;
		String month;
		String day;
		int day_parameter;
		String shour;
		String sminute;
		String ehour;
		String eminute;
		String plan;
		String memo;
		String id_now;

		// parameterCHeckを呼び出したいがためだけにmonthインスタンスを生成.よき方法求む
		Month m = new Month();

		// パラメータのチェックをすべきやけど、ツール等をつかわん限り、不正な値を作れそうなのは日のみ。 そのため今回は日のみチェックを行う

		request.setCharacterEncoding("UTF-8");
		year = request.getParameter("YEAR");
		month = request.getParameter("MONTH");
		day = request.getParameter("DAY");
		shour = request.getParameter("SHOUR");
		sminute = request.getParameter("SMINUTE");
		ehour = request.getParameter("EHOUR");
		eminute = request.getParameter("EMINUTE");
		plan = request.getParameter("PLAN");
		memo = request.getParameter("MEMO");
		id_now = user.getId();
		System.out.println("id_nowは" + id_now);

		// 日付が不正な値な時,日付一覧ページへリダイレクト
		day_parameter = m.dayParameterCheck(Integer.parseInt(year), Integer.parseInt(month), day);
		if (day_parameter == -999) {

			System.out.println("ひづけが不正");
			response.sendRedirect("/CalendarJsp/schedule/calendar.jsp?ID=" + id_now);
			return;

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
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// select文で時間に重複がないか調べる
			String sql = "select schedule from schedule where id = ? and scheduledate = to_date(?,'YYYY-MM-DD HH24:MI:SS') and (starttime between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS') or endtime between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS'))";
			stmt = conn.prepareStatement(sql);

			// sql文の値をセット
			stmt.setInt(1, id);
			stmt.setString(2, date_query);
			stmt.setString(3, start_time_query);
			stmt.setString(4, end_time_query);
			stmt.setString(5, start_time_query);
			stmt.setString(6, end_time_query);

			// selectを実行し、結果票を取得
			rs = stmt.executeQuery();

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

				if (rs != null) {

					rs.close();
				}

				if (stmt != null) {

					stmt.close();

				}

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