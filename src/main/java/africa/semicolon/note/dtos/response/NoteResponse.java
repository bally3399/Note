package africa.semicolon.note.dtos.response;

import lombok.Data;

@Data
public class NoteResponse {
    private String id;
    private String title;
    private String body;
    private String author;
    private String dateCreated;
}
