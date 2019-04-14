$(function () {


    //获取用户信息
    getUserInfo();
    //获取用户已上传文件列表
    getFileListInfo();


    //侧边栏按钮点击
    //点击文件按钮
    $('#file-li').on('click', function () {
        //隐藏上传文件列表
        $('.file-upload-container').addClass('hidden');
        //显示已上传文件列表
        $('.file-info-container').removeClass('hidden');
        //重新获取用户数据
        getUserInfo();
        getFileListInfo();
    });

    //删除已上传文件
    $('body').on('click' , '.file-table-container .delete',function () {
        var elBtn = $(this);
        new Dialog().confirm('\
            <h6>您确定要删除此文件吗？</h6>\
            <p>文件删除后将不可找回，请谨慎操作！</p>'
            , {
                buttons: [{
                    value: "删除",
                    events: function(event) {
                        deleteFile(elBtn);
                        event.data.dialog.remove();
                    }
                }, {}]
            });
    });


    $('body').on('click', '.download-button-box', function () {
        var downloadBtn = $(this);
        downloadFile(downloadBtn);
        console.log("test");

    });

    $('body').on('click', '.share-button-box', function () {
        var shareBtn = $(this);
        createFileShareLink(shareBtn);
    });

    function createFileShareLink(obj) {
        var shareLink;
        var $tr = $(obj).parents("tr");
        var tempFileId = $tr.attr("id");
        var fileId = tempFileId.slice(tempFileId.indexOf('_') + 1);
        var formData = new FormData();
        formData.append("fileId", fileId);
        $.ajax({
            url: createShareLinkUrl,
            type: 'POST',
            async: false,
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    shareLink = data.shareLink;
                    copyToClipboard(shareLink);
                    console.log('获取分享链接成功');
                } else {
                    console.log("创建文件分享链接失败");
                }
            }
        });
    }

    function downloadFile(obj) {
        var $tr = $(obj).parents("tr");
        var tempFileId = $tr.attr("id");
        var fileId = tempFileId.slice(tempFileId.indexOf('_') + 1);
        var url = downloadFileUrl + "?fileId=" + fileId;
        $.ajax({
            url: url,
            type: 'GET',
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (!data.success) {
                    errorAlert("下载文件失败");
                }
            }
        });
    }


    function deleteFile(obj) {
        var $tr = $(obj).parents("tr");
        var tempFileId = $tr.attr("id");
        var fileId = tempFileId.slice(tempFileId.indexOf('_') + 1);

        $.ajax({
            url: deleteFileUrl + "?fileId=" + fileId,
            type: 'GET',
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    getUserInfo();
                    getFileListInfo();
                    successAlert("删除成功");
                    console.log('获取成功');
                } else {
                    errorAlert("删除失败");
                    console.log('获取失败');
                }
            }
        });
    }



    function successAlert(msg) {
        new Dialog().alert(msg, {
            type: 'success'
        });
    }

    function errorAlert(msg) {
        new Dialog().alert(msg, {
            type: 'warning'
        });
    }

    //粘贴数据到剪切板
    function copyToClipboard(text) {
        if (window.clipboardData && window.clipboardData.setData) {
            // IE specific code path to prevent textarea being shown while dialog is visible.
            return clipboardData.setData("Text", text);

        } else if (document.queryCommandSupported && document.queryCommandSupported("copy")) {
            var textarea = document.createElement("textarea");
            textarea.textContent = text;
            textarea.style.position = "fixed";  // Prevent scrolling to bottom of page in MS Edge.
            document.body.appendChild(textarea);
            textarea.select();
            try {
                return document.execCommand("copy");  // Security exception may be thrown by some browsers.
            } catch (ex) {
                console.warn("Copy to clipboard failed.", ex);
                return false;
            } finally {
                document.body.removeChild(textarea);
            }
        }
    }


});

