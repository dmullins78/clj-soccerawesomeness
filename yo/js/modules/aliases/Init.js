var app = new Marionette.Application();

var AppRouter = Backbone.Router.extend({
  routes: {
    "" : "showAliases"
  },

  showAliases : function() {
    app.AppController.showAliases();
    app.otherRegion.show(new AddAliasLinkView());
  }
});

var AppController = Marionette.Controller.extend({
  showAliases: function() {
      var userListView = new AliasListView({ collection: app.collection});
      app.mainRegion.show(userListView);
    }
});

app.addInitializer(function() {
  app.addRegions({
    mainRegion : '#app',
    otherRegion : '#stats'
  });

  app.AppController = new AppController();
  app.Router = new AppRouter();
  app.collection = new AliasModels();

  app.collection.fetch();

  Backbone.history.start();
});



app.start();
