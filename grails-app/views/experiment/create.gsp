<%@ page import="edu.uoregon.nic.nemo.portal.Experiment" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'experiment.label', default: 'Experiment')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<div id="create-experiment" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${experimentInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${experimentInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>


    <g:if test="${experimentInstance?.laboratory?.id || availableLabs}">
        <g:form action="save">
            <fieldset class="form">
                <g:render template="form"/>
            </fieldset>
            <fieldset class="buttons">
                <g:submitButton name="create" class="save"
                                value="${message(code: 'default.button.create.label', default: 'Create')}"/>
                <g:cancelToHome/>
            </fieldset>
        </g:form>
    </g:if>
    <g:else>
        <div class="errors">
            Please <g:link controller="secUser" action="edit">associate yourself with a lab</g:link> to then create an experiment.
        </div>
    </g:else>


%{--<g:form action="save" >--}%
%{--<fieldset class="form">--}%
%{--<g:render template="form"/>--}%
%{--</fieldset>--}%
%{--<fieldset class="buttons">--}%
%{--<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />--}%
%{--<g:cancelToHome/>--}%
%{--</fieldset>--}%
%{--</g:form>--}%
</div>
</body>
</html>
