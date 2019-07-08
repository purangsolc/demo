package com.sports.clip.users;

import com.sports.clip.errors.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.HashSet;

@RestController
@RequestMapping("/api/users")
class UserController {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    UserController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    Page<User> all(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable, OAuth2Authentication authentication) {
        String auth = (String) authentication.getUserAuthentication().getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if (role.equals(User.Role.USER.name())) {
            return repository.findAllByEmail(auth, pageable);
        }
        return repository.findAll(pageable);
    }

    @GetMapping("/search")
    Page<User> search(@RequestParam String email, Pageable pageable, OAuth2Authentication authentication) {
        String auth = (String) authentication.getUserAuthentication().getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if (role.equals(User.Role.USER.name())) {
            return repository.findAllByEmailContainsAndEmail(email, auth, pageable);
        }
        return repository.findByEmailContains(email, pageable);
    }

    @GetMapping("/findByEmail")
    User findByEmail(@RequestParam String email, OAuth2Authentication authentication) {
        return repository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(User.class, "email", email));
    }

    @GetMapping("/{id}")
    User one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
    }

    @PutMapping("/{id}")
    void update(@PathVariable Long id, @Valid @RequestBody User res) {
        User u = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
        res.setPassword(u.getPassword());
        repository.save(res);
    }

    @PostMapping
    User create(@Valid @RequestBody User res) {
        return repository.save(res);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException(User.class, "id", id.toString());
        }
    }

    @PutMapping("/{id}/changePassword")
    void changePassword(@PathVariable Long id, @RequestParam(required = false) String oldPassword, @Valid @Size(min = 3) @RequestParam String newPassword) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
        if (oldPassword == null || oldPassword.isEmpty() || passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.save(user);
        } else {
            throw new ConstraintViolationException("old password doesn't match", new HashSet<>());
        }
    }
}
