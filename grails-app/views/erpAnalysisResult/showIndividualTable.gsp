<%@ page import="edu.uoregon.nic.nemo.portal.client.BrainLocationEnum" %>
<!doctype html>

<head>

</head>

<body>


<div id="${currentTime}">

    %{--${link}--}%
    %{--<g:link controller="erpAnalysisResult" id="${erpAnalysisResultInstance.id}#${erpAnalysisResultInstance.artifactFileName}" action="show">--}%
    <div class="table-legend">
        <g:link controller="erpAnalysisResult" id="${erpAnalysisResultInstance.id}" fragment="${link}" action="show">
            Experimental Contrast at ${currentTime} (ms)
        </g:link>
    </div>
    <br/>

    <table class="center-detail-table">
        <tbody>
        <tr>
            <th></th>
            <th>
                Left temporal
            </th>
            <th>
                Left
            </th>
            <th>
                Mid
            </th>
            <th>
                Right
            </th>
            <th>
                Right temporal
            </th>
        </tr>
        <tr>
            <th>
                Frontal
            </th>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.LFTEMP]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.LFRONT]"/>

            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.MFRONT]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.RFRONT]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.RFTEMP]"/>
            </td>
        </tr>
        <tr>
            <th>
                Central
            </th>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.LCTEMP]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.LCENT]"/>

            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.MCENT]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.RCENT]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.RCTEMP]"/>
            </td>
        </tr>
        <tr>
            <th>
                Parietal
            </th>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.LPTEMP]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.LPAR]"/>

            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.MPAR]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.RPAR]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.RPTEMP]"/>
            </td>
        </tr>
        <tr>
            <th>
                Occipital
            </th>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.LOTEMP]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.LOCC]"/>

            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.MOCC]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.ROCC]"/>
            </td>
            <td>
                <g:render template="meanIntensity" model="[location: BrainLocationEnum.ROTEMP]"/>
            </td>
        </tr></tbody>

    </table>
</div>

</body>
