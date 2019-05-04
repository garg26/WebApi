var orderId;
var selectedUserId;

function searchEmails() {

	$('#userList').hide();
	if ($('#userEmailSearch').val().length > 3) {
		getUserEmails();
	}

}

$(document).ready(function() {

	var table = $('#all-orders').DataTable({

		"columnDefs" : [ {
			"targets" : [ 6 ],
			"visible" : false,
			"searchable" : true
		}, {
			"targets" : [ 7 ],
			"visible" : false,
			"searchable" : true
		} ],
    "deferRender": true
	});
	table.order([ 0, 'desc' ]).draw();
	$('#allunitsTbl').DataTable();
	$('#inner-hidden-table').DataTable();
	$('#itemsTbl').DataTable();
	$('#prodTypeDD').hide();
	$('#existingAddressInfo').hide();
	$('#newAddressInfo').hide();
	$('.selectpicker').selectpicker();
	$('#productFilters').hide();
	$('#userList').hide();
	$('#divEMI').hide();
	$('#divInstallment').hide();

	// $('#add-new-order').submit(function(e) {
	// 	e.preventDefault();
	//
	// 	var address = {};
	//
	// 	address["firstName"] = $('#txtfname').val();
	// 	address["lastname"] = $('#txtlname').val();
	// 	address["mobileNumber"] = $('#txtMob').val();
	// 	address["addressText"] = $('#txtAdd').val();
	// 	address["pincode"] = $('txtPincode').val();
	// 	address["city"] = $('#txtCity').val();
	// 	address["state"] = $('#txtState').val();
	// 	address["defaultAddress"] = true;
	// 	var request = {};
	// 	request["address"] = address;
	// 	request["userId"] = selectedUserId;
	//
	// 	$.ajax({
	// 		"async" : false,
	// 		"crossDomain" : true,
	// 		"url" : "/admin_sales/addresses",
	// 		"method" : "POST",
	// 		"processData" : false,
	// 		"dataType" : "json",
	// 		"data" : JSON.stringify(request),
	// 		"contentType" : "application/json",
	// 		"mimeType" : "multipart/form-data",
	// 		// "headers": {
	// 		// "authorization": "Bearer "
	// 		// + window.localStorage
	// 		// .getItem('access_token'),
	// 		//
	// 		// },
	// 		success : function(response) {
	// 			alert("Address saved succesfully");
	// 			changeDefaultAddress(response.addressId, selectedUserId);
	// 			showAddresses(selectedUserId);
	//
	// 		},
	// 		error : function(response) {
	// 			$('#loading').remove();
	// 			alert("Error occurred while saving address");
	// 		}
	// 	});
	//
	// });

	if ($('#itemsTbl').length > 0) {
		$('#btnSubmitOrder').show();
	} else {
		$('#btnSubmitOrder').hide();

	}

});


function saveNewAddress() {
  var firstName = $('#txtfname').val().length;
  var lastName =  $('#txtlname').val().length;
  var mobileNumber = $('#txtMob').val().length;
  var address =$('#txtAdd').val().length;
  var pincode = $('#txtPincode').val().length;
  var city =$('#txtCity').val().length;
  var state = $('#txtState').val().length;

  if(firstName != "" && lastName != "" && mobileNumber != "" && address != "" && pincode != '' && city != "" && state !="") {

    var address = {};

    address["firstName"] = $('#txtfname').val();
    address["lastname"] = $('#txtlname').val();
    address["mobileNumber"] = $('#txtMob').val();
    address["addressText"] = $('#txtAdd').val();
    address["pincode"] = $('#txtPincode').val();
    address["city"] = $('#txtCity').val();
    address["state"] = $('#txtState').val();
    address["defaultAddress"] = true;
    var request = {};
    request["address"] = address;
    request["userId"] = selectedUserId;

    $.ajax({
      "async": false,
      "crossDomain": true,
      "url": "/admin_sales/addresses",
      "method": "POST",
      "processData": false,
      "dataType": "json",
      "data": JSON.stringify(request),
      "contentType": "application/json",
      "mimeType": "multipart/form-data",
      // "headers": {
      // "authorization": "Bearer "
      // + window.localStorage
      // .getItem('access_token'),
      //
      // },
      success: function (response) {
        alert("Address saved succesfully");
         changeDefaultAddress(response.addressId, selectedUserId);
        showAddresses(selectedUserId);

      },
      error: function (response) {
        $('#loading').remove();
        alert("Error occurred while saving address");
      }
    });
  }
  else{
  	alert("please fill all required fields");
	}

}

$("#edit-order").on('click', function(e) {
	$(".input-error").html("");
	var nChecked = $('input[name=select-order]:checked').length;
	if (nChecked == 1) {
		var orderId = $('input[name=select-order]:checked').val();
		window.location.href = '/admin/ecommerce/orders/update/' + orderId;
	} else {
		$(".input-error").html("Select exactly one order to edit!");
	}
});

$("#trash-order").on('click', function(e) {
	$(".input-error").html("");
	var nChecked = $('input[name=select-order]:checked').length;
	if (nChecked == 1) {
		// todo: call update order API with status changed to TRASH and remove
		// the row from table.
	} else {
		$(".input-error").html("Select exactly one order to trash!");
	}
});

