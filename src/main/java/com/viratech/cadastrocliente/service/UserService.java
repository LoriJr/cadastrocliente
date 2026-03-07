package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;



}
