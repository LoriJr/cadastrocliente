package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.model.User;
import com.viratech.cadastrocliente.model.dto.UserRequestDTO;
import com.viratech.cadastrocliente.model.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.exceptions.ApiResponseError;
import com.viratech.cadastrocliente.model.exceptions.CustomValidationException;
import com.viratech.cadastrocliente.model.exceptions.ResourceNotFoundException;
import com.viratech.cadastrocliente.model.mapper.AddressMapper;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public UserResponseDTO userSave(UserRequestDTO request, Locale locale){

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

        return userMapper.toResponseDTO(
                userRepository.save(userMapper.toEntity(request)));
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
}
