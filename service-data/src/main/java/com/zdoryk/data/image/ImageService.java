package com.zdoryk.data.image;

import com.zdoryk.data.exception.NotFoundException;
import com.zdoryk.data.utils.Base64Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
