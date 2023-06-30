package com.minsait.api.sicurity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${security.enabled}")
    private String securityEnabled;

    public String generateToken(String username, List<String> authorities, Integer user_id) {
        return Jwts.builder()
                .claim("user_name",username)
                .claim("authorities",authorities)
                .claim("user_id", user_id)
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret.getBytes())
                .compact();
    }

    public String generateToken(String username, ArrayList<String> authorities){
       return this.generateToken(username,  authorities, null);
    }

    public Boolean isSecurityEnabled() {
        return Boolean.getBoolean(securityEnabled);
    }

    public boolean isValidToken(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            String username = (String) claims.get("user_name");
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (username != null && expirationDate != null && now.before(expirationDate)) {
                return true;
            }
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            return (String) claims.get("user_name");
        }
        return null;
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
        }
        catch (Exception e) {
            return null;
        }
    }
}
