const staffs = [
    { id: 'STA001', name: 'Ameer Megat', nric: '010101-01-1010', email: 'ameermegat@gmail.com', phone: '+60 14-1234 567', password: 'Staff@2024' },
    { id: 'STA002', name: 'Amirah Izzati', nric: '020202-02-2020', email: 'amirahizzati@gmail.com', phone: '+60 14-2345 678', password: 'Staff@2024' },
    { id: 'STA003', name: 'Aina Sufia', nric: '040101-01-1010', email: 'ainasufia@gmail.com', phone: '+60 14-3677 414', password: 'Staff@2024' },
    { id: 'STA004', name: 'Izyan Sarihah', nric: '040404-04-4040', email: 'izyansarihah@gmail.com', phone: '+60 14-4567 890', password: 'Staff@2024' },
    { id: 'STA005', name: 'Siti Zulaikha', nric: '050505-05-5050', email: 'sitizulaikha@gmail.com', phone: '+60 14-5678 901', password: 'Staff@2024' },
    { id: 'STA006', name: 'Masdiana Mahasim', nric: '060606-06-6060', email: 'masdiana@gmail.com', phone: '+60 14-6789 012', password: 'Staff@2024' },
    { id: 'STA007', name: 'Ismail Punan', nric: '070707-07-7070', email: 'ismailpunan@gmail.com', phone: '+60 14-7890 123', password: 'Staff@2024' },
    { id: 'STA008', name: 'Zul Black', nric: '080808-08-8080', email: 'zulblack@gmail.com', phone: '+60 14-8901 234', password: 'Staff@2024' }
];

let currentServiceIndex = 0;
let currentStaffIndex = 0;

// Page navigation
function showPage(page) {
    // Hide all pages
    document.querySelectorAll('.page-container').forEach(p => p.classList.remove('active'));

    // Show selected page
    const pageMap = {
        'services': 'servicesPage',
        'staffs': 'staffsPage'
    };

    const pageId = pageMap[page];
    if (pageId) {
        document.getElementById(pageId).classList.add('active');
    }

    // Update sidebar active state
    document.querySelectorAll('.menu-item').forEach(item => {
        item.classList.remove('active');
        if (item.getAttribute('data-page') === page) {
            item.classList.add('active');
        }
    });
}

// Sidebar toggle for mobile
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('mobile-open');
}

// Modal functions
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
    }
}

function openModal(modalId) {
    // Close all modals first
    document.querySelectorAll('.modal-overlay').forEach(m => m.classList.remove('active'));

    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
    }
}
// ==================== STAFF FUNCTIONS ====================

// View Staff modal
function openStaffViewModal(index) {
    currentStaffIndex = index;
    const staff = staffs[index];

    document.getElementById('staffViewId').textContent = staff.id;
    document.getElementById('staffViewName').textContent = staff.name;
    document.getElementById('staffViewNric').textContent = staff.nric;
    document.getElementById('staffViewEmail').textContent = staff.email;
    document.getElementById('staffViewPhone').textContent = staff.phone;
    document.getElementById('staffViewPassword').textContent = staff.password;

    openModal('staffViewModal');
}

// Edit Staff modal
function openStaffEditModal(index) {
    currentStaffIndex = index;
    const staff = staffs[index];

    document.getElementById('staffEditName').value = staff.name;
    document.getElementById('staffEditNric').value = staff.nric;
    document.getElementById('staffEditEmail').value = staff.email;
    document.getElementById('staffEditPhone').value = staff.phone;
    document.getElementById('staffEditPassword').value = staff.password;
    document.getElementById('staffEditConfirmPassword').value = staff.password;

    openModal('staffEditModal');
}

function openStaffEditModalFromView() {
    closeModal('staffViewModal');
    openStaffEditModal(currentStaffIndex);
}

function saveStaffEdit() {
    const staff = staffs[currentStaffIndex];

    staff.name = document.getElementById('staffEditName').value;
    staff.nric = document.getElementById('staffEditNric').value;
    staff.email = document.getElementById('staffEditEmail').value;
    staff.phone = document.getElementById('staffEditPhone').value;
    staff.password = document.getElementById('staffEditPassword').value;

    closeModal('staffEditModal');
    openModal('staffSuccessEditModal');
}

// Add Staff modal
function openStaffAddModal() {
    document.getElementById('staffAddForm').reset();
    openModal('staffAddModal');
}

function saveStaffAdd() {
    const newStaff = {
        id: 'STA' + String(staffs.length + 1).padStart(3, '0'),
        name: document.getElementById('staffAddName').value,
        nric: document.getElementById('staffAddNric').value,
        email: document.getElementById('staffAddEmail').value,
        phone: document.getElementById('staffAddPhone').value,
        password: document.getElementById('staffAddPassword').value
    };

    staffs.push(newStaff);

    closeModal('staffAddModal');
    openModal('staffSuccessAddModal');
}

// Delete Staff modal
function openStaffDeleteModal(index) {
    currentStaffIndex = index;
    openModal('staffDeleteModal');
}

function openStaffDeleteModalFromView() {
    closeModal('staffViewModal');
    openStaffDeleteModal(currentStaffIndex);
}

function confirmStaffDelete() {
    staffs.splice(currentStaffIndex, 1);

    closeModal('staffDeleteModal');
    openModal('staffSuccessDeleteModal');
}

// Close modal when clicking outside
document.querySelectorAll('.modal-overlay').forEach(overlay => {
    overlay.addEventListener('click', function(e) {
        if (e.target === this) {
            this.classList.remove('active');
        }
    });
});

// Close sidebar when clicking outside on mobile
document.addEventListener('click', function(e) {
    const sidebar = document.getElementById('sidebar');
    const menuIcon = document.querySelector('.menu-icon');

    if (window.innerWidth <= 768) {
        if (!sidebar.contains(e.target) && !menuIcon.contains(e.target)) {
            sidebar.classList.remove('mobile-open');
        }
    }
});