var orderIdToUpdate = -1;
	$("#saveNoteBtn").on('click', function(e) {
		var newNote = {};
		newNote["noteContent"] = $('#txtContent').val();
		$.ajax({
			"async" : false,
			"crossDomain" : true,
			"url" : "/admin_sales/orders/" + orderIdToUpdate + "/ordernotes",
			"method" : "POST",
			"processData" : false,
			"data" : JSON.stringify(newNote),
			"dataType" : "json",
			"contentType" : "application/json",

			// "headers": {
			// "authorization": "Bearer "
			// + window.localStorage.getItem('access_token'),
			//
			// },
			success : function(response) {

				alert("Note added succesfully");
				this.selectedOrderId = orderIdToUpdate;
				$('#addNoteModal').modal('hide');
				e.preventDefault();

			},
			error : function(response) {

				alert("Error occurred while adding notes");
			}
		});

	});

	$("#approveBtnTrue").on('click', function(e) {
		approveOrder(true);
		e.preventDefault();
	});


	$("#approveBtnFalse").on('click', function(e) {
		approveOrder(false);
		e.preventDefault();
	});

	$("#refundBtn").on('click', function(e) {
		refundOrder();
		$('#refundReasonModal').modal('hide');
		e.preventDefault();
	});

	$("#partialrefundBtn").on('click', function(e) {
		partialRefundOrder();
		$('#partialRefundReasonModal').modal('hide');
		e.preventDefault();
	});

function askNotification(orderId) {
	$('#sendNotModal').modal();

	$('#txtOrderId').val(orderId);

}

function callApproverApi(orderId, sendNotif)
{
	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : apiBasePath + "/admin_sales/orders/" + orderId
				+ "/approve?sendNotification=" + sendNotif,
		"method" : "PATCH",
		"processData" : false,
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },
		success : function(response) {
			/*
			 * $.ajax({ "async" : true, "crossDomain" : true, "url" :
			 * "/admin/ecommerce/orders", "method" : "GET", "processData" :
			 * false, "dataTpe" : "json", "mimeType" : "multipart/form-data",
			 * "headers" : { "authorization" : "Bearer " +
			 * window.localStorage.getItem('access_token'), }, success :
			 * function(response) { location.reload(); }, error :
			 * function(response) { alert("Error occured while refreshing."); }
			 * });
			 */
			$('#loading').remove();
    $('#orderApproveLoader').hide();
      $("#" + orderId).html('Approve Order');
			$('.approve-button[id="'+orderId+'"]').attr("disabled",true);
			$('#status'+orderId).text('APPROVED');
      $('#sendNotModal').modal('hide');

			/*location.reload();*/
		},
		error : function(response) {
			$('#loading').remove();
			alert("Error occurred while approving the order");
		}
	});

}

function approveOrder(sendNotif) {
  $('#orderApproveLoader').show();
	var orderId = $('#txtOrderId').val();
	$("#" + orderId).html('Approving Order ');
	$('#' + orderId)
			.append(
					'<i class="fa fa-refresh fa-spin" id="loading" hidden="hidden"></i>');
	callApproverApi(orderId, sendNotif);

}

function refundOrder() {

	$("#btn" + orderId).html('Refunding Amount ');
	$('#btn' + orderId).append('<i class="fa fa-refresh fa-spin" id="loading" hidden="hidden"></i>');

	var refundRequest = {};
	refundRequest["orderId"] = orderId;
	refundRequest["refundReason"] = $('#txtReason').val();
	refundRequest["amount"] = $('#txtRefundAmount').val();
	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : apiBasePath + "/admin_sales/orders/refund",
		"method" : "PATCH",
		"processData" : false,
		"contentType" : "application/json",
		"data" : JSON.stringify(refundRequest),
		"mimeType" : "multipart/form-data",
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },
		success : function(response) {
			$('#loading').remove();
			$("#btn" + orderId).html('Refund');
//			location.reload();
            $('.refund-button[id="btn'+orderId+'"]').attr("disabled",true);
            $('.partial-refund-button[id="btn'+orderId+'"]').attr("disabled",true);

		},
		error : function(response) {
			$('#loading').remove();
			alert("Error occurred while refunding the order");
		}
	});

}

function partialRefundOrder() {

	$("#btn-partial" + orderId).html('Refunding Partial Amount ');
	$('#btn-partial' + orderId)
			.append(
					'<i class="fa fa-refresh fa-spin" id="loading" hidden="hidden"></i>');

	var refundRequest = {};
	refundRequest["orderId"] = orderId;
	refundRequest["refundReason"] = $('#txtPartialReason').val();
	refundRequest["amount"] = $('#txtPartialRefundAmount').val();
	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : apiBasePath + "/admin_sales/orders/partialrefund",
		"method" : "PATCH",
		"processData" : false,
		"contentType" : "application/json",
		"data" : JSON.stringify(refundRequest),
		"mimeType" : "multipart/form-data",
		success : function() {
			$('#loading').remove();
			$("#btn" + orderId).html('Refund');
//			location.reload();
          $('.partial-refund-button[id="btn'+orderId+'"]').attr("disabled",true);
          $('.refund-button[id="btn'+orderId+'"]').attr("disabled",true);
		},
		error : function() {
			$('#loading').remove();
			alert("Error occurred while refunding the order partially");
		}
	});

}

function confirmPayment(orderId) {
	$("#cnf" + orderId).html('Confirming Payment ');
	$('#cnf' + orderId)
			.append(
					'<i class="fa fa-refresh fa-spin" id="loading" hidden="hidden"></i>');

	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : apiBasePath + "/admin_sales/payment-confirm/" + orderId,
		"method" : "PATCH",
		"processData" : false,
		"mimeType" : "multipart/form-data",
		// "headers" : {
		// "authorization" : "Bearer "
		// + window.localStorage.getItem('access_token'),
			//
		// },
		success : function(response) {
			$('#loading').remove();
			$("#cnf" + orderId).html('Confirm Payment');
	//		location.reload();
	        $('.approve-button[id="cnf'+orderId+'"]').attr("disabled",true);
      $('.approve-button[id="'+orderId+'"]').attr("disabled",false);
      $('#status'+orderId).text('PROCESSED');

    },
		error : function(response) {
			$('#loading').remove();
			alert("Error occurred while confirming payment");
		}
	});

}

function showReasonModal(id) {
	orderId = id;
	$('#refundReasonModal').modal();
}

