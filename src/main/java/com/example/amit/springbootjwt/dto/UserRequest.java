package com.example.amit.springbootjwt.dto;

import com.example.amit.springbootjwt.domain.Role;
import com.example.amit.springbootjwt.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(builderClassName = "Builder", setterPrefix = "with")
public class UserRequest {
    private String userName;
    private String password;
    private String email;
    private List<String> roles;

    public User toUser() {
        return User.builder()
                .withEmail(this.email)
                .withPassword(this.password)
                .withUsername(this.userName)
                .withRoles(roles.stream().map(Role::new).collect(toList()))
                .build();
    }
}
