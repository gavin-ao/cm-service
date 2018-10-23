/**
 * Created by 12045 on 2018/10/22.
 */

(function ($) {

    navSelect();
    $($("#main-menu").find("li")[0]).trigger("click");
    console.log($("#main-menu").find("li")[0])


}(jQuery));

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
        }else if(navName == "dataStatistics"){


            $.ajax({
                url: "/wechat/total/dataStatistics",
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

