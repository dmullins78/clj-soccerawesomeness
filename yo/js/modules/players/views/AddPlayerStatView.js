var AddPlayerStatView = Marionette.ItemView.extend({
  template: "templates/game/person-input.html",

  events: {
    "click #cancel" : "cancel",
    "click #saved" : "save"
  },

  save: function(ev) {
    app.collection.create({
      "id" : $('.name', this.$el).val(),
      "playername": $(".name option:selected", this.$el).text(),
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
      name: this.model.get('playername'),
      goals: this.model.get('goals'),
      assists: this.model.get('assists'),
      card: this.model.get('card'),
      players: players
    };
  },

  cancel: function(ev) {
    app.otherRegion.show(new AddPlayerLinkView());
  }
});

