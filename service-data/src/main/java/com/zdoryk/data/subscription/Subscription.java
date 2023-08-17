package com.zdoryk.data.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private String email;

    @NotBlank(message = "name must not be blank")
    @NotNull(message = "name must not be blank")
    @Size(min = 2,max = 50,message = "name length should be between 2 and 40 characters")
    private String name;



}
