package controller;

import dao.AppointmentDao;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/recentAppointments")
public class RecentAppointmentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        AppointmentDao dao = new AppointmentDao();
        List<String[]> recent = dao.getRecentAppointments(10); // Changed to 10 for dashboard

        response.setContentType("text/html;charset=UTF-8");
        
        // If no appointments found
        if (recent.isEmpty()) {
            response.getWriter().print(
                "<tr><td colspan='6' style='text-align:center;'>No appointments found</td></tr>"
            );
            return;
        }

        StringBuilder sb = new StringBuilder();
        
        for (String[] row : recent) {
            String appointmentId = row[0];
            String customerId = row[1];
            String datetimeStr = row[2];
            String serviceName = row[3];
            String status = row[4];
            
            // Format date and time
            String formattedDate = "";
            String formattedTime = "";
            String formattedDateTime = datetimeStr;
            
            // Get status class for styling
            String statusClass = getStatusClass(status);
            
            sb.append("<tr>");
            sb.append("<td>").append(appointmentId).append("</td>");
            sb.append("<td>").append(customerId).append("</td>");
            sb.append("<td>").append(formattedDateTime).append("</td>");
            sb.append("<td>").append(serviceName != null ? serviceName : "N/A").append("</td>");
            
            // Status badge
            sb.append("<td><span class='status-badge ").append(statusClass).append("'>")
              .append(status).append("</span></td>");
            
            
            
            sb.append("</td>");
            sb.append("</tr>");
        }
        response.getWriter().print(sb.toString());
    }
    
    private String getStatusClass(String status) {
        if (status == null) {
            return "";
        }
        switch (status.toUpperCase()) {
            case "COMING SOON":
                return "coming-soon";
            case "ONGOING":
                return "ongoing";
            case "COMPLETED":
                return "completed";
            case "CANCELLED":
                return "cancelled";
            default:
                return "";
        }
    }
}