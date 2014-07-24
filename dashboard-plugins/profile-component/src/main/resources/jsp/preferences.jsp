<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%--@elvariable id="userInfo" type="{{componentsPackage}}.model.UserInfo"--%>
<%--@elvariable id="areaOfInterestStates" type="java.util.List"--%>
<%--@elvariable id="componentStates" type="java.util.List"--%>

<c:choose>
  <c:when test="${empty userInfo}">
    <c:out value="no userinfo, check the logs"/>
  </c:when>
  <c:otherwise>
    <div id="sidebar"><hst:include ref="left"/></div>
    <div id="content">
      <div class="main-content preferences small">
        <h1><fmt:message key="account.preferences.title"/></h1>
        <hst:actionURL var="actionUrl"/>
        <form action="${actionUrl}" method="POST">
          <h2><fmt:message key="account.preferences.areasofinterest.title"/></h2>
          <p><fmt:message key="account.preferences.areasofinterest.intro"/></p>
          <fieldset>
            <legend><fmt:message key="account.preferences.areasofinterest.myareasofinterest"/></legend>
            <ul>
              <c:forEach var="areaOfInterest" items="${areaOfInterestStates}">
                <li>
                    <input type="checkbox" id="${componentState.key}" name="${componentState.key}" value="${componentState.key}" <c:if test="${componentState.enabled}">checked</c:if> />
                    <label for="${componentState.key}">
                        <c:choose>
                            <c:when test="${empty componentState.title or componentState.title eq ''}">
                                <c:out value="${componentState.key}"></c:out>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${componentState.title}"></c:out>
                            </c:otherwise>
                        </c:choose>
                    </label>
                </li>
              </c:forEach>
            </ul>
          </fieldset>
          
          <%-- the design shows settings for type of activities, these are not implemented --%>
          
          <h2><fmt:message key="account.preferences.widgets.title"/></h2>
          <fieldset>
            <legend><fmt:message key="account.preferences.widgets.mywidgets"/></legend>
            <ul>
              <c:forEach var="componentState" items="${componentStates}">
                <li>
                  <input type="checkbox" id="${componentState.key}" name="${componentState.key}" value="${componentState.key}" <c:if test="${componentState.enabled}">checked</c:if> />
                  <label for="${componentState.key}"><c:out value="${componentState.title}"></c:out></label>
                </li>
              </c:forEach>
            </ul>
          </fieldset>

          <button class="button" value="save" type="submit">Opslaan</button>
        </form>
      </div>
      <div class="sidebar-right">
        <hst:include ref="right" />
      </div>
    </div>
  </c:otherwise>
</c:choose>