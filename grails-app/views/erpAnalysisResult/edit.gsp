<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpAnalysisResult.label', default: 'File')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
    <r:require module="jquery"/>
</head>

<body>

<div id="edit-erpAnalysisResult" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${erpAnalysisResultInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${erpAnalysisResultInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${erpAnalysisResultInstance?.id}"/>
        <g:hiddenField name="version" value="${erpAnalysisResultInstance?.version}"/>
        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:actionSubmit class="save" action="update"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            %{--<g:submitButton name="Update" class="save" action="update" --}%
            %{--value="${message(code: 'default.button.update.label', default: 'Update')}"/>--}%
            <g:link name="cancel" class="cancel" controller="erpAnalysisResult" id="${erpAnalysisResultInstance.id}"
                    action="show">Cancel</g:link>
            %{--<sec:ifAnyGranted roles="ROLE_ADMIN">--}%

            %{--</sec:ifAnyGranted>--}%
        </fieldset>
    </g:form>


    <div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'artifactFileName', 'error')} required">
        %{--<g:propertyEntry number="11.1" term="NEMO_3087000" key="Artifact File Name"/>--}%
        %{--<g:link controller="experiment" action="download"--}%
                %{--id="${erpAnalysisResultInstance.id}">${erpAnalysisResultInstance.artifactFileName}</g:link>--}%

        <div class="property-value">
            Last upload:
            <g:if test="${erpAnalysisResultInstance.lastUploaded}">
                <g:formatDate date="${erpAnalysisResultInstance.lastUploaded}" type="datetime" style="LONG"
                              timeStyle="SHORT"/>
            </g:if>
            <g:else>
                N/A
            </g:else>

            <g:if test="${erpAnalysisResultInstance.isRdfInProcess()}">
                Classifying ... can not upload new file
                Started <g:formatDate date="${erpAnalysisResultInstance.startClassification}" type="datetime"
                                      style="LONG"
                                      timeStyle="SHORT"/>
            </g:if>
            <g:else>
                <g:if test="${erpAnalysisResultInstance.startClassification && erpAnalysisResultInstance.endClassification}">
                    <br/>
                    Classification Time
                    ${erpAnalysisResultInstance.processingMinutes()} minutes
                </g:if>
                <g:uploadForm action="upload">
                    <g:hiddenField name="id" value="${erpAnalysisResultInstance.id}"/>
                    <input type="file" name="newRdf"/>
                    <input type="submit" value="Upload"/>
                </g:uploadForm>
            </g:else>
        </div>
    </div>

    <div class="fieldcontain">
        <g:include controller="erpAnalysisResult" action="classify" id="${erpAnalysisResultInstance.id}"
                   params="[edit: true]"/>
        %{--<r:script>--}%
        %{--<g:remoteFunction action="classify" update="classificationBox${erpAnalysisResultInstance.id}"--}%
        %{--asynchronous="false"--}%
        %{--id="${erpAnalysisResultInstance.id}"--}%
        %{--params="[edit: true]"--}%
        %{--controller="experiment"/>--}%
        %{--</r:script>--}%
    </div>

    <div id="classificationBox${erpAnalysisResultInstance.id}"></div>

</div>
</body>
</html>
