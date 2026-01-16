// staff.js - Complete CRUD operations for Staff Management
console.log("staff.js loaded successfully!");

let currentStaffIndex = null;
let staffData = [];

// Load staff data on page load
document.addEventListener('DOMContentLoaded', function () {
    console.log("DOMContentLoaded - Starting staff management");

    // Call loadStaffData immediately
    loadStaffData();

    // Setup search functionality
    const searchInput = document.getElementById('staffSearch');
    if (searchInput) {
        searchInput.addEventListener('input', function (e) {
            console.log("Searching for:", e.target.value);
            searchStaff(e.target.value);
        });
    }
});

// ==================== CORE FUNCTIONS ====================

// Load staff data from server
async function loadStaffData() {
    console.log("loadStaffData() called");

    try {
        console.log("Fetching data from StaffServlet...");
        const response = await fetch('StaffServlet');
        console.log("Response received, status:", response.status);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        console.log("Data parsed successfully, records:", data.length);
        console.log("Sample data:", data.length > 0 ? data[0] : "No data");

        staffData = data;
        populateStaffTable(staffData);

    } catch (error) {
        console.error('Error loading staff data:', error);

        // Show error in table
        const tbody = document.getElementById('staffTableBody');
        if (tbody) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="4" style="text-align: center; padding: 40px; color: red;">
                        <h3>⚠️ Error Loading Data</h3>
                        <p>${error.message}</p>
                        <p>Check console for details</p>
                    </td>
                </tr>
            `;
        }

        alert('Failed to load staff data. Check console for details.');
    }
}

// Populate staff table
function populateStaffTable(data) {
    console.log("populateStaffTable() called with", data.length, "records");

    const tbody = document.getElementById('staffTableBody');
    if (!tbody) {
        console.error("ERROR: staffTableBody not found in HTML!");
        return;
    }

    tbody.innerHTML = '';

    if (!data || data.length === 0) {
        console.log("No data to display");
        tbody.innerHTML = `
            <tr>
                <td colspan="4" style="text-align: center; padding: 40px; color: #666;">
                    <div style="font-size: 48px; color: #ddd; margin-bottom: 16px;">
                        <i class="fas fa-users-slash"></i>
                    </div>
                    <h3>No Staff Members Found</h3>
                    <p>The database appears to be empty.</p>
                    <button onclick="openStaffAddModal()" style="margin-top: 10px; padding: 8px 16px; background: #624DE3; color: white; border: none; border-radius: 5px; cursor: pointer;">
                        Add First Staff
                    </button>
                </td>
            </tr>
        `;
        return;
    }

    console.log("Creating table rows...");
    data.forEach((staff, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td><strong>${staff.staffId || 'N/A'}</strong></td>
            <td>${staff.name || 'N/A'}</td>
            <td>${staff.email || 'N/A'}</td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-view" onclick="window.openStaffViewModal(${index})">View</button>
                    <button class="btn-action btn-edit" onclick="window.openStaffEditModal(${index})">Edit</button>
                    <button class="btn-action btn-delete" onclick="window.openStaffDeleteModal(${index})">Delete</button>
                </div>
            </td>
        `;
        tbody.appendChild(row);
    });

    console.log("Table populated with", data.length, "rows");
}

