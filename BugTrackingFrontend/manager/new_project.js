document.getElementById('new-project-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const projectName = document.getElementById('project-name').value;
    const startDate = document.getElementById('start-date').value;
    const teamMembers = document.getElementById('team-members').value.split(',').map(Number);

    if (new Date(startDate) < new Date(Date.now() + 2 * 24 * 60 * 60 * 1000)) {
        alert('Start date should be at least 2 days later than the current date.');
        return;
    }

    const projectId = generateId(PROJECTS_KEY);
    const currentUser = getCurrentUser();

    const project = {
        id: projectId,
        name: projectName,
        startDate,
        managerId: currentUser.id,
        teamMembers,
        status: 'In Progress',
        bugs: []
    };

    addProject(project);
    alert('Project created successfully!');
    window.location.href = 'manager.html';
});
