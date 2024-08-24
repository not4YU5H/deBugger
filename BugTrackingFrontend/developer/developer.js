document.addEventListener('DOMContentLoaded', function () {
    const currentUser = getCurrentUser();
    displayUserInfo(currentUser);
    displayAssignedProjects(currentUser.id);
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

function displayAssignedProjects(developerId) {
    const projects = loadData(PROJECTS_KEY).filter(p => p.teamMembers.includes(developerId));
    const projectsInfoDiv = document.getElementById('projects-info');
    if (projects.length === 0) {
        projectsInfoDiv.innerHTML = '<p>You are not assigned to any projects.</p>';
    } else {
        projectsInfoDiv.innerHTML = projects.map(p => `
            <h3>${p.name}</h3>
            <p>Bugs:</p>
            <ul>
                ${p.bugs.map(bugId => {
                    const bug = loadData(BUGS_KEY).find(b => b.id === bugId);
                    return bug ? `<li>${bug.title} - ${bug.status} - ${bug.severity}</li>` : '';
                }).join('')}
            </ul>
        `).join('');
    }
}

function closeBug(bugId) {
    const bugs = loadData(BUGS_KEY);
    const bugIndex = bugs.findIndex(b => b.id === bugId);
    if (bugIndex !== -1) {
        bugs[bugIndex].status = 'Closed';
        saveData(BUGS_KEY, bugs);
        alert('Bug closed successfully!');
        location.reload();
    }
}
