/**
 * Nash Car Rentals - Customer Portal & Fleet Showcase (main.js)
 */

let allCars = [];
let currentCategory = 'ALL';
let activeCarForBooking = null;

document.addEventListener('DOMContentLoaded', () => {
    initAuthUI();
    initFilterHandlers();
    loadCars();
    initBookingModal();
    initMyBookingsModal();
});

/**
 * Configure header navigation depending on user session
 */
function initAuthUI() {
    const authContainer = document.getElementById('nav-auth-container');
    if (!authContainer) return;

    const user = NashApi.Storage.getUser();

    if (user && NashApi.Storage.isAuthenticated()) {
        const adminBtn = user.role === 'ROLE_ADMIN' 
            ? `<a href="admin.html" class="btn btn-admin btn-sm">Admin Portal</a>` 
            : '';

        authContainer.innerHTML = `
            ${adminBtn}
            <button id="btn-my-bookings" class="btn btn-outline btn-sm">My Bookings</button>
            <span class="user-greeting" style="font-size:0.9rem; color:var(--text-secondary); margin-left: 5px;">
                <strong style="color:var(--text-primary);">${escapeHtml(user.fullName.split(' ')[0])}</strong>
            </span>
            <button id="btn-logout" class="btn btn-outline btn-sm" title="Log Out">Sign Out</button>
        `;

        document.getElementById('btn-logout').addEventListener('click', () => {
            NashApi.Storage.clearAuth();
            NashApi.showToast('You have been logged out.', 'info');
            setTimeout(() => window.location.reload(), 500);
        });

        document.getElementById('btn-my-bookings').addEventListener('click', () => {
            openMyBookingsModal();
        });
    } else {
        authContainer.innerHTML = `
            <a href="login.html" class="btn btn-primary btn-sm">Sign In / Register</a>
        `;
    }
}

/**
 * Filter handlers & category pills
 */
function initFilterHandlers() {
    const pills = document.querySelectorAll('.pill-btn');
    pills.forEach(pill => {
        pill.addEventListener('click', () => {
            pills.forEach(p => p.classList.remove('active'));
            pill.classList.add('active');
            currentCategory = pill.getAttribute('data-category') || 'ALL';
            applyFilters();
        });
    });

    const searchInput = document.getElementById('search-brand');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(() => {
            applyFilters();
        }, 300));
    }

    const categorySelect = document.getElementById('filter-category-select');
    if (categorySelect) {
        categorySelect.addEventListener('change', (e) => {
            currentCategory = e.target.value;
            // sync with pills
            pills.forEach(p => {
                if (p.getAttribute('data-category') === currentCategory) {
                    p.classList.add('active');
                } else {
                    p.classList.remove('active');
                }
            });
            applyFilters();
        });
    }

    const resetBtn = document.getElementById('btn-reset-filters');
    if (resetBtn) {
        resetBtn.addEventListener('click', () => {
            if (searchInput) searchInput.value = '';
            if (categorySelect) categorySelect.value = 'ALL';
            currentCategory = 'ALL';
            pills.forEach(p => p.classList.toggle('active', p.getAttribute('data-category') === 'ALL'));
            applyFilters();
        });
    }
}

/**
 * Load fleet from REST API
 */
