package com.example.CRUD.dto;


import com.example.CRUD.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto {
    String name ;
    String email;
    String username ;
    public static UserResponseDto from(User user) {
        return new UserResponseDto();
    }
}
