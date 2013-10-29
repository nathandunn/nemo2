<!doctype html>

<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpAnalysisResult.label', default: 'Erp Analysis Result')}"/>
    <title>
        <g:message code="default.show.label" args="[entityName]"/>
        ${erpAnalysisResultInstance.artifactFileName}
    </title>
    <r:require module="jquery"/>
</head>

<body>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                                  args="[entityName]"/>s</g:link></li>
            </sec:ifAllGranted>
            <li><g:link class="create" action="create" id="${erpAnalysisResultInstance?.experiment?.id}"><g:message
                    code="default.new.label" args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="show-erpAnalysisResult" class="content scaffold-show" role="main">
    <h1>11. Erp Analysis Result ${erpAnalysisResultInstance.artifactFileName}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="associated-with">
        <g:if test="${erpAnalysisResultInstance?.erpPatternExpression}">
            <strong>ERP Pattern Extraction</strong>
            <g:link controller="erpPatternExtraction" action="show"
                    id="${erpAnalysisResultInstance?.erpPatternExpression?.id}">${erpAnalysisResultInstance?.erpPatternExpression?.artifactFileName}</g:link>
            <br/>
        </g:if>

        <g:if test="${erpAnalysisResultInstance?.experiment}">
            <strong>Associated Experiment</strong>
            <g:link controller="experiment" action="show"
                    id="${erpAnalysisResultInstance?.experiment?.id}">${erpAnalysisResultInstance?.experiment?.identifier}</g:link>
        </g:if>
    </div>

    <div class="section-narrative">
        <g:toggleTextLength input="${erpAnalysisResultInstance.experiment.erpAnalysisResultNarrativeSummary}"
                            maxLength="350"/>
    </div>
    <br/>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>
        %{--<g:tableEntry key="ERP Analysis Results ID" number="11.1"--}%
                      %{--term="NEMO_7123457" value="${erpAnalysisResultInstance.artifactFileName}"/>--}%

        %{--statistical threshold quality (corrected, uncorrected…)--}%
        <td class="table-uri">
            %{--<g:renderLabel number="11.8" term="NEMO_3087000"/>--}%
            <g:renderLabel number="11.1" term="NEMO_7123457"/>
        </td>
        <td class="table-key">
            ERP Analysis Results ID
        </td>
        <td class="table-value">
            <g:if test="${erpAnalysisResultInstance.artifactFileName}">
                <g:link controller="erpAnalysisResult" action="download"
                        id="${erpAnalysisResultInstance.id}">${erpAnalysisResultInstance.artifactFileName} <g:img
                        dir="images/icon" file="download2.png"/></g:link>

                <g:if test="${!erpAnalysisResultInstance.isRdfInProcess()}">
                    <sec:ifAllGranted roles="ROLE_ADMIN">
                        &nbsp;
                        <g:link controller="erpAnalysisResult" action="reinfer"
                                id="${erpAnalysisResultInstance.id}">Re-infer <g:img
                                dir="images/icon/16px" file="redo.png"/></g:link>
                    </sec:ifAllGranted>
                </g:if>

                <br/>

                <div id="showUrl${erpAnalysisResultInstance.id}" class="link-like">Show URL +</div>

                <div id="hideUrl${erpAnalysisResultInstance.id}" class="link-like">Hide URL -</div>

                <div id="downloadUrl${erpAnalysisResultInstance.id}">
                    <g:createLink absolute="true" id="${erpAnalysisResultInstance.id}" action="download"
                                  controller="erpAnalysisResult"/>
                </div>
                <r:script>
                        jQuery('#hideUrl${erpAnalysisResultInstance.id}').hide();
                        jQuery('#downloadUrl${erpAnalysisResultInstance.id}').hide();


                        jQuery("#showUrl${erpAnalysisResultInstance.id}").click(function(){
                            jQuery("#downloadUrl${erpAnalysisResultInstance.id}").show("slow");
                            jQuery("#showUrl${erpAnalysisResultInstance.id}").hide();
                            jQuery("#hideUrl${erpAnalysisResultInstance.id}").show();
                        });

                        jQuery("#hideUrl${erpAnalysisResultInstance.id}").click(function(){
                            jQuery("#downloadUrl${erpAnalysisResultInstance.id}").hide("slow");
                            jQuery("#hideUrl${erpAnalysisResultInstance.id}").hide();
                            jQuery("#showUrl${erpAnalysisResultInstance.id}").show();
                        });
                </r:script>
            </g:if>
            <g:elseif test="${erpAnalysisResultInstance.download}">
                <g:link action="download" controller="erpAnalysisResult" id="${erpAnalysisResultInstance.id}">
                    ${erpAnalysisResultInstance?.artifactFileName?.replaceAll("_", " ")}<g:img dir="images/icon"
                                                                                               file="download2.png"/></g:link>
            </g:elseif>
            <g:else>
                <div style="color: #808080;display: inline-block;">${erpAnalysisResultInstance?.artifactFileName?.replaceAll("_", " ")} (Empty)</div>
            </g:else>

            <br/>
            Last upload:
            <g:if test="${erpAnalysisResultInstance.lastUploaded}">
                <g:formatDate date="${erpAnalysisResultInstance.lastUploaded}" type="datetime" style="LONG"
                              timeStyle="SHORT"/>
            </g:if>
            <g:else>
                N/A
            </g:else>

            <g:if test="${erpAnalysisResultInstance.isRdfInProcess()}">
                <br/>
                Classifying ... can not upload new file
                Started <g:formatDate date="${erpAnalysisResultInstance.startClassification}" type="datetime"
                                          style="LONG"
                                          timeStyle="SHORT"/>
            </g:if>
            <g:else>
                <g:if test="${erpAnalysisResultInstance.startClassification && erpAnalysisResultInstance.endClassification}">
                    <br/>
                    Last classification:
                    <g:formatDate date="${erpAnalysisResultInstance.endClassification}" type="datetime"
                                  dateStyle="LONG"
                                  timeStyle="SHORT"/>
                    - ${erpAnalysisResultInstance.processingMinutes()} minutes
                </g:if>
            %{--<g:uploadForm action="upload" >--}%
            %{--<g:hiddenField name="id" value="${erpAnalysisResultInstance.id}"/>--}%
            %{--<input type="file" name="newRdf"/>--}%
            %{--<input type="submit" value="Upload"/>--}%
            %{--</g:uploadForm>--}%
            </g:else>
        </td>

        %{--<g:tableEntry key="Condition of Interest" number="11.2" related="erpAnalysisResult"--}%
                      %{--term="NEMO_2494000" ontological="${erpAnalysisResultInstance.dependentVariable}"/>--}%
        %{--<g:tableEntry key="Baseline Condition" number="11.3" related="erpAnalysisResult"--}%
                      %{--term="NEMO_9335000" ontological="${erpAnalysisResultInstance.independentVariable}"/>--}%
        <g:tableEntry key="Condition of Interest" number="11.2" related="erpAnalysisResult"
                      term="NEMO_2494000" ontological=""/>
        <g:tableEntry key="Baseline Condition" number="11.3" related="erpAnalysisResult"
                      term="NEMO_9335000" ontological=""/>
        <g:tableEntry key="Statistical Analysis Method (ANOVA, ttest, etc.)" number="11.4" related="erpAnalysisResult"
                      term="NEMO_6879000" ontological="${erpAnalysisResultInstance.analysisMethod}"/>
        <g:tableEntry key="Statistical Significance Threshold" number="11.5"
                      term="NEMO_8259000" value="${erpAnalysisResultInstance.significanceThreshold}"/>
        <g:tableEntry key="Statistical Threshold Quality (corrected, uncorrected…)" number="11.6"
                      term="NEMO_6587000" ontological="${erpAnalysisResultInstance.thresholdQuality}"/>

        <g:tableEntry key="Data file format" number="11.7"
                      term="NEMO_1194000" ontological="${erpAnalysisResultInstance.format}"/>

        <g:tableEntry key="Data file contents" number="11.8"
                      term="IAO_0000100" ontological="${erpAnalysisResultInstance.set}"/>
        %{--<g:tableEntry key="Artifact File Name" number="11.4"--}%
        %{--term="NEMO_3087000" url="${erpAnalysisResultInstance.dataFormat}"/>--}%

        %{--<tr>--}%
        %{--</tr>--}%

    </table>


    <g:editable users="${erpAnalysisResultInstance?.experiment?.laboratory?.users}">
        <g:if test="${!erpAnalysisResultInstance.isRdfInProcess()}">
            <g:form>
                <fieldset class="buttons">
                    <g:hiddenField name="id" value="${erpAnalysisResultInstance?.id}"/>
                    <g:link class="edit" action="edit" id="${erpAnalysisResultInstance?.id}">
                        <g:message code="default.button.edit.label" default="Edit"/>
                    </g:link>
                    <g:actionSubmit class="delete" action="delete"
                                    value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                </fieldset>
            </g:form>
        </g:if>
    </g:editable>

    <g:include controller="erpAnalysisResult" action="classify" id="${erpAnalysisResultInstance.id}"
               params="[edit: false]"/>

    <div id="classificationBox${erpAnalysisResultInstance.id}"></div>

</div>

</body>
</html>
