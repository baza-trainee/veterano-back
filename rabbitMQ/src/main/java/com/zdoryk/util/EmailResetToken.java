package com.zdoryk.util;

import java.io.Serializable;

public record EmailResetToken(
        String to,
        String link

) implements Serializable {
}
