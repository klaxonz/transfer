
var getInfoUrl = "/transfer/getuserinfo",
    getFileListUrl = "/transfer/getfilelist",
    deleteFileUrl = "/transfer/deletefile",
    downloadFileUrl = "/transfer/download",
    createShareLinkUrl = "/transfer/createsharelink",
    indexPageUrl = "/transfer",
    loginPageUrl = "/transfer/login",
    registerPageUrl = "/transfer/register";


function getUserInfo() {
    $.ajax({
        url: getInfoUrl,
        type: 'POST',
        contentType: false,
        processData: false,
        cache: false,
        success: function (data) {
            if (data.success) {
                var totalSize = data.totalCapacity;
                var usedSize = data.usedCapacity;
                updateUserInfo(totalSize, usedSize);
                console.log('获取成功');
            } else {
                //TODO 错误处理
                console.log('获取失败');
            }
        }
    });
}

function getFileListInfo(){
    $.ajax({
        url: getFileListUrl,
        type: 'GET',
        contentType: false,
        processData: false,
        cache: false,
        success: function (data) {
            if (data.success) {
                updateFileList(data.files);
                console.log('获取成功');
            } else {
                console.log('获取失败');
            }
        }
    });
}

//更新用户信息
function updateUserInfo(totalSize, usedSize) {
    var $totalSize = $('#total-size');
    var $usedSize = $('#used-size');
    //更新总容量
    $totalSize.text(transferFileSizeFormat(totalSize));
    //更新已使用容量
    $usedSize.text(transferFileSizeFormat(usedSize));
}

function updateFileList(fileList) {

    var tempHtml = '';
    fileList.map(function (value, index) {
        tempHtml +=
            '<tr id="' + value.fileId + '">' +
            '    <td class="blank"></td>' +
            '    <td class="delete-box icon-wrapper uk-inline" >' +
            '        <div class="delete uk-position-center "></div>' +
            '    </td>' +
            '    <td class="folder-box icon-wrapper uk-inline">' +
            '        <div class="folder uk-position-center"></div>' +
            '    </td>' +
            '    <td class="menu-box icon-wrapper uk-inline">' +
            '        <div class="menu uk-position-center"></div>' +
            '    </td>' +
            '    <td class="divider uk-inline">' +
            '        <div class="uk-position-center"></div>' +
            '    </td>' +
            '    <td class="file-box uk-inline">' +
            '        <div class="file-wrapper uk-position-left" >' +
            '            <div class="file">' +
            '                <div class="file-name uk-text-bold"><p>' + value.fileName + '</p></div>' +
            '                <div class="file-size">' +  transferFileSizeFormat(value.fileSize) +'</div>' +
            '            </div>' +
            '        </div>' +
            '    </td>' +
            '   <td class="valid-time uk-text-bold">' +
            '       <p>有效期至</p><p>' +  transferDateTime(value.validTime)  + '</p>' +
            '   </td>' +
            '    <td class="share-button-box uk-inline">' +
            '        <div class="uk-position-left">' +
            '            <div class="share-wrapper">' +
            '                <div class="share-icon"></div>' +
            '                <div class="share-text uk-text-lead uk-text-bold"><span>分享</span></div>' +
            '            </div>' +
            '        </div>' +
            '    </td>' +
            '    <td class="download-button-box uk-inline">' +
            '        <div class="uk-position-left">' +
            '            <div class="download-wrapper">' +
            '                <div class="download-icon"></div>' +
            '                <div class="download-text uk-text-lead uk-text-bold"><span>下载</span></div>' +
            '            </div>' +
            '        </div>' +
            '    </td>' +
            '</tr>'
    });

    $('.file-table-container tbody').html(tempHtml);

}

