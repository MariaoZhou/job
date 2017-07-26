<%
	if(!isEmpty(message)) {
%>

<!--  alert BEGAIN-->
<div class="alert alert-block alert-${message.type}">
	<button type="button" class="close" data-dismiss="alert">
		<i class="ace-icon fa fa-times"></i>
	</button>

	<i class="ace-icon fa fa-check green"></i> 
	${message.msg!}
</div>
<!-- alert END -->

<%
	}
%>