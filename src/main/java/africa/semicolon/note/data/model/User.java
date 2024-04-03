package africa.semicolon.note.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Data
@Document("Users")
public class User {
    private String username;
    private String password;
    private boolean isLocked = true;
    @DBRef
    private List<Note> notes = new ArrayList<>();
}
