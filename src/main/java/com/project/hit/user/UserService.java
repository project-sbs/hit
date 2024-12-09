package com.project.hit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JavaMailSender mailSender;
    private final AuthcodeRepository authcodeRepository;

    public ResponseEntity<String> sendMail(String email) {
        String randomCode = String.valueOf(new Random().nextInt(899999) + 100000);
        Authcode authcode = new Authcode();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("비밀번호 찾기 인증번호");
            message.setText("인증번호는 다음과 같습니다: " + randomCode);
            mailSender.send(message);
            System.out.println("randomCode: " + randomCode);
            authcode.setEmail(email);
            authcode.setCode(randomCode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
        this.authcodeRepository.save(authcode);
        return ResponseEntity.ok().body("인증번호를 전송했습니다.");
    }

    public boolean checkCode(String email, String code) {
        Authcode authcode = this.authcodeRepository.findByEmail(email);
        String _code = authcode.getCode();
        return code.equals(_code);
    }

    @Transactional
    public void deleteAuthcode(String email) {
        Authcode authcode = this.authcodeRepository.findByEmail(email);
        this.authcodeRepository.delete(authcode);
    }
}
