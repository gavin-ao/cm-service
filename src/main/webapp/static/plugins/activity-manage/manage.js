/**
 * Created by 12045 on 2018/10/22.
 */
var wholeStartTime, wholeEndTime, startDates, endDates;
var invitationGlobalId, assistanceGlobalId, invitationCurrencyId, assistanceCurrencyId,  assistanceRewardTypes,initiatorRewardTypes;
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
        // 变量初始化
        initData();
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
        $("input[name='assistanceAwardsNum']").val(1);
        $("#assistanceExitReward").val(2);
        $("#assistanceNoReward").val(1);
        //默认助力有奖
        $("#assistanceExitReward").trigger("click");
        //默认邀请奖励类型 一人一码
        $("#invitationRewardType option:first").prop("selected", 'selected');
        $(".invitationGroup").hide();
        $(".invitationReward").show();
        $("input[name='invitingAwards']").attr("placeholder", "自由设置，领取后按设置核销，如:满199减30");
        //默认助力奖励类型 一人一码
        $("#assistanceRewardType option:first").prop("selected", 'selected');
        $(".assistanceGroup").hide();
        $(".showExit").show();
        $("input[name='aidReward']").attr("placeholder", "应比邀请奖励小，以刺激转发，如:满199减10");
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
            flag = false;
            $.MsgBox.Alert("温馨提示", "助力目标人数要大于0");
        }
        if (dataObjArr.invitingAwardsNum <= 0) {
            flag = false;
            $.MsgBox.Alert("温馨提示", "邀请奖励数量要大于0");
        }
        var src = $("#show").attr("src");
        var pictureJson = {};
        if (dataObjArr.invitationRewardType == 3) {
            var tarms = $("#invitationImagesShow p");
            if (tarms.length == 0) {
                flag = false;
                $.MsgBox.Alert("温馨提示", "邀请奖励进群二维码不能为空");
            } else {
                var invitatorImg = [];
                for (var i = 0; i < tarms.length; i++) {
                    var imgSrc = $(tarms[i]).find("img").attr("src");
                    if (imgSrc) {
                        invitatorImg.push(imgSrc);
                    }
                }
                pictureJson.invitatorImg = invitatorImg;
            }
        }
        if (dataObjArr.assistanceRewardType == 3) {
            var tarms = $("#assistanceImagesShow p");
            if (tarms.length == 0) {
                flag = false;
                $.MsgBox.Alert("温馨提示", "助力奖励进群二维码不能为空");
            } else {
                var assistanceImg = [];
                for (var i = 0; i < tarms.length; i++) {
                    var imgSrc = $(tarms[i]).find("img").attr("src");
                    if (imgSrc) {
                        assistanceImg.push(imgSrc);
                    }
                }
                pictureJson.assistanceImg = assistanceImg;
            }
        }
        if (src && flag) {
            pictureJson.first = src;
            $.ajax({
                type: "post",
                url: "/system/file/pictureUploads",
                cache: false,  //禁用缓存
                data: {paramJson: JSON.stringify(pictureJson)},  //传入组装的参数?
                dataType: "json",
                success: function (result) {
                    // console.log(result)
                    if (result.success) {
                        if (result.first) {
                            addActivity(result.first, dataObjArr, result.assistanceImg, result.invitatorImg);
                        }
                    }
                }
            })
        } else {
            $.MsgBox.Alert("温馨提示", "活动海报不能为空。");
        }

    });
    $("input[name='helpNumber']").on("change", function () {
        console.log($(this).val())
        var helpNumber = parseInt($(this).val());
        var invitingAwardsNum = parseInt($("input[name='invitingAwardsNum']").val())
        $("input[name='assistanceAwardsNum']").val(helpNumber * invitingAwardsNum);
        $("input[name='assistanceAwardsNum']").attr("min", helpNumber * invitingAwardsNum);
    })
    $("input[name='invitingAwardsNum']").on("change", function () {
        console.log($(this).val())
        var invitingAwardsNum = parseInt($(this).val());
        var helpNumber = parseInt($("input[name='helpNumber']").val())
        $("input[name='assistanceAwardsNum']").val(helpNumber * invitingAwardsNum);
        $("input[name='assistanceAwardsNum']").attr("min", helpNumber * invitingAwardsNum);
    })
    //上传海报图片
    $("#upImage").off("click");
    $("#upImage").on("click", function (e) {
        $("#file").click();
    })
    // $("#corpper").off("click");
    // $("#corpper").on("click", function (e) {
    //     e.preventDefault();
    //     $('#show').imgAreaSelect({handles: true, onSelectEnd: preview});
    // })
    // $("#saveCorpper").off("click");
    // $("#saveCorpper").click(function (e) {
    //     e.preventDefault();
    //     var pic = $('#show').attr('src');
    //     // console.log(pic)
    //     $('#show').attr("src", pic);
    // });

    //助力是否有奖
    //没奖
    $("#assistanceNoReward").off("click");
    $("#assistanceNoReward").on("click", function () {
        $(".showExit").hide();
        $(".assistanceGroup").hide();
        $("#assistanceImagesShow").html("");
        $("#assistanceImagesShow").hide();
    });
    //有奖
    $("#assistanceExitReward").off("click");
    $("#assistanceExitReward").on("click", function () {
        $(".showExit").show();
        $("#assistanceRewardType option:first").prop("selected", 'selected');
        $(".showExit input[name='aidReward']").val("");
        $(".assistanceGroup").hide();
        $("input[name='aidReward']").attr("placeholder", "应比邀请奖励小，以刺激转发，如:满199减10");
    });

    // 邀请奖励类型
    $("#invitationRewardType").off("change");
    $("#invitationRewardType").on("change", function () {
        console.log($(this).val())
        var actId = $("#currentActId").attr("data-curr-actid");
        var index = parseInt($(this).val().trim())
        switch (index) {
            case 1: // 一人一码
                $(".invitationGroup").hide();
                $(".invitationReward").show();
                $("#invitationImagesShow").html("");
                $("#invitationImagesShow").hide();
                $("input[name='invitingAwards']").val('');
                $("input[name='invitingAwards']").attr("placeholder", "自由设置，领取后按设置核销，如:满199减30");
                $("input[name='invitingAwardsNum']").val(1);
                if (actId&&initiatorRewardTypes==1) {
                    $(".invitationNumber").hide();
                }
                break;
            case 2:// 淘口令
                $(".invitationGroup").hide();
                $(".invitationReward").show();
                $("#invitationImagesShow").html("");
                $("#invitationImagesShow").hide();
                $("input[name='invitingAwards']").val('');
                $("input[name='invitingAwardsNum']").val(1);
                $("input[name='invitingAwards']").attr("placeholder", "设置自己的淘口令，如:€u56pb2a7sOn€");
                $(".invitationNumber").hide();
                break;
            case 3: // 进群领奖
                console.log(22222222)
                $(".invitationReward").hide();
                $(".invitationGroup").show();
                $("input[name='invitationUploadPicture']").val('');
                $("#invitationImagesShow").html("");
                break;
            case 4://落地页
                $(".invitationGroup").hide();
                $(".invitationReward").show();
                $(".invitationNumber").hide();
                $("input[name='invitingAwards']").val('');
                $("input[name='invitingAwards']").attr("placeholder", "设置领取奖励的链接");
                $(".invitationNumber").hide();
                break;
        }
    });


    // 助力奖励类型
    $("#assistanceRewardType").off("change");
    $("#assistanceRewardType").on("change", function () {
        console.log($(this).val())
        var actId = $("#currentActId").attr("data-curr-actid");
        var index = parseInt($(this).val().trim())
        if (index == 1 || index == 2 || index == 4) {
            $(".assistanceGroup").hide();
            $(".assistanceReward").show();
            $("input[name='aidReward']").val('');
            $("#assistanceImagesShow").html("");
            $("#assistanceImagesShow").hide();
        }
        switch (index) {
            case 1:
                $("input[name='aidReward']").attr("placeholder", "应比邀请奖励小，以刺激转发，如:满199减10");
                if (actId&&assistanceRewardTypes==1) {
                    $(".assistanceNumber").hide();
                }
                break;
            case 2:
                $("input[name='aidReward']").attr("placeholder", "设置自己的淘口令，如:€u56pb2a7sOn€");
                $(".assistanceNumber").hide();
                break;
            case 3:
                $(".assistanceGroup").show();
                $(".assistanceReward").hide();
                $("input[name='assistanceUploadPicture']").val('');
                $("#assistanceImagesShow").html("");
                break;
            case 4:
                $("input[name='aidReward']").attr("placeholder", "设置领取奖励的链接");
                $(".assistanceNumber").hide();
                break;
        }
    });
    // 一人一码
    // 淘口令
    // 客服消息

    //上传邀请进群图片
    $("#invitationUpImage").off("click");
    $("#invitationUpImage").on("click", function (e) {
        $("#invitationFile").click();
    })
    //上传助力进群图片
    $("#assistanceUpImage").off("click");
    $("#assistanceUpImage").on("click", function (e) {
        $("#assistanceFile").click();
    })


}());
// 变量初始化
function initData() {
    invitationGlobalId = '', assistanceGlobalId = '', invitationCurrencyId = '', assistanceCurrencyId = '',assistanceRewardTypes='',initiatorRewardTypes='';
    $("#currentActId").attr("data-curr-actid", "");
    $("#currentActId").attr("data-curr-contentid1", "");
    $("#currentActId").attr("data-curr-contentid2", "");
}

