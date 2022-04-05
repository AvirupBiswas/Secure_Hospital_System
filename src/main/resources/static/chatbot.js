$(function() {
    INDEX = 0;
    $("#chat-submit").click(function(e) {
        var rawText = $("#chat-input").val();
        if(rawText.trim() == ''){
            return false;
        }

        generate_message(rawText, 'self');
        $.ajax({
            data: {
                msg: rawText,
            },
            type: "POST",
            url: "https://cse-chatbot.herokuapp.com/get_response"
            // url: "http://localhost:5000/get_response"
        }).done(function(data) {
            console.log(data['data']);
            console.log(data['success']);
            
            // var msg = urlify(data['data']);
            var msg = data['data'];
            generate_message(msg, 'user');
            
        });
        event.preventDefault();
    });



    function generate_message(msg, type) {
        INDEX++;
        var str="";
        str += "<div id='cm-msg-"+INDEX+"' class=\"chat-msg "+type+"\">";
        str += "          <div class=\"cm-msg-text\">";
        str += msg;
        str += "          <\/div>";
        str += "        <\/div>";
        $(".chat-logs").append(str);
        $("#cm-msg-"+INDEX).hide().fadeIn(300);
        if(type == 'self'){
            $("#chat-input").val('');
        }
        $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight}, 1000);
    }

    $(document).delegate(".chat-btn", "click", function() {
        var value = $(this).attr("chat-value");
        var name = $(this).html();
        $("#chat-input").attr("disabled", false);
        generate_message(name, 'self');
    })

    function urlify(text) {
        var urlRegex = /(https?:\/\/[^\s]+)/g;
        return text.replace(urlRegex, function(url) {
            return '<a href="' + url + '">' + url + '</a>';
        })
        // or alternatively
        // return text.replace(urlRegex, '<a href="$1">$1</a>')
    }

    $("#chat-circle").click(function() {
        $("#chat-circle").toggle('scale');
        $(".chat-box").toggle('scale');
    })

    $(".chat-box-toggle").click(function() {
        $("#chat-circle").toggle('scale');
        $(".chat-box").toggle('scale');
    })

})
