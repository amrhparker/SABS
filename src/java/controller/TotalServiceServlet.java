package controller;

import dao.ServiceDao;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/totalServices")
public class TotalServiceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        ServiceDao dao = new ServiceDao();
        int total = dao.getTotalServices();

        response.setContentType("text/plain");
        response.getWriter().print(total);
    }
}
