
var statusIntervalRefId;

function askProcessingStatusFunction() {

    $.getJSON('/webapp/processing')
        .done( function(json) {
            console.log(json.percent + ' / ' + json.code);
            if(json.code != -1) {
                $('.status-info').html(json.code);
            }
            else {
                $('.status-info').html(json.percent);
            }

            if (json.percent == 100) {
                console.log('processing done')
                clearInterval(statusIntervalRefId);
            }
        })
        .fail(function( jqxhr, textStatus, error ) {
            $('.status-info').html("something exploded");
        });

};
