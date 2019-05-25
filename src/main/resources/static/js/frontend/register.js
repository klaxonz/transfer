$(function () {
    var checkInfoValue = {};
    var registerUrl = '/user/register',
        loginUrl = '/user/login';


    $("#submit").on('click', function () {

        if (isValid(checkInfoValue)) {
            var userName = $("#name").val();
            var userPassword = $("#pass").val();
            var userEmail = $("#email").val();

            var formData = new FormData();
            formData.append("userName", userName);
            formData.append("userPassword", userPassword);
            formData.append("userEmail", userEmail);

            //将数据提交至后台处理相关操作
            $.ajax({
                url: registerUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.status === 0) {
                        $.lightTip.success('注册成功');
                        console.log('注册成功');
                        $(location).attr('href', loginPageUrl);
                        //重定向到登录页面
                    } else {
                        //用户注册失败
                        errorToast("注册失败", "请检查注册信息是否填写正确");
                    }
                }
            });
        }else {
            //前端注册信息检验失败
            errorToast("注册失败", "请检查注册信息是否填写正确");
        }


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
            tips = "用户名为3~16位的数字，字母，下划线",
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

