package com.viratech.cadastrocliente.controller;

import com.viratech.cadastrocliente.service.UserExportReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserExportReportController {

    private final UserExportReportService userExport;

    @GetMapping(value = "/export", produces = "text/csv; charset=ISO-8859-1")
    public ResponseEntity<StreamingResponseBody> export(){

        StreamingResponseBody responseBody = userExport::exportCsv;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=extracaoUsuarios.csv")
                .body(responseBody);
    }
}
