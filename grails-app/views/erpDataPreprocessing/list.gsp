<%@ page import="edu.uoregon.nic.nemo.portal.ErpDataPreprocessing" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessings')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

    <g:if test="${experimentHeader}">
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                                  args="[entityName]"/></g:link></li>
        <sec:ifAllGranted roles="ROLE_VERIFIED">
                <li><g:link class="create" action="create" id="${experimentHeader.id}"><g:message
                        code="default.new.label" args="[entityName]"/></g:link></li>
        </sec:ifAllGranted>
            </ul>
        </div>
    </g:if>

<div id="list-erpDataPreprocessing" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>

    <g:render template="/common/relatedFilter" model="['related': related]"/>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="identifier" params="[related: related?.relatedLookup]"
                              title="${message(code: 'erpDataPreprocessing.identifier.label', default: 'ID')}"/>

            <th>Erp Event</th>
            %{--<g:sortableColumn property="event.name" params="[related: related?.relatedLookup]"--}%
                              %{--title="${message(code: 'erpDataPreprocessing.baselineLength.label', default: 'Erp Event')}"/>--}%
            %{--<th>--}%
            %{--E--}%
            %{--</th>--}%

            <g:sortableColumn property="erpEpochLength" params="[related: related?.relatedLookup]"
                              title="${message(code: 'erpDataPreprocessing.baselineLength.label', default: 'Erp Epoch Length')}"/>

            <g:sortableColumn property="baselineLength" params="[related: related?.relatedLookup]"
                              title="${message(code: 'erpDataPreprocessing.baselineLength.label', default: 'Baseline Length')}"/>

            <g:sortableColumn property="reference" params="[related: related?.relatedLookup]"
                              title="${message(code: 'erpDataPreprocessing.reference.label', default: 'Offline Reference')}"/>

            <th>
                Transformation Labels
            </th>
            %{--<g:sortableColumn property="dataCleaningTransformation"--}%
            %{--title="${message(code: 'erpDataPreprocessing.dataCleaningTransformation.label', default: 'Data Cleaning Transformation')}"/>--}%

        </tr>
        </thead>
        <tbody>
        <g:each in="${erpDataPreprocessingInstanceList}" status="i" var="erpDataPreprocessingInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    <g:showIdentifier instance="${erpDataPreprocessingInstance}"/>
                </td>
                <td>
                    <g:renderOntological
                            input="${erpDataPreprocessingInstance.event}" related="erpDataPreprocessing"/>
                </td>

                <td>
                    ${erpDataPreprocessingInstance.erpEpochLength}
                </td>

                <td>
                    ${erpDataPreprocessingInstance.baselineLength}
                </td>

                <td>
                    <g:renderOntological
                            input="${erpDataPreprocessingInstance.reference}" related="erpDataPreprocessing"/>
                </td>

                <td>
                    <g:renderOntological input="${erpDataPreprocessingInstance.cleaningTransformations}" delimiter=", "
                                         related="erpDataPreprocessing"/>
                </td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${erpDataPreprocessingInstanceTotal}" id="${experimentHeader?.id}"/>
    </div>
</div>
</body>
</html>
