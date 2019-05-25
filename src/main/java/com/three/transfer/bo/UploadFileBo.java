package com.three.transfer.bo;

public class UploadFileBo {

    private String id;
    private Long size;
    private String guid;
    private String md5;
    private Integer chunk;
    private Integer chunks;
    private String name;
    private String ext;

    public UploadFileBo(String id, Long size, String guid, String md5, Integer chunk, Integer chunks,
                        String name, String ext) {
        this.id = id;
        this.size = size;
        this.guid = guid;
        this.md5 = md5;
        this.chunk = chunk;
        this.chunks = chunks;
        this.name = name;
        this.ext = ext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getChunk() {
        return chunk;
    }

    public void setChunk(Integer chunk) {
        this.chunk = chunk;
    }

    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
