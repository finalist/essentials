<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>

<c:forEach var="facet" items="${facetNavigation.folders}">
    <c:if test="${facet.count > 0}">
        <c:choose>
            <c:when test="${facet.leaf}">
                <li class="filter-by"><c:out value="${facet.name}"/>
                    <ul class="bullet-points">
                        <li><a class="filter" href="#"><c:out value="${facet.name}"/></a></li>
                    </ul>
                </li>
            </c:when>
            <c:when test="${facet.childCountsCombined eq 0}">
            </c:when>
            <c:otherwise>
                <li class="filter-by"><c:out value="${facet.name}"/>:
                    <c:if test="${not empty facet.folders}">
                        <ul class="bullet-points">
                            <c:forEach items="${facet.folders}" var="item">
                                <c:choose>
                                    <c:when test="${item.leaf and item.count gt 0}">
                                        <hst:facetnavigationlink remove="${item}" current="${facetNavigation}" var="removeLink"/>
                                        <li>
                                            <a class="filter" href="${removeLink}">
                                                <c:out value="${item.name}"/>
                                            </a>
                                        </li>
                                    </c:when>
                                    <c:when test="${item.leaf and item.count eq 0}">
                                    </c:when>
                                    <c:otherwise>
                                        <hst:link var="link" hippobean="${item}" navigationStateful="true"/>
                                        <li>
                                            <a href="${link}"><c:out value="${item.name}"/></a>&nbsp;(${item.count})
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </ul>
                    </c:if>
                </li>
            </c:otherwise>
        </c:choose>
    </c:if>
</c:forEach>

<div id="content">
	<div>
		<ul>
			<c:forEach var="employee" items="${employees}">
				<tag:employeeitem item="${employee}" />
			</c:forEach>
		</ul>
	</div>
	
</div>
