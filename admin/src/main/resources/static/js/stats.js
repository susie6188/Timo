layui.config({
    base: '/lib/echarts-v4.9.0/'
}).use(['layer','element','echarts'],function(){
    var element = layui.element //元素操作
        ,$=layui.jquery
        ,echarts = layui.echarts;
    var myChart = echarts.init(document.getElementById('echartZhu'));
    var option = {
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
                color: 'rgba(220, 220, 220, 0.8)'
            }
        }]
    };
    myChart.setOption(option,true);
})


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

