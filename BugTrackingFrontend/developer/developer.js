// document.addEventListener('DOMContentLoaded', function () {
//     const currentUser = getCurrentUser();
//     displayUserInfo(currentUser);
//     displayAssignedProjects(currentUser.id);
// });

// function displayUserInfo(user) {
//     const userInfoDiv = document.getElementById('user-info');
//     userInfoDiv.innerHTML = `
//         <p>ID: ${user.id}</p>
//         <p>Name: ${user.name}</p>
//         <p>Email: ${user.email}</p>
//         <p>Role: ${user.role}</p>
//         <p>Assigned Projects: ${user.assignedProjects.length}</p>
//         <p>Last Logged In: ${formatDate(user.lastLogin)}</p>
//     `;
// }

// function displayAssignedProjects(developerId) {
//     const projects = loadData(PROJECTS_KEY).filter(p => p.teamMembers.includes(developerId));
//     const projectsInfoDiv = document.getElementById('projects-info');
//     if (projects.length === 0) {
//         projectsInfoDiv.innerHTML = '<p>You are not assigned to any projects.</p>';
//     } else {
//         projectsInfoDiv.innerHTML = projects.map(p => `
//             <h3>${p.name}</h3>
//             <p>Bugs:</p>
//             <ul>
//                 ${p.bugs.map(bugId => {
//                     const bug = loadData(BUGS_KEY).find(b => b.id === bugId);
//                     return bug ? `<li>${bug.title} - ${bug.status} - ${bug.severity}</li>` : '';
//                 }).join('')}
//             </ul>
//         `).join('');
//     }
// }

// function closeBug(bugId) {
//     const bugs = loadData(BUGS_KEY);
//     const bugIndex = bugs.findIndex(b => b.id === bugId);
//     if (bugIndex !== -1) {
//         bugs[bugIndex].status = 'Closed';
//         saveData(BUGS_KEY, bugs);
//         alert('Bug closed successfully!');
//         location.reload();
//     }
// }

document.addEventListener('DOMContentLoaded', function() {
    fetch('../data/Users.json')  // Update the path if needed
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const currentUserId = 'dev001'; // Example; replace with actual logic to get the current user's ID
            const developer = data.find(user => user.id === currentUserId && user.role === 'Developer');

            if (developer) {
                document.getElementById('developer-name').textContent = developer.name;
                document.getElementById('developer-email').textContent = developer.email;

                const projectsInfo = document.getElementById('projects-info');
                developer.assignedProjects.forEach(projectId => {
                    const projectDiv = document.createElement('div');
                    projectDiv.className = 'project';
                    projectDiv.innerHTML = `
                        <h4>Project ${projectId}</h4>
                        <p>Status: In Progress</p>
                        <p>Details about Project ${projectId}</p>
                        <button onclick="markBug()">Mark Bug as Closed</button>
                    `;
                    projectsInfo.appendChild(projectDiv);
                });
            } else {
                console.error('No developer found or invalid role.');
            }
        })
        .catch(error => console.error('Error fetching user data:', error));
});

function markBug() {
    alert('Bug marked as closed!');
}




