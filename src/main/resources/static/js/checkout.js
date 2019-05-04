var cartId = "";
var validatedCoupon = "";
var totalPayable = 0;
var totalPayableAfterDiscount = 0;
var addressToBeChecked = '';
var razorpayKey = "";
var validatedNeoCashAmount=0;
$(document)
		.ready(
				function() {
					$('#checkoutLoader').hide();
					$('#accordion').css("visibility", "visible");
					$('#username').html(username);
					reviewCart();
					showAddress();
					$('.couponMoney').hide();
					$('.neoCash').hide();
					$('#invalidNCZest').hide();
					$('#invalidNCRazor').hide();
					$('#invalidNCNEFT').hide();

					cartId = getParameterByName('id');
					if (window.location.href.indexOf("checkout") != -1) {
						if (window.location.href.indexOf("coupon") != -1) {
							validatedCoupon = getParameterByName('coupon');
						} else {
							$('.isPromocodeApplied').hide();
						}
						$
								.ajax({
									url : apiBasePath
											+ "/user/validatecoupon?coupon="
											+ validatedCoupon,
									type : "get",
									contentType : "application/json",
									success : function(response) {
										if (response.totalAmount == '0'
												&& response.items.length > 0) {
											$('#createOrderBtn')
													.attr("onclick",
															"addToCart('addFreeCourse')");
										}
										if (response.valid != false) {
											$('#isPromocodeApplied').show();
											$('.progress-button').addClass(
													'success');

											$('#couponMoney').html(
													"-" + response.discount);
											$('.couponMoney').show();
											$('#amountPayable')
													.html(
															numberWithCommas(response.payAmount));
											$('.validate input').val(
													validatedCoupon);
											$('.validate input ~ label').hide();
											$('.validate').addClass("active");
											$('.finalAmount')
													.html(
															"Rs "
																	+ numberWithCommas(response.payAmount));
											totalPayableAfterDiscount = response.payAmount;
										} else {
											$('#isPromocodeApplied').hide();
											$('.couponMoney').hide();
											$('#enterCoupon').val("");
											$('.validate')
													.removeClass("active");
											$('.validate input ~ label').show();
											totalPayableAfterDiscount = response.payAmount;
											$('.finalAmount')
													.html(
															"Rs "
																	+ numberWithCommas(response.payAmount));
											$('#amountPayable')
													.html(
															numberWithCommas(response.payAmount));
										}
									},

									error : function(response) {
										$('#isPromocodeApplied').hide();
										$('.couponMoney').hide();
										$('#enterCoupon').val("");
										$('.validate').removeClass("active");
										$('.validate input ~ label').show();
									}
								});
						
						//For getting and populating NeoCash
						

												$.ajax({
							url : apiBasePath + "/user/neocash-details",
							type : "get",
							contentType : "application/json",
							dataType:"json",
							success : function(response) {

										$('#ipUseableCashRazor').val(
												response.useableNeoCash);
										$('#ipTotalCashRazor')
												.html(
														numberWithCommas(response.totalNeoCash));
										$('#ipUseableCashNEFT').val(
												response.useableNeoCash);
										$('#ipTotalCashNEFT')
												.html(
														numberWithCommas(response.totalNeoCash));
										$('#ipUseableCashZest').val(
												response.useableNeoCash);
										$('#ipTotalCashZest')
												.html(
														numberWithCommas(response.totalNeoCash));
							},

							error : function(response) {

							}
						});
						
						
					}
					getRazorPayKey();
				})
				



