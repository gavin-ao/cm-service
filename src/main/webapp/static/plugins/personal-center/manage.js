/**
 * Created by 12045 on 2018/10/22.
 */

(function () {
    tablesData();
    $("#addAccount").off("click");
    $("#addAccount").on("click", function () {
        console.log(33333)
        $("#manageAdd").show();
    });
    $("#closeBtn").off("click");
    $("#closeBtn").on("click", function () {
        $("#manageAdd").hide();
    });
    $("#submitBtn").off("click");
    $("#submitBtn").on("click", function () {
        var params = decodeURIComponent($("#formsearch").serialize())
        var dataPars = params.split("&");
        var dataObjArr = []
        for (var i = 0; i < dataPars.length; i++) {
            var par = dataPars[i].split("=");
            dataObjArr[par[0]] = par[1];
        }
        console.log(dataObjArr)
    });

    //上传图片

    $("#upImage").off("click");
    $("#upImage").on("click", function (e) {
        $("#file").click();
    })
    $("#corpper").off("click");
    $("#corpper").on("click", function (e) {
        e.preventDefault();
        $('#show').imgAreaSelect({handles: true, onSelectEnd : preview});
    })
    $("#saveCorpper").off("click");
    $("#saveCorpper").click(function (e) {
        e.preventDefault();
        var pic = $('#show').attr('src');
        console.log(pic)
        $('#show').attr("src",pic);
    });
    //活动时间 选择
    laydateTime();


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
        iDisplayLength: 50,
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
            console.log(data)
            var param;
            var datastoreids = $("#dataStoreId").attr("datastoreid");
            if(datastoreids==""){
                return false;
            }
            param = {
                "storeId":datastoreids,
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
                    console.log(result)
                    if (result.success) {
                        downloaddata.search.pg.lmt = result.page.pageBean.totalNum;
                        var arry = ["actId", "actName", "startAt",  "status","commandType","remark"];
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
                            // var html = '<span style="display: inline-block;margin-left: 20px;">共计 '+ result.hit +' 条</span>';
                            //
                            // $("#dataTables-example_length").find("span").remove();
                            // $("#dataTables-example_length").append(html)
                        }, 200);
                    }
                    // downloaddata.search.pg.lmt = result.hit;
                }
            });
        },
        aoColumns: [
            {"data": ""},
            {"data": "actId"},
            {"data": "startAt"},
            {"data": "actName"},
            {"data": "status"},
            {"data": "remark"},
            {"data": "commandType"},
            {"data": "status"}
        ],
        aoColumnDefs: [
            {　　//为每一行数据添加一个checkbox，
                'aTargets': [0],
                'className': 'dt-body-center',
                'render': function (data, type, row) {
                    return '<input class="checkbox_select" type="checkbox" data-status="' + row.status + '"name="id[]" value="' + $('<div/>').text(row.id).html() + '">';
                }
            },
            {
                "aTargets": [6],
                "mRender": function (data, type, full, meta) {
                    console.log(meta);
                    console.log(data);
                    if(data==2){
                        return "助力有奖";
                    }else if(data==1){
                        return "发起邀请";
                    }

                }
            },
            {
                "aTargets": [7],
                "mRender": function (data, type, full, meta) {
                    status = 1
                    if(status == 1){
                        return "<button class='modify_btn btn btn-primary' style='padding: 2px 4px;margin-left: 8px;'>编辑</button>";
                    }else if(status == 2){
                        return "<button class='see_btn btn btn-primary' style='padding: 2px 4px;margin-left: 8px;' >查看</button>";
                    }
                 }
            },
            {
                "bSortable": false,
                "aTargets": [1, 2, 3, 4, 5]
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

    // 初始化刪除按钮
    $('#example tbody').on('click', 'button.delete_btn', function (e) {
        e.preventDefault();
        if (confirm("确定要删除该属性？")) {
            var table = $('#example').DataTable();
            table.row($(this).parents('tr')).remove().draw();
        }

    });
    // 初始化修改按钮
    $('#example tbody').on('click', 'button.modify_btn', function (e) {
        e.preventDefault();
        // var index = $(this).context._DT_RowIndex; //行号
        // console.log(index)
        var actId = $(this).parents('tr').find("td")[1].innerHTML.trim();
        var actName = $(this).parents('tr').find("td")[3].innerHTML.trim();
        var commandType = $(this).parents('tr').find("td")[6].innerHTML.trim();
        $("#actId").html(actId);
        $("#actName").html(actName);
        $("#rewardType").html(commandType);
        console.log($(this).parents('tr').find("td")[6].innerHTML.trim())
        // if (confirm("确定要修改该属性？")) {
        var datatable = $("#example1").dataTable();
        if (datatable) {
            datatable.fnClearTable();    //清空数据
            datatable.fnDestroy();         //销毁datatable
        }
        rewardCodetablesData(actId,commandType);
        $("#manageAdd").show();

    });
    // 查看按钮
    $('#example tbody').on('click', 'button.see_btn', function (e) {
        e.preventDefault();
        var datatable = $("#example1").dataTable();
        if (datatable) {
            datatable.fnClearTable();    //清空数据
            datatable.fnDestroy();         //销毁datatable
        }

        var storeId = $(this).parents('tr').find("td")[0].innerHTML.trim();
        $.ajax({
            type: "post",
            url: "/system/store/getStoreQrCode",
            cache: false,  //禁用缓存
            data: JSON.stringify({storeId: storeId}),  //传入组装的参数?
            headers: {"Content-type": "text/plain;charset=utf-8"},
            dataType: "json",
            success: function (result) {
                console.log(result)
                if (result.success) {

                }
            }
        })
        $("#manageAdd").show();

    });
}

//判断活动id是否重复
function exitId(tar) {
    console.log($(tar).val())
    return false;
}
//判断每个字段允许的长度
function fieldLength(tar) {
    console.log($(tar).val())
    return false;
}
//js锚点效果
function anchorss(id) {
    console.log(id)
    document.getElementById(id).scrollIntoView(true);
    return false;
}
//上传海报图片
function changepic() {
    var reads = new FileReader();
    f = document.getElementById('file').files[0];
    reads.readAsDataURL(f)
    console.log(f)
    if(f){
        fileSize =f.size;
        var size = fileSize / 1024;
        if (size > 300) {
            alert("文件大小不能大于300Kb！");
            file.value = "";
            return false;
        }else if (size <= 0) {
            alert("文件大小不能为0Kb！");
            file.value = "";
            return false;
        }else{
            reads.onload = function (e) {
                console.log(this)
                document.getElementById('show').src = this.result;
                $("#selShow").attr("src",this.result);
                //开启裁剪功能
                $('#show ').imgAreaSelect(
                    {
                        handles:true,
                        fadeSpeed:200,
                        imageHeight:100,
                        imageWidth:100,
                        onSelectEnd : preview
                    }
                );

            };
        }
    }else{
        return false;
    }


}

function preview(img, selection) {
    console.log(img)
    console.log(selection)
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
            if(!wholeEndTime){
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
            if(!wholeStartTime){
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
function rewardCodetablesData(actId,status) {
    $("#example1 tbody").html("");
    $('#example1').dataTable({
        searching: false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
        bLengthChange: false,   //去掉每页显示多少条数据方法
        processing: true,  //隐藏加载提示,自行处理
        serverSide: true,  //启用服务器端分页
        ordering: false,
        bDestory: true,
        aLengthMenu: [5, 10, 20, 50], //更改显示记录数选项
        iDisplayLength: 50,
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
            console.log(data)
            var param;
            var datastoreids = $("#dataStoreId").attr("datastoreid");
            if(datastoreids==""){
                return false;
            }
            if(status=="发起邀请"){
                status=1
            }else if(status=="助力有奖"){
                status=2
            }
            param = {
                "actId":actId,
                "commandType":status,
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
                    console.log(result)
                    if (result.success) {
                        downloaddata.search.pg.lmt = result.page.pageBean.totalNum;
                        var arry = ["actId", "command", "beingUsed", "commandType"];
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
                            // var html = '<span style="display: inline-block;margin-left: 20px;">共计 '+ result.hit +' 条</span>';
                            //
                            // $("#dataTables-example_length").find("span").remove();
                            // $("#dataTables-example_length").append(html)
                        }, 200);
                    }
                    // downloaddata.search.pg.lmt = result.hit;
                }
            });
        },
        aoColumns: [
            {"data": "actId"},
            {"data": "commandType"},
            {"data": "command"},
            {"data": "beingUsed"}
        ],
        aoColumnDefs: [
            {
                "aTargets": [3],
                "mRender": function (data, type, full, meta) {
                    console.log(meta);
                    console.log(data);
                    if(data==2){
                        return "助力有奖";
                    }else if(data==1){
                        return "发起邀请";
                    }

                }
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

    // 初始化刪除按钮
    $('#example tbody').on('click', 'button.delete_btn', function (e) {
        e.preventDefault();
        if (confirm("确定要删除该属性？")) {
            var table = $('#example').DataTable();
            table.row($(this).parents('tr')).remove().draw();
        }

    });
    // 初始化修改按钮
    $('#example tbody').on('click', 'button.modify_btn', function (e) {
        e.preventDefault();
        // var index = $(this).context._DT_RowIndex; //行号
        // console.log(index)
        $("#manageAdd").show();
        var status = $(this).attr(" data-status");
        var actId = $(this).parents('tr').find("td")[1].innerHTML.trim();
        console.log($(this).parents('tr').find("td")[1].innerHTML.trim())
        // if (confirm("确定要修改该属性？")) {
        var table = $('#example').DataTable();
        var rowData = table.row($(this).parents('tr').context._DT_RowIndex).data();
        console.log(rowData)
        $.ajax({
            type: "post",
            url: "/system/store/getStoreById",
            cache: false,  //禁用缓存
            data: JSON.stringify({storeId: storeId}),  //传入组装的参数?
            headers: {"Content-type": "text/plain;charset=utf-8"},
            dataType: "json",
            success: function (result) {
                console.log(result)
                if (result.success) {
                    if (result.store) {

                    }
                }
            }
        })
        $("#manageAdd").show();

    });
    // 查看按钮
    $('#example tbody').on('click', 'button.see_btn', function (e) {
        e.preventDefault();
        $("#formsearch").hide();
        $("#storeQRCode").show();

        var storeId = $(this).parents('tr').find("td")[0].innerHTML.trim();
        $.ajax({
            type: "post",
            url: "/system/store/getStoreQrCode",
            cache: false,  //禁用缓存
            data: JSON.stringify({storeId: storeId}),  //传入组装的参数?
            headers: {"Content-type": "text/plain;charset=utf-8"},
            dataType: "json",
            success: function (result) {
                console.log(result)
                if (result.success) {

                }
            }
        })
        $("#manageAdd").show();

    });
}

