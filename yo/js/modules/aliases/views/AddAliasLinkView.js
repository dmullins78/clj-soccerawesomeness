var AddAliasLinkView = Marionette.ItemView.extend({
  template: _.template("<a id='addAlias'>Add Alias</a>"),

  events: {
    "click #addAlias" : "showEntryScreen"
  },

  showEntryScreen: function(ev) {
    app.otherRegion.show(new AddAliasView({model: new AliasModel() }));
  }

});
