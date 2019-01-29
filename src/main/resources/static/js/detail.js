define(function (require,exports,module) {
    require('bootstrap');
    require('highlight');
    $('pre code').each(function(i, block) {
        hljs.highlightBlock(block);
    })
    $('.media.article').on('click',function () {
        window.location.href=`/detail?id=${$(this).attr('data')}`
    })
});