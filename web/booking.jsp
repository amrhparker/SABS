<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, db.DBConnection" %>
<%@ page import="java.util.*" %>

<%
    if (session == null || session.getAttribute("customerId") == null) {
        response.sendRedirect("login.html");
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
            "SELECT customer_name FROM customer WHERE customer_id = ?"
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
    Boolean bookingSuccess =
        (Boolean) request.getAttribute("bookingSuccess");
%>


<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>SALONIX</title>

        <!-- Main site styles -->
        <link rel="stylesheet" href="cust_style.css">


        <!-- Fonts if you already had them -->
        <link href="https://fonts.googleapis.com/css2?family=Poppins&family=Jost&family=Inter&family=Montserrat&display=swap" rel="stylesheet">
    </head>
    <body>

        <!-- ================= BOOK APPOINTMENT PAGE ================= -->
        <div id="bookingPage" class="page active">

            <!-- Header -->
            <div class="main-header">
                <div class="nav-container">
                    <img src="https://api.builder.io/api/v1/image/assets/TEMP/db3e61846a007f94950a5874b027025362939df5?width=200"
                         alt="SALONIX Logo" class="site-logo">

                    <div class="nav-links">
                        <a class="nav-link" style="color: #000;" onclick="location.href='booking.jsp'">Book Appointment</a>
                        <a class="nav-link inactive" onclick="location.href='cancel.jsp'">List of Appointments</a>
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
                <h1 class="story-title">Book Appointment</h1>
                <img src="https://api.builder.io/api/v1/image/assets/TEMP/5252448fbceb81ccb72274cd5c954da62deea0d3?width=1080"
                     alt="Salon Products"
                     style="max-width: 540px; height: auto; margin: 40px auto; display: block; box-shadow: 0 19px 34px rgba(0, 0, 0, 0.34);">
            </div>

            <!-- Booking Form Container -->
            <div style="padding: 80px 20px; background: #FFF;">
                <div style="max-width: 1326px; margin: 0 auto; background: #FFF; border-radius: 26px; box-shadow: 14px 14px 21px rgba(0, 0, 0, 0.25); display: grid; grid-template-columns: 444px 1fr;">

                    <!-- Sidebar Steps -->
                    <div style="background: #FDE4DB; padding: 3rem 2rem; display: flex; flex-direction: column; gap: 2rem;">
                        <div class="booking-step" data-step="1"
                             onclick="changeBookingStep(1)"
                             style="background: #DEB3A4; color: #000; border-radius: 8px; padding: 1.75rem; text-align: center; font-size: 18px; font-weight: 500; cursor: pointer;">
                            Service Selection
                        </div>
                        <div class="booking-step" data-step="2"
                             onclick="changeBookingStep(2)"
                             style="background: #DEB3A4; color: #000; border-radius: 8px; padding: 1.75rem; text-align: center; font-size: 18px; font-weight: 500; cursor: pointer;">
                            Date & Time
                        </div>
                        <div class="booking-step" data-step="3"
                             onclick="changeBookingStep(3)"
                             style="background: #DEB3A4; color: #000; border-radius: 8px; padding: 1.75rem; text-align: center; font-size: 18px; font-weight: 500; cursor: pointer;">
                            Confirmation
                        </div>
                    </div>

                    <!-- Form Content -->
                    <div style="padding: 3rem; overflow-y: auto;">

                        <!-- Step 1 -->
                        <div class="booking-form-section active" data-section="1">
                            <label style="display: block; font-size: 18px; font-weight: 500;">Service *</label>
                            <select id="bookingService"
                                    style="width: 100%; padding: 1rem; border-radius: 8px; margin-bottom: 2rem;"
                                    required>

                                <%
                                    Connection c = null;
                                    PreparedStatement ps = null;
                                    ResultSet r = null;

                                    try {
                                        c = DBConnection.getConnection();
                                        ps = c.prepareStatement(
                                                "SELECT service_id, service_name FROM service ORDER BY service_id"
                                        );
                                        r = ps.executeQuery();

                                        while (r.next()) {
                                            String serviceId = r.getString("service_id");
                                            String serviceName = r.getString("service_name");
                                %>
                                <option value="<%= serviceId%>" data-name="<%= serviceName%>">
                                    <%= serviceName%>
                                </option>

                                <%
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                %>
                                <option disabled>Error loading services</option>
                                <%
                                    } finally {
                                        if (r != null) {
                                            try {
                                                r.close();
                                            } catch (Exception ignored) {
                                            }
                                        }
                                        if (ps != null) {
                                            try {
                                                ps.close();
                                            } catch (Exception ignored) {
                                            }
                                        }
                                        if (c != null) {
                                            try {
                                                c.close();
                                            } catch (Exception ignored) {
                                            }
                                        }
                                    }
                                %>

                            </select>



                            <div style="display: flex; justify-content: flex-end;">
                                <button onclick="changeBookingStep(2)" style="padding: 1rem 2.5rem; border: none; border-radius: 8px; background: #000; color: #FFF; font-family: 'Jost', sans-serif; font-size: 18px; font-weight: 700; cursor: pointer; min-width: 189px;">
                                    Next
                                </button>
                            </div>
                        </div>

                        <!-- Step 2 -->
                        <div class="booking-form-section" data-section="2" style="display:none;">
                            <label style="font-size: 18px; font-weight: 500;">Date & Time *</label>

                            <div style="margin-top: 2rem; display: grid; grid-template-columns: 1fr 1fr; gap: 2rem;">
                                <!-- Date Picker -->
                                <input
                                    id="bookingDate"
                                    type="date"
                                    value="<%= java.time.LocalDate.now()%>"
                                    min="<%= java.time.LocalDate.now()%>"
                                    style="
                                    padding: 1rem;
                                    border: 1px solid #ADADAD;
                                    border-radius: 9px;
                                    font-family: 'Poppins', sans-serif;
                                    font-size: 14px;
                                    "
                                    required
                                    >

                                <!-- Hour-Only Time Selector -->
                                <select
                                    id="bookingTime"
                                    style="
                                        padding: 1rem;
                                        border: 1px solid #ADADAD;
                                        border-radius: 9px;
                                        font-family: 'Poppins', sans-serif;
                                        font-size: 14px;
                                    "
                                    required
                                    ></select>

                            </div>
                            <script>
                                    (function () {
                                        const timeSelect = document.getElementById("bookingTime");

                                        const OPEN = 8;   // 8 AM
                                        const CLOSE = 24; // 12 AM (midnight)

                                        for (let h = OPEN; h <= CLOSE; h++) {
                                            const option = document.createElement("option");

                                            // value (24h format for backend)
                                            let valueHour = (h === 24) ? "00" : String(h).padStart(2, "0");
                                            option.value = valueHour + ":00";

                                            // display (12h format for user)
                                            let displayHour;
                                            if (h === 24) {
                                                displayHour = "12:00 AM";
                                            } else {
                                                displayHour = (h % 12 || 12) + ":00 " + (h < 12 ? "AM" : "PM");
                                            }

                                            option.textContent = displayHour;
                                            timeSelect.appendChild(option);
                                        }
                                    })();
                            </script>

                            <div style="display: flex; justify-content: space-between; margin-top: 3rem;">
                                <button onclick="changeBookingStep(1)" style="padding: 1rem 2.5rem; border: none; border-radius: 8px; background: #000; color: #FFF; font-family: 'Jost', sans-serif; font-size: 18px; font-weight: 700; cursor: pointer; min-width: 189px;">Back</button>
                                <button onclick="changeBookingStep(3)" style="padding: 1rem 2.5rem; border: none; border-radius: 8px; background: #000; color: #FFF; font-family: 'Jost', sans-serif; font-size: 18px; font-weight: 700; cursor: pointer; min-width: 189px;">Next</button>
                            </div>
                        </div>

                        <!-- Step 3 -->
                        
                        <div class="booking-form-section" data-section="3" style="display:none;">
                            <form action="BookingAppointmentServlet" method="post">
                            <h3>Appointment Details</h3>

                            <p><strong>Customer:</strong> <%= customerName%></p>
                            <p><strong>Service:</strong> <span id="confirmServiceText"></span></p>
                            <p><strong>Date:</strong> <span id="confirmDateText"></span></p>
                            <p><strong>Time:</strong> <span id="confirmTimeText"></span></p>
                            
                            <input type="hidden" name="service" id="confirmServiceInput">
                            <input type="hidden" name="date" id="confirmDateInput">
                            <input type="hidden" name="time" id="confirmTimeInput">


                            <div style="display: flex; justify-content: space-between; margin-top: 3rem;">
                                <button onclick="changeBookingStep(2)" style="padding: 1rem 2.5rem; border: none; border-radius: 8px; background: #E34D50; color: #FFF; font-family: 'Jost', sans-serif; font-size: 18px; font-weight: 700; cursor: pointer; min-width: 189px;">Back</button>
                                <button type="submit" style="padding: 1rem 2.5rem; border: none; border-radius: 8px; background: #2DA831; color: #FFF; font-family: 'Jost', sans-serif; font-size: 18px; font-weight: 700; cursor: pointer; min-width: 189px;">Confirm</button>
                            </div>
                            </form>

                        </div>

                        <!-- Step 4 -->
                        <div class="booking-form-section" data-section="4" style="display:none; text-align:center;">
                            <div style="font-size: 18px; font-weight: 500; letter-spacing: 3.6px; color: #000; margin-bottom: 1.5rem;">YOUR APPOINTMENT HAS BEEN BOOKED!</div>
                            <div style="font-size: 18px; font-weight: 400; color: #000;">Please be on time, we will be waiting for you :)</div>
                            <div style="display: flex; gap: 1.5rem; justify-content: center; margin-top: 3rem;">
                                <button onclick="location.href = 'index.jsp'" style="padding: 1rem 2.5rem; border: none; border-radius: 8px; background: #000; color: #FFF; font-family: 'Jost', sans-serif; font-size: 18px; font-weight: 700; cursor: pointer; min-width: 189px;">H O M E</button>
                            </div>
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
        <!-- ================= END BOOKING PAGE ================= -->


        <!-- ================= STEP HANDLER ================= -->
        <script>
        function changeBookingStep(step) {
            const sections = document.querySelectorAll('.booking-form-section');
            sections.forEach((sec, i) => {
                sec.style.display = (i + 1 === step) ? 'block' : 'none';
            });

            if (step === 3) {
                const serviceSelect = document.getElementById("bookingService");
                const selectedOption = serviceSelect.options[serviceSelect.selectedIndex];

                const serviceId = selectedOption.value;
                const serviceName = selectedOption.textContent;
                const date = bookingDate.value;
                const time = bookingTime.value;

                document.getElementById('confirmServiceText').textContent = serviceName;
                document.getElementById('confirmDateText').textContent =
                    new Date(date).toLocaleDateString('en-GB', {
                        day: 'numeric', month: 'long', year: 'numeric'
                    });

                const h = parseInt(time.split(':')[0]);
                document.getElementById('confirmTimeText').textContent =
                    (h % 12 || 12) + ':00 ' + (h < 12 ? 'AM' : 'PM');

                document.getElementById('confirmServiceInput').value = serviceId;
                document.getElementById('confirmDateInput').value = date;
                document.getElementById('confirmTimeInput').value = time;
            }
        }

        </script>
        <script>
            <% if (Boolean.TRUE.equals(bookingSuccess)) { %>
                changeBookingStep(4);
            <% }%>
        </script>


    </body>
</html>