document.addEventListener('DOMContentLoaded', function () {
    const currentUser = getCurrentUser();
    displayUserInfo(currentUser);
    displayManagedProjects(currentUser.id);
});

function displayUserInfo(user) {
    const userInfoDiv = document.getElementById('user-info');
    userInfoDiv.innerHTML = `
        <p>ID: ${user.id}</p>
        <p>Name: ${user.name}</p>
        <p>Email: ${user.email}</p>
        <p>Role: ${user.role}</p>
        <p>Assigned Projects: ${user.assignedProjects.length}</p>
        <p>Last Logged In: ${formatDate(user.lastLogin)}</p>
    `;
}

function displayManagedProjects(managerId) {
    const projects = loadData(PROJECTS_KEY).filter(p => p.managerId === managerId);
    const projectsList = document.getElementById('projects-list');
    projectsList.innerHTML = projects.map(p => `
        <li>
            <a href="project_details.html?projectId=${p.id}">${p.name}</a>
        </li>
    `).join('');
}
