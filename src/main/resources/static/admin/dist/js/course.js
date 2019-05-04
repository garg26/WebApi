$(document).ready(function () {
  var table = $('#all-courses').DataTable();
  table.order( [ 1, 'desc' ] ).draw();
});



