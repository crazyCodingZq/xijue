var t = 0;
var mh = 0;
var fh = 0;
var searchshow = false;
var loading = false;
var loadingAllImg = false;

function closeFeedback() {
    $('.mkfeedbackclass').remove();
}

$(document).ready(function () {


    $('#feedbackbtn').click(function () {
        var html = '<div class="mkfeedbackbg mkfeedbackclass"></div><div class="mkfeedbackwindow mkfeedbackclass"><h6>意见反馈<span onclick="closeFeedback()" class="wxClose">&times;</span></h6><iframe frameborder="0" width="100%" height="360" src="/Public/feedback/"></iframe></div>';
        $('.mkfeedbackclass').remove();
        $('body').append(html);

    });


    if ($.cookie == undefined) {

    } else {
        /*

      var searchHistoryCookie = $.cookie('searchHistory');

      if(typeof(searchHistoryCookie)!="undefined"){
          searchHistoryCookie = searchHistoryCookie.split("###");

          if(searchHistoryCookie.length>0){
              var hishisHTML = "";
              var kkk = 0;
              var hishisArray = new Array();
              for(v in searchHistoryCookie){
                  if(kkk<=8){
                      if(!isInArray(hishisArray,searchHistoryCookie[v])){
                          hishisHTML = "<a href='/search?wd="+searchHistoryCookie[v]+"'>"+searchHistoryCookie[v]+"</a>" + hishisHTML;
                          hishisArray.push(searchHistoryCookie[v]);
                          kkk++;
                      }
                  }else{
                      continue;
                  }
              }

              $('.hishis').html(hishisHTML);
          }
      }
          */

    }

    $(document).click(function (event) {
        var _con = $('.heda-searchbox');  // 设置目标区域
        if (!_con.is(event.target) && _con.has(event.target).length === 0) { // Mark 1
            //$('#divTop').slideUp('slow');  //滑动消失
            $('.heda-searchbox').removeClass('heda-searchbox-show');
            searchshow = false;
        }
    });

    $(window).scroll(function () {
        if (searchshow) {
            $('.heda-searchbox').removeClass('heda-searchbox-show');
            searchshow = false;
        }
    });

    var $imglength = $('img.ijtoo-bimg').length;
    var $loadimglength = 0;


    $("img.ijtoo-bimg").lazyload({
        effect: "fadeIn", placeholder: window['placeholder'], threshold: -200, load: function () {
            $loadimglength++;
            if ($loadimglength == $imglength) {
                loadingAllImg = true;
                srollRightFix();
                mh = $('.ijtoo-wrap-detail').height() - 512;
                fh = $('.ijtoo-slideright').height() + 16;

            }
        }
    });

    //if(window['categoryName'] == "PPT"){
    $('.category3, .subcategory81').hover(function () {
        var obj = $(this).find('.imgbox img');
        var thisHeight = parseInt(obj.height()) - 326;
        obj.stop(true, true).animate({top: "-" + thisHeight + "px"}, 10000, 'linear');
    }, function () {
        var obj = $(this).find('.imgbox img');
        obj.stop(true, true).css('top', 0);
    });
    //}

    $('.returntop').click(function () {
        $('html, body').animate({
            scrollTop: 0
        }, 500);
    });

    $('.rightslides .wx').hover(function () {
        $('.wxqrcode').stop(true, true).fadeIn(200);
    }, function () {
        $('.wxqrcode').stop(true, true).fadeOut(200);
    });

    $('.search-item').click(function (e) {
        e.stopPropagation();
        $('.heda-searchbox').toggleClass('heda-searchbox-show');
        searchshow = !searchshow;
        $('._t').focus();
        return false;
    });
    $(window).on('load resize', function () {
        var l = $('.current-nav-item').length;
        if (l <= 0)
            return false;

        var $thisnav = $('.current-nav-item').offset().left;
        $('.nav-main-item').hover(function () {
            var $left = $(this).offset().left - $thisnav;
            var $width = $(this).outerWidth();
            var $start = 0;
            $('.line').css({'left': $left + 20, 'width': $width - 40});
        }, function () {
            var $initwidth = $('.current-nav-item').width();
            $('.line').css({'left': '20px', 'width': $initwidth - 40});
        });
    });

    $('.favor-btn').click(function () {
        var that = $(this);

        $.post("/favor", "id=" + window.$goodsID, function (e) {
            if (e == 'true') {
                that.removeClass("favor_n");
                that.addClass("favor");
            }
            if (e == 'cancel') {
                that.addClass("favor_n");
                that.removeClass("favor");
            }

            if (e == 'login') {
                location.href = '/login';
                // if(window['mobile']=='true'){
                // 	location.href='/login';
                // }else{
                // 	quklogin();
                // }
            }
        });

    });


    $('.download-btn').click(function () {

        var href = $(this).attr("data-href");
        var login = $(this).attr("data-islogin");
        if (login == "0") {
            //quklogin();
            location.href = '/login';
        } else {
            location.href = href;
            return false;

            var storage = $(this).attr('data-storage');

            if (storage == 1) {

                var html = '<div class="vipdownloadsmark vipdclass"></div><div class="vipdownloads vipdclass">	<span class="close" onclick="$(\'.vipdclass\').remove()"></span>	<div class="password"><div class="passwordShow">密码：<em class="_emps">####</em></div></div>	<a href="javascript:;" class="baidu_links" target="_blank">打开百度云</a>	<p class="yw" onClick="$(\'.wangzhi\').show();">打不开网址？</p>    <p class="wangzhi">百度网盘地址：<a href="https://pan.baidu.com/" target="_blank">https://pan.baidu.com/</a><br>请复制到浏览器里打开</p></div>';
                $.post("/gd", "_s=" + window.$sessionRead, function (e) {

                    var data = e.x;
                    var state = e.state;

                    if (state == 101) {
                        quklogin();
                    } else if (state == 400) {
                        location.href = '/u-vip';
                    } else if (state == 501) {
                        alert("您当前会员下载次数已达上限！");
                    } else if (state == 502) {
                        alert("抱歉，会员类型不识别！");
                    } else if (state == 200) {

                        d = $.trim(b64DecodeUnicode(data));

                        d = d.replace("@i1j2t3o4o5!", "");
                        d = "https://pan.baidu.com" + d;

                        d = d.split("$$#$!");
                        a = d[0];
                        p = d[1].replace("///", "");
                        $('.vipdclass').remove();
                        $('body').append(html);
                        $('.passwordShow').hide();

                        $('._emps').text(p);
                        $('.passwordShow').fadeIn(200);
                        $('.baidu_links').attr('href', a);

                    }
                }, "json");

            } else {
                location.href = href;
            }
        }
    });

    if (window['loginWindow'] == '1') {
        $('.userlogin-f').attr("href", "javascript:;").click(function () {
            qklogins();
        });
    }

    $('#username').keyup(function () {
        var username = $.trim($('#username').val());
        var mobileCheck = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;

        if (username.length == 11) {
            if (!mobileCheck.test(username)) {
                $('#username-error').html("手机号码有误").show();
            } else {
                $('#password').focus();
                $('#username-error').hide();
            }
        }

    });
    $("#loginform").validate({
        rules: {
            username: "required",
            password: "required"
        },
        messages: {
            username: "请输入手机号",
            password: "请输入密码",
        },
        submitHandler: function (form) {

            $(form).ajaxSubmit({
                type: 'post',
                dataType: "json",
                url: "/?a=verifyLoginAccount&token=1",
                beforeSubmit: function (formData, jqForm, options) {
                    var username = $.trim($('#username').val());

                    var mobileCheck = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;

                    if (!mobileCheck.test(username)) {
                        $('#username-error').html("手机号码有误").show();
                        return false;
                    } else {
                        $('#username-error').hide();
                    }
                    loadingLogin(true);
                },
                success: function (data, status) {

                    loadingLogin(false);
                    if (data.state == 1) {
                        $('#password-error').hide();
                        location.reload();
                    } else if (data.state == 1001) {
                        $('#password-error').html(data.message).show();

                    }

                }
            });
            return false;
        }
    });
});

