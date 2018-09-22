package error;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/ErrorClose")
public class ErrorClose extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//セッション殺し
		HttpSession session = request.getSession();
		session = request.getSession();
		session.invalidate();
		
		System.out.println("経由しマース");
		
		//トップページへ遷移
		StringBuffer sb = new StringBuffer();
		sb.append("/CalendarJsp/index.jsp");
		response.sendRedirect(new String(sb));

	}

}
