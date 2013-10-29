<%@ page import="edu.uoregon.nic.nemo.portal.ErpDataPreprocessing" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpDataPreprocessing.label', default: 'ERP Data Preprocessing')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-erpDataPreprocessing" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                           default="Skip to content&hellip;"/></a>


<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                                  args="[entityName]"/></g:link></li>
            </sec:ifAllGranted>
            <li><g:link class="create" action="create" id="${erpDataPreprocessingInstance?.experiment?.id}"><g:message
                    code="default.new.label"
                    args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="show-erpDataPreprocessing" class="content scaffold-show" role="main">
    <h1>9. Erp Data Preprocessing ${erpDataPreprocessingInstance.identifier}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="associated-with">
        <strong>Associated Experiment</strong>
        <g:link controller="experiment" action="show" id="${erpDataPreprocessingInstance?.experiment?.id}">
            ${erpDataPreprocessingInstance?.experiment?.identifier}
        </g:link>
    </div>

    %{--<div style="background-color: #add8e6; padding: 25px; font-size: small; border-radius: 15px; width: 90%; text-align: left; margin: 0px auto;">--}%
    <div class="section-narrative">
        <g:toggleTextLength input="${erpDataPreprocessingInstance?.experiment?.erpDataPreprocessingsNarrativeSummary}"
                            maxLength="350"/>
    </div>
    <br/>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>
        <g:tableEntry key="ERP Data Preprocessing Set ID" number="9.1"
                      term="NEMO_4694000" value="${erpDataPreprocessingInstance.identifier}"/>
        <g:tableEntry key="Digital highpass filter transformation" number="9.2"
                      term="NEMO_1525000" value="${erpDataPreprocessingInstance.highpassFilterAlgorithm}" suffix=" Hz"/>
        <g:tableEntry key="Digital lowpass filter transformation" number="9.3"
                      term="NEMO_9612000" value="${erpDataPreprocessingInstance.lowpassFilterAlgorithm}" suffix=" Hz"/>
        %{--<g:tableEntry key="Data Cleaning Transformation" number="9.4"--}%
        %{--term="NEMO_1626000" yamlValue="${erpDataPreprocessingInstance.dataCleaningTransformation}"--}%
        %{--yamlDelimiter=" &bull; "/>--}%
        <g:tableEntry key="Data cleaning transformation" number="9.4" related="erpDataPreprocessing"
                      term="NEMO_1626000" ontological="${erpDataPreprocessingInstance.cleaningTransformations}"/>
        <g:tableEntry key="Erp event (for averaging)" number="9.5"
                      term="NEMO_6783000" ontological="${erpDataPreprocessingInstance.event}"/>
        <g:tableEntry key="Number EEG Segments (per condition)" number="9.6"
                      term="NEMO_2055000" value="${erpDataPreprocessingInstance.numberGoodTrials}"/>
        <g:tableEntry key="Erp epoch length" number="9.7"
                      term="NEMO_3620000" value="${erpDataPreprocessingInstance.erpEpochLength}" suffix=" ms"/>
        <g:tableEntry key="Erp baseline length" number="9.8"
                      term="NEMO_6232000" value="${erpDataPreprocessingInstance.baselineLength}" suffix=" ms"/>
        <g:tableEntry key="Offline reference" number="9.9" related="erpDataPreprocessing"
                      term="NEMO_2400000" ontological="${erpDataPreprocessingInstance.reference}"/>

    </table>

    <g:editable users="${erpDataPreprocessingInstance?.experiment?.laboratory?.users}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${erpDataPreprocessingInstance?.id}"/>
                <g:link class="edit" action="edit" id="${erpDataPreprocessingInstance?.id}"><g:message
                        code="default.button.edit.label" default="Edit"/></g:link>
                <g:actionSubmit class="delete" action="delete"
                                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </fieldset>
        </g:form>
        <br/>
    </g:editable>


    %{--<g:include view="dataFile/section.gsp" model="[experimentInstance: erpDataPreprocessingInstance.experiment]"/>--}%
    <g:include view="erpPatternExtraction/section.gsp"
               model="[experimentInstance: erpDataPreprocessingInstance.experiment
               ,erpPatternExtractions: erpDataPreprocessingInstance.erpPatternExtractions]"
    />
</div>
</body>
</html>