function showPartialReasonModal(id) {
	orderId = id;
	$('#partialRefundReasonModal').modal();
}

function showProducts() {
	$('#prodTypeDD').show();

}

function showUnitsPanel() {
	$('#unitsModal').modal();

}

function showCoursesPanel() {
	$('#coursesModal').modal();
}

function getUserEmails() {
	var searchString = $('#userEmailSearch').val();

	$
			.ajax({
				"async" : false,
				"crossDomain" : true,
				"url" : apiBasePath + "/search-user",
				"method" : "POST",
				"processData" : false,
				"data" : searchString,

				success : function(response) {

					var htmlToShow = '<ul id="userList">';
					var oneRow = '<li> <a onclick="selectUser(\'#userId1#\');" >#userEmail#</a><i id="userId">(#userId1#)</i></li>';

					$.each(response, function(index, element) {
						htmlToShow += oneRow;
						htmlToShow = htmlToShow
								.replace(/#userId1#/g, element[1]);
						htmlToShow = htmlToShow.replace("#userEmail#",
								element[0]);
					})
					htmlToShow += '</ul>';
					$('#userList').html(htmlToShow).show();

				},
				error : function(response) {
					console.log(response);

				}
			});
}

/*
 * $('#dropUsers').change(function(){ var userId =
 * $("form[name='add-new-order']").find("#dropUsers :selected") .val(); ;
 * showAddresses(userId); })
 */

/*
 * function selectUserEmail() { var emailId = $('#userEmail').val(); }
 */
function selectUser(userId) {
	/*
	 * var userId = $("form[name='add-new-order']").find("#dropUsers :selected")
	 * .val(); ;
	 */
	selectedUserId = userId;
	showAddresses(selectedUserId);
}

function cancelAddress() {
	/*
	 * var userId = $("form[name='add-new-order']").find("#dropUsers :selected")
	 * .val();
	 */
	showAddresses(selectedUserId);
}

function showAddresses(userId) {
	$
			.ajax({
				"async" : false,
				"crossDomain" : true,
				"url" : "/admin_sales/users/" + userId + "/addresses",
				"method" : "GET",
				"processData" : false,
				"dataType" : "json",
				"contentType" : "application/json",
				"mimeType" : "multipart/form-data",
				// "headers": {
				// "authorization": "Bearer "
				// + window.localStorage.getItem('access_token'),
				//
				// },
				success : function(response) {

					var htmlToShow = '';
					var oneRow = '<div class="radio" style="margin: 20px; width:calc(50% - 40px); float:left !important " for="#addressId#" ><input id="#addressId1#" type="radio" #isDefault# onchange="changeDefaultAddress('
							+ "#addressId2#"
							+ ","
							+ "#userId#"
							+ ');">#AddressDetails#</input></div>';
					var htmlEnd = '<div style="clear:both;"><button type="button" class="btn btn-primary" onclick="showNewAddressPanel();">Add New Address</button></div>';

					$
							.each(
									response,
									function(index, element) {
										htmlToShow += oneRow;
										htmlToShow = htmlToShow.replace(
												"#addressId#",
												element.addressId);
										htmlToShow = htmlToShow.replace(
												"#addressId1#",
												element.addressId);
										htmlToShow = htmlToShow.replace(
												"#addressId2#",
												element.addressId);
										htmlToShow = htmlToShow.replace(
												"#userId#", userId);
										var address = '';
										address += element.firstName;
										address += '&nbsp;';
										address += element.lastname;
										address += '&nbsp;';
										address += '&nbsp;';
										address += '&nbsp;';

										var trashButton = '<a id="#addressId#" onclick="deleteAddress('
												+ "#addressId1#"
												+ ')"><i class="fa fa-close"></i></a>';
										trashButton = trashButton.replace(
												"#addressId#",
												element.addressId);
										trashButton = trashButton.replace(
												"#addressId1#",
												element.addressId);

										address += trashButton;
										address += '<br/>';
										address += element.mobileNumber;
										address += '<br/>';
										address += element.addressText;
										address += '<br/>';
										address += element.city;
										address += ',';
										address += element.state;
										address += '<br/>';
										address += element.pincode;
										htmlToShow = htmlToShow.replace(
												"#AddressDetails#", address);
										if (element.defaultAddress == true) {
											htmlToShow = htmlToShow.replace(
													"#isDefault#",
													'checked="checked"');
										} else {
											htmlToShow = htmlToShow.replace(
													"#isDefault#", '');
										}

									})
					htmlToShow += htmlEnd;
					// alert(htmlToShow);
					// $('#existingAddressInfo').remove();
					$('#existingAddressInfo').html(htmlToShow).show();
					$('#newAddressInfo').hide();
					// alert("Addresses fetched succesfully");

				},
				error : function(response) {
					$('#loading').remove();
					alert("Error occurred while saving address");
				}
			});
}

function deleteAddress(addressId) {
	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : "/admin_sales/addresses/" + addressId,
		"method" : "DELETE",
		"processData" : false,
		"dataType" : "json",
		"contentType" : "application/json",
		"mimeType" : "multipart/form-data",
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },
		success : function(response) {
			// var userId = $("form[name='add-new-order']").find(
			// 		"#dropUsers :selected").val();
			var userId= selectedUserId;
			if (response == true) {
				alert("Address deleted succesfully");
				userId = showAddresses(userId);
			} else {
				alert("Error occurred while deleting address");
			}

		},
		error : function(response) {

			alert("Error occurred while deleting address");
		}
	});
}

function changeDefaultAddress(addressId, userId) {

	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : "/admin_sales/users/" + userId + "/addresses/" + addressId
				+ "/makeDefault",
		"method" : "PATCH",
		"processData" : false,
		"dataType" : "json",
		"contentType" : "application/json",
		"mimeType" : "multipart/form-data",
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },
		success : function(response) {

			showAddresses(selectedUserId);
		},
		error : function(response) {

			alert("Error occurred while changing default address");
		}
	});
}

