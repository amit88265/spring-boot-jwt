package com.example.amit.springbootjwt.dto;

import com.example.amit.springbootjwt.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(builderClassName = "Builder", setterPrefix = "with")
public class UserResponse {
    private Long id;
    private String userName;
    private String password;
    private String email;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .withEmail(user.getEmail())
                .withId(Long.valueOf(user.getId()))
                .withUserName(user.getUsername())
                .withPassword(user.getPassword())
                .build();
    }
}
