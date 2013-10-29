<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpPatternExtraction.label', default: 'File')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
    <r:require module="jquery"/>
</head>

<body>

<div id="edit-erpPatternExtraction" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${erpPatternExtractionInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${erpPatternExtractionInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${erpPatternExtractionInstance?.id}"/>
        <g:hiddenField name="version" value="${erpPatternExtractionInstance?.version}"/>
        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:actionSubmit class="save" action="update"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            %{--<g:submitButton name="Update" class="save" action="update" --}%
            %{--value="${message(code: 'default.button.update.label', default: 'Update')}"/>--}%
            <g:link name="cancel" class="cancel" controller="erpPatternExtraction"
                    id="${erpPatternExtractionInstance.id}"
                    action="show">Cancel</g:link>
            %{--<sec:ifAnyGranted roles="ROLE_ADMIN">--}%

            %{--</sec:ifAnyGranted>--}%
        </fieldset>
    </g:form>

    <div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'artifactFileName', 'error')} required">
        %{--<g:propertyEntry number="11.1" term="NEMO_3087000" key="Artifact File Name"/>--}%
        %{--<g:link controller="experiment" action="download"--}%
        %{--id="${erpPatternExtractionInstance.id}">${erpPatternExtractionInstance.artifactFileName}</g:link>--}%

        <div class="property-value">
                <g:uploadForm action="upload">
                    <g:hiddenField name="id" value="${erpPatternExtractionInstance.id}"/>
                    <input type="file" name="newRdf"/>
                    <input type="submit" value="Upload"/>
                </g:uploadForm>
        </div>
    </div>

    %{--<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'artifactFileName', 'error')} required">--}%
        %{--<g:propertyEntry number="10.4" term="NEMO_3087000" key="Artifact File Name"/>--}%
        %{--${erpPatternExtractionInstance?.artifactFileName?.replaceAll("_", " ")}--}%

        %{--<div class="property-value">--}%

            %{--Include download, etc. here.--}%
        %{--</div>--}%
    %{--</div>--}%

</div>
</body>
</html>
