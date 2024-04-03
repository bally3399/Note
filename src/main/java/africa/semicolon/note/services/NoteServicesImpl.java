package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.data.repositories.NoteRepository;
import africa.semicolon.note.exception.NoteNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServicesImpl implements NoteServices{
    @Autowired
    private NoteRepository repository;
    @Override
    public List<Note> getNoteFor(String username) {
        List<Note> notes = repository.findByAuthor(username);
        if(notes.isEmpty()) throw new NoteNotFound("note not found");
        return notes;
    }

    @Override
    public List<Note> getAllNote() {
        return repository.findAll();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
