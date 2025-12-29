// js/staffView.js

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("staffTableBody")) displayList();
    if (document.getElementById("detailStaffID")) displayDetails();
});

function displayList() {
    const staffList = JSON.parse(localStorage.getItem("staffList")) || [];
    const tbody = document.getElementById("staffTableBody");

    tbody.innerHTML = "";

    staffList.forEach(staff => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${staff.staffID}</td>
            <td>${staff.staffName}</td>
            <td>${staff.phoneNumber}</td>
            <td>${staff.email}</td>
            <td>${staff.adminID ? "Admin" : "Staff"}</td>
            <td>
                <a href="staff-details.html?staffID=${staff.staffID}">View</a> |
                <a href="staff-update.html?staffID=${staff.staffID}">Edit</a> |
                <button onclick="deleteStaff('${staff.staffID}')">Delete</button>
            </td>
        `;

        tbody.appendChild(row);
    });
}

function displayDetails() {
    const params = new URLSearchParams(window.location.search);
    const staffID = params.get("staffID");

    const staffList = JSON.parse(localStorage.getItem("staffList")) || [];
    const staff = staffList.find(s => s.staffID === staffID);

    if (!staff) return;

    detailStaffID.textContent = staff.staffID;
    detailStaffName.textContent = staff.staffName;
    detailStaffIC.textContent = staff.staffICNumber;
    detailPhone.textContent = staff.phoneNumber;
    detailEmail.textContent = staff.email;
    detailRole.textContent = staff.adminID ? "Admin" : "Staff";

    document.getElementById("editStaffBtn").href =
        `staff-update.html?staffID=${staff.staffID}`;

    document.getElementById("deleteStaffBtn").onclick =
        () => deleteStaff(staff.staffID);
}
