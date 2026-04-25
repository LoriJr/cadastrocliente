package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.mapper.UserCredentialMapper;
import com.viratech.cadastrocliente.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserCredentialMapper userCredentialMapper;
    private final PasswordEncoder passwordEncoder;

    public UserCredentialResponseDTO saveUserCredential(UserCredentialRequestDTO request){

        UserCredential userCredential = userCredentialMapper.toEntity(request);
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));

        // TODO adicionar o find para o email do usuário existente

        var save = userCredentialRepository.save(userCredential);
        return userCredentialMapper.toDTO(save);
    }
}
