package com.three.transfer.web;

import com.three.transfer.common.Const;
import com.three.transfer.common.ResponseCode;
import com.three.transfer.common.ServerResponse;
import com.three.transfer.entity.TFileLink;
import com.three.transfer.entity.User;
import com.three.transfer.service.TFileLinkService;
import com.three.transfer.service.TFileService;
import com.three.transfer.vo.ShareFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequestMapping("/share")
@Controller
public class ShareController {
    @Autowired
    private TFileLinkService fileLinkService;
    @Autowired
    private TFileService fileService;

    @RequestMapping(value = "/createsharelink", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<TFileLink> createShareLink(Integer fileId, String linkPassword, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getMsg());
        }
        return fileLinkService.createFileShareLink(fileId, linkPassword);
    }


    @RequestMapping(value = "/{linkUrl}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ShareFileVo> share(@PathVariable String linkUrl) {
        return fileLinkService.share(linkUrl);
    }

    @RequestMapping(value = "/comfirmlinkpass", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<ShareFileVo> share(String linkAddr, String shareCode) {
        if (linkAddr == null || shareCode == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGEL_ARGUMENT.getCode(), ResponseCode.ILLEAGEL_ARGUMENT.getMsg());
        }
        return fileLinkService.confirmLinkPassword(linkAddr, shareCode);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ServerResponse<String> download(Integer fileId, HttpServletRequest request, HttpServletResponse response) {
        try {
            ServerResponse<String> serverResponse = fileService.downloadFile(fileId, request, response);
            if (!serverResponse.isSuccess()) {
                return serverResponse;
            }
        } catch (IOException e) {
            return ServerResponse.createByErrorMessage("文件下载失败");
        }
        return ServerResponse.createBySuccessMessage("下载文件成功");
    }


}
