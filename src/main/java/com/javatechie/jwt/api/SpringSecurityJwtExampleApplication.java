package com.javatechie.jwt.api;

import com.javatechie.jwt.api.entity.User;
import com.javatechie.jwt.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringSecurityJwtExampleApplication {
    @Autowired
    private UserRepository repository;
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

   /* @PostConstruct
    public void initUsers() {
        List<User> users = Stream.of(
                new User(102, "user1", bCryptPasswordEncoder.encode("pwd1"), "user1@gmail.com"),
                new User(103, "user2", bCryptPasswordEncoder.encode("pwd2"), "user2@gmail.com"),
                new User(104, "user3", bCryptPasswordEncoder.encode("pwd3"), "user3@gmail.com")
        ).collect(Collectors.toList());
        repository.saveAll(users);
    }*/

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtExampleApplication.class, args);
    }

}
