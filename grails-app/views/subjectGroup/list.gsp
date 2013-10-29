<%@ page import="edu.uoregon.nic.nemo.portal.SubjectGroup" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'subjectGroup.label', default: 'Subjects')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<g:if test="${experimentHeader?.id}">
    <div class="nav" role="navigation">
        <ul>
            <li><g:link class="list" action="list">All  ${entityName}</g:link></li>
            <sec:ifAllGranted roles="ROLE_VERIFIED">
                <li><g:link class="create" action="create" id="${experimentHeader?.id}"><g:message
                        code="default.new.label"
                        args="[entityName]"/></g:link></li>
            </sec:ifAllGranted>
        </ul>
    </div>
</g:if>

<div id="list-subjectGroup" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <g:sortableColumn property="identifier"
                              title="${message(code: 'subjectGroup.identifier.label', default: 'Subject Group ID')}"/>
            <g:sortableColumn property="groupSize"
                              title="${message(code: 'subjectGroup.groupSize.label', default: 'Subject Group Size')}"/>
            <th>
                Diagnostic Classifications
            </th>
            %{--<g:sortableColumn property="diagnosticClassifications" title="${message(code: 'subjectGroup.diagnosticClassification.label', default: 'Diagnostic DiagnosticClassification')}" />--}%

            <g:sortableColumn property="genus" title="${message(code: 'subjectGroup.genus.label', default: 'Genus')}"/>
            <g:sortableColumn property="species"
                              title="${message(code: 'subjectGroup.species.label', default: 'Species')}"/>

            <g:sortableColumn property="groupAge"
                              title="${message(code: 'subjectGroup.groupAge.label', default: 'Age (average)')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${subjectGroupInstanceList}" status="i" var="subjectGroupInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${subjectGroupInstance.id}">${fieldValue(bean: subjectGroupInstance, field: "identifier")}</g:link>
                <g:if test="${subjectGroupInstance.unverifiedCopy}">
                    <div class="error-small">Unverified</div>
                </g:if>
                </td>

                <td>${fieldValue(bean: subjectGroupInstance, field: "groupSize")}</td>
                <td>
                    <g:each in="${subjectGroupInstance.diagnosticClassifications}" var="classification">
                        <g:link action="show" controller="term"
                                id="${classification.suffix}">${classification.name}</g:link>
                    </g:each>

                </td>

                <td>
                    <g:renderUrlLink input="${subjectGroupInstance.genus}"/>
                </td>
                <td>
                    <g:renderUrlLink input="${subjectGroupInstance.species}"/>
                </td>

                <td>${fieldValue(bean: subjectGroupInstance, field: "groupAge")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${subjectGroupInstanceTotal}" id="${experimentHeader?.id}"/>
    </div>
</div>
</body>
</html>
