<html>

<head>
    %{--<meta name='layout' content='register'/>--}%
    <meta name='layout' content='main'/>
    <title><g:message code='spring.security.ui.register.title'/></title>
    %{--<r:require modules="jquery,jquery-ui"/>--}%
    <r:require modules="register"/>
</head>

<body>

<p/>

<s2ui:form width='650' height='400' elementId='loginFormContainer'
           titleCode='spring.security.ui.register.description' center='true'>

    <g:form action='register' name='registerForm'>

        <g:if test='${emailSent}'>
            <br/>
            <g:message code='spring.security.ui.register.sent'/>
        </g:if>
        <g:else>

            <br/>

            <table>
                <tbody>

                <s2ui:textFieldRow name='username' labelCode='user.username.label' bean="${command}"
                                   size='40' labelCodeDefault='Username / Email ' value="${command.username}"/>

                <s2ui:passwordFieldRow name='password' labelCode='user.password.label' bean="${command}"
                                       size='40' labelCodeDefault='Password' value="${command.password}"/>

                <s2ui:passwordFieldRow name='password2' labelCode='user.password2.label' bean="${command}"
                                       size='40' labelCodeDefault='Password (again)' value="${command.password2}"/>

                <s2ui:textFieldRow name='fullName' bean="${command}" value="${command.fullName}"
                                   size='40' labelCode='user.fullName.label' labelCodeDefault='Full Name'/>

                <tr class="prop">

                    %{--<div class="control-group">--}%
                    <td valign="top" class="name">
                        Laboratory
                    </td>
                    <td valign="top" class="value">
                        <g:select name="laboratoryId" from="${laboratories}" id="laboratory" optionKey="id"
                                  optionValue="identifier"
                                  noSelection="['null': '- None -']"/>
                    </td>

                </tr>

                </tbody>
            </table>

            %{--<s2ui:submitButton elementId='create' form='registerForm' messageCode='spring.security.ui.register.submit' class="btn btn-primary offset1"/>--}%
            %{--<a id="create"  class="btn btn-primary offset1"  >Create your account</a>--}%
            <a id="create"  >Create Account</a>
            <input type='submit' value=' ' id='create_submit' class='s2ui_hidden_button' style="display: none;"/>

            <r:script>
                $(document).ready(function() {

                    $("#create").button();
                    $('#create').bind('click', function() {
                        document.forms.registerForm.submit();
                    });

                });
            </r:script>

            <g:link controller="experiment" action="list" class="btn">Cancel</g:link>

        </g:else>

    </g:form>

</s2ui:form>

<r:script>
    $(document).ready(function () {
        $('#username').focus();
    });
</r:script>

</body>
</html>
