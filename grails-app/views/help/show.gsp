
<%@ page import="edu.uoregon.nic.nemo.portal.Help" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'help.label', default: 'Help')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-help" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-help" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list help">
			
				<g:if test="${helpInstance?.emailFrom}">
				<li class="fieldcontain">
					<span id="emailFrom-label" class="property-label"><g:message code="help.emailFrom.label" default="Email From" /></span>
					
						<span class="property-value" aria-labelledby="emailFrom-label"><g:fieldValue bean="${helpInstance}" field="emailFrom"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${helpInstance?.subject}">
				<li class="fieldcontain">
					<span id="subject-label" class="property-label"><g:message code="help.subject.label" default="Subject" /></span>
					
						<span class="property-value" aria-labelledby="subject-label"><g:fieldValue bean="${helpInstance}" field="subject"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${helpInstance?.message}">
				<li class="fieldcontain">
					<span id="message-label" class="property-label"><g:message code="help.message.label" default="Message" /></span>
					
						<span class="property-value" aria-labelledby="message-label"><g:fieldValue bean="${helpInstance}" field="message"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${helpInstance?.id}" />
					<g:link class="edit" action="edit" id="${helpInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
