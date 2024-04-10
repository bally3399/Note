package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.data.model.User;
import africa.semicolon.note.data.repositories.NoteRepository;
import africa.semicolon.note.data.repositories.UserRepository;
import africa.semicolon.note.dtos.request.*;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.dtos.response.UserResponse;
import africa.semicolon.note.exception.IncorrectPassword;
import africa.semicolon.note.exception.NoteNotFound;
import africa.semicolon.note.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;

import static africa.semicolon.note.utils.Mapper.map;

@Service
public class UserServicesImpl implements UserServices {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private NoteServices noteServices;

    @Override
    public UserResponse registerUser(RegisterUserRequest registerUserRequest) {
        validateRegistration(registerUserRequest);
        User user = userRepository.findByUsername(registerUserRequest.getUsername());
        if (user == null) {
            User newUser = map(registerUserRequest);
            UserResponse response = map(newUser);
            userRepository.save(newUser);
            return response;
        }
        throw new UserAlreadyExistException("User already exist");
    }

    private static void validateRegistration(RegisterUserRequest registerUserRequest) {
        if (!registerUserRequest.getUsername().matches("[a-zA-Z]+")) throw new InputMismatchException("Invalid Input");
        if (registerUserRequest.getPassword().isEmpty())
            throw new InputMismatchException("Invalid Password provide a Password");
    }

    @Override
    public UserResponse login(LoginUserRequest loginUserRequest) {
        validateLogin(loginUserRequest);
        User newUser = map(loginUserRequest);
        UserResponse response = map(newUser);
        newUser.setLoggedIn(true);
        if (!newUser.getPassword().equals(loginUserRequest.getPassword()))
            throw new IncorrectPassword("Incorrect password");
        return response;
    }

    private static void validateLogin(LoginUserRequest loginUserRequest) {
        if (!loginUserRequest.getUsername().matches("[a-zA-Z]+")) throw new InputMismatchException("Invalid Input");
        if (loginUserRequest.getPassword().isEmpty())
            throw new InputMismatchException("Invalid Password provide a Password");
    }

    @Override
    public UserResponse logout(LogoutRequest logoutRequest) {
        User user = userRepository.findByUsername(logoutRequest.getUsername());
        if (user == null) throw new IncorrectPassword("Username is not valid");
        UserResponse userResponse = new UserResponse();
        userResponse.setMessage("Logout successful");
        user.setLoggedIn(false);
        return userResponse;
    }

    @Override
    public User findByUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public NoteResponse creatNote(NoteRequest createNoteRequest) {
        User user = userRepository.findByUsername(createNoteRequest.getAuthor());
        if(!user.isLoggedIn()) throw new NoteNotFound("You have to login first");
        user.setLoggedIn(false);
        return noteServices.createNote(createNoteRequest);
    }


    @Override
    public NoteResponse updateNote(UpdateNoteRequest updateNoteRequest) {
        return noteServices.updateNote(updateNoteRequest);

    }

    @Override
    public Note findNoteByTitle(String title) {
        for (Note note : noteRepository.findAll()) {
            if (note.getTitle().equals(title)) {
                return note;
            }
        }
        throw new NoteNotFound("not found");
    }

    @Override
    public String deleteNote(NoteRequest deleteNoteRequest) {
        return noteServices.deleteNote(deleteNoteRequest);
    }
    @Override
    public List<Note> getAllNote(){
        return noteServices.getAllNote();
    }

}

