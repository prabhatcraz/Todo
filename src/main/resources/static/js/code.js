var Todo = React.createClass({
    deleteMe: function(e) {
        BackendHandler.deleteTodo(this.props.todo.id, TodoTable.render);
    },
    render: function() {
        return (
            <div className="aTodo alert alert-dismissible alert-primary">
                <button type="button" className="close" data-dismiss="alert"
                 onClick={(e) => this.deleteMe(e)} >&times;</button>
                {this.props.todo.title}
            </div>);
    }
});

var TodoTable = React.createClass({
    render: function() {
        var rows = [];
        var i = 0;
        this.props.todos.forEach(function(todo) {
          rows.push(<Todo todo={todo} key={todo.id} />);
        });
        return (
            <div className="container">
                {rows}
            </div>
        );
    }
});

var CreateTodo = React.createClass({
    componentDidMount: function() {
        this.nameInput.focus();
    },
    _handleKeyPress: function(e) {
        if (e.key === 'Enter') {
            BackendHandler.create(
                JSON.stringify({
                    title: e.target.value,
                    userId: "user1"
                }), function() {

                }, function() {
                    alert("error");
                }
            );
            e.target.value = "";
        }
    },
    render: function() {
        return (
            <div id="createTodo" className="form-group centre">
                <input ref={input => { this.nameInput = input; }} onKeyPress={this._handleKeyPress}
                className="form-control form-control-lg" type="text" placeholder="Today I want to ..." id="inputLarge" />
            </div>
        )
    }
});

var Header = React.createClass({
    render: function() {
        return (
        <div className="navContainer">
            <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                <a className="navbar-brand" href="#">Todo</a>
            </nav>
        </div>
        );
    }
});

var Footer = React.createClass({
    render: function() {
        return (
        <div id="footerContainer" className="centre">
            <hr/>
            <p id="email" className="centre">
                prabhatcraz@gmail.com
            </p>
        </div>);
    }
});

var App = React.createClass({
    componentDidMount: function() {
        var self = this;
        return BackendHandler.get(function(todos) {
            console.log(todos);
            self.setState({todos: todos});
        });
    },
    getInitialState: function () {
        return {todos: []};
    },

    render: function() {
        var elements = [];
        elements.push(<Header  key={'header'} />);
        elements.push(<CreateTodo  key={'createTodo'} />);
        elements.push(<TodoTable todos={this.state.todos} key={'todosContainer'} />);
        elements.push(<Footer  key={'footer'} />);

        return(
            <div class="mainContainer">
                {elements}
            </div>
        )
    }
});

var renderPage = function() {
    ReactDOM.render(<App />, document.getElementById('root'));
}

renderPage();
