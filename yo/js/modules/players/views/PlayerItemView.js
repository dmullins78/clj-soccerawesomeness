var PlayerItemView = Marionette.ItemView.extend({
  template: "templates/game/person-display.html",
  tagName: "tr",

  events: {
    "click #remove" : "remove2",
    "click #edit" : "edit"
  },

  edit: function() {
    var entryView = new AddPlayerStatView({model: this.model});

    app.otherRegion.show(entryView);
  },

  remove2: function() {
    app.collection.remove(this.model);
  },

  templateHelpers: function () {
    return {
      getCardLabel: function() {
        switch(this.card) {
            case 'R':
                return "Red"
            case 'Y':
                return "Yellow"
            default:
              return "No"
        }
      }
    }
  },

  serializeData: function() {
    return {
      name: this.model.get('playername'),
      goals: this.model.get('goals'),
      assists: this.model.get('assists'),
      card: this.model.get('card')
    };
  }
});

var PlayerListView = Marionette.CompositeView.extend({
  template: "templates/game/people-display.html",
  childView: PlayerItemView,
  childViewContainer: "tbody"
});

