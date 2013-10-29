
<br/>
<div id="search_bar">
    <div id="search_bar_header">Search&nbsp;&nbsp;</div>
    <g:link action="search"
        class="${selected=='erpEffects'?'selected':''}"
            controller="erpAnalysisResult">Spatiotemporal</g:link>
    <g:link action="show"
            class="${selected=='pattern'?'selected':''}"
            controller="pattern">Experimental Contrast</g:link>
    <g:link action="reference"
            class="${selected=='reference'?'selected':''}"
            controller="erpAnalysisResult">ROI Definitions</g:link>
</div>