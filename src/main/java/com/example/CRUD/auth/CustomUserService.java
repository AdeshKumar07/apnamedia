package com.example.CRUD.auth;

import com.example.CRUD.entity.User;
import com.example.CRUD.repository.UserRepository;
import com.example.CRUD.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserService implements UserDetailsService{

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username);

        return new CustomUserDetails(user) ;

    }
}
