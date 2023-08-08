package com.zdoryk.card.image;

import com.zdoryk.card.dto.ImageDTO;
import com.zdoryk.card.exception.ImageNotBase64Exception;
import com.zdoryk.card.exception.NotFoundException;
import com.zdoryk.card.utils.Base64Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final Base64Service base64Service;


    public void saveImage(Image image){
        image.setImage(image.getImage());
        imageRepository.save(image);
    }

    public byte[] getImageById(Long id) {
        Image image = imageRepository.getImageByImageId(id)
                .orElseThrow(() -> new NotFoundException("Image with this ID doesn't exist"));

        return base64Service.decodeBase64ToImage(image.getImage());
    }
}
