import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("adminName") != null) {
            String adminName = (String) session.getAttribute("adminName");
            response.getWriter().write(adminName); // return only the name
        } else {
            response.getWriter().write(""); // no admin logged in
        }
    }
}
