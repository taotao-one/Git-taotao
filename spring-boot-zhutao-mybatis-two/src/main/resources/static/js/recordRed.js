window.onload = function () {
    // var entrepots = [[${beans}]];
    // for (var i =0 ;i<entrepots.length;i++){
    //     var entrepot = entrepots[i];
    //     document.createElement("tr")
    // }

    var saves = document.getElementById("saves");
    saves.onclick = function () {
        savesOrDlts("save");
    }

    var dlts = document.getElementById("dlts");
    dlts.onclick = function () {
        savesOrDlts("dlt");
    }





    var box = document.getElementById("box");
    box.onclick = function () {
        boxOr(box.checked);
    };

    var impl = document.getElementById("impl");
    impl.onclick = function () {
        window.location = "impl";
    };

    var downBtn = document.getElementById("downBtn");
    downBtn.onclick = function () {
        var page = document.getElementById("page");
        var $page = page.value;

        var rows = document.getElementById("rows");
        var $rows = rows.value;

        var name = document.getElementById("name");
        var $name = name.value;

        var flag = document.getElementById("flag");
        var $flag = flag.value;

        window.location = "down?"+"page="+$page+"&rows="+$rows+"&name="+$name+"&flag="+$flag;

    }

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

    var saves = document.getElementsByName("save");
    console.log(saves);
    console.log(saves.length)
    for(var i =0;i<saves.length;i++){
        console.log(saves[i])
    }
    console.log(saves);
    console.log(saves.length)

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
        url: "recordRedQuery",
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

            var alert = document.createElement('td');
            tr.appendChild(alert);
            alert.innerHTML = "<a href=\"#\" name='save' class='btn btn-success' id='"+entrepot.id+"'>保存</a><a href=\"#\" name='dlt' class='btn btn-danger' id='"+entrepot.id+"'>删除</a>";



            // var save = document.createElement("a");
            // alert.appendChild(save);
            // save.href="save?id="+entrepot.id;
            // save.className = "btn btn-success";
            // save.innerHTML = "保存";
            //
            // var dlt = document.createElement("a");
            // alert.appendChild(dlt);
            // dlt.href="delete?id="+entrepot.id;
            // dlt.className = "btn btn-danger";
            // dlt.innerHTML = "删除";

        }

        var saves = document.getElementsByName("save");
        for(var i = 0;i<saves.length;i++){
            saves[i].onclick = function () {
                saveOr(this.id,"save");
            }
        }
        var dlts = document.getElementsByName("dlt");
        for(var i = 0;i<dlts.length;i++){
            dlts[i].onclick = function () {
                saveOr(this.id,"dlt");
            }
        }

    }

}

function boxOr(a) {
    var boxs = document.getElementsByClassName("valueBox")
    for(var i =0;i<boxs.length;i++){
        boxs[i].checked = a;
    }
}

function saveOr(id,flag) {
    var dt = {id:id,flag:flag}
    $.ajax({
        type: "post",
        url: "saveOrDlt",
        data: dt,
        success:function (result) {
            saveOrDlt(result)
        }
    })
}
function saveOrDlt(result) {
    alert(result);
    query();
}

function savesOrDlts(flag) {
    var boxs = document.getElementsByClassName("valueBox")
    var str ='';
    for(var i =0;i<boxs.length;i++){

        if(boxs[i].checked){
            str +=boxs[i].value+",";
        }
    }
    if(str==''){
        alert("至少有一项被选中")
    }else {
        $.ajax({
            type:"post",
            url:"savesOrDlts",
            data:{str:str,flag:flag},
            success:function (result) {
                alert(result)
                query();
            }
        })
    }


}