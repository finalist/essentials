<%@ include file="/WEB-INF/jspf/taglibs.jspf"%>
<%@ page trimDirectiveWhitespaces="true"%>

<%-- the component is only visible for authenticated users. Show a text in the channelmanager to easily identify this component--%>
<hst:defineObjects />
<c:if test="${hstRequest.requestContext.preview}">
    Calendar component
</c:if>

<h1><fmt:message key="schedule.user.title"/></h1>
<p><a href='<hst:link siteMapItemRefId="scheduleSearch"/>'><fmt:message key="schedule.search.link"/></a></p>

<div id="filters"></div>
<div id="calendar"></div>

<script id="subjectsFilterTemplate" type="text/x-handlebars-template">
    <select name="subjectFilter">
        <option value="">Alles</option>
        {{#each subjects}}
        <option value="{{this}}">{{this}}</option>
        {{/each}}
    </select>
</script>

<script id="dialogBoxTemplate" type="text/x-handlebars-template">
    <div class="dialog-box">
        <h1>{{title}}</h1>
        <button class="button close">&times;</button>
        <dl>
            <dt>Datum:</dt>
            <dd>{{ucFirst (formatDate start "dddd D MMMM YYYY")}}</dd>
            {{#unless allDay}}
            <dt>Tijd:</dt>
            <dd>{{formatDate start "HH:mm"}} - {{formatDate end "HH:mm"}}</dd>
            {{/unless}}
            {{#if room}}
            <dt>Lokaal / kamer:</dt>
            <dd>{{room}}</dd>
            {{/if}}
            {{#if teachers}}
            <dt>Docenten:</dt>
            {{#each teachers}}
            <dd>{{name}} ({{abbreviation}})</dd>
            {{/each}}
            {{/if}}
            {{#if faculty}}
            <dt>Faculteit:</dt>
            <dd>{{faculty}}</dd>
            {{/if}}
        </dl>
        {{#if description}}
        <article>
            {{description}}
        </article>
        {{/if}}
    </div>
    <div class="overlay"></div>
</script>
