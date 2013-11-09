<%@ page import="edu.uoregon.nic.nemo.portal.Response" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'response.label', default: 'Response')}"/>
    <title>
        Admin Screen
    </title>
</head>

<body>

<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>

<div class="admin">
    <li>
        <g:link controller="secUser" action="list">Users</g:link>
    </li>
    <li>
        <g:link controller="erpAnalysisResult" action="reinferAll">Re-infer all RDF files</g:link>
    </li>
    <li>
        <g:link controller="erpAnalysisResult" action="recacheSearch">Recache Erp's</g:link>
    </li>
    <li>
        <g:link controller="erpAnalysisResult" action="cacheSearch">Cache Erp's</g:link>
    </li>
    <li>
        <g:link controller="admin" action="updateOntologies">Update Ontologies</g:link>
    </li>

</div>

</body>
</html>