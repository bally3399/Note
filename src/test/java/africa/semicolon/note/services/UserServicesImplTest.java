package africa.semicolon.note.services;

import africa.semicolon.note.data.repositories.UserRepository;
import africa.semicolon.note.dtos.request.NoteRequest;
import africa.semicolon.note.dtos.request.LoginUserRequest;
import africa.semicolon.note.dtos.request.RegisterUserRequest;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.dtos.response.UserResponse;
import africa.semicolon.note.exception.UserAlreadyExistException;
import africa.semicolon.note.services.NoteServices;
import africa.semicolon.note.services.UserServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServicesImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServices userServices;
    @Autowired
    private NoteServices noteServices;
    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        noteServices.deleteAll();
    }
    @Test
    public void registerUserTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("password");
        UserResponse response = userServices.registerUser(registerUserRequest);
        assertEquals(1, userRepository.count());
        assertEquals(response.getUsername(), "bally");
    }
    @Test
    public void registerTwoUserTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        RegisterUserRequest registerUserRequest1 = new RegisterUserRequest();
        registerUserRequest1.setUsername("bim bim");
        registerUserRequest1.setPassword("password1");
        userServices.registerUser(registerUserRequest1);
        assertEquals(2, userRepository.count());
    }

    @Test
    public void registerUserWithSameInput_ThrowUserAlreadyExistExceptionTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);
        assertThrows(UserAlreadyExistException.class,() -> userServices.registerUser(registerUserRequest));
    }
    @Test
    public void RegisterUser_loginTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);
        assertEquals(1, userRepository.count());


        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("bally");
        loginUserRequest.setPassword("password");
        UserResponse response = userServices.login(loginUserRequest);
        assertEquals(1, userRepository.count());
        assertEquals(response.getMessage(), "success");

    }
    @Test
    public void registerTwoUser_loginTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("bally");
        loginUserRequest.setPassword("password");
        UserResponse response = userServices.login(loginUserRequest);

        RegisterUserRequest registerUserRequest1 = new RegisterUserRequest();
        registerUserRequest1.setUsername("bim bim");
        registerUserRequest1.setPassword("password1");
        userServices.registerUser(registerUserRequest1);

        LoginUserRequest loginUserRequest1 = new LoginUserRequest();
        loginUserRequest1.setUsername("bim bim");
        loginUserRequest1.setPassword("password1");
        UserResponse response1 =userServices.login(loginUserRequest1);
        assertEquals(response.getMessage(), "success");
        assertEquals(response1.getMessage(), "success");

    }
    @Test
    public void registerUser_login_logoutTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("Bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);
        userServices.logout("Bally");
        assertTrue(userServices.findByUser("Bally").isLocked());

    }

    @Test
    public void registerUser_login_createNote(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("Bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("Bally");
        userServices.creatNote(createNoteRequest);
        assertEquals(1, noteServices.getNoteFor("Bally").size());

    }

    @Test
    public void registerTwoUsers_login_createNoteForUsersTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("Bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("Bally");
        userServices.creatNote(createNoteRequest);

        RegisterUserRequest registerUserRequest1 = new RegisterUserRequest();
        registerUserRequest1.setUsername("bim bim");
        registerUserRequest1.setPassword("password1");
        userServices.registerUser(registerUserRequest1);

        LoginUserRequest loginUserRequest1 = new LoginUserRequest();
        loginUserRequest1.setUsername("bim bim");
        loginUserRequest1.setPassword("password1");
        userServices.login(loginUserRequest1);

        NoteRequest createNoteRequest1 = new NoteRequest();
        createNoteRequest1.setTitle("title1");
        createNoteRequest1.setBody("body1");
        createNoteRequest1.setAuthor("bim bim");
        userServices.creatNote(createNoteRequest1);
        assertEquals(2, noteServices.getAllNote().size());

    }
    @Test
    public void registerUser_login_createNote_updateTheNote(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("Bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("Bally");
        userServices.creatNote(createNoteRequest);

        NoteRequest updateNoteRequest = new NoteRequest();
        updateNoteRequest.setTitle("newTitle");
        updateNoteRequest.setBody("newBody");
        updateNoteRequest.setAuthor("Bally");
        NoteResponse note = userServices.updateNote(updateNoteRequest);
        assertEquals("newTitle", note.getTitle());
        assertEquals("newBody", note.getBody());
        assertEquals(1, noteServices.getNoteFor("Bally").size());
    }
    @Test
    public void registerUser_login_createNote_findNoteByAuthor(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("Bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("Bally");
        userServices.creatNote(createNoteRequest);
        assertEquals(userServices.findNoteByTitle("title").getTitle(),"title");
    }

    @Test
    public void registerUser_login_createNote_deleteNote(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("Bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("Bally");
        userServices.creatNote(createNoteRequest);

        userServices.deleteNote(createNoteRequest);
        assertEquals(0, noteServices.getAllNote().size());
    }
}