package com.ead.authuser.dtos;

import com.ead.authuser.validation.UserNameConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    public interface UserView{
        interface RegistrationPost{}
        interface UserPut{}
        interface PassWordPut{}
        interface ImagePut{}
    }
    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    @UserNameConstraint(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String userName;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @NotBlank(groups = {UserView.RegistrationPost.class,UserView.PassWordPut.class})
    @Size(min = 6, max = 20, groups = {UserView.RegistrationPost.class,UserView.PassWordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PassWordPut.class})
    private String password;

    @NotBlank(groups = UserView.PassWordPut.class)
    @Size(min = 6, max = 60, groups = UserView.PassWordPut.class)
    @JsonView(UserView.PassWordPut.class)
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @NotBlank(groups = UserView.ImagePut.class)
    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
