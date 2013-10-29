<%@ page import="edu.uoregon.nic.nemo.portal.Publication" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'publication.label', default: 'Publication')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                                  args="[entityName]"/></g:link></li>
            </sec:ifAllGranted>

            <g:each in="${publicationInstance.experiments}" var="experiment">
                <li><g:link class="create" action="create" id="${experiment.id}"><g:message
                        code="default.new.label"
                        args="[entityName]"/> for ${experiment.identifier}</g:link></li>
            </g:each>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="show-publication" class="content scaffold-show" role="main">
    <h1>3. Publication '${publicationInstance.titlePaper}'</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="associated-with">
        <strong>Associated Experiments</strong>
        <g:each in="${publicationInstance.experiments}" var="experiment">
            <g:link action="show" id="${experiment.id}"
                    controller="experiment">${experiment.identifier}</g:link>
        </g:each>
    </div>

    <div class="associated-with">
        <strong>Associated Authors</strong>
        <g:each in="${publicationInstance.authors}" var="author">
            <g:link action="show" id="${author.id}"
                    controller="secUser">${author.fullName}</g:link>
        </g:each>
    </div>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>

        <g:tableEntry key="Publication ID" number="3.1" term="NEMO_0038000" value="${publicationInstance.identifier}"/>
        <g:tableEntry key="Publication type" number="3.2" term="Narrative_Resource"
                      value="${publicationInstance?.type?.nameOnly}"/>
        <g:tableEntry key="First author family name" number="3.3" term="NEMO_3229000" value="${publicationInstance.familyName}"/>
        <g:tableEntry key="Publication year (YYYY)" number="3.4" term="NEMO_1264000"
                      value="${publicationInstance.publicationDate}"/>
        <g:tableEntry key="Paper title" number="3.5" term="NEMO_1010000" value="${publicationInstance.titlePaper}"/>
        <g:tableEntry key="Volume title" number="3.6" term="NEMO_5339000" value="${publicationInstance.titleVolume}"/>
        <g:tableEntry key="Digital Object Identifier (DOI)" number="3.7" term="NEMO_2062000"
                      publication="${publicationInstance}"/>

    </table>

    <g:editable users="${publicationInstance?.experiments?.laboratory?.users?.flatten()}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${publicationInstance?.id}"/>
                <g:link class="edit" action="edit" id="${publicationInstance?.id}"><g:message
                        code="default.button.edit.label" default="Edit"/></g:link>
                <g:actionSubmit class="delete" action="delete"
                                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </fieldset>
        </g:form>
    </g:editable>
</div>
</body>
</html>
