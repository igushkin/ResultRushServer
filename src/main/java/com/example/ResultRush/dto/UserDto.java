package com.example.ResultRush.dto;

import com.example.ResultRush.entity.Usr;
import lombok.Data;

@Data
public class UserDto {

    private Integer id;
    private String login;

    public static Usr toUser(UserDto userDto) {
        var user = new Usr();
        user.setId(userDto.getId());
        user.setUsername(userDto.getLogin());

        return user;
    }

    public static UserDto toUserDto(Usr user) {
        var userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getUsername());

        return userDto;
    }
}