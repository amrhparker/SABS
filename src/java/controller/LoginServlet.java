package controller;

import dao.CustomerDao;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        CustomerDao dao = new CustomerDao();
        Customer customer = dao.login(email, password);

        if (customer != null) {
            HttpSession session = request.getSession();
            session.setAttribute("customer", customer);
            response.sendRedirect("customerDashboard.jsp");
        } else {
            response.sendRedirect("login.html?error=invalid");
        }
    }
}
