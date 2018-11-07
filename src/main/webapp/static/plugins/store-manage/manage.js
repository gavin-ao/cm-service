/**
 * Created by 12045 on 2018/10/22.
 */

(function () {
    // 退出登录
    // $("#loginOut").on("click", function (){
    //     console.log(6645346)
    //     loginOut();
    // });
    // 添加账号
    $("#addAccount").off("click");
    $("#addAccount").on("click", function () {
        $("#manageAdd").show();
        // $("#storeQRCode").hide();

        $('#distpicker').distpicker({
            province: '---- 所在省 ----',
            city: '---- 所在市 ----',
            district: '---- 所在区 ----',
            autoSelect: false
        });
        $("#formsearch>div>input").each(function () {
            $(this).val("")
        })
        $("#formsearch>div>textarea").each(function () {
            $(this).val("")
        })

    });
    $("#closeBtn").off("click");
    $("#closeBtn").on("click", function () {
        console.log(44444)
        $("#manageAdd").hide();
    });
    $("#closeBtn1").off("click");
    $("#closeBtn1").on("click", function () {
        console.log(44444)
        $("#manageAdd").hide();
    });
    // 提交用户信息
    $("#submitBtn").off("click");
    $("#submitBtn").on("click", function () {
        $("#manageAdd").hide();
        var params = decodeURIComponent($("#formsearch").serialize())
        var dataPars = params.split("&");
        var dataObjArr = {}, dataArr = [];
        var table = $('#example').DataTable();
        var rowData = table.row(0).data();
        for (var i = 0; i < rowData.length; i++) {
            dataArr.push("");
        }
        for (var i = 0; i < dataPars.length; i++) {
            var par = dataPars[i].split("=");
            dataObjArr[par[0]] = par[1];
            dataArr[i] = par[1];
        }
        console.log(dataObjArr)
        // var rowData = table.row(0).data(dataArr);
        // console.log(table.api())
        table.draw(false)
        // var currentPage=table.api().page();
        // table.api().page(currentPage).draw( 'page' );
    });
    $(document).ready(function () {
        $('#example').dataTable({
            searching: false, //去掉搜索框方法一：百度上的方法，但是我用这没管用
            bLengthChange: false,   //去掉每页显示多少条数据方法
            processing: true,  //隐藏加载提示,自行处理
            serverSide: true,  //启用服务器端分页
            ordering: true,
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
                var param = {
                    "search": {
                        "cdt": {
                            "op": "and",
                            "flts": [
                                {
                                    "op": "range",
                                    "min": 1507601410000,
                                    "max": 1539137410000,
                                    "lf": "created_at"
                                }
                            ]
                        },
                        "fds": [
                            {
                                "fn": "screen_name"
                            },
                            {
                                "fn": "post_title"
                            },
                            {
                                "fn": "text"
                            },
                            {
                                "fn": "page_url"
                            },
                            {
                                "fn": "platform"
                            },
                            {
                                "fn": "created_at"
                            },
                            {
                                "fn": "reposts_count"
                            },
                            {
                                "fn": "comments_count"
                            },
                            {
                                "fn": "praises_count"
                            },
                            {
                                "fn": "interaction_count"
                            },
                            {
                                "fn": "read_count"
                            },
                            {
                                "fn": "brand_machine"
                            },
                            {
                                "fn": "brand_artificial"
                            },
                            {
                                "fn": "topic_artificial"
                            },
                            {
                                "fn": "topic_machine"
                            },
                            {
                                "fn": "article_emotion_machine"
                            },
                            {
                                "fn": "article_emotion_artificial"
                            }
                        ],
                        "pg": {
                            "sta": data.start,
                            "lmt": data.length
                        },
                        "sorts": [
                            {
                                "fn": "created_at",
                                "ord": "desc"
                            }
                        ]
                    },
                    "tableName": 'siemens'
                };
                downloaddata = param
                param = {
                    "pageNo": data.draw,
                    "pageSize": data.length
                }
                // console.log(JSON.stringify(condition));
                $.ajax({
                    type: "post",
                    url: "/system/store/findStorePage",
                    cache: false,  //禁用缓存
                    data: JSON.stringify(param),  //传入组装的参数?
                    headers: {"Content-type": "text/plain;charset=utf-8"},
                    dataType: "json",
                    success: function (result) {
                        console.log(result)
                        if (result.success) {
                            downloaddata.search.pg.lmt = result.page.pageBean.totalNum;
                            var arry = ["storeId", "createAt", "appInfoId", "storeName", "manager", "storeAddr"];
                            var tabledata = [];
                            for (var i = 0; i < result.page.result.length; i++) {
                                result.page.result[i]["cid"] = data.start + i + 1;
                                result.page.result[i]["createAt"] = timestampToTime(result.page.result[i]["createAt"] / 1000);
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
                        }
                        // downloaddata.search.pg.lmt = result.hit;
                    }
                });
            },
            aoColumns: [
                {"data": "storeId", "sClass": "hidden"},
                {"data": ""},
                {"data": "cid"},
                {"data": "storeName"},
                {"data": "storeAddr"},
                {"data": "storeName"},
                {"data": "manager"},
                {"data": "appInfoId"},
                {"data": "createAt"}
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
                    "aTargets": [8],
                    "mRender": function (data, type, full, meta) {
                        // console.log(meta);
                        // console.log(data);
                        return "<button class='modify_btn btn btn-primary' style='padding: 2px 4px;margin-left: 8px;' >修改</button><button class='see_btn btn btn-primary' style='padding: 2px 4px;margin-left: 8px;' >查看</button>";
                    }
                },
                {
                    "bSortable": false,
                    "aTargets": [1, 2, 3, 4, 5, 6, 7, 8]
                },

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
            var storeId = $(this).parents('tr').find("td")[0].innerHTML.trim();
            console.log($(this).parents('tr').find("td")[0].innerHTML.trim())
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
                    if(result.success){
                        if(result.store){

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
                    if(result.success){

                    }
                }
            })
            $("#manageAdd").show();

        });
    });

    $("#downloadQRCode").off("click");
    $("#downloadQRCode").on("click", function () {
        downloadImg();
    })

}());

