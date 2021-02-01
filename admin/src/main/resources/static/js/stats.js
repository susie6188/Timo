layui.config({
    base: '/lib/echarts-v4.9.0/'
}).use(['layer','element','echarts','table'],function(){
    var element = layui.element //元素操作
        ,$=layui.jquery
        ,echarts = layui.echarts
        ,table = layui.table;
    var colors = ['#5470C6', '#91CC75', '#EE6666'];
    var provinceChart = echarts.init(document.getElementById('provinceChart'));
    var protectedObjectsChart = echarts.init(document.getElementById('protectedObjectsChart'));
    var myTable = layui.table;

    //第一个实例
    var myTableOption = {
        elem: '#table'
        ,url: ''
        ,page: false
        ,totalRow: true
        ,toolbar: true
        ,cols: [[
            {type: 'numbers', title: '序号', totalRowText: '合计'}
            ,{field: 'name', title: '名称'}
            ,{field: 'level', title: '级别'}
            ,{field: 'category', title: '类型'}
            ,{field: 'province', title: '所在省'}
            ,{field: 'city', title: '所在市'}
            ,{field: 'county', title: '所在县'}
            ,{field: 'protectedObjects', title: '主要保护对象'}
            ,{field: 'currentArea', title: '现状面积(平方公里)', totalRow: true, templet: function(d){
                    return Number(d.currentArea).toFixed(2);
                }}
        ]]
    };

    $("#query").click(function(){
        var regionType = $("#regionType").val();
        var province = $("#province").val();
        var city = $("#city").val();
        var county = $("#county").val();

        var topic = $("#topic").val();
        var subTopic = $("#subTopic").val();
        var protectedObjects =  $("#protectedObjects").val();
        var startYear = $("#startYear").val();
        var endYear = $("#endYear").val();
        var titleRegion = "";
        var titleYear = "";

        if(endYear > startYear){
            return;
        }
        else{
            if(endYear == startYear){
                titleYear = startYear + "年";
            }
            else{
                titleYear = startYear + "-" + endYear + "年";
            }
        }

        if("district" == regionType){
            if(province == null || province.length == 0){
                titleRegion = "全国";
                // return;
            }
            else{
                titleRegion = province + city + county;
            }
        }
        else if("topic" == regionType){
            if(topic == null || topic.length == 0){
                return;
            }
        }

        $.ajax({
            type: "GET",
            url: "/protectArea/json/query4Chart",
            data: {
                regionType: regionType,
                province: province,
                city: city,
                county: county,
                topic: topic,
                subTopic: subTopic,
                protectedObjects: protectedObjects,
                startYear: startYear,
                endYear: endYear
            },
            success: function (data) {
                var provinceData = data.provinceData;
                var provinceXData = new Array();
                var provinceCountData = new Array();
                var provinceAreaData = new Array();

                for(var i=0; i<provinceData.length; i++){
                    provinceXData.push(provinceData[i].province);
                    provinceCountData.push(provinceData[i].count);
                    provinceAreaData.push(provinceData[i].area);
                }

                var provinceChartOption = {
                    title: {
                        text: titleYear + titleRegion + '自然保护地',
                        subtext: '按行政区统计',
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        },
                        formatter:function(params){
                            var res = params[0].name;
                            res += "<br>"+params[0].marker+params[0].seriesName+"："+ params[0].data;
                            res += "<br>"+params[1].marker+params[1].seriesName+"："+ params[1].data.toFixed(2);

                            // for (var i = 0; i < params.length; i++) {
                            //     res += "<br>"+params[i].marker+params[i].seriesName+"："+ params[i].data.toFixed(2);
                            // }
                            return res;
                        }
                    },
                    legend: {
                        data: ['数量', '面积']
                    },
                    xAxis: {
                        type: 'category',
                        data: provinceXData
                    },
                    yAxis: [
                        {
                            type: 'value',
                                name: '数量',
                            position: 'left',
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: colors[0]
                                }
                            },
                            axisLabel: {
                                formatter: '{value}个'
                            }
                        },
                        {
                            type: 'value',
                            name: '面积',
                            position: 'right',
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: colors[1]
                                }
                            },
                            axisLabel: {
                                formatter: '{value}km2'
                            }
                        }
                    ],
                    series: [{
                            name: '数量',
                            data: provinceCountData,
                            yAxisIndex: 0,
                            barGap: 0,
                            color: colors[0],
                            type: 'bar'
                        },
                        {
                            name: '面积',
                            data: provinceAreaData,
                            yAxisIndex: 1,
                            color: colors[1],
                            type: 'bar'
                    }]
                };
                provinceChart.setOption(provinceChartOption, true);

                var protectedObjectsData = data.protectedObjectsData;
                var protectedObjectsXData = new Array();
                var protectedObjectsCountData = new Array();
                var protectedObjectsAreaData = new Array();

                for(var i=0; i<protectedObjectsData.length; i++){
                    protectedObjectsXData.push(protectedObjectsData[i].protectedObjects);
                    protectedObjectsCountData.push(protectedObjectsData[i].count);
                    protectedObjectsAreaData.push(protectedObjectsData[i].area);
                }

                var protectedObjectsChartOption = {
                    title: {
                        text: titleYear + titleRegion + '自然保护地',
                        subtext: '按保护对象统计',
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        },
                        formatter:function(params){
                            var res = params[0].name;
                            res += "<br>"+params[0].marker+params[0].seriesName+"："+ params[0].data;
                            res += "<br>"+params[1].marker+params[1].seriesName+"："+ params[1].data.toFixed(2);

                            // for (var i = 0; i < params.length; i++) {
                            //     res += "<br>"+params[i].marker+params[i].seriesName+"："+ params[i].data.toFixed(2);
                            // }
                            return res;
                        }
                    },
                    legend: {
                        data: ['数量', '面积']
                    },
                    xAxis: {
                        type: 'category',
                        data: protectedObjectsXData
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name: '数量',
                            position: 'left',
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: colors[0]
                                }
                            },
                            axisLabel: {
                                formatter: '{value}个'
                            }
                        },
                        {
                            type: 'value',
                            name: '面积',
                            position: 'right',
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: colors[1]
                                }
                            },
                            axisLabel: {
                                formatter: '{value}km2'
                            }
                        }
                    ],
                    series: [{
                        name: '数量',
                        data: protectedObjectsCountData,
                        yAxisIndex: 0,
                        barGap: 0,
                        color: colors[0],
                        type: 'bar'
                    },
                    {
                        name: '面积',
                        data: protectedObjectsAreaData,
                        yAxisIndex: 1,
                        color: colors[1],
                        type: 'bar'
                    }]
                };
                protectedObjectsChart.setOption(protectedObjectsChartOption, true);
            }
        });

        myTableOption.url="/protectArea/json/query4Table?" +
            "regionType=" + regionType +
            "&province=" + province +
            "&city=" + city +
            "&county=" + county +
            "&topic=" + topic +
            "&subTopic=" + subTopic +
            "&protectedObjects=" + protectedObjects +
            "&startYear=" + startYear +
            "&endYear=" + endYear;
        myTable.render(myTableOption);

    });

    $("#regionType").change(function(){
        var regionType = $("#regionType").val();
        if(regionType == "district"){
            $("#regionDistrict").show();
            $("#regionTopic").hide();
        }
        else if(regionType == "topic"){
            $("#regionDistrict").hide();
            $("#regionTopic").show();
        }
    });

    $("#province").change(function(){
        var provinceCode = $("#province").val();
        $("#city").empty();
        $("#county").empty();
        $.ajax({
            type: "GET",
            url: "/adcode/json/cities",
            data: {provinceCode: provinceCode},
            success: function (data) {
                $("#city").append("<option value=''></option>");
                $("#county").append("<option value=''></option>");
                for(var i=0; i<data.length; i++){
                    $("#city").append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");
                }
                // $("#city").get(0).selectedIndex = -1;
            }
        });
    });

    $("#city").change(function(){
        var cityCode = $("#city").val();
        $("#county").empty();
        $.ajax({
            type: "GET",
            url: "/adcode/json/counties",
            data: {cityCode: cityCode},
            success: function (data) {
                $("#county").append("<option value=''></option>");
                for(var i=0; i<data.length; i++){
                    $("#county").append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");
                }
                // $("#county").get(0).selectedIndex = -1;
            }
        });
    });

    $("#topic").change(function(){
        var topic = $("#topic").val();
        $("#subTopic").empty();
        $.ajax({
            type: "GET",
            url: "/topic/json/subTopics",
            method: "get",
            data: {topic: topic},
            success: function (data) {
                $("#subTopic").append("<option value=''></option>");
                for(var i=0; i<data.length; i++){
                    $("#subTopic").append("<option value='" + data[i].name + "'>" + data[i].name + "</option>");
                }
                // $("#subTopic").get(0).selectedIndex = -1;
            }
        });
    });

    loadProvinces();
    loadTopics();
})

