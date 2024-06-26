package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.dtos.request.NoteRequest;
import africa.semicolon.note.dtos.request.UpdateNoteRequest;
import africa.semicolon.note.dtos.response.NoteResponse;

import java.util.List;

public interface NoteServices {
    List<Note> getNoteFor(String username);

    List<Note> getAllNote();

    void deleteAll();

    NoteResponse createNote(NoteRequest createNoteRequest);

    NoteResponse updateNote(UpdateNoteRequest updateNoteRequest);

    String deleteNote(NoteRequest deleteNoteRequest);

    Note findNoteByTitle(String title);
}
