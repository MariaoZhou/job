<%
	if (!isEmpty(key)) {
		map = selectlist(key);
	}
	
	var isMt = false;
	if ((!isEmpty(isMultiple)) && (isMultiple == 'true')) {
		isMt = true;
	}	
	
	var valuelist = value;
	if (!isMt) {
		valuelist = [value];
	}
	
%>
<select id="${id!}" name="${name}" class="${class!}" <% if (isMt) {%> multiple="" <% } %> >
	<% 
		if (!isEmpty(pleaseSelect)) {
	%>
	<option value="">${pleaseSelect}</option>
	<% 			
		}
	%>

	<% 
		for(entry in map) { 
			if (array.contain(valuelist , entry.value)) {
	%>
	<option selected value="${entry.value}">${entry.key}</option>
	<% 
			} else {
	%>
	<option value="${entry.value}">${entry.key}</option>
	<% 			
			}
		}
	%>
</select>
