import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/StaffServlet")
public class StaffServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("staffName") != null) {
            String staffName = (String) session.getAttribute("staffName");
            response.getWriter().write(staffName);
        } else {
            response.getWriter().write("");
        }
    }
}
