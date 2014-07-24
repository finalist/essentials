<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>

<hst:defineObjects/>

<c:if test="${hstRequest.requestContext.cmsRequest}">
  <c:set var="containerClassName" value="hst-container"/>
</c:if>

<div class="dashboard ${containerClassName}">
  <c:forEach var="childContentName" items="${hstResponseChildContentNames}">
    <hst:include ref="${childContentName}"/>
  </c:forEach>
</div>