async function loadCars() {
    const grid = document.getElementById('fleet-grid');
    if (!grid) return;

    grid.innerHTML = `
        <div style="grid-column: 1 / -1; text-align: center; padding: 4rem; color: var(--text-muted);">
            <div style="display:inline-block; width:36px; height:36px; border:3px solid var(--accent-amber); border-top-color:transparent; border-radius:50%; animation:spin 1s linear infinite;"></div>
            <p style="margin-top:1rem;">Accessing Nash Vault Fleet...</p>
        </div>
    `;

    try {
        const cars = await NashApi.request('/cars');
        allCars = cars;
        renderCars(allCars);
    } catch (err) {
        grid.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; padding: 3rem; background: var(--bg-card); border-radius: var(--radius-md);">
                <p style="color: var(--accent-red); margin-bottom: 1rem;">Unable to load vehicles: ${escapeHtml(err.message)}</p>
                <button class="btn btn-outline btn-sm" onclick="loadCars()">Retry Connection</button>
            </div>
        `;
    }
}

/**
 * Filter car list in-memory or by parameter
 */
function applyFilters() {
    const searchVal = (document.getElementById('search-brand')?.value || '').toLowerCase().trim();
    
    let filtered = allCars.filter(car => {
        const matchesCategory = (currentCategory === 'ALL' || !currentCategory) 
            || car.category.toUpperCase() === currentCategory.toUpperCase();
        
        const matchesSearch = !searchVal 
            || car.brand.toLowerCase().includes(searchVal) 
            || car.model.toLowerCase().includes(searchVal);

        return matchesCategory && matchesSearch;
    });

    renderCars(filtered);
}

/**
 * Render car cards into grid
 */
function renderCars(cars) {
    const grid = document.getElementById('fleet-grid');
    if (!grid) return;

    if (cars.length === 0) {
        grid.innerHTML = `
            <div style="grid-column: 1 / -1; text-align: center; padding: 4rem; background: var(--bg-card); border-radius: var(--radius-md);">
                <h3 style="margin-bottom: 0.5rem;">No Vehicles Matched</h3>
                <p style="color: var(--text-secondary);">Try clearing your search or picking another category.</p>
            </div>
        `;
        return;
    }

    grid.innerHTML = cars.map(car => {
        const statusClass = car.status === 'AVAILABLE' ? 'status-available' 
                          : car.status === 'RENTED' ? 'status-rented' 
                          : 'status-maintenance';
        const isAvailable = car.status === 'AVAILABLE';

        return `
            <div class="car-card">
                <div class="card-image-wrap">
                    <img src="${escapeHtml(car.imageUrl)}" alt="${escapeHtml(car.brand)} ${escapeHtml(car.model)}" class="card-image" loading="lazy" onerror="this.src='https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=1200&q=80'">
                    <span class="card-badge-status ${statusClass}">${escapeHtml(car.status)}</span>
                    <span class="card-badge-category">${escapeHtml(car.category)}</span>
                </div>
                <div class="card-body">
                    <div class="card-header-info">
                        <div>
                            <h3 class="car-title">${escapeHtml(car.brand)} ${escapeHtml(car.model)}</h3>
                            <span class="car-subtitle">${car.year} Precision Engineering</span>
                        </div>
                        <div class="car-price-block">
                            <span class="car-price-value">$${Number(car.dailyRate).toFixed(0)}</span>
                            <span class="car-price-unit">/day</span>
                        </div>
                    </div>
                    <div class="car-specs">
                        <div class="spec-item">
                            <span class="spec-label">Gearbox</span>
                            <span class="spec-value">${escapeHtml(car.transmission || 'Auto')}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Fuel</span>
                            <span class="spec-value">${escapeHtml(car.fuelType || 'Petrol')}</span>
                        </div>
                        <div class="spec-item">
                            <span class="spec-label">Seats</span>
                            <span class="spec-value">${car.seatingCapacity || 5}</span>
                        </div>
                    </div>
                    <div class="card-actions">
                        <button class="btn btn-primary" style="width: 100%;" 
                                ${!isAvailable ? 'disabled style="opacity: 0.5; cursor: not-allowed;"' : ''} 
                                onclick="openBookingModal(${car.id})">
                            ${isAvailable ? 'Book This Vehicle' : 'Unavailable'}
                        </button>
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

/**
 * Initialize Booking Modal & Dynamic Price Calculation
 */
function initBookingModal() {
    const modal = document.getElementById('booking-modal');
    const closeBtn = document.getElementById('modal-close-btn');
    const form = document.getElementById('booking-form');
    const startDateInput = document.getElementById('booking-start-date');
    const endDateInput = document.getElementById('booking-end-date');

    // Set default dates to today and tomorrow
    const today = new Date().toISOString().split('T')[0];
    const tomorrow = new Date(Date.now() + 86400000).toISOString().split('T')[0];

    if (startDateInput && endDateInput) {
        startDateInput.min = today;
        endDateInput.min = today;
        startDateInput.value = today;
        endDateInput.value = tomorrow;

        startDateInput.addEventListener('change', () => {
            if (endDateInput.value < startDateInput.value) {
                endDateInput.value = startDateInput.value;
            }
            endDateInput.min = startDateInput.value;
            calculateBookingTotal();
        });

        endDateInput.addEventListener('change', calculateBookingTotal);
    }

    if (closeBtn && modal) {
        closeBtn.addEventListener('click', () => modal.classList.remove('active'));
        modal.addEventListener('click', (e) => {
            if (e.target === modal) modal.classList.remove('active');
        });
    }

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            if (!NashApi.Storage.isAuthenticated()) {
                NashApi.showToast('Please sign in or create an account to book.', 'info');
                setTimeout(() => window.location.href = 'login.html', 1200);
                return;
            }

            if (!activeCarForBooking) return;

            const startDate = startDateInput.value;
            const endDate = endDateInput.value;

            const submitBtn = form.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerText;
            submitBtn.innerText = 'Securing Reservation...';
            submitBtn.disabled = true;

            try {
                const response = await NashApi.request('/bookings', {
                    method: 'POST',
                    body: JSON.stringify({
                        carId: activeCarForBooking.id,
                        startDate,
                        endDate
                    })
                });

                NashApi.showToast(`Booking reserved successfully for ${activeCarForBooking.brand} ${activeCarForBooking.model}!`, 'success');
                modal.classList.remove('active');
                
                // Refresh cars and update status if rented
                loadCars();
            } catch (err) {
                NashApi.showToast(err.message || 'Failed to place booking.', 'error');
            } finally {
                submitBtn.innerText = originalText;
                submitBtn.disabled = false;
            }
        });
    }
}

