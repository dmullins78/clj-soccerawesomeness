var AddPlayerLinkView = Marionette.ItemView.extend({
  template: _.template("<a id='addStat'>Add Player Details</a>"),

  events: {
    "click #addStat" : "showEntryScreen"
  },

  showEntryScreen: function(ev) {
    app.otherRegion.show(new AddPlayerStatView({model: new PlayerModel() }));
  }

});
