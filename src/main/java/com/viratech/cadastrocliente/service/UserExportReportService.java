package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.dto.UserExportDTO;
import com.viratech.cadastrocliente.model.entity.User;
import com.viratech.cadastrocliente.model.mapper.UserMapper;
import com.viratech.cadastrocliente.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@Service
public class UserExportReportService {

    private final UserExportRepository exportRepository;
    private final UserMapper userMapper;
    private final TransactionTemplate transactionTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    public void exportCsv(OutputStream outputStream){

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.ISO_8859_1))){

            final int[] pageNumber = {0};
//            Page<User> page;
            boolean nextPage;

            writer.write("name;email;phone;cpf;rg;birth_date;created_at;address_line1;number;address_line2;neighborhood;zip_code;city;state");
            writer.newLine();
            writer.flush();

            do {
                nextPage = Boolean.TRUE.equals(transactionTemplate.execute(status -> {

                    Pageable pageable = PageRequest.of(pageNumber[0], 1000);
                    Slice<User> page = exportRepository.findPageIsolated(pageable);
                    Slice<UserExportDTO> dtoPage = page.map(userMapper::toExportDTO);

                    try {

                        for (UserExportDTO response : dtoPage) {
                            writer.write(
                                    String.join(";",
                                            response.name(),
                                            response.email(),
                                            response.phone(),
                                            response.cpf(),
                                            response.rg(),
                                            response.birthDate().toString(),
                                            response.createdAt().toString(),
                                            response.addressLine1(),
                                            response.number(),
                                            value(response.addressLine2()),
                                            response.neighborhood(),
                                            response.zipCode(),
                                            response.city(),
                                            response.state()
                                    )
                            );

                            writer.newLine();
                        }

                        writer.flush();

                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao escrever no arquivo CSV", e);
                    }

                    entityManager.clear();

                    return page.hasNext();
                }));

                pageNumber[0]++;

            } while (nextPage);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar relatório de usuários", e);
        }
    }

    private String value(String value){
        return value == null ? "":value;
    }

}
