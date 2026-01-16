// staff.js - Updated with your context
let currentStaffIndex = null;
let staffData = [];

// Load staff data on page load
document.addEventListener('DOMContentLoaded', function () {
    loadStaffData();

    // Setup search functionality
    const searchInput = document.querySelector('.search-box input');
    if (searchInput) {
        searchInput.addEventListener('input', function (e) {
            searchStaff(e.target.value);
        });
    }
});

// Load staff data from server
async function loadStaffData() {
    try {
        const response = await fetch('StaffServlet');
        staffData = await response.json();
        populateStaffTable(staffData);
    } catch (error) {
        console.error('Error loading staff data:', error);
    }
}

// Populate staff table
function populateStaffTable(data) {
    const tbody = document.getElementById('staffsTableBody');
    tbody.innerHTML = '';

    data.forEach((staff, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${staff.staffId}</td>
            <td>${staff.name}</td>
            <td>${staff.email}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-view" onclick="openStaffViewModal(${index})">View</button>
                    <button class="btn-action btn-edit" onclick="openStaffEditModal(${index})">Edit</button>
                    <button class="btn-action btn-delete" onclick="openStaffDeleteModal(${index})">Delete</button>
                </div>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Search staff
async function searchStaff(keyword) {
    try {
        const response = await fetch(`StaffServlet?action=search&keyword=${encodeURIComponent(keyword)}`);
        const data = await response.json();
        populateStaffTable(data);
    } catch (error) {
        console.error('Error searching staff:', error);
    }
}

// Open View Modal
async function openStaffViewModal(index) {
    currentStaffIndex = index;
    const staff = staffData[index];

    document.getElementById('staffViewId').textContent = staff.staffId;
    document.getElementById('staffViewName').textContent = staff.name;
    document.getElementById('staffViewNric').textContent = staff.nric;
    document.getElementById('staffViewEmail').textContent = staff.email;
    document.getElementById('staffViewPhone').textContent = staff.phone;
    document.getElementById('staffViewPassword').textContent = '••••••••';

    openModal('staffViewModal');
}

// Open Edit Modal
async function openStaffEditModal(index) {
    currentStaffIndex = index;
    const staff = staffData[index];

    document.getElementById('staffEditName').value = staff.name;
    document.getElementById('staffEditNric').value = staff.nric;
    document.getElementById('staffEditEmail').value = staff.email;
    document.getElementById('staffEditPhone').value = staff.phone;
    document.getElementById('staffEditPassword').value = '';
    document.getElementById('staffEditConfirmPassword').value = '';

    openModal('staffEditModal');
}

// Open Edit Modal from View
function openStaffEditModalFromView() {
    closeModal('staffViewModal');
    openStaffEditModal(currentStaffIndex);
}

// Open Add Modal
async function openStaffAddModal() {
    // Clear form
    document.getElementById('staffAddForm').reset();

    // Generate next staff ID (optional - you can show it or generate on server)
    try {
        const response = await fetch('StaffServlet?action=generateId');
        const data = await response.json();
        // You could show the nextId in the form if you want
        console.log('Next staff ID:', data.nextId);
    } catch (error) {
        console.error('Error generating ID:', error);
    }

    openModal('staffAddModal');
}

// Open Delete Modal
function openStaffDeleteModal(index) {
    currentStaffIndex = index;
    openModal('staffDeleteModal');
}

// Open Delete Modal from View
function openStaffDeleteModalFromView() {
    closeModal('staffViewModal');
    openModal('staffDeleteModal');
}

// Save Edit
async function saveStaffEdit() {
    const staff = staffData[currentStaffIndex];
    const form = document.getElementById('staffEditForm');

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    // Check passwords match
    const password = document.getElementById('staffEditPassword').value;
    const confirmPassword = document.getElementById('staffEditConfirmPassword').value;

    if (password && password !== confirmPassword) {
        alert('Passwords do not match!');
        return;
    }

    // Prepare data
    const formData = new FormData();
    formData.append('action', 'update');
    formData.append('staffId', staff.staffId);
    formData.append('name', document.getElementById('staffEditName').value);
    formData.append('nric', document.getElementById('staffEditNric').value);
    formData.append('email', document.getElementById('staffEditEmail').value);
    formData.append('phone', document.getElementById('staffEditPhone').value);
    formData.append('password', password || staff.password); // Use existing password if not changed

    try {
        const response = await fetch('StaffServlet', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            closeModal('staffEditModal');
            openModal('staffSuccessEditModal');
            loadStaffData(); // Refresh data
        } else {
            alert('Failed to update staff. Please try again.');
        }
    } catch (error) {
        console.error('Error updating staff:', error);
        alert('Error updating staff. Please try again.');
    }
}

// Save Add
async function saveStaffAdd() {
    const form = document.getElementById('staffAddForm');

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    // Check passwords match
    const password = document.getElementById('staffAddPassword').value;
    const confirmPassword = document.getElementById('staffAddConfirmPassword').value;

    if (password !== confirmPassword) {
        alert('Passwords do not match!');
        return;
    }

    // Check if email already exists
    const email = document.getElementById('staffAddEmail').value;
    try {
        const response = await fetch(`StaffServlet?action=checkEmail&email=${encodeURIComponent(email)}`);
        const data = await response.json();
        if (data.exists) {
            alert('Email already exists!');
            return;
        }
    } catch (error) {
        console.error('Error checking email:', error);
    }

    // Check if NRIC already exists
    const nric = document.getElementById('staffAddNric').value;
    try {
        const response = await fetch(`StaffServlet?action=checkNric&nric=${encodeURIComponent(nric)}`);
        const data = await response.json();
        if (data.exists) {
            alert('NRIC already exists!');
            return;
        }
    } catch (error) {
        console.error('Error checking NRIC:', error);
    }

    // Get next staff ID
    let staffId;
    try {
        const response = await fetch('StaffServlet?action=generateId');
        const data = await response.json();
        staffId = data.nextId;
    } catch (error) {
        console.error('Error generating ID:', error);
        alert('Error generating staff ID. Please try again.');
        return;
    }

    // Prepare data
    const formData = new FormData();
    formData.append('action', 'add');
    formData.append('staffId', staffId);
    formData.append('name', document.getElementById('staffAddName').value);
    formData.append('nric', nric);
    formData.append('email', email);
    formData.append('phone', document.getElementById('staffAddPhone').value);
    formData.append('password', password);

    try {
        const response = await fetch('StaffServlet', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            closeModal('staffAddModal');
            openModal('staffSuccessAddModal');
            loadStaffData(); // Refresh data
        } else {
            alert('Failed to add staff. Please try again.');
        }
    } catch (error) {
        console.error('Error adding staff:', error);
        alert('Error adding staff. Please try again.');
    }
}

// Confirm Delete
async function confirmStaffDelete() {
    const staff = staffData[currentStaffIndex];

    const formData = new FormData();
    formData.append('action', 'delete');
    formData.append('staffId', staff.staffId);

    try {
        const response = await fetch('StaffServlet', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.success) {
            closeModal('staffDeleteModal');
            openModal('staffSuccessDeleteModal');
            loadStaffData(); // Refresh data
        } else {
            alert('Failed to delete staff. Please try again.');
        }
    } catch (error) {
        console.error('Error deleting staff:', error);
        alert('Error deleting staff. Please try again.');
    }
}

// Modal functions (keep existing)
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'flex';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// Close modal when clicking outside
window.onclick = function (event) {
    const modals = document.querySelectorAll('.modal-overlay');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}