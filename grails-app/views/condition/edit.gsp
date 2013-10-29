<%@ page import="edu.uoregon.nic.nemo.portal.Condition" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'condition.label', default: 'Condition')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
    <r:require module="jquery" />
</head>

<body>

<div id="edit-condition" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${conditionInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${conditionInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:editable users="${conditionInstance?.experiment?.laboratory?.users}">
        <g:form method="post">
            <g:hiddenField name="id" value="${conditionInstance?.id}"/>
            <g:hiddenField name="version" value="${conditionInstance?.version}"/>
            <fieldset class="form">
                <g:render template="form"/>
            </fieldset>
            <fieldset class="buttons">
                <g:actionSubmit class="save" action="update"
                                value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                <g:link name="cancel" class="cancel" id="${conditionInstance?.id}" action="show">Cancel</g:link>
            </fieldset>
        </g:form>
    </g:editable>
</div>
</body>
</html>
