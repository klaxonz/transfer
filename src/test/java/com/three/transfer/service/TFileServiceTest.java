package com.three.transfer.service;


import com.three.transfer.dto.TFileExecution;
import com.three.transfer.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TFileServiceTest {

    @Autowired
    private TFileService fileService;
    @Autowired
    private UserService userService;

    @Test
    public void testAddFile() throws IOException {
        File file = new File("F:\\Ebook\\计算机程序设计\\前端\\JavaScript\\JavaScript语言精粹.pdf");
        InputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), fileInputStream);
        User user = new User();
        user.setUserId(21);
        TFileExecution fileExecution = fileService.addFile(multipartFile, user);
        System.out.println(fileExecution.getStateInfo());
    }

    @Test
    public void testDeleteFile() {
        int fileId = 87;
        User user = userService.getUserById(19);
        TFileExecution tFileExecution = fileService.deleteFile(fileId, user);
        System.out.println(tFileExecution.getStateInfo());
    }





}
