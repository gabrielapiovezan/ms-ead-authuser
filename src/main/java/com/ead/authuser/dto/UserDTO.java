package com.ead.authuser.dto;

import com.ead.authuser.enums.UserStatus;
import lombok.Data;

@Data
public class UserDTO {

    private String userName;
    private String email;
    private String password;
    private String oldPassword;
    private UserStatus userStatus;
    private String fullName;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;

}
