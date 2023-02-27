package com.example.jwtex.security;

import com.example.jwtex.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date; // sql 의 Date가 아닌 util 의 Date임에 주의

@Log4j2
@Service
public class TokenProvider {
    private static final String SECRET_KEY =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                    "eyJmb28iOiJiYXIiLCJpYXQiOjE0OTg1ODY1ODAsImV4cCI6MTQ5OTE5MTM4MH0" +
                    "YMS-Z1PmRsjxyrtkLADayLJ99OEz0BzgpFZmeVXSRE8";

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
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
                // payload 에 들어갈 내용들
                .setSubject(user.getId()) // sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    public String validateAndGetUserId(String token) {
        // parseClaimsJws 메서드가 인코딩된 토큰을 Base64로 디코딩 및 파싱
        // -> 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명 후, token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims) 반환, 위조라면 에외 반환
        // 그 중 우리는 userId 가 필요하므로 getBody 호출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8))).build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // 바디 내용물 꺼내기
    }
}
