package vinicius.dev.CronoTask.domain.usecases;

import vinicius.dev.CronoTask.dto.UserInputDTO;
import vinicius.dev.CronoTask.dto.UserOutputDTO;
import vinicius.dev.CronoTask.dto.UserPatchDTO;

import java.util.UUID;

public interface UserUseCase {
    UserOutputDTO create(UserInputDTO input);
    UserOutputDTO findById(UUID id);
    UserOutputDTO findByEmail(String email);
    UserOutputDTO update(UUID id, UserInputDTO input);
    UserOutputDTO patch(UUID id, UserPatchDTO patch);
    void delete(UUID id);
}
