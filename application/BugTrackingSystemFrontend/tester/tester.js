document.addEventListener('DOMContentLoaded', function () {
    const currentUser = getCurrentUser();
    displayUserInfo(currentUser);
    displayAssignedProjects(currentUser.id);
});

function displayUserInfo(user) {
    const userInfoDiv = document.getElementById('user-info');
    userInfoDiv.innerHTML = `
    <h3>Hey! ${user.name}</h3>
        <p>ID: ${user.id}</p>
        
        <p>Email: ${user.email}</p>
        <p>Role: ${user.role}</p>
        <p>Assigned Projects: ${user.assignedProjects.length}</p>
        <p>Last Logged In: ${formatDate(user.lastLogin)}</p>
    `;
}

function displayAssignedProjects(testerId) {
    const projects = loadData(PROJECTS_KEY).filter(p => p.teamMembers.includes(testerId));
    const projectsInfoDiv = document.getElementById('projects-info');
    if (projects.length === 0) {
        projectsInfoDiv.innerHTML = '<p>You are not assigned to any projects.</p>';
    } else {
        projectsInfoDiv.innerHTML = projects.map(p => `
            <h3>${p.name}</h3>
            <ul>
                ${p.bugs.map(bugId => {
                    const bug = loadData(BUGS_KEY).find(b => b.id === bugId && b.createdBy === testerId);
                    return bug ? `<li><ul><li>Title: ${bug.title}</li> <li>Status: ${bug.status}</li></ul></li>` : '';
                }).join('')}
            </ul>
        `).join('');
    }
}
