package scheduleSql;

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

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("LOGINUSER");

		// parameterCHeckを呼び出したいがためだけにmonthインスタンスを生成.よき方法求む
		Month m = new Month();

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
		String totale_time;
		String id_now_parameter;

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
		totale_time = request.getParameter("TOTALETIME");
		id_now_parameter = user.getId();

		// 日付が不正な値な時、パラメータ無しでCalendar.jspにリダイレクト
		day_parameter = m.dayParameterCheck(Integer.parseInt(year), Integer.parseInt(month), day);
		if (day_parameter == -999) {

			response.sendRedirect("/CalendarJsp/schedule/calendar.jsp?ID=" + id_now_parameter);
			return;

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

		String edit_target_year = (String) session.getAttribute("YEAR");
		String edit_target_month = (String) session.getAttribute("MONTH");
		String edit_target_day = (String) session.getAttribute("DAY");
		String edit_target_start_time = totale_time.substring(0, 5) + ":00";

		// 0埋め
		edit_target_year = String.format("%02d", Integer.parseInt(edit_target_year));
		edit_target_month = String.format("%02d", Integer.parseInt(edit_target_month));
		edit_target_day = String.format("%02d", Integer.parseInt(edit_target_day));

		// 整形して実際に流されるのものがこちら
		String edit_target_search_query = edit_target_year + "-" + edit_target_month + "-" + edit_target_day + " "
				+ edit_target_start_time;

		int id_now = Integer.parseInt(user.getId());

		response.setContentType("text/html; charset=UTF-8");

		Connection conn = null;

		try {
			// JDBCドライバを読み込み
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// データベースへ接続
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// select文で時間に重複がないか調べる
			// 条件としては、登録先の時間かぶりは許さないが、更新前の時間への重複は許す
			String sql = "select schedule from schedule where id = ? and scheduledate = to_date(?,'YYYY-MM-DD HH24:MI:SS') and starttime not in (to_date(?,'YYYY-MM-DD HH24:MI:SS')) and (starttime between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS') or endtime between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS')) ";
			PreparedStatement stmt = conn.prepareStatement(sql);

			// sql文の値をセット
			stmt.setInt(1, id_now);
			System.out.println(id_now);
			stmt.setString(2, date_query);
			System.out.println(date_query);
			stmt.setString(3, start_time_query);
			System.out.println(start_time_query);
			stmt.setString(4, start_time_query);
			System.out.println(start_time_query);
			stmt.setString(5, end_time_query);
			System.out.println(end_time_query);
			stmt.setString(6, start_time_query);
			System.out.println(start_time_query);
			stmt.setString(7, end_time_query);
			System.out.println(end_time_query);

			// selectを実行し、結果票を取得
			ResultSet rs = stmt.executeQuery();

			// 検索結果が存在しない場合のみ追加を行う
			if (!(rs.isBeforeFirst())) {

				// update文を準備
				sql = "update schedule set scheduledate = case id when ? then to_date(?,'YYYY-MM-DD HH24:MI:SS') end, starttime = case id when ? then to_date(?,'YYYY-MM-DD HH24:MI:SS') end,endtime = case id when ? then to_date(?,'YYYY-MM-DD HH24:MI:SS') end, schedule = case id when ? then ? END, schedulememo = case id when ? then ? end where  id = ? and starttime = to_date(?,'YYYY-MM-DD HH24:MI:SS')";

				// ?にセット
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, id_now);
				stmt.setString(2, date_query);
				stmt.setInt(3, id_now);
				stmt.setString(4, start_time_query);
				stmt.setInt(5, id_now);
				stmt.setString(6, end_time_query);
				stmt.setInt(7, id_now);
				stmt.setString(8, plan);
				stmt.setInt(9, id_now);
				stmt.setString(10, memo);
				stmt.setInt(11, id_now);
				stmt.setString(12, edit_target_search_query);

				// UPDATE文を実行する
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
		sb.append(id_now);
		response.sendRedirect(new String(sb));
	}
}