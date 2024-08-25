
const USERS_KEY = 'users';
const PROJECTS_KEY = 'projects';
const BUGS_KEY = 'bugs';
const CURRENT_USER_KEY = 'current_user';

// Function to load data from localStorage
function loadData(key) {
    return JSON.parse(localStorage.getItem(key)) || [];
}

// Function to save data to localStorage
function saveData(key, data) {
    localStorage.setItem(key, JSON.stringify(data));
}

// Get current logged in user
function getCurrentUser() {
    return JSON.parse(localStorage.getItem(CURRENT_USER_KEY));
}

// Set current logged in user
function setCurrentUser(user) {
    localStorage.setItem(CURRENT_USER_KEY, JSON.stringify(user));
}

// Get user by ID
function getUserById(userId) {
    const users = loadData(USERS_KEY);
    return users.find(user => user.id === userId);
}

// Add new user
function addUser(user) {
    const users = loadData(USERS_KEY);
    users.push(user);
    saveData(USERS_KEY, users);
}

// Add new project
function addProject(project) {
    const projects = loadData(PROJECTS_KEY);
    projects.push(project);
    saveData(PROJECTS_KEY, projects);
}

// Add new bug
function addBug(bug) {
    const bugs = loadData(BUGS_KEY);
    bugs.push(bug);
    saveData(BUGS_KEY, bugs);
}

// Utility function to generate unique IDs
function generateId(key) {
    const data = loadData(key);
    return data.length > 0 ? data[data.length - 1].id + 1 : 1;
}

// Utility function to format date
function formatDate(date) {
    const d = new Date(date);
    return `${d.getDate()}/${d.getMonth() + 1}/${d.getFullYear()}`;
}
