window.TodoDAL = {
    todos: [],
    create: function(todo, successCallBack, errorCallBack) {
        var self = this;
        $.ajax({
            type: "POST",
            url : "/api/todo/",
            contentType:'application/json',
            data : todo,
            success: function(data) {
                console.log(data);
                self.todos.push(data);
                successCallBack();
            },
            error: errorCallBack
        });
    },
    get: function(successCallBack) {
        var self = this;
        var response = $.ajax({
            type: "GET",
            contentType:'application/json',
            url : "/api/todo/?date=03062018",
            success: function(data) {
                self.todos = data;
                successCallBack();
            }
        });
    },
    deleteTodo: function(id, successCallBack) {
        var self = this;
        $.ajax({
            type: "DELETE",
            url : "/api/todo/" + id,
            contentType:'application/json',
            success: function() {
                self.todos = self.todos.filter(todo => todo.id != id);
                successCallBack();
            }
        });
    }
};