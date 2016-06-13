$(document).ready(function () {
    window.car = Object();
    window.car.loginDialog = new function () {
        var disabled = false;
        var name = "", pwd = "";

        this.disable = function () {
            disabled = true;
            $("#login-holder").fadeIn(500);
        };

        this.enable = function () {
            disabled = false;
            $("#login-holder").fadeOut(500);
        };

        this.show = function () {
            $("#login-panel").fadeIn(2000);
        };

        this.hide = function () {
            $("#login-panel").fadeOut(1000);
        };

        this.login = function () {
            if (!disabled) {
                window.car.loginDialog.disable();
                window.car.loginDialog.disableLogin();
                Materialize.toast('尝试登录', 3000, 'rounded');
                $.getJSON("api.jsp?method=login&name=" + $("#name").val() + "&pwd=" + $("#pwd").val(), function (res) {
                    window.car.loginDialog.enable();
                    if (res["code"] != 0) {
                        // error
                        Materialize.toast('登录失败：用户名密码错误', 3000, 'rounded');
                    } else {
                        Materialize.toast('登录成功', 3000, 'rounded');
                        window.car.loginDialog.hide();
                    }
                }).fail(function () {
                    window.car.loginDialog.enable();
                    window.car.loginDialog.enableLogin();
                    Materialize.toast('登录失败：服务器异常', 3000, 'rounded');
                });
            }
        };

        this.disableLogin = function () {
            $("#btn-login-submit").addClass("disabled").addClass("waves-effect");
        };

        this.enableLogin = function () {
            $("#btn-login-submit").removeClass("disabled").removeClass("waves-effect");
        };

        this.onchange = function () {
            if (name != "" && pwd != "") {
                this.enableLogin();
            } else {
                this.disableLogin();
            }
        };

        $("#name").keydown(function () {
            name = $(this).val();
            window.car.loginDialog.onchange();
        });

        $("#pwd").keydown(function (e) {
            pwd = $(this).val();
            window.car.loginDialog.onchange();

            if (e.keyCode == 13) {
                window.car.loginDialog.login();
            }
        });

        $("#btn-login-submit").click(function () {
            window.car.loginDialog.login();
        });

        $("#login-form").submit(function () {
            window.car.loginDialog.login();
        });

        $("#btn-login-reset").click(function () {
            $("#name").val("");
            $("#pwd").val("");
            this.onchange();
        });

        this.show();
        this.enable();
    };
});