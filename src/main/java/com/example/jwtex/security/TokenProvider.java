package com.example.jwtex.security;

import com.example.jwtex.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date; // sql 의 Date가 아닌 util 의 Date임에 주의

@Log4j2
@Service
public class TokenProvider {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String create(User user) {
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );
        /*
        { // header
            "alg":"HS512"
        }.
        { // payload
            "sub":"40288093784915d201784916a40c001",
            "iss":"demo app",
            "iat":1595733657,
            "exp":1595733657
        }. // SECRET_KEY 를 이용해 서명한 부분
        "gi6rKUYFI67F8IUGUYFI8YGO86huyfi6gYJ6TIUYHF6U56fgygu5uygk" +
            "hG5YFUYGYIF76ukfygu75FTYTRD5Ytfdy5dr67TUTFR54EEDRFTYGY"
         */
        return Jwts.builder()
                // header 에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SECRET_KEY)
                // payload 에 들어갈 내용들
                .setSubject(user.getId()) // sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }
}