function showNewAddressPanel() {
	resetAddressPanel();
	$('#existingAddressInfo').html('');
	$('#existingAddressInfo').hide();
	$('#newAddressInfo').show();
}

function resetAddressPanel() {
	$('#newAddressInfo').find('input:text').val('');
	// $('#newAddressInfo').find('input:number').val(0);
}

function addToCart(prodId) {

	var htmlBeg = '	<table id="itemsTbl" class="table table-striped table-bordered" cellspacing="0" width="100%"><thead><tr><th>Item</th><th>Price</th><th>Actions</th>	</tr></thead><tbody>'
	var oneRow = '<tr><td>#itemName#</td><td>#itemPrice#</td><td><button type="button" onclick="deleteItemfromCart('
			+ "#prodId#" + ');">Delete</button></td></tr>'
	var htmlEnd = '</tbody><tfoot><tr><td></td><td></td><td><span>Total: #cartTotal#</span></td><td><span id="totalPayable"></span></td></tr>	</tfoot></table>'
	var htmlToShow = '';

	var updateCartRequest = {};
	updateCartRequest["userId"] = selectedUserId;
	updateCartRequest["productId"] = prodId;
	updateCartRequest["quantity"] = 1;
	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : apiBasePath + "/admin_sales/ecommerce/updatecart",
		"method" : "POST",
		"processData" : false,
		"dataType" : "json",
		"data" : JSON.stringify(updateCartRequest),
		"contentType" : "application/json",
		"mimeType" : "multipart/form-data",
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },
		success : function(response) {
			htmlToShow += htmlBeg;
			$.each(response.items, function(index, element) {
				htmlToShow += oneRow;
				htmlToShow = htmlToShow.replace("#itemName#", element.title);
				htmlToShow = htmlToShow.replace("#itemPrice#", element.price);
				htmlToShow = htmlToShow.replace("#prodId#", element.productId);
			});
			console.log(response);
			htmlToShow += htmlEnd;
			htmlToShow = htmlToShow
					.replace("#cartTotal#", response.totalAmount);

			$('#loading').remove();
			$('#itemsDiv').html(htmlToShow).show();
			$('#btnSubmitOrder').show();
			$('#OrderReasonText').show();

		},
		error : function(response) {
			$('#loading').remove();
			alert("Error occurred while adding item to the order");
		}
	});

	/*
	 * var t = $('#itemsTbl').DataTable(); t.row.add([ 'a', 'b' ]).draw(false);
	 */
	/*$('#unitsModal').modal('hide');*/
	$('#prodTypeDD').hide();







}

function deleteItemfromCart(prodId) {
	var htmlBeg = '	<table id="itemsTbl" class="table table-striped table-bordered" cellspacing="0" width="100%"><thead><tr><th>Item</th><th>Price</th>	<th>Actions</th></tr></thead><tbody>'
	var oneRow = '<tr><td>#itemName#</td><td>#itemPrice#</td><td><button type="button" onclick="deleteItemfromCart('
			+ '#prodId#' + ');">Delete</button></td></tr>'
	var htmlEnd = '</tbody><tfoot><tr><td></td><td><span>Total: #cartTotal#</span><span></span></td></tr>	</tfoot></table>'
	var htmlToShow = '';

	var updateCartRequest = {};
	updateCartRequest["userId"] = selectedUserId;
	updateCartRequest["productId"] = prodId;
	updateCartRequest["quantity"] = 0;

	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : apiBasePath + "/admin_sales/ecommerce/updatecart",
		"method" : "POST",
		"processData" : false,
		"dataType" : "json",
		"data" : JSON.stringify(updateCartRequest),
		"contentType" : "application/json",
		"mimeType" : "multipart/form-data",
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },
		success : function(response) {
			htmlToShow += htmlBeg;
			$.each(response.items, function(index, element) {
				htmlToShow += oneRow;
				htmlToShow = htmlToShow.replace("#itemName#", element.title);
				htmlToShow = htmlToShow.replace("#itemPrice#", element.price);
				htmlToShow = htmlToShow.replace("#prodId#", element.productId);
			})
			console.log(response);
			htmlToShow += htmlEnd;
			htmlToShow = htmlToShow
					.replace("#cartTotal#", response.totalAmount);

			$('#loading').remove();
			$('#itemsDiv').html(htmlToShow).show();

		},
		error : function(response) {
			$('#loading').remove();
			alert("Error occurred while adding item to the order");
		}
	});

	/*
	 * var t = $('#itemsTbl').DataTable(); t.row.add([ 'a', 'b' ]).draw(false);
	 */
	$('#unitsModal').modal('hide');
	$('#prodTypeDD').hide();
}

$("#dropCoupons").change(
		function(event) {
			validatedCoupon = $(this).val();
			$.ajax({
				url : "/admin_sales/users/" + selectedUserId + '/couponCode/'
						+ validatedCoupon,
				type : "get",
				contentType : "application/json",
				success : function(response) {
					if (response.valid != false) {

						$('#totalPayable').text(
								'Payable: ' + response.payAmount);
						alert("Coupon applied successful")
					} else {
						$('#totalPayable').text(
								'Payable: ' + response.payAmount);
						alert(response.messages);
					}
				},

				error : function(response) {
					$('#totalPayable').text('Payable: ' + response.payAmount);
					alert(response.messages);
				}
			});
		});