//判断内容是否为空
function exitNameNull(tar) {
    console.log($(tar).val())
    console.log($(tar).attr("name"))
    var titleName = $.trim($(tar).attr("name"));
    var titleValue = $.trim($(tar).val());
    switch (titleName) {
        case 'storeUser':
            var flag = requirements(titleValue);
            if (flag) {
                exitName(tar);
            }
            break;
        case 'storePassword':
            requirements(titleValue);
            break;
        case 'storePrice':
            requirements(titleValue);
            break;
        case 'storeTime':
            requirements(titleValue);
            break;
        case 'storeLate':
            requirements(titleValue);
            break;
        case 'detailAddress':
            requirements(titleValue);
            break;
    }
    return false;
}

function exitName(tar) {
    console.log($(tar).val())
    return false;
}

//判断内容 是否符合要求
function requirements(text) {
    if (text == "") {
        $.MsgBox.Alert("温馨提示", "内容不能为空。");
        return false;
    }
    return true;
}


function returnIsNotInArray(arr, obj) {
    var arry = [];
    // var arrName = ["read_count","praises_count","comments_count","reposts_count"];
    for (var key in obj) {
        if (key == "text") {
            obj[key] = obj[key] + "..."
        } else if (obj[key] === "") {
            obj[key] = "--";
        }
        arry.push(key);
    }
    ;
    for (var i = 0; i < arr.length; i++) {
        if (!isInArray(arry, arr[i])) {
            // if(contains(arrName,arr[i])){
            //     obj[arr[i]]=0;
            // }else{
            obj[arr[i]] = "--";
            // }

        }
    }
    return obj;
}

function isInArray(arr, value) {
    for (var i = 0; i < arr.length; i++) {
        if (value === arr[i]) {
            return true;
        }
    }
    return false;
}

//下载二维码
function downloadImg() {
    var img = document.getElementById('QRCodeImg');   // 获取要下载的图片
    var url = img.src;                              // 获取图片地址
    var a = document.createElement('a');            // 创建一个a节点插入的document
    var event = new MouseEvent('click')             // 模拟鼠标click点击事件
    a.download = 'storeQRCode'                    // 设置a节点的download属性值
    a.href = url;                                   // 将图片的src赋值给a节点的href
    a.dispatchEvent(event)                          // 触发鼠标点击事件
}
