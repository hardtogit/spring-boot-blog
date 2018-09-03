define(function (require,exports,module) {
    var $=require('jquery');
    //输入框事件
    $('#searchfield').on('focus blur',function () {
        var field=this;
        if (field.defaultValue == field.value) field.value = '';
        else if (field.value == '') field.value = field.defaultValue;
    })
});