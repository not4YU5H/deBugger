document.addEventListener('DOMContentLoaded', function () {
    const currentUser = getCurrentUser();
    displayUserInfo(currentUser);
    displayAssignedProjects(currentUser.id);
});

function getCurrentUser() {
    // Simulate fetching user data (for testing, you might replace this with actual data retrieval)
    return {
        id: 'tester001',
        name: 'Dana White',
        email: 'dana@example.com',
        assignedProjects: [1],
        lastLogin: '2024-08-24T12:00:00Z'
    };
}

function displayUserInfo(user) {
    const userInfoDiv = document.getElementById('user-info');
    if (userInfoDiv) {
        userInfoDiv.innerHTML = `
            <p>ID: ${user.id}</p>
            <p>Name: ${user.name}</p>
            <p>Email: ${user.email}</p>
            <p>Last Logged In: ${formatDate(user.lastLogin)}</p>
        `;
    } else {
        console.error('Element with ID "user-info" not found.');
    }
}

function displayAssignedProjects(testerId) {
    // Replace this with actual data retrieval in practice
    const projects = loadData('projects'); // Function to load projects data
    const bugs = loadData('bugs'); // Function to load bugs data
    const userProjects = projects.filter(p => p.teamMembers.includes(testerId));
    
    const projectsInfoDiv = document.getElementById('projects-info');
    if (projectsInfoDiv) {
        if (userProjects.length === 0) {
            projectsInfoDiv.innerHTML = '<p>You are not assigned to any projects.</p>';
        } else {
            projectsInfoDiv.innerHTML = userProjects.map(project => `
                <div class="project-info">
                    <h3>${project.name}</h3>
                    <p>Status: ${project.status}</p>
                    <ul>
                        ${project.bugs.map(bugId => {
                            const bug = bugs.find(b => b.id === bugId && b.createdBy === testerId);
                            return bug ? `<li>${bug.title} - ${bug.status}</li>` : '';
                        }).join('')}
                    </ul>
                </div>
            `).join('');
        }
    } else {
        console.error('Element with ID "projects-info" not found.');
    }
}

// Helper function to load data (can be replaced with actual data loading logic)
function loadData(type) {
    const data = {
        projects: [
            {
                "id": 1,
                "name": "Project Alpha",
                "startDate": "2024-08-26",
                "managerId": "pm001",
                "teamMembers": ["dev001", "tester001"],
                "status": "In Progress",
                "bugs": [1, 2]
            },
            {
                "id": 2,
                "name": "Project Beta",
                "startDate": "2024-08-26",
                "managerId": "pm001",
                "teamMembers": ["dev002", "tester002"],
                "status": "In Progress",
                "bugs": [3]
            }
        ],
        bugs: [
            {
                "id": 1,
                "projectId": 1,
                "title": "Login Page Error",
                "description": "Error on login page when submitting the form.",
                "severity": "High",
                "status": "Open",
                "createdBy": "tester001",
                "createdOn": "2024-08-24T12:00:00Z",
                "assignedTo": "dev001"
            },
            {
                "id": 2,
                "projectId": 1,
                "title": "Dashboard Loading Issue",
                "description": "Dashboard takes too long to load.",
                "severity": "Medium",
                "status": "Assigned",
                "createdBy": "tester001",
                "createdOn": "2024-08-24T12:00:00Z",
                "assignedTo": "dev001"
            },
            {
                "id": 3,
                "projectId": 2,
                "title": "Profile Picture Upload",
                "description": "Profile picture upload fails.",
                "severity": "Low",
                "status": "Open",
                "createdBy": "tester002",
                "createdOn": "2024-08-24T12:00:00Z",
                "assignedTo": null
            }
        ]
    };
    return data[type];
}

function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}
