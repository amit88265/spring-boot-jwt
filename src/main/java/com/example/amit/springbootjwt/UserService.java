package com.example.amit.springbootjwt;

import com.example.amit.springbootjwt.domain.User;
import com.example.amit.springbootjwt.exception.InvalidCredentialsException;
import com.example.amit.springbootjwt.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.amit.springbootjwt.Constant.USER_DOES_NOT_EXISTS;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public String signIn(String userName, String password) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            System.out.println("auth = " + auth);
            return jwtTokenUtil.createToken(userName, userRepository.findByUsername(userName).getRoles());
        } catch (AuthenticationException e) {
            System.out.println("e =================================== " + e);
            throw new InvalidCredentialsException("invalid username/password");
        }
    }

    public String singUp(User user) {
        User savedUser = userRepository.findByUsername(user.getUsername());
        if (Objects.nonNull(savedUser)) {
            throw new EntityNotFoundException("User already exists with this username");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser1 = userRepository.save(user);
        return jwtTokenUtil.createToken(savedUser1.getUsername(), savedUser1.getRoles());
    }

    public boolean delete(String username) {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new EntityNotFoundException(USER_DOES_NOT_EXISTS);
        }
        userRepository.delete(user);
        return true;
    }

    public User getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new EntityNotFoundException(USER_DOES_NOT_EXISTS);
        }
        return user;
    }

    public User whoAmI(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenUtil.getUsername(jwtTokenUtil.resolveToken(req)));
    }

    public String refresh(String username) {
        return jwtTokenUtil.createToken(username, new ArrayList<>());
    }
}
