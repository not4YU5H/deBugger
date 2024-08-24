document.getElementById('register-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const userId = document.getElementById('user-id').value;
    const name = document.getElementById('name').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;

    const users = loadData(USERS_KEY);

    // Check if user ID is already taken
    if (users.some(u => u.id === userId)) {
        document.getElementById('register-error').textContent = 'User ID already taken.';
        return;
    }

    const user = {
        id: userId,
        name: name,
        password: password,
        role: role,
        assignedProjects: [],
        lastLogin: new Date().toISOString(),
    };

    addUser(user);
    setCurrentUser(user);
    alert('Registration successful!');

    redirectToRolePage(role);
});
