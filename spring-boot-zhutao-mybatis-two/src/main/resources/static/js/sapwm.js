window.onload = function () {
    // var entrepots = [[${beans}]];
    // for (var i =0 ;i<entrepots.length;i++){
    //     var entrepot = entrepots[i];
    //     document.createElement("tr")
    // }
    var box = document.getElementById("box");
    box.onclick = function () {
        boxOr(box.checked);
    }

//------------------------------------------------------------------------------------------------------------------------------
    var queryBtn = document.getElementById('queryBtn');

    queryBtn.onclick = function () {
        query();
    };
//------------------------------------------------------------------------------------------------------------------------------
    var clearBtn = document.getElementById("clearBtn");

    clearBtn.onclick = function () {
        document.getElementById("name").value='';
        query();
    };
//------------------------------------------------------------------------------------------------------------------------------
    var firstBtn = document.getElementById("firstBtn");

    firstBtn.onclick = function () {
        document.getElementById("page").value=1;

        query();
    };
//------------------------------------------------------------------------------------------------------------------------------
    var prevBtn = document.getElementById("prevBtn");

    prevBtn.onclick = function () {
        var page = document.getElementById("page");
        var $page = parseInt(page.value);
        if($page==1){
            alert("已经是首页了");
            return;
        }else {
            page.value = $page-1;

            query();
        }
    };
//------------------------------------------------------------------------------------------------------------------------------
    var nextBtn = document.getElementById("nextBtn");

    nextBtn.onclick = function () {
        var page = document.getElementById("page");
        var $page = parseInt(page.value);
        var max = document.getElementById("max");
        var $max = parseInt(max.innerHTML);

        if($page==$max){
            alert("已经是最后一页");
            return;
        }else {
            page.value = $page+1;

            query();
        }



    };
//------------------------------------------------------------------------------------------------------------------------------
    var endBtn = document.getElementById("endBtn");

    endBtn.onclick = function () {
        var max = document.getElementById("max");
        var $max = parseInt(max.innerHTML);
        document.getElementById("page").value=$max;
        query();

    };
//------------------------------------------------------------------------------------------------------------------------------
    var page = document.getElementById("page");
    page.onkeydown = function (ev) {
        var e = event || ev;
        if(e.keyCode == 13){
            query();
        }
    }
//------------------------------------------------------------------------------------------------------------------------------
    var rows = document.getElementById("rows");
    rows.onkeydown = function (ev) {
        var e = event || ev;
        if(e.keyCode == 13){
            query();
        }
    }
//------------------------------------------------------------------------------------------------------------------------------
    var name = document.getElementById("name");
    name.onkeydown = function (ev) {
        var e = event || ev;
        if(e.keyCode == 13){
            query();
        }
    }
//------------------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------------------------------------


    query();

};

function query() {

    var page = document.getElementById("page");
    var $page = page.value;

    var rows = document.getElementById("rows");
    var $rows = rows.value;

    var name = document.getElementById("name");
    var $name = name.value;

    var list = {page:$page,rows:$rows,name:$name};

    $.ajax({
        type: "post",
        url: "query",
        data:   list,
        success: function(result) {
            doBack(result);
        },

    });

    function doBack(result) {
        var tableBody = document.getElementById("tableBody");
        tableBody.innerHTML='';



        var max = result.max;
        console.log(max);
        document.getElementById("max").innerHTML = max;

        var entrepost = result.list;

        for(var i = 0;i<entrepost.length;i++){
            var entrepot = entrepost[i];
            //循环导入数据
            var tr = document.createElement('tr');
            //装入tbody里面
            tableBody.appendChild(tr);
            var box = document.createElement("td");
            tr.appendChild(box);
            box.innerHTML = "<input type='checkbox' class='valueBox' value='"+entrepot.id+"'>";

            var id = document.createElement('td');
            tr.appendChild(id);
            id.innerHTML=entrepot.id;

            var name = document.createElement('td');
            tr.appendChild(name);
            name.innerHTML=entrepot.name;

            var artNo = document.createElement('td');
            tr.appendChild(artNo);
            artNo.innerHTML=entrepot.artNo;

            var quantity = document.createElement('td');
            tr.appendChild(quantity);
            quantity.innerHTML=entrepot.quantity;

            var runTime = document.createElement('td');
            tr.appendChild(runTime);
            runTime.innerHTML=entrepot.runTime;

            var flag = document.createElement('td');
            tr.appendChild(flag);
            flag.innerHTML=entrepot.flag;

            var alert = document.createElement('td');
            tr.appendChild(alert);
            alert.innerHTML="<a href=\"#\">编辑</a> <a href=\"#\">删除</a>";


        }


    }
}
function boxOr(a) {
    var boxs = document.getElementsByClassName("valueBox")
    for(var i =0;i<boxs.length;i++){
        boxs[i].checked = a;
    }
}