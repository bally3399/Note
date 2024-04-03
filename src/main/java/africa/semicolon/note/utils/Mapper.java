package africa.semicolon.note.utils;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.data.model.User;
import africa.semicolon.note.dtos.request.NoteRequest;
import africa.semicolon.note.dtos.request.LoginUserRequest;
import africa.semicolon.note.dtos.request.RegisterUserRequest;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.dtos.response.UserResponse;

import java.time.format.DateTimeFormatter;

public class Mapper {
    public static User map(RegisterUserRequest registerUserRequest){
        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(registerUserRequest.getPassword());
        return user;
    }
    public static UserResponse map(User user){
        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());
        response.setMessage("success");
        return response;
    }
    public static User map(LoginUserRequest loginUserRequest){
        User user = new User();
        user.setPassword(loginUserRequest.getPassword());
        user.setUsername(loginUserRequest.getUsername());
        return user;
    }
    public static Note map(NoteRequest createNoteRequest){
        Note note = new Note();
        note.setTitle(createNoteRequest.getTitle());
        note.setBody(createNoteRequest.getBody());
        note.setAuthor(createNoteRequest.getAuthor());
        return note;
    }
    public static NoteResponse map(Note note){
        NoteResponse createNoteResponse = new NoteResponse();
        createNoteResponse.setId(note.getId());
        createNoteResponse.setAuthor(note.getAuthor());
        createNoteResponse.setTitle(note.getTitle());
        createNoteResponse.setBody(note.getBody());
        createNoteResponse.setDateCreated(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm:ss").format(note.getDateCreated()));
        return createNoteResponse;
    }
}
