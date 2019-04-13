package com.three.transfer.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;


@Getter
@Setter
public class FileHolder {

    private String fileName;
    private InputStream inputStream;

}
