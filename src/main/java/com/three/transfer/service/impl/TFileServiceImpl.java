package com.three.transfer.service.impl;

import com.three.transfer.dao.TFileDao;
import com.three.transfer.dao.UserDao;
import com.three.transfer.dto.FileHolder;
import com.three.transfer.dto.TFileExecution;
import com.three.transfer.dto.TFileLinkExecution;
import com.three.transfer.entity.TFile;
import com.three.transfer.entity.TFileCategory;
import com.three.transfer.entity.UploadInfo;
import com.three.transfer.entity.User;
import com.three.transfer.enums.TFileLinkStateEnum;
import com.three.transfer.enums.TFileStateEnum;
import com.three.transfer.exceptions.TFileException;
import com.three.transfer.service.TFileLinkService;
import com.three.transfer.service.TFileService;
import com.three.transfer.util.FileUtil;
import com.three.transfer.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TFileServiceImpl implements TFileService {

    @Autowired
    private TFileDao fileDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TFileLinkService fileLinkService;

    private long validTimeMills = 604800000;

    @Override
    @Transactional
    public TFileExecution addFile(MultipartFile file, User user) {
        FileHolder fileHolder = new FileHolder();
        TFile fileInfo = new TFile();
        String fileRelativePath = PathUtil.getFilePath(user.getUserId());
        //获取输入流
        if (file != null) {
            //保存文件至本地
            try {

                fileHolder.setFileName(file.getOriginalFilename());
                fileHolder.setInputStream(file.getInputStream());
                String dest = FileUtil.getFileStoreRelativeAddr(fileHolder,
                        fileRelativePath);
                file.transferTo(new File(PathUtil.getFileBasePath() + dest));

            } catch (IOException e) {
                throw new TFileException("add file error: " + "保存文件失败");
            }

            try {
                //将文件信息保存至数据库

                TFileCategory fileCategory = new TFileCategory();
                fileCategory.setFileCategoryId(2);
                fileInfo.setUser(user);
                fileInfo.setFileName(fileHolder.getFileName());
                fileInfo.setFilePath(fileRelativePath);
                fileInfo.setCreateTime(new Date());
                fileInfo.setFileSize(file.getSize());
                fileInfo.setLastEditTime(new Date());
                Date validTime = new Date(System.currentTimeMillis() + validTimeMills);
                fileInfo.setFileValidTime(validTime);
                fileInfo.setFileCategory(fileCategory);
                int res = fileDao.insertFile(fileInfo);

                if (res <= 0) {
                    throw new TFileException("存储文件失败");
                }

            } catch (Exception e) {
                throw new TFileException("add file error:" + "保存文件信息失败");
            }

            //更新用户信息
            try {
                User userInfo = userDao.getUserById(user.getUserId());
                userInfo.setUsedCapacity(userInfo.getUsedCapacity() + file.getSize());
                int res = userDao.updateUser(userInfo);
                if (res <= 0) {
                    throw new RuntimeException("更新用户信息失败");
                }

            } catch (Exception e) {
                return new TFileExecution(TFileStateEnum.INNER_ERROR);
            }



        } else {
            return new TFileExecution(TFileStateEnum.INNER_ERROR);
        }

        return new TFileExecution(TFileStateEnum.SUCCESS, fileInfo);
    }

    @Override
    @Transactional
    public TFileExecution addShardFile(UploadInfo info, MultipartFile file, User user) {

        if (file != null) {
            //保存文件
            try {
                if (info != null) {
                    int index;
                    String fileName;
                    String ext = info.getFileName().substring(info.getFileName().lastIndexOf("."));

                    //判断文件是否分块
                    if (info.getChunks() != null && info.getChunk() != null) {
                        index = Integer.parseInt(info.getChunk());
                        fileName = String.valueOf(index) + ext;
                        String tempShardPath = PathUtil.getFileBasePath() + PathUtil.getTempShardFilePath(user.getUserId());
                        String mergePath = PathUtil.getFileBasePath() + PathUtil.getFilePath(user.getUserId());
                        //保存分片文件到临时目录
                        FileUtil.saveFile(tempShardPath, fileName, file);

                        // 验证所有分块是否上传成功，成功的话进行合并
                        boolean result = FileUtil.Uploaded(info, mergePath, tempShardPath);
                        //合并完成后保存文件信息
                        if (result) {
                            try {
                                User tempUser = userDao.getUserById(user.getUserId());
                                TFile fileInfo = new TFile();
                                TFileCategory fileCategory = new TFileCategory();
                                fileCategory.setFileCategoryId(2);
                                //TODO 根据文件后缀名获取文件分类
                                fileInfo.setFileCategory(fileCategory);
                                fileInfo.setLastEditTime(new Date());
                                fileInfo.setCreateTime(new Date());
                                Date validTime = new Date(System.currentTimeMillis() + validTimeMills);
                                fileInfo.setFileValidTime(validTime);
                                fileInfo.setFilePath(PathUtil.getFilePath(user.getUserId()));
                                fileInfo.setFileName(info.getFileName());
                                fileInfo.setFileSize(info.getSize());
                                fileInfo.setUser(user);
                                int res = fileDao.insertFile(fileInfo);
                                if (res <= 0) {
                                    throw new RuntimeException();
                                }
                                //更新用户信息
                                tempUser.setUsedCapacity(user.getUsedCapacity() + info.getSize());
                                userDao.updateUser(tempUser);
                            } catch (Exception e) {
                                throw new RuntimeException("文件保存失败");
                            }
                        }
                    } else {
                        TFileExecution fileExecution = addFile(file, user);
                        if (fileExecution.getState() != TFileStateEnum.SUCCESS.getState()) {
                            throw new RuntimeException("文件上传失败");
                        }
                    }
                } else {
                    throw new RuntimeException("文件上传失败");
                }
            } catch (Exception e) {
                return new TFileExecution(TFileStateEnum.INNER_ERROR);
            }
        }
        return new TFileExecution(TFileStateEnum.SUCCESS);
    }

    @Override
    public List<TFile> getFileList(User user) {
        return fileDao.getFilesByUserId(user.getUserId());
    }

    @Override
    @Transactional
    public TFileExecution deleteFile(int fileId, User user) {
        //删除本地存储的文件
        TFile file = fileDao.getFileById(fileId);
        String filePath = PathUtil.getFileBasePath() + file.getFilePath() + file.getFileName();
        boolean result = FileUtil.deleteFile(filePath);
        if (!result) {
            return new TFileExecution(TFileStateEnum.INNER_ERROR);
        }
        //如果文件有分享链接，则删除
        TFileLinkExecution fileLinkExecution = fileLinkService.deleteFileLink(fileId);
        if (fileLinkExecution.getState() != TFileLinkStateEnum.SUCCESS.getState()) {
            return new TFileExecution(TFileStateEnum.INNER_ERROR);
        }
        //更新数据库文件信息
        int res = fileDao.deleteFileById(file.getFileId());
        if (res <= 0) {
            return new TFileExecution(TFileStateEnum.INNER_ERROR);
        }
        //更新数据库用户信息
        user.setUsedCapacity(user.getUsedCapacity() - file.getFileSize());
        res = userDao.updateUser(user);
        if (res <= 0) {
            return new TFileExecution(TFileStateEnum.INNER_ERROR);
        }

        return new TFileExecution(TFileStateEnum.SUCCESS, file);
    }

    @Override
    public File getFile(int filed) {
        TFile fileInfo = fileDao.getFileById(filed);
        String filePath = PathUtil.getFileBasePath() + fileInfo.getFilePath() + fileInfo.getFileName();
        File file = new File(filePath);
        return file;
    }

    @Override
    public TFile getFileByFileId(int filed) {
        return fileDao.getFileById(filed);
    }


}
