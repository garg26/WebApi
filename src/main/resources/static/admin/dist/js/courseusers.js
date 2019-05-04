$(document).ready(function() {
	$('#all-linkages').DataTable();
	$('#actionDiv').hide();
});

var selectedEmails = [];

$('.select-user').change(function() {
	$('input[name=select-user]:checked').each(function(i) {
		selectedEmails[i] = $(this).val();

	});

	if (selectedEmails.length > 0) {
		$('#actionDiv').show();
	} else {
		$('#actionDiv').hide();
	}

	/*
	 * var email = $('input[name=select-user]:checked').val();
	 * selectedEmails.push(email);
	 */
	console.log(selectedEmails);
});

function selectStudents(email) {

	selectedEmails.push(email);
	console.log(selectedEmails);

}

function openMessageModal() {
	$('#messageModal').modal();
}

function openValidityModal() {
	$('#validityModal').modal();
}

function openPaidExtensionModal() {
	$('#paidExtModal').modal();
}

function sendMessage(el) {

	var requestObj = {};
	var message = {};
	if ($(el).siblings('textarea').val() != 0) {
		message["body"] = $('textarea').val();
	}
	requestObj["message"] = message;
	requestObj["receiverList"] = selectedEmails;
	/*
	 * $.ajax({ url : apiBasePath + '/admin/broadcast', type : "POST",
	 * contentType : "application/json", processData : false, data :
	 * JSON.stringify(requestObj), headers : { // "authorization" : "Bearer " // +
	 * window.localStorage.getItem('access_token'), // // }, success :
	 * function(response) { alert("Message sent successfully"); }, error :
	 * function(response) { alert("Error occured") } });
	 */

	$.ajax({
		async : false,
		url : apiBasePath + "/admin/broadcast",
		type : "POST",
		data : JSON.stringify(requestObj),
		dataType : "json",
		contentType : "application/json",
		processData : false,
		// "headers" : {
		// "authorization" : "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },

		success : function(response) {
			alert("Message sent successfully");
		},

		error : function(response) {
			alert("Error occured");

		}
	});

}

function updateValidity(batchId) {

	var requestObj = {};
	requestObj["emails"] = selectedEmails;
	requestObj["newValidity"] = $('#txtvalidity').val();
	requestObj["batchId"] = batchId;

	/*
	 * $.ajax({ url : apiBasePath + '/userbatchlinkages', type : "PUT",
	 * contentType : "application/json", processData : false, data :
	 * JSON.stringify(requestObj), dataType : "json", headers : {
	 * "authorization" : "Bearer " +
	 * window.localStorage.getItem('access_token'), }, success :
	 * function(response) { alert("Validity Updated Successfully");
	 * //location.reload(); }, error : function(response) { alert("Error
	 * occured"); } });
	 */
	/*
	 * $.ajax({ url : apiBasePath + "/admin_sales/userbatchlinkages", type :
	 * "PUT", data : JSON.stringify(requestObj), dataType : "json", contentType :
	 * "application/json", processData : false, headers : { "authorization" :
	 * "Bearer " + window.localStorage.getItem('access_token'), }, success :
	 * function(response) { // $('#loader').hide(); alert("Successfully updated
	 * validity"); location.reload(); }, error : function(response) { //
	 * $('#loader').hide(); alert("Failed in updating validity");
	 * location.reload(); } });
	 */
	/*
	 * $.ajax({ "async" : true, "crossDomain" : true, "url" : apiBasePath +
	 * "/admin_sales/userbatchlinkages", "method" : "PUT", "data" :
	 * JSON.stringify(requestObj), "contentType" : "application/json",
	 * "dataType" : "json", "headers" : { // "authorization" : "Bearer " // +
	 * window.localStorage.getItem('access_token'), // // }, success :
	 * function(response) {
	 * 
	 * alert("Successfully updated validity"); location.reload(); }, error :
	 * function(response) {
	 * 
	 * alert("Failed in updating validity"); } });
	 */

	$.ajax({
		async : false,
		url : apiBasePath + "/admin_sales/userbatchlinkages",
		type : "PUT",
		data : JSON.stringify(requestObj),
		dataType : "json",
		contentType : "application/json",
		processData : false,
		// "headers" : {
		// "authorization" : "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },

		success : function(response) {
			alert("Successfully updated validity");
			location.reload();
		},

		error : function(response) {
			alert("Error occured");

		}
	});

}

function givePaidExtension(batchId) {
	var request = {};
	request["batchId"] = batchId;
	request["paymentMode"] = $("#dropPayMode :selected").val();
	request["email"] = selectedEmails[0];
	request["amount"] = $('#txtAmount').val();
	request["validity"] = $('#txtExtValidity').val();

	$
			.ajax({
				async : false,
				url : apiBasePath + "/admin_sales/give-paid-extension",
				type : "POST",
				data : JSON.stringify(request),
				dataType : "json",
				contentType : "application/json",
				processData : false,

				success : function(response) {
					alert("Paid Extension Order Created. Please approve in from the all orders page.");
					location.reload();
				},

				error : function(response) {
					alert("Error occured");

				}
			});
}