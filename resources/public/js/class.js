$(document).ready(function() {
  console.log('ready.');
  $('#label')
    .on('input', function (e) {
      var newText = e.target.value;
      console.log('label changed: ', newText);
      $('#preview').val(newText);
    });

  $('#cp-foreground')
    .on('changeColor', function(e) {
      var newColor = e.color.toHex();
      console.log('foreground color change: ', newColor);
      $('#preview').css('color', newColor);
    });

  $('#cp-background')
    .on('changeColor', function(e) {
      var newColor = e.color.toHex();
      console.log('background color change: ', newColor);
      $('#preview').css('background-color', newColor);
    });
});
