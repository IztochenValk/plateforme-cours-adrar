package com.adrar.skillforge.service.user;

import com.adrar.skillforge.dto.user.CreateUserRequest;
import com.adrar.skillforge.dto.user.PatchUserRequest;
import com.adrar.skillforge.dto.user.UpdateUserRequest;
import com.adrar.skillforge.entity.User;
import com.adrar.skillforge.entity.Role;

import java.util.Set;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User findByPublicId(UUID publicId);
    List<User> findAll();
    User create(CreateUserRequest request);
    User update(UUID publicId, UpdateUserRequest request);
    User patch(UUID publicId, PatchUserRequest request);
    void deleteByPublicId(UUID publicId);
    boolean emailExists(String email);

    Set<Role> setRoles(UUID publicId, Set<String> roles);
    User addRole(UUID publicId, String role);
    User removeRole(UUID publicId, String role);


}
