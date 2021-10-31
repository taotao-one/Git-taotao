window.onload = function () {
    // var entrepots = [[${beans}]];
    // for (var i =0 ;i<entrepots.length;i++){
    //     var entrepot = entrepots[i];
    //     document.createElement("tr")
    // }

    // var downBtn = document.getElementById("downBtn");
    // downBtn.onclick = function () {
    //     var page = document.getElementById("page");
    //     var $page = page.value;
    //
    //     var rows = document.getElementById("rows");
    //     var $rows = rows.value;
    //
    //     var name = document.getElementById("name");
    //     var $name = name.value;
    //
    //     var flag = document.getElementById("flag");
    //     var $flag = flag.value;
    //
    //     window.location = "down?"+"page="+$page+"&rows="+$rows+"&name="+$name+"&flag="+$flag;
    //
    // }

    var now = document.getElementById("now");
    now.onclick = function () {
        document.getElementById("flag").value = 1;
        query();
    }

    var old = document.getElementById("old");
    old.onclick = function () {
        document.getElementById("flag").value = 2;
        query();
    }

//------------------------------------------------------------------------------------------------------------------------------
    var queryBtn = document.getElementById('queryBtn');

    queryBtn.onclick = function () {
        document.getElementById("flag").value = 0;
        query();
    };
//------------------------------------------------------------------------------------------------------------------------------
    var clearBtn = document.getElementById("clearBtn");

    clearBtn.onclick = function () {
        document.getElementById("flag").value = 0;
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

    var flag = document.getElementById("flag");
    var $flag = flag.value;

    var list = {page:$page,rows:$rows,name:$name,flag:$flag};

    $.ajax({
        type: "post",
        url: "recordQuery",
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
            var id = document.createElement('td');
            tr.appendChild(id);
            id.innerHTML=entrepot.id;

            var name = document.createElement('td');
            tr.appendChild(name);
            name.innerHTML=entrepot.name;

            var coding = document.createElement('td');
            tr.appendChild(coding);
            coding.innerHTML=entrepot.coding;

            var detailedname = document.createElement('td');
            tr.appendChild(detailedname);
            detailedname.innerHTML=entrepot.detailedname;

            var barcode = document.createElement('td');
            tr.appendChild(barcode);
            barcode.innerHTML=entrepot.barcode;

            var quantity = document.createElement('td');
            tr.appendChild(quantity);
            quantity.innerHTML=entrepot.quantity;

            var alterTime = document.createElement('td');
            tr.appendChild(alterTime);
            alterTime.innerHTML=entrepot.alterTime;


        }


    }

}