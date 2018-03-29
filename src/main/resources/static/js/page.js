/* UI and rendering. */
function PageRenderer(store) {
    this.store = store;
    var htmlRow = $(TODO_ROW_TEMPLATE_ID).html();
    this.todoTemplate = _.template($(TODO_ROW_TEMPLATE_ID).html());
}

PageRenderer.prototype = {
    constructor: PageRenderer,

    render: function(withHistory) {
        var self = this;
        this.cards = {};
        this.cards['todaysList'] = this.getTodaysCard();
        this.cards['pastList'] = this.getUnfinishedCard();

        if(withHistory) {
            this.cards['historyList'] = this.getHistoryCard();
        }

        $(CARD_HOLDER_ID).html("");

        _.each(this.cards, function(card) {
            card.render();
        });
    },

    getTodaysCard: function() {
        this.todaysList = this.store.getTodaysTodos();

        var todoTemplate = _.template($(TODO_ROW_TEMPLATE_ID).html());
        var todaysListCard = new Card({
            title: TODAYS_CARD_TITLE,
            items: this.todaysList,
            parentId: CARD_HOLDER_ID,
            id: TODAYS_LIST_CARD_ID,
            store: this.store,
            itemTemplate: todoTemplate
        });
        return todaysListCard;
    },

    getUnfinishedCard: function() {
        var self = this;
        var todoTemplate = _.template($(TODO_ROW_TEMPLATE_ID).html());
        var pastListCard = new Card({
            title: PAST_ITEMS_CARD_TITLE,
            items: self.store.getUnfinishedTodos(),
            parentId: CARD_HOLDER_ID,
            id: PAST_LIST_CARD_ID,
            store: this.store,
            itemTemplate: todoTemplate
        });
        return pastListCard;
    },

    getHistoryCard: function() {
        var self = this;
        var todoTemplate = _.template($(TODO_IMUTABLE_TEMPLATE_ID).html());
        var pastListCard = new Card({
            title: TASKS_EVER_COMPLETED_TITLE,
            items: self.store.getAllFinishedTasks(),
            parentId: CARD_HOLDER_ID,
            id: TASKS_EVER_COMPLETED_CARD_ID,
            store: this.store,
            itemTemplate: todoTemplate
        });
        return pastListCard;
    }
}