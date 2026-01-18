package controller;

import dao.CustomerDao;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/totalCustomers")
public class TotalCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        CustomerDao dao = new CustomerDao();
        int total = dao.getTotalCustomers();

        response.setContentType("text/plain");
        response.getWriter().print(total);
    }
}
