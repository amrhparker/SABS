
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ServiceServlet")
public class ServiceServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:derby://localhost:1527/Salonix";
    private static final String DB_USER = "app";
    private static final String DB_PASS = "app";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String id = req.getParameter("id");
        res.setCharacterEncoding("UTF-8");

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            if (id != null) {
                res.setContentType("application/json");
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM APP.SERVICES WHERE SERVICE_ID=?");
                ps.setInt(1, Integer.parseInt(id));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    res.getWriter().print(
                        "{ \"category\":\"" + rs.getString("CATEGORY") +
                        "\", \"service\":\"" + rs.getString("SERVICE_NAME") +
                        "\", \"price\":" + rs.getDouble("PRICE") +
                        ", \"description\":\"" + rs.getString("DESCRIPTION") + "\" }"
                    );
                }
            } else {
                res.setContentType("text/html");
                ResultSet rs = con.createStatement()
                        .executeQuery("SELECT * FROM APP.SERVICES");

                int no = 1;
                PrintWriter out = res.getWriter();
                while (rs.next()) {
                    int serviceId = rs.getInt("SERVICE_ID");

out.println("<tr>");
out.println("<td>" + no++ + "</td>"); // No
out.println("<td>" + rs.getString("CATEGORY") + "</td>"); // Category
out.println("<td>" + rs.getString("SERVICE_NAME") + "</td>"); // Service
out.println("<td>RM " + rs.getDouble("PRICE") + "</td>"); // ✅ Price
out.println("<td>"); // Action
out.println("  <div class='action-buttons'>");
out.println("    <button class='btn-action btn-view' onclick='openViewModal(" + serviceId + ")'>View</button>");
out.println("    <button class='btn-action btn-edit' onclick='openEditModalDirect(" + serviceId + ")'>Edit</button>");
out.println("    <button class='btn-action btn-delete' onclick='deleteService(" + serviceId + ")'>Delete</button>");
out.println("  </div>");
out.println("</td>");
out.println("</tr>");


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String action = req.getParameter("action");

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            if ("add".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO APP.SERVICES VALUES (DEFAULT,?,?,?,?)");
                ps.setString(1, req.getParameter("category"));
                ps.setString(2, req.getParameter("service"));
                ps.setDouble(3, Double.parseDouble(req.getParameter("price")));
                ps.setString(4, req.getParameter("description"));
                ps.executeUpdate();
            }

            if ("edit".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE APP.SERVICES SET CATEGORY=?,SERVICE_NAME=?,PRICE=?,DESCRIPTION=? WHERE SERVICE_ID=?");
                ps.setString(1, req.getParameter("category"));
                ps.setString(2, req.getParameter("service"));
                ps.setDouble(3, Double.parseDouble(req.getParameter("price")));
                ps.setString(4, req.getParameter("description"));
                ps.setInt(5, Integer.parseInt(req.getParameter("id")));
                ps.executeUpdate();
            }

            if ("delete".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM APP.SERVICES WHERE SERVICE_ID=?");
                ps.setInt(1, Integer.parseInt(req.getParameter("id")));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
