package com.viratech.cadastrocliente.security;

import com.viratech.cadastrocliente.authentication.TokenService;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.repository.UserCredentialRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@AllArgsConstructor
@Component
public class FilterAccessToken extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(FilterAccessToken.class);

    private final TokenService tokenService;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recoveryToken(request);

        logger.info("token: " + token);

        if(token != null){
            String email = tokenService.verifyToken(token);
            UserCredential userCredential = userCredentialRepository.findByUserEmail(email).orElseThrow();

            Authentication authentication = new UsernamePasswordAuthenticationToken(userCredential, null, userCredential.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {

            String requestHeader = request.getHeader("Authorization");
            if(requestHeader != null){
                return requestHeader.replace("Bearer ", "");
            }
            return null;
        }
}
