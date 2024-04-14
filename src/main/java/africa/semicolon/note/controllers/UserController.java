package africa.semicolon.note.controllers;

import africa.semicolon.note.data.model.Note;
import africa.semicolon.note.dtos.request.*;
import africa.semicolon.note.dtos.response.ApiResponse;
import africa.semicolon.note.dtos.response.NoteResponse;
import africa.semicolon.note.dtos.response.UserResponse;
import africa.semicolon.note.exception.NoteAppException;
import africa.semicolon.note.services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/User")
@AllArgsConstructor
public class UserController {
    private final UserServices userServices;
    @PostMapping("/register")
    public ResponseEntity<?> RegisterUser(@RequestBody RegisterUserRequest registerUserRequest){
        try{
            UserResponse result = userServices.registerUser(registerUserRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }catch (NoteAppException | InputMismatchException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginUserRequest loginUserRequest){
        try{
            UserResponse result = userServices.login(loginUserRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }catch (NoteAppException | InputMismatchException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest){
        try{
            UserResponse result = userServices.logout(logoutRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }catch (NoteAppException | InputMismatchException e ){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> Create(@RequestBody NoteRequest noteRequest){
        try{
            NoteResponse result = userServices.createNote(noteRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }catch (NoteAppException | InputMismatchException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<?> UpdateNote(@RequestBody UpdateNoteRequest updateRequest){
        try{
            NoteResponse result = userServices.updateNote(updateRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }catch (NoteAppException | InputMismatchException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> DeleteNote(@RequestBody NoteRequest deleteNoteRequest){
        try{
            String result = userServices.deleteNote(deleteNoteRequest);
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }catch (NoteAppException |InputMismatchException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllTask(){
        try{
            List<Note> result = userServices.getAllNote();
            return new ResponseEntity<>(new ApiResponse(true, result), CREATED);
        }catch (NoteAppException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
      }


}
