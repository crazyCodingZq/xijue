let login = 2;
let send = false;
if ($("#m").val() === "1") {
    changeType(1)
} else if ($("#m").val() === "2") {
    changeType(2)
}
$(".login100-form").delegate(".sendSms", "click", function () {
    let mobile = $.trim($("#mobile").val());
    if (checkPhone(mobile)) {
        send = true;
        $.ajax({
            url: '/code/sms',
            type: 'post',
            dataType: 'json',
            async: true,
            data: {mobile: mobile},
            success: function (result) {
                if (result.success) {
                }
            },
            error: function () {

            }
        });
        let oTime = $(".login100-form .time"),
            oSend = $(".login100-form .sendSms"),
            num = parseInt(oTime.text()),
            oEm = $(".login100-form .time em");
        $(this).hide();
        oTime.removeClass("hide");
        let timer = setInterval(function () {
            let num2 = num -= 1;
            oEm.text(num2);
            if (num2 === 0) {
                clearInterval(timer);
                oSend.text("重新发送验证码");
                oSend.show();
                oEm.text("60");
                oTime.addClass("hide");
            }
        }, 1000);
    }
});

function checkPhone(phone) {
    if (phone === '') {
        showToast("请输入手机号");
        return false;
    }
    let param = /^1[34578]\d{9}$/;
    if (!param.test(phone)) {
        showToast("手机号码格式错误，请重新输入");
        return false;
    }
    return true;
}

function checkPass(pass) {
    if (pass === '') {
        showToast("请输入密码");
        return false;
    }
    let param = /^[a-zA-Z0-9]{6,12}$/;
    if (!param.test(pass)) {
        showToast("密码必须包含字母和数组，且最少6位不超过12位");
        return false;
    }
    return true;
}

function checkSmsCode(code) {
    if (code === '') {
        showToast("请输入验证码");
        return false;
    }
    let param = /^[\d]{4,8}$/;
    if (!param.test(code)) {
        showToast("验证码错误");
        return false;
    }
    return true;
}

function showToast(msg, duration) {
    duration = isNaN(duration) ? 3000 : duration;
    var m = document.createElement('div');
    m.innerHTML = msg;
    m.style.cssText = "width:20%; min-width:180px; background:#000; opacity:0.6; height:auto;min-height: 50px; color:#fff; line-height:50px; text-align:center; border-radius:4px; position:fixed; top:20%; left:42%; z-index:999999;";
    document.body.appendChild(m);
    setTimeout(function () {
        var d = 0.5;
        m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in';
        m.style.opacity = '0';
        setTimeout(function () {
            document.body.removeChild(m)
        }, d * 1000);
    }, duration);
}


function changeType(t) {
    if (t === 1) {
        $(".smsCode").show();
        $('#smsLogin').attr("action", "/authentication/mobile")
        $(".password").hide();
        login = 1;
    } else if (t === 2) {
        $(".smsCode").hide();
        $(".password").show();
        $('#smsLogin').attr("action", "/authentication/form")
        login = 2;
    }
}

function validForm() {
    let mobile = $.trim(document.forms["smsLogin"]["username"].value);
    let pass = $.trim(document.forms["smsLogin"]["password"].value);
    let smsCode = $.trim(document.forms["smsLogin"]["smsCode"].value);
    if (login === 1) {
        if (send === false) {
            showToast("请先发送验证码");
            return false;
        }
        if (checkPhone(mobile) && checkSmsCode(smsCode)) {
            return true;
        }
        return false;
    } else if (login === 2) {
        if (checkPhone(mobile) && checkPass(pass)) {
            return true;
        }
        return false;
    }
}

function validRegisterForm() {
    let mobile = $.trim($("#mobile").val());
    let pass = $.trim($("#pass").val());
    let smsCode = $.trim($("#smsCode").val());
    if (checkPhone(mobile) && checkSmsCode(smsCode) && checkPass(pass)) {
        $.ajax({
            type: "POST",
            url: "/register",
            data: {"mobile": mobile, "password": pass, "code": smsCode},
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    location.href = "/login";
                } else {
                    showToast(data.msg);
                }
            }
        });
    }
    return false;
}
