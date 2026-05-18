package com.tansen.administrative.core.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.tansen.entity.AuthorityUser;
import com.tansen.administrative.core.constant.JwtTokenConstants;
import com.tansen.repository.AuthorityUserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

    private final AuthorityUserTokenRepository authorityUserTokenRepository;

    public JwtService(AuthorityUserTokenRepository authorityUserTokenRepository1, AuthorityUserTokenRepository authorityUserTokenRepository) {
        this.authorityUserTokenRepository = authorityUserTokenRepository;
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(JWTClaimsSet extraClaims, AuthorityUser authorityUser) {
        return Jwts.builder()
                .claims(extraClaims.getClaims())
                .claim("id",authorityUser.getId())
                .claim("roles",authorityUser.getAuthorities())
                .subject(authorityUser.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(AuthorityUser authorityUsers) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", authorityUsers.getId());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(authorityUsers.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .and()
                .signWith(getKey())
                .compact();
    }

    public void setHttpOnlyCookie(HttpServletResponse response,  String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
//        cookie.setDomain();
        response.addCookie(cookie);
    }

    public void clearCookie(String cookieName, HttpServletResponse response) {
        Cookie clearedCookie = new Cookie(cookieName, null);
        clearedCookie.setPath("/");
//        clearedCookie.setHttpOnly(true);
//        clearedCookie.setSecure(true);
        clearedCookie.setMaxAge(0);
        response.addCookie(clearedCookie);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public  String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        Boolean isTokenValid =authorityUserTokenRepository.findByAccessTokenAndLoggedOutFalse(token).map(vendorUserToken->!vendorUserToken.isLoggedOut()).orElse(false);
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token)) && isTokenValid;
    }

    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        Boolean isTokenValid =authorityUserTokenRepository.findByAccessTokenAndLoggedOutFalse(token).map(vendorUserToken->!vendorUserToken.isLoggedOut()).orElse(false);
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token)) && isTokenValid;
    }

    private JWTClaimsSet.Builder getCommonClaims(UserDetails userDetails) {
        AuthorityUser vendorUser = (AuthorityUser) userDetails;
        return new JWTClaimsSet.Builder()
                .audience(JwtTokenConstants.MUNICIPALITY)
                .subject(JwtTokenConstants.AUTH)
                .issuer(JwtTokenConstants.COSMO)
                .claim(JwtTokenConstants.GROUP, vendorUser.getAuthorityAccessGroup().getName());
    }
    private JWTClaimsSet getClaims(UserDetails userDetails) {
        return getCommonClaims(userDetails).build();
    }
    public String generateAccessToken(UserDetails userDetails) {
        AuthorityUser vendorUser = (AuthorityUser) userDetails;
        return generateAccessToken(getClaims(vendorUser),vendorUser);
    }
}