$('#btnRedeemNCRazor')
		.click(
				function() {

					request = {}
					request["neoCashAmount"] = $('#ipUseableCashRazor').val();
					$
							.ajax({
								url : apiBasePath
										+ "/user/validate-neocash-redemption",
								type : "put",
								contentType : "application/json",
								data : JSON.stringify(request),
								dataType : "json",
								success : function(response) {
									if (response.valid == true) {
										$('#neoCash').html(
												"-" + response.neoCashRedeemed);
										$('.neoCash').show();
										$('#amountPayable')
												.html(
														numberWithCommas(response.payableAmount));
										$('.finalAmount')
												.html(
														"Rs "
																+ numberWithCommas(response.payableAmount));
										validatedNeoCashAmount=response.neoCashRedeemed;
										totalPayableAfterDiscount= response.payableAmount;
										$('#invalidNCRazor').hide();
									}
									else
										{
										$('#invalidNCRazor').show();
										}
								},

								error : function(response) {

								}
							});
				})
				

				$('#btnRedeemNCNEFT')
		.click(
				function() {

					request = {}
					request["neoCashAmount"] = $('#ipUseableCashNEFT').val();
					$
							.ajax({
								url : apiBasePath
										+ "/user/validate-neocash-redemption",
								type : "put",
								contentType : "application/json",
								data : JSON.stringify(request),
								dataType : "json",
								success : function(response) {
									if (response.valid == true) {
										$('#neoCash').html(
												"-" + response.neoCashRedeemed);
										$('.neoCash').show();
										$('#amountPayable')
												.html(
														numberWithCommas(response.payableAmount));
										$('.finalAmount')
												.html(
														"Rs "
																+ numberWithCommas(response.payableAmount));
										validatedNeoCashAmount = response.neoCashRedeemed;
										totalPayableAfterDiscount = response.payableAmount;
										$('#invalidNCNEFT').hide();
									} else {
										$('#invalidNCNEFT').show();
									}
								},

								error : function(response) {

								}
							});
				})

$('#btnRedeemNCZest')
		.click(
				function() {

					request = {}
					request["neoCashAmount"] = $('#ipUseableCashZest').val();
					$
							.ajax({
								url : apiBasePath
										+ "/user/validate-neocash-redemption",
								type : "put",
								contentType : "application/json",
								data : JSON.stringify(request),
								dataType : "json",
								success : function(response) {
									if (response.valid == true) {
										$('#neoCash').html(
												"-" + response.neoCashRedeemed);
										$('.neoCash').show();
										$('#amountPayable')
												.html(
														numberWithCommas(response.payableAmount));
										$('.finalAmount')
												.html(
														"Rs "
																+ numberWithCommas(response.payableAmount));
										validatedNeoCashAmount = response.neoCashRedeemed;
										totalPayableAfterDiscount = response.payableAmount;
										$('#invalidNCZest').hide();
									} else {
										$('#invalidNCZest').show();
									}
								},

								error : function(response) {

								}
							});
				})

$('#accordion > h3').click(function() {
	$(this).find('#saved-address-accordian').hide();
	$(this).find('.is-submitted').hide();
})

function showAddress() {
	$
			.ajax({
				url : apiBasePath + "/address",
				type : "get",
				contentType : "application/json",
				success : function(response) {
					$('#addressLoader').hide();
					var htmlToShow = '';
					var addresses = '<div class="mdl-cell mdl-cell--6-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"><label class="mdl-radio mdl-js-radio mdl-js-ripple-effect" for="#addressId#"> <input type="radio" id="#addressId#" class="mdl-radio__button" name="options" value="#addressId#" #isDefault#> <span class="mdl-radio__label"></span> </label><span id="" class="address-radio">#fName# #lName#<br />#addressText#, #city#,<br />#state#, #pincode#, Phone: #phone# </span> &nbsp;&nbsp;</div>';
					if (response != null && response != '') {
						$
								.each(
										response,
										function(index, element) {
											i = index + 1;
											htmlToShow = htmlToShow += addresses;
											htmlToShow = htmlToShow.replace(
													/#addressId#/g,
													element.addressId);
											htmlToShow = htmlToShow.replace(
													"#fName#",
													element.firstName);
											htmlToShow = htmlToShow
													.replace("#lName#",
															element.lastname);
											htmlToShow = htmlToShow.replace(
													"#addressText#",
													element.addressText);
											htmlToShow = htmlToShow.replace(
													"#state#", element.state);
											htmlToShow = htmlToShow.replace(
													"#city#", element.city);
											htmlToShow = htmlToShow.replace(
													"#pincode#",
													element.pincode);
											htmlToShow = htmlToShow.replace(
													"#phone#",
													element.mobileNumber);
											if (addressToBeChecked == element.addressId) {
												htmlToShow = htmlToShow
														.replace("#isDefault#",
																"checked");
											} else if (index == '0'
													&& addressToBeChecked == '') {
												htmlToShow = htmlToShow
														.replace("#isDefault#",
																"checked");
											} else {
												htmlToShow = htmlToShow
														.replace("#isDefault#",
																"");
											}
										});
						$('#existingAddress').html(htmlToShow);
						$('#existingAddressSection').show();
					} else {
						$('#existingAddressSection').hide();
						$('#addressCheckout').show();
					}
				},

				error : function(response) {
					$('#addressLoader').hide();
				}
			});
}

