var app = new Marionette.Application();

var AppRouter = Backbone.Router.extend({
  routes: {
    "" : "showUserList"
  },

  showUserList : function() {
    app.AppController.showUserList();
    if(editable) {
      app.otherRegion.show(new AddPlayerLinkView());
    }
  }
});

var AppController = Marionette.Controller.extend({
  showUserList: function() {
      var userListView = new PlayerListView({ collection: app.collection});
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
  app.collection = new PlayerModels();

  app.collection.fetch();

  Backbone.history.start();
});



app.start();
