$('#classes').on('change', function() {
  var i, uuid, prefix, qry;
    
  prefix = "http://localhost:3000";
  qry = "";

  var selected = this.selectedOptions;
  for (i = 0; i < selected.length; ++ i) {
    uuid = selected[i].value;
    if (qry.length === 0) {
      qry = "?labels=" + uuid;
    } else  {
      qry += "+" + uuid;
    }
  }
  
  var location = prefix + qry;
  
  console.log('Redirecting to ' + location);
  window.location = location;
});
