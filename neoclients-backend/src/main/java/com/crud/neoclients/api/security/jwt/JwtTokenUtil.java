package com.crud.neoclients.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_CREATED = "created";
    static final String CLAIM_KEY_EXPIRED = "exp";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            Map<String, Object> userMap = convertToStringToHashMap(claims.getSubject());
            username = userMap.get("username").toString();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private HashMap<String, Object> convertToStringToHashMap(String text) {
        HashMap<String, Object> data = new HashMap<>();
        Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
        String[] split = p.split(text);
        for (int i = 1; i + 2 <= split.length; i += 2) {
            data.put(split[i], split[i + 1]);
        }
        return data;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;

        try {

            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();

        } catch (Exception e) {
            expiration = null;
        }

        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;

        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(CLAIM_KEY_USERNAME, userDetails);

        final Date createdDate = new Date();
        claims.put(CLAIM_KEY_CREATED, createdDate);

        return doGenerateToken(claims);
    }

    private String doGenerateToken(Map<String, Object> claims) {
        final Date createdDate = (Date) claims.get(CLAIM_KEY_CREATED);
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshToken;

        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshToken = doGenerateToken(claims);
        } catch (Exception e) {
            refreshToken = null;
        }

        return refreshToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final String username = getUsernameFromToken(token);

        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

}
