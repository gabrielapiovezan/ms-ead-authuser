package com.ead.authuser.security;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUserName(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserDetailsImpl.build(userModel);
    }


    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
        return UserDetailsImpl.build(userModel);
    }
}
