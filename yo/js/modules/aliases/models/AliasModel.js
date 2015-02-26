var AliasModel = Backbone.Model.extend({
  initialize: function(){
    this.listenTo(this, "remove", this.delete);
  },

  delete: function() {
    this.destroy();
  }
});

var AliasModels = Backbone.Collection.extend({
  model: AliasModel,

  url: function() {
    return window.location.pathname + "/aliases";
  }
});
