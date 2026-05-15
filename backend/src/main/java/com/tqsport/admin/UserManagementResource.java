package com.tqsport.admin;

import com.tqsport.auth.User;
import com.tqsport.auth.UserRole;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/admin/users")
@RolesAllowed("ADMIN")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UserManagementResource {
    public record UserRow(Long id, String fullName, String email, UserRole role, boolean active) {}
    public record UserRequest(String fullName, String email, String password, UserRole role, boolean active) {}
    public record RoleUpdate(UserRole role, boolean active) {}

    @GET
    public List<UserRow> users() {
        List<User> rows = User.listAll();
        return rows.stream()
                .map(user -> new UserRow(user.id, user.fullName, user.email, user.role, user.active))
                .toList();
    }

    @POST
    @Transactional
    public UserRow create(UserRequest request) {
        User user = new User();
        user.fullName = request.fullName();
        user.email = request.email().toLowerCase();
        user.passwordHash = "{bcrypt-placeholder}" + request.password();
        user.role = request.role() == null ? UserRole.USER : request.role();
        user.active = request.active();
        user.persist();
        return new UserRow(user.id, user.fullName, user.email, user.role, user.active);
    }

    @PATCH
    @Path("/{id}/role")
    @Transactional
    public UserRow updateRole(@PathParam("id") Long id, RoleUpdate request) {
        User user = User.findById(id);
        user.role = request.role();
        user.active = request.active();
        return new UserRow(user.id, user.fullName, user.email, user.role, user.active);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        User.deleteById(id);
        return Response.noContent().build();
    }
}
