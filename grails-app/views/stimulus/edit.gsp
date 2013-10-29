<%@ page import="edu.uoregon.nic.nemo.portal.Stimulus" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'stimulus.label', default: 'Stimulus')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
    <r:require module="jquery" />
</head>

<body>
<div id="edit-stimulus" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${stimulusInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${stimulusInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${stimulusInstance?.id}"/>
        <g:hiddenField name="version" value="${stimulusInstance?.version}"/>
        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:actionSubmit class="save" action="update"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            <g:link name="cancel" class="cancel" id="${stimulusInstance?.id}" action="show">Cancel</g:link>
        </fieldset>
    </g:form>
</div>
</body>
</html>
