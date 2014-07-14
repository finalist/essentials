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
<!doctype html>
<html>
<head>
  <title>Hippo Essentials</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/components/angular-ui-tree/dist/angular-ui-tree.min.css"/>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/components/chosen/chosen.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/essentials-less/hippo-essentials.css"/>


  <script src="${pageContext.request.contextPath}/components/jquery/jquery.js"></script>
  <script src="${pageContext.request.contextPath}/components/angular/angular.js"></script>
  <script src="${pageContext.request.contextPath}/components/angular/angular-sanitize.min.js"></script>
  <script src="${pageContext.request.contextPath}/components/chosen/chosen.jquery.js"></script>
  <script src="${pageContext.request.contextPath}/components/underscore/underscore.js"></script>

  <script src="${pageContext.request.contextPath}/components/bootstrap/dist/js/bootstrap.js"></script>


  <%--  NOTE: enable once R&D team upgrades version(s)--%>
  <%--

    <script src="${pageContext.request.contextPath}/components/angular-bootstrap/ui-bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/components/angular-bootstrap/ui-bootstrap-tpls.min.js"></script>
  --%>
  <script src="${pageContext.request.contextPath}/js/lib/ui-bootstrap-tpls.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/lib/angular-route.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/lib/angular-ui-router.js"></script>
  <script src="${pageContext.request.contextPath}/js/lib/angular-animate.js"></script>

  <script src="${pageContext.request.contextPath}/js/lib/chosen.js"></script>

  <%-- HIPPO THEME DEPS --%>
  <script src="${pageContext.request.contextPath}/components/angular-ui-tree/dist/angular-ui-tree.js"></script>
  <script src="${pageContext.request.contextPath}/components/hippo-plugins/dist/js/main.js"></script>
  <script src="${pageContext.request.contextPath}/components/hippo-theme/dist/js/main.js"></script>

  <%-- ESSENTIALS --%>
  <script src="${pageContext.request.contextPath}/js/Essentials.js"></script>
  <script src="${pageContext.request.contextPath}/js/app.js"></script>
  <script src="${pageContext.request.contextPath}/js/routes.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives.js"></script>
  <script src="${pageContext.request.contextPath}/js/controllers.js"></script>

  <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>
</head>
<body id="container" ng-cloak ng-class="feedbackMessages.length ? 'body-push':''">
<essentials-notifier ng-show="feedbackMessages.length" messages="feedbackMessages"></essentials-notifier>
<div class="container-fluid">
  <div class="content fixed-fixed">
    <div class="row-fluid">
      <div class="col-lg-12">
        <div class="col-lg-4"></div>
        <div class="col-lg-4 essentials-header"><h1>{{mainHeader}}</h1></div>
        <div class="col-lg-4">
          <div ng-show="NEEDS_REBUILD" class="pull-right">
            <a href="#/build">System needs a rebuild</a>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="row-fluid">
    <div class="sidebar-nav">
      <ul class="side-menu" ng-controller="mainMenuCtrl">
        <li ng-class="{true:'active', false:''}[isPageSelected('#/plugins')]">
          <a href="#/plugins">
            <i class="fa fa-dashboard"></i>
            <span>Plugins</span>
            <span class="badge pull-right alert-info">{{TOTAL_PLUGINS}}</span>
          </a>

        </li>
        <li ng-class="{true:'active', false:''}[isPageSelected('#/installed-plugins')]">
          <a href="#/installed-plugins">
            <i class="fa fa-dashboard"></i>
            <span>Installed plugins</span>
            <span class="badge pull-right alert-success">{{TOTAL_CONFIGURABLE}}</span>
          </a>

        </li>
        <li ng-class="{true:'active', false:''}[isPageSelected('#/tools')]">
          <a href="#/tools">
            <i class="fa fa-dashboard"></i>
            <span>Tools</span>
            <span class="badge  pull-right alert-info">{{TOTAL_TOOLS}}</span>
          </a>
        </li>
        <li ng-class="{true:'active', false:''}[isPageSelected('#/build')]">
          <a href="#/build">
            <i class="fa fa-dashboard "></i>
            <span>Build</span>
                        <span ng-show="NEEDS_REBUILD" class="badge pull-right alert-danger">
                              <i class="fa fa-bell"></i>
                        </span>
                        <span ng-hide="NEEDS_REBUILD" class="badge pull-right alert-success">
                              <i class="fa fa-check"></i>
                        </span>
          </a>

        </li>
        <li>
          <a target="_blank" href="https://issues.onehippo.com/rest/collectors/1.0/template/form/a23eddf8?os_authType=none">
            <i class="fa fa-external-link"></i>
            <span>Feedback</span></a>
        </li>
      </ul>
    </div>
    <div class="content fixed-fixed">
      <div class="row-fluid">
        <div class="col-lg-12" ui-view autoscroll="false">
        </div>
      </div>
    </div>
  </div>

</div>

<footer class="footer">
  <p>&copy 1999-2014 Hippo B.V.</p>
</footer>

<!-- Include the loader.js script -->
<script src="${pageContext.request.contextPath}/js/loader.js" data-modules="http://localhost:8080/essentials/rest/plugins/modules"></script>

</body>
</html>