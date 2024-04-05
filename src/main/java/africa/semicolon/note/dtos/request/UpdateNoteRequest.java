package africa.semicolon.note.dtos.request;

import lombok.Data;

@Data
public class UpdateNoteRequest {
    private String title;
    private String newTitle;
    private String body;
    private String Author;
}
