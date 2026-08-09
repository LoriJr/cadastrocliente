package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.*;
import com.viratech.cadastrocliente.model.entity.Role;
import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.entity.UserVerificationToken;
import com.viratech.cadastrocliente.model.enums.UserStatus;
import com.viratech.cadastrocliente.model.exceptions.ApiResponseError;
import com.viratech.cadastrocliente.model.exceptions.CustomValidationException;
import com.viratech.cadastrocliente.model.exceptions.ResourceNotFoundException;
import com.viratech.cadastrocliente.model.mapper.AddressMapper;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import com.viratech.cadastrocliente.specification.UserSpecification;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final MessageSource messageSource;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public UserResponseDTO userSave(UserRequestDTO request, Locale locale) throws MessagingException {

        String className = UserService.class.getSimpleName();

        if(request == null){
            throw new IllegalArgumentException("Request body must not be null");
        }

        List<User> conflicts = userRepository.findConflicts(
                request.email(),
                request.cpf(),
                request.rg()
        );

        List<ApiResponseError.ObjectError> errors = new ArrayList<>();

        for (User user : conflicts) {

            if (user.getEmail().equals(request.email())) {
                errors.add(new ApiResponseError.ObjectError("email",
                        getMessage("error.email.violation", locale)));
            }

            if (user.getCpf().equals(request.cpf())) {
                errors.add(new ApiResponseError.ObjectError("cpf",
                        getMessage("error.cpf.violation", locale)));
            }

            if (user.getRg().equals(request.rg())) {
                errors.add(new ApiResponseError.ObjectError("rg",
                        getMessage("error.rg.violation", locale)));
            }
        }

        if(!errors.isEmpty()){
            throw new CustomValidationException(errors);
        }

        User user = userMapper.toEntity(request);

        user.setUserStatus(UserStatus.PENDING_VERIFICATION);

        UserVerificationToken verificationToken = new UserVerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpiration(LocalDateTime.now().plusMinutes(30));

        verificationToken.setUser(user);
        user.setVerificationToken(verificationToken);

        log.info("[{}] [UserSave] Recebido dados do usuário {}", className, user);

        userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }

    private String getMessage(String key, Locale locale){
        return messageSource.getMessage(key, null, locale);
    }

    public List<UserResponseDTO> findAllUsers(){
        List<User> user = userRepository.findAll();
        return userMapper.toListUserResponseDTO(user);
    }

    public UserResponseDTO findUserByEmail(String email){

        log.info("[findUserByEmail] " + email);

        return userRepository.findByEmail(email)
                .map(userMapper::toResponseDTO)
                .orElseThrow(()-> new ResourceNotFoundException(email));
    }

    @Transactional
    public void deleteUserByEmail(String email){
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email can not be empty");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found for this email: " + email));

        log.info("[deleteUserByEmail] id: {} nome: {} email: {}", user.getId(),user.getName(), user.getEmail());
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public UserResponseDTO updateUser(UserRequestDTO dto, Long id){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));

        userMapper.updateUser(dto, user);

        if(dto.address() != null && user.getAddress() != null){
            addressMapper.updateAddress(dto.address(), user.getAddress());
        }

        log.info("[updateUser] {}", dto.address());

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public Page<UserResponseDTO> getAllUsersPage(UserFilterRequest filter, Pageable pageable){

        Page<User> users = userRepository.findAll(UserSpecification.withFilter(filter), pageable);
        return users.map(userMapper::toResponseDTO);
    }

    public Page<UserRoleProjection> getUserRoleProjection(Pageable pageable){
        return userRepository.findUsersWithRoles(pageable);
    }

    public Page<UserRoleResponseDTO> getUsersPage(Pageable pageable){
        return userRepository.findAll(pageable)
                .map(user -> new UserRoleResponseDTO(
                        user.getName(),
                        user.getEmail(),
                        user.getCreatedAt(),
                        user.getCredential() != null ? user.getCredential()
                                .getRoles()
                                .stream()
                                .map(Role::getRoleName)
                                .collect(Collectors.toSet())
                                : Set.of()
                ));
    }
}
