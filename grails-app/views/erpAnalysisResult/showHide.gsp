<div class="hideshow-buttons">
    <input id="hideButton" type="button" value="Hide Images" onclick="
        $('.erp-image').hide('fast');
        $('#showButton').show('fast');
        $('#hideButton').hide('fast');
    "/>

    <input id="showButton" type="button" style="display: none;" value="Show Images" onclick="
        $('.erp-image').show('fast');
        $('#showButton').hide('fast');
        $('#hideButton').show('fast');
    "/>
</div>
