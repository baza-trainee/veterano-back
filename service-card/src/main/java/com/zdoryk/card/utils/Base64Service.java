package com.zdoryk.card.utils;

import com.zdoryk.card.exception.ImageFailedDecodeException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class Base64Service {

    public byte[] decodeBase64ToImage(String base64Content){
        try {
            base64Content = base64Content.replaceFirst("^data:image/[^;]+;base64,", "");
            return Base64.decodeBase64(base64Content);
        } catch (IllegalArgumentException e) {
            throw new ImageFailedDecodeException("DIDNT CONVERT");
        }
    }

    public boolean isBase64(String image){
        return Base64.isBase64(image);
    }

}
