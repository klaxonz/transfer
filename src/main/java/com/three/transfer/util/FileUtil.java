package com.three.transfer.util;

import com.three.transfer.dto.FileHolder;
import com.three.transfer.entity.UploadInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();

    public static String getFileStoreRelativeAddr (FileHolder file, String targetAddr) {
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + file.getFileName();
        return relativeAddr;
    }


    /**
     * 创建目标路径所涉及到的目录
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getFileBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }


    public static boolean deleteFile(String filePath) {

        boolean flag = false;

        if (!"".equals(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                flag = file.delete();
            }
        } else {
            flag = false;
        }
        return flag;
    }



    public static boolean saveFile(String tempShardFilePath, String fileFullName, MultipartFile file)
            throws Exception {

        byte[] data = readInputStream(file.getInputStream());
        // new一个文件对象用来保存文件，默认保存再当前用户的临时文件目录
        File uploadFile = new File(tempShardFilePath + fileFullName);
        // 判断文件夹是否存在，不存在就创建一个
        File fileDirectory = new File(tempShardFilePath);
        synchronized (fileDirectory) {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdirs()) {
                    throw new Exception("文件夹创建失败！路径为：" + tempShardFilePath);
                }
            }
        }

        // 创建输出流
        try (FileOutputStream outStream = new FileOutputStream(uploadFile)) {// 写入数据
            outStream.write(data);
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return uploadFile.exists();
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }

        // 把outStream里的数据写入内存
        byte[] data = outStream.toByteArray();
        //关闭输入输出流
        inStream.close();
        outStream.close();

        return data;
    }

    private final static List<UploadInfo> uploadInfoList = new ArrayList<>();

    public static boolean Uploaded(UploadInfo info, String mergePath, String shardFilePath) throws Exception {

        String md5 = info.getMd5();
        String chunk = info.getChunk();
        String chunks = info.getChunks();
        String fileName = info.getFileName();
        String ext = info.getExt();

        synchronized (uploadInfoList) {

            if ((md5!=null&&!md5.equals(""))&&(chunks!=null&&!chunks.equals(""))&&!isNotExist(md5,chunk)) {
                uploadInfoList.add(info);
            }
        }
        boolean allUploaded = isAllUploaded(md5, chunks);
        int chunksNumber = Integer.parseInt(chunks);

        if (allUploaded) {
            mergeFile(chunksNumber, fileName, mergePath, shardFilePath, ext);
        }
        return allUploaded;
    }

    //判断在uploadInfoList是否有存在MD5和chunk都相同的元素
    private static boolean isNotExist(final String md5, final String chunk) {
        boolean flag =false;
        for (UploadInfo uploadInfo : uploadInfoList) {
            if (uploadInfo.getChunk().equals(chunk)&&uploadInfo.getMd5().equals(md5)) {
                //若md5和chunk都相同，则认为两条记录相同，返回true
                flag=true;
            }
        }
        return flag;
    }

    private static boolean isAllUploaded(final String md5, String chunks) {
        int size = uploadInfoList.stream().filter(new Predicate<UploadInfo>() {
            @Override
            public boolean test(UploadInfo item) {
                return item.getMd5().equals(md5);
            }
        }).distinct().collect(Collectors.toList()).size();
        boolean bool = (size == Integer.parseInt(chunks));
        if (bool) {
            synchronized (uploadInfoList) {
                uploadInfoList.removeIf(new Predicate<UploadInfo>() {
                    @Override
                    public boolean test(UploadInfo item) {
                        return Objects.equals(item.getMd5(), md5);
                    }
                });
            }
        }
        return bool;
    }

    @SuppressWarnings("resource")
    private static void mergeFile(int chunksNumber, String fileName, String mergePath, String shardFilePath, String ext) {

        /* 合并输入流 */
        SequenceInputStream s;
        InputStream s1;
        try {
            s1 = new FileInputStream(shardFilePath + 0 + ext);
            String tempFilePath;
            InputStream s2 = new FileInputStream(shardFilePath + 1 + ext);
            InputStream s3;
            s = new SequenceInputStream(s1, s2);
            for (int i = 2; i < chunksNumber; i++) {
                tempFilePath = shardFilePath + i + ext;
                s3 = new FileInputStream(tempFilePath);
                s = new SequenceInputStream(s, s3);
            }
            // 通过输出流向文件写入数据(转移正式文件到目标目录)
            // uploadFolderPath + guid + ext
            saveStreamToFile(s, mergePath, fileName);
            // 删除保存分块文件的文件夹
            deleteFolder(shardFilePath);


            s2.close();
            s.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static boolean deleteFolder(String shardFilePath) {
        File dir = new File(shardFilePath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dir.delete();
    }

    private static void saveStreamToFile(SequenceInputStream inputStream, String filePath, String newName)
            throws Exception {
        File fileDirectory = new File(filePath);
        synchronized (fileDirectory) {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new Exception("保存文件的父文件夹创建失败！路径为：" + fileDirectory);
                }
            }
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new Exception("文件夹创建失败！路径为：" + fileDirectory);
                }
            }
        }

        /* 创建输出流，写入数据，合并分块 */
        OutputStream outputStream = new FileOutputStream(filePath + newName);
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            outputStream.close();
        }
    }

    public static void download(File file, HttpServletRequest request, HttpServletResponse response) {
        ServletContext context = request.getServletContext();
        // get MIME type of the file
        String mimeType = mimeType = "application/octet-stream";
        // set content attributes for the response
        response.setContentType(mimeType);
        // response.setContentLength((int) downloadFile.length());

        // set headers for the response

        String fileName = new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);
        // 解析断点续传相关信息
        response.setHeader("Accept-Ranges", "bytes");
        long downloadSize = file.length();
        long fromPos = 0, toPos = 0;
        if (request.getHeader("Range") == null) {
            response.setHeader("Content-Length", downloadSize + "");
        } else {
            // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);
            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }
            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }
            response.setHeader("Content-Length", size + "");
            downloadSize = size;
        }
        // Copy the stream to the response's output stream.
        RandomAccessFile in = null;
        OutputStream out = null;
        try {
            in = new RandomAccessFile(file, "rw");
            // 设置下载起始位置
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            // 缓冲区大小
            int bufLen = (int) (downloadSize < 2048 ? downloadSize : 2048);
            byte[] buffer = new byte[bufLen];
            int num;
            int count = 0; // 当前写到客户端的大小
            out = response.getOutputStream();
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;
                //处理最后一段，计算不满缓冲区的大小
                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize-count);
                    if(bufLen==0){
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }




    }


}
