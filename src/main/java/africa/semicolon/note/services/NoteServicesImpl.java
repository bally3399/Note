package africa.semicolon.note.services;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.data.model.User;
import africa.semicolon.note.data.repositories.NoteRepository;
import africa.semicolon.note.data.repositories.UserRepository;
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
    private final NoteRepository notes;
    private final UserRepository userRepository;

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

    @Override
    public NoteResponse createNote(NoteRequest createNoteRequest) {
        ValidateNote(createNoteRequest);
        User user = userRepository.findByUsername(createNoteRequest.getAuthor());
        for(Note note : notes.findAll()){
            if(note.getTitle().equals(createNoteRequest.getTitle())) throw new NoteNotFound("Note Title Already Exist");
        }
        if(user != null) user.setLocked(false);
        Note note = map(createNoteRequest);
        NoteResponse response = map(note);
        notes.save(note);
        return response;
    }

    @Override
    public NoteResponse updateNote(UpdateNoteRequest updateNoteRequest) {
        validateUpdate(updateNoteRequest);
        Note foundNote = notes.findNoteByTitle(updateNoteRequest.getTitle());
        if (updateNoteRequest.getNewTitle() != null) foundNote.setTitle(updateNoteRequest.getNewTitle());
        if (updateNoteRequest.getAuthor() != null) foundNote.setAuthor(updateNoteRequest.getAuthor());
        if (updateNoteRequest.getBody() != null) foundNote.setBody(updateNoteRequest.getBody());
        Note savedNote = notes.save(foundNote);
        NoteResponse noteResponse = new NoteResponse();
        noteResponse.setTitle(savedNote.getTitle());
        noteResponse.setBody(savedNote.getBody());
        noteResponse.setAuthor(savedNote.getAuthor());
        noteResponse.setDateCreated(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm:ss").format(savedNote.getDateCreated()));
        return noteResponse;
    }

    @Override
    public String deleteNote(NoteRequest deleteNoteRequest) {
        validate(deleteNoteRequest);
        Note foundNote = notes.findNoteByTitle(deleteNoteRequest.getTitle());
        if (foundNote != null){
            notes.delete(foundNote);
            return "Note Successfully Deleted";
        }
        throw new NoteNotFound("Note not found");
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
