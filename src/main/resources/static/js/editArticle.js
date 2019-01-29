define(function (require,exports,module) {
    require('bootstrap');
    var E=require('wangEditor')
    var  editor = new E( document.getElementById('editor') )
    editor.create();
    $('.submit').on('click',function () {
        var formData=$('#form').serializeArray()

        formData.push({name:'content',value:editor.txt.html()})
        console.log(formData)
        $.post('/article/add',formData,function (data) {
            console.log(data)
        })
    })
});