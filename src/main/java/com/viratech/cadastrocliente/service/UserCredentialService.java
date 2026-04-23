package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.mapper.UserCredentialMapper;
import com.viratech.cadastrocliente.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserCredentialMapper userCredentialMapper;

    public UserCredential saveUserCredential(UserCredentialRequestDTO request){
        return userCredentialRepository.save(userCredentialMapper.toEntity(request));
    }
}
