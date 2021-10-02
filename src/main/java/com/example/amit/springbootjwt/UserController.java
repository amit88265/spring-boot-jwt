package com.example.amit.springbootjwt;

import com.example.amit.springbootjwt.domain.User;
import com.example.amit.springbootjwt.dto.UserRequest;
import com.example.amit.springbootjwt.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.signIn(userRequest.getUserName(), userRequest.getPassword()));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequest userRequest) {
        User user = userRequest.toUser();
        return ResponseEntity.status(CREATED).body(userService.singUp(user));
    }
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable(value = "username") String username) {
        return ResponseEntity.status(NO_CONTENT).body(userService.delete(username));
    }
    @GetMapping("/{username}")
    @PreAuthorize(("hasRole('ROLE_ADMIN')"))
    public ResponseEntity<UserResponse> getById(@PathVariable(value = "username") String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(UserResponse.from(user));
    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<UserResponse> whoAmI(HttpServletRequest req) {
        User user = userService.whoAmI(req);
        return ResponseEntity.status(CREATED).body(UserResponse.from(user));
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }
}
