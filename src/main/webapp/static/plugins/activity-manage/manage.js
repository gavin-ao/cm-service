/**
 * Created by 12045 on 2018/10/22.
 */
var wholeStartTime, wholeEndTime, startDates, endDates;
(function () {
    // 退出登录
    // $("#loginOut").on("click", function (){
    //     console.log(6645346)
    //     loginOut();
    // });
//表格数据
    tablesData();
    $("#manageAdd").hide();
    $("#currentActId").attr("data-curr-actid", "");
    $("#addAccount").off("click");
    $("#addAccount").on("click", function () {
        $("#show").parent().hide()
        $("#formsearch .modal-footer").show();
        $("#currentActId").attr("data-curr-actid", "");
        $("#currentActId").attr("data-curr-contentid1","");
        $("#currentActId").attr("data-curr-contentid2","");
        $("#formsearch input[name='invitingAwardsNum']").parents("p").show();
        $("#manageAdd").show();
        $("#formsearch>p input").each(function () {
            $(this).val("")
        })
        $("#formsearch>p textarea").each(function () {
            $(this).val("")
        })
        $("input[name='helpNumber']").val(1);
        $("input[name='invitingAwardsNum']").val(1);
        //获取活动时间
        activTime();
    });
    $(".closeBtn1").off("click");
    $(".closeBtn1").on("click", function () {
        $("#manageAdd").hide();
    });
    $("#submitBtn").off("click");
    $("#submitBtn").on("click", function () {
        var params = decodeURIComponent($("#formsearch").serialize())
        console.log(params)
        var dataPars = params.split("&");
        var dataObjArr = []
        for (var i = 0; i < dataPars.length; i++) {
            var par = dataPars[i].split("=");
            dataObjArr[par[0]] = par[1];
        }
        console.log(dataObjArr);
        //校验字段
        var flag = checkField(dataObjArr);
        if (dataObjArr.helpNumber <= 0) {
            $.MsgBox.Alert("温馨提示", "助力目标人数要大于0");
            flag = false
        }
        if (dataObjArr.invitingAwardsNum <= 0) {
            $.MsgBox.Alert("温馨提示", "邀请奖励数量要大于0");
            flag = false
        }
        if (!flag) {
            return false;
        }
        var src = $("#show").attr("src");
        if (src) {
            $.ajax({
                type: "post",
                url: "/system/file/pictureUpload",
                cache: false,  //禁用缓存
                data: {pictureJson: src},  //传入组装的参数?
                // headers: {"Content-type": "text/plain;charset=utf-8;"},
                dataType: "json",
                success: function (result) {
                    console.log(result)
                    if (result.success) {
                        if (result.pictureId) {
                            addActivity(result.pictureId, dataObjArr);
                        }
                    }
                }
            })
        } else {
            $.MsgBox.Alert("温馨提示", "活动海报不能为空。");
        }

    });

    //上传图片

    $("#upImage").off("click");
    $("#upImage").on("click", function (e) {
        $("#file").click();
    })
    $("#corpper").off("click");
    $("#corpper").on("click", function (e) {
        e.preventDefault();
        $('#show').imgAreaSelect({handles: true, onSelectEnd: preview});
    })
    $("#saveCorpper").off("click");
    $("#saveCorpper").click(function (e) {
        e.preventDefault();
        var pic = $('#show').attr('src');
        console.log(pic)
        $('#show').attr("src", pic);
    });


}());
//获取活动时间
function activTime() {
    $.ajax({
        type: "post",
        url: "/mat/activity/getNextActivityStartDate",
        cache: false,  //禁用缓存
        // headers: {"Content-type": "text/plain;charset=utf-8"},
        dataType: "json",
        success: function (result) {
            console.log(result)
            if (result.success) {

//活动时间 选择
                laydateTime(result.nextDate);
            }
        }
    })
}

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
            console.log(data)

            param = {
                "pageNo": Math.floor(data.start / data.length) + 1,
                "pageSize": data.length
            }
            // console.log(JSON.stringify(condition));
            $.ajax({
                type: "post",
                url: "/mat/activity/findActivityPage",
                cache: false,  //禁用缓存
                data: param,  //传入组装的参数?
                // headers: {"Content-type": "text/plain;charset=utf-8"},
                dataType: "json",
                success: function (result) {
                    console.log(result)
                    if (result.success) {
                        var arry = ["actId", "actTitle", "startAt", "endAt", "status"];
                        var tabledata = [];
                        for (var i = 0; i < result.page.result.length; i++) {
                            result.page.result[i]["startAt"] = result.page.result[i]["startAt"] ? timestampToTime(result.page.result[i]["startAt"] / 1000) : "";
                            result.page.result[i]["endAt"] = result.page.result[i]["endAt"] ? timestampToTime(result.page.result[i]["endAt"] / 1000) : "";
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
                            // // 点击页码时，保存当前页码数据
                            // $("#dataTables-example_paginate .pagination").off("click","li.paginate_button a");
                            // $("#dataTables-example_paginate .pagination").on("click","li.paginate_button a",function(){
                            //     $("#select-all").prop("checked",false);
                            //     if($("#dataTables-example tbody").find(".artificial").length>0){
                            //         savePageData(table,true);
                            //     }
                            // });
                            $('table tr td:not(:last-child)').mouseover(function () {
                                var val = $(this).text().trim();
                                $(this).attr({title: val});
                            });
                            // var html = '<span style="display: inline-block;margin-left: 20px;">共计 '+ result.hit +' 条</span>';
                            //
                            // $("#dataTables-example_length").find("span").remove();
                            // $("#dataTables-example_length").append(html)
                        }, 200);
                    } else {
                        $.MsgBox.Alert("温馨提示", result.msg);
                    }
                    // downloaddata.search.pg.lmt = result.hit;
                }
            });
        },
        aoColumns: [
            {"data": ""},
            {"data": "actId"},
            {"data": "actTitle"},
            {"data": "startAt"},
            {"data": "endAt"},
            {"data": "status"},
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
                "aTargets": [6],
                "mRender": function (data, type, full, meta) {
                    // console.log(meta);
                    // console.log(data);
                    var text = '';
                    if (data == 0) {
                        text = "<button class='modify_btn btn btn-primary' style='padding: 2px 4px;margin-left: 8px;' >编辑</button>"
                    } else if (data == 1 || data == 2) {
                        text = "<button class='see_btn btn btn-primary' style='padding: 2px 4px;margin-left: 8px;' >查看</button>"
                    }
                    return text;
                }
            },
            {
                "bSortable": false,
                "aTargets": [0, 1, 2, 3, 4, 5, 6]
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
        $("#currentActId").attr("data-curr-actid", actId);
        console.log($(this).parents('tr').find("td")[0].innerHTML.trim())
        $.ajax({
            type: "post",
            url: "/mat/activity/getMatActivityAllInfo",
            cache: false,  //禁用缓存
            data: {actId: actId},  //传入组装的参数?
            // headers: {"Content-type": "text/plain;charset=utf-8"},
            dataType: "json",
            success: function (result) {
                console.log(result)
                if (result.success) {
                    $("#formsearch .modal-footer").show();
                    //获取活动时间
                    // activTime();
                    //反补
                    reverseSupplement(result);
                    $("#manageAdd").show();
                }
            }
        })


    });
    // 查看按钮
    $('#example tbody').on('click', 'button.see_btn', function (e) {
        e.preventDefault();
        // var index = $(this).context._DT_RowIndex; //行号
        // console.log(index)
        var actId = $(this).parents('tr').find("td")[1].innerHTML.trim();
        $("#currentActId").attr("data-curr-actid", actId);
        console.log($(this).parents('tr').find("td")[0].innerHTML.trim())
        $.ajax({
            type: "post",
            url: "/mat/activity/getMatActivityAllInfo",
            cache: false,  //禁用缓存
            data: {actId: actId},  //传入组装的参数?
            // headers: {"Content-type": "text/plain;charset=utf-8"},
            dataType: "json",
            success: function (result) {
                console.log(result)
                if (result.success) {
                    $("#formsearch .modal-footer").hide();
                    //反补
                    reverseSupplement(result);
                    $("#manageAdd").show();
                }
            }
        })

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
    console.log($(tar).attr("name"))
    var titleName = $.trim($(tar).attr("name"));
    var titleValue = $.trim($(tar).val());
    switch (titleName) {
        case 'storeTitle':
            judgmenLength(titleValue, 20);
            break;
        case 'invitingButton':
            judgmenLength(titleValue, 36);
            break;
        // case 'helpButton':
        //     judgmenLength(titleValue, 26);
        //     break;
        // case 'boosterButton':
        //     judgmenLength(titleValue, 26);
        //     break;
        // case 'fullHelpButton':
        //     judgmenLength(titleValue, 26);
        //     break;
        // case 'partsActivity':
        //     judgmenLength(titleValue, 18);
        //     break;
        case 'posterCopywriting':
            judgmenLength(titleValue, 72);
            break;
        case 'shareTitle':
            judgmenLength(titleValue, 52);
            break;
    }
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
    if (f) {
        fileSize = f.size;

        var size = fileSize / 1024;
        if (size > 200) {
            alert("文件大小不能大于200Kb！");
            file.value = "";
            return false;
        } else if (size <= 0) {
            alert("文件大小不能为0Kb！");
            file.value = "";
            return false;
        } else {
            // $('#show').attr("names",f.name);
            reads.onload = function (e) {
                console.log(this)
                $("#show").parent().show();
                document.getElementById('show').src = this.result;

                // $("#selShow").attr("src",this.result);
                //开启裁剪功能
                // $('#show ').imgAreaSelect(
                //     {
                //         handles:true,
                //         fadeSpeed:200,
                //         imageHeight:100,
                //         imageWidth:100,
                //         onSelectEnd : preview
                //     }
                // );

            };
        }
    } else {
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
function laydateTime(myDate) {
    lay('#version').html('-v' + laydate.v);
    // var myDate = new Date();
    console.log(myDate)
    var time = myDates(myDate);
    wholeEndTime = wholeStartTime = time
//执行一个laydate实例
//     开始时间
    laydate.render({
        elem: '#startTime' //指定元素
        , value: time
        , done: function (value, date) {
            startDates = new Date(Date.parse(value.replace(/-/g, "/")));
            startDates = startDates.getTime();
            endDates = new Date(Date.parse(wholeEndTime.replace(/-/g, "/")));
            endDates = endDates.getTime();
            if (startDates > endDates) {
                console.log(wholeEndTime)
                $("#startTime").val(wholeEndTime)
                wholeStartTime = wholeEndTime;
                $.MsgBox.Alert("温馨提示", "开始时间不能大于结束时间。");
            } else {
                $(".contain_main_title .time1").html(value)
                wholeStartTime = value;
            }
        }

    });

    // 结束时间
    laydate.render({
        elem: '#endTime' //指定元素
        , value: time
        , done: function (value, date) {
            startDates = new Date(Date.parse(wholeStartTime.replace(/-/g, "/")));
            startDates = startDates.getTime();
            endDates = new Date(Date.parse(value.replace(/-/g, "/")));
            endDates = endDates.getTime();
            if (startDates > endDates) {
                $("#endTime").val(wholeStartTime)
                wholeEndTime = wholeStartTime
                $.MsgBox.Alert("温馨提示", "结束时间不能小于开始时间。");
            } else {
                $(".contain_main_title .time2").html(value)
                wholeEndTime = value
            }
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

//判断字数长度 是否符合要求
function judgmenLength(text, lens, con) {
    if (text == "") {
        $.MsgBox.Alert("温馨提示", con + "不能为空");
        return false;
    }
    var realLength = 0, len = text.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = text.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128)
            realLength += 1;
        else
            realLength += 2;
    }
    console.log(realLength)
    if (realLength > lens * 2) {
        $.MsgBox.Alert("温馨提示", con + lens + "个字以内");
        return false;
    }
    return true;

}


//添加活动
function addActivity(picId, par) {

    var btnCopywritingJson = {
        bxt_invite: par.invitingButton,
        bxt_full: "助力已满，我也要领奖",
        bxt_help: "给他助力",
        bxt_alhelp: "已助力，我也要领奖",
        bxt_continue: "立即邀请",
        bxt_continue1: "继续邀请",
        bxt_reward: "领取奖励",
        bxt_share: "分享",
        bxt_saveImg: "保存图片"
    };


    var rewardActContentJson = [
        {
            "commandType": 1,
            remark: par.invitingAwards,
            contentTitle: "任务达成",
            contentHead: "恭喜您获得 " + par.invitingAwards,
            contentFoot: "数量有限，先到先得哦",
            contentBtn: "我也要领奖励"
        },
        {
            "commandType": 2,
            remark: par.aidReward,
            contentTitle: "为好友助力成功",
            contentHead: "恭喜您获得 " + par.aidReward,
            contentFoot: "数量有限，先到先得哦",
            contentBtn: "我也要领奖励"
        }
    ];
    var contentid1 = $("#currentActId").attr("data-curr-contentid1");
    var contentid2 = $("#currentActId").attr("data-curr-contentid2");
    if(contentid1&&contentid2){
        rewardActContentJson = [
            {
                "commandType": 1,
                remark: par.invitingAwards,
                contentTitle: "任务达成",
                contentHead: "恭喜您获得 " + par.invitingAwards,
                contentFoot: "数量有限，先到先得哦",
                contentBtn: "我也要领奖励",
                contentId:contentid1
            },
            {
                "commandType": 2,
                remark: par.aidReward,
                contentTitle: "为好友助力成功",
                contentHead: "恭喜您获得 " + par.aidReward,
                contentFoot: "数量有限，先到先得哦",
                contentBtn: "我也要领奖励",
                contentId:contentid2
            }
        ];
    }
    var activity = {
        actName: par.storeTitle,
        pictureId: picId,
        actTitle: par.storeTitle,
        actShareTitle: par.shareTitle,
        actShareCopywriting: par.posterCopywriting,
        actRule: par.activityRules,
        partakeNum: par.helpNumber,
        startAt: par.startTimes.replace(/-/g, "/"),
        endAt: par.endTimes.replace(/-/g, "/"),
        btnCopywritingJson: JSON.stringify(btnCopywritingJson),
        rewardActContentJson: JSON.stringify(rewardActContentJson),
    };
    var actId = $("#currentActId").attr("data-curr-actid");
    if (actId) {
        activity.actId = actId;
    }else{
        activity.rewardNum = par.invitingAwardsNum
    }
    $.ajax({
        type: "post",
        url: "/mat/activity/updateActivity",
        cache: false,  //禁用缓存
        data: activity,  //传入组装的参数?
        // headers: {"Content-type": "text/plain;charset=utf-8;"},
        dataType: "json",
        success: function (result) {
            console.log(result)
            if (result.success) {
                var table = $('#example').DataTable();
                $("#manageAdd").hide();
                table.draw(false)
            } else {
                $.MsgBox.Alert("温馨提示", result.msg);
            }
        }
    })
}

//修改 条件反补
function reverseSupplement(data) {
    var activity = data.matActivity;
    var rewardList = data.rewardList;
    $("#formsearch input[name='invitingButton']").val(data.btnMap.bxt_invite);
    $("#formsearch input[name='storeTitle']").val(activity.actTitle);
    $("#formsearch input[name='posterCopywriting']").val(activity.actShareCopywriting);
    $("#formsearch textarea[name='activityRules']").val(activity.actRule);
    $("#formsearch input[name='startTimes']").val(myDates(activity.startAt));
    $("#formsearch input[name='endTimes']").val(myDates(activity.endAt));
    $("#formsearch input[name='helpNumber']").val(activity.partakeNum);
    $("#formsearch input[name='shareTitle']").val(activity.actShareTitle);
    $("#formsearch input[name='invitingAwardsNum']").parents("p").hide();
    for (var i = 0; i < rewardList.length; i++) {
        if (rewardList[i].commandType == 1) {
            $("#currentActId").attr("data-curr-contentid1",rewardList[i].contentId);
            $("#formsearch input[name='invitingAwards']").val(rewardList[i].remark);
        } else if (rewardList[i].commandType == 2) {
            $("#currentActId").attr("data-curr-contentid2",rewardList[i].contentId);
            $("#formsearch input[name='aidReward']").val(rewardList[i].remark);
        }
    }
    // var url = "http://p1.pstatp.com/large/435d000085555bd8de10";
    // getBase64(url)
    //     .then(function (base64) {
    //         console.log(base64);//处理成功打印在控制台
    //         $("#show").parent().show()
    //         $("#show").attr("src", base64);
    //     }, function (err) {
    //         console.log(err);//打印异常信息
    //     });
    if (activity.filePath) {
        $("#show").parent().show();
        var filePath = "https://cm-service.easy7share.com" + activity.filePath;
        convertImgToBase64(filePath, function (base64Img) {
            //转化后的base64
            $("#show").attr("src", base64Img);
        });
    }

}

//校验字段
function checkField(dataField) {
    var flag = judgmenLength(dataField.storeTitle, 10, "活动标题");
    flag = judgmenLength(dataField.invitingButton, 18, "邀请按钮文案");
    flag = judgmenLength(dataField.posterCopywriting, 36, "活动玩法说明");
    flag = judgmenLength(dataField.shareTitle, 26, "分享标题");

    return flag;
}


