<!doctype html>

<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'term.label', default: 'Term')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-dataFile" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                               default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        %{--<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]"/></g:link></li>--}%
        %{--<li><g:link class="create" action="create"><g:message code="default.new.label"--}%
                                                              %{--args="[entityName]"/></g:link></li>--}%
    </ul>
</div>

<div id="show-dataFile" class="content scaffold-show" role="main">
    ${termView.label}
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list dataFile">

    %{--<g:if test="${dataFileInstance?.experiment}">--}%
    %{--<li class="fieldcontain">--}%
    %{--<span id="experiment-label" class="property-label"><g:message code="dataFile.experiment.label" default="Experiment" /></span>--}%
    %{----}%
    %{--<span class="property-value" aria-labelledby="experiment-label"><g:link controller="experiment" action="show" id="${dataFileInstance?.experiment?.id}">${dataFileInstance?.experiment?.encodeAsHTML()}</g:link></span>--}%
    %{----}%
    %{--</li>--}%
    %{--</g:if>--}%

        <g:if test="${dataFileInstance?.artifactContentType}">
            <li class="fieldcontain">
                <span id="artifactContentType-label" class="property-label"><g:message
                        code="dataFile.artifactContentType.label" default="Artifact Content Type"/></span>

                <span class="property-value" aria-labelledby="artifactContentType-label"><g:fieldValue
                        bean="${dataFileInstance}" field="artifactContentType"/></span>

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.artifactFileName}">
            <li class="fieldcontain">
                <span id="artifactFileName-label" class="property-label"><g:message
                        code="dataFile.artifactFileName.label" default="Artifact File Name"/></span>

                <span class="property-value" aria-labelledby="artifactFileName-label"><g:fieldValue
                        bean="${dataFileInstance}" field="artifactFileName"/></span>

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.artifactFileSize}">
            <li class="fieldcontain">
                <span id="artifactFileSize-label" class="property-label"><g:message
                        code="dataFile.artifactFileSize.label" default="Artifact File Size"/></span>

                <span class="property-value" aria-labelledby="artifactFileSize-label"><g:fieldValue
                        bean="${dataFileInstance}" field="artifactFileSize"/></span>

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.artifactUpdateAt}">
            <li class="fieldcontain">
                <span id="artifactUpdateAt-label" class="property-label"><g:message
                        code="dataFile.artifactUpdateAt.label" default="Artifact Update At"/></span>

                <span class="property-value" aria-labelledby="artifactUpdateAt-label"><g:formatDate
                        date="${dataFileInstance?.artifactUpdateAt}"/></span>

            </li>
        </g:if>


        <g:if test="${dataFileInstance?.currentAt}">
            <li class="fieldcontain">
                <span id="currentAt-label" class="property-label"><g:message code="dataFile.currentAt.label"
                                                                             default="Current At"/></span>

                <span class="property-value" aria-labelledby="currentAt-label"><g:formatDate
                        date="${dataFileInstance?.currentAt}"/></span>

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.format}">
            <li class="fieldcontain">
                <span id="format-label" class="property-label"><g:message code="dataFile.format.label"
                                                                              default="Data Format"/></span>
                <span class="property-value" aria-labelledby="cachedSlug-label">
                    %{--<g:renderUrl input="${dataFileInstance.dataFormat}"/>--}%
                    <g:renderOntological input="${dataFileInstance.format}"/>
                    %{--<g:fieldValue bean="${dataFileInstance}" field="dataFormat"/>--}%
                </span>

                %{--<span class="property-value" aria-labelledby="dataFormat-label"><g:formatDate date="${dataFileInstance?.dataFormat}" /></span>--}%

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.dataSet}">
            <li class="fieldcontain">
                <span id="dataSet-label" class="property-label"><g:message code="dataFile.dataSet.label"
                                                                           default="Data Set"/></span>

                <span class="property-value" aria-labelledby="dataSet-label">
                    <g:renderUrl input="${dataFileInstance.dataSet}"/>
                    %{--<g:fieldValue bean="${dataFileInstance}" field="dataSet"/>--}%
                </span>

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.erpDataPreprocessing}">
            <li class="fieldcontain">
                <span id="erpDataPreprocessing-label" class="property-label"><g:message
                        code="dataFile.erpDataPreprocessing.label" default="Erp Data Preprocessing"/></span>

                <span class="property-value" aria-labelledby="erpDataPreprocessing-label"><g:link
                        controller="erpDataPreprocessing" action="show"
                        id="${dataFileInstance?.erpDataPreprocessing?.id}">${dataFileInstance?.erpDataPreprocessing?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.identifier}">
            <li class="fieldcontain">
                <span id="identifier-label" class="property-label"><g:message code="dataFile.identifier.label"
                                                                              default="Identifier"/></span>

                <span class="property-value" aria-labelledby="identifier-label"><g:fieldValue bean="${dataFileInstance}"
                                                                                              field="identifier"/></span>

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.state}">
            <li class="fieldcontain">
                <span id="state-label" class="property-label"><g:message code="dataFile.state.label"
                                                                         default="State"/></span>

                <span class="property-value" aria-labelledby="state-label"><g:fieldValue bean="${dataFileInstance}"
                                                                                         field="state"/></span>

            </li>
        </g:if>

        <g:if test="${dataFileInstance?.updateAt}">
            <li class="fieldcontain">
                <span id="updateAt-label" class="property-label"><g:message code="dataFile.updateAt.label"
                                                                            default="Update At"/></span>

                %{--<span class="property-value" aria-labelledby="updateAt-label"><g:formatDate date="${dataFileInstance?.updateAt}" /></span>--}%

            </li>
        </g:if>


    </ol>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${dataFileInstance?.id}"/>
            <g:link class="edit" action="edit" id="${dataFileInstance?.id}"><g:message code="default.button.edit.label"
                                                                                       default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
