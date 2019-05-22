$(function () {

    //大文件分片上传

    var file_count = 0;
    var totalSize = 0;
    var uploadedSizeFastAndShow = 0;
    var uploadedSizeSlow = 0;

    var uploader = WebUploader.create({

        swf: '/js/Uploader.swf',
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

    // 当有文件被添加进队列的时候
    uploader.on('fileQueued', function (file) {

        var fileNums = uploader.getFiles().length;
        console.log("fileNums: " + fileNums);
        // var $ul = $(".box ul");
        // var $box = $(".box");
        // var boxHeight = $box[0].scrollHeight;
        // var $fileSize = $("#file-size");
        //删除所有已上传文件的DOM节点
        removeCompleteFileOnDom();
        //向DOM添加文件信息
        appendFilesToDom(file);
        //更新提示信息
        fileListTipsUpadte();
        //记录总文件大小
        totalSize += file.size;
        //设置文件总大小
        file_count++;

        //md5计算
        uploader.md5File(file)
            .progress(function (percentage) {
                //上传文件item右侧按钮设置为不可点击状态
                var fileId = '#' + file.id;
                var $tr = $(fileId);
                var $button = $tr.find("button");
                $button.attr("disabled", true);
                console.log('Percentage:', percentage);
            })
            // 完成
            .then(function (fileMd5) { // 完成
                filePrepared(file);
                var end = +new Date();
                console.log("before-send-file  preupload: file.size=" + file.size + " file.md5=" + fileMd5);
                file.wholeMd5 = fileMd5;//获取到了md5
                //uploader.options.formData.md5value = file.wholeMd5;//每个文件都附带一个md5，便于实现秒传
                console.info("MD5=" + fileMd5);
            });

        //删除要上传的文件
        //每次添加文件都给btn-delete绑定删除方法
        $("tr .delete").click(function () {
            var $tr = $(this).parent().parent();
            var file = uploader.getFile($($tr).attr("id"), true); //获取文件对象
            var fileSize = file.size;
            uploader.removeFile(file);
            $tr.fadeOut();//视觉上消失了
            $tr.remove();//DOM上删除了
            fileListTipsUpadte();
            file_count--;
            //TODO 文件总数为0，显示提示信息

            //更新文件总大小
            totalSize -= fileSize;
        });
    });

    //发送前填充数据
    uploader.on( 'uploadBeforeSend', function( block, data ) {
        // block为分块数据。
        // file为分块对应的file对象。
        var file = block.file;
        var fileMd5 = file.wholeMd5;
        //改变文件item右侧按钮按钮显示状态
        fileUploading(file);
        // 修改data可以控制发送哪些携带数据。
        console.info("fileName= "+file.name+" fileMd5= "+fileMd5+" fileId= "+file.id);
        console.info("input file= "+ file_count);
        // 将存在file对象中的md5数据携带发送过去。
        data.md5value = fileMd5;//md5
        data.fileName = $("#"+file.id).find(".file-name p").val();
        console.log("fileName: "+data.fileName);
        // 删除其他数据
        // delete data.key;
        if(block.chunks>1){ //文件大于chunksize 分片上传
            data.isChunked = true;
            console.info("data.isChunked= "+data.isChunked);
        }else{
            data.isChunked = false;
            console.info("data.isChunked="+data.isChunked);
        }
    });

    //点击按钮开始上传
    $("#uploadBtn").click(function () {
        console.log("开始上传");
        uploader.upload();//上传
    });

    //删除上传列表的所有文件
    $('#allClearBtn').on('click', function () {
        var $fileContainer = $('.upload-files-container tbody');
        //清空DOM文件列表
        $fileContainer.html('');
        //进度条置0
        setProgressValue(0);
        fileListTipsUpadte();
        //webuploader文件队列也需要清空
        uploader.reset();
    });


    //单个文件完成后的回调方法
    uploader.on('uploadComplete', function (file) {//上传完成后回调
        //改变显示状态
        fileComplete(file);
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {

        //获取文件进度对象
        var $uploadProgress = $('#progressbar');
        var length = uploader.getFiles().length;
        var size = file.size;
        var per = percentage.toFixed(2);
        uploadedSizeFastAndShow = uploadedSizeSlow + size * per;
        //计算已上传百分比
        var uploaderPer = uploadedSizeFastAndShow / totalSize;
        //更新百分比
        setProgressValue((uploaderPer * 100).toFixed(0));
        console.log((uploaderPer * 100).toFixed(0));

        console.log(file.name + " 已上传文件大小: " + transferFileSizeFormat(uploadedSizeFastAndShow));
        console.log(file.name + " 已上传文件大小(慢): " + transferFileSizeFormat(uploadedSizeSlow));
        console.log(file.name + " 上传进度: " + percentage * 100 + "%");

    });

    //上传成功后
    uploader.on('uploadSuccess', function (file) {
        uploadedSizeSlow += file.size;
        console.log("file size: " + file.size);
        console.log(file.name + " 上传成功");
        //判断文件队列是否还有文件需要上传,更新上传信息
        file_count--;

    });

    //所有文件上传结束后
    uploader.on('uploadFinished', function () {
        file_count = 0;
        totalSize = 0;
        uploadedSizeFastAndShow = 0;
        uploadedSizeSlow = 0;
        //更新用户信息
        getUserInfo();
        getFileListInfo();
    });


    //点击上传按钮切换至上传页面
    $('#upload-li').on('click', function () {

        //隐藏按钮
        $('.upload-button-box').addClass('hidden');
        //隐藏文件详情页
        $('.file-info-container').addClass('hidden');
        //显示上传界面
        $('.file-upload-container').removeClass('hidden');
        uploader.refresh();

    });


    //往DOM中添加上传文件信息
    function appendFilesToDom(file) {
        var $parent = $('.upload-files-container tbody');
        var domHtml =
            ' <tr id="' + file.id + '">' +
            '   <td class="blank"></td>' +
            '   <td class="delete-box icon-wrapper uk-inline">' +
            '       <div class="delete uk-position-center "></div>' +
            '   </td>' +
            '   <td class="folder-box icon-wrapper uk-inline">' +
            '       <div class="folder uk-position-center"></div>' +
            '   </td>' +
            '   <td class="menu-box icon-wrapper uk-inline">' +
            '       <div class="menu uk-position-center"></div>' +
            '   </td>' +
            '   <td class="divider uk-inline">' +
            '       <div class="uk-position-center"></div>' +
            '   </td>' +
            '   <td class="file-box uk-inline">' +
            '       <div class="file-wrapper uk-position-left" >' +
            '           <div class="file">' +
            '               <div class="file-name uk-text-bold">' +
            '                   <p>' + file.name + '</p>' +
            '               </div>' +
            '               <div class="file-size">' + transferFileSizeFormat(file.size) + '</div>' +
            '           </div>' +
            '       </div>' +
            '   </td>' +
            '   <td class="suspend-button-box uk-inline">' +
            '       <div class="uk-position-left">' +
            '           <div class="uk-button-group">' +
            '               <button class="uk-button uk-button-small">准备中</button>' +
            '           </div>' +
            '       </div>' +
            '   </td>' +
            '</tr>';
        $parent.append(domHtml);

    }

    function setProgressValue(value) {
        if (value >= 0 && value <= 100) {
            $('#progressbar').attr("value", value);
        } else {
            $('#progressbar').attr("value", 0);
        }
    }


    //删除所有上传完成的DOM节点
    function removeCompleteFileOnDom() {
        var $tbody = $('.upload-files-container tbody');
        var $tr = $('.upload-files-container tbody > tr').each(function () {
            if ($(this).hasClass('upload-compeleted')) {
                $(this).remove();
            }
        });

    }

    //计算文件md5值完成时的按钮状态
    function filePrepared(file) {
        var fileId = '#' + file.id;
        var $tr = $(fileId);
        var $button = $tr.find("button");
        $button.css("background", "#0057c3");
        $button.css("margin-left", "10px");
        $button.text("开始");
        $button.attr("disabled", false);
    }

    //正在上传状态
    function fileUploading(file) {
        var fileId = '#' + file.id;
        var $tr = $(fileId);
        var $button = $tr.find("button");
        $button.css("background", "#e72734");
        $button.css("margin-left", "12px");
        $button.text("暂停");
    }

    //单个文件上传完成状态
    function fileComplete(file) {
        var fileId = '#' + file.id;
        var $tr = $(fileId);
        var $button = $tr.find("button");
        $button.css("background", "#39f");
        $button.css("margin-left", "12px");
        $button.text("完成");
        //给该文件增加上传完成信息
        $tr.addClass("upload-compeleted")
    }

    //上传列表是否为空，并设置相应的提示信息
    function fileListTipsUpadte() {
        var $tr = $('.upload-files-container tbody > tr');
        var $tips = $('.file-null-tips');
        if ($tr.length === 0) {
            //没有上传文件则显示提示信息
            if ($tips.hasClass('hidden')){
                $tips.removeClass('hidden');
            }
        } else {
            if (!$tips.hasClass('hidden')){
                $tips.addClass('hidden');
            }
        }
    }


});