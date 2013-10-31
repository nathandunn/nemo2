<%@ page import="edu.uoregon.nic.nemo.portal.Publication; edu.uoregon.nic.nemo.portal.ErpPatternExtraction; edu.uoregon.nic.nemo.portal.ErpAnalysisResult; edu.uoregon.nic.nemo.portal.ErpDataPreprocessing; edu.uoregon.nic.nemo.portal.EegDataCollection; edu.uoregon.nic.nemo.portal.Condition; edu.uoregon.nic.nemo.portal.SubjectGroup" %>
<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>NEMO - <g:layoutTitle default="Neural ElectroMagnetic Ontologies"/> ${experimentHeader?.identifier}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="google-site-verification" content="ViPOUvyGsi9k6rX_OVpYqXF3ov5dQOO_uavnkJixieE"/>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
    <r:require module="jquery"/>

    <g:layoutHead/>

    <r:script>
        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-37938514-1']);
        _gaq.push(['_trackPageview']);

        (function () {
            var ga = document.createElement('script');
            ga.type = 'text/javascript';
            ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(ga, s);
        })();

        $(document).ready(function () {
            $("#spinner").bind("ajaxSend",function () {
                $(this).show();
            }).bind("ajaxStop",function () {
                        $(this).hide();
                    }).bind("ajaxError", function () {
                        $(this).hide();
                    });

        });

    </r:script>


    <r:layoutResources/>
</head>

<body>
<div class="container">
    <div id='logo'>
        <g:link absolute="/">
            <img src="${resource(dir: 'images', file: 'NEMO_Portal_small.png')}" alt="NEMO"/>
        </g:link>
    </div>

    <ul id='nav_bar'>
        <li><g:link controller="erpAnalysisResult" action="search">Cross-Lab Search</g:link></li>
        <li>
            <g:link controller="experiment" action="list">Experiments</g:link>
        </li>
        <li><g:link controller="laboratory" action="list">Labs</g:link></li>
        %{--<li><g:link controller="erpAnalysisResult" action="search">Search</g:link></li>--}%
        %{--<li><g:link controller="pattern" action="show">Cross Lab ERP</g:link></li>--}%
        %{--<li><g:link controller="erpAnalysisResult" action="reference">Spatial ROI</g:link></li>--}%
        <li><a href="http://nemo.nic.uoregon.edu/wiki/NEMO" target="_blank">Wiki</a></li>
        <li><a href="mailto:nemo-help@nic.uoregon.edu">Help</a></li>

        <sec:ifAllGranted roles="ROLE_ADMIN">
            <li>
                <g:link controller="admin" action="index">Admin</g:link>
            </li>
        </sec:ifAllGranted>
    </ul>


    <div id='user_nav'>
        <sec:ifNotLoggedIn>
            <g:link action="auth" controller="login">Login</g:link>
            &nbsp;
            <g:link action="index" controller="register">Register</g:link>
        </sec:ifNotLoggedIn>


        <sec:ifLoggedIn>
            <g:link action="index" controller="logout">Logout</g:link>
            &nbsp;
            <g:link action="show" controller="secUser">
                <sec:loggedInUserInfo field="username"/>
            </g:link>
        </sec:ifLoggedIn>
    </div>
</div>

<!--Environment: '${grails.util.Environment.current.name}'-->

<g:if env="production">
%{--this is a hack instead adding a new libarry--}%
%{--<div id="demo-site-warning">--}%
%{--Production--}%
%{--</div>--}%
</g:if>
<g:else>
    <div id="demo-site-warning">

        This is a demo site used for testing only!   The data on this site will be periodically be erased.
        Permanent community data is maintained on the production web-site:
        <a href="https://portal.nemo.nic.uoregon.edu">https://portal.nemo.nic.uoregon.edu</a>
    </div>
</g:else>

