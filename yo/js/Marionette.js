Marionette.Renderer = {
  render: function(template, data){
    if(typeof template === 'function') {
        return template(data);
    }
    else {
        if(!JST[template]) throw "Template '" + template + "' not found!";
        return JST[template](data);
    }
  }
};

