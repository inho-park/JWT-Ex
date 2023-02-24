package com.example.jwtex.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id; // 유저에게 고유하게 부여되는 id

    @Column(nullable = false)
    private String username; // 아이디로 사용할 유저네임. Ex) 이메일 혹은 닉네임 같은 문자열

    private String password;

    private String role; // 사용자의 롤 Ex) 어드민, 유저

    private String authProvider; // 이후 OAuth에서 사용할 유저 정보 제공자 Ex) 네이버, 카카오톡
}
