document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const projectId = Number(urlParams.get('projectId'));
    const project = loadData(PROJECTS_KEY).find(p => p.id === projectId);

    if (project) {
        displayProjectInfo(project);
        displayBugs(project.bugs);
    }
});

function displayProjectInfo(project) {
    const projectInfoDiv = document.getElementById('project-info');
    projectInfoDiv.innerHTML = `
        <p>Project Name: ${project.name}</p>
        <p>Start Date: ${formatDate(project.startDate)}</p>
        <p>Status: ${project.status}</p>
        <p>Team Members: ${project.teamMembers.join(', ')}</p>
    `;
}

function displayBugs(bugIds) {
    const bugsTableBody = document.getElementById('bugs-table').querySelector('tbody');
    const bugs = loadData(BUGS_KEY).filter(bug => bugIds.includes(bug.id));
    bugsTableBody.innerHTML = bugs.map(bug => `
        <tr>
            <td>${bug.id}</td>
            <td>${bug.title}</td>
            <td>${bug.description}</td>
            <td>${bug.status}</td>
            <td>${bug.severity}</td>
            <td>
                <button onclick="assignBug(${bug.id})">Assign</button>
                <button onclick="closeBug(${bug.id})">Close</button>
            </td>
        </tr>
    `).join('');
}

function assignBug(bugId) {
    const developerId = prompt('Enter Developer ID to assign this bug:');
    if (developerId) {
        const bugs = loadData(BUGS_KEY);
        const bugIndex = bugs.findIndex(bug => bug.id === bugId);
        if (bugIndex !== -1) {
            bugs[bugIndex].status = 'Assigned';
            bugs[bugIndex].assignedTo = Number(developerId);
            saveData(BUGS_KEY, bugs);
            alert('Bug assigned successfully!');
            location.reload();
        }
    }
}

function closeBug(bugId) {
    const bugs = loadData(BUGS_KEY);
    const bugIndex = bugs.findIndex(bug => bug.id === bugId);
    if (bugIndex !== -1) {
        bugs[bugIndex].status = 'Closed';
        saveData(BUGS_KEY, bugs);
        alert('Bug closed successfully!');
        location.reload();
    }
}
