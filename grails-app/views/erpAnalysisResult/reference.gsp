<head>
    <!-- Integrate with Sitemesh layouts           -->
    <meta name="layout" content="main"/>

    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>ROI-Spatial Reference</title>

    <!--                                           -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                -->
    <!--                                           -->
    %{--<script>--}%
    %{--var locations = {--}%
    %{--colorBrainLink: '<g:createLinkTo dir="images" file="colored-brain-clip.png"/>'--}%
    %{--, grayBrainLink : '<g:createLinkTo dir="images" file="detailed-brain-clip.png"/>'--}%
    %{--, baseTermUrl : '<g:createLink action="show" controller="term"/>'--}%
    %{--} ;--}%
    %{--</script>--}%

    %{--<script type="text/javascript" src="${resource(dir: 'gwt/edu.uoregon.nic.nemo.portal.Search', file: 'edu.uoregon.nic.nemo.portal.Search.nocache.js')}"></script>--}%
</head>

<!--                                           -->
<!-- The body can have arbitrary html, or      -->
<!-- you can leave the body empty if you want  -->
<!-- to create a completely dynamic ui         -->
<!--                                           -->
<body>

<g:render contextPath="/" template="searchMenu" model="[selected: 'reference']"/>

%{--<g:img dir="images" file="colored-brain.png" width="400"/>--}%
%{--<g:img dir="images" file="brain-details.png" width="400"/>--}%

%{--<g:createLinkTo dir="images" file="colored-brain.png"/>--}%
%{--<div id="brainPanel"></div>--}%

<div>
    <g:img dir="images" file="NEMO_ROI_TenTwentyOpaque_438x419.png"/>
    <g:img dir="images" file="NEMO_ROI_TenTwentyTransparent438x419.png"/>
    <br/>
    <g:img dir="images" file="NEMO_ROI_TenTwentyTable_755x325.png"/>

</div>
%{--<g:img dir="images" file="Spatial-ROI-reference-both.png"/>--}%
%{--<g:img dir="images" file="Spatial-ROI-Reference-table.png"/>--}%


%{--<div id="searchPanel"></div>--}%
%{--ON the proper GWT page!--}%
<!-- OPTIONAL: include this if you want history support -->
%{--<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>--}%

<!-- Add the rest of the page here, or leave it -->
<!-- blank for a completely dynamic interface.  -->
</body>
