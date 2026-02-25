package com.example.CRUD.service;


import com.example.CRUD.dto.UserDto;
import com.example.CRUD.dto.UserResponseDto;
import com.example.CRUD.entity.*;
import com.example.CRUD.repository.TokenRepository;
import com.example.CRUD.repository.UserInfoRepository;
import com.example.CRUD.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;



    private User toEntity(UserDto userDto){
        User user = new User();
        UserInfo userInfo = new UserInfo();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());

        userInfo.setName(userDto.getName());
        userInfo.setPhone(userDto.getPhone());
        userInfo.setProfilePic(userDto.getProfilePic());
        userInfo.setUser(user);
        user.setUserInfo(userInfo);

        return user;
    }

    private UserDto toDto(User user){
        UserDto userDto = new UserDto();
        UserInfo userInfo = user.getUserInfo();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(userInfo.getPhone());
        userDto.setProfilePic(userInfo.getProfilePic());
        userDto.setName(userInfo.getName());

        return userDto;

    }

    private UserResponseDto toResponseDto(User user){
        UserResponseDto userDto = new UserResponseDto();
        UserInfo userInfo = user.getUserInfo();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setName(userInfo.getName());

        return userDto;
    }

    public UserResponseDto createUser(UserDto userDto) throws MessagingException {


        User user   = toEntity(userDto) ;
        user.setVisible(false);

        user.getUserInfo().setRole(Role.ROLE_UNVERIFIED);

        Token token = new Token();
        token.setUser(user);
        UUID uuid = UUID.randomUUID();
        token.setToken(uuid.toString());


        sendVerificationEmail(user, uuid.toString());


        userRepository.save(user);
        userInfoRepository.save(user.getUserInfo());
        tokenRepository.save(token);

        return toResponseDto(user);

    }

    public List<UserResponseDto> getUsers() {

        List<User> users  = userRepository.findAll();
        List<UserResponseDto> response = new ArrayList<>();
        for (User user : users) {
            response.add(toResponseDto(user));
        }
        return response;
    }

    public UserResponseDto getuser(long id){
        User  user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return toResponseDto(user);

    }


    public UserResponseDto updateUserPartial(UserDto userDto) {

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserInfo userInfo = user.getUserInfo();

        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }

        if (userDto.getName() != null) {
            userInfo.setName(userDto.getName());
        }

        if (userDto.getPhone() != null) {
            userInfo.setPhone(userDto.getPhone());
        }

        if (userDto.getProfilePic() != null) {
            userInfo.setProfilePic(userDto.getProfilePic());
        }

        userRepository.save(user);
        userInfoRepository.save(userInfo);

        return toResponseDto(user);
    }



    public UserResponseDto deleteUser(long id) {
        User  user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        return toResponseDto(user);
    }


    public void toggleVisible(String username){
        User user = userRepository.findByUsername(username) ;
        user.setVisible(!user.isVisible());
    }


    public void sendVerificationEmail(User user , String token)
            throws MessagingException {

        String verificationUrl =
                "http://localhost:8080/api/auth/verify?token=" + token;

        String htmlContent = """
            <html>
                <body>
                    <h2>Email Verification</h2>
                    <p>Please click the button below to verify your account:</p>
                    <a href="%s"
                       style="padding:10px 20px;
                              background-color:#4CAF50;
                              color:white;
                              text-decoration:none;
                              border-radius:5px;">
                       Verify Account
                    </a>
                </body>
            </html>
            """.formatted(verificationUrl);

        String email = user.getEmail() ;

        emailService.sendHtmlMail(email, "Verify Your Account", htmlContent);
    }





}