function submitOrder() {

	$('#btnSubmitOrder').html('Submitting Order');
	$('#btnSubmitOrder')
			.append(
					'<i class="fa fa-refresh fa-spin" id="loading" hidden="hidden"></i>');

	if (document.getElementById('emi').checked) {
		$('#emi').attr('checked', true);
		$('input[name="emi"]').val(true);
	} else {
		$('#emi').attr('checked', false);
		$('input[name="emi"]').val(false);
	}

	var orderRequest = {};
	orderRequest["userId"] = selectedUserId;
	var order = {};
	var userId = selectedUserId;
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : "/admin_sales/users/" + selectedUserId + "/address",
		"method" : "GET",
		"contentType" : "application/json",
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },
		success : function(response) {
			orderRequest["addressId"] = response.addressId;
			orderRequest["couponCode"] = $("form[name='add-new-order']").find(
					"#dropCoupons :selected").val();
			orderRequest["paymentModeType"] = $("form[name='add-new-order']")
					.find("#dropPayMode :selected").val();
			orderRequest["emi"] = $('#emi').val();
			orderRequest["installmentNumber"] = $('#txtInstallmentNumber')
					.val();
			orderRequest["actualAmount"] = $('#txtActualAmount').val();
			orderRequest["reason"] = $('#txtReason').val();
			$.ajax({
				"async" : false,
				"crossDomain" : true,
				"url" : "/admin_sales/ecommerce/createorder",
				"method" : "POST",
				"processData" : false,
				"dataType" : "json",
				"data" : JSON.stringify(orderRequest),
				"contentType" : "application/json",
				"mimeType" : "multipart/form-data",
				// "headers": {
				// "authorization": "Bearer "
				// + window.localStorage.getItem('access_token'),
				//
				// },
				success : function(response) {
					// alert("Order Created succesfully");

					var customOrderRequest = {};
					customOrderRequest["order"] = response;
					customOrderRequest["userId"] = selectedUserId;

					$.ajax({
						"async" : false,
						"crossDomain" : true,
						"url" : "/admin_sales/ecommerce/submitorder",
						"method" : "POST",
						"processData" : false,
						"dataType" : "json",
						"data" : JSON.stringify(customOrderRequest),
						"contentType" : "application/json",
						"mimeType" : "multipart/form-data",
						// "headers": {
						// "authorization": "Bearer "
						// + window.localStorage
						// .getItem('access_token'),
						//
						// },
						success : function(response) {
							window.alert("Order Submitted Succesfully");
							// $('#loading').remove();
							location.reload();
						},
						error : function(response) {
							$('#loading').remove();
							alert("Error occurred while submitting order");
						}
					});

				},
				error : function(response) {
					$('#loading').remove();
					alert("Error occurred while saving address");
				}
			});

			// alert("Address saved succesfully");

		},
		error : function(response) {
			$('#loading').remove();
			alert("Error occurred while saving address");
		}
	});
}

function addInstallment() {
	$('#divEMI').show();
}

function showSummary(orderId) {

	$
			.ajax({
				"async" : false,
				"crossDomain" : true,
				"url" : apiBasePath + "/admin_sales/orders/" + orderId,
				"method" : "GET",
				"processData" : false,
				"dataType" : "json",
				"contentType" : "application/json",

				success : function(response) {

					if (response.customer != null) {
						$('#lblName').text(
								'Name: ' + response.customer.fullName);
						$('#lblEmail').text(
								'Email: ' + response.customer.emailId);
					}

					$('#lblEMI').text('Is EMI: ' + response.emi);

					if (response.orderType == 'MANUAL') {
						$('#lblOrderType').text('Order Type: Manual');

						$('#lblOrderReason').text(
								'Order Reason: ' + response.reason);

					} else {
						$('#lblOrderType').text('Order Type: Regular');
						$('#lblOrderReason').text('');

					}

					$('#lblInstallmentNo')
							.text(
									'Installment Number: '
											+ response.installmentNumber);

					if (response.address != null) {
						$('#lblMob')
								.text(
										'Contact No.: '
												+ response.address.mobileNumber);

						var address = '';
						address += response.address.addressText + ',';
						address += response.address.city;
						address += ', ';
						address += response.address.pincode;

						$('#lblAddress').text('Address: ' + address);
					} else {
						$('#lblMob').text('Contact No.: No Contact No.');
						$('#lblAddress').text('Address: No Address');
					}

					if (response.orderItems != null
							&& response.orderItems.length > 0) {
						$("#tblOrderSummary tbody").html('');
						$
								.each(
										response.orderItems,
										function(index, element) {

											tr = '<tr>\n'
													+ '                <td>#id#</td>\n'
													+ '                <td><a href="/course/#link#">#title#</a></td>\n'
													+ '                <td>#type#</td>\n'
													+ '                <td>#price#</td>\n'
													+ '\n' + '\n'
													+ '              </tr>'

											tr = tr
													.replace(
															"#link#",
															element.product.productSlug);
											tr = tr
													.replace(
															"#title#",
															element.product.productTitle);

											if (element.product.type == 'COURSE') {
												tr = tr.replace("#type#",
														'Course Batch');
												tr = tr
														.replace(
																"#id#",
																'Batch Id '
																		+ element.product.commodityId);
											} else {
												tr = tr.replace("#id#",
														element.product.type);
												tr = tr
														.replace(
																"#id#",
																element.product.type
																		+ ' '
																		+ element.product.commodityId);
											}
											tr = tr.replace("#price#",
													element.product.regularPrice);

											$("#tblOrderSummary tbody").append(
													tr);
										});

					}

					$('#lblAmt').text('Amount Paid: ' + response.payableAmount);
					$('#lblNeoCash').text('NeoCash Redeemed: '+ response.neoCashRedeemed);
					$('#lblPayMode').text(
							'Payment Mode: ' + response.paymentMode
									+ ' (TransactionID: '
									+ response.transactionId + ')');

					var date = new Date(response.paymentDate);
					var year = date.getFullYear();
					var month = date.getMonth() + 1;
					var day = date.getDate();
					var hours = date.getHours();
					var minutes = date.getMinutes();
					var seconds = date.getSeconds();
					var paymentDate = day + '/' + month + '/' + year + '    '
							+ hours + ':' + minutes + ':' + seconds;

					$('#lblPayDate').text('Payment Date: ' + paymentDate);

					if (response.coupon != null && response.coupon.code != null) {

						$('#lblCoupon').text('Coupon: ' + response.coupon.code);

					} else {
						$('#lblCoupon').text('Coupon: ' + 'No Coupon used');

					}
					$('#modalSummary').modal("show");

				},
				error : function(response) {

					alert("Error occurred while fetching data");
				}
			});

}

