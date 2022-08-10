package com.ead.authuser.controllers;

import com.ead.authuser.dtos.JwtDTO;
import com.ead.authuser.dtos.LoginDTO;
import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.security.JwtProvider;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDTO.UserView.RegistrationPost.class)
                                               @JsonView(UserDTO.UserView.RegistrationPost.class) UserDTO userDTO) {

        log.debug("POST Register user userDTO received {}", userDTO);
        if (userService.existsByUserName(userDTO.getUserName())) {
            log.warn("user {} name is Alread taken", userDTO.getUserName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: UserName is already taken!");
        }
        if (userService.existsByUserEmail(userDTO.getEmail())) {
            log.warn("email {} name is Alread taken", userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is already taken!");
        }

        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role not found"));

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDTO, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(roleModel);

        userService.saveUser(userModel);
        log.debug("POST Register user userDTO saved {}", userDTO.getImageUrl());
        log.info("user saved successfully userId {}", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> authenticateUser(@Valid @RequestBody LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDTO(jwt));
    }

    @GetMapping("/")
    public String index(){
        log.trace("TRACE");
        log.debug("DEBUG");
        log.info("INFO");
        log.warn("WARN");
        log.error("ERROR");
        return "Logging Spring Boot...";
    }


    @PostMapping("/signup/admin/usr")
    public ResponseEntity<Object> registerUserAdmin(@RequestBody @Validated(UserDTO.UserView.RegistrationPost.class)
                                               @JsonView(UserDTO.UserView.RegistrationPost.class) UserDTO userDTO) {

        log.debug("POST Register user userDTO received {}", userDTO);
        if (userService.existsByUserName(userDTO.getUserName())) {
            log.warn("user {} name is Alread taken", userDTO.getUserName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: UserName is already taken!");
        }
        if (userService.existsByUserEmail(userDTO.getEmail())) {
            log.warn("email {} name is Alread taken", userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is already taken!");
        }

        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role not found"));

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDTO, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.ADMIN);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(roleModel);

        userService.saveUser(userModel);
        log.debug("POST Register user userDTO saved {}", userDTO.getImageUrl());
        log.info("user saved successfully userId {}", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}


