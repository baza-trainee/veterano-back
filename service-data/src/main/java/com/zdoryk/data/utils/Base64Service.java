package com.zdoryk.data.utils;

import com.zdoryk.data.exception.ImageFailedDecodeException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class Base64Service {

    public static byte[] decodeBase64ToImage(String base64Content){
        try {
            base64Content = base64Content.replaceFirst("^data:image/[^;]+;base64,", "");
            return Base64.decodeBase64(base64Content);
        } catch (IllegalArgumentException e) {
            throw new ImageFailedDecodeException("DIDNT CONVERT");
        }
    }
}
