var totalPayable = 0;
function deleteCart(commodityId, productId) {
	$('#cartItems').css({
		"opacity" : "0.4",
		"pointer-events" : "none"
	});
	var cartRequest = {
		"quantity" : 0,
		"productId" : productId,
		"commodityId" : commodityId
	};
	$.ajax({
		url : apiBasePath + "/user/update-cart",
		type : "POST",
		data : JSON.stringify(cartRequest),
		contentType : "application/json",
		success : function(response) {
			updateCarts(response);
			$('#cartItems').css({
				"opacity" : "1",
				"pointer-events" : "all"
			});
			/*
			 * if (response.totalAmount == '0' && response.items.length > 0) {
			 * $('#proceedToCheckout').removeAttr("href").attr("onclick",
			 * "addToCart('addFreeCourse')"); }
			 */
		},
		error : function(response) {
			$('#cartItems').css({
				"opacity" : "1",
				"pointer-events" : "all"
			});
		}
	});
}

function updateCarts(response) {
	$('#cartLoader').hide();
	var htmlToShow = '';
	var cartItemsList = '<tr> <td class="mdl-data-table__cell--non-numeric"><img src="#imageUrl#" height="86px" width="128px"></td> <td style="text-align:left;">  <div class="cartCourseName">#courseName#</div><p style="display: none;" class="cart-price-mobile"><i class="fa fa-inr"></i>&nbsp;#totalPrice#</p><span style="display: none;" class="cart-starts-on">#startsOn#</span><span class="delete-from-cart"><i class="material-icons">delete</i>&nbsp;<a data-cart-id="#productId#" data-order-item-id="#commodityId#" class="delete-cart" onclick="deleteCart(#commodityId#,#productId#);">Delete</a></span>  </td> <td class="cart-table-price"><i class="fa fa-inr"></i>#salePrice#</td> '
			+ ' <td class="cart-table-price"><i class="fa fa-inr"></i>#totalPrice#</td> </tr>';
	var i = 0;
	if (response.items != null) {
		$.each(response.items, function(index, element) {
			i = index + 1;
			htmlToShow = htmlToShow += cartItemsList;
			htmlToShow = htmlToShow.replace(/#salePrice#/g,
					numberWithCommas(element.price));
			htmlToShow = htmlToShow.replace(/#totalPrice#/g,
					numberWithCommas(element.subTotal));
			htmlToShow = htmlToShow.replace("#courseName#", element.title);
			htmlToShow = htmlToShow.replace("#quantity#", element.quantity);
			htmlToShow = htmlToShow.replace("#imageUrl#", element.imageUrl
					+ '=w128-h79');
			htmlToShow = htmlToShow.replace(/#commodityId#/g,
					element.commmodityId);
			htmlToShow = htmlToShow.replace('#startsOn#', "");
			htmlToShow = htmlToShow.replace(/#productId#/g, element.productId);
		});
		$('#subtotal').html(numberWithCommas(response.payAmount));
		$('#amountPayable').html(numberWithCommas(response.totalAmount));
		$('#amountPayableCart').html(numberWithCommas(response.totalAmount));
		$('#cartLoader').hide();
		$('#cartItems').html(htmlToShow);
		$('#cartTable').show();
		totalPayable = response.payAmount;
		$('.finalAmount').html("Rs " + numberWithCommas(totalPayable));
	} else {
		$('#cartLoader').hide();
		$('#emptyCart').show();
		$('#cartTable').hide();
	}

	var addRemoveFromCart = [];
	addRemoveFromCart.push({
		'event' : 'addToCart',
		'ecommerce' : {
			'currencyCode' : 'EUR',
			'add' : { // 'add' actionFieldObject measures.
				'products' : [ { // adding a product to a shopping cart.
					courseName : 'element.title',
					salePrice : 'element.price',
					quantity : 'element.quantity'
				} ]
			}
		}
	});
	// Measure the removal of a product from a shopping cart.
	addRemoveFromCart.push({
		'event' : 'removeFromCart',
		'ecommerce' : {
			'remove' : { // 'remove' actionFieldObject measures.
				'products' : [ { // removing a product to a shopping cart.
					courseName : 'element.title',
					salePrice : 'element.price',
					quantity : 'element.quantity'
				} ]
			}
		}
	});

	validateCoupon();
}

$(document).ready(function() {
	loadCart();
})

Number.prototype.clamp = function(min, max) {
	return Math.min(Math.max(this, min), max);
};

(function($) {
	$.fn.filterFind = function(selector) {
		return this.find('*') // Take the current selection and find all
		// descendants,
		.addBack() // add the original selection back to the set
		.filter(selector); // and filter by the selector.
	};
})(jQuery);

(function($) {
	$.fn.svgDraw = function(progress) {
		this.filterFind('path').each(
				function() {
					var pathLength = this.getTotalLength();
					$(this).css('strokeDasharray',
							pathLength + ' ' + pathLength);
					$(this).css('strokeDashoffset',
							pathLength * ((1 - progress)).clamp(0, 1));
				});

		return this;
	};
})(jQuery);

function qtyplus(e, commodityId, productId) {
	fieldName = $(e).attr('field');
	var currentVal = parseInt($(e).siblings('input[name=' + fieldName + ']')
			.val());
	if (!isNaN(currentVal)) {
		$(e).siblings('input[name=' + fieldName + ']').val(currentVal + 1);
	} else {
		$(e).siblings('input[name=' + fieldName + ']').val(0);
	}
	addSubtractQuantity(e, commodityId, productId);

}

function onEnter(e, commodityId, productId) {

	if (e.keyCode == 13) {
		e.preventDefault();
	}
	minusplusFromInput(commodityId, productId);
}

function minusplusFromInput(commodityId, productId) {

	$('#cartItems').css({
		"opacity" : "0.4",
		"pointer-events" : "none"
	});
	var cartRequest = {
		"quantity" : $('.qty').val(),
		"productId" : productId,
		"commodityId" : commodityId
	};
	$.ajax({
		url : apiBasePath + "/user/update-cart",
		type : "POST",
		data : JSON.stringify(cartRequest),
		dataType : "json",
		contentType : "application/json",
		success : function(response) {
			updateCarts(response);
			$('#cartItems').css({
				"opacity" : "1",
				"pointer-events" : "all"
			});

		},
		error : function(response) {
			$('#cartItems').css({
				"opacity" : "1",
				"pointer-events" : "all"
			});

		}
	});

}

function qtyminus(e, commodityId, productId) {
	fieldName = $(e).attr('field');
	var currentVal = parseInt($(e).siblings('input[name=' + fieldName + ']')
			.val());
	if (!isNaN(currentVal) && currentVal > 0) {
		$(e).siblings('input[name=' + fieldName + ']').val(currentVal - 1);
	} else {
		$(e).siblings('input[name=' + fieldName + ']').val(0);
		deleteCart(commodityId, productId);
	}
	addSubtractQuantity(e, commodityId, productId);

}

function addSubtractQuantity(e, commodityId, productId) {
	$('#cartItems').css({
		"opacity" : "0.4",
		"pointer-events" : "none"
	});
	var cartRequest = {
		"quantity" : $(e).siblings('input[name=' + fieldName + ']').val(),
		"productId" : productId,
		"commodityId" : commodityId
	};
	$.ajax({
		url : apiBasePath + "/user/update-cart",
		type : "POST",
		data : JSON.stringify(cartRequest),
		dataType : "json",
		contentType : "application/json",
		success : function(response) {
			updateCarts(response);
			$('#cartItems').css({
				"opacity" : "1",
				"pointer-events" : "all"
			});
		},
		error : function(response) {
			$('#cartItems').css({
				"opacity" : "1",
				"pointer-events" : "all"
			});
		}
	});
}

$('.progress-button .progress-circle').svgDraw(0);
$('#enterCoupon').keypress(function() {
	$('.progress-button').removeClass('error');
})
$('#removeCoupon').click(function() {
	$('.progress-button').removeClass('error');
	$('.progress-button').removeClass('success');
	$('#enterCoupon').val("");
	$('.order-summary-div .mdl-textfield__input').removeAttr("style");
	$('.couponMoney').hide();
	$('#proceedToCheckout').attr("href", "/checkout");
	$('#amountPayableCart').html($('#subtotal').html());
})
$('#progressBar button').click(function(e) {
	e.preventDefault();
})

function progressBar(e) {
	var validatedCoupon = "";
	var $button = $(e);
	$(e).addClass('loading');

	var $progress = $(e).find('.progress-circle');
	var progress = 0;
	var intervalId = setInterval(function() {
		progress += Math.random() * 0.5;
		$progress.svgDraw(progress);

		if (progress >= 1) {
			clearInterval(intervalId);
			$button.removeClass('loading');
			validateCoupon(e);
		}
	}, 300);

	$(this).off('click');

}
function validateCoupon(e) {
	$('.progress-button').removeClass('error');
	$('.progress-button').removeClass('success');
	var $button = $(e);
	$
			.ajax({
				url : apiBasePath + "/user/validatecoupon?coupon="
						+ $('#enterCoupon').val(),
				type : "get",
				contentType : "application/json",
				success : function(response) {
					if (response.valid == true) {
						$button.addClass('success');
						$('.order-summary-div .mdl-textfield__input')
								.attr("style",
										"color: #3fbf53!important;border-bottom: solid 1px #3fbf53!important;");
						$('#couponMoney').html("-" + response.discount);
						$('#amountPayableCart').html(
								numberWithCommas(response.payAmount));
						$('.couponMoney').show();
						validatedCoupon = $('#enterCoupon').val();
						$('#proceedToCheckout').removeAttr("onclick").attr(
								"href", "/checkout?coupon=" + validatedCoupon);

					} else {
						$button.addClass('error');

						$('.order-summary-div .mdl-textfield__input')
								.removeAttr("style");
						$('#couponMoney').html("");
						$('#amountPayableCart').html(
								numberWithCommas(response.payAmount));
						$('.couponMoney').show();
						validatedCoupon = "";
						$('#proceedToCheckout').removeAttr("onclick").attr(
								"href", "/checkout");
					}
				},

				error : function(response) {
					$button.addClass('error');
					setTimeout(function() {
						$button.removeClass('error');
						$('.progress-button > button').blur();
					}, 3000);
					$('.order-summary-div .mdl-textfield__input').removeAttr(
							"style");
					$('.couponMoney').hide();
				}
			});

}

$('#increase-product .qty').change(
		function() {
			var tp = $(this).parent().parent().siblings('.price-of-one').find(
					'span').html().replace(/,/g, "");
			var num = $(this).val();
			var totalPrice = tp * num;
			$(this).parent().parent().siblings('.price-total').find('span')
					.html(numberWithCommas(totalPrice));
		})

// shopping workflow starts here

$('#removeCoupon').on('click', function() {
	$('.order-summary-div form')[0].reset();
	$('.progress-button').removeClass('error');
	$('.progress-button').removeClass('success');
	$('.progress-button > button').blur();
});

function loadCart() {

	$.ajax({
		url : apiBasePath + "/user/get-cart",
		type : "GET",
		success : function(response) {
			$('#proceedToCheckout').attr("href", "/checkout");
			$('#enterCoupon').attr("data-cart-id", response.cardId);
			updateCarts(response);
		},

		error : function(response) {
			$('#enterCoupon').attr("data-cart-id", response.cardId);
		}
	});
}
