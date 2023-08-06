package com.zdoryk.util;

import java.io.Serializable;

public record EmailVerificationToken(
        String to,
        String token

) implements Serializable {
}
