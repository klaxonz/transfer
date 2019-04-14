$(function () {

    $('body').on('click', '#title', function () {
        $(location).attr('href', indexPageUrl);

    })






});







function setFileName(filename) {
    $('.file-name p').text(filename);
}

function setFileSize(fileSize) {
    $('.file-size p').text(transferFileSizeFormat(fileSize));
}

function setFileValidTime(fileValidTime) {
    $('#time').text(transferDateTime(fileValidTime));
}

function setDownloadButtonListen(fileId) {
    $('body').on('click', '.download-button button', function () {
        var url = downloadFileUrl + "?fileId=" + fileId;
        $.ajax({
            url: url,
            type: 'GET',
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
            }
        });

    });
}

function setFileLinkPasswod(fileLinkPassword) {
    if (!(fileLinkPassword === undefined && fileLinkPassword !== '')) {
        $('.share-password-input').addClass('hidden')
    }
}
