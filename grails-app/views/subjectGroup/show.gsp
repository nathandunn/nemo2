<%@ page import="edu.uoregon.nic.nemo.portal.SubjectGroup" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'subjectGroup.label', default: 'Subjects')}"/>
    <title>Subject Group ${subjectGroupInstance.identifier}</title>
    <r:require module="jquery"/>
</head>

<body>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li>
                    <g:link class="list" action="list">All ${entityName}</g:link>
                </li>
            </sec:ifAllGranted>
            <li><g:link class="create" action="create" id="${subjectGroupInstance?.experiment?.id}"><g:message
                    code="default.new.label"
                    args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div class="content scaffold-show" role="main">

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <g:if test="${subjectGroupInstance.unverifiedCopy}">
        <div class="message" role="warning">Unverified Copy</div>
    </g:if>

    <h1>
        4. Subject Group ${subjectGroupInstance.identifier}
    </h1>

    <g:if test="${subjectGroupInstance.unverifiedCopy}">
        <div class="error-with">
            <strong>Unverified</strong>
        </div>
    </g:if>

    <div class="associated-with">
        <strong>Associated Experiment</strong>
        Experiment <g:link controller="experiment" action="show"
                           id="${subjectGroupInstance?.experiment?.id}">${subjectGroupInstance?.experiment?.identifier}</g:link>
    </div>


    <div class="section-narrative">
        <g:toggleTextLength input="${subjectGroupInstance.experiment.subjectGroupsNarrativeSummary}"
                            maxLength="160"/>
    </div>

    <br/>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>

        <g:tableEntry key="Subject Group ID" number="4.1" term="NEMO_2014000"
                      value="${subjectGroupInstance.identifier}"/>
        <g:tableEntry key="Subject Group Size" number="4.2" term="NEMO_1365000" value="${subjectGroupInstance.groupSize}"/>
        <g:tableEntry key="Diagnostic Classification" number="4.3" term="NEMO_5159000"
                      ontological="${subjectGroupInstance.diagnosticClassifications}"/>
        <g:tableEntry key="Genus" number="4.4" term="NEMO_5621000" url="${subjectGroupInstance.genus}"/>
        <g:tableEntry key="Species" number="4.5" term="NEMO_3454000" url="${subjectGroupInstance.species}"/>
        <g:tableEntry key="Age (average)" number="4.6" term="NEMO_3819000" value="${subjectGroupInstance.groupAge}"/>
        <g:tableEntry key="Number male subjects" number="4.7" term="NEMO_3593000"
                      value="${subjectGroupInstance.numberMaleStudySubjectsRetainedForAnalysis}"/>
        <g:tableEntry key="Number right-handed subjects" number="4.8" term="NEMO_0814000"
                      value="${subjectGroupInstance.numberRightHandedStudySubjectsRetainedForAnalysis}"/>
        %{--<g:tableEntry key="Native Language (Modal)" number="4.9" term="NEMO_6266000"--}%
        %{--yamlValue="${subjectGroupInstance.groupLanguage}"/>--}%
        <g:tableEntry key="Native language (modal)" number="4.9" term="NEMO_6266000"
                      ontological="${subjectGroupInstance.languages}"/>
    </table>

    <g:editable users="${subjectGroupInstance.experiment?.laboratory?.users}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${subjectGroupInstance?.id}"/>
                <g:link class="edit" action="edit" id="${subjectGroupInstance?.id}"><g:message
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
