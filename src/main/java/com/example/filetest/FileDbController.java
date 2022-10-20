package com.example.filetest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileDbController {

    private final FileDbService fileDbService;

    @Autowired
    public FileDbController(FileDbService fileDbService) {
        this.fileDbService = fileDbService;
    }

    @PostMapping("/upload")
    public ResponseDataFile uploadFile(@RequestParam MultipartFile file) {
        FileDbEntity fileDb = null;
        String downloadUrl = "";

        try {
            fileDb = fileDbService.saveFileToDb(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        downloadUrl = fileDbService.getDownloadUrl(fileDb);

        return new ResponseDataFile(
                file.getName(),
                downloadUrl,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        FileDbEntity fileDb = null;
        try {
            fileDb = fileDbService.getFileById(fileId);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileDb.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "fileDb; filename=\"" + fileDb.getFileName() + "\"")
                .body(new ByteArrayResource(fileDb.getData()));
    }
}
