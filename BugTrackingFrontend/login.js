document.getElementById('login-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const userId = document.getElementById('user-id').value;
    const password = document.getElementById('password').value;

    const users = loadData(USERS_KEY);
    const user = users.find(u => u.id === userId && u.password === password);

    if (user) {
        setCurrentUser(user);
        alert('Login successful!');
        redirectToRolePage(user.role);
    } else {
        document.getElementById('login-error').textContent = 'Invalid User ID or Password.';
    }
});

function redirectToRolePage(role) {
    if (role === 'Project Manager') {
        window.location.href = 'manager/manager.html';
    } else if (role === 'Tester') {
        window.location.href = 'tester/tester.html';
    } else if (role === 'Developer') {
        window.location.href = 'developer/developer.html';
    }
}
