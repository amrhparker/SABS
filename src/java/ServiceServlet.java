import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ServiceServlet")
public class ServiceServlet extends HttpServlet {

    private static final String DB_URL  = "jdbc:derby://localhost:1527/Salonix";
    private static final String DB_USER = "app";
    private static final String DB_PASS = "app";

    private static final int PAGE_SIZE = 5;

    /* ========================= GET ========================= */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            /* ---------- VIEW SINGLE SERVICE (JSON) ---------- */
            if (id != null) {
                res.setContentType("application/json");

                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM APP.SERVICES WHERE SERVICE_ID = ?"
                );
                ps.setInt(1, Integer.parseInt(id));

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JsonObject json = Json.createObjectBuilder()
                            .add("category", rs.getString("CATEGORY"))
                            .add("service", rs.getString("SERVICE_NAME"))
                            .add("price", rs.getDouble("PRICE"))
                            .add("description",
                                 rs.getString("DESCRIPTION") == null
                                     ? "" : rs.getString("DESCRIPTION"))
                            .build();

                    res.getWriter().print(json.toString());
                }
                return;
            }

            /* ---------- PAGINATION ---------- */
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            int offset = (page - 1) * PAGE_SIZE;

            /* ---------- SEARCH ---------- */
            String keyword = req.getParameter("keyword");
            if (keyword == null) keyword = "";
            keyword = keyword.trim();

            boolean isNumeric = keyword.matches("\\d+(\\.\\d+)?");

            /* ---------- COUNT ---------- */
            PreparedStatement countPs;

            if (isNumeric) {
                double value = Double.parseDouble(keyword);

                countPs = con.prepareStatement(
                        "SELECT COUNT(*) FROM APP.SERVICES " +
                        "WHERE SERVICE_ID = ? OR PRICE BETWEEN ? AND ?"
                );
                countPs.setInt(1, (int) value);
                countPs.setDouble(2, value - 0.01);
                countPs.setDouble(3, value + 0.01);

            } else {
                String kw = "%" + keyword.toLowerCase() + "%";

                countPs = con.prepareStatement(
                        "SELECT COUNT(*) FROM APP.SERVICES " +
                        "WHERE LOWER(CATEGORY) LIKE ? " +
                        "OR LOWER(SERVICE_NAME) LIKE ? " +
                        "OR LOWER(DESCRIPTION) LIKE ?"
                );
                countPs.setString(1, kw);
                countPs.setString(2, kw);
                countPs.setString(3, kw);
            }

            ResultSet countRs = countPs.executeQuery();
            int totalRecords = 0;
            if (countRs.next()) totalRecords = countRs.getInt(1);

            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

            /* ---------- DATA QUERY ---------- */
            PreparedStatement ps;

            if (isNumeric) {
                double value = Double.parseDouble(keyword);

                ps = con.prepareStatement(
                        "SELECT * FROM APP.SERVICES " +
                        "WHERE SERVICE_ID = ? OR PRICE BETWEEN ? AND ? " +
                        "ORDER BY SERVICE_ID DESC " +
                        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                );
                ps.setInt(1, (int) value);
                ps.setDouble(2, value - 0.01);
                ps.setDouble(3, value + 0.01);
                ps.setInt(4, offset);
                ps.setInt(5, PAGE_SIZE);

            } else {
                String kw = "%" + keyword.toLowerCase() + "%";

                ps = con.prepareStatement(
                        "SELECT * FROM APP.SERVICES " +
                        "WHERE LOWER(CATEGORY) LIKE ? " +
                        "OR LOWER(SERVICE_NAME) LIKE ? " +
                        "OR LOWER(DESCRIPTION) LIKE ? " +
                        "ORDER BY SERVICE_ID DESC " +
                        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
                );
                ps.setString(1, kw);
                ps.setString(2, kw);
                ps.setString(3, kw);
                ps.setInt(4, offset);
                ps.setInt(5, PAGE_SIZE);
            }

            ResultSet rs = ps.executeQuery();

            /* ---------- HTML OUTPUT ---------- */
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            out.println("<!--TOTAL_PAGES=" + totalPages + "-->");

            int no = offset + 1;
            while (rs.next()) {
                int serviceId = rs.getInt("SERVICE_ID");

                out.println("<tr>");
                out.println("<td>" + no++ + "</td>");
                out.println("<td>" + rs.getString("CATEGORY") + "</td>");
                out.println("<td>" + rs.getString("SERVICE_NAME") + "</td>");
                out.println("<td>RM " + rs.getDouble("PRICE") + "</td>");
                out.println("<td>");
                out.println("<div class='action-buttons'>");
                out.println("<button class='btn-action btn-view' onclick='openViewModal(" + serviceId + ")'>View</button>");
                out.println("<button class='btn-action btn-edit' onclick='openEditModalDirect(" + serviceId + ")'>Edit</button>");
                out.println("<button class='btn-action btn-delete' onclick='deleteService(" + serviceId + ")'>Delete</button>");
                out.println("</div>");
                out.println("</td>");
                out.println("</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ========================= POST ========================= */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String action = req.getParameter("action");

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            if ("add".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO APP.SERVICES VALUES (DEFAULT,?,?,?,?)"
                );
                ps.setString(1, req.getParameter("category"));
                ps.setString(2, req.getParameter("service"));
                ps.setDouble(3, Double.parseDouble(req.getParameter("price")));
                ps.setString(4, req.getParameter("description"));
                ps.executeUpdate();
            }

            if ("edit".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE APP.SERVICES SET CATEGORY=?, SERVICE_NAME=?, PRICE=?, DESCRIPTION=? " +
                        "WHERE SERVICE_ID=?"
                );
                ps.setString(1, req.getParameter("category"));
                ps.setString(2, req.getParameter("service"));
                ps.setDouble(3, Double.parseDouble(req.getParameter("price")));
                ps.setString(4, req.getParameter("description"));
                ps.setInt(5, Integer.parseInt(req.getParameter("id")));
                ps.executeUpdate();
            }

            if ("delete".equals(action)) {
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM APP.SERVICES WHERE SERVICE_ID=?"
                );
                ps.setInt(1, Integer.parseInt(req.getParameter("id")));
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
