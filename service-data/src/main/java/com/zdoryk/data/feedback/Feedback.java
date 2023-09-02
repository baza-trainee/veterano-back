package com.zdoryk.data.feedback;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity(name = "Feedback")
public class Feedback {

    @Id
    @SequenceGenerator(
            name = "feedback_id_sequence",
            sequenceName = "feedback_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "feedback_id_sequence"
    )
    public Long feedbackId;

    @NotBlank(message = "name should have length between 2 and 30 symbols")
    @Size(min = 2,max = 30,message = "name should have length between 2 and 30 symbols")
    private String name;

    @Email
    @Column(length = 300)
    @Size(min = 1,max = 300,message = "email should be between 1 and 300 symbols")
    private String email;

    @Column(length = 300)
    private String message;

}
