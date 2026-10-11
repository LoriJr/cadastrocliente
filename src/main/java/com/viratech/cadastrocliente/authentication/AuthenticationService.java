package com.viratech.cadastrocliente.authentication;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.exceptions.InvalidLoginException;
import com.viratech.cadastrocliente.repository.UserCredentialRepository;
import com.viratech.cadastrocliente.security.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserCredentialRepository repository;
    private final AuthenticationManager authenticationManager;

    public AuthToken login(UserCredentialRequestDTO requestLogin){
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    requestLogin.email(), requestLogin.password());

            var authentication = authenticationManager.authenticate(authenticationToken);

            String accessToken = tokenService.createToken((UserCredential) authentication.getPrincipal());
            String refreshToken = tokenService.createRefreshToken((UserCredential) authentication.getPrincipal());

            return new AuthToken(accessToken, refreshToken);
        }
        catch (DisabledException ex) {
            throw new InvalidLoginException("Account pending email verification.");
        }
        catch (LockedException ex) {
            throw new InvalidLoginException("User account is blocked.");
        }
        catch (AuthenticationException ex) {
            throw new InvalidLoginException("email or password invalid");
        }
    }

    public AuthToken refreshToken(String refreshToken) {

        String subject =
                tokenService.verifyRefreshToken(refreshToken);

        Long id;

        try {
            id = Long.valueOf(subject);
        } catch (NumberFormatException ex) {
            throw new InvalidLoginException(
                    "Invalid refresh token."
            );
        }

        UserCredential credential = repository.findById(id)
                .orElseThrow(() ->
                        new InvalidLoginException(
                                "Invalid refresh token."
                        )
                );

        if (!credential.isEnabled()) {
            throw new InvalidLoginException(
                    "Account disabled."
            );
        }

        if (!credential.isAccountNonLocked()) {
            throw new InvalidLoginException(
                    "Account locked."
            );
        }

        String accessToken =
                tokenService.createToken(credential);

        String newRefreshToken =
                tokenService.createRefreshToken(credential);

        return new AuthToken(accessToken, newRefreshToken);
    }
}
