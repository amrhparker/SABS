package controller;

import dao.AppointmentDao;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/recentAppointments")
public class RecentAppointmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        AppointmentDao dao = new AppointmentDao();
        List<String[]> recent = dao.getRecentAppointments(5);

        response.setContentType("text/html;charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        for (String[] row : recent) {
            sb.append("<tr>");
            for (String cell : row) {
                sb.append("<td>").append(cell).append("</td>");
            }
            sb.append("</tr>");
        }
        response.getWriter().print(sb.toString());
    }
}
