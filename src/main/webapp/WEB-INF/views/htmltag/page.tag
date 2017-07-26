<%
if (page.totalRow > page.pageSize) {

    var _pageNumber = page.pageNumber;   // 当前页
    var _totalPage = page.totalPage;     // 总页数
    var _totalRow = page.totalRow;		// 总条数
    var _num_display_entries = 11;       // 每次显示多少页
    var _num_edge_entries = 2;           // 前后各多少页
    var _ellipse_text = "...";

    var params_url= "";
    if(!isEmpty(params)) {
    	var array = strutil.split(params,",");
        for(a in array){
        	var aval = value(a);
        	if(!isEmpty(aval)) {
            	params_url = params_url+"&"+a+"="+aval;
            }
        }
    }
%>
<div>
	<ul class="pagination">
<%
        // 上一页
        if (_pageNumber != 1) { 
            printf("<li><a href='%s?_page=%s%s'><i class='ace-icon fa fa-angle-double-left'></i>上一页</a></li>", uri,  _pageNumber - 1, params_url);
        } else {
            printf("<li class='disabled'><a href='#'><i class='ace-icon fa fa-angle-double-left'></i>上一页</a></li>", uri, _pageNumber - 1);
        }
         
        // 总页数大于 (显示的页数+前后显示) 表示需要显示很多页
        var pageCount = _totalPage - (_num_display_entries + _num_edge_entries * 2);
        if (pageCount > 0) {
            var i = 1;
            var len = 0;
            var _start = trunc(_num_display_entries / 2);
            var _end = _start;
            if (_num_display_entries%2 == 0) {
                _start = _start - 1;
            }
             
            // 页码-前
            if (_pageNumber - _start - _num_edge_entries >= _num_edge_entries) {
                while(i < _num_edge_entries + 1) {
                    printf("<li><a href='%s?_page=%s%s'>%s</a></li>", uri, i, params_url, i);
                    i = i + 1;
                }
                printf("<li class='disabled'><a href='#'>%s</a></li>", _ellipse_text);
                 
                i = _pageNumber-_start;
                len = _pageNumber + _end+1;
            } else {
                len = _num_display_entries+1;
            }
             
            var show__num_edge_entries_next = _totalPage - _pageNumber - _end - _num_edge_entries > 0;
            if (!show__num_edge_entries_next) {
                i = _pageNumber - _start;
                if (_totalPage - _pageNumber - _end < 0) {
                    i = _totalPage - _num_display_entries+1;
                }
                len = _totalPage + 1;
            }
             
            // 显示中间的那串页码
            while(i < len) {
                if (_pageNumber == i) {
                	printf("<li class='active'><a href='#'>%s</a></li>", i);
                } else {
                   	printf("<li><a href='%s?_page=%s%s'>%s</a></li>", uri, i, params_url, i);
                }
                i = i + 1;
            }
             
            // 页码-后
            if (show__num_edge_entries_next) {
                printf("<li class='disabled'><a href='#'>%s</a></li>", _ellipse_text);
                i = _totalPage + 1 - _num_edge_entries;
                while(i < _totalPage + 1) {
                    printf("<li><a href='%s?_page=%s%s'>%s</a></li>", uri, i, params_url, i);
                    i = i + 1;
                }
            }
        } else {
            // 显示全部页码
            var i = 1;
            while(i < _totalPage + 1) {
                if (_pageNumber == i) {
                    printf("<li class='active'><a href='#'>%s</a></li>", i);
                } else {
                    printf("<li><a href='%s?_page=%s%s'>%s</a></li>", uri, i, params_url, i);
                }
                i = i + 1;
            }
        }
        
        // 下一页
        if (_pageNumber != _totalPage) {
            printf("<li><a href='%s?_page=%s%s'>下一页<i class='ace-icon fa fa-angle-double-right'></i></a></li>", uri, _pageNumber + 1, params_url);
        } else {
            printf("<li class='disabled'><a href='#'>下一页<i class='ace-icon fa fa-angle-double-right'></i></a></li>", uri, _pageNumber + 1);
        }        
    %>
    
    <li class="disabled"><a>共  ${_totalRow} 条记录</a></li>
    </ul>
</div>
<%
    }
%>