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
    });
}

function setFileLinkPasswod(fileLinkPassword) {
    if (!(fileLinkPassword === undefined && fileLinkPassword !== '')) {
        $('.share-password-input').addClass('hidden')
    }
}
