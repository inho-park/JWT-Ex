package com.example.jwtex.controller;

import com.example.jwtex.domain.Todo;
import com.example.jwtex.dto.ResponseDTO;
import com.example.jwtex.dto.TodoDTO;
import com.example.jwtex.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService service;

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            // 임시 유저 아이디 변수 생성
            String temporaryUserId = "temporary-user";
            // TodoEntity 로 변환
            Todo entity = TodoDTO.toEntity(dto);
            // id 를 null로 초기화.. 생성 당시에 id 가 없어야 하기 때문
            // 임시 유저 아이디 설정
            entity.setId(null);
            entity.setUserId(temporaryUserId);
            // 서비스를 이용해 TodoEntity 생성
            List<Todo> entities = service.create(entity);
            // 자바 스트림을 이용해 리턴된 엔티티리스트를 DTO리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 TodoDTO 리스트를 이용해 ReponseDTO 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder().error(e.getMessage()).build());
        }
    }


    @GetMapping
    public ResponseEntity<?> retrieveList() {
        try {
            // 임시 유저 아이디 변수 생성
            String temporaryUserId = "temporary-user";
            // 서비스 메서드의 retrieve 메서드를 사용해 Todo리스트를 가져온다
            List<Todo> entities = service.retrieve(temporaryUserId);
            // 자바 스트림을 이용해 리턴된 엔티티 리스트를 DTO리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(e->new TodoDTO(e)).collect(Collectors.toList());
            // 변환된 TodoDTO리스트를 이용해 ResponseDTO 초기화 후 응답엔티티에 담기
            return ResponseEntity.ok().body(ResponseDTO.<TodoDTO>builder().data(dtos).build());

        } catch(Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody TodoDTO dto) {
        try {
            // 임시 유저 아이디 변수 생성
            String temporaryUserId = "temporary-user";
            // TodoEntity 로 변환
            Todo entity = TodoDTO.toEntity(dto);
            // id 를 null로 초기화.. 생성 당시에 id 가 없어야 하기 때문
            entity.setUserId(temporaryUserId);
            // 서비스 메서드의 update 메서드를 사용해 Todo리스트를 수정한다
            List<Todo> entities = service.update(entity);
            // 자바 스트림을 이용해 리턴된 엔티티리스트를 DTO리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 TodoDTO리스트를 이용해 ResponseDTO 초기화 후 응답엔티티에 담기
            return ResponseEntity.ok().body(ResponseDTO.<TodoDTO>builder().data(dtos).build());

        } catch(Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder().error(e.getMessage()).build());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody TodoDTO dto) {
        try {
            // 임시 유저 아이디 변수 생성
            String temporaryUserId = "temporary-user";
            // TodoEntity 로 변환
            Todo entity = TodoDTO.toEntity(dto);
            // id 를 null로 초기화.. 생성 당시에 id 가 없어야 하기 때문
            entity.setUserId(temporaryUserId);
            // 서비스 메서드의 update 메서드를 사용해 Todo리스트를 수정한다
            List<Todo> entities = service.delete(entity);
            // 자바 스트림을 이용해 리턴된 엔티티리스트를 DTO리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 TodoDTO리스트를 이용해 ResponseDTO 초기화 후 응답엔티티에 담기
            return ResponseEntity.ok().body(ResponseDTO.<TodoDTO>builder().data(dtos).build());
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder().error(e.getMessage()).build());
        }
    }
}
