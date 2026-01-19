<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>SALONIX - Admin Appointments</title>

        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Montserrat:wght@500;700&family=Jost:wght@700&family=Poppins:wght@300;400&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="admin_1.css">
    </head>
    <body>

        <div class="admin-layout">

            <div class="admin-sidebar">
                <div class="sidebar-logo">
                    <img src="https://api.builder.io/api/v1/image/assets/TEMP/db3e61846a007f94950a5874b027025362939df5?width=200" alt="SALONIX">
                </div>
                <div class="sidebar-menu">
                    <a class="menu-item" href="admin-dashboard.html">
                        <svg viewBox="0 0 24 24" fill="none">
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M3 5.5C3 4.11929 4.11929 3 5.5 3H8.5C9.88071 3 11 4.11929 11 5.5V8.5C11 9.88071 9.88071 11 8.5 11H5.5C4.11929 11 3 9.88071 3 8.5V5.5ZM5.5 5H8.5C8.77614 5 9 5.22386 9 5.5V8.5C9 8.77614 8.77614 9 8.5 9H5.5C5.22386 9 5 8.77614 5 8.5V5.5C5 5.22386 5.22386 5 5.5 5Z" fill="black" fill-opacity="0.6"/>
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M13 5.5C13 4.11929 14.1193 3 15.5 3H18.5C19.8807 3 21 4.11929 21 5.5V8.5C21 9.88071 19.8807 11 18.5 11H15.5C14.1193 11 13 9.88071 13 8.5V5.5ZM15.5 5H18.5C18.7761 5 19 5.22386 19 5.5V8.5C19 8.77614 18.7761 9 18.5 9H15.5C15.2239 9 15 8.77614 15 8.5V5.5C15 5.22386 15.2239 5 15.5 5Z" fill="black" fill-opacity="0.6"/>
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M15.5 13C14.1193 13 13 14.1193 13 15.5V18.5C13 19.8807 14.1193 21 15.5 21H18.5C19.8807 21 21 19.8807 21 18.5V15.5C21 14.1193 19.8807 13 18.5 13H15.5ZM18.5 15H15.5C15.2239 15 15 15.2239 15 15.5V18.5C15 18.7761 15.2239 19 15.5 19H18.5C18.7761 19 19 18.7761 19 18.5V15.5C19 15.2239 18.7761 15 18.5 15Z" fill="black" fill-opacity="0.6"/>
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M3 15.5C3 14.1193 4.11929 13 5.5 13H8.5C9.88071 13 11 14.1193 11 15.5V18.5C11 19.8807 9.88071 21 8.5 21H5.5C4.11929 21 3 19.8807 3 18.5V15.5ZM5.5 15H8.5C8.77614 15 9 15.2239 9 15.5V18.5C9 18.7761 8.77614 19 8.5 19H5.5C5.22386 19 5 18.7761 5 18.5V15.5C5 15.2239 5.22386 15 5.5 15Z" fill="black" fill-opacity="0.6"/>
                        </svg>
                        <span>Dashboard</span>
                    </a>
                    <a class="menu-item active" href="admin-appointments.jsp">
                        <svg viewBox="0 0 24 24" fill="none">
                        <path d="M15.9666 12.6975C16.3519 12.3017 16.3433 11.6686 15.9475 11.2834C15.5517 10.8982 14.9186 10.9068 14.5334 11.3025L11.2737 14.6517L9.8755 13.5309C9.44459 13.1854 8.81522 13.2547 8.46979 13.6856C8.12435 14.1166 8.19365 14.7459 8.62457 15.0914L11.0857 17.0643C11.2867 17.2254 11.5771 17.2075 11.7567 17.0229L15.9666 12.6975Z" fill="white"/>
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M8 2C8.55228 2 9 2.44772 9 3H15C15 2.44772 15.4477 2 16 2C16.5523 2 17 2.44772 17 3H18C19.6569 3 21 4.34315 21 6V19C21 20.6569 19.6569 22 18 22H6C4.34315 22 3 20.6569 3 19V6C3 4.34315 4.34315 3 6 3H7C7 2.44772 7.44772 2 8 2ZM19 6V7H5V6C5 5.44772 5.44772 5 6 5H7C7 5.55228 7.44772 6 8 6C8.55228 6 9 5.55228 9 5H15C15 5.55228 15.4477 6 16 6C16.5523 6 17 5.55228 17 5H18C18.5523 5 19 5.44772 19 6ZM5 19V9H19V19C19 19.5523 18.5523 20 18 20H6C5.44772 20 5 19.5523 5 19Z" fill="white"/>
                        </svg>
                        <span>Appointments</span>
                    </a>
                    <a class="menu-item" href="admin-customers.html">
                        <svg viewBox="0 0 24 24" fill="none">
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M14.5 6.5C14.5 8.98528 12.4853 11 10 11C7.51472 11 5.5 8.98528 5.5 6.5C5.5 4.01472 7.51472 2 10 2C12.4853 2 14.5 4.01472 14.5 6.5ZM12.5 6.5C12.5 7.88071 11.3807 9 10 9C8.61929 9 7.5 7.88071 7.5 6.5C7.5 5.11929 8.61929 4 10 4C11.3807 4 12.5 5.11929 12.5 6.5Z" fill="#667085"/>
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M2 18.9231C2 15.0996 5.09957 12 8.92308 12H11.0769C14.9004 12 18 15.0996 18 18.9231C18 20.0701 17.0701 21 15.9231 21H4.07692C2.92987 21 2 20.0701 2 18.9231ZM4 18.9231C4 16.2041 6.20414 14 8.92308 14H11.0769C13.7959 14 16 16.2041 16 18.9231C16 18.9656 15.9656 19 15.9231 19H4.07692C4.03444 19 4 18.9656 4 18.9231Z" fill="#667085"/>
                        <path d="M18.9198 20.0973C18.8164 20.4981 19.0774 21 19.4913 21H19.9231C21.0701 21 22 20.0701 22 18.9231C22 15.0996 18.9004 12 15.0769 12C14.9347 12 14.8829 12.1975 15.0036 12.2727C15.9494 12.8614 16.7705 13.6314 17.4182 14.5343C17.4621 14.5955 17.5187 14.6466 17.5835 14.685C19.0301 15.5424 20 17.1195 20 18.9231C20 18.9656 19.9656 19 19.9231 19H19.4494C19.1985 19 19 19.2106 19 19.4615C19 19.6811 18.9721 18.8941 18.9198 20.0973Z" fill="#667085"/>
                        <path d="M14.919 8.96308C14.974 8.85341 15.0645 8.76601 15.1729 8.70836C15.9624 8.28814 16.5 7.45685 16.5 6.5C16.5 5.54315 15.9624 4.71186 15.1729 4.29164C15.0645 4.23399 14.974 4.14659 14.919 4.03692C14.6396 3.48001 14.2684 2.97712 13.8252 2.5481C13.623 2.35231 13.7185 2 14 2C16.4853 2 18.5 4.01472 18.5 6.5C18.5 8.98528 16.4853 11 14 11C13.7185 11 13.623 10.6477 13.8252 10.4519C14.2684 10.0229 14.6396 9.51999 14.919 8.96308Z" fill="#667085"/>
                        </svg>
                        <span>Customers</span>
                    </a>
                    <a class="menu-item" href = "admin-services.html">
                        <svg viewBox="0 0 24 24" fill="none">
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M16.6608 2C17.6951 2 18.6566 2.53286 19.2048 3.41L21.4103 6.93883C21.5695 7.19353 21.6539 7.48785 21.6539 7.78821C21.6539 8.84672 21.2108 9.80174 20.5 10.4779V18C20.5 20.2091 18.7091 22 16.5 22H7.5C5.29086 22 3.5 20.2091 3.5 18V10.4779C2.78924 9.80167 2.34619 8.84668 2.34619 7.78821C2.34619 7.48785 2.4306 7.19353 2.58979 6.93883L4.79531 3.41C5.34352 2.53286 6.30493 2 7.3393 2H16.6608ZM7.3393 4C6.99451 4 6.67404 4.17762 6.4913 4.47L4.34958 7.89676C4.40557 8.79158 5.14904 9.5 6.05798 9.5H6.23081C7.27165 9.5 8.11542 8.65623 8.11542 7.61538H10.1154C10.1154 8.65623 10.9592 9.5 12 9.5C13.0409 9.5 13.8847 8.65623 13.8847 7.61538H15.8847C15.8847 8.65623 16.7284 9.5 17.7693 9.5H17.9421C18.851 9.5 19.5945 8.79158 19.6505 7.89676L17.5088 4.47C17.326 4.17762 17.0056 4 16.6608 4H7.3393ZM17.9421 11.5C18.1317 11.5 18.318 11.4858 18.5 11.4583V18C18.5 19.1046 17.6046 20 16.5 20H16V16C16 14.3431 14.6569 13 13 13H11C9.34315 13 8 14.3431 8 16V20H7.5C6.39543 20 5.5 19.1046 5.5 18V11.4583C5.682 11.4858 5.86833 11.5 6.05798 11.5H6.23081C7.37546 11.5 8.40448 11.0049 9.11542 10.2172C9.82636 11.0049 10.8554 11.5 12 11.5C13.1447 11.5 14.1737 11.0049 14.8847 10.2172C15.5956 11.0049 16.6246 11.5 17.7693 11.5H17.9421ZM10 20H14V16C14 15.4477 13.5523 15 13 15H11C10.4477 15 10 15.4477 10 16V20Z" fill="#667085"/>
                        </svg>
                        <span>Services</span>
                    </a>
                    <a class="menu-item" href ="admin-staffs.html">
                        <svg viewBox="0 0 24 24" fill="none">
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M14.5 6.5C14.5 8.98528 12.4853 11 10 11C7.51472 11 5.5 8.98528 5.5 6.5C5.5 4.01472 7.51472 2 10 2C12.4853 2 14.5 4.01472 14.5 6.5ZM12.5 6.5C12.5 7.88071 11.3807 9 10 9C8.61929 9 7.5 7.88071 7.5 6.5C7.5 5.11929 8.61929 4 10 4C11.3807 4 12.5 5.11929 12.5 6.5Z" fill="#667085"/>
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M2 18.9231C2 15.0996 5.09957 12 8.92308 12H11.0769C14.9004 12 18 15.0996 18 18.9231C18 20.0701 17.0701 21 15.9231 21H4.07692C2.92987 21 2 20.0701 2 18.9231ZM4 18.9231C4 16.2041 6.20414 14 8.92308 14H11.0769C13.7959 14 16 16.2041 16 18.9231C16 18.9656 15.9656 19 15.9231 19H4.07692C4.03444 19 4 18.9656 4 18.9231Z" fill="#667085"/>
                        <path d="M18.9198 20.0973C18.8164 20.4981 19.0774 21 19.4913 21H19.9231C21.0701 21 22 20.0701 22 18.9231C22 15.0996 18.9004 12 15.0769 12C14.9347 12 14.8829 12.1975 15.0036 12.2727C15.9494 12.8614 16.7705 13.6314 17.4182 14.5343C17.4621 14.5955 17.5187 14.6466 17.5835 14.685C19.0301 15.5424 20 17.1195 20 18.9231C20 18.9656 19.9656 19 19.9231 19H19.4494C19.1985 19 19 19.2106 19 19.4615C19 19.6811 18.9721 18.8941 18.9198 20.0973Z" fill="#667085"/>
                        <path d="M14.919 8.96308C14.974 8.85341 15.0645 8.76601 15.1729 8.70836C15.9624 8.28814 16.5 7.45685 16.5 6.5C16.5 5.54315 15.9624 4.71186 15.1729 4.29164C15.0645 4.23399 14.974 4.14659 14.919 4.03692C14.6396 3.48001 14.2684 2.97712 13.8252 2.5481C13.623 2.35231 13.7185 2 14 2C16.4853 2 18.5 4.01472 18.5 6.5C18.5 8.98528 16.4853 11 14 11C13.7185 11 13.623 10.6477 13.8252 10.4519C14.2684 10.0229 14.6396 9.51999 14.919 8.96308Z" fill="#667085"/>
                        </svg>
                        <span>Staffs</span>
                    </a>
                </div>
            </div>

            <!-- ================= MAIN ================= -->
            <div class="admin-main">

                <!-- ================= TOPBAR ================= -->
                <div class="admin-topbar">
                    <div></div>
                    <div class="admin-user">
                        <div class="user-avatar"></div>
                        <div class="user-info">
                            <div class="user-name" id="adminName"></div>
                            <div class="user-role">Admin</div>
                        </div>
                        <div>
                            <a href="login.html">
                                <img src="images/logoutIcon.png" class="user-avatar">
                            </a>
                        </div>
                    </div>
                </div>

                <!-- ================= CONTENT ================= -->
                <div class="admin-content">

                    <div class="table-container">

                        <div class="table-header">
                            <div class="table-title">List of Appointments</div>

                            <div class="search-box">
                                <input
                                    type="text"
                                    id="searchInput"
                                    placeholder="Search ID..."
                                    onkeyup="handleSearch()"
                                    >
                            </div>
                        </div>

                        <div class="data-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Appointment ID</th>
                                        <th>Customer ID</th>
                                        <th>Date | Time</th>
                                        <th>Service</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>

                                <tbody id="appointmentTableBody">
                                    <tr>
                                        <td colspan="6" style="text-align:center;">
                                            Loading appointments...
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- ================= JS (FINAL & FIXED) ================= -->
        <script>
            fetch(`AdminServlet`)
            .then(res => res.text())
            .then(name => {
                document.getElementById("adminName").textContent = name && name.trim() !== "" ? name : "Admin";
            })
            .catch(() => {
                document.getElementById("adminName").textContent = "Admin";
            });
            // Load appointments (with optional search)
            function loadAppointments(query = '') {
                let url = '<%= request.getContextPath()%>/AppointmentServlet';

                if (query) {
                    url += '?search=' + encodeURIComponent(query);
                }

                fetch(url)
                        .then(response => response.text())
                        .then(html => {
                            document.getElementById('appointmentTableBody').innerHTML = html;
                        })
                        .catch(error => {
                            console.error('Error loading appointments:', error);
                        });
            }

            // Search handler
            function handleSearch() {
                const value = document.getElementById('searchInput').value;
                loadAppointments(value);
            }

            // Accept appointment
            function acceptAppointment(id) {
                if (!confirm("Accept appointment " + id + "?"))
                    return;

                const params = new URLSearchParams();
                params.append("id", id);
                params.append("status", "Ongoing");

                fetch('<%= request.getContextPath()%>/UpdateStatusServlet', {
                    method: "POST",
                    body: params
                })
                        .then(response => {
                            if (response.ok) {
                                loadAppointments();
                            } else {
                                alert("Failed to update appointment.");
                            }
                        })
                        .catch(error => console.error(error));
            }

            // Initial load
            document.addEventListener("DOMContentLoaded", () => {
                loadAppointments();
            });
            
            function completeAppointment(id) {
                if (!confirm("Mark appointment " + id + " as completed?")) return;

                const params = new URLSearchParams();
                params.append("id", id);

                fetch('<%= request.getContextPath() %>/CompleteAppointmentServlet', {
                    method: "POST",
                    body: params
                })
                .then(response => {
                if (response.ok) {
                    loadAppointments();
                } else {
                    alert("Failed to complete appointment.");
                }
                })
                .catch(err => console.error(err));
            }

        </script>

    </body>
</html>
