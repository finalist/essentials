<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>

<div id="content">
	<div>
		<ul>
			<c:forEach var="employee" items="${employees}">
				<tag:employeeitem item="${employee}" />
			</c:forEach>
		</ul>
	</div>
	
</div>
