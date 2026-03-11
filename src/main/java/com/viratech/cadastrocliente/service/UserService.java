package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.model.User;
import com.viratech.cadastrocliente.model.dto.UserRequestDTO;
import com.viratech.cadastrocliente.model.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO userSave(UserRequestDTO request){
        return userMapper.toResponseDTO(
                userRepository.save(userMapper.toEntity(request)));
    }

    public List<UserResponseDTO> findAllUsers(){
        return userMapper.toListUserResponseDTO(userRepository.findAll());
    }

    public UserResponseDTO findUserByEmail(String email){
        return userMapper.toResponseDTO(userRepository.findByEmail(email));
    }

    public void deleteUserByEmail(String email){
        userRepository.deleteByEmail(email);
    }

    public UserResponseDTO updateUser(UserRequestDTO dto, Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuário não encontrado"));
        userMapper.updateUser(dto, user);
        return userMapper.toResponseDTO(userRepository.save(user));
    }
}
