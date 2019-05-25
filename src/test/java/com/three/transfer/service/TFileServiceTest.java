package com.three.transfer.service;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TFileServiceTest {

    @Autowired
    private TFileService fileService;
    @Autowired
    private UserService userService;


}
