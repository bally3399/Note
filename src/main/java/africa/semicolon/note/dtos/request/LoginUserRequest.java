package africa.semicolon.note.dtos.request;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String username;
    private String password;
}
