package africa.semicolon.note.data.repositories;

import africa.semicolon.note.data.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByAuthor(String username);
}
