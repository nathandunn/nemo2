<%@ page import="edu.uoregon.nic.nemo.portal.SecUser" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'secUser.label', default: 'User')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div id="show-secUser" class="content scaffold-show" role="main">
<h1><g:message code="default.show.label" args="[entityName]"/></h1>
<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>
<ol class="property-list secUser">

    <g:if test="${secUserInstance?.username}">
        <li class="fieldcontain">
            <span id="username-label" class="property-label"><g:message code="secUser.username.label"
                                                                        default="Username"/></span>

            <span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${secUserInstance}"
                                                                                        field="username"/></span>

        </li>
    </g:if>

%{--<g:if test="${secUserInstance?.laboratories}">--}%
    <li class="fieldcontain">
        <span id="laboratory-label" class="property-label"><g:message code="secUser.laboratory.label"
                                                                      default="Laboratories"/></span>
        <span class="property-value" aria-labelledby="laboratory-label">
            <g:each in="${secUserInstance.laboratories}" var="laboratory">
                <g:link controller="laboratory" action="show"
                        id="${laboratory?.id}">${laboratory?.identifier}</g:link>
            </g:each>
        </span>

    </li>
%{--</g:if>--}%

    <g:if test="${secUserInstance?.fullName}">
        <li class="fieldcontain">
            <span id="laboratory-label" class="property-label"><g:message code="secUser.fullName"
                                                                          default="Full Name"/></span>
            <span class="property-value" aria-labelledby="laboratory-label">${secUserInstance?.fullName}</span>

        </li>
    </g:if>

    <sec:ifAllGranted roles="ROLE_ADMIN">

        <li class="fieldcontain">
            <span id="accountExpired-label" class="property-label"><g:message code="secUser.accountExpired.label"
                                                                              default="Account Expired"/></span>

            <span class="property-value" aria-labelledby="accountExpired-label"><g:formatBoolean
                    boolean="${secUserInstance?.accountExpired}"/></span>

        </li>


        <li class="fieldcontain">
            <span id="accountLocked-label" class="property-label"><g:message code="secUser.accountLocked.label"
                                                                             default="Account Locked"/></span>

            <span class="property-value" aria-labelledby="accountLocked-label"><g:formatBoolean
                    boolean="${secUserInstance?.accountLocked}"/></span>

        </li>
        <li class="fieldcontain">
            <span id="enabled-label" class="property-label"><g:message code="secUser.enabled.label"
                                                                       default="Enabled"/></span>

            <span class="property-value" aria-labelledby="enabled-label"><g:formatBoolean
                    boolean="${secUserInstance?.enabled}"/></span>

        </li>
        <li class="fieldcontain">
            <span id="passwordExpired-label" class="property-label"><g:message code="secUser.passwordExpired.label"
                                                                               default="Password Expired"/></span>

            <span class="property-value" aria-labelledby="passwordExpired-label"><g:formatBoolean
                    boolean="${secUserInstance?.passwordExpired}"/></span>

        </li>

    </sec:ifAllGranted>

</ol>
<g:editable users="${secUserInstance}">
    <br/>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${secUserInstance?.id}"/>
            <g:link class="edit" action="edit" id="${secUserInstance?.id}"><g:message code="default.button.edit.label"
                                                                                      default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
    </div>
</g:editable>

<br/>

<table>
    <thead>
    <tr>

        <th>Identifier</th>
        <th>
            Publication Type
        </th>
        <th>Publication Year</th>

        <th>
            Digital Object Identifier
        </th>

    </tr>

    </thead>
    <tbody>
    <g:each var="publicationInstance" in="${secUserInstance.publications}">
        <tr>
            <td><g:link action="show" controller="publication"
                        id="${publicationInstance.id}">${fieldValue(bean: publicationInstance, field: "identifier")}</g:link></td>

            <td>
                <a href="${publicationInstance.type.url}">
                    ${publicationInstance.type.nameOnly}
                </a>
            </td>

            <td>
                <g:formatNumber number="${publicationInstance.publicationDate}"/>
            </td>

            <td>
                %{--${fieldValue(bean: publicationInstance, field: "digitalObjectIdentifier")}--}%

                <g:if test="${publicationInstance.digitalObjectIdentifier?.startsWith("PMID")}">
                    <g:link target="_blank" class="external-link"
                            url="http://www.ncbi.nlm.nih.gov/pubmed/${publicationInstance.digitalObjectIdentifier.substring(6)}">${publicationInstance.digitalObjectIdentifier}</g:link>

                </g:if>
                <g:elseif test="${publicationInstance.digitalObjectIdentifier}">
                    <g:link target="_blank" class="external-link"
                            url="http://dx.doi.org/${publicationInstance.digitalObjectIdentifier}">${publicationInstance.digitalObjectIdentifier}</g:link>
                </g:elseif>
            </td>
        </tr>
    </g:each>
    </tbody>
</table>

</body>
</html>
