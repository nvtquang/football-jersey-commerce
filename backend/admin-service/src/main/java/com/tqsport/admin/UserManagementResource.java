package com.tqsport.admin;

import com.tqsport.auth.User;
import com.tqsport.auth.UserRole;
import com.tqsport.auth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/api/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserManagementResource {
    public record UserRow(Long id, String fullName, String email, UserRole role, boolean active) {}
    public record UserRequest(String fullName, String email, String password, UserRole role, boolean active) {}
    public record RoleUpdate(UserRole role, boolean active) {}

    private final UserRepository users;

    public UserManagementResource(UserRepository users) {
        this.users = users;
    }

    @GetMapping
    public List<UserRow> users() {
        return users.findAll().stream().map(this::toRow).toList();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserRow create(@RequestBody UserRequest request) {
        User user = new User();
        user.fullName = request.fullName();
        user.email = request.email().toLowerCase(Locale.ROOT).trim();
        user.passwordHash = "{bcrypt-placeholder}" + (request.password() == null || request.password().isBlank() ? "12345678" : request.password());
        user.role = request.role() == null ? UserRole.USER : request.role();
        user.active = request.active();
        return toRow(users.save(user));
    }

    @PatchMapping(value = "/{id}/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserRow updateRole(@PathVariable Long id, @RequestBody RoleUpdate request) {
        User user = users.findById(id).orElseThrow();
        user.role = request.role() == null ? user.role : request.role();
        user.active = request.active();
        return toRow(users.save(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        users.deleteById(id);
    }

    private UserRow toRow(User user) {
        return new UserRow(user.id, user.fullName, user.email, user.role, user.active);
    }
}
