var PlayerModel = Backbone.Model.extend({
  defaults: {
    "goals": 0,
    "assists": 0
  },

  initialize: function(){
    this.listenTo(this, "remove", this.delete);
  },

  delete: function() {
    this.destroy();
  }
});

var PlayerModels = Backbone.Collection.extend({
  model: PlayerModel,

  url: function() {
    return window.location.pathname + "/players";
  }
});
