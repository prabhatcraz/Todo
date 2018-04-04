/* Utils */
var guid = function() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
}

/* Main */
$(document).ready(function() {
    /* Underscore interpolation change.
     with this change, template variables can be placed within {{ }} instead of default <%= %> of
     underscore.
     */

//    _.templateSettings = {
//        interpolate: /\{\{=(.+?)\}\}/g,
//        evaluate: /\{\{(.+?)\}\}/g,
//    };

    window.controller = new Controller();
    controller.render();

    $("#" + TASK_INPUT_ID).on('keyup', function(e) {
        if (e.keyCode == 13) {
            window.controller.addItem();
        }
    });
});