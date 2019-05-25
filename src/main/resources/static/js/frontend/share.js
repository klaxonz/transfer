$(function () {

    getFileInfoByShareLink();

    function getFileInfoByShareLink() {
        //通过地址栏获取分享链接地址
        var url = window.location.href;
        var shareLink = url.substr(url.lastIndexOf("/") + 1);
        shareLink = shareUrlPrefix + shareLink;
        $.ajax({
            url: shareLink,
            type: 'GET',
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.status === 0) {
                    handleSharePage(data.data);
                } else {
                    //todo 显示错误页面
                }
            }
        });

    }


    function handleSharePage(data) {
        if (data.hasPassword) {
            $(".share-code-container").removeClass("hidden");
            $(".confirm-button button").click(function () {
                var shareCode = $("#share-num").val();
                var linkAddr = window.location.href;
                var formData = new FormData();
                formData.append("linkAddr", linkAddr);
                formData.append("shareCode", shareCode);
                $.ajax({
                    url: confirmShareCodeUrl,
                    type: 'POST',
                    data: formData,
                    contentType: false,
                    processData: false,
                    cache: false,
                    success: function (data) {
                        if (data.status === 0) {
                            showFileInfo(data.data);
                        } else {
                            errorToast("错误提示",data.msg);
                        }
                    }
                });

            });
        } else {
            showFileInfo(data);
        }
    }

    function showFileInfo(data) {
        $(".share-code-container").addClass("hidden");
        $(".share-file-info-container").removeClass("hidden");
        $(".file-name p").text(data.fileName);
        $(".file-name p").attr("id", "file-" + data.fileId);
        $(".file-size p").text(transferFileSizeFormat(data.fileSize));
        $("#time").text(data.fileValidTime);
    }


    $(".download-button button").click(function () {
        var fileId = $(".file-name p").attr("id");
        fileId = fileId.substr(fileId.indexOf("-") + 1);
        var url = downloadShareFileUrl;
        downloadFile(url, fileId);
    });


    function downloadFile(url,fileId) {
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


    $('body').on('click', '#title', function () {
        $(location).attr('href', indexPageUrl);
    })

});
