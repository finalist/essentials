{{#repositoryBased}}
    <#include "../../hst:default/hst:templates/imports">
{{/repositoryBased}}
{{#fileBased}}
    <#include "/WEB-INF/freemarker/include/imports.ftl">
{{/fileBased}}
<#--
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
-->
<div>
    <div class="col-md-9">
    <@hst.include ref="detailcontainer"/>
    </div>
    <div class="col-md-3">
    <@hst.include ref="sidebarcontainer"/>
    </div>
</div>