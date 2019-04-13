$(function () {

    //大文件分片上传
    var uploader = WebUploader.create({

        //设置选完文件后是否自动上传
        auto: false,
        // 文件接收服务端。
        server: '/transfer/BigFileUp',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',
        chunked: true, //开启分块上传
        chunkSize: 10 * 1024 * 1024,
        chunkRetry: 3,//网络问题上传失败后重试次数
        threads: 1, //上传并发数
        //fileNumLimit :1,
        fileSizeLimit: 2000 * 1024 * 1024,//最大上传文件总大小 2GB
        fileSingleSizeLimit: 2000 * 1024 * 1024,//单个文件上传大小上限 2GB
        resize: false//不压缩
    });







});