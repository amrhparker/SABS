// js/staffAuth.js
if (!localStorage.getItem("staffList")) {
    localStorage.setItem("staffList", JSON.stringify([
        {
            staffID: "S001",
            staffName: "Admin User",
            staffICNumber: "900101-01-1234",
            phoneNumber: "0123456789",
            email: "admin@salonix.com",
            password: "admin123",
            adminID: "ADMIN"
        }
    ]));
}


document.getElementById("staffLoginForm")?.addEventListener("submit", function (e) {
    e.preventDefault();
    login();
});

function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const staffList = JSON.parse(localStorage.getItem("staffList")) || [];

    const staff = staffList.find(
        s => s.email === email && s.password === password
    );

    if (!staff) {
        document.getElementById("loginError").textContent = "Invalid email or password.";
        return;
    }

    localStorage.setItem("loggedInStaff", JSON.stringify(staff));

    if (staff.adminID) {
        window.location.href = "staff-list.html";
    } else {
        alert("Login successful (non-admin staff).");
    }
}
