define(function (require,exports,module) {
    require('bootstrap');
    $('.media.article').on('click',function () {
        window.location.href=`/detail?id=${$(this).attr('data')}`
    })
});