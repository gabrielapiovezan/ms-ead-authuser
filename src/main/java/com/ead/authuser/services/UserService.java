package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel userModel);

    UserModel update(UserModel userModel);

    void save(UserModel userModel);

    UserModel saveUser(UserModel userModel);

    public boolean existsByUserName(String userName);

    boolean existsByUserEmail(String email);

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

    UserModel updatePassword(UserModel userModel);

    UserModel updateUser(UserModel userModel);

    UserModel deleteUser(UserModel userModel);
}
