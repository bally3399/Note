package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.data.repositories.NoteRepository;
import africa.semicolon.note.dtos.request.NoteRequest;
import africa.semicolon.note.dtos.request.UpdateNoteRequest;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.exception.NoteNotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;

import static africa.semicolon.note.utils.Mapper.map;

@Service
@AllArgsConstructor
public class NoteServicesImpl implements NoteServices{
    private final NoteRepository noteRepository;
    @Override
    public List<Note> getNoteFor(String username) {
        List<Note> userNotes = noteRepository.findByAuthor(username);
        if(userNotes.isEmpty()) throw new NoteNotFound("note not found");
        return userNotes;
    }

    @Override
    public List<Note> getAllNote() {
        return noteRepository.findAll();
    }
    @Override
    public void deleteAll() {
        noteRepository.deleteAll();
    }

    @Override
    public NoteResponse createNote(NoteRequest createNoteRequest) {
        ValidateNote(createNoteRequest);
        for(Note note : noteRepository.findAll()){
            if(note.getTitle().equals(createNoteRequest.getTitle())) throw new NoteNotFound("Note Title Already Exist");
        }
        Note note = map(createNoteRequest);
        NoteResponse response = map(note);
        noteRepository.save(note);
        return response;
    }

    @Override
    public NoteResponse updateNote(UpdateNoteRequest updateNoteRequest) {
        validateUpdate(updateNoteRequest);
        Note foundNote = noteRepository.findNoteByTitle(updateNoteRequest.getTitle());
        Note savedNote = noteRepository.save(foundNote);
        NoteResponse noteResponse = new NoteResponse();
        noteResponse.setTitle(savedNote.getTitle());
        noteResponse.setBody(savedNote.getBody());
        noteResponse.setAuthor(savedNote.getAuthor());
        noteResponse.setDateCreated(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm:ss").format(savedNote.getDateCreated()));
        return noteResponse;
    }

    @Override
    public String  deleteNote(NoteRequest deleteNoteRequest) {
        validate(deleteNoteRequest);
        Note foundNote = noteRepository.findNoteByTitle(deleteNoteRequest.getTitle());
        if (foundNote != null){
            noteRepository.delete(foundNote);
            return "Note Successfully Deleted";
        }
        throw new NoteNotFound("Note not found");
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

    private static void validate(NoteRequest deleteNoteRequest) {
        if(deleteNoteRequest.getAuthor().trim().isEmpty()) throw new InputMismatchException("Invalid Input");
        if(deleteNoteRequest.getTitle().trim().isEmpty())throw new InputMismatchException("Title not found");
        if(deleteNoteRequest.getBody().trim().isEmpty())throw new InputMismatchException("Body not found");
    }

    private static void validateUpdate(UpdateNoteRequest updateNoteRequest) {
        if(updateNoteRequest.getAuthor().trim().isEmpty()) throw new InputMismatchException("Invalid Input");
        if(updateNoteRequest.getTitle().trim().isEmpty())throw new InputMismatchException("Title not found");
        if(updateNoteRequest.getBody().trim().isEmpty())throw new InputMismatchException("Body not found");
    }


    private static void ValidateNote(NoteRequest createNoteRequest) {
        if(createNoteRequest.getAuthor().trim().isEmpty())throw new InputMismatchException("Invalid Input");
        if(createNoteRequest.getTitle().trim().isEmpty())throw new InputMismatchException("Title not found");
        if(createNoteRequest.getBody().trim().isEmpty())throw new InputMismatchException("Body not found");
    }


}