//获取活动时间
function activTime() {
    $.ajax({
        type: "post",
        url: "/mat/activity/getNextActivityStartDate",
        cache: false,  //禁用缓存
        // headers: {"Content-type": "text/plain;charset=utf-8"},
        dataType: "json",
        success: function (result) {
            // console.log(result)
            if (result.success) {

//活动时间 选择
                var time = myDates(result.nextDate);
                wholeEndTime = wholeStartTime = time
                laydateTime(time);
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
            // console.log(data)

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
                    // console.log(result)
                    if (result.success) {
                        var arry = ["actId", "actTitle", "startAt", "endAt", "status", "actNum"];
                        var tabledata = [];
                        for (var i = 0; i < result.page.result.length; i++) {
                            result.page.result[i]["startAt"] = result.page.result[i]["startAt"] ? myDates(result.page.result[i]["startAt"]) : "";
                            result.page.result[i]["endAt"] = result.page.result[i]["endAt"] ? myDates(result.page.result[i]["endAt"]) : "";
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
            {"data": "actId", "sClass": "hidden"},
            {"data": ""},
            {"data": "actNum"},
            {"data": "actTitle"},
            {"data": "startAt"},
            {"data": "endAt"},
            {"data": "status"},
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
                "aTargets": [6],
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
                "aTargets": [0, 1, 2, 3, 4, 5, 6, 7]
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
        // 变量初始化
        initData();
        var actId = $(this).parents('tr').find("td")[0].innerHTML.trim();
        $("#currentActId").attr("data-curr-actid", actId);
        // console.log($(this).parents('tr').find("td")[0].innerHTML.trim())
        $.ajax({
            type: "post",
            url: "/mat/activity/getMatActivityAllInfo",
            cache: false,  //禁用缓存
            data: {actId: actId},  //传入组装的参数?
            // headers: {"Content-type": "text/plain;charset=utf-8"},
            dataType: "json",
            success: function (result) {
                // console.log(result)
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
        // 变量初始化
        initData();
        var actId = $(this).parents('tr').find("td")[0].innerHTML.trim();
        $("#currentActId").attr("data-curr-actid", actId);
        // console.log($(this).parents('tr').find("td")[0].innerHTML.trim())
        $.ajax({
            type: "post",
            url: "/mat/activity/getMatActivityAllInfo",
            cache: false,  //禁用缓存
            data: {actId: actId},  //传入组装的参数?
            // headers: {"Content-type": "text/plain;charset=utf-8"},
            dataType: "json",
            success: function (result) {
                // console.log(result)
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

//判断每个字段允许的长度
function fieldLength(tar) {
    var titleName = $.trim($(tar).attr("name"));
    var titleValue = $.trim($(tar).val());
    switch (titleName) {
        case 'storeTitle':
            judgmenLength(titleValue, 10, "活动标题");
            break;
        case 'invitingButton':
            judgmenLength(titleValue, 18, "邀请按钮文案");
            break;
        case 'posterCopywriting':
            judgmenLength(titleValue, 40, "活动玩法说明");
            break;
        case 'shareTitle':
            judgmenLength(titleValue, 26, "分享标题");
            break;
    }
    return false;
}

//上传海报图片
function changepic() {
    var reads = new FileReader();
    f = document.getElementById('file').files[0];
    reads.readAsDataURL(f)
    // console.log(f)
    if (f) {
        fileSize = f.size;

        var size = fileSize / 1024;
        if (size > 200) {
            $.MsgBox.Alert("温馨提示", "海报图片大小不能大于200Kb！");
            file.value = "";
            return false;
        } else if (size <= 0) {
            $.MsgBox.Alert("温馨提示", "海报图片大小不能为0Kb！");
            file.value = "";
            return false;
        } else {
            // $('#show').attr("names",f.name);

            reads.onload = function (e) {
                // console.log(e)
                var data = this.result;
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
                var flag = false;
                var image = new Image();
                image.src = data;
                image.onload = function () {
                    console.log(image.height)
                    console.log(image.width)
                    var width = image.width;
                    var height = image.height;

                    for (var i = 1; i < 6;) {
                        if (((351 * i - 10) <= width && width <= (351 * i + 10)) && ((523 * i - 10) <= height && height <= (523 * i + 10))) {
                            flag = true;
                        }
                        i = i + 0.1
                    }
                    if (flag) {
                        $("#show").parent().show();
                        document.getElementById('show').src = data;
                    } else {
                        $.MsgBox.Alert("温馨提示", "图片尺寸应为：351*523(或同比例放大)");
                        return false;
                    }
                };

            };
        }
    } else {
        return false;
    }


}

//上传邀请进群图片
function invitationchangepic(id, tar, newId) {
    var that = this;
    var reads = new FileReader();
    f = document.getElementById(id).files[0];
    reads.readAsDataURL(f);
    console.log(f)
    if (f) {
        fileSize = f.size;
        var size = fileSize / 1024;
        if (size > 200) {
            $.MsgBox.Alert("温馨提示", "图片大小不能大于200Kb！");
            return false;
        } else if (size <= 0) {
            $.MsgBox.Alert("温馨提示", "图片大小不能为0Kb！");
            return false;
        } else {
            reads.onload = function (e) {
                // console.log(e)
                var data = this.result;
                console.log()
                $("#" + newId).show();
                var img = '<p class="newImages"><img src="' + data + '" onclick="modifyCurrentImg(this,' + tar + ')"><span onclick="deleteImage(this,' + id + ')">✖</span></p>';
                $("#" + newId).append(img);
            };
        }
    } else {
        return false;
    }
}
// 修改当前 进群二维码图片
function modifyCurrentImg(tar, id) {
    console.log(tar)
    $(id).trigger("click");
    $("#modifyCurrentImgs").on("change", function () {
        modifyCurrentImgFile("modifyCurrentImgs", tar);
    })
}
function modifyCurrentImgFile(id, tar) {
    var reads = new FileReader();
    f = document.getElementById(id).files[0];
    reads.readAsDataURL(f);
    console.log(f)
    if (f) {
        fileSize = f.size;
        var size = fileSize / 1024;
        if (size > 200) {
            $.MsgBox.Alert("温馨提示", "图片大小不能大于200Kb！");
            return false;
        } else if (size <= 0) {
            $.MsgBox.Alert("温馨提示", "图片大小不能为0Kb！");
            return false;
        } else {
            reads.onload = function (e) {
                // console.log(e)
                var data = this.result;
                $(tar).attr("src", data);

            };
        }
    } else {
        return false;
    }
}

// 删除 进群二维码图片
function deleteImage(tar, id) {
    console.log(tar)
    var that = $(tar).parent().parent();
    $(tar).parent().remove();
    $(id).val('')
    if (that.find(".newImages").length <= 0) {
        that.hide();
    }
}
//活动时间 选择
function laydateTime(time, times) {
    lay('#version').html('-v' + laydate.v);
    // var myDate = new Date();

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
                $("#startTime").val(wholeEndTime);
                wholeStartTime = wholeEndTime;
                $.MsgBox.Alert("温馨提示", "开始时间不能大于结束时间。");
            } else {
                $(".contain_main_title .time1").html(value);
                wholeStartTime = value;
            }
        }

    });
    if (times) {
        // 结束时间
        laydate.render({
            elem: '#endTime' //指定元素
            , value: times
            , done: function (value, date) {
                startDates = new Date(Date.parse(wholeStartTime.replace(/-/g, "/")));
                startDates = startDates.getTime();
                endDates = new Date(Date.parse(value.replace(/-/g, "/")));
                endDates = endDates.getTime();
                if (startDates > endDates) {
                    $("#endTime").val(wholeStartTime);
                    wholeEndTime = wholeStartTime;
                    $.MsgBox.Alert("温馨提示", "结束时间不能小于开始时间。");
                } else {
                    $(".contain_main_title .time2").html(value);
                    wholeEndTime = value
                }
            }
        });
    } else {
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
                    $("#endTime").val(wholeStartTime);
                    wholeEndTime = wholeStartTime;
                    $.MsgBox.Alert("温馨提示", "结束时间不能小于开始时间。");
                } else {
                    $(".contain_main_title .time2").html(value);
                    wholeEndTime = value
                }
            }
        });
    }

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
    // console.log(realLength)
    if (realLength > lens * 2) {
        $.MsgBox.Alert("温馨提示", con + lens + "个字以内");
        return false;
    }
    return true;

}


//添加活动
function addActivity(picId, par, assistanceImg, invitatorImg) {
    console.log(par)


    var activity = addActCondition(picId, par, assistanceImg, invitatorImg);

    console.log(activity)
    $.ajax({
        type: "post",
        url: "/mat/activity/updateActivity",
        cache: false,  //禁用缓存
        data: activity,  //传入组装的参数?
        // headers: {"Content-type": "text/plain;charset=utf-8;"},
        dataType: "json",
        success: function (result) {
            // console.log(result)
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
// 添加活动 参数拼接
function addActCondition(picId, par, assistanceImg, invitatorImg) {
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
    var rewardActContentJson = [];
    var contentid1 = $("#currentActId").attr("data-curr-contentid1");
    var contentid2 = $("#currentActId").attr("data-curr-contentid2");
    if (par.assistanceNoReward == 1) { //助力没奖
        var parmas = {
            "commandType": 1,
            remark: par.invitingAwards,
            contentTitle: "任务达成",
            contentHead: "恭喜您获得 " + par.invitingAwards,
            contentFoot: "数量有限，先到先得哦",
            contentBtn: "我也要领奖励"
        };
        if (contentid1) {
            parmas.contentId = contentid1;
        }
        par.assistanceRewardType = 0;
        rewardActContentJson.push(parmas);
    } else if (par.assistanceNoReward == 2) { //助力有奖
        var parmas1 = {
            "commandType": 1,
            remark: par.invitingAwards,
            contentTitle: "任务达成",
            contentHead: "恭喜您获得 " + par.invitingAwards,
            contentFoot: "数量有限，先到先得哦",
            contentBtn: "我也要领奖励"
        };
        var parmas2 = {
            "commandType": 2,
            remark: par.aidReward,
            contentTitle: "为好友助力成功",
            contentHead: "恭喜您获得 " + par.aidReward,
            contentFoot: "数量有限，先到先得哦",
            contentBtn: "我也要领奖励"
        };
        if (contentid1 && contentid2) {
            parmas1.contentId = contentid1;
            parmas2.contentId = contentid2;
        }
        rewardActContentJson.push(parmas1);
        rewardActContentJson.push(parmas2);
    }
    var invitatorRewardJson = {};
    var assistanceRewardJson = {};
    if (par.invitationRewardType == 2) {
        invitatorRewardJson = {
            command: par.invitingAwards
        }
        if (invitationCurrencyId) {
            invitatorRewardJson.currencyId = invitationCurrencyId
        }
    } else if (par.invitationRewardType == 3) {
        invitatorRewardJson = {
            type: 2,
            context: invitatorImg.join(",")
        }

        if (invitationGlobalId) {
            invitatorRewardJson.globalId = invitationGlobalId
        }
    } else if (par.invitationRewardType == 4) {
        invitatorRewardJson = {
            type: 1,
            context: par.invitingAwards
        }
        if (invitationGlobalId) {
            invitatorRewardJson.globalId = invitationGlobalId
        }
    }
    if (par.assistanceRewardType == 2) {
        assistanceRewardJson = {
            command: par.aidReward
        }
        if (assistanceCurrencyId) {
            assistanceRewardJson.currencyId = assistanceCurrencyId
        }
    } else if (par.assistanceRewardType == 3) {
        assistanceRewardJson = {
            type: 2,
            context: assistanceImg.join(",")
        }
        if (assistanceGlobalId) {
            invitatorRewardJson.globalId = assistanceGlobalId
        }
    } else if (par.assistanceRewardType == 4) {
        assistanceRewardJson = {
            type: 1,
            context: par.aidReward
        }
        if (assistanceGlobalId) {
            invitatorRewardJson.globalId = assistanceGlobalId
        }
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
        initiatorRewardType: par.invitationRewardType,
        assistanceRewardType: par.assistanceRewardType,
        initiatorRewardJson: JSON.stringify(invitatorRewardJson),
        assistanceRewardJson: JSON.stringify(assistanceRewardJson)
    };
    if (par.invitationRewardType == 4) {
        activity.initiatorRewardType = 3
    }
    if (par.assistanceRewardType == 4) {
        activity.assistanceRewardType = 3
    }
    var actId = $("#currentActId").attr("data-curr-actid");
    if (actId) {
        activity.actId = actId;
    } else {
        if (par.invitationRewardType == 1) {
            activity.rewardNum = par.invitingAwardsNum
        }
        if (par.assistanceRewardType == 1) {
            activity.assistanceAwardsNum = par.assistanceAwardsNum
        }
    }
    return activity;
}
//修改 条件反补
function reverseSupplement(data) {
    $("#show").attr("src", "");
    $("#invitationImagesShow").html("");
    $("#assistanceImagesShow").html("");
    var activity = data.matActivity;
    var rewardList = data.rewardList;
    assistanceRewardTypes=activity.assistanceRewardType ;
    initiatorRewardTypes = activity.initiatorRewardType;
    // 助力是否有奖反补
    if (activity.assistanceRewardType == 0) {
        $("#assistanceNoReward").trigger("click");
        $(".assistanceNumber").hide();
    } else {
        $("#assistanceExitReward").trigger("click");
        if (activity.assistanceRewardType == 3) {
            if (data.assistanceReward.type == 1) {
                $($("#assistanceRewardType option")[3]).attr("selected", "selected");
                $(".assistanceGroup").hide();
                $(".assistanceReward").show();
                $(".assistanceNumber").hide();
            } else if (data.assistanceReward.type == 2) {
                $($("#assistanceRewardType option")[activity.assistanceRewardType - 1]).attr("selected", "selected");
                $(".assistanceGroup").show();
                $(".assistanceReward").hide();
                if (data.assistanceRewardPicture.length) {
                    $("#assistanceImagesShow").show();
                    for (var i = 0; i < data.assistanceRewardPicture.length; i++) {
                        // var filePath = "https://cm-service.easy7share.com" + data.assistanceRewardPicture[i];
                        var filePath = "http://localhost:8083" + data.assistanceRewardPicture[i];

                        convertImgToBase64(filePath, function (base64Img) {
                            //转化后的base64
                            var img = '<p class="newImages"><img src="' + base64Img + '" onclick="modifyCurrentImg(this,' + "modifyCurrentImgs" + ')"><span onclick="deleteImage(this,' + "assistanceFile" + ')">✖</span></p>';
                            $("#assistanceImagesShow").append(img);
                        });
                    }
                }
            }
            assistanceGlobalId = data.assistanceReward.globalId;
        } else {
            $($("#assistanceRewardType option")[activity.assistanceRewardType - 1]).attr("selected", "selected");
            $(".assistanceReward").show();
            $(".assistanceNumber").hide();
            $(".assistanceGroup").hide();
            if (activity.assistanceRewardType == 2) {
                assistanceCurrencyId = data.assistanceReward.currencyId;
            }
        }
    }

    // 邀请奖励反补
    if (activity.initiatorRewardType == 3) {
        if (data.initiatorReward.type == 1) {
            $($("#invitationRewardType option")[3]).attr("selected", "selected");
            $(".invitationGroup").hide();
            $(".invitationReward").show();
            $(".invitationNumber").hide();
        } else if (data.initiatorReward.type == 2) {
            $($("#invitationRewardType option")[activity.initiatorRewardType - 1]).attr("selected", "selected");
            $(".invitationGroup").show();
            $(".invitationReward").hide();
            if (data.initiatorRewardPicture.length) {
                $("#invitationImagesShow").show();
                for (var i = 0; i < data.initiatorRewardPicture.length; i++) {
                    // var filePath = "https://cm-service.easy7share.com" +data.initiatorRewardPicture[i];
                    var filePath = "http://localhost:8083" + data.initiatorRewardPicture[i];
                    convertImgToBase64(filePath, function (base64Img) {
                        //转化后的base64
                        var img = '<p class="newImages"><img src="' + base64Img + '" onclick="modifyCurrentImg(this,' + "modifyCurrentImgs" + ')"><span onclick="deleteImage(this,' + "invitationFile" + ')">✖</span></p>';
                        $("#invitationImagesShow").append(img);
                    });
                }
            }
        }
        invitationGlobalId = data.initiatorReward.globalId;
    } else {
        $($("#invitationRewardType option")[activity.initiatorRewardType - 1]).attr("selected", "selected");
        $(".invitationReward").show();
        $(".invitationNumber").hide();
        $(".invitationGroup").hide();
        if (activity.initiatorRewardType == 2) {
            invitationCurrencyId = data.initiatorReward.currencyId;
        }
    }
    $("#formsearch input[name='invitingButton']").val(data.btnMap.bxt_invite);
    $("#formsearch input[name='storeTitle']").val(activity.actTitle);
    $("#formsearch input[name='posterCopywriting']").val(activity.actShareCopywriting);
    $("#formsearch textarea[name='activityRules']").val(activity.actRule);
    $("#formsearch input[name='startTimes']").val(myDates(activity.startAt));
    $("#formsearch input[name='endTimes']").val(myDates(activity.endAt));
    $("#formsearch input[name='helpNumber']").val(activity.partakeNum);
    $("#formsearch input[name='shareTitle']").val(activity.actShareTitle);
    $("#formsearch input[name='invitingAwardsNum']").parents("p").hide();
    // var myDate = new Date();
    wholeStartTime = myDates(activity.startAt);
    wholeEndTime = myDates(activity.endAt);
    laydateTime(myDates(activity.startAt), myDates(activity.endAt));
    for (var i = 0; i < rewardList.length; i++) {
        if (rewardList[i].commandType == 1) {
            $("#currentActId").attr("data-curr-contentid1", rewardList[i].contentId);
            $("#formsearch input[name='invitingAwards']").val(rewardList[i].remark);
        } else if (rewardList[i].commandType == 2) {
            $("#currentActId").attr("data-curr-contentid2", rewardList[i].contentId);
            $("#formsearch input[name='aidReward']").val(rewardList[i].remark);
        }
    }
    if (activity.filePath) {
        $("#show").parent().show();
        // var filePath = "https://cm-service.easy7share.com" + activity.filePath;
        var filePath = "http://localhost:8083" + activity.filePath;
        convertImgToBase64(filePath, function (base64Img) {
            //转化后的base64
            $("#show").attr("src", base64Img);
        });
    }
}

//校验字段
function checkField(dataField) {
    var flag = judgmenLength(dataField.storeTitle, 10, "活动标题");
    var flag1 = judgmenLength(dataField.invitingButton, 18, "邀请按钮文案");
    var flag2 = judgmenLength(dataField.posterCopywriting, 40, "活动玩法说明");
    var flag3 = judgmenLength(dataField.shareTitle, 26, "分享标题");
    if (flag && flag1 && flag2 && flag3) {
        return true;
    } else {
        return false;
    }
}


function keydowns(e, tar) {
    if (e.keyCode == 13) {
        fieldLength(tar)
    }
}


