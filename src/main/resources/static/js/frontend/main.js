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
            url: logoutUrl,
            type: 'GET',
            async: false,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
            }
        });
        $(location).attr('href', indexPageUrl);
    });


    //删除已上传文件
    $('body').on('click', '.file-table-container .delete', function () {
        var elBtn = $(this);
        $.confirm({
            icon: 'fa fa-warning',
            title: '删除文件',
            content: '是否确认删除文件，删除不可找回!',
            animateFromElement: false,
            animation: 'RotateYR',
            closeAnimation: 'RotateYR',
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
        createShareLinkAlert(shareBtn);
    });

    function createShareLinkAlert(obj) {
        $.confirm({
            icon: 'fa fa-check-square',
            title: "生成链接",
            content: "分享链接是否需要分享码?",
            animation: 'RotateYR',
            closeAnimation: 'RotateYR',
            animateFromElement: false,
            closeIcon: true,
            buttons: {
                "是": function () {
                    promptAlert(obj,createFileShareLink);
                },
                "否": function () {
                    createFileShareLink(obj);
                }
            }
        });
    }


    function promptAlert(obj, cal) {
        $.confirm({
            title: '分享码',
            icon: 'fa fa-barcode',
            content: '' +
                '<form action="" class="formCode">' +
                '<div class="form-group">' +
                '<label>请输入分享码!</label>' +
                '<input type="text" placeholder="分享码" class="code form-control" required />' +
                '</div>' +
                '</form>',
            animateFromElement: false,
            closeIcon: true,
            buttons: {
                formSubmit: {
                    text: '提交',
                    action: function () {
                        var code = this.$content.find('.code').val();
                        if(!code){
                            errorAlert("操作失败", '未输入分享码,请重新操作');
                            return false;
                        }
                        //发送请求
                        cal(obj, code);
                    }
                },
                "取消": function () {
                }
            }
        });
    }


    function createFileShareLink(obj, shareCode) {
        var shareLink;
        var $tr = $(obj).parents("tr");
        var tempFileId = $tr.attr("id");
        var fileId = tempFileId.slice(tempFileId.indexOf('_') + 1);
        var formData = new FormData();
        formData.append("fileId", fileId);
        if (shareCode !== undefined) {
            formData.append("linkPassword", shareCode);
        }
        $.ajax({
            url: createShareLinkUrl,
            type: 'POST',
            async: false,
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.status === 0) {
                    shareLink = data.data.fileLinkAddr;
                    shareCode = data.data.fileLinkPassword;
                    copyToClipboard(shareLink,shareCode);
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
                if (data.status === 0) {
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
    function copyToClipboard(link, password) {
        if (password !== null) {
            link = link + ",分享码: " + password;
        }
        if (window.clipboardData && window.clipboardData.setData) {
            // IE specific code path to prevent textarea being shown while dialog is visible.
            return clipboardData.setData("Text", link);

        } else if (document.queryCommandSupported && document.queryCommandSupported("copy")) {
            var textarea = document.createElement("textarea");
            textarea.textContent = link;
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

