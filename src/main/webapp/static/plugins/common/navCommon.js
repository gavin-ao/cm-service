/**
 * Created by 12045 on 2018/10/22.
 */

(function ($) {

    navSelect();
    $($("#main-menu").find("li")[0]).trigger("click");
    console.log($("#main-menu").find("li")[0])

}(jQuery));

// 提示信息弹窗
(function () {
    $.MsgBox = {
        Alert: function (title, msg) {
            GenerateHtml("alert", title, msg);
            btnOk();  //alert只是弹出消息，因此没必要用到回调函数callback
            btnNo();
        },
        Confirm: function (title, msg, callback) {

            GenerateHtml("confirm", title, msg);
            btnOk(callback);
            btnNo();
        }
    }
    //生成Html
    var GenerateHtml = function (type, title, msg) {
        var _html = "";
        _html += '<div id="mb_box"></div><div id="mb_con"><span id="mb_tit">' + title + '</span>';
        _html += '<a id="mb_ico">✖</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
        if (type == "alert") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "confirm") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
        }
        _html += '</div></div>';
        //必须先将_html添加到body，再设置Css样式
        $("body").append(_html);
        //生成Css
        GenerateCss();
    }

    //生成Css
    var GenerateCss = function () {
        $("#mb_box").css({ width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
        });
        $("#mb_con").css({ zIndex: '999999', width: '400px', position: 'fixed',
            backgroundColor: 'White', borderRadius: '15px'
        });
        $("#mb_tit").css({ display: 'block', fontSize: '14px', color: '#444', padding: '10px 15px',
            backgroundColor: '#DDD', borderRadius: '15px 15px 0 0',
            borderBottom: '3px solid #009BFE', fontWeight: 'bold',textAlign:"center",height:"43px"
        });
        $("#mb_msg").css({ padding: '20px', lineHeight: '20px',
            borderBottom: '1px dashed #DDD', fontSize: '13px',textAlign:"center"
        });
        $("#mb_ico").css({ display: 'block', position: 'absolute', right: '10px', top: '9px',
            border: '1px solid Gray', width: '18px', height: '18px', textAlign: 'center',
            lineHeight: '16px', cursor: 'pointer', borderRadius: '12px', fontFamily: '微软雅黑'
        });
        $("#mb_btnbox").css({ margin: '15px 0 10px 0', textAlign: 'center' });
        $("#mb_btn_ok,#mb_btn_no").css({ width: '85px', height: '30px', color: 'white', border: 'none' });
        $("#mb_btn_ok").css({ backgroundColor: '#168bbb' });
        $("#mb_btn_no").css({ backgroundColor: 'gray', marginLeft: '20px' });
        //右上角关闭按钮hover样式
        $("#mb_ico").hover(function () {
            $(this).css({ backgroundColor: '#3789DD', color: 'White' });
        }, function () {
            $(this).css({ backgroundColor: '#DDD', color: 'black' });
        });
        var _widht = document.documentElement.clientWidth;  //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高
        var boxWidth = $("#mb_con").width();
        var boxHeight = $("#mb_con").height();
        //让提示框居中
        $("#mb_con").css({ top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px" });
    }
    //确定按钮事件
    var btnOk = function (callback) {
        $("#mb_btn_ok").click(function () {
            $("#mb_box,#mb_con").remove();
            if (typeof (callback) == 'function') {
                callback();
            }
        });
    }
    //取消按钮事件
    var btnNo = function () {
        $("#mb_btn_no,#mb_ico").click(function () {
            $("#mb_box,#mb_con").remove();
        });
    }
})();

function navSelect() {
    $("#main-menu").off("click","li");
    $("#main-menu").on("click","li",function(){
        console.log(11111)
        var navName = $.trim($(this).attr("name"));
        console.log(navName)
        $(this).siblings().removeClass("bgStyle");
        $(this).addClass("bgStyle");
        if(navName == "storeManage"){
            // $.getScript("/static/plugins/common/data-statistics.js",function(){
            //
            // });
            $.ajax({
                url: "/wechat/total/storeManage",
                type: "get",
                dataType: "html",
                success: function (data) {
                    $("#main-contain").html("");
                    $("#main-contain").html(data);
                }
            })
        }else if(navName == "activityManage"){
            $.ajax({
                url: "/wechat/total/activityManage",
                type: "get",
                dataType: "html",
                success: function (data) {
                    $("#main-contain").html("");
                    $("#main-contain").html(data);
                }
            })
        }else if(navName == "awardCancel"){


            $.ajax({
                url: "/wechat/total/personalCenter",
                type: "get",
                dataType: "html",
                success: function (data) {
                    $("#main-contain").html("");
                    $("#main-contain").html(data);
                }
            })
        }
    });
}

function timestampToTime(timestamp) {
    var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = (date.getDate()<10?"0"+  date.getDate(): date.getDate())+ ' ';
    var h = (date.getHours()<10?"0"+  date.getHours(): date.getHours()) + ':';
    var m = (date.getMinutes()<10?"0"+  date.getMinutes(): date.getMinutes())  + ':';
    var s = date.getSeconds()<10?"0"+  date.getSeconds(): date.getSeconds() ;
    return Y+M+D+h+m+s;
}

