package com.three.transfer.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadInfo {

    private long size;
    private String md5;
    private String chunk;
    private String chunks;
    private String fileName;
    private String ext;

    public UploadInfo(String md5, String chunk, String chunks, String fileName, String ext, long size) {
        this.md5 = md5;
        this.chunk = chunk;
        this.chunks = chunks;
        this.fileName = fileName;
        this.ext = ext;
        this.size = size;
    }
}
