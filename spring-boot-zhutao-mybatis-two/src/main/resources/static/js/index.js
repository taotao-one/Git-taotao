

window.onload = function () {
    var login = document.getElementById("login");
    login.onclick =function () {
        var name = document.getElementById("names");
        var password = document.getElementById("passwords");
        console.log(name.value);
        console.log(password.value);
        if("账号" == name.value||password.value=="******"){
            alert("请输入账号或密码");
        }else {
            var xml = new XMLHttpRequest();

            xml.open("GET","toIndex"+"?name="+name.value+"&"+"password="+password.value,true);

            function doBack(result){
                if(result=="1"){
                    //登录成功
                    alert("登录成功");
                    location.href="main";
                }else {
                    //登录失败
                    alert("账号或密码错误");

                }
            }

            xml.onreadystatechange = function () {
                if(xml.readyState == 4&&xml.status == 200){

                    doBack(xml.responseText);

                }
            }

            xml.send();

        }
    }

};