<g:if test="${experimentHeader?.id}">
    <div id="header_menu">
        <h2>
            Experiment
            <g:link action="show" controller="experiment" id="${experimentHeader.id}"
                    class="${params.controller == "experiment" ? "selected" : ""}">
                <b>${experimentHeader.identifier}</b>
            </g:link>
            &nbsp;
            <g:link action="list">
                X
            </g:link>
        </h2>
    </div>

    <div id="second_menu">
        <g:link action="list" controller="subjectGroup" id="${experimentHeader.id}"
                class="${params.controller == "subjectGroup" ? "selected" : ""}">
            Subject Groups (${experimentHeader?.subjectGroups.size()})
        </g:link>
        <g:link action="list" controller="condition" id="${experimentHeader.id}"
                class="${params.controller == "condition" || params.controller == "stimulus" || params.controller == "response" ? "selected" : ""}">
            Conditions (${experimentHeader?.conditions.size()})

        </g:link>
        <g:link action="list" controller="eegDataCollection" id="${experimentHeader.id}"
                class="${params.controller == "eegDataCollection" ? "selected" : ""}">
            EEG Data Acquisition (${experimentHeader?.eegDataCollections.size()})
        </g:link>
        <g:link action="list" controller="erpDataPreprocessing" id="${experimentHeader.id}"
                class="${params.controller == "erpDataPreprocessing" ? "selected" : ""}">
            ERP Data Preprocessing (${experimentHeader?.erpDataPreprocessings.size()})
        </g:link>
        <g:link action="list" controller="erpPatternExtraction" id="${experimentHeader.id}"
                class="${params.action == "erpPatterns" ? "selected" : ""}">
            ERP Pattern Extraction (${experimentHeader?.erpPatternExtractions?.size()})
        </g:link>
        <g:link action="list" controller="erpAnalysisResult" id="${experimentHeader.id}"
                class="${params.action == "erpPatterns" ? "selected" : ""}">
            ERP Results (${experimentHeader?.erpAnalysisResultsList?.size()})
        </g:link>
    %{--<g:link action="list" controller="dataFile" id="${experimentHeader.id}"--}%
    %{--class="${params.controller == "dataFile" && params.action != "erpPatterns" ? "selected" : ""}">--}%
    %{--Files (${experimentHeader?.dataFiles.size()})--}%
    %{--</g:link>--}%
        <g:link action="list" controller="publication" id="${experimentHeader.id}"
                class="${params.controller == "publication" ? "selected" : ""}">
            Publications (${experimentHeader?.publications.size()})
        </g:link>


        <g:if test="${experimentHeader.headItLink}">
            <r:script>
                <g:remoteFunction action="generateHeadItLink" update="headItLink"
                                  asynchronous="true"
                                  id="${experimentHeader?.id}"
                                  controller="experiment"/>
            </r:script>
            <b>
            <div id="headItLink" style="display: inline-block;"></div>
            </b>
        </g:if>
        %{--<g:else>--}%
            %{--<a class="external headit" href="${experimentHeader.createHeadItLink()}"--}%
               %{--target="_blank">DIGGITY</a>--}%
        %{--</g:else>--}%
        %{--<g:if test="${experimentHeader.headItLink}">--}%
        %{--<div class="headit-box">--}%
            %{--<b>--}%
                %{--<a class="external headit" href="${experimentHeader.createHeadItLink()}"--}%
                   %{--target="_blank">HeadIT Data&nbsp;&nbsp;&nbsp;</a>--}%
            %{--</b>--}%

        %{--<div id="headItData"></div>--}%
        %{--<script>--}%
        %{--jQuery('#headItData').load("${subjectGroupInstance.createHeadItLink()}");--}%
        %{--</script>--}%

        %{--</div>--}%
        %{--</g:if>--}%
    </div>
</g:if>
<g:else>
%{--Not defined--}%
    <div id="header_menu">
        <h2>
            <g:link action="list" controller="experiment">
                All Experiments
            </g:link>
        </h2>
    </div>


        %{--<g:if test="${experimentHeader.headItLink}">--}%
        %{--<div class="headit-box">--}%
        %{--<b>--}%
        %{--<a class="external headit" href="${experimentHeader.createHeadItLink()}"--}%
        %{--target="_blank">HeadIT Data&nbsp;&nbsp;&nbsp;</a>--}%
        %{--</b>--}%

        %{--<div id="headItData"></div>--}%
        %{--<script>--}%
        %{--jQuery('#headItData').load("${subjectGroupInstance.createHeadItLink()}");--}%
        %{--</script>--}%

        %{--</div>--}%
        %{--</g:if>--}%
    </div>
</g:else>
%{--<nav:render group="experiment"/>--}%

<div class="clear"></div>
<g:layoutBody/>
<div class="footer" role="contentinfo"></div>


<div id="spinner">

    <div id="spinner-content">
        Loading . . . <br/>
        <g:img dir="images" file="ajax-loader.gif"/>
    </div>



    <r:layoutResources/>
</body>
</html>