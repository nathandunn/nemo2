<%@ page import="edu.uoregon.nic.nemo.portal.EegDataCollection" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>
<div id="edit-eegDataCollection" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${eegDataCollectionInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${eegDataCollectionInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${eegDataCollectionInstance?.id}"/>
        <g:hiddenField name="version" value="${eegDataCollectionInstance?.version}"/>
        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:actionSubmit class="save" action="update"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            <g:link name="cancel" class="cancel" id="${eegDataCollectionInstance?.id}" action="show">Cancel</g:link>
        </fieldset>
    </g:form>
</div>
</body>
</html>
