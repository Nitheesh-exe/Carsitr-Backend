/**
 * Nash Car Rentals - API & Auth Helper Service
 */
const API_BASE_URL = (window.location.protocol === 'http:' || window.location.protocol === 'https:') && window.location.port === '8080'
    ? '/api'
    : 'http://localhost:8080/api';

const Storage = {
    TOKEN_KEY: 'nash_jwt_token',
    USER_KEY: 'nash_user_profile',

    getToken() {
        return localStorage.getItem(this.TOKEN_KEY);
    },

    setToken(token) {
        localStorage.setItem(this.TOKEN_KEY, token);
    },

    getUser() {
        const raw = localStorage.getItem(this.USER_KEY);
        try {
            return raw ? JSON.parse(raw) : null;
        } catch {
            return null;
        }
    },

    setUser(user) {
        localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    },

    clearAuth() {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
    },

    isAuthenticated() {
        return !!this.getToken();
    },

    isAdmin() {
        const user = this.getUser();
        return user && user.role === 'ROLE_ADMIN';
    }
};

/**
 * Robust fetch wrapper with authorization headers and JSON handling
 */
async function apiRequest(endpoint, options = {}) {
    const url = endpoint.startsWith('http') ? endpoint : `${API_BASE_URL}${endpoint}`;
    const token = Storage.getToken();

    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...(options.headers || {})
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    try {
        const response = await fetch(url, {
            ...options,
            headers
        });

        // Parse JSON or return text
        const contentType = response.headers.get('content-type');
        let data = null;
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            const text = await response.text();
            data = text ? { message: text } : {};
        }

        if (!response.ok) {
            if (response.status === 401 && !endpoint.includes('/auth/login')) {
                console.warn('Session expired or unauthorized request.');
            }
            const errorMsg = data?.message || `HTTP Error ${response.status}: ${response.statusText}`;
            throw new Error(errorMsg);
        }

        return data;
    } catch (err) {
        throw err;
    }
}

/**
 * Toast Notification Utility
 */
function showToast(message, type = 'info', duration = 3500) {
    let container = document.getElementById('nash-toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'nash-toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast-item toast-${type}`;
    
    const icon = type === 'success' ? '✓' : type === 'error' ? '✕' : 'ℹ';
    toast.innerHTML = `
        <span class="toast-icon">${icon}</span>
        <span class="toast-text">${message}</span>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => toast.remove(), 400);
    }, duration);
}

// Global API service export
window.NashApi = {
    Storage,
    request: apiRequest,
    showToast,
    API_BASE_URL
};