function showNotes(orderId) {
	$
			.ajax({
				"async" : false,
				"crossDomain" : true,
				"url" : apiBasePath + "/admin_sales/orders/" + orderId,
				"method" : "GET",
				"processData" : false,
				"dataType" : "json",
				"contentType" : "application/json",
				"mimeType" : "multipart/form-data",
				// "headers": {
				// "authorization": "Bearer "
				// + window.localStorage.getItem('access_token'),
				//
				// },
				success : function(response) {
					if (response.orderNotes != null) {
						var htmlToShow = '';
						var htmlBeg = '<div id="orderNotesModal" class="modal fade" role="dialog"><div class="modal-dialog">Modal content<div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal">&times;</button><h4><span></span> Order Notes</h4></div><div class="modal-body" style="padding: 40px 50px;"><form role="form"><div class="form-group"><ul class="list-group">';
						var oneRow = '<li class="list-group-item d-flex justify-content-between align-items-center"> #notesContent#<span class="badge badge-primary badge-pill">by&nbsp;#noteBy#&nbsp;at&nbsp;#noteDate#</span></li>';
						var htmlEnd = '</ul></div></form></div></div></div></div> ';

						htmlToShow += htmlBeg;

						$.each(response.orderNotes,
								function(index, element) {
									htmlToShow += oneRow;
									htmlToShow = htmlToShow.replace(
											"#notesContent#",
											element.noteContent);
									htmlToShow = htmlToShow.replace("#noteBy#",
											element.addedBy);
									var a = new Date(element.noteDate);
									var months = [ 'Jan', 'Feb', 'Mar', 'Apr',
											'May', 'Jun', 'Jul', 'Aug', 'Sep',
											'Oct', 'Nov', 'Dec' ];
									var year = a.getFullYear();
									var month = months[a.getMonth()];
									var date = a.getDate();
									var hour = a.getHours();
									var min = a.getMinutes();
									var sec = a.getSeconds();
									var time = date + ' ' + month + ' ' + year
											+ ' ' + hour + ':' + min + ':'
											+ sec;
									htmlToShow = htmlToShow.replace(
											"#noteDate#", time);

								});

						htmlToShow += htmlEnd;
						htmlToShow = htmlToShow.replace("#orderId#", orderId);
						htmlToShow = htmlToShow.replace("#orderId2#", orderId);

						$('#orderNotesDiv').html(htmlToShow);
						$('#orderNotesModal').modal("show");

					}
				},
				error : function(response) {

					alert("Error occurred while fetching data");
				}
			});
}

var selectedOrderId;

function addNote(orderId) {
	// $('#orderNotesModal').modal("hide");
	$('#addNoteModal').modal();

	orderIdToUpdate = orderId;
	}

$('#addNoteModal').on('hidden', function() {
	showNotes(selectedOrderId);
})

function exportData() {
	var entityName = $('#divEntities input:radio:checked').val();

	var request = {};

	var dataFieldList = getDataFields(entityName);

	request["entityName"] = entityName;
	request["dataFields"] = dataFieldList;
	request["endDate"] = $('#endDate').val();
	request["startDate"] = $('#startDate').val();

	$.ajax({
		"async" : false,
		"crossDomain" : true,
		"url" : apiBasePath + "/admin_sales/export",
		"method" : "POST",
		"processData" : false,
		"contentType" : "application/json",
		"data" : JSON.stringify(request),
		"dataType" : 'TEXT',
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },
		success : function(response) {
			if (response != null) {
				window.location.href = response;
			} else {
				alert("Error occurred while exporting data");
			}

		},
		error : function(response) {

			alert("Error occurred while exporting data");
		}
	});

}

function getDataFields(entityName) {
	var fieldList = [];
	switch (entityName) {
	case 'Product':
		var dataField = {};
		dataField["fieldName"] = "type";
		dataField["fieldValue"] = $('#dropProdType').val();

		fieldList.push(dataField);
		return fieldList;

	case 'Order':

	}
}

