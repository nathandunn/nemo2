<%@ page import="edu.uoregon.nic.nemo.portal.Stimulus" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'stimulus.label', default: 'Stimulus')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>


<div id="list-stimulus" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>

    <g:render template="/common/relatedFilter" model="['related': related]"/>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <g:sortableColumn property="identifier"  params="[related: related?.relatedLookup]"
                              title="${message(code: 'stimulus.identifier.label', default: 'Identifier')}"/>

            <g:sortableColumn property="targetType"  params="[related: related?.relatedLookup]"
                              title="${message(code: 'stimulus.targetType.label', default: 'Stimulus Type')}"/>

            <g:sortableColumn property="targetModality"  params="[related: related?.relatedLookup]"
                              title="${message(code: 'stimulus.targetModality.label', default: 'Stimulus Modality')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${stimulusInstanceList}" status="i" var="stimulusInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    %{--${fieldValue(bean: stimulusInstance, field: "identifier")}--}%
                    <g:showIdentifier instance="${stimulusInstance}"/>
                </td>

                <td>
                    %{--${fieldValue(bean: stimulusInstance, field: "targetStimulusType")}--}%
                    <g:renderOntological input="${stimulusInstance.targetType}" related="stimulus"/>
                </td>

                <td>
                    <g:renderOntological input="${stimulusInstance.targetModality}" related="stimulus"/>
                </td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${stimulusInstanceTotal}" id="${experimentHeader?.id}" params="[related: related?.relatedLookup]"/>
    </div>
</div>
</body>
</html>
