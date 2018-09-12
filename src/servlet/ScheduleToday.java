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
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;

import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import model.Month;

@WebServlet("/ScheduleToday")
public class ScheduleToday extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		System.out.println("俺をみろぉぉぉ！");

		// パラメータ取得
		String year_now = request.getParameter("YEAR");
		String month_now = request.getParameter("MONTH");
		String day_now = request.getParameter("DAY");

		// 0埋め
		String month_shaping = String.format("%02d", Integer.parseInt(month_now));
		String day_shaping = String.format("%02d", Integer.parseInt(day_now));

		// where旬でつかう
		String specified_day = year_now + "-" + month_shaping + "-" + day_shaping + " 00:00:00";
		// ログイン機能をつけた時に考える
		int id = 1;

		String[] schedule_array = new String[24];

		for (int i = 0; i < 24; i++) {

			schedule_array[i] = "";

		}

		response.setContentType("text/html; charset=UTF-8");

		Connection conn = null;

		try {
			// JDBCドライバを読み込み
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// データベースへ接続
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// select文を準備
			/*String sql = "select schedule,schedulememo from schedule where id =  and STARTTIME = to_date('"
					+ specified_day + "','YYYY-MM-DD HH24:MI:SS')";

			System.out.println("実行するsqlはこれだ!" + sql);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			
*/

			PreparedStatement stmt = conn.prepareStatement(
					"SELECT * FROM schedule WHERE ID = ? and scheduledate = to_date(?,'YYYY-MM-DD HH24:MI:SS')");
			System.out.println(specified_day);
			stmt.setInt(1, 1);
			stmt.setString(2, specified_day);
			ResultSet rs = stmt.executeQuery();
			
			
			// selectを実行し、結果票を取得
			//ResultSet rs = pstmt.executeQuery();

			// 結果表に格納されたレコードの内容を表示
			while (rs.next()) {

				String start_time = rs.getString("STARTTIME");
				String end_time = rs.getString("ENDTIME");
				String schedule = rs.getString("SCHEDULE");

				String start_time_time = start_time.substring(11, 13);
				String start_time_minute = start_time.substring(14, 16);
				String end_time_time = end_time.substring(11, 13);
				String end_time_minute = end_time.substring(14, 16);
				String totale_time = start_time_time + ":" + start_time_minute + "-" + end_time_time + ":"
						+ end_time_minute + " ";

				for (int i = 0; i < 24; i++) {

					String character_conversion = Integer.toString(i);
					String time_schedule = String.format("%02d", Integer.parseInt(character_conversion));
					System.out.println(time_schedule);
					System.out.println(start_time_time);

					if (start_time_time.equals(time_schedule)) {

						schedule_array[i] = totale_time + schedule;

					}

				}

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

		System.out.println("sessionにはこいつらをいれるよ.year" + year_now + ":month:" + month_now + ":day:" + day_now);

		request.setAttribute("YEAR", year_now);
		request.setAttribute("MONTH", month_now);
		request.setAttribute("DAY", day_now);
		request.setAttribute("SCHEDULEARRAY", schedule_array);

		// ユーザーのスケジュール表示画面へフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/scheduleDetail.jsp");
		dispatcher.forward(request, response);

	}
}
