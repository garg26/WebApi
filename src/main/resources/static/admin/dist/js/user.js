$(document).ready(function() {
	var table = $('#all-users').DataTable();
  table.order( [ 0, 'desc' ] ).draw();

	/*$('.btn btn-primary btn-block').click(function() {
		var currentTD = $(this).parents('tr').find('td');
		if ($(this).html() == 'Edit') {
			$.each(currentTD, function() {
				$(this).prop('contenteditable', true)
				$('#btnEdit').attr('hidden');
				$('#btnSave').removeAttr('hidden');
			});
		} else {
			$.each(currentTD, function() {
				$(this).prop('contenteditable', false)
			});
		}

		

	})*/
	/*$('#btnSave').click(function() {
		alert("Inside Save button");
		location.reload();
	})*/
});
