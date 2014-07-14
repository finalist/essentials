<%@ include file="/WEB-INF/jsp/include/imports.jsp" %>
<%--
  Copyright 2014 Hippo B.V. (http://www.onehippo.com)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  --%>

<%--@elvariable id="document" type="{{beansPackage}}.Blogpost"--%>
<div class="has-edit-button">
<hst:cmseditlink hippobean="${document}"/>
<h1><c:out value="${document.title}"/></h1>
<h2>by: <c:out value="${document.author}"/></h2>
<strong>
  <c:if test="${document.publicationDate ne null}">
    <fmt:formatDate type="date" pattern="yyyy-MM-dd" value="${document.publicationDate.time}"/>
  </c:if>
</strong>
<p><c:out value="${document.introduction}"/></p>
<div><hst:html hippohtml="${document.content}"/></div>
</div>