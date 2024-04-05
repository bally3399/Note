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

import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;

import static africa.semicolon.note.utils.Mapper.map;

@Service
public class UserServicesImpl implements UserServices{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Override
    public UserResponse registerUser(RegisterUserRequest registerUserRequest) {
        validateRegistration(registerUserRequest);
        User user = userRepository.findByUsername(registerUserRequest.getUsername());
        if(user == null){
            User newUser = map(registerUserRequest);
            UserResponse response = map(newUser);
            userRepository.save(newUser);
            return response;
        }
        throw new UserAlreadyExistException("User already exist");
    }

    private static void validateRegistration(RegisterUserRequest registerUserRequest) {
        if(!registerUserRequest.getUsername().matches("[a-zA-Z]+"))throw new InputMismatchException("Invalid Input");
        if(registerUserRequest.getPassword().isEmpty())throw new InputMismatchException("Invalid Password provide a Password");
    }

    @Override
    public UserResponse login(LoginUserRequest loginUserRequest) {
        validateLogin(loginUserRequest);
        User newUser = map(loginUserRequest);
        UserResponse response = map(newUser);
        newUser.setLocked(false);
        if(!newUser.getPassword().equals(loginUserRequest.getPassword())) throw new IncorrectPassword("Incorrect password");
        return response;
    }

    private static void validateLogin(LoginUserRequest loginUserRequest) {
        if(!loginUserRequest.getUsername().matches("[a-zA-Z]+"))throw new InputMismatchException("Invalid Input");
        if(loginUserRequest.getPassword().isEmpty())throw new InputMismatchException("Invalid Password provide a Password");
    }

    @Override
    public UserResponse logout(LogoutRequest logoutRequest) {
        User user = userRepository.findByUsername(logoutRequest.getUsername());
        if (user == null) throw new IncorrectPassword("Username is not valid");
        UserResponse userResponse = new UserResponse();
        userResponse.setMessage("Logout successful");
        user.setLocked(true);
        return userResponse;
    }

    @Override
    public User findByUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public NoteResponse creatNote(NoteRequest createNoteRequest) {
        ValidateNote(createNoteRequest);
        User user = userRepository.findByUsername(createNoteRequest.getAuthor());
        for(Note note : noteRepository.findAll()){
            if(note.getTitle().equals(createNoteRequest.getTitle())) throw new NoteNotFound("Note Title Already Exist");
        }
        if(user != null) user.setLocked(false);
        Note note = map(createNoteRequest);
        NoteResponse response = map(note);

        noteRepository.save(note);
        return response;
    }

    private static void ValidateNote(NoteRequest createNoteRequest) {
        if(createNoteRequest.getAuthor().trim().isEmpty())throw new InputMismatchException("Invalid Input");
        if(createNoteRequest.getTitle().trim().isEmpty())throw new InputMismatchException("Title not found");
        if(createNoteRequest.getBody().trim().isEmpty())throw new InputMismatchException("Body not found");
    }

    @Override
    public NoteResponse updateNote(UpdateNoteRequest updateNoteRequest) {
        validateUpdate(updateNoteRequest);
        Note foundNote = noteRepository.findNoteByTitle(updateNoteRequest.getTitle());
        if (updateNoteRequest.getNewTitle() != null) foundNote.setTitle(updateNoteRequest.getNewTitle());
        if (updateNoteRequest.getAuthor() != null) foundNote.setAuthor(updateNoteRequest.getAuthor());
        if (updateNoteRequest.getBody() != null) foundNote.setBody(updateNoteRequest.getBody());
        Note savedNote = noteRepository.save(foundNote);
        NoteResponse noteResponse = new NoteResponse();
        noteResponse.setTitle(savedNote.getTitle());
        noteResponse.setBody(savedNote.getBody());
        noteResponse.setAuthor(savedNote.getAuthor());
        noteResponse.setDateCreated(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm:ss").format(savedNote.getDateCreated()));
        return noteResponse;
    }

    private static void validateUpdate(UpdateNoteRequest updateNoteRequest) {
        if(updateNoteRequest.getAuthor().trim().isEmpty()) throw new InputMismatchException("Author not found");
        if(updateNoteRequest.getTitle().trim().isEmpty())throw new InputMismatchException("Title not found");
        if(updateNoteRequest.getBody().trim().isEmpty())throw new InputMismatchException("Body not found");
    }

    @Override
    public Note findNoteByTitle(String title) {
        for(Note note: noteRepository.findAll()){
            if(note.getTitle().equals(title)){
                return note;
            }
        }
        throw new NoteNotFound("not found");
    }
    @Override
    public String deleteNote(NoteRequest deleteNoteRequest) {
        validate(deleteNoteRequest);
        Note foundNote = noteRepository.findNoteByTitle(deleteNoteRequest.getTitle());

        if (foundNote != null){
            noteRepository.delete(foundNote);
            return "Note Successfully Deleted";
        }
        throw new NoteNotFound("Note not found");

    }
    private static void validate(NoteRequest deleteNoteRequest) {
        if(deleteNoteRequest.getAuthor().isEmpty()) throw new InputMismatchException("Author not found");
        if(deleteNoteRequest.getTitle().isEmpty())throw new InputMismatchException("Title not found");
        if(deleteNoteRequest.getBody().isEmpty())throw new InputMismatchException("Body not found");
    }
}
