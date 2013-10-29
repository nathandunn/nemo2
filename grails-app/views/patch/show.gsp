
<%@ page import="edu.uoregon.nic.nemo.portal.Patch" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'patch.label', default: 'Patch')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-patch" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-patch" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list patch">
			
				<g:if test="${patchInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="patch.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${patchInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${patchInstance?.dateApplied}">
				<li class="fieldcontain">
					<span id="dateApplied-label" class="property-label"><g:message code="patch.dateApplied.label" default="Date Applied" /></span>
					
						<span class="property-value" aria-labelledby="dateApplied-label"><g:formatDate date="${patchInstance?.dateApplied}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${patchInstance?.note}">
				<li class="fieldcontain">
					<span id="note-label" class="property-label"><g:message code="patch.note.label" default="Note" /></span>
					
						<span class="property-value" aria-labelledby="note-label"><g:fieldValue bean="${patchInstance}" field="note"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${patchInstance?.id}" />
					<g:link class="edit" action="edit" id="${patchInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
