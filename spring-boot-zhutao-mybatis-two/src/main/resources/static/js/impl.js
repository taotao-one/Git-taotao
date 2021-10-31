window.onload = function () {
    var saveBtn = document.getElementById("saveBtn");
    saveBtn.onclick = function () {
        saves();
    };
    function saves() {
        var xhr = new XMLHttpRequest();

        xhr.open("post",'impl.do',true);

        xhr.onreadystatechange = function () {
            if(xhr.readyState==4&&xhr.status==200){
                doBack(xhr.responseText);
            }

        };
        function doBack(result) {
            window.location="recordRed";
        }

        var data = new FormData(document.getElementById("usersForm"));
        xhr.send(data);
        document.getElementById('progress').style.display='block';
    }

}