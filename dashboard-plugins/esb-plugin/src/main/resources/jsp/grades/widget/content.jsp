<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%@ page trimDirectiveWhitespaces="true" %>

<c:if test="${visible}">
  <tag:widgetHeader title="${info.title}"/>

<c:choose>
    <c:when test="${error}">
      <p>
        <fmt:message key="widget.error" />
      </p>
    </c:when>
    <c:when test="${empty grades}">
        <p>
          <fmt:message key="widget.empty" />
        </p>
    </c:when>
    <c:otherwise>
      <c:forEach var="grade" items="${grades}">
        <p>
          <span class="activity">
            <c:choose>
              <c:when test="${locale eq 'en'}">
                <c:out value="${grade.studyProgramShortEn}"/>
              </c:when>
              <c:otherwise>
                <c:out value="${grade.studyProgramShortNl}"/>
              </c:otherwise>
            </c:choose>
          </span>
          <span class="grade"><c:out value="${grade.grade}"/></span>
          <span class="description">
            <span class="date"><fmt:formatDate value="${grade.mutationDate}" pattern="dd-MM-yyyy"/></span>
             <c:choose>
                <c:when test="${locale eq 'en'}">
                  <span class="text"><c:out value="${grade.testDescriptionEn}"/></span>
                </c:when>
                <c:otherwise>
                  <span class="text"><c:out value="${grade.testDescriptionNl}"/></span>
                </c:otherwise>
              </c:choose>
            </span>
        </p>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</c:if>
