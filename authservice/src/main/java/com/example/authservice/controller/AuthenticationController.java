package com.example.authservice.controller;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Value("${spring.security.user.name}")
    private String myUsername;

    @Value("${spring.security.user.password}")
    private String myPassword;

    @GetMapping("/validate")
    public ResponseEntity<String> validateUser(@RequestHeader("Authorization") String authHeader) {

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String encodedCredentials = authHeader.substring(6);  // "Basic " prefix is 6 chars long
            // Decode base64 and extract username/password
            String decodedCredentials = new String(java.util.Base64.getDecoder().decode(encodedCredentials));
            String[] credentials = decodedCredentials.split(":");

            String username = credentials[0];
            String password = credentials[1];
            if (myUsername.equals(username) && myPassword.equals(password)) {
                return ResponseEntity.ok("User validated successfully!");
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");


    }
}
