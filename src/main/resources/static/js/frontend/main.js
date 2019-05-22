$(function () {





    //设置滚动条样式
    $(window).on("load", function () {
        $(".file-table-container, .upload-files-container").mCustomScrollbar({
            axis: "y",
            theme: "dark"
        });
    });
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

    //顶部右侧导航栏按钮
    $('.home-icon-box').on('click', function () {
        $(location).attr('href', indexPageUrl);
    });

    $('.log-out-icon-box').on('click', function () {

        $.ajax({
            url: createShareLinkUrl,
            type: 'GET',
            async: false,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
            }
        });
        $(location).attr('href', logoutUrl);
    });


    //删除已上传文件
    $('body').on('click', '.file-table-container .delete', function () {
        var elBtn = $(this);
        $.confirm({
            icon: 'fa fa-warning',
            title: '删除文件',
            content: '是否确认删除文件，删除不可找回!',
            buttons: {
                tryAgain: {
                    text: '确认',
                    action: function () {
                        deleteFile(elBtn);
                    }
                },
                close: {
                    text: '取消'
                }
            }
        });

    });


    $('body').on('click', '.download-button-box', function () {
        var downloadBtn = $(this);
        downloadFile(downloadBtn);
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
                    successAlert("操作成功", "获取分享链接成功,链接已复制至剪切板!")
                } else {
                    errorAlert("操作失败", "获取分享链接失败");
                }
            }
        });
    }

    function downloadFile(obj) {
        var $tr = $(obj).parents("tr");
        var tempFileId = $tr.attr("id");
        var fileId = tempFileId.slice(tempFileId.indexOf('_') + 1);
        var url = downloadFileUrl;
        var form = $("<form>");
        form.attr("style","display:none");
        form.attr("method","get");
        form.attr("action", url);
        var input = $("<input>");
        input.attr("type","hidden");
        input.attr("name", "fileId");
        input.attr("value", fileId);
        $("body").append(form);
        form.append(input);
        form.submit();
        form.remove();
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
                    successAlert("操作成功", "文件已成功删除!");

                } else {
                    errorAlert("操作失败", "em...文件删除失败");

                }
            }
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

