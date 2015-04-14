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

  app.allPlayers = new RosterModels();
  app.allPlayers.add([
    {id: 1, name: "John Smith"},
    {id: 2, name: "John Rolfe"},
    {id: 3, name: "Govenor Ratcliffe"},
    {id: 4, name: "Pocahontas"},
    {id: 5, name: "Kocoum"}
  ]);

  app.collection.fetch();

  Backbone.history.start();
});



app.start();
