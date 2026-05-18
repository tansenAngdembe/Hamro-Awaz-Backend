package com.tansen.administrative.core.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tansen.common.exception.ConflictException;
import com.tansen.administrative.core.constant.JwtTokenConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@Component
public class JwtToken implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(JwtToken.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;

    public String getGroupTypeName() {
        String token = getToken();
        return (String) jwtService.extractAllClaims(token).get(JwtTokenConstants.GROUP);
    }

    public String getToken() {
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public JWTClaimsSet getAllClaimsFromToken(String token) {
        if (token != null && !token.isEmpty() && !"null".equalsIgnoreCase(token)) {
            try {
                SignedJWT signedJWT = decryptToken(token);
                return signedJWT.getJWTClaimsSet();
            } catch (ParseException ex) {
                LOG.error("Failed to parse JWT: {}", ex.getMessage());
                throw new ConflictException("Token is not valid");
            }
        } else {
            LOG.error("No token found in request.");
            throw new ConflictException("No token found in request");
        }
    }

    public SignedJWT decryptToken(String token) {
        try {
            JWEObject jweObject = JWEObject.parse(token);
            DirectDecrypter directDecrypter = new DirectDecrypter(JWT_SECRET_KEY.getBytes("UTF-8"));
            jweObject.decrypt(directDecrypter);
            if (verifySignature(jweObject.getPayload().toSignedJWT())) {
                return jweObject.getPayload().toSignedJWT();
            }
            LOG.error("Invalid Signature");
            return null;
        } catch (ParseException | UnsupportedEncodingException | JOSEException ex) {
            LOG.error("Exception :  {}", ex.getMessage());
            return null;
        }
    }

    public boolean verifySignature(SignedJWT signedJWT) throws JOSEException, ParseException {
        RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
        return signedJWT.verify(new RSASSAVerifier(publicKey));
    }
}
