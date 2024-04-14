package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.data.model.User;
import africa.semicolon.note.dtos.request.*;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.dtos.response.UserResponse;

import java.util.List;

public interface UserServices {
    UserResponse registerUser(RegisterUserRequest registerUserRequest);

    UserResponse login(LoginUserRequest loginUserRequest);

    UserResponse logout(LogoutRequest logoutRequest);

    User findByUser(String username);

    NoteResponse createNote(NoteRequest createNoteRequest);

    NoteResponse updateNote(UpdateNoteRequest updateNoteRequest);

    Note findNoteByTitle(String title);

    String deleteNote(NoteRequest deleteNoteRequest);

    List<Note> getAllNote();
}
