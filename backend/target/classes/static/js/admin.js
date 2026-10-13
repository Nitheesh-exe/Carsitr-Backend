/**
 * Nash Car Rentals - Admin Dashboard Controller (admin.js)
 */

let adminCars = [];
let adminBookings = [];
let currentBookingFilter = 'ALL';
let editingCarId = null;

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    initNavigation();
    initCarModal();
    loadDashboardData();
});

/**
 * Authentication & Guard Check
 */
function checkAdminAuth() {
    if (!NashApi.Storage.isAuthenticated() || !NashApi.Storage.isAdmin()) {
        NashApi.showToast('Access Denied. Administrator privileges required.', 'error');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 800);
    }
}

/**
 * Navigation tabs (Overview, Fleet, Bookings)
 */
function initNavigation() {
    const links = document.querySelectorAll('.sidebar-link[data-section]');
    const sections = document.querySelectorAll('.admin-section');

    links.forEach(link => {
        link.addEventListener('click', () => {
            const targetSection = link.getAttribute('data-section');

            links.forEach(l => l.classList.remove('active'));
            link.classList.add('active');

            sections.forEach(sec => {
                if (sec.id === `section-${targetSection}`) {
                    sec.classList.add('active');
                } else {
                    sec.classList.remove('active');
                }
            });

            if (targetSection === 'overview') loadStats();
            if (targetSection === 'fleet') loadFleetTable();
            if (targetSection === 'bookings') loadBookingsTable();
        });
    });

    // Logout
    const logoutBtn = document.getElementById('admin-logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            NashApi.Storage.clearAuth();
            window.location.href = 'login.html';
        });
    }

    // Bookings status filter buttons
    const filterBtns = document.querySelectorAll('.booking-filter-btn');
    filterBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            filterBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            currentBookingFilter = btn.getAttribute('data-status') || 'ALL';
            renderBookingsTable();
        });
    });
}

/**
 * Load all dashboard data
 */
async function loadDashboardData() {
    await Promise.all([
        loadStats(),
        loadFleetTable(),
        loadBookingsTable()
    ]);
}

/**
 * Load & Render KPI metrics
 */
async function loadStats() {
    try {
        const stats = await NashApi.request('/admin/stats');

        const elRev = document.getElementById('stat-revenue');
        const elActive = document.getElementById('stat-active-rentals');
        const elAvailable = document.getElementById('stat-available-fleet');
        const elTotal = document.getElementById('stat-total-fleet');

        if (elRev) elRev.innerText = `$${Number(stats.totalRevenue || 0).toLocaleString(undefined, { minimumFractionDigits: 2 })}`;
        if (elActive) elActive.innerText = stats.rentedCars || 0;
        if (elAvailable) elAvailable.innerText = stats.availableCars || 0;
        if (elTotal) elTotal.innerText = stats.totalCars || 0;
    } catch (err) {
        console.error('Error fetching admin stats:', err);
    }
}

/**
 * Load Fleet Inventory
 */
async function loadFleetTable() {
    const tbody = document.getElementById('fleet-table-body');
    if (!tbody) return;

    try {
        adminCars = await NashApi.request('/cars');
        renderFleetTable(adminCars);
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:var(--accent-red);">Error loading fleet: ${escapeHtml(err.message)}</td></tr>`;
    }
}

