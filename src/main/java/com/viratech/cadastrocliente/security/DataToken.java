package com.viratech.cadastrocliente.security;

public record DataToken(
        String accessToken,
        String refreshToken
) {
}
