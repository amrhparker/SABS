// js/staff.js

// CREATE
document.getElementById("createStaffForm")?.addEventListener("submit", function (e) {
    e.preventDefault();
    create();
});

function create() {
    const staffList = JSON.parse(localStorage.getItem("staffList")) || [];

    const staff = {
        staffID: "S" + Date.now(),
        staffName: staffName.value,
        staffICNumber: staffICNumber.value,
        phoneNumber: phoneNumber.value,
        email: email.value,
        password: password.value,
        adminID: isAdmin.checked ? "ADMIN" : null
    };

    staffList.push(staff);
    localStorage.setItem("staffList", JSON.stringify(staffList));

    window.location.href = "staff-list.html";
}

// UPDATE
document.getElementById("updateStaffForm")?.addEventListener("submit", function (e) {
    e.preventDefault();
    update();
});

function update() {
    const staffList = JSON.parse(localStorage.getItem("staffList")) || [];
    const id = staffID.value;

    const index = staffList.findIndex(s => s.staffID === id);
    if (index === -1) return;

    staffList[index].staffName = staffName.value;
    staffList[index].staffICNumber = staffICNumber.value;
    staffList[index].phoneNumber = phoneNumber.value;
    staffList[index].email = email.value;

    if (password.value) {
        staffList[index].password = password.value;
    }

    staffList[index].adminID = isAdmin.checked ? "ADMIN" : null;

    localStorage.setItem("staffList", JSON.stringify(staffList));
    window.location.href = `staff-details.html?staffID=${id}`;
}

// DELETE
function deleteStaff(staffID) {
    if (!confirm("Are you sure you want to delete this staff?")) return;

    let staffList = JSON.parse(localStorage.getItem("staffList")) || [];
    staffList = staffList.filter(s => s.staffID !== staffID);

    localStorage.setItem("staffList", JSON.stringify(staffList));
    window.location.href = "staff-list.html";
}
