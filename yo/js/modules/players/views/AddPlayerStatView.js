var AddPlayerStatView = Marionette.ItemView.extend({
  template: "templates/game/person-input.html",

  events: {
    "click #cancel" : "cancel",
    "click #saved" : "save"
  },

  onShow: function() {
    new AutoCompleteView({
        value: $("#playerId"),
        input: $("#playerSearch"),
        model: app.allPlayers
    }).render();
  },

  save: function(ev) {
    app.collection.create({
      "id" : $('#playerId').val(),
      "playername": $('#playerSearch').val(),
      "goals": $('.goals', this.$el).val(),
      "assists": $('.assists', this.$el).val(),
      "card": $('.card', this.$el).val()
    });

    app.otherRegion.show(new AddPlayerLinkView());
  },

  templateHelpers: function () {
    return {
      isCardChecked: function(value){
        return this.card === value ? 'selected' : "";
      }
    };
  },

  serializeData: function() {
    return {
      id: this.model.get('id'),
      name: this.model.get('playername'),
      goals: this.model.get('goals'),
      assists: this.model.get('assists'),
      card: this.model.get('card'),
    };
  },

  cancel: function(ev) {
    app.otherRegion.show(new AddPlayerLinkView());
  }
});

