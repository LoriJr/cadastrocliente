package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.model.User;
import com.viratech.cadastrocliente.model.dto.UserRequestDTO;
import com.viratech.cadastrocliente.model.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO userSave(UserRequestDTO request){
        return userMapper.toResponseDTO(
                userRepository.save(userMapper.toEntity(request)));
    }

    public List<UserResponseDTO> findAllUsers(){
        List<User> user = userRepository.findAll();
        return userMapper.toListUserResponseDTO(user);
    }

    public UserResponseDTO findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .map(userMapper::toResponseDTO)
                .orElseThrow(()-> new RuntimeException("User not found for this email: " + email));
    }

    @Transactional
    public void deleteUserByEmail(String email){
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email can not be empty");
        }
        if(!userRepository.existsByEmail(email)){
            throw new RuntimeException("User not found for this email: " + email);
        }
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public UserResponseDTO updateUser(UserRequestDTO dto, Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        userMapper.updateUser(dto, user);
        return userMapper.toResponseDTO(userRepository.save(user));
    }
}
