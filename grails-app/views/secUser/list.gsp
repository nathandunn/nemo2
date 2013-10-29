<%@ page import="edu.uoregon.nic.nemo.portal.SecUser" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'secUser.label', default: 'SecUser')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

%{--<div class="nav" role="navigation">--}%
    %{--<ul>--}%
        %{--<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>--}%
        %{--<li><g:link class="create" action="create"><g:message code="default.new.label"--}%
                                                              %{--args="[entityName]"/></g:link></li>--}%
    %{--</ul>--}%
%{--</div>--}%

<div id="list-secUser" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="username"
                              title="${message(code: 'secUser.username.label', default: 'Username')}"/>

            <th><g:message code="secUser.laboratory.label" default="Laboratories"/></th>
            %{--<g:sortableColumn property="laboratories.identifier"--}%
                              %{--title="${message(code: 'secUser.username.label', default: 'Laboratory')}"/>--}%

            <g:sortableColumn property="accountExpired"
                              title="${message(code: 'secUser.accountExpired.label', default: 'Account Expired')}"/>

            <g:sortableColumn property="accountLocked"
                              title="${message(code: 'secUser.accountLocked.label', default: 'Account Locked')}"/>

            <g:sortableColumn property="enabled" title="${message(code: 'secUser.enabled.label', default: 'Enabled')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${secUserInstanceList}" status="i" var="secUserInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${secUserInstance.id}">${fieldValue(bean: secUserInstance, field: "username")}</g:link></td>

                <td>
                    <g:each var="laboratory" in="${secUserInstance?.laboratories}">
                        <g:link action="show" controller="laboratory" id="${laboratory?.id}">
                            ${laboratory?.identifier}
                        </g:link>
                    </g:each>
                </td>

                <td><g:formatBoolean boolean="${secUserInstance.accountExpired}"/></td>

                <td><g:formatBoolean boolean="${secUserInstance.accountLocked}"/></td>

                <td><g:formatBoolean boolean="${secUserInstance.enabled}"/></td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${secUserInstanceTotal}"/>
    </div>
</div>
</body>
</html>
