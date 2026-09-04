package com.eventostec.api.adapters.outbound.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploaderPort {
    String uploadImage(MultipartFile imageBytes);
}
