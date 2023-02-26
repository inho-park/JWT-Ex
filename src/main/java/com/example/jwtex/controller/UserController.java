package com.example.jwtex.controller;

import com.example.jwtex.domain.User;
import com.example.jwtex.dto.ResponseDTO;
import com.example.jwtex.dto.UserDTO;
import com.example.jwtex.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        try {
            if (dto == null || dto.getPassword() == null)
                throw new RuntimeException("Invalid Password value");

            // 요청을 이용해 저장할 유저 만들기
            User user = User.builder()
                    .username(dto.getUsername())
                    .password(dto.getPassword())
                    .build();

            //  서비스를 이용해 리포지터리에 유저 저장
            User registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);

        } catch(Exception e) {
            // 유저 정보는 항상 하나이므로 리스트로 만들어야 하는 ReponseDTO 를 사용하지 않고 UserDTO 를 리턴
            ResponseDTO reponseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(reponseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO dto) {
        User user = userService.getByCredentials(
                dto.getUsername(),
                dto.getPassword()
        );

        if(user != null) {
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);

        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

//    @PostMapping("/todo")
//    public ReponseDTO<TodoDTO>
}
