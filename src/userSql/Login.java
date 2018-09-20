package userSql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String input_id = request.getParameter("ID");
		String input_pass = request.getParameter("PASS");
		String name = "";
		User user = new User(input_id, input_pass, name);

		response.setContentType("text/html; charset=UTF-8");

		Connection conn = null;

		try {
			// JDBCドライバを読み込み
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// データベースへ接続
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// SQLを実行し、登録されている名前、パスワードを取得
			String sql = "select * from users";
			PreparedStatement pstmt = conn.prepareStatement(sql);

			// selectを実行し、結果票を取得
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				String db_exist_id = rs.getString("ID");
				String db_exist_pass = rs.getString("PASS");
				String db_exist_name = rs.getString("NAME");

				// user名が一致するか
				if (db_exist_id.equals(input_id)) {
					// パスワードが一致するか
					if (db_exist_pass.equals(input_pass)) {

						// ログインに成功した時の処理
						// 現在userインスタンスの名前に何も入っていないからDBにある名前を代入
						// ユーザー情報をセッションスコープに保存
						user.setName(db_exist_name);
						HttpSession session = request.getSession();
						session.setAttribute("LOGINUSER", user);
						break;

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

		// ログイン結果画面にフォワード
		RequestDispatcher dispacher = request.getRequestDispatcher("/user/loginResult.jsp");
		dispacher.forward(request, response);

	}
}