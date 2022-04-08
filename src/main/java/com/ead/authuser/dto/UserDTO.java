package com.ead.authuser.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class UserDTO {

    public interface UserView{
        interface RegistrationPost{}
        interface UserPut{}
        interface PassWordPut{}
        interface ImagePut{}
    }
    @JsonView(UserView.RegistrationPost.class)
    private String userName;

    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @JsonView({UserView.RegistrationPost.class, UserView.PassWordPut.class})
    private String password;

    @JsonView(UserView.PassWordPut.class)
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
