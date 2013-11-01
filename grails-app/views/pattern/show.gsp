<%@ page import="edu.uoregon.nic.nemo.portal.SearchService" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'datafile.label', default: 'Cross Lab ERP')}"/>
    <title><g:message code="default.show.label" args="[entityName + ': ' + label?.replaceAll('_', ' ')]"/></title>
</head>

<body>

<g:render contextPath="/" template="searchMenu" model="[selected:'pattern']"/>

<div id="instances" class="content scaffold-show" role="main">
    <br/>


    <div class="pattern-header">
        <strong>Viewing Effect</strong>: <g:link action="show" controller="term" id="${id}"><g:renderUrl
            input="${id}"/></g:link>
        <br/>
        <br/>

        <g:form controller="pattern" action="show">
            Select ERP Effects: <g:select class="list" id="otherInstance" optionKey="key" optionValue="value"
                                          from="${availableInstances}" value="${id}" name="instances"
                                          noSelection="['': '- Choose Effect -']"
                                          onchange="window.location.href=document.getElementById('otherInstance').options[document.getElementById('otherInstance').selectedIndex].value">

        </g:select>
        </g:form>
    </div>
    <br/>
    <div class="hideshow-buttons">
        <input id="hideButton" type="button" value="Hide Images" onclick="
            $('.erp-image').hide();
            $('#showButton').show();
            $('#hideButton').hide();
        "/>

        <input id="showButton" type="button" style="display: none;" value="Show Images" onclick="
            $('.erp-image').show();
            $('#showButton').hide();
            $('#hideButton').show();
        "/>
    </div>
    <table>
        <thead>
        <tr>
            <th>
                <div class="erp-header">
                    Pattern Instances
                    %{--<br/>--}%
                    <g:link action="show" controller="term" id="${url}">${label?.replaceAll("_", "&nbsp;")}</g:link>
                </div>
            </th>
            <th class="erp-header">
                Source Dataset
            </th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${instances}" var="instance" status="i">
            <tr>
                <td style="width: 50%;">
                    <g:if test="${instance.key.startsWith('http:')}">
                    <strong class="erp-pattern-title">${instance.key}</strong>
                        %{--<g:link controller="erpAnalysisResult" action="showIndividuals" id="${instance.value.id}"--}%
                                %{--params="[time: time]">--}%
                            %{--${instance.key}--}%
                        %{--</g:link>--}%
                    </g:if>
                    <g:else>
                        <div class="erp-pattern-title">
                            <g:createIndividualLink time="${instance.key}" value="${instance.value}"/>
                            <br/>

                            <div class="images-div" style="display: inline-block;">

                                <g:ifImage key="${instance.key.encodeAsURL()}">
                                    <div class="image1-div" style="display: inline-table; text-align: center;">
                                        <strong>Extracted ERP Pattern</strong>
                                        <br/>
                                        <g:link class="erp-image" controller="patternImage" action="viewImage"
                                                id="${instance.key.encodeAsURL()}">
                                            <img style="max-width: 250px;"
                                                 src="${createLink(controller: 'patternImage', action: 'viewImage', id: instance.key.encodeAsURL())}"
                                                 alt="${instance.key} Image"/>

                                        </g:link>
                                    </div>
                                </g:ifImage>

                                <g:ifRawImage key="${instance.key.encodeAsURL()}">
                                    <div class="erp-image" class="image1-div" style="display: inline-table; text-align: center;">
                                        <strong>Extracted ERP Pattern</strong>
                                        <strong>Reconstructed ERP Data</strong>
                                        <br/>

                                        <g:link controller="patternImage" action="viewRawImage"
                                                id="${instance.key.encodeAsURL()}">
                                            <img style="max-width: 250px;"
                                                 src="${createLink(controller: 'patternImage', action: 'viewRawImage', id: instance.key.encodeAsURL())}"
                                                 alt="${instance.key} Image"/>
                                        </g:link>
                                    </div>
                                </g:ifRawImage>
                            </div>
                        </div>

                    </g:else>
                </td>

                <td>
                    <g:renderLabExperimentForDataFiles input="${instance.value}"/>
                </td>
            </tr>
        %{--<tr>--}%
        %{--<td colspan="2"><hr/></td>--}%
        %{--</tr>--}%
        </g:each>
        </tbody>
    </table>
</div>

</body>
</html>

