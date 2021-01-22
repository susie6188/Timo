layui.use('table', function(){
    var table = layui.table;

    table.render({
        elem: '#test'
        ,url:'/protectArea/json/levelData'
        ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        ,cols: [[
            {field:'category', width:80, title: 'category'}
            ,{field:'area', width:80, title: 'area'}

        ]]
    });


    table.render({
        elem: '#table2'
        ,url:'/protectArea/json/levelData'
        ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        ,cols: [[
            {field:'category', width:80, title: 'category'}
            ,{field:'area', width:80, title: 'area'}

        ]]
    });


});