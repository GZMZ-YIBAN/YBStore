function errorStatus(status) {
    if (status === 401) {
        var currentURL = window.location;
        window.location.href = currentURL.origin + currentURL.pathname + "login.html";
    } else if (status === 500 || status === 502 || status === 503) {
        alert("啊哦，可能出了点小错误，请刷新尝试~");
        var currentURL = window.location;
        window.location.href = currentURL.origin + currentURL.pathname + "login.html";
    } else if (status === 611) {
        console.error("通过当前用户获取已购买的订单,获取用户的id失败");
    } else if (status === 501) {
        console.error("前台验证被跳过或修改，参数错误，请勿擅自修改参数");
    } else if (status === 600) {
        console.error("前台验证被跳过或修改，网薪不足！小哥哥你很皮。");
    } else if (status === 601) {
        console.error("前台验证被跳过或修改，未知商品id！别再皮了=-=");
    } else if (status === 602) {
        console.error("商品获取出现异常");
    } else if (status === 603) {
        console.error("前台验证被跳过或修改，商品数目不足,你想干嘛啊=-=");
    } else if (status === 604) {
        console.error("前台验证被跳过或修改，手机号格式错误，老铁你要干嘛！");
    } else if (status === 605) {
        console.error("前台验证被跳过或修改，同学别玩儿了好不，，，");
    } else if (status === 606) {
        console.error("网薪支付链接生成失败");
    } else if (status === 510) {
        console.error("严重错误！支付成功但存入出现异常！！");
        alert("严重错误！支付成功但存入出现异常！！请截图并联系易班管理员！")
    }
}

function submitUrl(toData, url, callback, async) {
    $.ajax({
        type: "post",
        url: url,
        data: toData,
        dataType: "json",
        async: async,
        success: function (data) {
            callback(data);
        },
        error: function (data) {
            errorStatus(data.status);
        }
    });
}


(function ($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
})(jQuery);

var verify_request = {
    'verify_request': $.getUrlParam('verify_request')
};
var products = null;
var user = null;
var userList = null;
$(function () {
    if (verify_request.verify_request == null || verify_request.verify_request === ""){
        window.location.href="https://oauth.yiban.cn/code/html?client_id=c3ae2d27d5fe89b5&" +
            "redirect_uri=http://f.yiban.cn/iapp213531&state=YBStore";
    }
    $('.load-bg').removeClass("disNo");
    $('#home').click();
    box.css("display", "none");
    setTimeout(function () {
        $('.load-bg').detach();
        $('#home').click();
    }, 3000);
});

$('.navLink').click(function () {
    $('.navLink').removeClass("active");
    $(this).addClass("active");
});

var app1 = $('.app1');
var app2 = $('.app2');
var app3 = $('.app3');
var box = $('.box');
var nowMoney = null;
var needMoey = null;
var nowP = null;
var nowphoto = null;
$('#home').click(function () {
    box.css("display", "none");
    box.css("padding-top", "0.5rem");
    app3 = app3.detach();
    app2 = app2.detach();
    app1 = $('.app1');
    var url = "/YBStore/queryList";
    if (products == null) {
        submitUrl(verify_request, url, function (data) {
            products = $.parseJSON(data.result);
            user = $.parseJSON(data.user);
        }, false);
    }
    if (app1.length === 0) {
        $('.box').prepend("<div class='app1'></div>");
        app1 = $('.app1');
        app1.append("<img src='img/title.jpg' class='img-responsive'>")
        app1.append("<div class='title'><span>商品推荐</span></div>");
        $(products).each(function (i, product) {
            if (product.enable === 1)
                app1.append("        <div  class='pro overflow-h'>\n" +
                    "           <div class='fl relative overflow-h'>\n" +
                    "              <img class='proImg' src='" + product.photo + "' alt='" + product.photo + "'>\n" +
                    "           </div>\n" +
                    "           <div class='price'>\n" +
                    "               <span class='num'>" + product.price + "</span>\n" +
                    "               <em class='dw'>网薪</em>\n" +
                    "           </div>\n" +
                    "           <div class='proNum'>\n" +
                    "               剩余:" + product.number + "\n" +
                    "           </div>" +
                    "           <div class='ds'>\n" +
                    "               <p class='text-overflow-l pro-title-1'>" + product.name + "</p>\n" +
                    "               <p class='text-overflow-l pro-title-2'>" + product.describe + "</p>\n" +
                    "           </div>\n" +
                    "           <div class='pId' style='display: none;'>" + product.id + "</div>" +
                    "        </div>");
        });
    } else {
        $('.box').prepend(app1);
    }
    box.fadeIn("normal");
});

