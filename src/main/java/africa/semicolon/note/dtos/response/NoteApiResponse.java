package africa.semicolon.note.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteApiResponse {
    boolean isSuccessful;
    Object NoteResponse;
}
