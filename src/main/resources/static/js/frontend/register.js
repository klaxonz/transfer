$(function () {
    var checkInfoValue = {};
    var registerUrl = '/transfer/registercheck',
        loginUrl = '/transfer/login';


    $("#submit").on('click', function () {

        if (isValid(checkInfoValue)) {
            var user = {};

            user.userName = $("#name").val();
            user.userPassword = $("#pass").val();
            user.userEmail = $("#email").val();
            var formData = new FormData();
            formData.append("userStr", JSON.stringify(user));

            //将数据提交至后台处理相关操作
            $.ajax({
                url: registerUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                        $.lightTip.success('注册成功');
                        console.log('注册成功');
                        $(location).attr('href', loginUrl);
                        //重定向到登录页面
                    } else {
                        //弹框显示注册失败
                        $.lightTip.error('注册失败');
                        console.log('注册失败');
                    }
                }
            });
        }else {
            console.log("failure")
        }


    });

    $("#submit").mouseover(function () {
        if (!isValid(checkInfoValue)) {
            $("#submit").css("background-color", "#d9d9f3");
        }
    });
    $("#submit").mouseout(function () {
        $("#submit").css("background-color","#6dabe4");
    });


    function isValid(data) {
        return data.userName === true && data.userPassword === true
            && data.userPasswordConfirm === true && data.userEmail === true;
    }

    //检测用户信息
    function checkInfo(obj, id, uploadValue, regex, tips) {
        var parent = $(obj).parent().parent();
        var value = $('#' + id).val();
        var res = regex.exec(value);
        if (!res) {
            var tempHtml = '<div class="input-tips">'
                + '<span>' + tips + '</span>'
                + '</div>';

            if (!parent.children().is(".input-tips")){
                parent.append(tempHtml);
            }
            checkInfoValue[uploadValue] = false;
        } else {
            if (parent.children().is(".input-tips")){
                parent.children().remove(".input-tips");
            }
            checkInfoValue[uploadValue] = true;
        }
    }


    //用户注册信息检测
    //用户名校验
    $("#name").on("change", function () {
        var regex = /^[a-zA-Z0-9_-]{3,16}$/,
            tips = "用户名为3~16位的数字，字母，下划线组合",
            id = 'name',
            uploadValue = 'userName';
        checkInfo(this, id, uploadValue, regex, tips);
    });


    //密码校验
    $("#pass").on("change", function () {
        var regex = /^[a-zA-Z0-9_-]{8,}$/,
            tips = "密码须为不小于8位的数字及字母组合",
            id = 'pass',
            uploadValue = 'userPassword';
        checkInfo(this, id, uploadValue, regex, tips);

    });

    //邮箱校验
    $("#email").on("change", function () {
        var regex = /^([A-Za-z0-9_\-\.\u4e00-\u9fa5])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,8})$/,
            tips = "邮箱格式不正确",
            id = 'email',
            uploadValue = 'userEmail';
        checkInfo(this, id, uploadValue, regex, tips);
        
    });

    //确认密码校验
    $("#re_pass").on("change", function () {
        var parent = $(this).parent().parent();
        var password = $("#pass").val();
        var passwordConfirm = $("#re_pass").val();
        var tips = "两次密码输入不一致";
        //密码须为不小于8位的数字及字母组合
        if (!(password === passwordConfirm)) {
            var tempHtml = '<div class="input-tips">'
                + '<span>' + tips + '</span>'
                + '</div>';
            if (!parent.children().is(".input-tips")){
                parent.append(tempHtml);
            }
            checkInfoValue.userPasswordConfirm = false;
        }else {
            if (parent.children().is(".input-tips")){
                parent.children().remove(".input-tips");
            }
            checkInfoValue.userPasswordConfirm = true;
        }
    });

});

