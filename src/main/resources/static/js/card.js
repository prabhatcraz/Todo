/**
 * This class assumes that it has to append to div with id - cardBody 
 */
function Card(properties) {
    this.id           = properties.id;
    this.title        = properties.title;
    this.items        = properties.items;
    this.parentId     = properties.parentId;
    this.store        = properties.store;
    this.itemTemplate = properties.itemTemplate
}

Card.prototype = {
    render: function() {
        var self = this;
        var cardTemplate = _.template($("#" + CARD_TEMPLATE_ID).html());
        $(CARD_HOLDER_ID).append(cardTemplate({
            id: self.id,
            title: self.title
        }));

        // We might re-render card during user events, so clear the cardBody
        var card = $("#" + self.id);
        var cardBody = card.find(".cardBody")[0];
        $(cardBody).html("");

        _.each(self.items, function(item) {
            var templateData = {
                "description" : item.description,
                "checked"     : item.status ? "checked" : "",
                "statusClass" : item.status ? "striked" : "",
                "id"          : item.id,
                "taskDate"    : item.date
            };

            $(cardBody).append(self.itemTemplate(templateData));
        });

        $(".close").click(function() {
            var id = $(this).data("id");
            // self.store.remove(id);
            window.controller.deleteItem(id);
        });

        $(".markDone").change(function() {
            var id = $(this).data("id");
            console.log(id);
            window.controller.setItemStatus(id, this.checked);
        });
    }
};