package userSql;

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

import model.User;

@WebServlet("/RegisterUser")
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// 登録状況確認フラグ
		boolean register_status = false;

		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String input_name = request.getParameter("NAME");
		String input_pass = request.getParameter("PASS");

		User user = new User(input_pass, input_name);

		response.setContentType("text/html; charset=UTF-8");

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			// JDBCドライバを読み込み
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// データベースへ接続
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.132:1521:xe", "stockuser", "moriara0029");

			// SQLを実行し、登録されている名前、パスワードを取得
			String sql = "select name,pass from users";
			stmt = conn.prepareStatement(sql);
			// selectを実行し、結果票を取得
			rs = stmt.executeQuery();

			while (rs.next()) {

				String db_exist_name = rs.getString("NAME");
				String db_exist_pass = rs.getString("PASS");

				// 名前が一致するか
				if (db_exist_name.equals(input_name)) {

					// パスワードが一致するか
					if (db_exist_pass.equals(input_pass)) {

						// 登録されているならセッションはnullで次ページへ遷移
						HttpSession session = request.getSession();
						session = request.getSession();
						session.invalidate();

						register_status = true;

					}

				}

			}

			if (register_status == false) {

				// DBに未登録の時の処理
				// 現存するID最大値を取得し、+1を新規登録するユーザーのIDとする

				// sql準備
				sql = "select max(id) as ID from users";
				int grant_id = 0;
				stmt = conn.prepareStatement(sql);
				// selectを実行し、結果票を取得
				rs = stmt.executeQuery();

				while (rs.next()) {
					grant_id = rs.getInt("ID") + 1;

				}

				// ユーザー情報をDBに登録
				// 登録に成功したかの判定に使うためuserインスタンスをセッションスコープに保存

				// sql準備
				sql = "insert into users(name,pass,id) values (?,?,?)";
				stmt = conn.prepareStatement(sql);

				System.out.println(user.getName());
				System.out.println(user.getPass());
				System.out.println(grant_id);

				// ?にセット
				stmt.setString(1, user.getName());
				stmt.setString(2, user.getPass());
				stmt.setInt(3, grant_id);

				System.out.println("ここまでいきてる");
				// sql実行
				int num = stmt.executeUpdate();
				System.out.println("すでにしんでる");

				// セッションに保存
				HttpSession session = request.getSession();
				user.setId(String.valueOf(grant_id));
				System.out.println("登録完了" + user.getId());
				session.setAttribute("User", user);

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

		// ログイン結果画面にフォワード
		RequestDispatcher dispacher = request.getRequestDispatcher("/user/registerUserResult.jsp");
		dispacher.forward(request, response);

	}
}