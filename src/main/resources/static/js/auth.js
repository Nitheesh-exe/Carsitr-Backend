/**
 * Nash Car Rentals - Auth Handler
 */
document.addEventListener('DOMContentLoaded', () => {
    const tabLogin = document.getElementById('tab-login');
    const tabRegister = document.getElementById('tab-register');
    const formLogin = document.getElementById('form-login');
    const formRegister = document.getElementById('form-register');

    // Tab switching
    if (tabLogin && tabRegister) {
        tabLogin.addEventListener('click', () => {
            tabLogin.classList.add('active');
            tabRegister.classList.remove('active');
            formLogin.style.display = 'block';
            formRegister.style.display = 'none';
        });

        tabRegister.addEventListener('click', () => {
            tabRegister.classList.add('active');
            tabLogin.classList.remove('active');
            formLogin.style.display = 'none';
            formRegister.style.display = 'block';
        });
    }

    // Handle Login
    if (formLogin) {
        formLogin.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('login-email').value.trim();
            const password = document.getElementById('login-password').value;

            const submitBtn = formLogin.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerText;
            submitBtn.innerText = 'Authenticating...';
            submitBtn.disabled = true;

            try {
                const data = await NashApi.request('/auth/login', {
                    method: 'POST',
                    body: JSON.stringify({ email, password })
                });

                NashApi.Storage.setToken(data.token);
                NashApi.Storage.setUser({
                    id: data.id,
                    email: data.email,
                    fullName: data.fullName,
                    role: data.role
                });

                NashApi.showToast(`Welcome back, ${data.fullName}!`, 'success');

                setTimeout(() => {
                    if (data.role === 'ROLE_ADMIN') {
                        window.location.href = 'admin.html';
                    } else {
                        window.location.href = 'index.html';
                    }
                }, 800);
            } catch (err) {
                NashApi.showToast(err.message || 'Login failed. Please check your credentials.', 'error');
            } finally {
                submitBtn.innerText = originalText;
                submitBtn.disabled = false;
            }
        });
    }

    // Handle Registration
    if (formRegister) {
        formRegister.addEventListener('submit', async (e) => {
            e.preventDefault();
            const fullName = document.getElementById('reg-name').value.trim();
            const email = document.getElementById('reg-email').value.trim();
            const phone = document.getElementById('reg-phone').value.trim();
            const password = document.getElementById('reg-password').value;
            const confirmPassword = document.getElementById('reg-confirm-password').value;

            if (password !== confirmPassword) {
                NashApi.showToast('Passwords do not match.', 'error');
                return;
            }

            const submitBtn = formRegister.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerText;
            submitBtn.innerText = 'Creating Account...';
            submitBtn.disabled = true;

            try {
                const data = await NashApi.request('/auth/register', {
                    method: 'POST',
                    body: JSON.stringify({
                        fullName,
                        email,
                        phone,
                        password,
                        role: 'ROLE_CUSTOMER'
                    })
                });

                NashApi.Storage.setToken(data.token);
                NashApi.Storage.setUser({
                    id: data.id,
                    email: data.email,
                    fullName: data.fullName,
                    role: data.role
                });

                NashApi.showToast('Account created successfully! Welcome to Nash.', 'success');

                setTimeout(() => {
                    window.location.href = 'index.html';
                }, 1000);
            } catch (err) {
                NashApi.showToast(err.message || 'Registration failed. Try again.', 'error');
            } finally {
                submitBtn.innerText = originalText;
                submitBtn.disabled = false;
            }
        });
    }

    // Quick Demo filler helpers
    window.fillCredentials = (email, password) => {
        if (tabLogin) tabLogin.click();
        const emailInput = document.getElementById('login-email');
        const passInput = document.getElementById('login-password');
        if (emailInput && passInput) {
            emailInput.value = email;
            passInput.value = password;
        }
    };
});
