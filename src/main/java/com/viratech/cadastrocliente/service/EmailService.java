package com.viratech.cadastrocliente.service;

import com.viratech.cadastrocliente.model.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.url}")
    private String URL_APP;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendVerificationEmail(User user) throws MessagingException {

        String token = user.getVerificationToken().getToken();

        String link = URL_APP + "/api/v1/auth/verify?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setFrom(sender);
        helper.setTo(user.getEmail());
        helper.setSubject("Confirmação de cadastro");

        String html = """
                <html>
                    <body>
                        <p>Olá <strong>%s</strong>,</p>
    
                        <p>Obrigado por realizar seu cadastro.</p>
    
                        <p>Para ativar sua conta clique no link abaixo:</p>
    
                        <h3>
                            <a href="%s" target="_blank">
                                VALIDAR
                            </a>
                        </h3>
    
                        <p>Este link expira em <strong>30 minutos</strong>.</p>
    
                        <p>Caso você não tenha solicitado este cadastro, ignore este e-mail.</p>
                    </body>
                </html>
                """.formatted(user.getName(), link);

        helper.setText(html, true);

        mailSender.send(message);
    }
}
