document.addEventListener('DOMContentLoaded', function () {
    const currentUser = getCurrentUser();
    const projects = loadData(PROJECTS_KEY).filter(p => p.teamMembers.includes(currentUser.id) && p.status === 'In Progress');

    const projectSelect = document.getElementById('project-name');
    projects.forEach(project => {
        const option = document.createElement('option');
        option.value = project.id;
        option.textContent = project.name;
        projectSelect.appendChild(option);
    });
});

document.getElementById('report-bug-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const projectId = Number(document.getElementById('project-name').value);
    const title = document.getElementById('bug-title').value;
    const description = document.getElementById('bug-description').value;
    const severity = document.getElementById('severity').value;
    const currentUser = getCurrentUser();

    const bugId = generateId(BUGS_KEY);
    const bug = {
        id: bugId,
        projectId,
        title,
        description,
        severity,
        status: 'Open',
        createdBy: currentUser.id,
        createdOn: new Date().toISOString(),
        assignedTo: null,
    };

    addBug(bug);

    const projects = loadData(PROJECTS_KEY);
    const projectIndex = projects.findIndex(p => p.id === projectId);
    if (projectIndex !== -1) {
        projects[projectIndex].bugs.push(bugId);
        saveData(PROJECTS_KEY, projects);
    }

    alert('Bug reported successfully!');
    window.location.href = 'tester.html';
});
