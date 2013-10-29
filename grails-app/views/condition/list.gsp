<%@ page import="edu.uoregon.nic.nemo.portal.Condition" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'condition.label', default: 'Condition')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<g:if test="${experimentHeader?.id}">
    <div class="nav" role="navigation">
        <ul>
            %{--<li><g:link class="list" action="list">All  ${entityName}s</g:link></li>--}%
            <sec:ifAllGranted roles="ROLE_VERIFIED">
                <li><g:link class="create" action="create" id="${experimentHeader?.id}"><g:message
                        code="default.new.label"
                        args="[entityName]"/>s</g:link></li>
            </sec:ifAllGranted>
        </ul>
    </div>
</g:if>

<div id="list-condition" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${related}">
        <div class="filter-list">${related.description}: <g:renderOntological input="${related}"/>
        <g:link action="list">
            <g:img dir="images/icon" file="close.png"/>
        </g:link>
        </div>
    </g:if>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <g:sortableColumn property="identifier" params="[related: related?.relatedLookup]"
                              title="${message(code: 'condition.description.label', default: 'Experimental Condition ID')}"/>

            <th>
                %{--<g:sortableColumn property="experimentConditionType" title="${message(code: 'condition.experimentConditionType.label', default: 'Experiment Condition (Type)')}" />--}%
                Experiment Condition (Type)
            </th>

            <g:sortableColumn property="experimentInstruction" params="[related: related?.relatedLookup]"
                              title="${message(code: 'condition.experimentInstruction.label', default: 'Task (Instructions)')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${conditionInstanceList}" status="i" var="conditionInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    <g:showIdentifier instance="${conditionInstance}"/>
                    <g:if test="${conditionInstance.unverifiedCopy}">
                        <div class="error-small">Unverified</div>
                    </g:if>
                </td>

                <td>
                    <g:renderOntological input="${conditionInstance.types.sort()}" related="condition"/>
                </td>

                <td>
                    %{--${fieldValue(bean: conditionInstance, field: "experimentInstruction")}--}%
                    <g:renderOntological input="${conditionInstance.taskInstruction}" related="condition"/>
                </td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${conditionInstanceTotal}" id="${experimentHeader?.id}"
                    params="[related: related?.relatedLookup]"/>
    </div>
</div>
</body>
</html>
