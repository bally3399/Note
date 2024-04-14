package africa.semicolon.note.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    boolean isSuccessful;
    Object NoteResponse;
}
