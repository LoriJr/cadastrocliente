package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserCredentialRequestDTO;
import com.viratech.cadastrocliente.dto.UserCredentialResponseDTO;
import com.viratech.cadastrocliente.dto.UserRoleRequest;
import com.viratech.cadastrocliente.dto.UserRoleResponse;
import com.viratech.cadastrocliente.model.entity.Role;
import com.viratech.cadastrocliente.model.entity.UserCredential;
import com.viratech.cadastrocliente.model.enums.RoleName;
import com.viratech.cadastrocliente.model.exceptions.ResourceNotFoundException;
import com.viratech.cadastrocliente.model.mapper.UserCredentialMapper;
import com.viratech.cadastrocliente.repository.RoleRepository;
import com.viratech.cadastrocliente.repository.UserCredentialRepository;
import com.viratech.cadastrocliente.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserCredentialService implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserRepository userRepository;
    private final UserCredentialMapper userCredentialMapper;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public UserCredentialResponseDTO saveUserCredential(UserCredentialRequestDTO request) throws MessagingException {

        if(request == null){
            throw new IllegalArgumentException("Credentials not be null");
        }

        // 1. Validar se o usuário existe pelo e-mail
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found for email: " + request.email()));

        // 2. Verificar se este usuário já possui uma credencial (evitar duplicidade)
        if(userCredentialRepository.existsByUserEmail(request.email())){
            throw new RuntimeException( "Credentials already registered for this user.");
        }

        // 3. Converter DTO para Entity
        // O Mapper já injeta o PasswordEncoder e faz o encode automaticamente!
        UserCredential userCredential = userCredentialMapper.toEntity(request);

        emailService.sendVerificationEmail(user);

        // 4. VINCULAR O USUÁRIO (Obrigatório por causa do @MapsId)
        userCredential.setUser(user);

        Role defaultRole = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(()->
                        new IllegalStateException("ROLE_USER not found."));
        userCredential.getRoles().add(defaultRole);

        // 5. Salvar
        var save = userCredentialRepository.save(userCredential);

        // 6. Retornar o DTO de resposta
        return userCredentialMapper.toDTO(save);
    }

    @Transactional
    public UserRoleResponse addRole(Long id, UserRoleRequest request){
        if( id == null){
            throw new IllegalArgumentException("Id has been not null");
        }
        if(request == null){
            throw new IllegalArgumentException("roleName has been not null");
        }

        UserCredential user = userCredentialRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("User not found."));

        Role role = roleRepository.findByRoleName(request.roleName())
                .orElseThrow(()->
                        new EntityNotFoundException("Role not found."));

        if(user.getRoles().contains(role)){
            throw new IllegalStateException("User already has this role.");
        }

        user.getRoles().add(role);

        return userCredentialMapper.toRoleResponse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialRepository.findByUserEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User Not found"));
    }
}
