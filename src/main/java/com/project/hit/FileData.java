package com.project.hit;

import lombok.Getter;

@Getter
public class FileData {
    private final String encodeName;
    private final String decodeName;

    public FileData(String encodeName, String decodeName) {
        this.encodeName = encodeName;
        this.decodeName = decodeName;
    }
}
