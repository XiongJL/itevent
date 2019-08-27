   //table转ul函数
   $.fn.setTable = function () {
    var el=this;
    this.start=function(){
        $(el).find("ul").remove();
        $(el).map(function () {
            var list = '';
            var name = [];
            if ($(this).find("th").length == 0) {
                name = false;
            } else {
                $(this).find("th").map(function () {
                    name.push($(this).html());
                });
            }
            $(this).find("tbody tr").map(function () {
                var ul = '<ul>';
                $(this).find("td").map(function (index, item) {
                    if(name) {
                        ul += '<li>' + name[index] + ":&nbsp;" + $(this).html() + '</li>';
                    }else{
                        ul += '<li>' +"&nbsp;" + $(this).html() + '</li>';
                    }
                });
                ul += '</ul>';
                list += ul;
            });
            $(this).find("table").hide();
            $(this).append(list);
        })
    }
    var _this=this;
   $(window).resize(function(){
        if($(window).width()<767){
           _this.start();
           }else{
            $(el).find("table").show();
            $(el).find("ul").hide();
        }
    });
    if($(window).width()<767){
        _this.start();
    }
};

