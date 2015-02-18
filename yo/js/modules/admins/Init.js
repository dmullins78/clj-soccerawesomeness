$('.deleteAdmin').click(function(ev) {
  ev.preventDefault();

  var that = this;
  $.ajax({
    url: $(this).attr('href'),
    type: 'DELETE'
  }).done(function() {
    $( that ).parent().parent().remove();
  });
});

