package africa.semicolon.note.dtos.request;

import lombok.Data;

@Data
public class NoteRequest {
    private String title;
    private String body;
    private String author;
}
