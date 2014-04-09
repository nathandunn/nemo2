<%@ page import="edu.uoregon.nic.nemo.portal.ErpDataPreprocessing" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessing')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<sec:ifAllGranted roles="ROLE_ADMIN">
    <div class="nav" role="navigation">
        <ul>
            <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                              args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>
<div id="create-erpDataPreprocessing" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${erpDataPreprocessingInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${erpDataPreprocessingInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form action="save">
        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
            <g:link name="cancel" class="cancel" action="list">Cancel</g:link>
        </fieldset>
    </g:form>
</div>
</body>
</html>
