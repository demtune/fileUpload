package com.example.filetest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;


@Service
public class FileDbService {

    private final FileDbRepository fileDbRepository;

    @Autowired
    public FileDbService(FileDbRepository fileDbRepository) {
        this.fileDbRepository = fileDbRepository;
    }

    public FileDbEntity saveFileToDb(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Имя файла содержит недопустимый путь " + fileName);
            }

            FileDbEntity fileDb = new FileDbEntity(
                    fileName,
                    file.getContentType(),
                    file.getBytes());
            return fileDbRepository.save(fileDb);

        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл " + fileName);
        }
    }

    public FileDbEntity getFileById(Long id) throws FileNotFoundException {
        return fileDbRepository
                .findById(id)
                .orElseThrow(() -> new FileNotFoundException("Файл не найден с идентификатором:" + id));
    }

    public String getDownloadUrl(FileDbEntity fileDb) {
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/file/download/")
                .path(fileDb.getId().toString())
                .toUriString();
        return downloadUrl;
    }
}