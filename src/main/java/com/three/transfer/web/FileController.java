package com.three.transfer.web;


import com.three.transfer.bo.UploadFileBo;
import com.three.transfer.common.Const;
import com.three.transfer.common.ResponseCode;
import com.three.transfer.common.ServerResponse;
import com.three.transfer.dao.UserMapper;
import com.three.transfer.entity.TFile;
import com.three.transfer.entity.User;
import com.three.transfer.service.TFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private TFileService fileService;

    @Autowired
    private UserMapper userMapper;


    @RequestMapping(value = "/upload")
    @ResponseBody
    public ServerResponse<String> uploadFile(UploadFileBo uploadFileBo, MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        String fileName = uploadFileBo.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        uploadFileBo.setExt(ext);
        ServerResponse<String> userServerResponse = null;
        //保存文件
        try {
            //判断是否分片上传，执行不同的处理逻辑
            if (uploadFileBo.getChunk() == null && uploadFileBo.getChunks() == null) {
                userServerResponse = fileService.uploadFile(uploadFileBo, file, user);
            } else {
                userServerResponse = fileService.uploadShardFile(uploadFileBo, file, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("文件上传失败");
        }
        if (userServerResponse.getStatus() == ResponseCode.FILE_UPLOAD_SUCCESS.getCode()) {
            //更新用户session
            user = userMapper.selectByPrimaryKey(user.getUserId());
            if (user == null) {
                return ServerResponse.createByErrorMessage("上传文件失败");
            }
            session.setAttribute(Const.CURRENT_USER, user);
        }
        return userServerResponse;
    }


    @RequestMapping(value = "/getfilelist", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<TFile>> getFileList(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return fileService.getFileList(user);
    }

    @RequestMapping(value = "/deletefile", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> deleteFile(Integer fileId ,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        if (fileId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGEL_ARGUMENT.getCode(), "请求参数错误");
        }
        //删除文件业务逻辑
        ServerResponse<String> serverResponse = fileService.deleteFile(fileId, user);
        //更新session用户信息
        if (serverResponse.isSuccess()) {
            user = userMapper.selectByPrimaryKey(user.getUserId());
            if (user == null) {
                return ServerResponse.createByErrorMessage("删除文件失败");
            }
            session.setAttribute(Const.CURRENT_USER, user);
        }
        return ServerResponse.createBySuccessMessage("删除文件成功");
    }

    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public ServerResponse<String> downloadFile(Integer fileId, HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        try {
            ServerResponse<String> serverResponse = fileService.downloadFile(fileId, request, response);
            if (!serverResponse.isSuccess()) {
                return serverResponse;
            }
        } catch (IOException e) {
            return ServerResponse.createByErrorMessage("下载文件失败");
        }
        return ServerResponse.createBySuccessMessage("下载文件成功");
    }

}
