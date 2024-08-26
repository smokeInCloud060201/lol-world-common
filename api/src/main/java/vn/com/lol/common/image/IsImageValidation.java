package vn.com.lol.common.image;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.com.lol.common.enums.FileExtension;

public class IsImageValidation implements ConstraintValidator <IsImage, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        String fileExtensionName = FilenameUtils.getExtension(file.getOriginalFilename());
        return FileExtension.isImageTypeExtension(fileExtensionName);
    }
}
