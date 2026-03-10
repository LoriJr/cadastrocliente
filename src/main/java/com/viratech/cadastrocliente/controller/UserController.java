package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.model.User;
import com.viratech.cadastrocliente.model.dto.UserRequestDTO;
import com.viratech.cadastrocliente.model.dto.UserResponseDTO;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid UserRequestDTO requestDTO){
        UserResponseDTO userResponseDTO = userService.userSave(requestDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponseDTO.id())
                .toUri();

        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDTO> findUser(@RequestParam String email){
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@RequestParam String email){
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
