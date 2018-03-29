/* Todo model */
function Todo(id, date, description, status) {
    this.id = id;
    this.date = date;
    this.description = description;
    this.status = status;
};

Todo.prototype = {
    constructor: Todo,
    serialize: function() {
        var j = {
            "id": this.id,
            "d": this.date.toString(),
            "de": this.description,
            "s": this.status
        };
        return JSON.stringify(j);
    },

    deserialize: function(s) {
        var j = JSON.parse(s);
        this.id = j.id;
        this.date = new Date(j.d);
        this.description = j.de;
        this.status = j.s;
        return this;
    }
}