define(function (require,exports,module) {
    require('jquery');
    require('bootstrap');
    var ImgCorp=require('/static/js/util/img-corp.js');
    var E=require('wangEditor');
    var  editor = new E( document.getElementById('editor') );
    editor.create();
    //图片上传实例化
    $('.fileUpload').on('click',function () {
        var advertiseId = $("#id").val();
        var imgCorp = new ImgCorp();
        imgCorp.init({resize:false, aspectRatio: 1}, function (data) {
            var imgId = data.id;
            if(advertiseId != ""){
                var posting = $.post($CONFIG.base_url + "/api/post/advertise/profile/avatar/update", {id: advertiseId, attachment: data.id});
                posting.done(function (data) {
                    $('#avatar-preview').attr("src", $CONFIG.base_url + '/commons/attachment/download/' + imgId + "?t="+new Date());
                    $("#photo-id").val(imgId);
                });
            }else {
                $('#avatar-preview').attr("src", $CONFIG.base_url + '/commons/attachment/download/' + imgId + "?t=" + new Date());
                $("#photo-id").val(imgId);
            }
        });
    });
    $('.submit').on('click',function () {
        var formData=$('#form').serializeArray();

        formData.push({name:'content',value:editor.txt.html()});
        console.log(formData);
        $.post('/article/add',formData,function (data) {
            console.log(data)
        })
    })
});