function closeMemberlogin() {
    $('.qkloginclass').hide();
}

function qklogins() {
    $('#username,#password').val('');
    $('.qkloginclass').show();
    $('#username').focus();
}

function loadingLogin(f) {
    if (f) {
        $('#username,#password,#loginbtn').attr("readonly", "readonly");
        $('#loginbtn').html("登 录 中..").addClass("loingloadings").attr("disabled", "disabled");
    } else {
        $('#username,#password,#loginbtn').removeAttr("readonly");
        $('#loginbtn').html("登 录").removeClass("loingloadings").removeAttr("disabled");
    }
}

if (document.getElementById('ijtoo-detailR')) {
    t = $('.ijtoo-slideright').offset().top;
}
$(window).scroll(function () {
    if (loadingAllImg) {
        srollRightFix();
    }
});

function b64DecodeUnicode(str) {
    return decodeURIComponent(atob(str).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
}

function srollRightFix() {

    var s = $(document).scrollTop();

    if (s > t - 85) {
        $('.ijtoo-slideright').css('position', 'fixed').css('top', '85px').css('margin-left', '30px');
        if (s + fh + 50 > mh) {
            $('.ijtoo-slideright').css('top', (mh - s - fh + 30) + 'px');
        }
    } else {
        $('.ijtoo-slideright').css('position', '');
    }

}

var tipTimeObj;

function tips(f) {
    clearTimeout(tipTimeObj);
    if (f) {
        var html = '<div class="ijtoo-public-messagetips ijtoo-success">保存成功</div>';
    } else {
        var html = '<div class="ijtoo-public-messagetips ijtoo-mwaings" style="display:none"><div class="timer"></div> 正在保存...</div>';
    }

    $('body').append(html);

    $('.ijtoo-public-messagetips').stop(true).stop(true).fadeOut(500);
    if (f) {
        $('.ijtoo-success').stop(true).stop(true).fadeIn(500);

        tipTimeObj = setTimeout(function () {
            $('.ijtoo-public-messagetips').stop(true).stop(true).fadeOut(500);
        }, 2000);

    } else {
        $('.ijtoo-mwaings').stop(true).stop(true).fadeIn(500);
    }


}

function searchBox() {
    var search_wd = $('#search_wd').val();
    search_wd = $.trim(search_wd);
    if (search_wd != "") {
        var ck = $.cookie('searchHistory');
        var k = "";
        if (typeof (ck) == "undefined") {
            k = search_wd;
        } else {
            k = ck + "###" + search_wd;
        }

        $.cookie('searchHistory', k, {expires: 90, path: '/'});

    } else {
        return false;
    }
}

function searchBox2(obj) {
    var search_wd = $(obj).find('.searchtext').val();
    search_wd = $.trim(search_wd);
    if (search_wd != "") {
        var ck = $.cookie('searchHistory');
        var k = "";
        if (typeof (ck) == "undefined") {
            k = search_wd;
        } else {
            k = ck + "###" + search_wd;
        }

        $.cookie('searchHistory', k, {expires: 90, path: '/'});

    } else {
        return false;
    }
}

function quklogin() {
    qklogins();
}

var goodsObj = {

    isfavor: function () {
        window.$goodsID
        $.post("/favor", "check=1&id=" + window.$goodsID, function (e) {
            if (e == 'true') {
                that.removeClass("favor_n");
                that.addClass("favor");
            }
            if (e == 'cancel') {
                that.addClass("favor_n");
                that.removeClass("favor");
            }
        });
    }
}

function isInArray(arr, value) {
    for (var i = 0; i < arr.length; i++) {
        if (value === arr[i]) {
            return true;
        }
    }
    return false;
}


jQuery.fn.selectFilter = function (options) {
    var defaults = {
        callBack: function (res) {
        }
    };
    $(this).each(function () {
        var ops = $.extend({}, defaults, options);
        var selectList = $(this).find('select option');
        var _in = $(this).find('select').attr('data-in');
        var _for = $(this).find('select').attr('data-for');
        var that = this;
        var html = '';
        var nextClick = $(this).attr('next-click') ? $(this).attr('next-click') : 'false';
        if ($(that).find('.filter-list').length > 0) {
            $(that).find('.filter-list').remove();
        }
        // 读取select 标签的值
        html += '<ul class="filter-list" data-in="' + _in + '" data-for="' + _for + '">';

        $(selectList).each(function (idx, item) {
            var val = $(item).val();
            var valText = $(item).html();
            var selected = $(item).attr('selected');
            var disabled = $(item).attr('disabled');
            var _id = $(item).attr('data-id');
            var _upid = $(item).attr('data-upid');
            var isSelected = selected ? 'filter-selected' : '';
            var isDisabled = disabled ? 'filter-disabled' : '';
            if (selected) {
                html += '<li class="' + isSelected + '" data-value="' + val + '" data-id="' + _id + '" data-upid="' + _upid + '"><a title="' + valText + '">' + valText + '</a></li>';
                $(that).find('.filter-title').val(valText);
                iffirst = false;
            } else if (disabled) {
                html += '<li class="' + isDisabled + '" data-value="' + val + '" data-id="' + _id + '" data-upid="' + _upid + '"><a>' + valText + '</a></li>';
            } else {
                html += '<li data-value="' + val + '" data-id="' + _id + '" data-upid="' + _upid + '"><a title="' + valText + '">' + valText + '</a></li>';
            }
            ;
        });

        html += '</ul>';
        $(that).append(html);
        $(that).find('select').hide();

        //点击选择
        $(that).off('click', '.filter-text');
        $(that).on('click', '.filter-text', function () {
            $(that).find('.filter-list').slideToggle(100);
            $(that).find('.filter-list').toggleClass('filter-open');
            $(that).find('.icon-filter-arrow').toggleClass('filter-show');
        });

        //点击选择列表
        $(that).find('.filter-list li').not('.filter-disabled').off('click');
        $(that).find('.filter-list li').not('.filter-disabled').on('click', function () {
            var val = $(this).attr('data-value');
            var _id = $(this).attr('data-id');
            var _upid = $(this).attr('data-upid');
            var valText = $(this).find('a').html();
            var this_in = $(this).parents('.filter-list').attr('data-in');
            var this_for = $(this).parents('.filter-list').attr('data-for');
            if (this_for) {
                $('.filter-list[data-for="' + this_in + '"]').find('li').hide();
                if (_id != "undefined") {
                    $('.filter-list[data-for="' + this_in + '"]').find('li[data-upid="' + _id + '"]').show();
                    if (nextClick != 'false') {
                        $('.filter-list[data-for="' + this_in + '"]').find('li[data-upid="' + _id + '"]').eq(0).click();
                        $('.filter-list[data-for="' + this_in + '"]').removeClass('filter-open').hide().parents('.filter-box').find('.icon').removeClass('filter-show');
                    }
                } else {
                    $('.filter-list[data-for="' + this_in + '"]').find('li').show();
                }
            }
            $(that).find('.filter-title').val(valText).attr('title', valText);
            $(that).find('.icon-filter-arrow').toggleClass('filter-show');
            $(this).addClass('filter-selected').siblings().removeClass('filter-selected');
            $(this).parent().slideToggle(50);
            for (var i = 0; i < selectList.length; i++) {
                var selectVal = selectList.eq(i).val();
                selectList.eq(i).prop('selected', 'false');
                selectList.eq(i).removeAttr('selected');
                if (val == selectVal) {
                    selectList.eq(i).prop('selected', 'true');
                    selectList.eq(i).attr('selected', '1');
                    $(that).find('select').val(val);
                }
                ;
            }
            ;
            ops.callBack(val); //返回值
        });

        //其他元素被点击则收起选择
        $(document).on('mousedown', function (e) {
            closeSelect(that, e);
        });
        $(document).on('touchstart', function (e) {
            closeSelect(that, e);
        });

        function closeSelect(that, e) {
            var filter = $(that).find('.filter-list'),
                filterEl = $(that).find('.filter-list')[0];
            var filterBoxEl = $(that)[0];
            var target = e.target;
            if (filterEl !== target && !$.contains(filterEl, target) && !$.contains(filterBoxEl, target)) {
                filter.slideUp(50);
                $(that).find('.filter-list').removeClass('filter-open');
                $(that).find('.icon-filter-arrow').removeClass('filter-show');
            }
            ;
        }
    });
};


$('.filter-box').selectFilter({
    callBack: function (val) {
    }
});

function timest() {
    $(".animated-circles").toggleClass("animated");
};
setInterval('timest();', 3000);
$(".index_jbxx .tx").click(function () {
    $(".xxzl").toggleClass("on");
});

$(".index_jbxx .title span").click(function () {
    $(".xxzl").removeClass("on");

});