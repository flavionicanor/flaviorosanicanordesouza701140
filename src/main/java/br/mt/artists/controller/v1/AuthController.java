package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.request.LoginRequestDTO;
import br.mt.artists.domain.dto.response.AuthResponseDTO;
import br.mt.artists.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@RequestHeader("Authorization") String authorization) {

        String refreshToken = authorization.replace("Bearer", "").trim();

        String username = jwtService.extractUsername(refreshToken);

        String newAccessToken = jwtService.generateTokenFromUsername(username);

        return new AuthResponseDTO(newAccessToken);
    }

}
