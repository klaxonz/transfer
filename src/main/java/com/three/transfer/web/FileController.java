package com.three.transfer.web;


import com.three.transfer.dto.TFileExecution;
import com.three.transfer.entity.FileInfo;
import com.three.transfer.entity.TFile;
import com.three.transfer.entity.UploadInfo;
import com.three.transfer.entity.User;
import com.three.transfer.enums.TFileStateEnum;
import com.three.transfer.service.TFileService;
import com.three.transfer.service.UserService;
import com.three.transfer.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/transfer")
public class FileController {

    @Autowired
    private TFileService fileService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        User user = new User();
        user.setUserId(5);
        if (!multipartFile.isEmpty()) {
            try {
                TFileExecution fileExecution = fileService.addFile(multipartFile, user);
                if (fileExecution.getState() == TFileStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", fileExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "文件为空");
        }
        return modelMap;
    }


    @ResponseBody
    @RequestMapping(value = "/BigFileUp")
    public Map<String, Object> fileUpload(String guid, String md5value, String chunks, String chunk, String id, String name,
                                          String type, String lastModifiedDate, long size, MultipartFile file, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        User user = (User) request.getSession().getAttribute("user");
        try {
            int index;
            String ext = name.substring(name.lastIndexOf("."));
            UploadInfo info = new UploadInfo(md5value, chunk, chunks, name, ext, size);
            user = userService.getUserById(user.getUserId());
            TFileExecution fileExecution = fileService.addShardFile(info, file, user);
            if (fileExecution.getState() == TFileStateEnum.SUCCESS.getState()) {
                modelMap.put("code", 1);
                modelMap.put("msg", "上传成功");
            } else {
                modelMap.put("code", 0);
                modelMap.put("msg", "上传失败");
                modelMap.put("data", null);
            }
        } catch (Exception ex) {
            modelMap.put("code", 0);
            modelMap.put("msg", "上传失败");
            modelMap.put("data", null);
        }
        return modelMap;
    }


    @RequestMapping(value = "/getfilelist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getFileList(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Map<String, Object> modelMap = new HashMap<>();
        List<TFile> fileList = new ArrayList<>();
        List<FileInfo> fileInfoList = new ArrayList<>();


        try {
            fileList = fileService.getFileList(user);

            for (TFile file : fileList) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileId(file.getFileId());
                fileInfo.setFileName(file.getFileName());
                fileInfo.setFileSize(file.getFileSize());
                fileInfo.setUploadTime(file.getCreateTime());
                fileInfo.setValidTime(file.getFileValidTime());
                fileInfo.setFileCategoryId(file.getFileCategory().getFileCategoryId());
                fileInfo.setUserId(file.getUser().getUserId());
                fileInfoList.add(fileInfo);
            }

            modelMap.put("success", true);
            modelMap.put("msg", "获取文件列表成功");
            modelMap.put("count", fileInfoList.size());
            modelMap.put("files", fileInfoList);

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("msg", e.getMessage());
        }
        return modelMap;

    }

    @RequestMapping(value = "/deletefile", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object>deleteFile(int fileId ,HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        User user = (User) request.getSession().getAttribute("user");
        if (fileId != -1 && user != null) {
            user = userService.getUserById(user.getUserId());
            TFileExecution fileExecution = fileService.deleteFile(fileId, user);
            if (fileExecution.getState() != TFileStateEnum.SUCCESS.getState()) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "删除失败");
            } else {
                modelMap.put("success", true);
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "删除失败");
        }

        return modelMap;
    }

    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void downloadFile(int fileId, HttpServletRequest request, HttpServletResponse response) {

        if (fileId >= 0) {
            File file = fileService.getFile(fileId);
            if (file != null) {
                FileUtil.download(file, request, response);
            }
        }

    }



}
