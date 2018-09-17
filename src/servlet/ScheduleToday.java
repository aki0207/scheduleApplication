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
import java.util.Calendar;
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

		Month month = new Month();
		Calendar theDay = Calendar.getInstance();

		// パラメータ取得
		String year_parameter = request.getParameter("YEAR");
		String month_parameter = request.getParameter("MONTH");
		String day_parameter = request.getParameter("DAY");

		// パラメータ確認。
		int year_shaping_before = month.intParameterCheck(year_parameter);
		int month_shaping_before = month.intParameterCheck(month_parameter);
		//int day_shaping_before = month.intParameterCheck(day_parameter);

		if (year_shaping_before == -999 || month_shaping_before== -999 ) {

			//不正な値なら今月の日付けをセット
			year_shaping_before = month.cal.get(Calendar.YEAR);
			month_shaping_before = month.cal.get(Calendar.MONTH) + 1;

		}
		
		
		int day_shaping_before = month.dayParameterCheck(year_shaping_before, month_shaping_before, day_parameter);
		
		if (day_shaping_before == - 999) {
			
			//不正な値なら今月の値をセット
			month_shaping_before = month.cal.get(Calendar.MONTH) + 1;
			day_shaping_before = month.cal.get(Calendar.DATE);
			
		}
		
		
		//確認後、ようやく年月日の変数登場
		String year_now = String.valueOf(year_shaping_before);
		String month_now = String.valueOf(month_shaping_before);
		String day_now = String.valueOf(day_shaping_before);

		//0埋めで整形
		String month_shaping_after = String.format("%02d", Integer.parseInt(month_now));
		String day_shaping_after = String.format("%02d", Integer.parseInt(day_now));

		// where旬でつかう
		String specified_day = year_now + "-" + month_shaping_after + "-" + day_shaping_after + " 00:00:00";
		// ログイン機能をつけた時に考える
		int id = 1;

		// 1日は24時間なんで
		String[] schedule_array = new String[24];
		String[] schedule_memo_array = new String[24];

		for (int i = 0; i < 24; i++) {

			schedule_array[i] = "";
			schedule_memo_array[i] = "";

		}

		response.setContentType("text/html; charset=UTF-8");
		Connection conn = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			PreparedStatement stmt = conn.prepareStatement(
					"SELECT * FROM schedule WHERE ID = ? and scheduledate = to_date(?,'YYYY-MM-DD HH24:MI:SS')");

			// sql文の値をセット
			stmt.setInt(1, 1);
			stmt.setString(2, specified_day);
			// selectを実行し、結果票を取得
			ResultSet rs = stmt.executeQuery();

			// 結果表に格納されたレコードの内容を表示
			while (rs.next()) {

				String start_time = rs.getString("STARTTIME");
				String end_time = rs.getString("ENDTIME");
				String schedule = rs.getString("SCHEDULE");
				String schedule_memo = rs.getString("SCHEDULEMEMO");

				// 0から23の数字と比較したいから時分秒の時分を抜き出し
				String start_time_time = start_time.substring(11, 13);
				String start_time_minute = start_time.substring(14, 16);
				String end_time_time = end_time.substring(11, 13);
				String end_time_minute = end_time.substring(14, 16);

				// スケジュール欄に表示させる時間
				String totale_time = start_time_time + ":" + start_time_minute + "-" + end_time_time + ":"
						+ end_time_minute + " ";

				// 配列に予定とメモをいれる
				for (int i = 0; i < 24; i++) {

					String character_conversion = Integer.toString(i);
					String time_schedule = String.format("%02d", Integer.parseInt(character_conversion));

					if (start_time_time.equals(time_schedule)) {

						schedule_array[i] = totale_time + schedule;
						schedule_memo_array[i] = schedule_memo;

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

		// セッションにセット
		HttpSession session = request.getSession();
		session.setAttribute("YEAR", year_now);
		session.setAttribute("MONTH", month_now);
		session.setAttribute("DAY", day_now);
		session.setAttribute("SCHEDULEARRAY", schedule_array);
		session.setAttribute("SCHEDULEMEMOARRAY", schedule_memo_array);

		// ユーザーのスケジュール表示画面へフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/scheduleIndex.jsp");
		dispatcher.forward(request, response);

	}
}
