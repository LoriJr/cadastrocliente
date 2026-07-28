package com.viratech.cadastrocliente.repository;

import com.viratech.cadastrocliente.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserExportReportRepository {

    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Slice<User> findPageIsolated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
