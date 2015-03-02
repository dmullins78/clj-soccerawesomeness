this["JST"] = this["JST"] || {};

this["JST"]["templates/game/people-display.html"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __e = _.escape;
with (obj) {
__p += '  <table class="playerstats">\n    <thead>\n      <tr>\n        <th>Player</th>\n        <th>Goals</th>\n        <th>Assists</th>\n        <th>Cards</th>\n        <th>&nbsp;</th>\n        <th>&nbsp;</th>\n      </tr>\n    </thead>\n    <tbody></tbody>\n    <tfoot>\n      <tr>\n        <td colspan="6"><div id="stats"></div></td>\n      </tr>\n    </tfoot>\n  </table>\n\n';

}
return __p
};

this["JST"]["templates/game/person-display.html"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __e = _.escape, __j = Array.prototype.join;
function print() { __p += __j.call(arguments, '') }
with (obj) {
__p += '  <td>' +
((__t = ( name )) == null ? '' : __t) +
'</td>\n  <td>' +
((__t = ( goals )) == null ? '' : __t) +
'</td>\n  <td>' +
((__t = ( assists )) == null ? '' : __t) +
'</td>\n  <td>' +
((__t = ( getCardLabel() )) == null ? '' : __t) +
'</td>\n  <td>\n    ';
 if (editable) { ;
__p += '\n      <a id="edit">Edit</a>\n    ';
 } ;
__p += '\n  </td>\n  <td>\n    ';
 if (editable) { ;
__p += '\n      <a id="remove" class="">Remove</a>\n    ';
 } ;
__p += '\n  </td>\n';

}
return __p
};

this["JST"]["templates/game/person-input.html"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __e = _.escape, __j = Array.prototype.join;
function print() { __p += __j.call(arguments, '') }
with (obj) {
__p += '<form id="player_stats">\n  <div class="row">\n    <div class="small-12 medium-6 columns">\n      <label>Player\n        <select class="name">\n          ';
 _.each(players, function(player){ ;
__p += '\n          <option value="' +
((__t = (player.id)) == null ? '' : __t) +
'">' +
((__t = (player.team )) == null ? '' : __t) +
' - ' +
((__t = (player.name)) == null ? '' : __t) +
'</option>\n          ';
 }); ;
__p += '\n        </select>\n      </label>\n    </div>\n    <div class="small-3 medium-2 columns">\n      <label>Goals\n        <input type="number" class="goals" value="' +
((__t = ( goals )) == null ? '' : __t) +
'" value="0" pattern="[0-9]*"/>\n      </label>\n    </div>\n    <div class="small-3 medium-2 columns">\n      <label>Assists\n        <input type="number" class="assists" value="' +
((__t = ( assists )) == null ? '' : __t) +
'" value="0" pattern="[0-9]*"/>\n      </label>\n    </div>\n    <div class="small-6 medium-2 columns">\n      <label>Card\n        <select class="card">\n          <option value="N" ' +
((__t = ( isCardChecked('N') )) == null ? '' : __t) +
'>None</option>\n          <option value="Y" ' +
((__t = ( isCardChecked('Y') )) == null ? '' : __t) +
'>Yellow</option>\n          <option value="R" ' +
((__t = ( isCardChecked('R') )) == null ? '' : __t) +
'>Red</option>\n        </select>\n      </label>\n    </div>\n  </div>\n  <div class="row">\n    <div class="small-12 medium-4 columns small-centered">\n      <ul class="button-group">\n        <li><a id="cancel" class="tiny button secondary">Cancel</a></li>\n        <li><a id="saved" class="tiny button">Save</a></li>\n      </ul>\n    </div>\n  </div>\n</form>\n';

}
return __p
};

this["JST"]["templates/rosters/alias-display.html"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __e = _.escape;
with (obj) {
__p += '  <td>' +
((__t = ( teamName )) == null ? '' : __t) +
'</td>\n  <td>' +
((__t = ( alias )) == null ? '' : __t) +
'</td>\n  <td><a id="remove" class="">Remove</a></td>\n';

}
return __p
};

this["JST"]["templates/rosters/alias-input.html"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __e = _.escape, __j = Array.prototype.join;
function print() { __p += __j.call(arguments, '') }
with (obj) {
__p += '<form id="team_alias">\n  <div class="row">\n    <div class="small-12 medium-6 columns">\n      <label>Teams\n        <select class="team">\n          ';
 _.each(teams, function(team){ ;
__p += '\n          <option value="' +
((__t = (team.id)) == null ? '' : __t) +
'">' +
((__t = (team.name)) == null ? '' : __t) +
'</option>\n          ';
 }); ;
__p += '\n        </select>\n      </label>\n    </div>\n    <div class="small-2 medium-6 columns">\n      <label>Alias\n        <input type="text" class="alias" value="' +
((__t = ( alias )) == null ? '' : __t) +
'"/>\n      </label>\n    </div>\n  </div>\n  <div class="row">\n    <div class="small-12 medium-6 columns small-centered">\n      <ul class="button-group">\n        <li><a id="cancel" class="tiny button secondary">Cancel</a></li>\n        <li><a id="saved" class="tiny button">Save</a></li>\n      </ul>\n    </div>\n  </div>\n</form>\n';

}
return __p
};

this["JST"]["templates/rosters/aliases-display.html"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __e = _.escape;
with (obj) {
__p += '  <table class="aliases">\n    <thead>\n      <tr>\n        <th>Team</th>\n        <th>Alias</th>\n        <th>&nbsp;</th>\n      </tr>\n    </thead>\n    <tbody></tbody>\n    <tfoot>\n      <tr>\n        <td colspan="3"><div id="stats"></div></td>\n      </tr>\n    </tfoot>\n  </table>\n\n';

}
return __p
};
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
  tagName: "tr",

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

var PlayerListView = Marionette.CompositeView.extend({
  template: "templates/game/people-display.html",
  childView: PlayerItemView,
  childViewContainer: "tbody"
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
