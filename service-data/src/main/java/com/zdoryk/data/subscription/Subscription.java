package com.zdoryk.data.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Subscription implements Serializable {


    @Id
    @SequenceGenerator(
            name = "subscription_id_sequence",
            sequenceName = "subscription_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subscription_id_sequence"
    )
    @Schema(hidden = true)
    private Long subscriptionId;

    @Email
    @NotBlank(message = "email must not be blank")
    @NotNull(message = "email must not be blank")
    @Size(min = 1,max = 300,message = "email should be between 1 and 300 symbols")
    @Column(length = 300)
    private String email;

    @NotBlank(message = "name must not be blank")
    @NotNull(message = "name must not be blank")
    @Size(min = 2,max = 50,message = "name length should be between 2 and 40 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯіІїЇєЄ\\s]+$", message = "should contain only letters")
    private String name;





}
