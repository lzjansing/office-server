// 显示加载框
function loading(mess, type) {
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "positionClass": "toast-top-right",
        "onclick": null,
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    if (type == null) {
        toastr["success"](mess);
    } else {
        toastr[type](mess);
    }
}

// 恢复提示框显示
function resetTip() {
    top.$.jBox.tip.mess = null;
}

// 确认对话框
function confirmDialog(mess, url) {
    $('#confirm-body').html(mess);
    $('#confirm-ok').click(function() {
        window.location.href = url;
    });
}

//获取字典标签
function getDictLabel(data, value, defaultValue){
    for (var i = 0; i < data.length; i++) {
        var row = data[i];
        if (row.value == value) {
            return row.label;
        }
    }
    return defaultValue;
}

// Html转义字符编解码
var REGX_HTML_ENCODE = /"|&|'|<|>|[\x00-\x20]|[\x7F-\xFF]|[\u0100-\u2700]/g;
var REGX_HTML_DECODE = /&\w+;|&#(\d+);/g;
var HTML_DECODE = {
    "&lt;" : "<",
    "&gt;" : ">",
    "&amp;" : "&",
    "&nbsp;": " ",
    "&quot;": "\"",
    "&ldquo;": "“",
    "&rdquo;": "”",
    "©": ""
    // Add more
};
function encodeHtml(s) {
    return (typeof s != "string") ? s :
        s.replace(REGX_HTML_ENCODE,
            function ($0) {
                var c = $0.charCodeAt(0), r = ["&#"];
                c = (c == 0x20) ? 0xA0 : c;
                r.push(c);
                r.push(";");
                return r.join("");
            });
};

function decodeHtml(s) {
    return (typeof s != "string") ? s :
        s.replace(this.REGX_HTML_DECODE,
            function ($0, $1) {
                var c = HTML_DECODE[$0]; // 尝试查表
                if (c === undefined) {
                    // Maybe is Entity Number
                    if (!isNaN($1)) {
                        c = String.fromCharCode(($1 == 160) ? 32 : $1);
                    } else {
                        // Not Entity Number
                        c = $0;
                    }
                }
                return c;
            });
};