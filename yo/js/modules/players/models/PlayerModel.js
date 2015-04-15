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


var RosterModel = Backbone.Model.extend({
  value: function() {
    return this.get("id");
  },

  label: function() {
    return this.get("name");
  }
});

var RosterModels = Backbone.Collection.extend({
  model: RosterModel,

  url: function() {
    return base + '/players/all';
  }
});
