<%-- 
    Document   : list-appointment
    Created on : Jan 17, 2026, 5:29:26 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<title>SALONIX - Appointment List</title>

<meta name="viewport" content="width=device-width, initial-scale=1">

<style>

/* RESET */
*{
    margin:0;
    padding:0;
    box-sizing:border-box;
}

body{
    font-family: Inter, Arial, sans-serif;
    background:#f6f7fb;
}

/* HEADER */

.header{
    background:white;
    padding:18px 30px;
    display:flex;
    justify-content:space-between;
    align-items:center;
    box-shadow:0 3px 12px rgba(0,0,0,.05);
}

.title{
    font-size:18px;
    font-weight:600;
}

/* BUTTON */

.btn-back{
    background:#6366f1;
    color:white;
    border:none;
    padding:8px 14px;
    border-radius:6px;
    cursor:pointer;
}

/* MAIN */

.container{
    max-width:1200px;
    margin:30px auto;
    padding:20px;
    background:white;
    border-radius:12px;
    box-shadow:0 5px 25px rgba(0,0,0,.06);
}

/* TABLE */

table{
    width:100%;
    border-collapse:collapse;
}

th, td{
    padding:14px;
    border-bottom:1px solid #eee;
    font-size:14px;
}

th{
    color:#6b7280;
    text-align:left;
}

/* STATUS BADGES */

.badge{
    padding:6px 12px;
    border-radius:20px;
    font-size:12px;
    font-weight:600;
}

.orange{background:#fff3e0;color:#fb8c00;}
.yellow{background:#fff9c4;color:#f9a825;}
.red{background:#fdecea;color:#e53935;}
.green{background:#e8f5e9;color:#43a047;}

/* RESPONSIVE */

@media(max-width:768px){

.container{
    margin:15px;
    padding:10px;
}

table{
    font-size:13px;
}

}

</style>

</head>

<body>

<!-- HEADER -->

<div class="header">

    <div class="title">
        Appointment List
    </div>

    <button class="btn-back" onclick="goBack()">
        ← Back Dashboard
    </button>

</div>

<!-- TABLE CONTAINER -->

<div class="container">

<table>

<thead>
<tr>
    <th>Appointment ID</th>
    <th>Customer ID</th>
    <th>Date</th>
    <th>Time</th>
    <th>Service</th>
    <th>Status</th>
</tr>
</thead>

<tbody id="appointmentTable">

<!-- DATA WILL LOAD HERE -->

</tbody>

</table>

</div>

<!-- SCRIPT -->

<script>

// BACK BUTTON
function goBack(){
    window.location.href = "staff-dashboard.html";
}


// LOAD APPOINTMENTS
fetch("AppointmentServlet?action=list")
.then(res => res.json())
.then(data => {

    let table = document.getElementById("appointmentTable");
    table.innerHTML = "";

    data.forEach(a => {

        let badgeClass = "";

        if(a.status === "Coming Soon") badgeClass = "orange";
        else if(a.status === "Ongoing") badgeClass = "yellow";
        else if(a.status === "Cancelled") badgeClass = "red";
        else if(a.status === "Completed") badgeClass = "green";

        table.innerHTML += `
            <tr>
                <td>${a.appointmentId}</td>
                <td>${a.customerId}</td>
                <td>${a.date}</td>
                <td>${a.time}</td>
                <td>${a.service}</td>
                <td><span class="badge ${badgeClass}">${a.status}</span></td>
            </tr>
        `;
    });

})
.catch(err => {
    console.error(err);
    alert("Failed to load appointment list");
});

</script>

</body>
</html>