// Search staff
async function searchStaff(keyword) {
    console.log("searchStaff() called with keyword:", keyword);

    if (!keyword || keyword.trim() === '') {
        console.log("Empty search, showing all data");
        populateStaffTable(staffData);
        return;
    }

    try {
        const response = await fetch(`StaffServlet?action=search&keyword=${encodeURIComponent(keyword)}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log("Search results:", data.length, "records");
        populateStaffTable(data);
    } catch (error) {
        console.error('Error searching staff:', error);
        alert('Search failed: ' + error.message);
    }
}

// ==================== MODAL FUNCTIONS ====================

// Open View Modal
async function openStaffViewModal(index) {
    console.log("openStaffViewModal() called for index:", index);
    currentStaffIndex = index;
    const staff = staffData[index];

    if (!staff) {
        console.error("No staff data at index:", index);
        return;
    }

    // Update modal with staff data
    document.getElementById('staffViewId').textContent = staff.staffId || 'N/A';
    document.getElementById('staffViewName').textContent = staff.name || 'N/A';
    document.getElementById('staffViewNric').textContent = staff.nric || 'N/A';
    document.getElementById('staffViewEmail').textContent = staff.email || 'N/A';
    document.getElementById('staffViewPhone').textContent = staff.phone || 'N/A';
    document.getElementById('staffViewPassword').textContent = '••••••••';

    window.openModal('staffViewModal');
}

// Open Edit Modal
async function openStaffEditModal(index) {
    console.log("openStaffEditModal() called for index:", index);
    currentStaffIndex = index;
    const staff = staffData[index];

    if (!staff) {
        console.error("No staff data at index:", index);
        return;
    }

    // Update form with staff data
    document.getElementById('staffEditName').value = staff.name || '';
    document.getElementById('staffEditNric').value = staff.nric || '';
    document.getElementById('staffEditEmail').value = staff.email || '';
    document.getElementById('staffEditPhone').value = staff.phone || '';
    document.getElementById('staffEditPassword').value = '';
    document.getElementById('staffEditConfirmPassword').value = '';

    window.openModal('staffEditModal');
}

// Open Edit Modal from View
function openStaffEditModalFromView() {
    console.log("openStaffEditModalFromView() called");
    window.closeModal('staffViewModal');
    openStaffEditModal(currentStaffIndex);
}

// Open Add Modal
async function openStaffAddModal() {
    console.log("openStaffAddModal() called");

    // Clear form
    document.getElementById('staffAddForm').reset();

    // Try to get next staff ID (optional)
    try {
        const response = await fetch('StaffServlet?action=generateId');
        if (response.ok) {
            const data = await response.json();
            console.log('Next staff ID would be:', data.nextId);
        }
    } catch (error) {
        console.error('Error generating ID:', error);
    }

    window.openModal('staffAddModal');
}

// Open Delete Modal
function openStaffDeleteModal(index) {
    console.log("openStaffDeleteModal() called for index:", index);
    currentStaffIndex = index;
    window.openModal('staffDeleteModal');
}

// Open Delete Modal from View
function openStaffDeleteModalFromView() {
    console.log("openStaffDeleteModalFromView() called");
    window.closeModal('staffViewModal');
    window.openModal('staffDeleteModal');
}

// Save Edit
async function saveStaffEdit() {
    console.log("saveStaffEdit() called");

    const staff = staffData[currentStaffIndex];
    if (!staff) {
        alert('No staff selected!');
        return;
    }

    const form = document.getElementById('staffEditForm');

    // Basic validation
    const name = document.getElementById('staffEditName').value.trim();
    const email = document.getElementById('staffEditEmail').value.trim();

    if (!name || !email) {
        alert('Name and Email are required!');
        return;
    }

    // Check passwords match
    const password = document.getElementById('staffEditPassword').value;
    const confirmPassword = document.getElementById('staffEditConfirmPassword').value;

    if (password && password !== confirmPassword) {
        alert('Passwords do not match!');
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'update');
        formData.append('staffId', staff.staffId);
        formData.append('name', name);
        formData.append('nric', document.getElementById('staffEditNric').value.trim());
        formData.append('email', email);
        formData.append('phone', document.getElementById('staffEditPhone').value.trim());
        formData.append('password', password || '');

        console.log("Saving staff update...");
        const response = await fetch('StaffServlet', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();
        console.log("Save result:", result);

        if (result.success) {
            window.closeModal('staffEditModal');
            alert('Staff details updated successfully!');
            loadStaffData(); // Refresh data
        } else {
            alert('Failed to update staff. Please try again.');
        }
    } catch (error) {
        console.error('Error updating staff:', error);
        alert('Error updating staff: ' + error.message);
    }
}

// Save Add
async function saveStaffAdd() {
    console.log("saveStaffAdd() called");

    const form = document.getElementById('staffAddForm');

    // Basic validation
    const name = document.getElementById('staffAddName').value.trim();
    const nric = document.getElementById('staffAddNric').value.trim();
    const email = document.getElementById('staffAddEmail').value.trim();
    const password = document.getElementById('staffAddPassword').value;
    const confirmPassword = document.getElementById('staffAddConfirmPassword').value;

    if (!name || !nric || !email || !password) {
        alert('All fields except phone are required!');
        return;
    }

    if (password !== confirmPassword) {
        alert('Passwords do not match!');
        return;
    }

    try {
        // Get next staff ID
        const idResponse = await fetch('StaffServlet?action=generateId');
        const idData = await idResponse.json();
        const staffId = idData.nextId;

        if (!staffId) {
            throw new Error('Failed to generate staff ID');
        }

        // Prepare data
        const formData = new FormData();
        formData.append('action', 'add');
        formData.append('staffId', staffId);
        formData.append('name', name);
        formData.append('nric', nric);
        formData.append('email', email);
        formData.append('phone', document.getElementById('staffAddPhone').value.trim());
        formData.append('password', password);

        console.log("Adding new staff...");
        const response = await fetch('StaffServlet', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();
        console.log("Add result:", result);

        if (result.success) {
            window.closeModal('staffAddModal');
            alert('New staff added successfully!');
            loadStaffData(); // Refresh data
        } else {
            alert('Failed to add staff. Please try again.');
        }
    } catch (error) {
        console.error('Error adding staff:', error);
        alert('Error adding staff: ' + error.message);
    }
}

// Confirm Delete
async function confirmStaffDelete() {
    console.log("confirmStaffDelete() called");

    const staff = staffData[currentStaffIndex];
    if (!staff || !staff.staffId) {
        alert('Invalid staff data!');
        return;
    }

    if (!confirm(`Are you sure you want to delete ${staff.name}?`)) {
        return;
    }

    try {
        const formData = new FormData();
        formData.append('action', 'delete');
        formData.append('staffId', staff.staffId);

        console.log("Deleting staff...");
        const response = await fetch('StaffServlet', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();
        console.log("Delete result:", result);

        if (result.success) {
            window.closeModal('staffDeleteModal');
            alert('Staff deleted successfully!');
            loadStaffData(); // Refresh data
        } else {
            alert('Failed to delete staff. Please try again.');
        }
    } catch (error) {
        console.error('Error deleting staff:', error);
        alert('Error deleting staff: ' + error.message);
    }
}

// ==================== EXPORT FUNCTIONS ====================
// Make functions available to HTML onclick handlers
window.loadStaffData = loadStaffData;
window.searchStaff = searchStaff;
window.openStaffViewModal = openStaffViewModal;
window.openStaffEditModal = openStaffEditModal;
window.openStaffEditModalFromView = openStaffEditModalFromView;
window.openStaffAddModal = openStaffAddModal;
window.openStaffDeleteModal = openStaffDeleteModal;
window.openStaffDeleteModalFromView = openStaffDeleteModalFromView;
window.saveStaffEdit = saveStaffEdit;
window.saveStaffAdd = saveStaffAdd;
window.confirmStaffDelete = confirmStaffDelete;

console.log("staff.js initialization complete!");