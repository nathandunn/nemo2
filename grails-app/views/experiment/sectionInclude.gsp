%{--<div class="summary-section internal-section">--}%
%{--<g:toggleTextLength input="${experimentInstance?.narrativeSummary}" maxLength="160"/>--}%
%{--</div>--}%
<div class="section-narrative">
    <g:toggleTextLength input="${experimentInstance?.narrativeSummary}" maxLength="160"/>
</div>
<br/>

%{--<table class="show-grid">--}%
<table class="center-detail-table">
    <g:render template="/detail-table-header"/>
    %{--<g:include view="detail-table-header" />--}%
    %{--<thead>--}%
    %{--<tr>--}%
    %{--<th class="table-reference-header">Reference</th>--}%
    %{--<th class="table-label-header">Label</th>--}%
    %{--<th class="table-value-header">Value</th>--}%
    %{--</tr>--}%
    %{--</thead>--}%

    <g:tableEntry key="Experiment ID" number="2.1" term="NEMO_0000537" value="${experimentInstance.identifier}"/>
    %{--<g:tableEntry key="Experimental Paradigm" number="2.2" term="NEMO_0000379"--}%
    %{--yamlValue="${experimentInstance.experimentalParadigm}"--}%
    %{--yamlDelimiter="&nbsp;&bull;&nbsp;"/>--}%
    <g:tableEntry key="Experimental Paradigm" number="2.2" term="NEMO_0000379"
                  ontological="${experimentInstance.experimentalParadigms}"/>
    %{--yamlDelimiter="&nbsp;&bull;&nbsp;"/>--}%
    <tr>
        <td class="table-uri">
            <g:renderLabel number="2.3" term="NEMO_8539000"/>
        </td>
        <td class="table-key">
            Start Date
        </td>
        <td class="table-value">
            <g:if test="${experimentInstance.startDateForDataCollection}">
                <g:formatNumber number="${experimentInstance.startDateForDataCollection}"/>
            </g:if>
            <g:else>
                ?
            </g:else>
        </td>

    </tr>
    <tr>
        <td class="table-uri">
            <g:renderLabel number="2.4" term="NEMO_0917000"/>
        </td>
        <td class="table-key">
            End Date
        </td>
        <td class="table-value">
            <g:if test="${experimentInstance.endDateForDataCollection}">
                <g:formatNumber number="${experimentInstance.endDateForDataCollection}"/>
            </g:if>
            <g:else>
                ?
            </g:else>
        </td>

    </tr>

</table>