var savedAddressAccordian = '';

function saveAddress() {
	savedAddressAccordian = $(
			"#existingAddressSection input[type='radio']:checked").parent()
			.siblings('.address-radio').html();
	$('#saved-address-accordian').html(
			$("#existingAddressSection input[type='radio']:checked").parent()
					.siblings('.address-radio').html()).css("display",
			"inline-flex");
	$("#accordion").accordion({
		active : 2
	});
	$('.is-submitted-2').show();
}

jQuery.validator.addMethod("lettersonly", function(value, element) {
	return this.optional(element) || /^[a-z\s]+$/i.test(value);
}, "Only alphabetical characters");
$(function() {
	$("form[name='address_checkout']").validate(
			{
				rules : {
					firstName : {
						required : true,
						lettersonly : true
					},
					lastname : {
						required : true,
						lettersonly : true
					},
					mobileNumber : {
						required : true,
						digits : true
					},
					city : {
						required : true
					},
					state : {
						required : true
					},
					pincode : {
						required : true,
						minlength : 6,
						digits : true
					},
					addressText : {
						required : true
					},
					alternateMobileNumber : {
						digits : true,
					}
				},
				messages : {
					firstName : {
						required : "Please enter your first name",
						lettersonly : "Please enter only letters"
					},
					lastname : {
						required : "Please enter your last name",
						lettersonly : "Please enter only letters"
					},
					mobileNumber : {
						required : "Please enter your mobile number",
						digits : "Please enter a valid mobile number"
					},
					city : {
						required : "Please enter your city"
					},
					state : {
						required : "Please enter your state"
					},
					pincode : {
						required : "Please enter your pincode",
						minlength : "Please enter your 6 digit pincode",
						digits : "Your pincode can only contain numbers"
					},
					addressText : {
						required : "Please enter your address",
					},
					alternateMobileNumber : {
						digits : "Please enter correct number",
					}
				},
				submitHandler : function(form, e) {
					$('#accordion').css("opacity", "0.7");
					$('#loader-login').show();
					$('#btn-login').hide();
					var input = {
						firstName : $("form[name='address_checkout']").find(
								"input[name='firstName']").val(),
						lastname : $("form[name='address_checkout']").find(
								"input[name='lastname']").val(),
						mobileNumber : $("form[name='address_checkout']").find(
								"input[name='mobileNumber']").val(),
						city : $("form[name='address_checkout']").find(
								"input[name='city']").val(),
						state : $("form[name='address_checkout']").find(
								"input[name='state']").val(),
						pincode : $("form[name='address_checkout']").find(
								"input[name='pincode']").val(),
						addressText : $("form[name='address_checkout']").find(
								"textarea[name=addressText]").val(),
						alternateMobileNumber : $(
								"form[name='address_checkout']").find(
								"input[name='alternateMobileNumber']").val()
					};

					$
							.ajax({
								url : apiBasePath + "/address",
								type : "post",
								data : JSON.stringify(input),
								dataType : "json",
								contentType : "application/json",
								processData : false,

								success : function(response) {

									$('#accordion').css("opacity", "1");
									$('.is-submitted-2').show();
									addressToBeChecked = response.addressId;
									showAddress();
									/*
									 * $('#existingAddress
									 * #'+response.addressId).parent().parent().siblings().find('input').attr("checked",
									 * false); $('#existingAddress
									 * #'+response.addressId).attr("checked",
									 * true);
									 */
									$("#accordion").accordion({
										active : 2
									});
									$('#saveAndContinueExisting').show();
									$('#addressLoader').hide();
									element = response;
									$('#saved-address-accordian').append(
											'<span class="address-radio" style="margin-top: 0px;">'
													+ element.firstName + ' '
													+ element.lastname
													+ '<br />'
													+ element.addressText
													+ ', ' + element.city
													+ ',<br />' + element.state
													+ ', ' + element.pincode
													+ ', Phone: '
													+ element.mobileNumber
													+ '</span>');
								},

								error : function(response) {

									$("#accordion").accordion({
										active : 2
									});
									$('#saveAndContinueExisting').show();
									$('#addressLoader').hide();
									$('#accordion').css("opacity", "1");
								}
							});
				}
			});
});

