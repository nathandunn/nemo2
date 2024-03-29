<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpAnalysisResult.label', default: 'File')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>


<div id="create-erpAnalysisResult" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
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
    <g:form method="post" action="save">
        <g:hiddenField name="id" value="${erpAnalysisResultInstance?.id}"/>
        <g:hiddenField name="version" value="${erpAnalysisResultInstance?.version}"/>
        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
            <g:link name="cancel" class="cancel" controller="erpAnalysisResult" action="list">Cancel</g:link>
        </fieldset>
    </g:form>
%{--<g:uploadForm action="save">--}%
%{--<fieldset class="form">--}%
%{--<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'experiment', 'error')} required">--}%
%{--<g:propertyEntry key="Experiment ID" number="2.1" term="NEMO_0000537"/>--}%
%{--<g:showIdentifier instance="${experimentInstance}"/>--}%
%{--<g:select id="experiment" name="experiment.id"--}%
%{--from="${edu.uoregon.nic.nemo.portal.Experiment.listOrderByIdentifier()}"--}%
%{--optionKey="id" required="" optionValue="identifier"--}%
%{--value="${erpAnalysisResultInstance?.experiment?.id}" class="many-to-one"/>--}%
%{--</div>--}%


%{--<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'erpDataPreprocessing', 'error')} ">--}%
%{--<g:propertyEntry key="ERP Data Preprocessing Set ID" number="9.1"--}%
%{--term="NEMO_4694000"/>--}%
%{--<g:select id="erpDataPreprocessing" name="erpDataPreprocessing.id"--}%
%{--from="${edu.uoregon.nic.nemo.portal.ErpDataPreprocessing.listOrderByIdentifier()}"--}%
%{--optionKey="id" optionValue="identifier"--}%
%{--value="${erpAnalysisResultInstance?.erpDataPreprocessing?.id}"--}%
%{--class="many-to-one"--}%
%{--noSelection="['null': '']"/>--}%
%{--</div>--}%


%{--<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'identifier', 'error')}  required">--}%
%{--<g:propertyEntry key="Data File ID" number="10.1" term="NEMO_9799000" required="true"/>--}%
%{--<g:textField name="identifier" value="${erpAnalysisResultInstance?.identifier}"/>--}%
%{--</div>--}%

%{--<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'dataSet', 'error')}  required">--}%
%{--<g:propertyEntry key="Data File Contents" number="10.2" term="IAO_0000100"/>--}%
%{--<g:textField name="artifactContentType" value="${erpAnalysisResultInstance?.artifactContentType}"/>--}%
%{--<g:select id="dataSet" name="dataSet"--}%
%{--from="${dataSets}"--}%
%{--optionKey="key" optionValue="value"--}%
%{--value="${erpAnalysisResultInstance?.dataSet}"--}%
%{--class="many-to-one"/>--}%
%{--</div>--}%

%{--<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'dataFormat', 'error')}  required">--}%
%{--<g:propertyEntry key="Data Format" number="10.3" term="NEMO_1194000"--}%
%{--url="${erpAnalysisResultInstance.dataFormat}"/>--}%
%{--<g:select id="dataFormat" name="dataFormat"--}%
%{--from="${dataFormats}"--}%
%{--optionKey="key" optionValue="value"--}%
%{--value="${erpAnalysisResultInstance?.dataFormat}"--}%
%{--class="many-to-one"/>--}%
%{--</div>--}%

%{--<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'artifactFileName', 'error')} required">--}%
%{--<g:propertyEntry number="10.4" term="NEMO_3087000" key="Artifact File Name"/>--}%
%{--<input type="file" name="newRdf"/>--}%
%{--</div>--}%

%{--</fieldset>--}%

%{--<fieldset class="buttons">--}%
%{--<g:submitButton name="create" class="save"--}%
%{--value="${message(code: 'default.button.create.label', default: 'Create')}"/>--}%
%{--<g:link name="cancel" class="cancel" controller="erpAnalysisResult"--}%
%{--id="${erpAnalysisResultInstance?.experiment?.id}" action="show">Cancel</g:link>--}%
%{--</fieldset>--}%
%{--</g:uploadForm>--}%
</div>
</body>
</html>
