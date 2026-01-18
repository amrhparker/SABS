<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, db.DBConnection" %>

<%
    if (session == null || session.getAttribute("customerId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Integer customerId = (Integer) session.getAttribute("customerId");
%>

<%
    String customerName = "Unknown";

    Connection connCustomer = null;
    PreparedStatement psCustomer = null;
    ResultSet rsCustomer = null;

    try {
        connCustomer = DBConnection.getConnection();
        psCustomer = connCustomer.prepareStatement(
            "SELECT name FROM customer WHERE customer_id = ?"
        );
        psCustomer.setInt(1, customerId);
        rsCustomer = psCustomer.executeQuery();

        if (rsCustomer.next()) {
            customerName = rsCustomer.getString("customer_name");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rsCustomer != null) rsCustomer.close(); } catch (Exception ignored) {}
        try { if (psCustomer != null) psCustomer.close(); } catch (Exception ignored) {}
        try { if (connCustomer != null) connCustomer.close(); } catch (Exception ignored) {}
    }
%>

<%
    String step = request.getParameter("step");
    String selectedAppointmentId = request.getParameter("appointmentId");

    String apptService = "";
    String apptDate = "";
    String apptTime = "";

    if ("2".equals(step) && selectedAppointmentId != null) {
        Connection conn2 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;

        try {
            conn2 = DBConnection.getConnection();
            ps2 = conn2.prepareStatement(
                    "SELECT a.APPOINTMENT_DATETIME, s.SERVICE_NAME "
                    + "FROM APPOINTMENT a JOIN SERVICES s ON a.SERVICE_ID = s.SERVICE_ID "
                    + "WHERE a.APPOINTMENT_ID = ? AND a.CUSTOMER_ID = ?"
            );
            ps2.setString(1, selectedAppointmentId);
            ps2.setInt(2, customerId);
            rs2 = ps2.executeQuery();

            if (rs2.next()) {
                Timestamp ts = rs2.getTimestamp("APPOINTMENT_DATETIME");
                apptService = rs2.getString("SERVICE_NAME");
                apptDate = new java.text.SimpleDateFormat("dd MMM yyyy").format(ts);
                apptTime = new java.text.SimpleDateFormat("hh:mm a").format(ts);
            }

            // store for servlet
            session.setAttribute("cancelAppointmentId", selectedAppointmentId);

        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (ps2 != null) {
                ps2.close();
            }
            if (conn2 != null) {
                conn2.close();
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>SALONIX</title>

        <!-- Main site styles -->
        <link rel="stylesheet" href="cust_style.css">

        <link href="https://fonts.googleapis.com/css2?family=Poppins&family=Jost&family=Inter&family=Montserrat&display=swap" rel="stylesheet">
    </head>
    <body>

        <!-- ================= CANCEL APPOINTMENT PAGE ================= -->
        <div id="cancelPage" class="page active">

            <!-- Header -->
            <div class="main-header">
                <div class="nav-container">
                    <img src="https://api.builder.io/api/v1/image/assets/TEMP/db3e61846a007f94950a5874b027025362939df5?width=200"
                         alt="SALONIX Logo" class="site-logo">

                    <div class="nav-links">
                        <a class="nav-link inactive" onclick="location.href='booking.jsp'">Book Appointment</a>
                        <a class="nav-link" style="color: #000;" onclick="location.href='cancel.jsp'">List of Appointments</a>
                        <a class="nav-link inactive" onclick="location.href='services.jsp'">Service</a>
                        <a class="nav-link inactive" onclick="location.href='story.jsp'">Our story</a>
                    </div>

                    <div class="nav-buttons">
                        <button class="nav-btn home" onclick="location.href='index.jsp'">H O M E</button>
                        <button class="nav-btn logout" onclick="location.href='login.jsp'">LOG OUT</button>
                    </div>
                </div>
                <div class="nav-line dark"></div>
            </div>

            <!-- Hero Section -->
            <div style="background: #FDE4DB; padding: 160px 20px 60px; text-align: center;">
                <h1 class="story-title">List of Appointments</h1>
                <img src="https://api.builder.io/api/v1/image/assets/TEMP/5252448fbceb81ccb72274cd5c954da62deea0d3?width=1080"
                     alt="Salon Products"
                     style="max-width: 540px; height: auto; margin: 40px auto; display: block; box-shadow: 0 19px 34px rgba(0, 0, 0, 0.34);">
            </div>

            <!-- Cancel Form Container -->
            <div style="padding: 80px 20px; background: #FFF;">
                <div style="max-width: 1326px; margin: 0 auto; background: #FFF; border-radius: 26px;
                    box-shadow: 14px 14px 21px rgba(0, 0, 0, 0.25);
                    display: grid; grid-template-columns: 444px 1fr; min-height: 668px;">

                    <!-- Sidebar -->
                    <div style="background: #FDE4DB; padding: 3rem 2rem;">
                        <div class="cancel-step active"
                             style="background: #000; color: #FFF; border-radius: 8px;
                            padding: 1.75rem; text-align: center; font-size: 18px; font-weight: 500;">
                            List of Appointments
                        </div>
                    </div>

                    <!-- Form Content -->
                    <div style="padding: 3rem; overflow-y: auto;">

                        <!-- STEP 1 -->
                        <div class="cancel-form-section" data-section="1" style="<%= "2".equals(step) || "3".equals(step) ? "display:none;" : ""%>">


                            <label style="font-size: 18px; font-weight: 500;">
                                List of Appointments *
                            </label>

                            <div style="margin-top: 2rem; overflow-x: auto;">
                                <table class="modern-table">
                                    <thead>
                                        <tr>
                                            <th>No.</th>
                                            <th>Appointment ID</th>
                                            <th>Date | Time</th>
                                            <th>Service</th>
                                            <th>Status</th>
                                            <th style="text-align:center;">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            Connection conn = null;
                                            PreparedStatement ps = null;
                                            ResultSet rs = null;
                                            int count = 1;

                                            try {
                                                conn = DBConnection.getConnection();

                                                String sql
                                                        = "SELECT a.APPOINTMENT_ID, a.APPOINTMENT_DATETIME, a.STATUS, s.SERVICE_NAME "
                                                        + "FROM APPOINTMENT a "
                                                        + "JOIN SERVICES s ON a.SERVICE_ID = s.SERVICE_ID "
                                                        + "WHERE a.CUSTOMER_ID = ? "
                                                        + "ORDER BY a.APPOINTMENT_DATETIME DESC";

                                                ps = conn.prepareStatement(sql);
                                                ps.setInt(1, customerId);
                                                rs = ps.executeQuery();

                                                while (rs.next()) {
                                                    Timestamp dateTime = rs.getTimestamp("APPOINTMENT_DATETIME");
                                        %>
                                        <tr>
                                            <td><%= count++%></td>
                                            <td><%= rs.getString("APPOINTMENT_ID")%></td>
                                            <td>
                                                <%= new java.text.SimpleDateFormat("dd MMM yyyy | hh:mm a").format(dateTime)%>
                                            </td>
                                            <td><%= rs.getString("SERVICE_NAME")%></td>
                                            <td>
                                                <%
                                                    String status = rs.getString("STATUS");
                                                    String statusClass = "status-coming";

                                                    if ("ONGOING".equalsIgnoreCase(status)) {
                                                        statusClass = "status-ongoing";
                                                    }else if ("COMING SOON".equalsIgnoreCase(status)) {
                                                        statusClass = "status-ongoing";
                                                    } else if ("COMPLETED".equalsIgnoreCase(status)) {
                                                        statusClass = "status-completed";
                                                    } else if ("CANCELLED".equalsIgnoreCase(status)) {
                                                        statusClass = "status-cancelled";
                                                    }
                                                %>

                                                <span class="status-badge <%= statusClass%>">
                                                    <%= status%>
                                                </span>


                                            </td>
                                            <td style="text-align:center;">
                                                <form action="cancel.jsp" method="get">
                                                    <input type="hidden" name="step" value="2">
                                                    <input type="hidden" name="appointmentId"
                                                           value="<%= rs.getString("APPOINTMENT_ID")%>">
                                                    <button type="submit"
                                                            class="cancel-btn"
                                                            <%= "CANCELLED".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status) ? "disabled" : ""%>>
                                                        Cancel
                                                    </button>
                                                </form>

                                            </td>
                                        </tr>
                                        <%
                                            }

                                            if (count == 1) {
                                        %>
                                        <tr>
                                            <td colspan="6" style="text-align:center;">
                                                No appointments found
                                            </td>
                                        </tr>
                                        <%
                                            }

                                        } catch (Exception e) {
                                        %>
                                        <tr>
                                            <td colspan="5" style="color:red;">
                                                Error loading appointments
                                            </td>
                                        </tr>
                                        <%
                                                e.printStackTrace();
                                            } finally {
                                                if (rs != null) {
                                                    rs.close();
                                                }
                                                if (ps != null) {
                                                    ps.close();
                                                }
                                                if (conn != null) {
                                                    conn.close();
                                                }
                                            }
                                        %>
                                    </tbody>

                                </table>
                            </div>

                            <div style="display:flex; justify-content:flex-end; margin-top:3rem;">
                                <button onclick="location.href='index.jsp'"
                                        style="padding:1rem 2.5rem; background:#000; color:#FFF; border:none; border-radius:8px;">
                                    Back
                                </button>
                            </div>
                        </div>

                        <!-- STEP 2 -->
                        <div class="cancel-form-section" data-section="2" style="<%= "2".equals(step) ? "" : "display:none;"%>">

                            <h3>Appointment Details</h3>

                            <p><strong>Name:</strong> <%= customerName%></p>
                            <p><strong>Service:</strong> <%= apptService%></p>
                            <p><strong>Date:</strong> <%= apptDate%></p>
                            <p><strong>Time:</strong> <%= apptTime%></p>


                            <div style="display:flex; justify-content:space-between; margin-top:3rem;">
                                <button class="black-btn" onclick="location.href='cancel.jsp'">
                                    Back
                                </button>


                                <form action="CancelAppointmentServlet" method="post">
                                    <input type="hidden" name="appointmentId" value="<%= (String) session.getAttribute("cancelAppointmentId")%>">

                                    <button type="submit"
                                            style="background:#E34D50;color:#FFF;border:none;padding:10px 20px;border-radius:6px;">
                                        Confirm Cancellation
                                    </button>
                                </form>


                            </div>
                        </div>

                        <!-- STEP 3 -->
                        <div class="cancel-form-section"
                             data-section="3"
                             style="<%= "3".equals(request.getParameter("step")) ? "" : "display:none;"%>; text-align:center;">


                            <h2>YOUR APPOINTMENT HAS BEEN CANCELED</h2>
                            <button onclick="location.href = 'index.jsp'"
                                    style="margin-top:2rem;padding:1rem 2.5rem;background:#000;color:#FFF;border:none;border-radius:8px;">
                                HOME
                            </button>
                        </div>

                    </div>
                </div>
            </div>

            <!-- Footer -->
            <div class="site-footer">
                <div class="footer-content">
                    <img src="https://api.builder.io/api/v1/image/assets/TEMP/66f64fa4e40cc7a57c1939f7f444c578ae8d0a99?width=440" alt="SALONIX Logo" class="footer-logo">
                    <div class="footer-links">
                        <a href="index.jsp" class="footer-link">Home</a>
                        <a href="booking.jsp" class="footer-link">Book Appointment</a>
                        <a href="cancel.jsp" class="footer-link">List of Appointments</a>
                        <a href="services.jsp" class="footer-link">Service</a>
                        <a href="story.jsp" class="footer-link">Our story</a>
                    </div>
                </div>
                <div class="footer-bottom">
                    <div class="copyright">2025 Salon All rights reserved</div>
                </div>
            </div>

        </div>
        <!-- ================= END CANCEL PAGE ================= -->

        <script>
            function changeCancelStep(step) {
                const sections = document.querySelectorAll('.cancel-form-section');
                sections.forEach((sec, index) => {
                    sec.style.display = (index + 1 === step) ? 'block' : 'none';
                });
            }

            function viewCancelAppointment(id) {
                changeCancelStep(2);
            }

            function confirmCancellation() {
                changeCancelStep(3);
            }
        </script>

    </body>
</html>