$('#addNew').click(function() {
	$('#existingAddressSection').hide();
	$('#addressCheckout').show();
})
$('#viewSaved').click(function() {
	$('#existingAddressSection').show();
	$('#addressCheckout').hide();
	showAddress();
})

function reviewCart() {

	$
			.ajax({
				url : apiBasePath + "/user/get-cart",
				type : "GET",
				"headers" : {
					"content-type" : "application/json",
				},
				success : function(response) {
					$('#enterCoupon').attr("data-cart-id", response.cardId);
					$('#cartLoader').hide();
					var htmlToShow = '';
					var cartItemsList = '<tr> <td class="mdl-data-table__cell--non-numeric"><img src="#imageUrl#" height="86px" width="128px"></td> <td style="text-align:left;">  <div class="cartCourseName">#courseName#</div><p><i class="fa fa-inr"></i>#totalPrice#</p></td> <td class="cart-table-price"><i class="fa fa-inr"></i>#salePrice#</td> <td class="review-order-quantity cart-table-price"><span>#quantity#</span></td> <td class="cart-table-price"><i class="fa fa-inr"></i>#totalPrice#</td> </tr>';
					var i = 0;
					if (response.items != null) {
						$.each(response.items, function(index, element) {
							i = index + 1;
							htmlToShow = htmlToShow += cartItemsList;
							htmlToShow = htmlToShow.replace(/#salePrice#/g,
									numberWithCommas(element.price));
							htmlToShow = htmlToShow.replace(/#totalPrice#/g,
									numberWithCommas(element.subTotal));
							htmlToShow = htmlToShow.replace("#courseName#",
									element.title);
							htmlToShow = htmlToShow.replace("#quantity#",
									element.quantity);
							htmlToShow = htmlToShow.replace(/#commodityId#/g,
									element.commmodityId);
							htmlToShow = htmlToShow.replace(/#productId#/g,
									element.productId);
							htmlToShow = htmlToShow.replace("#imageUrl#",
									element.imageUrl);
						});

						$('#subtotal').html(
								numberWithCommas(response.payAmount));
						if (validatedCoupon == null || validatedCoupon == '') {
							$('#amountPayable').html(
									numberWithCommas(response.payAmount));
						}
						totalPayable = response.totalAmount;
						$('#cartLoader').hide();
						$('#cartItems').html(htmlToShow);
						$('#cartTable').show();
					} else {
						$('#cartLoader').hide();
						$('#emptyCart').show();
						$('#cartTable').hide();
					}
				},

				error : function(response) {
					$('#enterCoupon').attr("data-cart-id", response.cardId);
				}
			});
}

function createOrder() {
	$("#accordion").accordion({
		active : 3
	});
	$('#createOrderBtn').show();
	$('#createOrderLoader').hide();
	$('.is-submitted-3').show();
}

function submitOrder(paymentModeType) {
	$
			.ajax({
				url : apiBasePath + "/user/submit_order",
				type : "POST",
				data : JSON
						.stringify({
							addressId : $(
									"#existingAddressSection input[type='radio']:checked")
									.val(),
							paymentModeType : paymentModeType,
							couponCode : validatedCoupon,
							neoCashRedeemed:validatedNeoCashAmount
						}),
				contentType : "application/json",
				success : function(response) {
					if (paymentModeType == 'razorpay'
							&& response.payAmount != 0) {
						payUsingRazorpay(response.orderId);
					} else if (paymentModeType == 'neft') {
						window.location = "/thank-you?orderId="
								+ response.orderId;
					}
					else if(paymentModeType == 'zestmoney'){
						payUsingZestmoneyEmi(response.orderId);
					}
					else {

						$
								.ajax({
									url : apiBasePath
											+ "/user/handle-zero-payment-razorpay-order/"
											+ response.orderId,
									type : "PATCH",
									success : function(response) {
										window.location = "/thank-you?orderId="
												+ response.orderId;
									}

								});
					}
				},

			});
}

