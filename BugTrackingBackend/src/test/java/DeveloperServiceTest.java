import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.dao.BugTrackingDao;
import com.codefury.exception.BugNotFoundException;
import com.codefury.exception.InvalidTokenException;
import com.codefury.exception.NoProjectsAssignedException;
import com.codefury.service.DeveloperServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeveloperServiceTest {

    @Mock
    private BugTrackingDao bugTrackingDao;

    @InjectMocks
    private DeveloperServiceImpl developerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchUserInfo_Success() throws InvalidTokenException {
        String token = "validToken";
        User expectedUser = new User(); // Set user properties as needed
        when(bugTrackingDao.fetchUserInfo(token)).thenReturn(expectedUser);

        User result = developerService.fetchUserInfo(token);

        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(bugTrackingDao, times(1)).fetchUserInfo(token);
    }

    @Test
    public void testFetchUserInfo_InvalidToken() throws InvalidTokenException {
        String token = "invalidToken";
        when(bugTrackingDao.fetchUserInfo(token)).thenThrow(new InvalidTokenException("Invalid token"));

        assertThrows(InvalidTokenException.class, () -> developerService.fetchUserInfo(token));
    }

    @Test
    public void testFetchProjectDetailsByUser_Success() throws NoProjectsAssignedException {
        int userId = 1;
        List<Project> expectedProjects = new ArrayList<>();
        // Add some projects to the list if needed
        when(bugTrackingDao.fetchProjectInfoByUserId(userId)).thenReturn(expectedProjects);

        List<Project> result = developerService.fetchProjectDetailsByUser(userId);

        assertNotNull(result);
        assertEquals(expectedProjects, result);
        verify(bugTrackingDao, times(1)).fetchProjectInfoByUserId(userId);
    }

    @Test
    public void testFetchProjectDetailsByUser_NoProjectsAssigned() throws NoProjectsAssignedException {
        int userId = 1;
        when(bugTrackingDao.fetchProjectInfoByUserId(userId)).thenThrow(new NoProjectsAssignedException("No projects assigned"));

        assertThrows(NoProjectsAssignedException.class, () -> developerService.fetchProjectDetailsByUser(userId));
    }

    @Test
    public void testMarkBugForClose_Success() throws BugNotFoundException {
        int bugId = 1;
        when(bugTrackingDao.markGivenBugForClose(bugId)).thenReturn(true);

        boolean result = developerService.markBugForClose(bugId);

        assertTrue(result);
        verify(bugTrackingDao, times(1)).markGivenBugForClose(bugId);
    }

    @Test
    public void testMarkBugForClose_BugNotFound() throws BugNotFoundException {
        int bugId = 1;
        when(bugTrackingDao.markGivenBugForClose(bugId)).thenThrow(new BugNotFoundException("Bug not found"));

        assertThrows(BugNotFoundException.class, () -> developerService.markBugForClose(bugId));
    }
}
