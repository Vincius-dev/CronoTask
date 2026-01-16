package vinicius.dev.CronoTask.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vinicius.dev.CronoTask.domain.usecases.UserUseCase;
import vinicius.dev.CronoTask.dto.UserInputDTO;
import vinicius.dev.CronoTask.dto.UserOutputDTO;
import vinicius.dev.CronoTask.dto.UserPatchDTO;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutputDTO create(@RequestBody UserInputDTO input) {
        return userUseCase.create(input);
    }

    @GetMapping("/{id}")
    public UserOutputDTO findById(@PathVariable UUID id) {
        return userUseCase.findById(id);
    }

    @GetMapping("/email/{email}")
    public UserOutputDTO findByEmail(@PathVariable String email) {
        return userUseCase.findByEmail(email);
    }

    @PutMapping("/{id}")
    public UserOutputDTO update(@PathVariable UUID id, @RequestBody UserInputDTO input) {
        return userUseCase.update(id, input);
    }

    @PatchMapping("/{id}")
    public UserOutputDTO patch(@PathVariable UUID id, @RequestBody UserPatchDTO patch) {
        return userUseCase.patch(id, patch);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userUseCase.delete(id);
    }
}
