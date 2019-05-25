$(function () {
    var loginUrl = "/user/login";
    var mainPageUrl = "/transfer/main"

    $("#login").on('click', function () {
        //判断用户名和密码输入是否完整
        var username = $("#username").val();
        var password = $("#password").val();
        var usernameId = "#username";
        var passwordId = "#password"
        checkInfo(usernameId, "请输入用户名");
        checkInfo(passwordId, "请输入密码");
        if (username !== "" && password !== "") {
            var formData = new FormData();
            formData.append("username", username);
            formData.append("password", password);

            $.ajax({
                url: loginUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.status === 0) {
                        console.log('登录成功');
                        $(location).attr('href', mainPageUrl);
                        //重定向到登录页面
                    } else {
                        //弹框显示注册失败
                        errorToast("登录失败", "请输入正确的用户名或密码");
                    }
                }
            });
        }

    });

    function checkInfo(elementId, tips) {
        var val = $(elementId).val();
        var parent = $(elementId).parent().parent();
        var tempHtml = '<div class="input-tips">'
            + '<span>' + tips + '</span>'
            + '</div>';

        if (val === "") {
            if (!parent.children().is(".input-tips")) {
                parent.append(tempHtml);
            }
        }
    }

    function removeTips(elementId) {
        var parent = $(elementId).parent().parent()
        if (parent.children().is(".input-tips")) {
            parent.children(".input-tips").remove();
        }
    }

    $("#username").on("blur", function () {
        removeTips(this);
    });

    $("#password").on("blur", function () {
        removeTips(this);
    });



});