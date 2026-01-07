// Updated staffAuth.js with actual staff validation
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('staffLoginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value.toLowerCase();
            const password = document.getElementById('password').value;
            const errorElement = document.getElementById('loginError');
            
            // Demo staff database (from your staff.js)
            const staffDatabase = [
                { email: 'ameermegat@gmail.com', password: 'Staff@2024', name: 'Ameer Megat' },
                { email: 'amirahizzati@gmail.com', password: 'Staff@2024', name: 'Amirah Izzati' },
                { email: 'ainasufia@gmail.com', password: 'Staff@2024', name: 'Aina Sufia' },
                { email: 'izyansarihah@gmail.com', password: 'Staff@2024', name: 'Izyan Sarihah' },
                { email: 'sitizulaikha@gmail.com', password: 'Staff@2024', name: 'Siti Zulaikha' },
                { email: 'masdiana@gmail.com', password: 'Staff@2024', name: 'Masdiana Mahasim' },
                { email: 'ismailpunan@gmail.com', password: 'Staff@2024', name: 'Ismail Punan' },
                { email: 'zulblack@gmail.com', password: 'Staff@2024', name: 'Zul Black' }
            ];
            
            // Find staff member
            const staff = staffDatabase.find(s => s.email === email);
            
            if (staff && staff.password === password) {
                // Show loading state
                const submitBtn = loginForm.querySelector('button[type="submit"]');
                const originalText = submitBtn.textContent;
                submitBtn.textContent = 'Logging in...';
                submitBtn.disabled = true;
                
                // Simulate API call delay
                setTimeout(() => {
                    localStorage.setItem('staffLoggedIn', 'true');
                    localStorage.setItem('staffEmail', email);
                    localStorage.setItem('staffName', staff.name);
                    
                    // Redirect to staff dashboard
                    window.location.href = 'staffDashboard.html';
                }, 1000);
                
            } else {
                errorElement.textContent = 'Invalid email or password';
                errorElement.style.color = '#E34D57';
            }
        });
    }
    
    // Rest of the code remains the same...
});