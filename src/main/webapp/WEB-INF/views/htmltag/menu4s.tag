<ul class="nav nav-list">

<%
	var list = menu();
	if (!isEmpty(list)) {
%>
	<%
		for(entry in list) { 
			if (entry.pid == 0) {
	%>
				<li class="" id="function_${entry.id}"><a href="#" class="dropdown-toggle">
					<i class="menu-icon fa fa-desktop"></i> <span class="menu-text">${entry.name}</span><b class="arrow fa fa-angle-down"></b></a><b class="arrow"></b>
					<ul class="submenu">
		<%
				for(entry2 in list) { 
					if (entry2.pid == entry.id) {
					
						var flag = false;
						for(entry3 in list) { 
							if (entry3.pid == entry2.id) {
								flag = true;
							}
						}
						
						if (flag) {
			%>	
							<li class="" id="function_${entry2.id}"><a href="#" class="dropdown-toggle">
									<i class="menu-icon fa fa-caret-right"></i> ${entry2.name} <b class="arrow fa fa-angle-down"></b></a><b class="arrow"></b>
								<ul class="submenu">

		<%
							for(entry3 in list) { 
								if (entry3.pid == entry2.id) {								
			%>						
										<li class="" id="function_${entry3.id}">
											<a href="${ctxPath}${entry3.url}?_func=function_${entry.id}-function_${entry2.id}-function_${entry3.id}"> <i class="menu-icon fa"></i> ${entry3.name}</a> <b class="arrow"></b>
										</li>
				<%			
								}
							}							
				%>	
								</ul>
							</li>

			<%			
						
						} else {
			%>			
						
							<li class="" id="function_${entry2.id}"><a href="${ctxPath}${entry2.url}?_func=function_${entry.id}-function_${entry2.id}"> 
								<i class="menu-icon fa fa-caret-right"></i>${entry2.name}</a> <b class="arrow"></b>
							</li>		
		<%				
						}		
					}
				}
		%>		
					</ul>
				</li>				
		<%		
			}
		%>
<%
		}
	}
%>	

</ul>