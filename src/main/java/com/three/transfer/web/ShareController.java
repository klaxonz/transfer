package com.three.transfer.web;

import com.three.transfer.dto.TFileLinkExecution;
import com.three.transfer.entity.TFile;
import com.three.transfer.entity.TFileLink;
import com.three.transfer.enums.TFileLinkStateEnum;
import com.three.transfer.service.TFileLinkService;
import com.three.transfer.service.TFileService;
import com.three.transfer.util.ShareLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/transfer")
@Controller
public class ShareController {
    @Autowired
    private TFileLinkService linkService;
    @Autowired
    private TFileService fileService;

    @RequestMapping(value = "/createsharelink", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createShareLink(int fileId, String linkPassword, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        if (fileId <= -1) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "文件不存在");
        }
        try {
            TFileLinkExecution fileLinkExecution = linkService.addFileLink(fileId, linkPassword);
            if (fileLinkExecution.getState() != TFileLinkStateEnum.SUCCESS.getState()) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "文件不存在");
            }
            String fileLinkAddr = fileLinkExecution.getFileLink().getFileLinkAddr();
            String fileLinkPassword = fileLinkExecution.getFileLink().getFileLinkPassword();
            modelMap.put("success", true);
            modelMap.put("shareLink", ShareLinkUtil.generateShareLink(fileLinkAddr));
            modelMap.put("shareLinkPassword", fileLinkPassword);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "创建分享链接失败");
        }
        return modelMap;
    }


    @RequestMapping(value = "/share/{linkUrl}", method = RequestMethod.GET)
    @ResponseBody
    public void downloadShareFile(@PathVariable String linkUrl, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (linkUrl != null && !"".equals(linkUrl)) {
            //查询该链接的相关信息
            TFileLink fileLink = linkService.getFileLinkByLinkAddr(linkUrl);
            if (fileLink == null) {
                //TODO资源不存在
            }
            TFile file = fileService.getFileByFileId(fileLink.getFileLinkId());
            request.setAttribute("fileName", file.getFileName());
            request.setAttribute("fileSize", file.getFileSize());
            //计算文件有效时间
            long createTime = file.getCreateTime().getTime();

            //request.setAttribute("fileValidTime",);
        }
    }





}
