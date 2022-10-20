package com.example.filetest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDbRepository extends JpaRepository<FileDbEntity, Long> {
}
