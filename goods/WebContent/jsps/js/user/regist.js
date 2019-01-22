/**
 * 
 */
//这样定义的函数是页面加载完执行
$(function(){
	/*
	 * 得到每一个错误信息，遍历，如果有就显示，如果没有 不显示
	 */
	$(".labelError").each(function(){
		showError($(this));
	});
	/*
	 * 鼠标到注册按钮上则切换图片功能
	 */
	$("#submit").hover(
			function(){
				$("#submit").attr("src", "/goods/images/regist2.jpg");
			},
			function(){
				$("#submit").attr("src", "/goods/images/regist1.jpg");
			}
	);
	
	$("#registform").submit(function(){
		$("#msg").text("");
		var bool = true;
		$(".input").each(function() {
			var inputName = $(this).attr("name");
			if(!invokeValidateFunction(inputName)) {
				bool = false;
			}
		});
		return bool;
	});
	/*
	 * 输入框得到焦点隐藏错误信息
	 */
	$(".input").focus(function(){
		var varid = $(this).attr("id") + "Error"; //找到input对应的error
		$("#" + varid).text(""); //将该input对应的error信息清空
		showError($("#" + varid)); // 展示错误信息
	});
	/*
	 * 输入框失去焦点时，校验表单
	 */
	$(".input").blur(function(){
		var inputname = $(this).attr("name");
		invokeValidateFunction(inputname);
	});
	
	
})

//这个函数用来展示出错误信息,如果有就显示，如果没有 不显示
function showError(ele){
	var text = ele.text();
	if(!text){
		ele.css("display","none");
	}else{
		ele.css("display","");
	}
}

function invokeValidateFunction(inputName) {
	inputName = inputName.substring(0, 1).toUpperCase() + inputName.substring(1);
	var functionName = "validate" + inputName;
	return eval(functionName + "()");	
}

function validateLoginname(){
	/*
	 * 校验用户名
	 */
	var bool = true;
	var value = $("#loginname").val();
	if(!value) {// 非空校验	
		$("#loginnameError").text("用户名不能为空！");
		bool = false;
	} else if(value.length < 3 || value.length > 20) {//长度校验
		$("#loginnameError").text("用户名长度必须在3 ~ 20之间！");
		bool = false;
	} else {
		$.ajax({//校验是否被注册，如果async选择true，这段代码将一单独的线程出发，没等这个执行完，已经进行了 return bool
			cache: false,
			async: false,
			type: "POST",
			dataType: "json",
			data: {method: "ajaxValidateLoginname", loginname: value},
			url: "/goods/UserServlet",
			success: function(flag) {
				if(!flag) {
					$("#loginnameError").text("用户名已经被注册！");
					bool = false;					
				}
			}
		});
	}
	showError($("#loginnameError"));
	return bool;
}

function validateLoginpass(){
	/*
	 * 校验登录密码
	 */
	var bool = true;
	var value = $("#loginpass").val();
	if(!value) {// 非空校验
		$("#loginpassError").text("密码不能为空！");
		bool = false;
	} else if(value.length < 3 || value.length > 20) {//长度校验
		$("#loginpassError").text("密码长度必须在3 ~ 20之间！");
		bool = false;
	}
	showError($("#loginpassError"));
	return bool;
}

function validateReloginpass(){
	/*
	 * 校验确认密码
	 */
	var bool = true;
	var value = $("#reloginpass").val();
	if(!value) {// 非空校验
		$("#reloginpassError").text("确认密码不能为空！");
		bool = false;
	} else if(value != $("#loginpass").val()) {//密码一致校验
		$("#reloginpassError").text("两次输入密码不一致！");
		bool = false;
	}
	showError($("#reloginpassError"));
	return bool;
}

function validateEmail(){
	/*
	 * 校验邮箱
	 */
	var bool = true;
	var value = $("#email").val();
	if(!value) {// 非空校验
		$("#emailError").text("邮箱不能为空！");
		bool = false;
	} else if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)) {//邮箱格式校验
		$("#emailError").text("邮箱格式错误！");
		bool = false;
	} else{
		$.ajax({//校验是否被注册，如果async选择true，这段代码将一单独的线程出发，没等这个执行完，已经进行了 return bool
			cache: false,
			async: false,
			type: "POST",
			dataType: "json",
			data: {method: "ajaxValidateEmail", email: value},
			url: "/goods/UserServlet",
			success: function(flag) {
				if(!flag) {
					$("#emailError").text("该邮箱已经被注册！");
					bool = false;					
				}
			}
		});
	}
	showError($("#emailError"));
	return bool;
	
}

function validateVerifyCode(){
	/*
	 * 校验验证码
	 */
	var bool = true;
	var value = $("#verifyCode").val();
	if(!value) {// 非空校验
		$("#verifyCodeError").text("验证码不能为空！");
		bool = false;
	} else if(value.length != 4) {//长度校验
		$("#verifyCodeError").text("验证码错误！");
		bool = false;
	} else {
		$.ajax({//校验验证码是否正确
			cache: false,
			async: false,
			type: "POST",
			dataType: "json",
			data: {method: "ajaxValidateVerifyCode", verifyCode: value},
			url: "/goods/UserServlet",
			success: function(flag) {
				if(!flag) {
					$("#verifyCodeError").text("验证码错误！");
					bool = false;					
				}
			}
		});
	}
	showError($("#verifyCodeError"));
	return bool;
}