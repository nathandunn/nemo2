<%@ page import="edu.uoregon.nic.nemo.portal.Publication" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'publication.label', default: 'Publication')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<sec:ifAllGranted roles="ROLE_ADMIN">
    <g:if test="${experimentHeader?.id}">
        <div class="nav" role="navigation">
            <ul>
                %{--<li> <g:link class="list" action="list" >All ${entityName}</g:link> </li>--}%
                <li><g:link class="create" action="create" id="${experimentHeader?.id}"><g:message
                        code="default.new.label"
                        args="[entityName]"/></g:link></li>
            </ul>
        </div>
    </g:if>
</sec:ifAllGranted>


<div id="list-publication" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="identifier"
                              title="${message(code: 'publication.identifier.label', default: 'Identifier')}"/>
            <th>
                Publication Type
            </th>
            <g:sortableColumn property="publicationDate"
                              title="${message(code: 'publication.year.label', default: 'Publication Year')}"/>

            <g:sortableColumn property="digitalObjectIdentifier"
                              title="${message(code: 'publication.digitalObjectIdentifier.label', default: 'Digital Object Identifier')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${publicationInstanceList}" status="i" var="publicationInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${publicationInstance.id}">${fieldValue(bean: publicationInstance, field: "identifier")}</g:link></td>

                <td>
                    <a href="${publicationInstance.type.url}">
                        ${publicationInstance.type.nameOnly}
                    </a>
                </td>

                <td>
                    <g:formatNumber number="${publicationInstance.publicationDate}"/>
                </td>

                <td>
                    %{--${fieldValue(bean: publicationInstance, field: "digitalObjectIdentifier")}--}%

                    <g:if test="${publicationInstance.digitalObjectIdentifier?.startsWith("PMID")}">
                        <g:link target="_blank" class="external-link"
                                url="http://www.ncbi.nlm.nih.gov/pubmed/${publicationInstance.digitalObjectIdentifier.substring(6)}">${publicationInstance.digitalObjectIdentifier}</g:link>

                    </g:if>
                    <g:elseif test="${publicationInstance.digitalObjectIdentifier}">
                        <g:link target="_blank" class="external-link"
                                url="http://dx.doi.org/${publicationInstance.digitalObjectIdentifier}">${publicationInstance.digitalObjectIdentifier}</g:link>
                    </g:elseif>
                </td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${publicationInstanceTotal}" id="${experimentHeader?.id}"/>
    </div>
</div>
</body>
</html>
