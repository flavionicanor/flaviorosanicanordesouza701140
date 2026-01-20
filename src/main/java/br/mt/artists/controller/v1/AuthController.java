package br.mt.artists.controller.v1;

import br.mt.artists.domain.dto.AuthResponseDTO;
import br.mt.artists.domain.dto.LoginRequestDTO;
import br.mt.artists.service.JwtService;
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
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {

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
}
