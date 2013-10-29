<%@ page import="edu.uoregon.nic.nemo.portal.OntologyService;" %>
<!doctype html>

<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'term.label', default: 'Term')}"/>
    <title>${termView.label} ${termView.nemoId}</title>
    %{--<r:require module="jquery"/>--}%
    <r:require modules="jquery,jquery-ui"/>
    %{--<jqui:resources theme="ui-lightness"/>--}%


    %{--<script type="text/javascript">--}%
    <r:script type="text/javascript">
        $(document).ready(function () {
//            $("#datepicker").datepicker({dateFormat: 'yy/mm/dd'});
            $("#tabs").tabs();
        });
    </r:script>

    <meta name="keywords" content="NEMO, Ontology, Term, ${termView.label}, ${termView.nemoId}">

    %{--<r:layoutResources/>--}%
</head>

<body>

<div class="nav" role="navigation">
    <ul>
        <li><g:link class="list" action="list">All ${entityName}s</g:link></li>
    </ul>
</div>

<div class="page-heading">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:each in="${related}" var="relatedItem">
        <g:relatedLink ontological="${relatedItem}"/>
    </g:each>

    <g:if test="${instances}">
        <div id="related-info">
            <g:link action="show" controller="pattern"
                    id="${termView.uri}">${instances.size()} Cross Lab Patterns</g:link>
        </div>
    </g:if>
</div>

<div id="tabs">
    <ul>
        <li><a href="#basic">Basic Info</a></li>
        <li><a href="#detailed">Detailed Info</a></li>
        <li><a href="#curation">Curation Status</a></li>
    </ul>

    <div id="basic">
        <table>
            <tr>
                <td>
                    Label
                </td>
                <td>
                    ${termView.label}
                </td>
            </tr>
            <tr>
                <td>
                    Definition
                </td>
                <td>
                    ${termView.definition}
                </td>
            </tr>
            <tr>
                <td>
                    Synonyms
                </td>
                <td>
                    <g:each in="${termView.synonyms}" var="syn" status="index">
                        ${syn}
                        ${index.intValue() < termView.synonyms.size() - 1 ? "&bull;" : ""}
                    </g:each>
                </td>
            </tr>
            <tr>
                <td>
                    URI
                </td>
                <td>
                    ${termView.uri}
                    <g:link url="${OntologyService.NS + "#" + termView.uri}">Source</g:link>
                    <g:bioPortalLink uri="${termView.uri}"/>
                </td>
            </tr>
        </table>

    </div>

    <div id="detailed">

        <h4>Detailed Info</h4>
        <pre>
            ${termView.detailedInfoDump}
        </pre>
    </div>

    <div id="curation">
        <h4>Curation Status</h4>
        <table>
            <tr>
                <td nowrap>Preferred Label</td>
                <td>
                    ${termView.preferredLabel}
                </td>
            </tr>
            <tr>
                <td>Creation Date</td>
                <td>
                    ${termView.creationDate}
                </td>
            </tr>
            <tr>
                <td>Modified Date</td>
                <td>
                    ${termView.modifiedDate}
                </td>
            </tr>
            <tr>
                <td>Curation Status</td>
                <td>
                    ${termView.curationStatus}
                </td>
            </tr>
            <tr>
                <td>Definition Source</td>
                <td>
                    <g:each in="${termView.definitionSources}" var="source" status="index">
                        ${source}
                        ${index.intValue() < termView.definitionSources.size() - 1 ? "&bull;" : ""}
                    </g:each>
                </td>
            </tr>
            <tr>
                <td>
                    Evidence Code
                </td>
                <td>
                    ${termView.evidenceCode}
                </td>
            </tr>
            <tr>
                <td>
                    Contributors
                </td>
                <td>
                    <g:each in="${termView.contributors}" var="contributor" status="index">
                        ${contributor}
                        ${index.intValue() < termView.contributors.size() - 1 ? "&bull;" : ""}
                    </g:each>
                </td>
            </tr>
        </table>
    </div>
</div>

</body>

</html>