$('#get').click(function () {
    box.css("display", "none");
    box.css("padding-top", "0.5rem");
    var url = "/YBStore/userList";
    app1 = app1.detach();
    app3 = app3.detach();
    app2 = $('.app2');
    if (userList == null) {
        submitUrl(verify_request, url, function (data) {
            userList = $.parseJSON(data.productUserList);
        }, false);
    }
    if (app2.length === 0) {
        box.prepend("<div class='app2'></div>");
        app2 = $('.app2');
        app2.append("           <div class='title-1'>\n" +
            "               <p>您已成功兑换的商品</p>\n" +
            "           </div>");
        $(userList).each(function (i, product) {
            var enable = product.enable > 0 ? "已兑换" : "未兑换";
            app2.append("           <div class='pl overflow-h'>\n" +
                "               <div class='fl pic'>\n" +
                "                   <img src='" + product.img + "' alt=''>\n" +
                "               </div>\n" +
                "               <div class='des'>\n" +
                "                  <div>\n" +
                "                    <p class='title-2'>" + product.productName + "</p>\n" +
                "                    <p class='title-3'>状态: " + enable + "</p>\n" +
                "                   </div>\n" +
                "                   <div class='tip' pId='" + product.id + "'>点我获取二维码" +
                "                   </div>\n" +
                "               </div>" +
                "           </div>");
        });

    } else {
        $('.box').prepend(app2);
    }
    box.fadeIn("normal");
});

$('#user').click(function () {
    box.css("display", "none");
    box.css("padding-top", "0.0rem");
    app1 = app1.detach();
    app2 = app2.detach();
    var url = "/YBStore/queryList";
    if (user == null) {
        submitUrl(verify_request, url, function (data) {
            products = $.parseJSON(data.result);
            user = $.parseJSON(data.user);
        }, false);
    }
    if (app3.length === 0) {
        box.prepend("<div class='app3'></div>");
        app3 = $('.app3');
        var sex = user.yb_sex === "M" ? "帅哥" : "美女";
        app3.append(" <div class='info'>\n" +
            "            <div class='info-main'>\n" +
            "                <div class='head'>\n" +
            "                    <img src=" + user.yb_userhead + " alt=''>\n" +
            "                </div>\n" +
            "                <div class='info-body-col'>\n" +
            "                    <span>昵称:</span>\n" +
            "                    <span>" + user.yb_usernick + "</span>\n" +
            "                </div>\n" +
            "                <div class='info-body-col'>\n" +
            "                    <span>用户id:</span>\n" +
            "                    <span>" + user.yb_userid + "</span>\n" +
            "                </div>\n" +
            "                <div class='info-body-col'>\n" +
            "                    <span>属性:</span>\n" +
            "                    <span>" + sex + "</span>\n" +
            "                </div>\n" +
            "                <div class='info-body-col'>\n" +
            "                    <span>当前网薪:</span>\n" +
            "                    <span>" + user.yb_money + "</span>\n" +
            "                </div>\n" +
            "                <div class='info-body-col'>\n" +
            "                    <span>当前经验:</span>\n" +
            "                    <span>" + user.yb_exp + "</span>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "            <div id='qun'>易班交流群：121535</div>" +
            "        </div>")
    } else {
        $('.box').prepend(app3);
    }
    box.fadeIn("normal");
});


box.on("click", ".pro", function (e) {
    var now = $(this);
    needMoey = now.find(".num")[0].innerHTML;
    nowMoney = user.yb_money;
    nowP = now.find(".pId")[0].innerHTML;
    nowphoto = now.find(".proImg")[0].alt;
    box.before("<div class='filter disNo'></div>");
    box.before("<div class='flexCen flexCol ask disNo'>\n" +
        "        <div class='flexCen ask-head '>\n" +
        "            提示信息\n" +
        "        </div>\n" +
        "        <div class='flexCen big'>\n" +
        "            <a class='bigBtn' href='" + nowphoto + "'>\n" +
        "                →点我查看大图←\n" +
        "            </a>\n" +
        "        </div>" +
        "        <div class='flexCen flexCol flexBwn ask-body'>\n" +
        "            <div class='ask-row'>\n" +
        "                <span class='ask-row-l'>\n" +
        "                    兑换名称：\n" +
        "                    </span>\n" +
        "                <span class='ask-row-r'>\n" +
        "                    " + now.find(".pro-title-1")[0].innerHTML + "\n" +
        "                    </span>\n" +
        "            </div>\n" +
        "            <div class='ask-row'>\n" +
        "                <span class='ask-row-l'>\n" +
        "                    所需网薪：\n" +
        "                    </span>\n" +
        "                <span class='ask-row-r'>\n" +
        "                    " + now.find(".num")[0].innerHTML + "\n" +
        "                    </span>\n" +
        "            </div>\n" +
        "            <div class='ask-row'>\n" +
        "                <span class='ask-row-l'>\n" +
        "                    当前网薪：\n" +
        "                    </span>\n" +
        "                <span class='ask-row-r'>\n" +
        "                    " + user.yb_money + "\n" +
        "                    </span>\n" +
        "            </div>\n" +
        "            <div class='ask-row'>\n" +
        "                <p id='phoneTip'>请输入联系电话</p>\n" +
        "            </div>\n" +
        "            <div class='ask-row'>\n" +
        "                <input id='phone' type='phone'>\n" +
        "            </div>" +
        "            <div class='flexCen flexBwn ask-choose'>\n" +
        "                <div class='flexCen ask-sub'>\n" +
        "                    确认兑换\n" +
        "                </div>\n" +
        "                <div class='flexCen ask-exit'>\n" +
        "                    不兑换了\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>");
    $('.filter').show("fast");
    $('.ask').show("normal");
});

