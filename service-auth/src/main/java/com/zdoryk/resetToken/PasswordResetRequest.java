package com.zdoryk.resetToken;

import com.netflix.discovery.EurekaNamespace;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record PasswordResetRequest(
        @Email String email
) {
}
