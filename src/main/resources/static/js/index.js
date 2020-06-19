var swiper = new Swiper('.swiper-container', {
    pagination: '.swiper-pagination',
    paginationClickable: '.swiper-pagination',
    nextButton: '.swiper-button-next',
    prevButton: '.swiper-button-prev',
    spaceBetween: 0,
    effect: 'fade',
    autoplay: 4000,
    speed: 1200,
    autoplayDisableOnInteraction: false,
    loop: true,
});

var swiper = new Swiper('.ind_ggtu .swiper-containers', {
    pagination: '.swiper-paginations',
    paginationClickable: '.swiper-paginations',
    nextButton: '.swiper-button-nexts',
    prevButton: '.swiper-button-prevs',
    slidesPerView: 4,
    paginationClickable: true,
    spaceBetween: 20,
    freeMode: true
});

//定时器返回值
var time = null;
//记录当前位子
var nexImg = 0;
//用于获取轮播图图片个数
var imgLength = $(".c-banner .banner ul li").length;
//当时动态数据的时候使用,上面那个删除
// var imgLength =0;
//设置底部第一个按钮样式
$(".c-banner .jumpBtn ul li[jumpImg=" + nexImg + "]").css("background-color", "black");

//页面加载
$(document).ready(function () {
    // dynamicData();
    //启动定时器,设置时间为3秒一次
    time = setInterval(intervalImg, 4000);
});

//点击上一张
$(".preImg").click(function () {
    //清楚定时器
    clearInterval(time);
    var nowImg = nexImg;
    nexImg = nexImg - 1;
    console.log(nexImg);
    if (nexImg < 0) {
        nexImg = imgLength - 1;
    }
    //底部按钮样式设置
    $(".c-banner .jumpBtn ul li").css("background-color", "white");
    $(".c-banner .jumpBtn ul li[jumpImg=" + nexImg + "]").css("background-color", "black");

    //将当前图片试用绝对定位,下一张图片试用相对定位
    $(".c-banner .banner ul img").eq(nowImg).css("position", "absolute");
    $(".c-banner .banner ul img").eq(nexImg).css("position", "relative");

    //轮播淡入淡出
    $(".c-banner .banner ul li").eq(nexImg).css("display", "block");
    $(".c-banner .banner ul li").eq(nexImg).stop().animate({"opacity": 1}, 1000);
    $(".c-banner .banner ul li").eq(nowImg).stop().animate({"opacity": 0}, 1000, function () {
        $(".c-banner ul li").eq(nowImg).css("display", "none");
    });

    //启动定时器,设置时间为3秒一次
    time = setInterval(intervalImg, 4000);
})

//点击下一张
$(".nexImg").click(function () {
    clearInterval(time);
    intervalImg();
    time = setInterval(intervalImg, 4000);
})

//轮播图
function intervalImg() {
    if (nexImg < imgLength - 1) {
        nexImg++;
    } else {
        nexImg = 0;
    }
    //将当前图片试用绝对定位,下一张图片试用相对定位
    $(".c-banner .banner ul img").eq(nexImg - 1).css("position", "absolute");
    $(".c-banner .banner ul img").eq(nexImg).css("position", "relative");

    $(".c-banner .banner ul li").eq(nexImg).css("display", "block");
    $(".c-banner .banner ul li").eq(nexImg).stop().animate({"opacity": 1}, 1000);
    $(".c-banner .banner ul li").eq(nexImg - 1).stop().animate({"opacity": 0}, 1000, function () {
        $(".c-banner .banner ul li").eq(nexImg - 1).css("display", "none");
    });
    $(".c-banner .jumpBtn ul li").css("background-color", "white");
    $(".c-banner .jumpBtn ul li[jumpImg=" + nexImg + "]").css("background-color", "black");
}

//轮播图底下按钮
//动态数据加载的试用应放在请求成功后执行该代码,否则按钮无法使用
$(".c-banner .jumpBtn ul li").each(function () {
    //为每个按钮定义点击事件
    $(this).click(function () {
        clearInterval(time);
        $(".c-banner .jumpBtn ul li").css("background-color", "white");
        jumpImg = $(this).attr("jumpImg");
        if (jumpImg != nexImg) {
            var after = $(".c-banner .banner ul li").eq(jumpImg);
            var befor = $(".c-banner .banner ul li").eq(nexImg);

            //将当前图片试用绝对定位,下一张图片试用相对定位
            $(".c-banner .banner ul img").eq(nexImg).css("position", "absolute");
            $(".c-banner .banner ul img").eq(jumpImg).css("position", "relative");

            after.css("display", "block");
            after.stop().animate({"opacity": 1}, 2000);
            befor.stop().animate({"opacity": 0}, 2000, function () {
                befor.css("display", "none");
            });
            nexImg = jumpImg;
        }
        $(this).css("background-color", "black");
        time = setInterval(intervalImg, 4000);
    });
});

