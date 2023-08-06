package com.zdoryk.token;
import com.zdoryk.RabbitMQRunner;
import com.zdoryk.util.EmailVerificationToken;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConfirmationService {

    private final TokenRepository tokenRepository;
    private final RabbitMQRunner rabbitMQRunner;

    @Value("${config.urls.approve-register}")
    private String approveLink;


    public void saveConfirmationToken(ConfirmationToken token) {
        tokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return tokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }


    public void sendEmailConfirmation(String email, String token) {
        rabbitMQRunner.sendEmailVerification(new EmailVerificationToken(
                email,
                approveLink + token
        ));
    }
}
