package vinicius.dev.CronoTask.domain.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinicius.dev.CronoTask.domain.entities.User;
import vinicius.dev.CronoTask.domain.repositories.UserRepository;
import vinicius.dev.CronoTask.dto.UserInputDTO;
import vinicius.dev.CronoTask.dto.UserOutputDTO;
import vinicius.dev.CronoTask.dto.UserPatchDTO;
import vinicius.dev.CronoTask.infra.exceptions.EmailAlreadyExistsException;
import vinicius.dev.CronoTask.infra.exceptions.ResourceNotFoundException;
import vinicius.dev.CronoTask.infra.mappers.UserMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUseCaseImpl implements UserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserOutputDTO create(UserInputDTO input) {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + input.getEmail());
        }

        User user = User.create(
                input.getName(),
                input.getEmail(),
                input.getPassword()
        );

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UserOutputDTO findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public UserOutputDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserOutputDTO update(UUID id, UserInputDTO input) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!user.getEmail().equals(input.getEmail()) && 
            userRepository.existsByEmail(input.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + input.getEmail());
        }

        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword());

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Transactional
    public UserOutputDTO patch(UUID id, UserPatchDTO patch) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (patch.getName() != null) {
            user.setName(patch.getName());
        }
        if (patch.getEmail() != null) {
            if (!user.getEmail().equals(patch.getEmail()) && 
                userRepository.existsByEmail(patch.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists: " + patch.getEmail());
            }
            user.setEmail(patch.getEmail());
        }
        if (patch.getPassword() != null) {
            user.setPassword(patch.getPassword());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Transactional
    public void delete(UUID id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