$(document).ready(function () {
    $('.navs li').hover(function () {
        var dirname = $(this).find("a").attr("data-dirname");
        $(this).parents(".w1380").find('ul.work-item').hide();
        var i = $(this).parent().find("li").index(this);
        $(this).parents(".w1380").find('ul.work-item').eq(i).show();
        $(this).parent().find("li").removeClass("more");
        $(this).addClass("more");
        $('.moret a').attr("href", dirname);
    });
    $('.navs').each(function (index, element) {
        $(this).find("li").eq(0).click();
    });
    $('.moret a').attr("href", $('.navs li').eq(0).find("a").attr("data-dirname"));
    $.ajax({
        type: "POST",
        url: "/category/queryCategoryList",
        dataType: "json",
        success: function (data) {
            if (data && data.success && data.data.length > 0) {
                var html = '';
                $.each(data.data, function (index, item) {
                    if (item.parentId == 0) {
                        html += '<li class="nav_ico_' + (index + 1) + '"><a href="/queryChildren/' + item.id + '">' + item.categoryName + '<span></span></a></li>'
                    }
                });
            }
            //$('.banner_box .nav_menu ul').html(html);
        }
    });
});

$(".index_jbxx .tx").click(function () {
    $(".xxzl").toggleClass("on");
});

$(".index_jbxx .title span").click(function () {
    $(".xxzl").removeClass("on");

});

$.fn.fixedDiv = function (actCls) {
    var that = $(this),
        offsetTop = that.offset().top,
        scrollTop;

    function fix() {
        scrollTop = $(document).scrollTop();

        if (scrollTop > 111) {
            that.addClass(actCls);
        } else {
            that.removeClass(actCls);
        }
    }

    fix();
    $(window).scroll(fix);
}
$('.header').fixedDiv('fix-div');

$.fn.extend({
    Scroll: function (opt, callback) {
        //参数初始化
        if (!opt) var opt = {};
        var _this = this.eq(0).find("ul:first");
        var lineH = _this.find("li:first").height(), //获取行高
            line = opt.line ? parseInt(opt.line, 10) : parseInt(this.height() / lineH, 1), //每次滚动的行数，默认为一屏，即父容器高度
            speed = opt.speed ? parseInt(opt.speed, 10) : 800, //卷动速度，数值越大，速度越慢（毫秒）
            timer = opt.timer ? parseInt(opt.timer, 10) : 3000; //滚动的时间间隔（毫秒）

        if (line == 0) line = 1;
        var upHeight = 0 - line * lineH;
        //滚动函数
        scrollUp = function () {
            _this.animate({
                marginTop: upHeight
            }, speed, function () {
                for (i = 1; i <= line; i++) {
                    _this.find("li:first").appendTo(_this);
                }
                _this.css({
                    marginTop: 0
                });
            });
        }
        //鼠标事件绑定
        _this.hover(function () {
            clearInterval(timerID);
        }, function () {
            timerID = setInterval("scrollUp()", timer);
        }).mouseout();
    }
});
$(function () {
    $('#scrollDiv ').Scroll({
        line: 1,
        speed: 800,
        timer: 3000
    });
});

$(".navs ul li").each(function () {
    var hover_a = $(this);
    var hover_awidth = $(this).innerWidth();
    var hover_aleft = hover_a.position().left;
    $(this).mouseover(function () {
        $(".bg").stop();
        hover_awidth = $(this).innerWidth();
        $(".bg").animate({
            left: hover_aleft + "px",
            width: hover_awidth + "px"
        }, 300);
        $(".ijtoo-block3 .navs li.on").removeClass("gl_hover");
    })
    $(this).mouseout(function () {
        $(".bg").stop();
        if ($(this).parent().find(".on").length > 0) {
            var gl_hover_left = $(this).parent().find(".on").position().left;
            hover_awidth = $(this).parent().find(".on").innerWidth();
            $(".bg").animate({
                left: gl_hover_left + "px",
                width: hover_awidth + "px"
            }, 300);
            $(".ijtoo-block3 .navs li.on").addClass("gl_hover");
        } else {
            hover_awidth = $(".nav ul a:first").innerWidth();
            $(".bg").animate({
                left: "1px",
                width: hover_awidth + "px"
            }, 300);

        }
    })
})
$(".navs ul li").hover(function () {
    $(this).siblings().removeClass("on");
    $(this).siblings().removeClass("gl_hover");
    $(this).addClass("on");
})

$(function () {
    $(".ijtoo-block3 .navs li:first-of-type").addClass("gl_hover");
    $(".ijtoo-recommendblock ul:first-of-type").show();
});
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
        $(that).on('click', '.filter-text', function () {
            $(that).find('.filter-list').slideToggle(100);
            $(that).find('.filter-list').toggleClass('filter-open');
            $(that).find('.icon-filter-arrow').toggleClass('filter-show');
        });

        //点击选择列表
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
                } else {
                    $('.filter-list[data-for="' + this_in + '"]').find('li').show();
                }
            }
            $(that).find('.filter-title').val(valText);
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
setInterval('timest();', 3000)

$(function () {
    var numLi = $("#scrollDivs  a").length;
    // console.log(numLi);
    for (var i = 0; i < numLi; i++) {
        $("#scrollDivs a").eq(i).prepend(+(i + 1) + '，');
    }
})
