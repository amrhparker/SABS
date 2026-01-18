package controller;

import dao.AppointmentDao;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/totalAppointments")
public class TotalAppointmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        AppointmentDao dao = new AppointmentDao();
        int total = dao.getTotalAppointments();

        response.setContentType("text/plain");
        response.getWriter().print(total);
    }
}
