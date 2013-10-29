<%@ page import="edu.uoregon.nic.nemo.portal.Experiment" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'experiment.label', default: 'Experiment')}"/>
    <title>
        Experiment Summary
    </title>
    <r:require module="jquery"/>

    <r:script>
        function hidediv(id) {
            var element = document.getElementById('classificationBox' + id);
            element.parentNode.removeChild(element);

//        element.style.visibility = 'hidden';
            var element = document.getElementById('hidetext' + id);
//            element.style.visibility = 'hidden';
            element.parentNode.removeChild(element);
        }

        function toggleClassification(id) {
            var element = document.getElementById("showLink" + id);
            alert('index: ' + element.innerHTML.indexOf("Show"))
            if (element.innerHTML.indexOf("Show") >= 0) {
                element.innerHTML = "Hide";
            }
            else {
                element.innerHTML = "Show";
                var classificationBox = document.getElementById("classificationBox" + id);
                element.style.display = 'none';
//                classificationBox.parentNode.removeChild(classificationBox);
            }
        }
    </r:script>
</head>

<body>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                              args="[entityName]"/></g:link></li>
            <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                  args="[entityName]"/></g:link></li>

            <g:if test="${experimentInstance.headItLink}">
            %{--<div class="headit-box">--}%
            %{--<div class="headit-button">--}%
                <a class="external headit-button" href="${experimentInstance.createHeadItLink()}"
                   target="_blank">HeadIT Data&nbsp;</a>
            %{--</div>--}%
            %{--<div id="headItData"></div>--}%
            %{--<script>--}%
            %{--jQuery('#headItData').load("${subjectGroupInstance.createHeadItLink()}");--}%
            %{--</script>--}%

            %{--</div>--}%
            </g:if>

        %{--<g:if test="${subjectGroupInstance.headItLink}">--}%
        %{--<div class="headit-box">--}%
        %{--<a class="external" href="${subjectGroupInstance.createHeadItLink()}"--}%
        %{--target="_blank">Individual Subject Data (HeadIT)</a>--}%

        %{--<div id="headItData"></div>--}%
        %{--<script>--}%
        %{--jQuery('#headItData').load("${subjectGroupInstance.createHeadItLink()}");--}%
        %{--</script>--}%

        %{--</div>--}%
        %{--</g:if>--}%
        </ul>
    </div>
</sec:ifAllGranted>

<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>

<div id="show-experiment" class="content scaffold-show main-page" role="main">
    <br/>
    <g:include view="laboratory/section.gsp"/>

    <fieldset>

        <legend>2. Experiment ${experimentInstance?.identifier} Summary</legend>

        <g:include view="experiment/sectionInclude.gsp"/>

        <g:editable users="${experimentInstance?.laboratory?.users}">
            <g:form>
                <fieldset class="buttons">
                    <g:hiddenField name="id" value="${experimentInstance?.id}"/>
                    <g:link class="edit" action="edit" id="${experimentInstance?.id}"><g:message
                            code="default.button.edit.label" default="Edit"/></g:link>
                    <sec:ifAllGranted roles="ROLE_ADMIN">
                        <g:actionSubmit class="delete" action="delete"
                                        value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                    </sec:ifAllGranted>
                </fieldset>
            </g:form>
            <br/>
            <br/>
        </g:editable>
    </fieldset>

    <g:include view="publication/section.gsp"/>
    <g:include view="subjectGroup/section.gsp"/>
    <g:include view="condition/section.gsp"/>

    <div class="indent-dependent">
        <g:include view="stimulus/section.gsp"/>
        <g:include view="response/section.gsp"/>
    </div>
    <g:include view="eegDataCollection/section.gsp"/>
    <g:include view="erpDataPreprocessing/section.gsp"/>


    %{--<g:include view="dataFile/section.gsp"/>--}%
    <g:include view="erpPatternExtraction/section.gsp"
               model="[experimentInstance: experimentInstance
                       , erpPatternExtractions: experimentInstance?.erpPatternExtractions]"/>
    %{--<g:include view="erpAnalysisResult/section.gsp"/>--}%
    <g:include view="erpAnalysisResult/section.gsp"
               model="[experimentInstance: experimentInstance
                       , erpAnalysisResults: experimentInstance.erpAnalysisResults]"/>

</div>
</body>
</html>


