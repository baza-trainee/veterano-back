package com.zdoryk.resetToken;

import com.zdoryk.RabbitMQRunner;
import com.zdoryk.auth.User;
import com.zdoryk.auth.AuthenticationService;
import com.zdoryk.auth.UserRepository;
import com.zdoryk.exceptions.IncorrectDataException;
import com.zdoryk.exceptions.NotFoundException;
import com.zdoryk.util.EmailResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetService {
    
    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RabbitMQRunner rabbitMQRunner;
    private final AuthenticationService userService;


    @Value("${config.urls.reset-password}")
    private String resetLink;
    
    public void generatePasswordResetToken(String email) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("USER DOES NOT EXISTS"));

        PasswordResetToken token = new PasswordResetToken().builder()
                        .token(UUID.randomUUID().toString())
                        .user(user)
                        .expiryDate(LocalDateTime.now().plusMinutes(60))
                        .build();

         tokenRepository.save(token);
         rabbitMQRunner.sendEmailResetPasswordToken(new EmailResetToken(
                 user.getEmail(),
                 resetLink+token.getToken()+"&password="
         ));
    }
    
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("TOKEN DOES NOT EXIST"));
        if(passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new IncorrectDataException("TOKEN IS DEPRECATED");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(newPassword);
        userService.updateUser(user);
        tokenRepository.delete(passwordResetToken);
    }
}
