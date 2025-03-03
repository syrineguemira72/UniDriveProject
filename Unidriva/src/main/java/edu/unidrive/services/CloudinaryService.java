    package edu.unidrive.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class CloudinaryService {
        private Cloudinary cloudinary;

        public CloudinaryService() {
            this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dy0nurg4h",
                    "api_key", "243289355461118",
                    "api_secret", "dXQrVYXuqjTvvGXXLL3NJM2Ed_s"
            ));
        }

        public String uploadImage(File file) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                return uploadResult.get("secure_url").toString();  // Returns the image URL
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


