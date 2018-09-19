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

@WebServlet("/ScheduleDelete")
public class ScheduleDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();

		// なんか移動するときセッションの値消えるらしいから再度セッションに保存
		session.setAttribute("YEAR", session.getAttribute("YEAR"));
		session.setAttribute("MONTH", session.getAttribute("MONTH"));
		session.setAttribute("DAY", session.getAttribute("DAY"));
		session.setAttribute("SCHEDULEARRAY", session.getAttribute("SCHEDULEARRAY"));
		session.setAttribute("SCHEDULEMEMOARRAY", session.getAttribute("SCHEDULEMEMOARRAY"));

		String year_now = ((String) session.getAttribute("YEAR"));
		String month_now =((String) session.getAttribute("MONTH"));
		String day_now = ((String) session.getAttribute("DAY"));

		// delete文のwhere旬に代入する値を整形してつくる
		String delete_target_start_time = request.getParameter("TOTALETIME").substring(0, 5) + ":00";
		delete_target_start_time = year_now + "-" + month_now + "-" + day_now + " " + delete_target_start_time;
		int edit_id = 1;
		System.out.println("ちなみにこーなってる" +delete_target_start_time);

		response.setContentType("text/html; charset=UTF-8");
		Connection conn = null;

		try {
			// JDBCドライバを読み込み
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// データベースへ接続
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// update文を準備
			String sql = "delete from schedule where id = ? and starttime = to_date(?,'YYYY-MM-DD HH24:MI:SS')";

			// ?にセット
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, edit_id);
			stmt.setString(2, delete_target_start_time);

			// delete文を実行する
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
		sb.append(year_now);
		sb.append("&MONTH=");
		sb.append(month_now);
		response.sendRedirect(new String(sb));
	}
}