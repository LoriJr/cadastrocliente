package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.mapper.UserCredentialMapper;
import com.viratech.cadastrocliente.repository.UserCredentialRepository;
import com.viratech.cadastrocliente.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCredentialService implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserRepository userRepository;
    private final UserCredentialMapper userCredentialMapper;
    private final PasswordEncoder passwordEncoder;

    public UserCredentialResponseDTO saveUserCredential(UserCredentialRequestDTO request){

        if(request == null){
            throw new IllegalArgumentException("Credentials not be null");
        }

        // 1. Validar se o usuário existe pelo e-mail
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("e-mail not found"));

        // 2. Verificar se este usuário já possui uma credencial (evitar duplicidade)
        if(userCredentialRepository.existsByUserEmail(request.email())){
            throw new RuntimeException("This username already");
        }

        // 3. Converter DTO para Entity
        // O Mapper já injeta o PasswordEncoder e faz o encode automaticamente!
        UserCredential userCredential = userCredentialMapper.toEntity(request);

        // 4. VINCULAR O USUÁRIO (Obrigatório por causa do @MapsId)
        userCredential.setUser(user);

        // 5. Salvar
        var save = userCredentialRepository.save(userCredential);

        // 6. Retornar o DTO de resposta
        return userCredentialMapper.toDTO(save);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialRepository.findByUserEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User Not found"));
    }
}
