/* Functions to interact with local storage */
function getFromStorage() {
    if (typeof(Storage) !== "undefined") {
        return localStorage.todos;
    } else {
        alert("No local storage support!!")
    }
}

function setToStorage(s) {
    if (typeof(Storage) !== "undefined") {
        localStorage.setItem("todos", s);
    } else {
        alert("No local storage support!!")
    }
}

/* Store to interact with Browser's storage */
function Store() {
    this.todos = {};
    if (typeof(Storage) !== "undefined") {
        if (getFromStorage() != undefined) {
            this.todos = JSON.parse(getFromStorage());
        }

    } else { // Sorry! No Web Storage support..
    }
}

Store.prototype = {
    constructor: Store,

    getTodaysTodos: function() {
        return this.getTodos(new Date());
    },

    getTodos: function(d) {
        var self = this;
        var filteredTodo = {};
        _.each(this.todos, function(todo, id) {
            if (todo.date === d.toDateString()) {
                filteredTodo[id] = todo;
            }
        });
        return filteredTodo;
    },

    getAllTodos: function() {
        return this.todos;
    },

    getAllFinishedTasks: function() {
        var allTodos = this.getAllTodos();

        var items = {};
        _.each(_.keys(allTodos), function(key) {
            var item = allTodos[key];
            if (item.status) {
                items[key] = item;
            }
        });
        return items;
    },

    addTodo: function(todo) {
        this.todos[todo.id] = todo;
        this.save();
    },

    save: function() {
        setToStorage(JSON.stringify(this.todos));
    },

    getUnfinishedTodos: function() {
        var todaysList = this.getTodaysTodos();
        var allTodos = this.getAllTodos();

        var todaysItems = _.keys(todaysList);

        var allItems = _.keys(allTodos);
        var pastItems = _.difference(allItems, todaysItems);

        var pastTodos = {};
        _.each(pastItems, function(key) {
            var item = allTodos[key];
            if (!item.status) {
                pastTodos[key] = item;
            }
        });
        return pastTodos;
    },

    remove: function(id) {
        delete this.todos[id];
        this.save();
    },

    setTodoStatus: function(id, status) {
        this.todos[id].status = status;
        this.save();
    }
}