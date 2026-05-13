package com.viratech.cadastrocliente.security;

public record AuthToken(
        String accessToken,
        String refreshToken
) {
}
