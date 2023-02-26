package com.example.jwtex.service;

import com.example.jwtex.domain.Todo;
import com.example.jwtex.domain.TodoRepository;
import com.example.jwtex.dto.ResponseDTO;
import com.example.jwtex.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class TodoService {
    @Autowired
    private TodoRepository repository;

    public String testService() {
        Todo todo = Todo.builder().title("my first todo").build();
        repository.save(todo);
        Todo result = repository.findById(todo.getId()).get();
        return result.getTitle();
    }

    public List<Todo> create(final Todo todo)  {
        validate(todo);
        repository.save(todo);

        log.info("Entity Id : {} ", todo.getId());
        return repository.findByUserId(todo.getUserId());
    }

    public List<Todo> retrieve (final String userId) {
        return repository.findByUserId(userId);
    }

    public List<Todo> update(final Todo todo) {
        try {
            validate(todo);
            // 넘겨받은 엔티티 id를 이용해 Todo엔티티를 가져올 때 없을 시 에러를 방지하고 null값을 받기 위해 Optional 사용
            final Optional<Todo> original = repository.findById(todo.getId());
            original.ifPresent(i -> {
                i.setTitle(todo.getTitle());
                i.setDone(todo.isDone());
            });
            return retrieve(todo.getUserId());
        } catch(Exception e) {
            return null;
        }
    }

    public List<Todo> delete(final Todo todo) {
        validate(todo);
        try {
            repository.delete(todo);
        } catch(Exception e) {
            // 아이디와 에러메시지 로깅
            log.error("error deleting entity    ID : {}, Error : {}", todo.getId(), e);
            // 컨트롤러로 exception 날리.. 데이터베이스 내부 로직을 캡슐화하기 위해 e 를 리턴하지 않고
            // 새로운 exception 오브젝트 리턴
            throw new RuntimeException("error deleting entity ID : " + todo.getId());
        }
        return retrieve(todo.getUserId());
    }



    // Validation 검증 : 넘어온 entity 가 유효한지 검사하는 로직 나중에 Validator로 클래스 분리하기도 함
    private void validate(final Todo todo) {
        if (todo == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        if (todo.getUserId() == null) {
            log.warn("Unknown User");
            throw new RuntimeException("Unknown User");
        }
    }
}
