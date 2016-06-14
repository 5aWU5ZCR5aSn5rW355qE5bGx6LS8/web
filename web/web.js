$(document).ready(function () {

    Date.prototype.format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1,                 //月份
            "d+": this.getDate(),                    //日
            "h+": this.getHours(),                   //小时
            "m+": this.getMinutes(),                 //分
            "s+": this.getSeconds(),                 //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };


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
                        window.car.manage.show();
                        window.car.selector.enable();
                        window.car.checker.enable();
                        window.car.simulator.enable();
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
            window.car.loginDialog.onchange();
        });

        this.show();
        this.enable();
    };

    window.car.manage = new function () {
        this.hashChange = function () {
            window.car.manage.hideAll(function () {
                var hash = window.location.hash;
                if (hash == "#page-welcome") {
                    $("#page-welcome").fadeIn(500);
                } else if (hash == "#page-simulate") {
                    $("#page-simulate").fadeIn(500);
                } else if (hash == "#page-check") {
                    $("#page-check").fadeIn(500);
                } else if (hash == "#page-select") {
                    $("#page-select").fadeIn(500);
                } else if (hash.split(':', 1)[0] == "#page-result") {
                    $("#page-select-result").fadeIn(500);
                }
            });
        };

        this.hideAll = function (callback) {
            if (callback === undefined) {
                $("#page-check,#page-select,#page-simulate,#page-welcome,#page-select-result").fadeOut(200);
            } else {
                $("#page-check,#page-select,#page-simulate,#page-welcome,#page-select-result").fadeOut(200, callback);
            }
        };

        this.show = function () {
            $("#manage-page").fadeIn(1000);
            $("#nav").animate({"left": 0}, 1000);
            window.location.hash = "#page-welcome";
        };


    };

    window.car.fab = new function () {
        this.enable = function () {
            $(".fixed-action-btn   a").removeClass("disabled");
        };

        this.disable = function () {
            $(".fixed-action-btn   a").addClass("disabled");
        }
    };

    window.car.simulator = new function () {
        var enabled = false;

        this.enable = function () {
            if (!enabled) {
                window.car.fab.enable();
            }
            enabled = true;
        };

        this.disable = function () {
            if (enabled) {
                window.car.fab.disable();
            }
            enabled = false;
        };

        this.run = function () {
            if (enabled) {
                $.getJSON("api.jsp?method=simulate&cmd=run", function (res) {
                    if (res['code'] != 0) {
                        Materialize.toast('服务器异常，无法启动模拟', 3000, 'rounded');
                    }
                }).fail(function () {
                    Materialize.toast('服务器通讯故障，无法启动模拟', 3000, 'rounded');
                });
            }
        };

        this.pause = function () {
            if (enabled) {
                $.getJSON("api.jsp?method=simulate&cmd=pause", function (res) {
                    if (res['code'] != 0) {
                        Materialize.toast('服务器异常，无法暂停模拟', 3000, 'rounded');
                    }
                }).fail(function () {
                    Materialize.toast('服务器通讯故障，无法暂停模拟', 3000, 'rounded');
                });
            }
        };

        this.step = function () {
            if (enabled) {
                $.getJSON("api.jsp?method=simulate&cmd=step", function (res) {
                    if (res['code'] != 0) {
                        Materialize.toast('服务器异常，无法单步模拟', 3000, 'rounded');
                    }
                }).fail(function () {
                    Materialize.toast('服务器通讯故障，无法单步模拟', 3000, 'rounded');
                });
            }
        };

        setInterval(function () {
            $("#simulate-info-local-time").text(new Date().format("yy-MM-dd hh:mm:ss"));
            if (enabled) {
                $.getJSON("api.jsp?method=simulate", function (res) {
                    if (res['code'] == 0) {
                        $("#simulate-info-remote-time").text(res['time'] + 's');

                        if (res['run'] == true) {
                            $("#simulate-info-pause").text("运行");
                        } else {
                            $("#simulate-info-pause").text("暂停");
                        }
                    } else {
                        Materialize.toast('服务器异常，无法获得服务', 3000, 'rounded');
                    }
                }).fail(function () {
                    Materialize.toast('服务器通讯故障', 3000, 'rounded');
                });
            }
        }, 1000);
    };

    window.car.checker = new function () {
        var enabled = false;

        this.enable = function () {
            if (!enabled) {
                enabled = true;
            }
        };

        this.disable = function () {
            if (enabled) {
                enabled = false;
            }
        };

        this.check = function () {
            if (enabled) {
                //TODO:
            }
        }
    };

    window.car.selector = new function () {
        var enabled = false;

        var canvas = document.getElementById("mapCanvas");
        var ctx = canvas.getContext("2d");

        this.enable = function () {
            if (!enabled) {
                enabled = false;
            }
        };

        this.disable = function () {
            if (enabled) {
                enabled = true;
            }
        };

        var mapSize = $("#mapCanvas").parent().width();
        var scale = mapSize / 1000;
        $("#mapCanvas").css({"scale:": scale});

        this.drawMap = function (pt) {
            ctx.fillStyle = "#FFFFFF";
            ctx.fillRect(0, 0, 1000, 1000);


        };


    };

    window.location.hash = "";
    window.onhashchange = window.car.manage.hashChange;

    $('.collapsible').collapsible({
        accordion: false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
    });

});