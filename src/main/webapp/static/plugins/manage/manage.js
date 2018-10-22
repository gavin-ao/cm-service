/**
 * Created by 12045 on 2018/10/22.
 */

(function () {

    $("#addAccount").off("click");
    $("#addAccount").on("click",function(){
        $("#manageAdd").show();
    });
    $("#closeBtn").off("click");
    $("#closeBtn").on("click",function(){
        $("#manageAdd").hide();
    });
    $("#submitBtn").off("click");
    $("#submitBtn").on("click",function(){
        var params = decodeURIComponent($("#formsearch").serialize())
        var dataPars = params.split("&");
        var dataObjArr= []
        for(var i=0;i<dataPars.length;i++){
            var par = dataPars[i].split("=");
            dataObjArr[par[0]] = par[1];
        }
        console.log(dataObjArr)
    });
    $(document).ready(function() {
        $('#example').DataTable( {
            searching : false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
            bLengthChange: false,   //去掉每页显示多少条数据方法
            oLanguage: {    // 汉化
                sLengthMenu: "每页显示 _MENU_ 条",
                sZeroRecords: "没有找到符合条件的数据",
                sProcessing: "加载中...",
                sInfo: "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
                sInfoEmpty: "没有记录",
                sInfoFiltered: "(从 _MAX_ 条记录中过滤)",
                sSearch: "搜索：",
                oPaginate: {
                    "sFirst": "首页",
                    "sPrevious": "前一页",
                    "sNext": "后一页",
                    "sLast": "尾页"
                }
            },
            'fnDrawCallback': function (table) {
                $("#example_paginate").append("  <div style='display: inline-block; position: relative;top: -14px;'>到第 <input type='text' id='changePages' class='input-text' style='width:50px;height:27px'> 页 <a class='btn btn-default shiny' href='javascript:void(0);' id='dataTables-btn' style='text-align:center'>确认</a></div>");
                var oTable = $("#example").dataTable();
                $('#dataTables-btn').click(function (e) {
                    if ($("#changePages").val() && $("#changePages").val() > 0) {
                        var redirectpage = $("#changePages").val() - 1;
                    } else {
                        var redirectpage = 0;
                    }

                    oTable.fnPageChange(redirectpage);
                });

                $('#changePages').keyup(function (e) {
                    if (e.keyCode == 13) {
                        if ($(this).val() && $(this).val() > 0) {
                            var redirectpage = $(this).val() - 1;
                        } else {
                            var redirectpage = 0;
                        }
                        oTable.fnPageChange(redirectpage);
                    }
                })

            }
        } );
    } );



}());

function exitName(tar) {
    console.log($(tar).val())
    return false;
}