/**
 * Open Booking Modal for specific car
 */
window.openBookingModal = function(carId) {
    const car = allCars.find(c => c.id === carId);
    if (!car) return;

    activeCarForBooking = car;
    const modal = document.getElementById('booking-modal');

    document.getElementById('modal-car-title').innerText = `${car.brand} ${car.model} (${car.year})`;
    document.getElementById('modal-car-rate').innerText = `$${Number(car.dailyRate).toFixed(2)} / day`;
    document.getElementById('modal-car-img').src = car.imageUrl;
    document.getElementById('modal-car-category').innerText = car.category;

    calculateBookingTotal();
    modal.classList.add('active');
};

function calculateBookingTotal() {
    if (!activeCarForBooking) return;

    const startVal = document.getElementById('booking-start-date').value;
    const endVal = document.getElementById('booking-end-date').value;

    if (!startVal || !endVal) return;

    const start = new Date(startVal);
    const end = new Date(endVal);

    let days = Math.round((end - start) / (1000 * 60 * 60 * 24));
    if (days <= 0) days = 1;

    const total = days * Number(activeCarForBooking.dailyRate);

    document.getElementById('calc-days').innerText = `${days} day${days > 1 ? 's' : ''}`;
    document.getElementById('calc-total').innerText = `$${total.toFixed(2)}`;
}

/**
 * "My Bookings" Modal for signed-in customer
 */
function initMyBookingsModal() {
    const modal = document.getElementById('my-bookings-modal');
    const closeBtn = document.getElementById('my-bookings-close-btn');

    if (closeBtn && modal) {
        closeBtn.addEventListener('click', () => modal.classList.remove('active'));
        modal.addEventListener('click', (e) => {
            if (e.target === modal) modal.classList.remove('active');
        });
    }
}

async function openMyBookingsModal() {
    const modal = document.getElementById('my-bookings-modal');
    const listContainer = document.getElementById('my-bookings-list');
    modal.classList.add('active');

    listContainer.innerHTML = `<p style="color:var(--text-muted); text-align:center; padding: 2rem;">Loading your reservations...</p>`;

    try {
        const bookings = await NashApi.request('/bookings/my-bookings');
        if (bookings.length === 0) {
            listContainer.innerHTML = `
                <div style="text-align: center; padding: 3rem 1rem;">
                    <p style="color: var(--text-secondary); margin-bottom: 1rem;">You have not placed any reservations yet.</p>
                    <button class="btn btn-primary btn-sm" onclick="document.getElementById('my-bookings-modal').classList.remove('active')">Explore Fleet</button>
                </div>
            `;
            return;
        }

        listContainer.innerHTML = bookings.map(b => {
            const statusColor = b.status === 'CONFIRMED' ? '#34d399' 
                              : b.status === 'PENDING' ? '#fbbf24' 
                              : b.status === 'COMPLETED' ? '#38bdf8' 
                              : '#f87171';

            return `
                <div style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:var(--radius-sm); padding:1.2rem; margin-bottom:1rem; display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:1rem;">
                    <div style="display:flex; align-items:center; gap:14px;">
                        <img src="${escapeHtml(b.carImageUrl)}" alt="Car" style="width:60px; height:45px; object-fit:cover; border-radius:var(--radius-sm);">
                        <div>
                            <h4 style="font-size:1.1rem; color:var(--text-primary);">${escapeHtml(b.carBrand)} ${escapeHtml(b.carModel)}</h4>
                            <p style="font-size:0.85rem; color:var(--text-muted);">${b.startDate} to ${b.endDate} (${b.totalDays} days)</p>
                        </div>
                    </div>
                    <div style="text-align:right;">
                        <span style="display:inline-block; padding:0.2rem 0.6rem; border-radius:var(--radius-full); font-size:0.75rem; font-weight:700; color:${statusColor}; border:1px solid ${statusColor}40; margin-bottom:4px;">
                            ${escapeHtml(b.status)}
                        </span>
                        <div style="font-size:1.2rem; font-weight:800; color:var(--accent-amber);">$${Number(b.totalAmount).toFixed(2)}</div>
                    </div>
                </div>
            `;
        }).join('');
    } catch (err) {
        listContainer.innerHTML = `<p style="color:var(--accent-red); text-align:center; padding: 2rem;">Error: ${escapeHtml(err.message)}</p>`;
    }
}

// Utility: Debounce
function debounce(func, wait) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}

// Utility: XSS Escape
function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}
