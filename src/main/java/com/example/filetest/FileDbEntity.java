package com.example.filetest;

import javax.persistence.*;

@Entity
public class FileDbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String fileName;

    @Column(name = "type")
    private String fileType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    public FileDbEntity(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public FileDbEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public byte[] getData() {
        return data;
    }
}