function renderFleetTable(cars) {
    const tbody = document.getElementById('fleet-table-body');
    if (!tbody) return;

    if (cars.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:var(--text-muted); padding:2rem;">No vehicles found in database.</td></tr>`;
        return;
    }

    tbody.innerHTML = cars.map(car => {
        const statusBadge = car.status === 'AVAILABLE' ? '<span class="badge badge-confirmed">AVAILABLE</span>'
                          : car.status === 'RENTED' ? '<span class="badge badge-pending">RENTED</span>'
                          : '<span class="badge badge-cancelled">MAINTENANCE</span>';

        return `
            <tr>
                <td>#${car.id}</td>
                <td>
                    <div class="car-cell">
                        <img src="${escapeHtml(car.imageUrl)}" alt="thumb" class="car-thumb" onerror="this.src='https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=1200&q=80'">
                        <div>
                            <strong style="color:var(--text-primary);">${escapeHtml(car.brand)} ${escapeHtml(car.model)}</strong>
                            <div style="font-size:0.8rem; color:var(--text-muted);">${car.year} • ${escapeHtml(car.transmission || 'Auto')}</div>
                        </div>
                    </div>
                </td>
                <td><span style="color:var(--text-secondary); font-weight:600;">${escapeHtml(car.category)}</span></td>
                <td><strong style="color:var(--accent-amber);">$${Number(car.dailyRate).toFixed(2)}</strong></td>
                <td>${statusBadge}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn btn-outline btn-sm" onclick="openEditCarModal(${car.id})">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteCar(${car.id})">Delete</button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

/**
 * Load Bookings Table
 */
async function loadBookingsTable() {
    const tbody = document.getElementById('bookings-table-body');
    if (!tbody) return;

    try {
        adminBookings = await NashApi.request('/admin/bookings');
        renderBookingsTable();
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:var(--accent-red);">Error loading bookings: ${escapeHtml(err.message)}</td></tr>`;
    }
}

function renderBookingsTable() {
    const tbody = document.getElementById('bookings-table-body');
    if (!tbody) return;

    let filtered = adminBookings;
    if (currentBookingFilter !== 'ALL') {
        filtered = adminBookings.filter(b => b.status === currentBookingFilter);
    }

    if (filtered.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:var(--text-muted); padding:2rem;">No bookings found matching filter.</td></tr>`;
        return;
    }

    tbody.innerHTML = filtered.map(b => {
        const badgeClass = b.status === 'CONFIRMED' ? 'badge-confirmed'
                         : b.status === 'PENDING' ? 'badge-pending'
                         : b.status === 'COMPLETED' ? 'badge-completed'
                         : 'badge-cancelled';

        return `
            <tr>
                <td>#${b.id}</td>
                <td>
                    <div>
                        <strong style="color:var(--text-primary);">${escapeHtml(b.userFullName)}</strong>
                        <div style="font-size:0.8rem; color:var(--text-muted);">${escapeHtml(b.userEmail)}</div>
                    </div>
                </td>
                <td>
                    <div>
                        <strong style="color:var(--text-primary);">${escapeHtml(b.carBrand)} ${escapeHtml(b.carModel)}</strong>
                        <div style="font-size:0.8rem; color:var(--text-muted);">${b.totalDays} day(s)</div>
                    </div>
                </td>
                <td>${b.startDate} <br><span style="font-size:0.8rem; color:var(--text-muted);">to ${b.endDate}</span></td>
                <td><strong style="color:var(--accent-amber);">$${Number(b.totalAmount).toFixed(2)}</strong></td>
                <td><span class="badge ${badgeClass}">${escapeHtml(b.status)}</span></td>
                <td>
                    <div class="action-buttons">
                        ${b.status === 'PENDING' ? `
                            <button class="btn btn-success btn-sm" onclick="updateBookingStatus(${b.id}, 'CONFIRMED')">Approve</button>
                            <button class="btn btn-danger btn-sm" onclick="updateBookingStatus(${b.id}, 'CANCELLED')">Reject</button>
                        ` : ''}
                        ${b.status === 'CONFIRMED' ? `
                            <button class="btn btn-outline btn-sm" onclick="updateBookingStatus(${b.id}, 'COMPLETED')">Complete</button>
                            <button class="btn btn-danger btn-sm" onclick="updateBookingStatus(${b.id}, 'CANCELLED')">Cancel</button>
                        ` : ''}
                        ${b.status === 'COMPLETED' || b.status === 'CANCELLED' ? `
                            <span style="font-size:0.8rem; color:var(--text-muted); font-style:italic;">No action needed</span>
                        ` : ''}
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

/**
 * Approve or Reject Booking
 */
window.updateBookingStatus = async function(bookingId, newStatus) {
    try {
        await NashApi.request(`/admin/bookings/${bookingId}/status`, {
            method: 'PUT',
            body: JSON.stringify({ status: newStatus })
        });

        NashApi.showToast(`Booking #${bookingId} marked as ${newStatus}.`, 'success');
        await loadBookingsTable();
        await loadFleetTable();
        await loadStats();
    } catch (err) {
        NashApi.showToast(err.message || 'Failed to update booking status.', 'error');
    }
};

/**
 * Add / Edit Car Modal Management
 */
function initCarModal() {
    const modal = document.getElementById('car-modal');
    const openBtn = document.getElementById('btn-add-car');
    const closeBtn = document.getElementById('car-modal-close');
    const cancelBtn = document.getElementById('car-modal-cancel');
    const form = document.getElementById('car-form');

    if (openBtn) {
        openBtn.addEventListener('click', () => {
            editingCarId = null;
            form.reset();
            document.getElementById('car-modal-title').innerText = 'Add New Fleet Vehicle';
            modal.classList.add('active');
        });
    }

    const closeModal = () => modal.classList.remove('active');
    if (closeBtn) closeBtn.addEventListener('click', closeModal);
    if (cancelBtn) cancelBtn.addEventListener('click', closeModal);

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const carPayload = {
                brand: document.getElementById('car-brand').value.trim(),
                model: document.getElementById('car-model').value.trim(),
                year: parseInt(document.getElementById('car-year').value),
                category: document.getElementById('car-category').value,
                dailyRate: parseFloat(document.getElementById('car-daily-rate').value),
                status: document.getElementById('car-status').value,
                imageUrl: document.getElementById('car-image-url').value.trim(),
                transmission: document.getElementById('car-transmission').value,
                fuelType: document.getElementById('car-fuel').value,
                seatingCapacity: parseInt(document.getElementById('car-seats').value)
            };

            const submitBtn = form.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerText;
            submitBtn.innerText = 'Saving...';
            submitBtn.disabled = true;

            try {
                if (editingCarId) {
                    await NashApi.request(`/admin/cars/${editingCarId}`, {
                        method: 'PUT',
                        body: JSON.stringify(carPayload)
                    });
                    NashApi.showToast('Vehicle updated successfully.', 'success');
                } else {
                    await NashApi.request('/admin/cars', {
                        method: 'POST',
                        body: JSON.stringify(carPayload)
                    });
                    NashApi.showToast('New vehicle added to fleet.', 'success');
                }

                modal.classList.remove('active');
                await loadFleetTable();
                await loadStats();
            } catch (err) {
                NashApi.showToast(err.message || 'Failed to save vehicle.', 'error');
            } finally {
                submitBtn.innerText = originalText;
                submitBtn.disabled = false;
            }
        });
    }
}

