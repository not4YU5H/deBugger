// document.addEventListener('DOMContentLoaded', function () {
//     const urlParams = new URLSearchParams(window.location.search);
//     const projectId = Number(urlParams.get('projectId'));
//     const project = loadData(PROJECTS_KEY).find(p => p.id === projectId);

//     if (project) {
//         displayProjectInfo(project);
//         displayBugs(project.bugs);
//     }
// });

// function displayProjectInfo(project) {
//     const projectInfoDiv = document.getElementById('project-info');
//     projectInfoDiv.innerHTML = `
//         <p>Project Name: ${project.name}</p>
//         <p>Start Date: ${formatDate(project.startDate)}</p>
//         <p>Status: ${project.status}</p>
//         <p>Team Members: ${project.teamMembers.join(', ')}</p>
//     `;
// }

// function displayBugs(bugIds) {
//     const bugsTableBody = document.getElementById('bugs-table').querySelector('tbody');
//     const bugs = loadData(BUGS_KEY).filter(bug => bugIds.includes(bug.id));
//     bugsTableBody.innerHTML = bugs.map(bug => `
//         <tr>
//             <td>${bug.id}</td>
//             <td>${bug.title}</td>
//             <td>${bug.description}</td>
//             <td>${bug.status}</td>
//             <td>${bug.severity}</td>
//             <td>
//                 <button onclick="assignBug(${bug.id})">Assign</button>
//                 <button onclick="closeBug(${bug.id})">Close</button>
//             </td>
//         </tr>
//     `).join('');
// }

// function assignBug(bugId) {
//     const developerId = prompt('Enter Developer ID to assign this bug:');
//     if (developerId) {
//         const bugs = loadData(BUGS_KEY);
//         const bugIndex = bugs.findIndex(bug => bug.id === bugId);
//         if (bugIndex !== -1) {
//             bugs[bugIndex].status = 'Assigned';
//             bugs[bugIndex].assignedTo = Number(developerId);
//             saveData(BUGS_KEY, bugs);
//             alert('Bug assigned successfully!');
//             location.reload();
//         }
//     }
// }

// function closeBug(bugId) {
//     const bugs = loadData(BUGS_KEY);
//     const bugIndex = bugs.findIndex(bug => bug.id === bugId);
//     if (bugIndex !== -1) {
//         bugs[bugIndex].status = 'Closed';
//         saveData(BUGS_KEY, bugs);
//         alert('Bug closed successfully!');
//         location.reload();
//     }
// }
document.addEventListener('DOMContentLoaded', function() {
    // Get projectId from query parameter
    const urlParams = new URLSearchParams(window.location.search);
    const projectId = parseInt(urlParams.get('projectId'), 10); // Example: ?projectId=1

    if (isNaN(projectId)) {
        console.error('Invalid or missing projectId');
        return;
    }

    // Fetch the data files
    Promise.all([
        fetch('../data/Projects.json').then(response => response.json()),
        fetch('../data/Bugs.json').then(response => response.json())
    ])
    .then(([projects, bugs]) => {
        // Find the specific project
        const project = projects.find(p => p.id === projectId);
        if (project) {
            // Display project details
            document.getElementById('project-info').innerHTML = `
                <h3>${project.name}</h3>
                <p><strong>Start Date:</strong> ${project.startDate}</p>
                <p><strong>Status:</strong> ${project.status}</p>
                <p><strong>Team Members:</strong> ${project.teamMembers.join(', ')}</p>
            `;

            // Find bugs related to this project
            const projectBugs = bugs.filter(bug => project.bugs.includes(bug.id));

            // Populate the bugs table
            const bugsTableBody = document.querySelector('#bugs-table tbody');
            bugsTableBody.innerHTML = ''; // Clear existing rows

            projectBugs.forEach(bug => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${bug.id}</td>
                    <td>${bug.title}</td>
                    <td>${bug.description}</td>
                    <td>${bug.status}</td>
                    <td>${bug.severity}</td>
                    <td>
                        <button onclick="markBug(${bug.id})">Mark as Closed</button>
                    </td>
                `;
                bugsTableBody.appendChild(row);
            });
        } else {
            console.error('Project not found');
            document.getElementById('project-info').innerHTML = '<p>Project not found.</p>';
        }
    })
    .catch(error => console.error('Error fetching data:', error));
});

function markBug(bugId) {
    alert(`Bug with ID ${bugId} marked as closed.`);
}
