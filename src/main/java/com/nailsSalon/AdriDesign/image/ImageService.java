package com.nailsSalon.AdriDesign.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image saveImage(String title, byte[] data, String contentType, String type) {
        Image image = new Image(title, data, contentType, type);
        return imageRepository.save(image);
    }

    public Optional<Image> getImageById(UUID id) {
        return imageRepository.findById(id);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public boolean deleteImageById(UUID id) {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