/**
 * Open Edit Car Modal
 */
window.openEditCarModal = function(id) {
    const car = adminCars.find(c => c.id === id);
    if (!car) return;

    editingCarId = id;
    const modal = document.getElementById('car-modal');

    document.getElementById('car-modal-title').innerText = `Edit: ${car.brand} ${car.model}`;
    document.getElementById('car-brand').value = car.brand;
    document.getElementById('car-model').value = car.model;
    document.getElementById('car-year').value = car.year;
    document.getElementById('car-category').value = car.category;
    document.getElementById('car-daily-rate').value = car.dailyRate;
    document.getElementById('car-status').value = car.status;
    document.getElementById('car-image-url').value = car.imageUrl;
    document.getElementById('car-transmission').value = car.transmission || 'Automatic';
    document.getElementById('car-fuel').value = car.fuelType || 'Petrol';
    document.getElementById('car-seats').value = car.seatingCapacity || 5;

    modal.classList.add('active');
};

/**
 * Delete Car
 */
window.deleteCar = async function(id) {
    if (!confirm(`Are you sure you want to remove vehicle #${id} from the active fleet?`)) {
        return;
    }

    try {
        await NashApi.request(`/admin/cars/${id}`, {
            method: 'DELETE'
        });
        NashApi.showToast(`Vehicle #${id} successfully removed.`, 'success');
        await loadFleetTable();
        await loadStats();
    } catch (err) {
        NashApi.showToast(err.message || 'Failed to delete vehicle.', 'error');
    }
};

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}
