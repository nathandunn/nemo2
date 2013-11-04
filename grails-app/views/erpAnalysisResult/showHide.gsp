<div class="hideshow-buttons">
    <input id="hideButton" type="button" value="Hide Images" onclick="
        $('.erp-image').hide();
        $('#showButton').show();
        $('#hideButton').hide();
    "/>

    <input id="showButton" type="button" style="display: none;" value="Show Images" onclick="
        $('.erp-image').show();
        $('#showButton').hide();
        $('#hideButton').show();
    "/>
</div>
