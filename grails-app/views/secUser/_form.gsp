<%@ page import="edu.uoregon.nic.nemo.portal.SecUser" %>



<div class="fieldcontain ${hasErrors(bean: secUserInstance, field: 'username', 'error')} ">
    <label for="username">
        <g:message code="secUser.username.label" default="Username"/>

    </label>
    %{--<g:textField name="username" value="${secUserInstance?.username}"  size="40"/>--}%
    <g:field type="email" name="username" value="${secUserInstance?.username}"  size="40" placeholder="user@institution.edu"/>
</div>

<div class="fieldcontain ${hasErrors(bean: secUserInstance, field: 'password', 'error')} ">
    <label for="password">
        <g:message code="secUser.password.label" default="Password"/>

    </label>
    %{--<g:textField name="password" value="${secUserInstance?.password}"/>--}%
    <g:passwordField name="password" value="" size="40"/>
</div>

<div class="fieldcontain ${hasErrors(bean: secUserInstance, field: 'password2', 'error')} ">
    <label for="password">
        <g:message code="secUser.password2.label" default="Repeat Password"/>

    </label>
    %{--<g:textField name="password" value="${secUserInstance?.password}"/>--}%
    <g:passwordField name="password2" value="" size="40"/>
</div>

<div class="fieldcontain ${hasErrors(bean: secUserInstance, field: 'laboratories', 'error')} ">
    <label for="laboratory">
        <g:message code="secUser.laboratory.label" default="Laboratories"/>

    </label>
    <g:select id="laboratories" name="laboratories.id" multiple="true"
              from="${edu.uoregon.nic.nemo.portal.Laboratory.listOrderByIdentifier()}" optionKey="id"
              value="${secUserInstance?.laboratories?.id}" optionValue="identifier" class="many-to-one"
              noSelection="['null': '- None - ']"/>
</div>

<sec:ifAllGranted roles="ROLE_ADMIN">

    <div class="fieldcontain ${hasErrors(bean: secUserInstance, field: 'accountExpired', 'error')} ">
        <label for="accountExpired">
            <g:message code="secUser.accountExpired.label" default="Account Expired"/>

        </label>
        <g:checkBox name="accountExpired" value="${secUserInstance?.accountExpired}"/>
    </div>

    <div class="fieldcontain ${hasErrors(bean: secUserInstance, field: 'accountLocked', 'error')} ">
        <label for="accountLocked">
            <g:message code="secUser.accountLocked.label" default="Account Locked"/>

        </label>
        <g:checkBox name="accountLocked" value="${secUserInstance?.accountLocked}"/>
    </div>

    <div class="fieldcontain ${hasErrors(bean: secUserInstance, field: 'enabled', 'error')} ">
        <label for="enabled">
            <g:message code="secUser.enabled.label" default="Enabled"/>

        </label>
        <g:checkBox name="enabled" value="${secUserInstance?.enabled}"/>
    </div>

    <div class="fieldcontain ${hasErrors(bean: secUserInstance, field: 'passwordExpired', 'error')} ">
        <label for="passwordExpired">
            <g:message code="secUser.passwordExpired.label" default="Password Expired"/>

        </label>
        <g:checkBox name="passwordExpired" value="${secUserInstance?.passwordExpired}"/>
    </div>

</sec:ifAllGranted>

