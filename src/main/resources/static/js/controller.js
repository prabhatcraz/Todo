function Controller() {
    this.store = new Store();
    this.pageRenderer = new PageRenderer(this.store);
}

Controller.prototype = {
    addItem: function(item) {
        var description = $(TASK_INPUT_ID).val();
        if (_.isEmpty(description)) return;

        $(TASK_INPUT_ID).val("");

        var todo = new Todo(guid(), new Date().toDateString(), description);

        this.store.addTodo(todo);
        this.store.save();

        this.render();
    },

    deleteItem: function(itemId) {
        this.store.remove(itemId);
        this.render();
    },

    render: function() {
        var showHistory = localStorage.showHistory == "true";
        this.pageRenderer.render(showHistory);
    },

    setItemStatus: function(itemId, isDone) {
        this.store.setTodoStatus(itemId, isDone);
        this.render();
    },

    toggleShowHistory: function() {
        if(localStorage.showHistory == "true") {
            localStorage.showHistory = false;
        } else {
            localStorage.showHistory = true;
        }
        console.log(localStorage.showHistory);
        this.render();
    }
};