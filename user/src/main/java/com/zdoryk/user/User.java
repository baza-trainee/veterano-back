package com.zdoryk.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @NotBlank
    @Size(min = 2, max = 40, message = "First name length should be between 2 and 40 characters")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 40, message = "Second name length should be between 2 and 40 characters")
    private String secondName;

    @Email
    private String email;

    @Pattern(regexp = "^\\+?380\\d{9}$", message = "Invalid phone number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole userRole;

    @NotBlank
    @Size(min = 1, message = "Password should not be empty")
    private String password;

    private Boolean enabled;
}
