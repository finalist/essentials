<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%--@elvariable id="userInfo" type=" {{componentsPackage}}.model.UserInfo"--%>
<%--@elvariable id="areaOfInterestStates" type="java.util.List"--%>
<%--@elvariable id="componentStates" type="java.util.List"--%>

<c:choose>
  <c:when test="${empty userInfo}">
    <c:out value="no userinfo, check the logs"/>
  </c:when>
  <c:otherwise>
    <div id="sidebar"><hst:include ref="left"/></div>
    <div id="content">
      <div class="main-content profile small">
        <h1><fmt:message key="account.profile.title"/></h1>
        
        <div class="userdata section">
          <c:if test="${not empty userInfo.photoData}">
            <div class="image"><img src="data:image/jpeg;base64,${userInfo.photoData}" /></div>
          </c:if>
          <div class="details">
            <h2><c:out value="${userInfo.fullName}"/></h2>
            <dl>
              <c:if test="${not empty userInfo.function}">
                <dt><fmt:message key="account.profile.function"/>:</dt>
                <dd><c:out value="${userInfo.function}"/></dd>
              </c:if>
              <c:if test="${not empty userInfo.faculty and not empty userInfo.faculty.facultyName}">
                <dt><fmt:message key="account.profile.department"/>:</dt>
                <dd><c:out value="${userInfo.faculty.facultyName}"/></dd>
              </c:if>
              <c:if test="${not empty userInfo.phone}">
                <dt><fmt:message key="account.profile.phone"/>:</dt>
                <dd><c:out value="${userInfo.phone}"/></dd>
              </c:if>
              <c:if test="${not empty userInfo.email}">
                <dt><fmt:message key="account.profile.email"/>:</dt>
                <dd><a href='mailto:<c:out value="${userInfo.email}"/>'><c:out value="${userInfo.email}"/></a></dd>
              </c:if>
            </dl>
            <c:if test="${not empty profileEditUrl}">
              <a href="${profileEditUrl}" class="button">Wijzig je profiel</a>
            </c:if>
          </div>
        </div>
<%--        <c:if test="${userInfo.class.simpleName eq 'EmployeeInfo'}">--%>
          <div class="presence section">
            <h3><fmt:message key="account.profile.presence.title"/></h3>
          
            <table>
              <thead>
                <tr>
                  <th></th>
                  <th><fmt:message key="account.profile.presence.monday"/></th>
                  <th><fmt:message key="account.profile.presence.tuesday"/></th>
                  <th><fmt:message key="account.profile.presence.wednesday"/></th>
                  <th><fmt:message key="account.profile.presence.thursday"/></th>
                  <th><fmt:message key="account.profile.presence.friday"/></th>
                </tr>
              </thead>
              
              <tbody>
                <tr>
                  <th><fmt:message key="account.profile.presence.morning"/></th>
                  <td>
                    <c:if test="${userInfo.presenceMONMorning}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceTUEMorning}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceWEDMorning}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceTHUMorning}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceFRIMorning}"><div class="check">V</div></c:if>
                  </td>
                </tr>
                <tr>
                  <th><fmt:message key="account.profile.presence.afternoon"/></th>
                  <td>
                    <c:if test="${userInfo.presenceMONAfternoon}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceTUEAfternoon}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceWEDAfternoon}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceTHUAfternoon}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceFRIAfternoon}"><div class="check">V</div></c:if>
                  </td>
                </tr>
                <tr>
                  <th><fmt:message key="account.profile.presence.evening"/></th>
                  <td>
                    <c:if test="${userInfo.presenceMONEvening}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceTUEEvening}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceWEDEvening}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceTHUEvening}"><div class="check">V</div></c:if>
                  </td>
                  <td>
                    <c:if test="${userInfo.presenceFRIEvening}"><div class="check">V</div></c:if>
                  </td>
                </tr>
              </tbody>
            </table>
            <c:if test="${not empty notPresentContact}">
              <div class="unavailable">
                <h4><fmt:message key="account.profile.presence.backup"/></h3>
                <p><c:out value="${notPresentContact.fullName}"/>
                <c:if test="${not empty notPresentContact.email}">
                  <a href="mailto:${notPresentContact.email}"><c:out value="${notPresentContact.email}"/></a>
                </c:if>
                </p>
              </div>
            </c:if>
          </div>
          
          <div class="miscinfo section">
            <c:if test="${not empty userInfo.work}">
              <h3><fmt:message key="account.profile.work"/></h3>
              <p><c:out value="${userInfo.work}"/></p>
            </c:if>
            <c:if test="${not empty userInfo.fields}">
              <h3><fmt:message key="account.profile.expertise"/></h3>
              <p><c:out value="${userInfo.fields}"/></p>
            </c:if>
            <c:if test="${not empty userInfo.projects}">
              <h3><fmt:message key="account.profile.projects"/></h3>
              <p><c:out value="${userInfo.projects}"/></p>
            </c:if>
            <c:if test="${not empty userInfo.publications}">
              <h3><fmt:message key="account.profile.publications"/></h3>
              <p><c:out value="${userInfo.publications}"/></p>
            </c:if>
            <c:if test="${not empty userInfo.otherPositions}">
              <h3><fmt:message key="account.profile.otherPositions"/></h3>
              <p><c:out value="${userInfo.otherPositions}"/></p>
            </c:if>
            <c:if test="${not empty socialAccounts}">
              <h3><fmt:message key="account.profile.socialnetworks"/></h3>
              <ul>
                <c:forEach var="socialAccount"  items="${socialAccounts}">
                  <li>
                    <a href="${socialAccount.url}" class="${socialAccount.title}"><c:out value="${socialAccount.title}"/></a>
                  </li>
                </c:forEach>
              </ul>
            </c:if>
            <c:if test="${not empty personalWebsites}">
              <h3><fmt:message key="account.profile.personalwebsites"/></h3>
              <ul>
                <c:forEach var="personalWebsite" items="${personalWebsites}">
                  <li>
                    <a href="${personalWebsite.url}"><c:out value="${personalWebsite.url}"/></a>
                  </li>
                </c:forEach>
              </ul>
            </c:if>
          </div> 
<%--        </c:if>--%>
      </div>
      <div class="sidebar-right">
        <hst:include ref="right" />
      </div>
    </div>
  </c:otherwise>
</c:choose>