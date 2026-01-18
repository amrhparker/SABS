import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AppointmentServlet")
public class AppointmentServlet extends HttpServlet {

    private final String DB_URL = "jdbc:derby://localhost:1527/SABSdb";
    private final String DB_USER = "app";
    private final String DB_PASS = "app";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action != null && action.equals("list")) {
            listAppointments(response);
        }
    }

    private void listAppointments(HttpServletResponse response) {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "SELECT * FROM appointment ORDER BY appointment_date DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                JsonObject obj = Json.createObjectBuilder()
                        .add("appointmentId", rs.getString("appointment_id"))
                        .add("customerId", rs.getString("customer_id"))
                        .add("date", rs.getString("appointment_date"))
                        .add("time", rs.getString("appointment_time"))
                        .add("service", rs.getString("service"))
                        .add("status", rs.getString("status"))
                        .build();

                arrayBuilder.add(obj);
            }

            con.close();

            PrintWriter out = response.getWriter();
            JsonWriter jsonWriter = Json.createWriter(out);

            jsonWriter.writeArray(arrayBuilder.build());

            jsonWriter.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
