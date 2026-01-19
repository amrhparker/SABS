import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/CustomerServiceServlet")
public class CustomerServiceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain; charset=UTF-8");

        // Database connection parameters (adjust as needed)
        String jdbcURL = "jdbc:derby://localhost:1527/SABSdb";
        String jdbcUser = "app";
        String jdbcPass = "app";

        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass)) {
            String sql = "SELECT CATEGORY, SERVICE_NAME, PRICE, DESCRIPTION FROM SERVICES";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                StringBuilder sb = new StringBuilder();

                // Format: CATEGORY|SERVICE_NAME|PRICE|DESCRIPTION\n per service
                while (rs.next()) {
                    String category = rs.getString("CATEGORY");
                    String name = rs.getString("SERVICE_NAME");
                    double price = rs.getDouble("PRICE");
                    String desc = rs.getString("DESCRIPTION");

                    // Replace newline or pipe characters to avoid breaking format (optional)
                    category = category.replace("|", "/").replace("\n", " ");
                    name = name.replace("|", "/").replace("\n", " ");
                    desc = desc.replace("|", "/").replace("\n", " ");

                    sb.append(category).append("|")
                      .append(name).append("|")
                      .append(price).append("|")
                      .append(desc).append("\n");
                }
                response.getWriter().write(sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("ERROR: " + e.getMessage());
        }
    }
}
