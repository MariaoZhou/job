<%
	list = breadcrumbs(key);
%>

	<%
		if (!isEmpty(list)) {
			for(entity in list) { 
	%>
		<li class="active"><a href="${entity.url}">${entity.name}</a></li>
	<% 			
			}
		}
	%>