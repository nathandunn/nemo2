<html>

<head>
    <title><g:message code='spring.security.ui.login.title'/></title>
    <meta name='layout' content='main'/>
    %{--<link rel="stylesheet" href="${resource(dir: 'css', file: 'auth.css')}" type="text/css">--}%
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'auth.css')}" type="text/css">
    <r:require modules="register"/>
</head>

<body>

<br/>

<div class="login s2ui_center ui-corner-all" style='text-align:center;'>
    <div class="login-inner">
        <form action='${postUrl}' method='POST' id="loginForm" name="loginForm" autocomplete='off'>
            <div class="sign-in">

                <g:if test="${flash.message}">
                    <div class="errors" role="alert">${flash.message}</div>
                </g:if>

                <h1><g:message code='spring.security.ui.login.signin'/></h1>

                <table>
                    <tr>
                        <td><label for="username"><g:message code='spring.security.ui.login.username'/></label></td>
                        <td><input name="j_username" id="username" size="40" value="${j_username}"/></td>
                    </tr>
                    <tr>
                        <td><label for="password"><g:message code='spring.security.ui.login.password'/></label></td>
                        <td><input type="password" name="j_password" id="password" size="40"/></td>
                    </tr>
                    <tr>
                        <td colspan='2'>
                            <input type="checkbox" class="checkbox" name="${rememberMeParameter}" id="remember_me"
                                   checked="checked"/>
                            <label for='remember_me'><g:message code='spring.security.ui.login.rememberme'/></label> |
                            <span class="forgot-link">
                                <g:link controller='register' action='forgotPassword'><g:message
                                        code='spring.security.ui.login.forgotPassword'/></g:link>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan='2'>
                            <input type="submit" value="Login" id="loginButton_submit" >
                            <br/>
                            <br/>
                            <hr/>
                            <br/>
                            <s2ui:linkButton elementId='register' controller='register'
                                             messageCode='spring.security.ui.login.register'>
                            </s2ui:linkButton>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#username').focus();
    });

    <s2ui:initCheckboxes/>

</script>

</body>
</html>

