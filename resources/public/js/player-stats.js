this["JST"] = this["JST"] || {};

this["JST"]["templates/game/person-display.html"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __e = _.escape, __j = Array.prototype.join;
function print() { __p += __j.call(arguments, '') }
with (obj) {
__p += '<div class="row">\n  <div class="small-12 medium-2 columns">\n    <span>' +
((__t = ( name )) == null ? '' : __t) +
'</span>\n  </div>\n  <div class="small-2 medium-2 columns">\n    <span>' +
((__t = ( goals )) == null ? '' : __t) +
' Goals</span>\n  </div>\n  <div class="small-2 medium-2 columns">\n    <span>' +
((__t = ( assists )) == null ? '' : __t) +
' Assists</span>\n  </div>\n  <div class="small-3 medium-2 columns">\n    ' +
((__t = ( getCardLabel() )) == null ? '' : __t) +
' Card\n  </div>\n  ';
 if (editable) { ;
__p += '\n  <div class="small-5 medium-4 columns">\n    <a id="edit">Edit</a>\n    <a id="remove" class="">Remove</a>\n  </div>\n  ';
 } ;
__p += '\n</div>\n';

}
return __p
};

this["JST"]["templates/game/person-input.html"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __e = _.escape, __j = Array.prototype.join;
function print() { __p += __j.call(arguments, '') }
with (obj) {
__p += '<div class="row">\n  <form id="player_stats">\n    <div class="small-12 medium-7 columns">\n      <label>Player\n        <select class="name">\n          ';
 _.each(players, function(player){ ;
__p += '\n          <option value="' +
((__t = (player.id)) == null ? '' : __t) +
'">' +
((__t = (player.team + '-' + player.name)) == null ? '' : __t) +
'</option>\n          ';
 }); ;
__p += '\n        </select>\n      </label>\n    </div>\n    <div class="small-2 medium-1 columns">\n      <label>Goals\n        <input type="text" class="goals" value="' +
((__t = ( goals )) == null ? '' : __t) +
'"/>\n      </label>\n    </div>\n    <div class="small-2 medium-1 columns">\n      <label>Assists\n        <input type="text" class="assists" value="' +
((__t = ( assists )) == null ? '' : __t) +
'"/>\n      </label>\n    </div>\n    <div class="small-3 medium-3 columns">\n      <label>Card\n        <select class="card">\n          <option value="N" ' +
((__t = ( isCardChecked('N') )) == null ? '' : __t) +
'>None</option>\n          <option value="Y" ' +
((__t = ( isCardChecked('Y') )) == null ? '' : __t) +
'>Yellow</option>\n          <option value="R" ' +
((__t = ( isCardChecked('R') )) == null ? '' : __t) +
'>Red</option>\n        </select>\n      </label>\n    </div>\n    <div class="row">\n      <div class="small-12 medium-4 columns small-centered">\n        <ul class="button-group">\n          <li><a id="cancel" class="tiny button">Cancel</a></li>\n          <li><a id="saved" class="tiny button">Save</a></li>\n        </ul>\n      </div>\n    </div>\n  </form>\n</div>\n';

}
return __p
};
var PlayerModel = Backbone.Model.extend({
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

var AddPlayerLinkView = Marionette.ItemView.extend({
  template: _.template("<a id='addStat'>Add Player Details</a>"),

  events: {
    "click #addStat" : "showEntryScreen"
  },

  showEntryScreen: function(ev) {
    app.otherRegion.show(new AddPlayerStatView({model: new PlayerModel() }));
  }

});

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


var PlayerItemView = Marionette.ItemView.extend({
  template: "templates/game/person-display.html",

  events: {
    "click #remove" : "remove2",
    "click #edit" : "edit"
  },

  edit: function() {
    var entryView = new AddPlayerStatView({model: this.model});

    app.otherRegion.show(entryView);
  },

  remove2: function() {
    app.collection.remove(this.model);
  },

  templateHelpers: function () {
    return {
      getCardLabel: function() {
        switch(this.card) {
            case 'R':
                return "Red"
            case 'Y':
                return "Yellow"
            default:
              return "No"
        }
      }
    }
  },

  serializeData: function() {
    return {
      name: this.model.get('playername'),
      goals: this.model.get('goals'),
      assists: this.model.get('assists'),
      card: this.model.get('card')
    };
  }
});

var PlayerListView = Marionette.CollectionView.extend({
  childView: PlayerItemView
});


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