function loadProvinces(){
    $.ajax({
        type: "GET",
        url: "/adcode/json/provinces",
        data: {},
        success: function (data) {
            $("#province").append("<option value=''></option>");
            for(var i=0; i<data.length; i++){
                $("#province").append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");
            }
            // $("#province").get(0).selectedIndex = -1;
        }
    });

}

function loadTopics(){
    $.ajax({
        type: "GET",
        url: "/topic/json/topics",
        data: {},
        success: function (data) {
            $("#topic").append("<option value=''></option>");
            for(var i=0; i<data.length; i++){
                $("#topic").append("<option value='" + data[i].name + "'>" + data[i].name + "</option>");
            }
            // $("#topic").get(0).selectedIndex = -1;
        }
    });
}

//
// $(function(){
//     $("#query").click(function(){
//         alert();
//     });
// });

function showWeight() {
    $.ajax({
        type: "GET",
        url: "protectArea/json/levelData",
        data: {},
        success: function (r) {
            //console.log(r)
            initChart()
            //option.title.text = r.username+"的体重"
            option.legend.data[0] = r.username
            option.series[0] = {"name": r.username, "type": "line", "data": []}
            //option.series[0].name = r.username
            //option.series[0].type = 'line'
            for (var i in r.weights) {
                option.series[0].data[i] = r.weights[i].weight
                option.xAxis[0].data[i] = r.weights[i].wdate
            }
            //option.series[1] = {"name": r.username, "type": "line", "data": [1,2,8,12,21,9]}
            myChart.setOption(option)
        }
    });
}

function initChart() {
    option = {
        title: {
            text: "体重趋势表",
            x: "center"
        },
        tooltip: {
            trigger: "item",
            formatter: "{a} <br/>{b} : {c}kg"
        },
        legend: {
            x: 'left',
            data: []
        },
        xAxis: [
            {
                type: "category",
                name: "日期",
                splitLine: {show: false},
                data: []
            }
        ],
        yAxis: [
            {
                type: "value",
                axisLabel: {
                    formatter: '{value} kg'
                },
                name: "体重",
                max: 90,
                min: 50,
                splitNumber: 5
            }
        ],
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: true
                },
                dataView: {
                    show: true,
                    readOnly: true
                },
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        calculable: true,
        series: []
    };
}

