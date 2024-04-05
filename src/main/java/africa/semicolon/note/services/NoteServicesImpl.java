package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.data.model.User;
import africa.semicolon.note.data.repositories.NoteRepository;
import africa.semicolon.note.data.repositories.UserRepository;
import africa.semicolon.note.dtos.request.NoteRequest;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.exception.NoteNotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;

import static africa.semicolon.note.utils.Mapper.map;

@Service
@AllArgsConstructor
public class NoteServicesImpl implements NoteServices{
    private final NoteRepository notes;
    private final UserRepository users;
    @Override
    public List<Note> getNoteFor(String username) {
        List<Note> userNotes = notes.findByAuthor(username);
        if(userNotes.isEmpty()) throw new NoteNotFound("note not found");
        return userNotes;
    }

    @Override
    public List<Note> getAllNote() {
        return notes.findAll();
    }

    @Override
    public void deleteAll() {
        notes.deleteAll();
    }

}
