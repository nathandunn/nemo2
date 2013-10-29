<%@ page import="edu.uoregon.nic.nemo.portal.Laboratory" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'laboratory.label', default: 'Laboratory')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            %{--<li><g:link controller="experiment" action="list"><g:message code="default.home.label"--}%
                                                                         %{--args="[entityName]"/></g:link></li>--}%
            %{--<li><g:link controller="laboratory" action="list"><g:message code="default.laboratory.label"--}%
                                                                         %{--default="Laboratories"/></g:link></li>--}%
            %{--<li><g:link controller="instance" action="show" id="NEMO_3056000"><g:message code="default.instance.label"--}%
                                                                                         %{--default="Instances"/></g:link></li>--}%
            <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                  args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>
<div id="list-laboratory" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            %{--<th>--}%
                %{--${message(code: 'laboratory.identifier.label', default: 'Lab ID')}--}%
            %{--</th>--}%
            <g:sortableColumn property="identifier" title="${message(code: 'laboratory.identifier.label', default: 'Identifier')}" />
            <th>
                ${message(code: 'laboratory.institution.label', default: 'Institution')}
            </th>

            %{--<g:sortableColumn property="institution" title="${message(code: 'laboratory.institution.label', default: 'Institution')}" />--}%

            <th>
                ${message(code: 'laboratory.principalInvestigatorRole.label', default: 'Principal investigator (PI)')}
            </th>
            %{--<g:sortableColumn property="principalInvestigatorPostalAddress" title="${message(code: 'laboratory.principalInvestigatorPostalAddress.label', default: 'Principal Investigator Postal Address')}" />--}%

        </tr>
        </thead>
        <tbody>
        <g:each in="${laboratoryInstanceList}" status="i" var="laboratoryInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    <g:link controller="laboratory" action="show" id="${laboratoryInstance.id}">
                        ${fieldValue(bean: laboratoryInstance, field: "identifier")}
                    </g:link>
                </td>

                <td>
                    <g:if test="${laboratoryInstance.institution}">
                        ${laboratoryInstance.institution}
                        %{--<g:renderUrl input="${laboratoryInstance.institution}"/>--}%
                    </g:if>
                    <g:else>
                        <span style="color: #d3d3d3;">[missing]</span>
                    </g:else>
                </td>

                <td>
                    <g:if test="${laboratoryInstance.principalInvestigatorRole}">
                        %{--<g:renderAnnotator input="${laboratoryInstance.principalInvestigatorRole}"/>--}%
                        ${laboratoryInstance.principalInvestigatorRole}
                    </g:if>
                    <g:else>
                        <span style="color: #d3d3d3;">[missing]</span>
                    </g:else>
                %{--${fieldValue(bean: laboratoryInstance, field: "principalInvestigatorRole")}--}%
                </td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${laboratoryInstanceTotal}"/>
    </div>
</div>
</body>
</html>
