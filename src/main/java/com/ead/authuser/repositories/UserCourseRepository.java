package com.ead.authuser.repositories;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {
@Query(value = "select * from tb_users_courses where user_user_id = :userId",nativeQuery = true)
    List<UserCourseModel>selectAllUserCourseIntoUser(String userId);
    boolean existsByUserAndCourseId(UserModel userModel, UUID userId);
}