$("#termsConditions").change(function(event) {

	if ($("#termsConditions").prop('checked') == false) {
		document.getElementById("rzp-button1").disabled = true;
		$("#rzp-button1").css({
			"color" : "#ffffff",
			"opacity" : "0.6"
		});
	} else {
		document.getElementById("rzp-button1").disabled = false;
		$("#rzp-button1").css({
			"color" : "#ffffff",
			"opacity" : "1"
		});
	}

});
$("#termsConditionsOfBank").change(function(event) {

	if ($("#termsConditionsOfBank").prop('checked') == false) {
		document.getElementById("bankTransferPaymentType").disabled = true;
		$("#bankTransferPaymentType").css({
			"color" : "#ffffff",
			"opacity" : "0.6"
		});
	} else {
		document.getElementById("bankTransferPaymentType").disabled = false;
		$("#bankTransferPaymentType").css({
			"color" : "#ffffff",
			"opacity" : "1"
		});
	}

});

$("#termsConditionsOfZestmoney").change(function(event) {

  if ($("#termsConditionsOfZestmoney").prop('checked') == false) {
    document.getElementById("zm-button").disabled = true;
    $("#zm-button").css({
      "color" : "#ffffff",
      "opacity" : "0.6"
    });
  } else {
    document.getElementById("zm-button").disabled = false;
    $("#zm-button").css({
      "color" : "#ffffff",
      "opacity" : "1"
    });
  }

});


function createRZPPaymentVerificationRequest(response, orderId_) {
	var input = {
		orderId : orderId_,
		paymentId : response.razorpay_payment_id
	};
	return input;
}

function getRazorPayKey() {
	$.ajax({
		type : 'GET',
		processData : false,
		url : "/api/v1/user/getrazorpaykeyid",
		dataType : "text",
		success : function(response) {
			razorpayKey = response;
		}

	});
}

// payment gateway integration starts here
function payUsingRazorpay(orderId) {

	var options = {
		"key" : razorpayKey,
		"amount" : totalPayableAfterDiscount * 100, // 2000 paise = INR 20
		"name" : "Merchant Name",
		"description" : "Purchase Description",
		"image" : "/images/logo/neostencilLogo.png",
		"handler" : function(response) {
			$.ajax({
				url : apiBasePath + "/user/verify_payment_razorpay",
				type : "POST",
				data : JSON.stringify(createRZPPaymentVerificationRequest(
						response, orderId)),
				contentType : "application/json",
				success : function(response) {

// <!--Google analytics for sucessful purchase orders-->
                var productItemList = [];
         				$.each(response.items,
                    function(index, element) {
                       var input = {
                           "id": element.productId,
                           "name": element.title,
                           "list_name": null,
                           "brand": "Neostencil",
                           "category": element.productType,
                           "variant": null,
                           "list_position": null,
                           "quantity": element.quantity,
                           "price": element.subTotal
                           }
                   productItemList.push(input);
                   });

         						gtag('event', 'purchase', {
                             "user_id": response.userId,
                             "transaction_id": response.orderId,
                             "affiliation": "NeoStencil.com",
                             "value": response.payAmount,
                             "currency": "INR",
                             "tax": null,
                              "items": productItemList
                       });
                 // <!--End of Google Analytics for sucessful purchase orders-->

					window.location = "/thank-you?orderId=" + orderId;
				},

				error : function(response) {
					alert("Failure");
				}
			});

		},
		"prefill" : {
			"name" : name_sub,
			"email" : username
		},
		"notes" : {
			"address" : savedAddressAccordian
		},
		"theme" : {
			"color" : "#F37254"
		}
	};
	var rzp1 = new Razorpay(options);

	rzp1.open();
}

function payUsingZestmoneyEmi(orderId){

	var option = {
    orderId: orderId
	};

  $.ajax({
    url : apiBasePath + "/user/create-zestmoney-order",
    type : "POST",
    data : JSON.stringify(option),
    contentType : "application/json",
    success : function(response) {
			 if(response.code==201){
        window.location = response.url;
			}
    },

    error : function(response) {
    }
  });

}

/*
function generateZestmoneyToken(orderId){
  $.ajax({
    url : apiBasePath + "/user/create-zestmoney-token",
    type : "GET",
    success : function(response) {
      payUsingZestmoneyEmi(orderId);
    },

    error : function(response) {
    }
  });
}
*/

$("#accordion").accordion({
	active : 1
});
var active = $("#accordion").accordion("option", "active");

$("#accordion").accordion("option", "active", 1);

function viewTermsConditions() {
	$('#termsConditionsAccordion').toggle();

}
function viewTermsConditionsBankTransfer() {
	$('#bankTransferTermsConditions').toggle();

}
