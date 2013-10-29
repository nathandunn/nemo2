<!doctype html>
<html>

<head>
    <title>Internal Error</title>
    <meta name="layout" content="main">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css">
</head>

<body>

<div class="main-page">
    <br/>

    <div class="error error-with">
        There was an internal error.  An administrator has been notified.
        <br/>
        <br/>
        Please send any details of what your were doing to <a
            href="mailto:ndunn@cas.uoregon.edu?subject=NEMO Error&body=Error Details">the site administrator</a> for a speedier resolution.
    </div>
    <br/>

    <hr/>

    <g:if test="${exception}">
        <br/>
        Additional Details: <br/>
        <g:renderException exception="${exception}"/>
    </g:if>
</div>

</body>

</html>