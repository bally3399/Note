package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.data.model.User;
import africa.semicolon.note.data.repositories.NoteRepository;
import africa.semicolon.note.data.repositories.UserRepository;
import africa.semicolon.note.dtos.request.NoteRequest;
import africa.semicolon.note.dtos.request.LoginUserRequest;
import africa.semicolon.note.dtos.request.RegisterUserRequest;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.dtos.response.UserResponse;
import africa.semicolon.note.exception.IncorrectPassword;
import africa.semicolon.note.exception.NoteNotFound;
import africa.semicolon.note.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static africa.semicolon.note.utils.Mapper.map;

@Service
public class UserServicesImpl implements UserServices{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Override
    public UserResponse registerUser(RegisterUserRequest registerUserRequest) {
        User user = userRepository.findByUsername(registerUserRequest.getUsername());
        if(user == null){
            User newUser = map(registerUserRequest);
            UserResponse response = map(newUser);
            userRepository.save(newUser);
            return response;
        }
        throw new UserAlreadyExistException("User already exist");
    }

    @Override
    public UserResponse login(LoginUserRequest loginUserRequest) {
        User newUser = map(loginUserRequest);
        UserResponse response = map(newUser);
        if(!newUser.getPassword().equals(loginUserRequest.getPassword())) throw new IncorrectPassword("Incorrect password");
        return response;
    }

    @Override
    public String logout(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new IncorrectPassword("Username is not valid");
        user.setLocked(true);

        return "Logout successfully";
    }

    @Override
    public User findByUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public NoteResponse creatNote(NoteRequest createNoteRequest) {
        User user = userRepository.findByUsername(createNoteRequest.getAuthor());
        user.setLocked(false);
        Note note = map(createNoteRequest);
        NoteResponse response = map(note);
        noteRepository.save(note);
        return response;
    }

    @Override
    public NoteResponse updateNote(NoteRequest updateNoteRequest) {
        User user = userRepository.findByUsername(updateNoteRequest.getAuthor());
        user.setLocked(false);
        Note newNote = map(updateNoteRequest);
        return map(newNote);
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
        Note foundNote = noteRepository.findNoteByTitle(deleteNoteRequest.getTitle());

        if (foundNote != null){
            noteRepository.delete(foundNote);
            return "Note Successfully Deleted";
        }
        throw new NoteNotFound("Note not found");

    }
}
