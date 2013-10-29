
<g:if test="${related}">
    <div class="filter-list">${related.description}: <g:renderOntological input="${related}"/>
    <g:link action="list">
        <g:img dir="images/icon" file="close.png"/>
    </g:link>
    </div>
</g:if>
