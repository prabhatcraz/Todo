window.BackendHandler = {
    create: function(todo, successCallBack, errorCallBack) {
        $.ajax({
            type: "POST",
            url : "/api/todo/",
            contentType:'application/json',
            data : todo,
            success: successCallBack,
            error: errorCallBack
        });
    },
    get: function(successCallBack) {
        var response = $.ajax({
            type: "GET",
            contentType:'application/json',
            url : "/api/todo/?date=03062018",
            success: successCallBack
        });
    },
    deleteTodo: function(id, successCallBack) {
        $.ajax({
            type: "DELETE",
            url : "/api/todo/" + id,
            contentType:'application/json',
            success: successCallBack
        });
    }
};