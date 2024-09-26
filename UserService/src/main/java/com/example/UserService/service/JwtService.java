package com.example.UserService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // generate secret key
    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.expiration_time_hour}")
    private String tokenExpirationTimeHour;


//
//    public JwtService(){
//        secretKey = generateSecretKey();
//    }
//
//    public String generateSecretKey() {
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGen.generateKey(); //Generates a new SecretKey object.
//            return Base64.getEncoder().encodeToString(secretKey.getEncoded()); //Encodes the raw bytes of the secret key into a Base64 string, which is a common way to represent binary data as a string.
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error generating secret key", e);
//        }
//    }
//    private final Environment environment;

//    public JwtService (Environment environment) {
//        this.environment = environment;
//    }

    // Generate Token
    public String generateToken(String username, String userId) {
        long expirationTimeInMillis = Long.parseLong(tokenExpirationTimeHour) * 60 * 60 * 1000;

        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", "admin");
//        claims.put("email", "user@example.com");
        claims.put("userId", userId);

        System.out.println("expiration_time is"+expirationTimeInMillis);

        return Jwts
                .builder() // initializes a new JwtBuilder instance. This instance is used to configure the JWT's various components, such as the header, payload, and signature.
                .claims(claims) // allows you to include any additional information you need
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(getKey())
                .compact(); // build and serialize the JWT into its final compact form, which is a string
    }

    // Generate signed key
    private SecretKey getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //Decodes the Base64-encoded key string back into raw bytes.
//        return Keys.hmacShaKeyFor(keyBytes); //create a Key for HMAC signing (like HMAC-SHA256).

        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        return secretKey;
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) { // claimResolver - A function to extract a specific claim from the Claims object.
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser() // Initializes a new JwtParser instance which is used to parse and validate JWT tokens.
                .verifyWith(getKey()) // Sets the verification key for the JwtParser. This key is used to verify the signature of the JWT to ensure it has not been tampered with.
                .build() //  Finalizes the JwtParser configuration with all the provided settings and returns a fully configured JwtParser instance.
                .parseSignedClaims(token) // Parses the provided JWT token. Verifies its signature using the configured key. Extracts and returns the claims if the signature is valid. If the signature is invalid, it typically throws an exception.
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserId(String token) {
        return extractClaim(token.substring(7), claims -> claims.get("userId", String.class));
    }

}