var body = $("body");
body.on("click", ".ask-sub", function (e) {
    needMoey = parseInt(needMoey);
    nowMoney = parseInt(nowMoney);
    nowP = parseInt(nowP);
    var phone = $('#phone').val().trim();
    if (needMoey == null || nowMoney == null) {
        alert("未获取到网薪！请刷新重试。。。");
        return false;
    }
    if (nowP == null) {
        alert("对不起，，，未获取到当前商品信息。。。");
        return false;
    }
    if (phone.length !== 11) {
        alert("emmmm，请确认您的联系电话是否正确。。。请输入11位手机号");
        return false;
    }
    if (needMoey >= nowMoney) {
        alert("sorry,,,你的网薪不够诶。。。");
        return false;
    }
    var url = "/YBStore/pay";
    var toData = {
        "needMoney": needMoey,
        "nowMoney": nowMoney,
        "phone": phone,
        "pId": nowP
    };
    var flag = confirm("您确定兑换吗？所需网薪" + needMoey + "，当前网薪" + nowMoney);
    if (flag) {
        submitUrl(toData,url,function (data) {
            var json = $.parseJSON(data.subRes);
            window.location.replace(json.url);
        },true);
        alert("请稍后，正在跳转至订单页面，订单支付过程中请勿关闭浏览器。");
    }
});

body.on("click", ".ask-exit", function (e) {
    needMoey = null;
    nowMoney = null;
    nowP = null;
    $('.filter').fadeOut("normal");
    $('.ask').fadeOut("normal");
    setTimeout(function () {
        $('.filter').detach();
        $('.ask').detach();
    }, 500);
});

body.on("click", "#phoneTip", function () {
    $('#phoneTip').animate({"top": "-.2rem", "font-size": ".3rem", "color": "rgb(85,26,139)"});
});

body.on("focusin", "#phone", function () {
    $('#phoneTip').animate({"top": "-.2rem", "font-size": ".3rem", "color": "rgb(85,26,139)"});
});
body.on("focusout", "#phone", function () {
    if ($('#phone').val().trim() === "") {
        $('#phoneTip').animate({"top": ".3rem", "font-size": ".4rem"});
    }
});
body.on("click", ".tip", function () {
    var rand1 = Math.floor(255 * Math.random()).toString(16) + Math.floor(255 * Math.random()).toString(16) + Math.floor(255 * Math.random()).toString(16);
    var rand2 = Math.floor(255 * Math.random()).toString(16) + Math.floor(255 * Math.random()).toString(16) + Math.floor(255 * Math.random()).toString(16);
    var rand3 = Math.floor(255 * Math.random()).toString(16) + Math.floor(255 * Math.random()).toString(16) + Math.floor(255 * Math.random()).toString(16);
    var rand4 = Math.floor(255 * Math.random()).toString(16) + Math.floor(255 * Math.random()).toString(16) + Math.floor(255 * Math.random()).toString(16);
    var url = "http://qr.liantu.com/api.php?" + "fg=" + rand1 + "&gc=" + rand2 + "&pt=" +
        rand3 + "&inpt=" + rand4 + "&el=h&text=" + "http://yiban.echocow.cn/YBStore/info.html?pId=" + $(this).attr("pId");
    body.append("    <div class='flexCen flexCol er disNo'>\n" +
        "        <div class='er-pic'>\n" +
        "            <img src='" + url + "' alt='二维码'>\n" +
        "        </div>\n" +
        "        <div class='flexCen er-close'>\n" +
        "            关 闭\n" +
        "        </div>\n" +
        "    </div>");
    body.on("click", ".er-close", function () {
        $('.er').fadeOut("slow");
        setTimeout(function () {
            $('.er').remove();
        }, 500);
    });

});
