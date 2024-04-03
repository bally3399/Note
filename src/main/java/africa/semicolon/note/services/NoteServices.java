package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;

import java.util.List;

public interface NoteServices {
    List<Note> getNoteFor(String username);

    List<Note> getAllNote();

    void deleteAll();

}
