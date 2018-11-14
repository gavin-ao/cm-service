/**
 * Created by 12045 on 2018/10/22.
 */
var tab="";
(function () {
    tablesData();
    $("#addAccount").off("click");
    $("#addAccount").on("click", function () {
        $("#manageAdd").show();
    });
    $("#closeBtn").off("click");
    $("#closeBtn").on("click", function () {
        $("#manageAdd").hide();
        $(".appendLog").hide()
    });

    //活动时间 选择
    laydateTime();

    //追加奖励
    $(".appendReward").off("click");
    $(".appendReward").on("click", function () {
        $(".appendLog").show();
    });
    $("#closeBtn1").off("click");
    $("#closeBtn1").on("click", function () {
        $(".appendLog").hide()

    });
    $("#submitBtn").off("click");
    $("#submitBtn").on("click", function () {
        var actId = $("#saveStatusL").attr("data-actid");
        var commandType = $("#saveStatusL").attr("data-commonrtype");
        var rewardNum = $(".appendLog input[name='rewardNums']").val().trim();
        if (rewardNum > 0) {
            if (actId && commandType) {

                $.ajax({
                    type: "post",
                    url: "/reward/command/insertRewardActCommand",
                    cache: false,  //禁用缓存
                    data: {actId: actId, commandType: commandType, rewardNum: rewardNum},  //传入组装的参数?
                    // headers: {"Content-type": "text/plain;charset=utf-8"},
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            $(".appendLog").hide();
                            var table = $('#example1').DataTable();
                            table.draw(false);
                        }
                    }
                })
            }
        } else {
            $.MsgBox.Alert("温馨提示", "不能小于0。");
        }

    });

}());
//表格数据
function tablesData() {
    $("#example tbody").html("");
    $('#example').dataTable({
        searching: false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
        bLengthChange: false,   //去掉每页显示多少条数据方法
        processing: true,  //隐藏加载提示,自行处理
        serverSide: true,  //启用服务器端分页
        ordering: false,
        bDestory: true,
        aLengthMenu: [5, 10, 20, 50], //更改显示记录数选项
        iDisplayLength: 20,
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
        ajax: function (data, callback, settings) {
            //封装请求参数
            var param;
            var datastoreids = $("#dataStoreId").attr("datastoreid");
            if (datastoreids == "") {
                return false;
            }
            param = {
                "storeId": datastoreids,
                "pageNo": Math.floor(data.start / data.length) + 1,
                "pageSize": data.length
            }
            // console.log(JSON.stringify(condition));
            $.ajax({
                type: "post",
                url: "/reward/content/findRewardActContentPage",
                cache: false,  //禁用缓存
                data: param,  //传入组装的参数?
                // headers: {"Content-type": "text/plain;charset=utf-8"},
                dataType: "json",
                success: function (result) {
                    if (result.success) {
                        var arry = ["actId", "actName", "startAt", "status", "commandType", "remark","actNum"];
                        var tabledata = [];
                        for (var i = 0; i < result.page.result.length; i++) {
                            result.page.result[i]["startAt"] = result.page.result[i]["startAt"] ? timestampToTime(result.page.result[i]["startAt"] / 1000) : "";
                            tabledata.push(returnIsNotInArray(arry, result.page.result[i]));
                        }
                        setTimeout(function () {
                            //封装返回数据
                            var returnData = {};
                            returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                            returnData.recordsTotal = result.page.pageBean.totalNum;//返回数据全部记录
                            returnData.recordsFiltered = result.page.pageBean.totalNum;//后台不实现过滤功能，每次查询均视作全部结果
                            returnData.data = tabledata;//返回的数据列表
                            callback(returnData);

                            $('table tr td:not(:last-child)').mouseover(function () {
                                var val = $(this).text().trim();
                                $(this).attr({title: val});
                            });
                        }, 200);
                    }
                }
            });
        },
        aoColumns: [
            {"data": "actId", "sClass": "hidden"},
            {"data": ""},
            {"data": "actNum"},
            {"data": "startAt"},
            {"data": "actName"},
            {"data": "status"},
            {"data": "remark"},
            {"data": "commandType"},
            {"data": "status"}
        ],
        aoColumnDefs: [
            {　　//为每一行数据添加一个checkbox，
                'aTargets': [1],
                'className': 'dt-body-center',
                'render': function (data, type, row) {
                    return '<input class="checkbox_select" type="checkbox" data-status="' + row.status + '"name="id[]" value="' + $('<div/>').text(row.id).html() + '">';
                }
            },
            {
                "aTargets": [5],
                "mRender": function (data, type, full, meta) {
                    var text = '';
                    if (data == 0) {
                        text = "未开始"
                    } else if (data == 1) {
                        text = "进行中"
                    } else if (data == 2) {
                        text = "已结束"
                    }
                    return text;
                }
            },
            {
                "aTargets": [7],
                "mRender": function (data, type, full, meta) {
                    if (data == 2) {
                        return "助力有奖";
                    } else if (data == 1) {
                        return "发起邀请";
                    }

                }
            },
            {
                "aTargets": [8],
                "mRender": function (data, type, full, meta) {
                    if (data == 0 || data == 1) {
                        return "<button class='modify_btn btn btn-primary' style='padding: 2px 4px;margin-left: 8px;'>编辑</button>";
                    } else if (data == 2) {
                        return "<button class='see_btn btn btn-primary' style='padding: 2px 4px;margin-left: 8px;' >查看</button>";
                    }
                }
            },
            {
                "bSortable": false,
                "aTargets": [0,1, 2, 3, 4, 5,6,7,8]
            }

        ],
        'fnDrawCallback': function (table) {
            $("#example_paginate").append("<div style='display: inline-block; position: relative;top: -2px;'>到第 <input type='text' id='changePages' class='input-text' style='width:50px;height:27px'> 页 <a class='btn btn-default shiny' href='javascript:void(0);' id='dataTables-btn' style='text-align:center'>确认</a></div>");
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
    });

    // 初始化修改按钮
    $('#example tbody').on('click', 'button.modify_btn', function (e) {
        e.preventDefault();
        var actId = $(this).parents('tr').find("td")[0].innerHTML.trim();
        var actNum = $(this).parents('tr').find("td")[2].innerHTML.trim();
        var actName = $(this).parents('tr').find("td")[4].innerHTML.trim();
        var commandType = $(this).parents('tr').find("td")[7].innerHTML.trim();
        $("#actId").html(actNum);
        $("#actName").html(actName);
        $("#rewardType").html(commandType);
        $("#saveStatusL").attr("data-actid", actId);
        $("#saveStatusL").attr("data-commonrtype", commandType);
        $(".appendReward").show();
        $("#manageAdd").show();
        tab = rewardCodetablesData(tab,actId, commandType);

    });
    // 查看按钮
    $('#example tbody').on('click', 'button.see_btn', function (e) {
        e.preventDefault();
        var actId = $(this).parents('tr').find("td")[0].innerHTML.trim();
        var actNum = $(this).parents('tr').find("td")[2].innerHTML.trim();
        var actName = $(this).parents('tr').find("td")[4].innerHTML.trim();
        var commandType = $(this).parents('tr').find("td")[7].innerHTML.trim();
        $("#actId").html(actNum);
        $("#actName").html(actName);
        $("#rewardType").html(commandType);
        $("#saveStatusL").attr("data-actid", actId);
        $("#saveStatusL").attr("data-commonrtype", commandType);
        $(".appendReward").hide();
        $("#manageAdd").show();
        tab = rewardCodetablesData(tab,actId, commandType);
    });
}


function preview(img, selection) {
    var scaleX = 100 / selection.width;
    var scaleY = 100 / selection.height;
    // var img = new Image();
    //传路径
    img.src = document.getElementById('show').src;

}
//活动时间 选择
function laydateTime() {
    lay('#version').html('-v' + laydate.v);
    var myDate = new Date();
    var time = currentTime(myDate);
//执行一个laydate实例
//     开始时间
    laydate.render({
        elem: '#startTime' //指定元素
        , value: time
        , done: function (value, date) {
            $(".contain_main_title .time1").html(value)
            wholeStartTime = value;
            if (!wholeEndTime) {
                wholeEndTime = value;
            }
            changeTimeAfterDataChange()
        }

    });

    // 结束时间
    laydate.render({
        elem: '#endTime' //指定元素
        , value: time
        , done: function (value, date) {
            $(".contain_main_title .time2").html(value)
            if (!wholeStartTime) {
                wholeStartTime = value;
            }
            wholeEndTime = value
            changeTimeAfterDataChange()
        }
    });
}

// 获取当前时间
function currentTime(myDate) {
    var year = myDate.getFullYear();
    var mounth = (myDate.getMonth() + 1) > 9 ? (myDate.getMonth() + 1) : "0" + (myDate.getMonth() + 1);
    var date = myDate.getDate() > 9 ? myDate.getDate() : "0" + myDate.getDate();
    return year + "-" + mounth + "-" + date;
}

//奖励码 表格数据
function rewardCodetablesData(table,actId, status) {
    if(table!=""){
        table.fnDestroy();         //销毁datatable
    }

    $("#example1 tbody").html("");
    var table = $('#example1').dataTable({
        searching: false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
        bLengthChange: false,   //去掉每页显示多少条数据方法
        processing: true,  //隐藏加载提示,自行处理
        serverSide: true,  //启用服务器端分页
        ordering: false,
        bDestory: true,
        aLengthMenu: [5, 10, 20, 50], //更改显示记录数选项
        iDisplayLength: 20,
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
        ajax: function (data, callback, settings) {
            //封装请求参数
            // console.log(data)
            var param;
            var datastoreids = $("#dataStoreId").attr("datastoreid");
            if (datastoreids == "") {
                return false;
            }
            if (status == "发起邀请") {
                status = 1
            } else if (status == "助力有奖") {
                status = 2
            }
            $("#saveStatusL").attr("data-commonrtype", status);
            param = {
                "actId": actId,
                "commandType": status,
                "pageNo": Math.floor(data.start / data.length) + 1,
                "pageSize": data.length
            }
            // console.log(JSON.stringify(condition));
            $.ajax({
                type: "post",
                url: "/reward/command/findRewardActCommandPage",
                cache: false,  //禁用缓存
                data: param,  //传入组装的参数?
                // headers: {"Content-type": "text/plain;charset=utf-8"},
                dataType: "json",
                success: function (result) {
                    if (result.success) {
                        var arry = ["commandId", "command", "beingUsed", "commandType"];
                        var tabledata = [];
                        for (var i = 0; i < result.page.result.length; i++) {
                            tabledata.push(returnIsNotInArray(arry, result.page.result[i]));
                        }
                        setTimeout(function () {
                            //封装返回数据
                            var returnData = {};
                            returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                            returnData.recordsTotal = result.page.pageBean.totalNum;//返回数据全部记录
                            returnData.recordsFiltered = result.page.pageBean.totalNum;//后台不实现过滤功能，每次查询均视作全部结果
                            returnData.data = tabledata;//返回的数据列表
                            callback(returnData);

                            $('table tr td:not(:last-child)').mouseover(function () {
                                var val = $(this).text().trim();
                                $(this).attr({title: val});
                            });
                        }, 200);
                    }
                }
            });
        },
        aoColumns: [
            {"data": "commandId"},
            {"data": "commandType"},
            {"data": "command"},
            {"data": "beingUsed"}
        ],
        aoColumnDefs: [
            {
                "aTargets": [1],
                "mRender": function (data, type, full, meta) {
                    if (data == 2) {
                        return "助力有奖";
                    } else if (data == 1) {
                        return "发起邀请";
                    }
                }
            },
            {
                "aTargets": [3],
                "mRender": function (data, type, full, meta) {
                    var text='';
                    if(data==0){
                        text="未使用"
                    }else if(data==1){
                        text="已核销"
                    }
                    return text;
                }
            },
            {
                "bSortable": false,
                "aTargets": [0, 1, 2, 3]
            },

        ],
        'fnDrawCallback': function (table) {
            $("#example1_paginate").append("<div style='display: inline-block; position: relative;top: -2px;'>到第 <input type='text' id='changePages1' class='input-text' style='width:50px;height:27px'> 页 <a class='btn btn-default shiny' href='javascript:void(0);' id='dataTables-btn1' style='text-align:center'>确认</a></div>");
            var oTable = $("#example1").dataTable();
            $('#dataTables-btn1').click(function (e) {
                if ($("#changePages1").val() && $("#changePages1").val() > 0) {
                    var redirectpage = $("#changePages1").val() - 1;
                } else {
                    var redirectpage = 0;
                }
                oTable.fnPageChange(redirectpage);
            });

            $('#changePages1').keyup(function (e) {
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
    });
    return table;
}
