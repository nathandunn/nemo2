%{--http://localhost:8080/nemo/erpAnalysisResult/showIndividuals/14?time=172--}%

<strong>Peak Effect Times (ms)</strong>
<g:if test="${instances}">
    <g:each var="instance" in="${instances}">
        <g:link action="showIndividuals" id="${instance.value}"
                controller="erpAnalysisResult" params="[time: instance.key]">${instance.key}</g:link>
        &nbsp;
    </g:each>
</g:if>
<g:else>
  None
</g:else>

