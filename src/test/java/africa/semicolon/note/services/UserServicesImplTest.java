package africa.semicolon.note.services;

import africa.semicolon.note.data.repositories.UserRepository;
import africa.semicolon.note.dtos.request.*;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.dtos.response.UserResponse;
import africa.semicolon.note.exception.NoteNotFound;
import africa.semicolon.note.exception.UserAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.InputMismatchException;

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
        registerUserRequest1.setUsername("bimbim");
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


        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("bally");
        loginUserRequest.setPassword("password");
        UserResponse response = userServices.login(loginUserRequest);
        assertEquals(1, userRepository.count());
        assertEquals(response.getMessage(), "successful");

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
        registerUserRequest1.setUsername("bimbim");
        registerUserRequest1.setPassword("password1");
        userServices.registerUser(registerUserRequest1);

        LoginUserRequest loginUserRequest1 = new LoginUserRequest();
        loginUserRequest1.setUsername("bimbim");
        loginUserRequest1.setPassword("password1");
        UserResponse response1 =userServices.login(loginUserRequest1);
        assertEquals(response.getMessage(), "successful");
        assertEquals(response1.getMessage(), "successful");

    }
    @Test
    public void registerUser_login_logoutTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("bally");
        userServices.logout(logoutRequest);

        assertFalse(userServices.findByUser("bally").isLoggedIn());

    }

    @Test
    public void registerUser_login_createNote(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("bally");
        userServices.createNote(createNoteRequest);

        NoteRequest createNoteRequest1 = new NoteRequest();
        createNoteRequest1.setTitle("title1");
        createNoteRequest1.setBody("body1");
        createNoteRequest1.setAuthor("bally");
        userServices.createNote(createNoteRequest1);
        assertEquals(2, noteServices.getNoteFor("bally").size());

    }

    @Test
    public void registerTwoUsers_login_createNoteForUsersTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("bally");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("bally");
        userServices.createNote(createNoteRequest);

        RegisterUserRequest registerUserRequest1 = new RegisterUserRequest();
        registerUserRequest1.setUsername("bimbim");
        registerUserRequest1.setPassword("password1");
        userServices.registerUser(registerUserRequest1);

        LoginUserRequest loginUserRequest1 = new LoginUserRequest();
        loginUserRequest1.setUsername("bimbim");
        loginUserRequest1.setPassword("password1");
        userServices.login(loginUserRequest1);

        NoteRequest createNoteRequest1 = new NoteRequest();
        createNoteRequest1.setTitle("title1");
        createNoteRequest1.setBody("body1");
        createNoteRequest1.setAuthor("bimbim");
        userServices.createNote(createNoteRequest1);
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
        userServices.createNote(createNoteRequest);

        UpdateNoteRequest updateNoteRequest = new UpdateNoteRequest();
        updateNoteRequest.setTitle("title");
        updateNoteRequest.setNewTitle("newTitle");
        updateNoteRequest.setBody("body");
        updateNoteRequest.setAuthor("Bally");

        NoteResponse note = userServices.updateNote(updateNoteRequest);
        assertEquals("title", note.getTitle());
        assertEquals("body", note.getBody());
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
        userServices.createNote(createNoteRequest);
        assertEquals(userServices.findNoteByTitle("title").getTitle(),"title");
    }

    @Test
    public void registerUser_login_createNote_deleteNote(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("vero");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("vero");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("vero");
        userServices.createNote(createNoteRequest);

        userServices.deleteNote(createNoteRequest);
        assertEquals(0, noteServices.getAllNote().size());
    }
    @Test
    public void testWhenUserInputEmptyStringAsUserName(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("");
        registerUserRequest.setPassword("password");
        try {
            userServices.registerUser(registerUserRequest);
        }catch(InputMismatchException e){
            assertEquals(e.getMessage(), "Invalid Input");
        }

    }

    @Test
    public void testWhenUserInputEmptyStringAsPassword(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bally");
        registerUserRequest.setPassword("");
        try {
            userServices.registerUser(registerUserRequest);
        }catch(InputMismatchException e){
            assertEquals(e.getMessage(), "Invalid Password provide a Password");
        }
    }

    @Test
    public void testWhenUSerInputEmptyStringAsTitle(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bali");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("Bali");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("Bali");
        try {
            userServices.createNote(createNoteRequest);
        }catch(InputMismatchException e){
            assertEquals(e.getMessage(), "Title not found");
        }
    }

    @Test
    public void testWhenUserInputEmptyStringAsBodyOfNote(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bim");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("bim");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("");
        createNoteRequest.setAuthor("bim");
        try {
            userServices.createNote(createNoteRequest);
        }catch(InputMismatchException e){
            assertEquals(e.getMessage(), "Body not found");
        }

    }
    @Test
    public void testWhenUserInputEmptyStringAsAuthorOfNote(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("bimbim");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("bimbim");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("");
        try {
            userServices.createNote(createNoteRequest);
        }catch(NoteNotFound e){
            assertEquals(e.getMessage(), "Note not found");
        }
    }

    @Test
    public void testWhenUserInputInvalidName(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("B a l l y");
        registerUserRequest.setPassword("password");
        try {
            userServices.registerUser(registerUserRequest);
        }catch(InputMismatchException e){
            assertEquals(e.getMessage(), "Invalid Input");
        }
    }

    @Test
    public void testWhenUserInputInvalidPassword(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("p a s s w o r d");
        try {
            userServices.registerUser(registerUserRequest);
        }catch(InputMismatchException e){
            assertEquals(e.getMessage(), "Invalid Password provide a Password");
        }
    }

    @Test
    public void testWhenUserInputInvalidTitle(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("p a s s w o r d");
        try {
            userServices.registerUser(registerUserRequest);
        }catch(InputMismatchException e){
            assertEquals(e.getMessage(), "Invalid Password provide a Password");
        }
    }
    @Test
    public void testThatWhenUserEnterIntegerAsUsername(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("1 2 3 4 5");
        loginUserRequest.setPassword("password");
        try {
            userServices.login(loginUserRequest);
        }catch(InputMismatchException e){
            assertEquals(e.getMessage(),"Invalid Input");
        }
    }

    @Test
    public void registerUserFindUserByUsernameTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Bally");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);
        assertEquals("Bally", userServices.findByUser("Bally").getUsername());
    }

    @Test
    public void registerUser_login_createNote_logout(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("username");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("username");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("username");
        userServices.createNote(createNoteRequest);

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("username");
        userServices.logout(logoutRequest);

        assertFalse(userServices.findByUser("username").isLoggedIn());
    }
    @Test
    public void registerTwoUsers_login_createNoteForUser_deleteAllTest(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("user");
        registerUserRequest.setPassword("password");
        userServices.registerUser(registerUserRequest);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername("user");
        loginUserRequest.setPassword("password");
        userServices.login(loginUserRequest);

        NoteRequest createNoteRequest = new NoteRequest();
        createNoteRequest.setTitle("title");
        createNoteRequest.setBody("body");
        createNoteRequest.setAuthor("user");
        userServices.createNote(createNoteRequest);

        RegisterUserRequest registerUserRequest1 = new RegisterUserRequest();
        registerUserRequest1.setUsername("bimbim");
        registerUserRequest1.setPassword("password1");
        userServices.registerUser(registerUserRequest1);

        LoginUserRequest loginUserRequest1 = new LoginUserRequest();
        loginUserRequest1.setUsername("bimbim");
        loginUserRequest1.setPassword("password1");
        userServices.login(loginUserRequest1);

        NoteRequest createNoteRequest1 = new NoteRequest();
        createNoteRequest1.setTitle("title1");
        createNoteRequest1.setBody("body1");
        createNoteRequest1.setAuthor("bimbim");
        userServices.createNote(createNoteRequest1);
        noteServices.deleteAll();
        assertEquals(0, noteServices.getAllNote().size());
    }

}