package com.chisimdi.Banking.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtsUtilService {
    private static final Logger log = LoggerFactory.getLogger(JwtsUtilService.class);
    @Value("${jwt.Secret}")
    public String Key;
    @Value("${jwt.Expiration}")
    public int expirationTime;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(Key.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(int userId,String userName,String roles){
        Map<String,Object>claims=new HashMap<>();
        claims.put("Roles",roles);
        claims.put("UserId",userId);
        return Jwts.builder().setSubject(userName).signWith(getSecretKey()).addClaims(claims).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+expirationTime)).compact();

    }
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }
    public String extractUserName(String token){
        return extractAllClaims(token).getSubject();
    }
    public int extractUserId(String token){
        return extractAllClaims(token).get("UserId",Integer.class);
    }
    public String extractRoles(String token){
        return extractAllClaims(token).get("Roles",String.class);
    }
    public Boolean isTokenValid(String token){
        try {
            extractAllClaims(token);
            log.info("Token valid");
            return !extractAllClaims(token).getExpiration().before(new Date());
        }
        catch (Exception e){
            log.info("Token invalid");
            return false;
        }
    }
}
