<fieldset>
    <legend>11. ERP Analysis Results</legend>

    <div class="section-narrative">
        <g:toggleTextLength input="${experimentInstance.erpAnalysisResultNarrativeSummary}" maxLength="160"/>
    </div>
    <br/>

    %{--<span class="fit-table" aria-labelledby="erpAnalysisResults-label">--}%
    %{--<table class="fit-table">--}%
    <table class="center-table">
        <thead>
        <tr>
            %{--<th>ID</th>--}%
            %{--<th>Download File</th>--}%
            <th>File</th>
            <th>Data File Contents</th>
            <th>Condition of Interest</th>
            <th>Baseline Condition</th>
            <th>Analysis Method</th>
            %{--<th>Data Format</th>--}%
            <th>Classification</th>
        </tr>
        </thead>
    %{--<g:each in="${experimentInstance.erpDataPreprocessings}" var="erpDataPreprocessing" status="st1">--}%
        <r:require module="jquery"/>
        <g:set var="erpAnalysisResults" value="${erpAnalysisResults?.size()==0? [] : erpAnalysisResults }"/>
        <g:each in="${erpAnalysisResults?.sort{ a,b -> a.artifactFileName < b.artifactFileName ? -1 : 1 } }" var="erpAnalysisResult" status="st2">
            <tr>
                <td>
                    <g:link action="show" controller="erpAnalysisResult"
                            id="${erpAnalysisResult.id}">${fieldValue(bean: erpAnalysisResult, field: "artifactFileName")?.replaceAll("_", " ")}</g:link>
                    <g:if test="${erpAnalysisResult.isRdfAvailable()}">
                        <g:link controller="erpAnalysisResult" action="download"
                                id="${erpAnalysisResult.id}"> <g:img
                                dir="images/icon" file="download2.png"/></g:link>
                    </g:if>
                    <g:else>
                        <div style="color: graytext;">${erpAnalysisResult.artifactFileName} Not uploaded</div>
                    </g:else>
                </td>


                <td>
                    <g:renderOntological input="${erpAnalysisResult.set}" related="erpAnalysisResult"/>
                </td>

                <td>
                    %{--<g:renderOntological input="${erpAnalysisResult.dependentVariable}" related="erpAnalysisResult"/>--}%
                </td>

                <td>
                    %{--<g:renderOntological input="${erpAnalysisResult.independentVariable}" related="erpAnalysisResult"/>--}%
                </td>

                <td>
                    <g:renderOntological input="${erpAnalysisResult.analysisMethod}" related="erpAnalysisResult"/>
                </td>


                <td>
                    <g:if test="${erpAnalysisResult?.isRdfAvailable()}">
                        <r:script>
                        %{--<g:remoteFunction action="classify" update="classificationBox${erpAnalysisResult.id}"--}%
                        %{--asynchronous="false"--}%
                        %{--id="${erpAnalysisResult.id}"--}%
                        %{--controller="experiment"/>--}%
                            jQuery('#classificationRow${erpAnalysisResult.id}').hide();
                                jQuery('#classificationLabel${erpAnalysisResult.id}').click(function() {
                                  jQuery('#classificationRow${erpAnalysisResult.id}').toggle('slow', function() {
                                         var element = document.getElementById('classificationLink${erpAnalysisResult.id}');
//                                         console.log(element.innerHTML)
//                                         console.log(element.innerHTML.indexOf('Hide'))
                                         if(element.innerHTML.indexOf("Hide")==0){
                                             element.innerHTML = 'Show';
                                         }
                                         else{
                                             element.innerHTML = 'Hide';
                                         }
                                  });
                                });

                                  jQuery('#classificationRow${erpAnalysisResult.id}').hide();
                        </r:script>

                        <div id="classificationLabel${erpAnalysisResult.id}">
                            <a id="classificationLink${erpAnalysisResult.id}"
                               href="javascript:;" onclick="
                                <g:remoteFunction action="classify" update="classificationBox${erpAnalysisResult.id}"
                                asynchronous="true"
                                id="${erpAnalysisResult.id}"
                                controller="erpAnalysisResult"/>
                            ">Show</a>
                        </div>

                    </g:if>
                    <g:elseif test="${erpAnalysisResult.isRdfError()}">
                        <div class="error">There was an Error processing the RDF</div>
                        <br/>
                        <a href="">Reprocess via jQuery?</a>
                    </g:elseif>
                    <g:elseif test="${erpAnalysisResult.isRdfInProcess()}">
                        Processing ...
                    </g:elseif>

                </td>
            </tr>
            <g:if test="${erpAnalysisResult.isRdfAvailable()}">
                <tr id="classificationRow${erpAnalysisResult.id}" class="nohover">
                    <td colspan="6"><div id="classificationBox${erpAnalysisResult.id}"></div></td>
                </tr>
            </g:if>
        </g:each>
    </table>
    <g:editable users="${experimentInstance?.laboratory?.users}">
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <div class="nav" style="width: auto;">
                <g:link class="create" action="create" controller="erpAnalysisResult" id="${experimentInstance.id}">
                    New Data File
                </g:link>
            </div>
        </sec:ifAllGranted>
    </g:editable>
</fieldset>

