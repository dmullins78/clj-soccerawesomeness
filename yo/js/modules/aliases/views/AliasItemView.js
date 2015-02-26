var AliasItemView = Marionette.ItemView.extend({
  template: "templates/rosters/alias-display.html",
  tagName: "tr",

  events: {
    "click #remove" : "remove2"
  },

  edit: function() {
    var entryView = new AddAliasView({model: this.model});

    app.otherRegion.show(entryView);
  },

  remove2: function() {
    app.collection.remove(this.model);
  },

  serializeData: function() {
    return {
      teamId: this.model.get('teamid'),
      teamName: this.model.get('teamname'),
      alias: this.model.get('alias')
    };
  }
});

var AliasListView = Marionette.CompositeView.extend({
  template: "templates/rosters/aliases-display.html",
  childView: AliasItemView,
  childViewContainer: "tbody"
});

