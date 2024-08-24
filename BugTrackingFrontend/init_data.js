// init_data.js

// Clear existing localStorage data
localStorage.clear();

// Constants for localStorage keys
const USERS_KEY = 'users';
const PROJECTS_KEY = 'projects';
const BUGS_KEY = 'bugs';

// Sample data
const sampleUsers = [
    {
        id: "pm001",
        name: "Alice Johnson",
        email: "alice@example.com",
        password: "password123",
        role: "Project Manager",
        assignedProjects: [1, 2],
        lastLogin: new Date().toISOString(),
    },
    {
        id: "dev001",
        name: "Bob Smith",
        email: "bob@example.com",
        password: "password123",
        role: "Developer",
        assignedProjects: [1],
        lastLogin: new Date().toISOString(),
    },
    {
        id: "dev002",
        name: "Charlie Brown",
        email: "charlie@example.com",
        password: "password123",
        role: "Developer",
        assignedProjects: [2],
        lastLogin: new Date().toISOString(),
    },
    {
        id: "tester001",
        name: "Dana White",
        email: "dana@example.com",
        password: "password123",
        role: "Tester",
        assignedProjects: [1],
        lastLogin: new Date().toISOString(),
    },
    {
        id: "tester002",
        name: "Eve Davis",
        email: "eve@example.com",
        password: "password123",
        role: "Tester",
        assignedProjects: [2],
        lastLogin: new Date().toISOString(),
    },
];

const sampleProjects = [
    {
        id: 1,
        name: "Project Alpha",
        startDate: new Date().toISOString().split("T")[0],
        managerId: "pm001",
        teamMembers: ["dev001", "tester001"],
        status: "In Progress",
        bugs: [1, 2],
    },
    {
        id: 2,
        name: "Project Beta",
        startDate: new Date().toISOString().split("T")[0],
        managerId: "pm001",
        teamMembers: ["dev002", "tester002"],
        status: "In Progress",
        bugs: [3],
    },
];

const sampleBugs = [
    {
        id: 1,
        projectId: 1,
        title: "Login Page Error",
        description: "Error on login page when submitting the form.",
        severity: "High",
        status: "Open",
        createdBy: "tester001",
        createdOn: new Date().toISOString(),
        assignedTo: "dev001",
    },
    {
        id: 2,
        projectId: 1,
        title: "Dashboard Loading Issue",
        description: "Dashboard takes too long to load.",
        severity: "Medium",
        status: "Assigned",
        createdBy: "tester001",
        createdOn: new Date().toISOString(),
        assignedTo: "dev001",
    },
    {
        id: 3,
        projectId: 2,
        title: "Profile Picture Upload",
        description: "Profile picture upload fails.",
        severity: "Low",
        status: "Open",
        createdBy: "tester002",
        createdOn: new Date().toISOString(),
        assignedTo: null,
    },
];

// Save sample data to localStorage
localStorage.setItem(USERS_KEY, JSON.stringify(sampleUsers));
localStorage.setItem(PROJECTS_KEY, JSON.stringify(sampleProjects));
localStorage.setItem(BUGS_KEY, JSON.stringify(sampleBugs));

console.log("Sample data initialized successfully.");
