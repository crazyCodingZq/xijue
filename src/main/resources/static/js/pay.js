var tq;
var defaultPrice = window['defaultPrice'];
var jc = "";
var chosePay = true;
var chosePayfunc = "";

function isWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == 'micromessenger' || ua.match(/_SQ_/i) == '_sq_') {
        return true;
    } else {
        return false;
    }
}

$(function () {
    $('#priceshowtext').html(defaultPrice);

    $('.wxClose').click(function () {
        $('.wxpayclass').fadeOut(100);
    });

    $('.listitems').click(function () {
        $(".listitems").removeClass("on");
        $(this).addClass("on");
        var price = $(this).find("strong").text();
        price = parseInt(price);
        defaultPrice = price;
        $('#priceshowtext').text(defaultPrice);
        $('.yhjtips').html('').hide();
        $('.yhjtips').removeAttr('readonly');
    });

    if (window['defaultItem'] == 3) {
        if (isWeiXin()) {
            $('#paypalWapBtnPay').attr("class", "btnpayn");
        }

        $('.cpay').click(function () {
            chosePay = true;
            $('#' + chosePayfunc).click();

        });

        $('.btnpay').click(function () {
            if (chosePay) {
                if (defaultPrice > 0) {
                    let id = $(this).attr("id");
                    if (id == "paypalBtnPay") {
                        id = "Alipay";
                    }
                    if (id == "wxpayBtnPay") {
                        id = "wePay";
                    }
                    $.ajax({
                        type: "POST",
                        url: "/order/createOrder",
                        data: {"payType": id, "amount": defaultPrice},
                        dataType: "json",
                        success: function (data) {
                            if (data.success) {
                                if (id == "Alipay") {
                                    location.href = "/alipay/createOrder?orderNo=" + data.data;
                                } else if (id == "wePay") {
                                    wepay(data.data);
                                }

                            }
                        },
                        error: function (e) {
                            console.dir(e)
                            location.href = "/sso";
                        }
                    });
                }
            } else {
                var id = $(this).attr("id");
                chosePayfunc = id;
                $('.btnpay').removeClass("on");
                $(this).addClass("on");
            }
        });
        if (isWeiXin()) {
            $('#wxH5payBtnPay').click();
        }
    }
});

function wepay(orderNo) {
    $.ajax({
        type: "POST",
        url: "/wxPay/unifiedOrder",
        data: {"orderCode": orderNo},
        dataType: "json",
        success: function (data) {
            console.dir(data);
            $.getScript("/static/js/qrcode.js", function () {
                if (jc == "") {
                    $('#pricewx').text(defaultPrice);
                } else {
                    $('#pricewx').text($('#priceshowtext').text());
                }
                $('#wxPayQrcode').html('');
                var url = data.data;
                //参数1表示图像大小，取值范围1-10；参数2表示质量，取值范围'L','M','Q','H'
                var qr = qrcode(10, 'H');
                qr.addData(url);
                qr.make();
                var code = document.createElement('DIV');
                code.innerHTML = qr.createImgTag();
                var element = document.getElementById("wxPayQrcode");
                element.appendChild(code);
                $('.paymkWind').removeClass('wxpayclass');
                $('.paymkbg').removeClass('wxpayclass');
                $('.paymkWind').addClass('paymkWindshow');
                $('#wxPayQrcode img').fadeIn(1000);
                $('.paymkWind p').addClass("s");

                tq = setInterval(function () {
                    $.post("/order/queryOrderStatus", "id=" + orderNo, function (data) {
                        console.dir(data)
                        if (data == 1) {
                            clearInterval(tq);
                            $('.paymkWind p.s').html("<strong>支付成功</strong><br>即将刷新页面");
                            setTimeout(function () {
                                location.reload();
                            }, 2000);
                        }
                    });
                }, 1000);

            })
        }
    });
}


