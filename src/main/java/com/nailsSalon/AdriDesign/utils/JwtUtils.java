package com.nailsSalon.AdriDesign.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nailsSalon.AdriDesign.payment.PaymentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    @Value("${security.jwt.private.key}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    //Creacion de Tokens
    public String createToken(Authentication authentication){

        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        //Queda en el context holder
        String username = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withClaim("authorities",authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 18000000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
        return jwtToken;
    }

    //Decodificar y validar nuestros tokens
    public DecodedJWT validateToken( String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();
            //Si todo esta ok, no genera ninguna excepcion y nos devuelve el JWT decodificado
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        }
        catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Invalid token. Not authorized");
        }
    }

    //Metodo para obtener el usuario/username
    public String extractUsername(DecodedJWT decodedJWT){
        logger.error("username in JWTUtil " + decodedJWT.getSubject());
        return decodedJWT.getSubject().toString();
        // Suponiendo que el username está almacenado en el 'subject' del token
        //return decodedJWT.getSubject();

        // Si el username está en otro claim, por ejemplo 'username', usa:
        //return decodedJWT.getClaim("username").asString();
    }

    //Obtener un Claim en particular
    public Claim getSpecificClaim( DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    //Obtener los Claims
    public Map<String, Claim> returnAllClaims( DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
