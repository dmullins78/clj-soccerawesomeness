var AddAliasView = Marionette.ItemView.extend({
  template: "templates/rosters/alias-input.html",

  events: {
    "click #cancel" : "cancel",
    "click #saved" : "save"
  },

  save: function(ev) {
    app.collection.create({
      "teamid" : $('.team', this.$el).val(),
      "teamname": $(".team option:selected", this.$el).text(),
      "alias": $('.alias', this.$el).val()
    });

    app.otherRegion.show(new AddAliasLinkView());
  },

  serializeData: function() {
    return {
      teamId: this.model.get('teamid'),
      teamName: this.model.get('teamname'),
      alias: this.model.get('alias')
    };
  },

  cancel: function(ev) {
    app.otherRegion.show(new AddAliasLinkView());
  }
});

