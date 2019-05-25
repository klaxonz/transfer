$(function () {

    //大文件分片上传
    var uploader = WebUploader.create({
        swf: '/js/Uploader.swf',
        auto: false,
        server: '/file/upload',
        pick: '#picker',
        chunked: true,
        chunkSize: 10 * 1024 * 1024,
        chunkRetry: 3,
        threads: 1,
        fileSizeLimit: 2000 * 1024 * 1024,
        fileSingleSizeLimit: 2000 * 1024 * 1024,
        resize: false
    });

    // 当有文件被添加进队列的时候
    uploader.on('fileQueued', function (file) {
        var fileNums = uploader.getFiles().length;
        console.log("fileNums: " + fileNums);
        //删除所有已上传文件的DOM节点
        removeCompleteFileOnDom();
        //向DOM添加文件信息
        appendFilesToDom(file);
        //更新提示信息
        fileListTipsUpadte();
        //设置文件guid
        file.guid = WebUploader.guid();
        //md5计算
        uploader.md5File(file)
            .progress(function (percentage) {
                //失效
            })
            .then(function (fileMd5) { // 完成
                filePrepared(file);
                file.wholeMd5 = fileMd5;//获取到了md5

            });

        //每次添加文件都给btn-delete绑定删除方法
        $("tr .delete").click(function () {
            var $tr = $(this).parent().parent();
            var file = uploader.getFile($($tr).attr("id"), true); //获取文件对象
            uploader.removeFile(file);
            $tr.fadeOut();//视觉上消失了
            $tr.remove();//DOM上删除了
            fileListTipsUpadte();
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
        data.md5 = fileMd5;//md5
        data.fileName = $("#"+file.id).find(".file-name p").val();
        //GUID
        data.guid = file.guid;
        // 删除其他数据
        if(block.chunks>1){ //文件大于chunksize 分片上传
            data.isChunked = true;
            console.info("data.isChunked= "+data.isChunked);
        }else{
            data.isChunked = false;
            console.info("data.isChunked="+data.isChunked);
        }
    });

    //点击按钮开始上传
    $("#uploadBtn").on("click",function () {
        console.log("开始上传");
        uploader.upload();//上传
    });

    //点击全部暂停按钮
    $("#allStopBtn").on("click", function () {
        uploader.stop(true);
    });

    //删除上传列表的所有文件
    $('#allClearBtn').on('click', function () {
        var $fileContainer = $('.upload-files-container tbody');
        //清空DOM文件列表
        $fileContainer.empty();
        //进度条置0
        setProgressValue(0);
        fileListTipsUpadte();
        //webuploader文件队列也需要清空
        uploader.reset();
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        fileProgress(file, percentage);
    });

    //上传完成后，不管成功或者失败
    uploader.on('uploadComplete', function (file) {
        fileComplete(file);
    });

    //上传成功后
    uploader.on('uploadSuccess', function (file, response) {
        console.log("response code: " + response.status);
        console.log("response code: " + response.msg);
        if (response.status === 15) {
            fileSuccess(file);
            getUserInfo();
        } else {
            fileError(file);
        }
    });

    //上传失败后
    uploader.on('uploadError', function (file, reason) {
        fileError(file);
        console.log("error:" + reason);
    });

    //所有文件上传结束后
    uploader.on('uploadFinished', function () {
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
        //清除删除文件列表
        $('.file-upload-container tbody').empty();
        //进度条归零
        // $("#progressbar").val("0");
        //显示提示信息
        $(".file-null-tips").removeClass("hidden");
        //刷新uploader
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
            '   <td class="file-status-box uk-inline">' +
            '       <div class="uk-position-center-right">' +
            '          <span class="uk-badge file-check-tip">正在校验</span>' +
            '          <span class="uk-badge file-wait-tip hidden">等待上传</span>' +
            '          <span class="uk-badge file-upload-tip hidden">上传中<span class="uplaod-process"></span></span>' +
            '          <span class="uk-badge file-success-tip hidden">上传成功</span>' +
            '          <span class="uk-badge file-suppend-tip hidden">已暂停</span>' +
            '          <span class="uk-badge file-error-tip hidden">上传出错</span>' +
            '      </div>' +
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
        $('.upload-files-container tbody > tr').each(function () {
            if ($(this).hasClass('upload-complete')) {
                $(this).remove();
            }
        });
    }

    //计算文件md5值完成时的按钮状态
    function filePrepared(file) {
        var fileId = '#' + file.id;
        uploadStatusChange(fileId, "file-wait-tip");
    }

    //正在上传状态
    function fileUploading(file) {
        var fileId = '#' + file.id;
        uploadStatusChange(fileId, "file-upload-tip")
    }

    //单个文件上传完成状态
    function fileComplete(file) {
        var fileId = '#' + file.id;
        var $tr = $(fileId);
        $tr.addClass("upload-complete");
    }

    //单个文件上传成功状态
    function fileSuccess(file) {
        var fileId = '#' + file.id;
        uploadStatusChange(fileId, "file-success-tip");
    }

    //单个文件上传失败状态
    function fileError(file) {
        var fileId = '#' + file.id;
        uploadStatusChange(fileId, "file-error-tip");
    }

    //单个文件上传进度
    function fileProgress(file,percentage) {
        var fileId = '#' + file.id;
        var $tr = $(fileId);
        var $p = $tr.find(".uplaod-process");
        var per = percentage * 100;
        $p.text(per.toFixed(0) + "%");
    }


    function uploadStatusChange(fileId, changeStatusClass) {
        var $tr = $(fileId);
        var tips = $tr.find(".uk-badge");
        tips.map(function (value,index) {
            var $item = $(index);
            if (!$item.hasClass("hidden")) {
                $item.addClass("hidden");
            }
            if ($item.hasClass(changeStatusClass)) {
                if ($item.hasClass("hidden")) {
                    $item.removeClass("hidden");
                }
            }
        });
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