function generateReport(timeperiod) {
	// alert(timeperiod);
	var request = {};
	var today = new Date();

	switch (timeperiod) {
	case "LastMonth":

		start = new Date(today.getFullYear(), today.getMonth() - 1, 1);
		end = new Date(today.getFullYear(), today.getMonth(), 0);

		request["endDate"] = end;
		request["startDate"] = start;
		break;

	case "ThisMonth":
		request["endDate"] = today;

		var start = new Date(today.getFullYear(), today.getMonth(), 1);
		request["startDate"] = start;
		break;

	case "ThisWeek":
		request["endDate"] = today;
		start = new Date();
		start.setDate(start.getDate() - 7);
		request["startDate"] = start;
		break;
	case "Custom":
		request["endDate"] = $('#endDate').val();
		request["startDate"] = $('#startDate').val();
		break;
	case "Year":
		location.reload();
		break;
	}

	if (timeperiod != "Year") {

		$
				.ajax({
					"async" : false,
					"crossDomain" : true,
					"url" : apiBasePath + "/admin_sales/report",
					"method" : "POST",
					"processData" : false,
					"contentType" : "application/json",
					"data" : JSON.stringify(request),
					"dataType" : "json",
					// "headers": {
					// "authorization": "Bearer "
					// + window.localStorage.getItem('access_token'),
					//
					// },
					success : function(response) {
						// alert();

						// htmlToShow = '<ul class="nav nav-pills
						// nav-stacked"><li><a href="#"><span><b>Gross
						// Sales:</b>&nbsp;&nbsp;<i class="fa fa-inr"></i>
						// </span>#grossSales#</a></li><li><a
						// href="#"><span><b>Gross Average Daily
						// Sales:</b>&nbsp;&nbsp; <i class="fa fa-inr"></i>
						// #grossAvgDailySales#</span></a></li><li><a
						// href="#"><span><b>Gross Average Daily Sales(Manual
						// Orders):</b>&nbsp;&nbsp; <i class="fa fa-inr"></i>
						// #grossAvgDailySalesManual#</span></a></li><li><a
						// href="#"><span><b>Gross Average Daily Sales(Regular
						// Orders):</b>&nbsp;&nbsp; <i class="fa fa-inr"></i>
						// #grossAvgDailySalesRegular#</span></a></li><li><a
						// href="#"><span><b>Net Average Daily Sales:
						// </b>&nbsp;&nbsp; <i class="fa
						// fa-inr"></i></span>#netAvgDailySales#</a></li><li><a
						// href="#"><span><b>Net Average Daily Sales(Manual
						// Orders): </b>&nbsp;&nbsp; <i class="fa
						// fa-inr"></i></span>#netAvgDailySalesManual#</a></li><li><a
						// href="#"><span><b>Net Average Daily Sales(Regular
						// Orders): </b>&nbsp;&nbsp; <i class="fa
						// fa-inr"></i></span>#netAvgDailySalesRegular#</a></li><li><a
						// href="#"><span><b>No.Of Orders Placed:&nbsp;&nbsp;
						// </b></span>#noOfOrders#</a></li><li><a
						// href="#"><span><b>No. Of Items Purchased:&nbsp;&nbsp;
						// </b></span>#noOfItems#</a></li><li><a
						// href="#"><span><b>Total Coupon Discount:</b>
						// &nbsp;&nbsp; <i class="fa
						// fa-inr"></i></span>#couponDiscount#</a></li><li><a
						// href="#"><span><b>No. Of Refunds:&nbsp;&nbsp;
						// </b></span>#noOfRefunds#</a></li><li><a
						// href="#"><span><b>Total Refund Value:
						// </b>&nbsp;&nbsp;<i class="fa fa-inr"></i>
						// </span>#refundValue#</a></li><li><a
						// href="#"><span><b>Total Shipping Charged:
						// </b>&nbsp;&nbsp; <i class="fa
						// fa-inr"></i></span>#shippingCharges#</a></li></ul>';

						htmlToShow = '<ul class="nav nav-pills nav-stacked">'
								+ '<li><a href="#"><span><b>Gross Sales:</b>&nbsp;&nbsp;<i class="fa fa-inr"></i> </span>#grossSales#</a></li>'
								+ '<li><a href="#"><span><b>Gross Sales(Manual Orders):</b>&nbsp;&nbsp;<i class="fa fa-inr"></i> </span>#grossSalesManual#</a></li>'
								+ '<li><a href="#"><span><b>Gross Sales(Regular Orders):</b>&nbsp;&nbsp;<i class="fa fa-inr"></i> </span>#grossSalesRegular#</a></li>'

								+ '<li><a href="#"><span><b>Net Sales:</b>&nbsp;&nbsp;<i class="fa fa-inr"></i> </span>#netSales#</a></li>'
								+ '<li><a href="#"><span><b>Net Sales(Manual Orders):</b>&nbsp;&nbsp;<i class="fa fa-inr"></i> </span>#netSalesManual#</a></li>'
								+ '<li><a href="#"><span><b>Net Sales(Regular Orders):</b>&nbsp;&nbsp;<i class="fa fa-inr"></i> </span>#netSalesRegular#</a></li>'

								+ '<li><a href="#"><span><b>Gross Average Daily Sales:</b>&nbsp;&nbsp; <i class="fa fa-inr"></i> #grossAvgDailySales#</span></a></li>'
								+ '<li><a href="#"><span><b>Gross Average Daily Sales(Manual Orders):</b>&nbsp;&nbsp; <i class="fa fa-inr"></i> #grossAvgDailySalesManual#</span></a></li>'
								+ '<li><a href="#"><span><b>Gross Average Daily Sales(Regular Orders):</b>&nbsp;&nbsp; <i class="fa fa-inr"></i> #grossAvgDailySalesRegular#</span></a></li>'

								+ '<li><a href="#"><span><b>Net Average Daily Sales: </b>&nbsp;&nbsp; <i class="fa fa-inr"></i></span>#netAvgDailySales#</a></li>'
								+ '<li><a href="#"><span><b>Net Average Daily Sales(Manual Orders): </b>&nbsp;&nbsp; <i class="fa fa-inr"></i></span>#netAvgDailySalesManual#</a></li>'
								+ '<li><a href="#"><span><b>Net Average Daily Sales(Regular Orders): </b>&nbsp;&nbsp; <i class="fa fa-inr"></i></span>#netAvgDailySalesRegular#</a></li>'

								+ '<li><a href="#"><span><b>No.Of Paid Orders Placed:&nbsp;&nbsp; </b></span>#noOfPaidOrders#</a></li>'
								+ '<li><a href="#"><span><b>No. Of Paid Items Purchased:&nbsp;&nbsp; </b></span>#noOfPaidItems#</a></li>'

								+ '<li><a href="#"><span><b>No.Of Free Orders Placed:&nbsp;&nbsp; </b></span>#noOfFreeOrders#</a></li>'
								+ '<li><a href="#"><span><b>No. Of Items in Free Orders:&nbsp;&nbsp; </b></span>#noOfFreeItems#</a></li>'

								+ '<li><a href="#"><span><b>No.Of Manual Orders:&nbsp;&nbsp; </b></span>#noOfManualOrders#</a></li>'
								+ '<li><a href="#"><span><b>No. Of Items Purchased:&nbsp;&nbsp; </b></span>#noOfManualItems#</a></li>'

								+ '<li><a href="#"><span><b>Total No.Of Orders Placed:&nbsp;&nbsp; </b></span>#noOfOrders#</a></li>'
								+ '<li><a href="#"><span><b>Total No. Of Items Purchased:&nbsp;&nbsp; </b></span>#noOfItems#</a></li>'

								+ '<li><a href="#"><span><b>Total Coupon Discount(Manual Orders):</b> &nbsp;&nbsp; <i class="fa fa-inr"></i></span>#couponDiscountManual#</a></li>'
								+ '<li><a href="#"><span><b>Total Coupon Discount(Regular Orders):</b> &nbsp;&nbsp; <i class="fa fa-inr"></i></span>#couponDiscountRegular#</a></li>'
								+ '<li><a href="#"><span><b>Total Coupon Discount:</b> &nbsp;&nbsp; <i class="fa fa-inr"></i></span>#couponDiscount#</a></li>'

								+ '<li><a href="#"><span><b>No. Of Refunds(Full):&nbsp;&nbsp; </b></span>#noOfRefunds#</a></li>'
								+ '<li><a href="#"><span><b>Total Refund(Full) Value: </b>&nbsp;&nbsp;<i class="fa fa-inr"></i> </span>#refundValue#</a></li>'
								+ '<li><a href="#"><span><b>No. Of Refunds(Partial): </b>&nbsp;&nbsp; <i class="fa fa-inr"></i></span>#noOfPartialRefunds#</a></li>'
								+ '<li><a href="#"><span><b>Total Refund(Partial) Value: </b>&nbsp;&nbsp; <i class="fa fa-inr"></i></span>#partialRefundValue#</a></li>'

								+ '</ul>';

						htmlToShow = htmlToShow.replace("#grossSales#",
								response.grossSales.toFixed(0));
						htmlToShow = htmlToShow.replace("#grossSalesManual#",
								response.grossSalesManual.toFixed(0));
						htmlToShow = htmlToShow.replace("#grossSalesRegular#",
								response.grossSalesRegular.toFixed(0));

						htmlToShow = htmlToShow.replace("#netSales#",
								response.netSales.toFixed(0));
						htmlToShow = htmlToShow.replace("#netSalesManual#",
								response.netSalesManual.toFixed(0));
						htmlToShow = htmlToShow.replace("#netSalesRegular#",
								response.netSalesRegular.toFixed(0));

						htmlToShow = htmlToShow.replace(
								"#grossAvgDailySalesManual#",
								response.grossAvgDailySalesManual.toFixed(0));
						htmlToShow = htmlToShow.replace(
								"#grossAvgDailySalesRegular#",
								response.grossAvgDailySalesRegular.toFixed(0));

						htmlToShow = htmlToShow.replace(
								"#netAvgDailySalesManual#",
								response.netAvgDailySalesManual.toFixed(0));
						htmlToShow = htmlToShow.replace(
								"#netAvgDailySalesRegular#",
								response.netAvgDailySalesRegular.toFixed(0));

						htmlToShow = htmlToShow.replace("#noOfPaidOrders#",
								response.noOfPaidOrders.toFixed(0));
						htmlToShow = htmlToShow.replace("#noOfPaidItems#",
								response.noOfPaidItems.toFixed(0));
						htmlToShow = htmlToShow.replace("#noOfFreeOrders#",
								response.noOfFreeOrders.toFixed(0));
						htmlToShow = htmlToShow.replace("#noOfFreeItems#",
								response.noOfFreeItems.toFixed(0));
						htmlToShow = htmlToShow.replace("#noOfManualOrders#",
								response.noOfManualOrders.toFixed(0));
						htmlToShow = htmlToShow.replace("#noOfManualItems#",
								response.noOfManualItems.toFixed(0));

						htmlToShow = htmlToShow.replace(
								"#couponDiscountManual#",
								response.totalCouponDiscountManual);
						htmlToShow = htmlToShow.replace(
								"#couponDiscountRegular#",
								response.totalCouponDiscountRegular);
						htmlToShow = htmlToShow.replace("#couponDiscount#",
								response.totalCouponDiscount);

						htmlToShow = htmlToShow.replace("#grossAvgDailySales#",
								response.grossAvgDailySales.toFixed(0));
						htmlToShow = htmlToShow.replace("#netAvgDailySales#",
								response.netAvgDailySales.toFixed(0));
						htmlToShow = htmlToShow.replace("#noOfOrders#",
								response.noOfOrders);
						htmlToShow = htmlToShow.replace("#noOfItems#",
								response.noOfItems);
						htmlToShow = htmlToShow.replace("#noOfRefunds#",
								response.noOfRefundedOrders);
						htmlToShow = htmlToShow.replace("#refundValue#",
								response.refundValue);
						htmlToShow = htmlToShow.replace("#partialRefundValue#",
								response.partialRefundValue);
						htmlToShow = htmlToShow.replace("#noOfPartialRefunds#",
								response.noOfPartialRefunds);


						$('#dataItemsOthers').html(htmlToShow).show();
						$('#dataItemsYear').hide();
					},
					error : function(response) {

						alert("Error occurred while getting report");
					}
				});
	}

}

function onClickAddAmount() {
	$('#divAmount').show();

}

function showInstallmentNumber() {
	if (document.getElementById('emi').checked) {
		$('#divInstallment').show();
		$('#lableAmountDiv').show();
	} else {
		$('#divInstallment').hide();
		$('#lableAmountDiv').hide();
	}
}
