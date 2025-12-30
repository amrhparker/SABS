package controller;

import dao.CustomerDao;
import model.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Customer customer = new Customer();
        customer.setName(request.getParameter("name"));
        customer.setEmail(request.getParameter("email"));
        customer.setPhone(request.getParameter("phone"));
        customer.setPassword(request.getParameter("password"));

        CustomerDao dao = new CustomerDao();

        if (dao.register(customer)) {
            response.sendRedirect("login.html?success=registered");
        } else {
            response.sendRedirect("register.html?error=failed");
        }
    }
}
