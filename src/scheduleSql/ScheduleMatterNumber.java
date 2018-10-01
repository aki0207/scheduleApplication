package scheduleSql;

import java.sql.*;

public class ScheduleMatterNumber {

	public int returnScheduleMatterNumber(String year_now, int month_now, int day_now, String id_now) {

		// 0埋めで整形
		String month_shaping_after = String.format("%02d", month_now);
		String day_shaping_after = String.format("%02d", day_now);

		// where旬でつかう
		String specified_day = year_now + "-" + month_shaping_after + "-" + day_shaping_after + " 00:00:00";
		// その日に何件スケジュールがあるか
		int schedule_matter_number = 0;

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			String sql = "SELECT count(schedule) as RESULT FROM schedule WHERE ID = ? and scheduledate = to_date(?,'YYYY-MM-DD HH24:MI:SS')";
			stmt = conn.prepareStatement(sql);

			// sql文の値をセット
			stmt.setInt(1, Integer.parseInt(id_now));
			stmt.setString(2, specified_day);
			// selectを実行し、結果票を取得
			rs = stmt.executeQuery();

			// 結果表に格納されたレコードの内容を表示
			while (rs.next()) {

				schedule_matter_number = Integer.parseInt(rs.getString("RESULT"));

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

		// 件数を返す
		return schedule_matter_number;

	}
}
