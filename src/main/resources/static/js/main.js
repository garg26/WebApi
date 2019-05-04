if (isUserLoggedIn() == true) {
	var userId = parseJwt(getAuthToken()).id;
	var username = parseJwt(getAuthToken()).emailId;
	var name_sub = parseJwt(getAuthToken()).sub;
	var user_name = parseJwt(getAuthToken()).name;
	var user_role = parseJwt(getAuthToken()).role;
	var city = parseJwt(getAuthToken()).city;
	var phonenumber = parseJwt(getAuthToken()).phonenumber;
	console.log(parseJwt(getAuthToken()));
}

function toTimestamp(strDate) {
	var datum = Date.parse(strDate);
	return datum / 1000;
}

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

$(".chat-bubble").click(function() {
	$('.chat-bubble').css("display", "none");
});
function eraseCookieFromAllPaths(name) {
	// This function will attempt to remove a cookie from all paths.
	var pathBits = location.pathname.split('/');
	var pathCurrent = ' path=';

	// do a simple pathless delete first.
	document.cookie = name + '=; expires=Thu, 01-Jan-1970 00:00:01 GMT;';

	for (var i = 0; i < pathBits.length; i++) {
		pathCurrent += ((pathCurrent.substr(-1) != '/') ? '/' : '')
				+ pathBits[i];
		document.cookie = name + '=; expires=Thu, 01-Jan-1970 00:00:01 GMT;'
				+ pathCurrent + ';';
	}
}

$.ajaxSetup({
	beforeSend : function(xhr) {
		xhr
				.setRequestHeader("Accept",
						"application/vvv.website+json;version=1");
		if (isUserLoggedIn() == true) {
			xhr.setRequestHeader("Authorization", "Bearer " + getAuthToken());
		}

	}
});
$(document).ajaxError(function(event, jqxhr) {

	if (jqxhr.status == 401) {
		window.location.href = "/login";
	}
});
$(document).ajaxSuccess(function(event, jqxhr) {

	if (jqxhr.status == 401) {
		window.location.href = "/login";
	}
});

function parseJwt(token) {
	var base64Url = token.split('.')[1];
	var base64 = base64Url.replace('-', '+').replace('_', '/');
	return JSON.parse(window.atob(base64));
};

function getAuthToken() {
	return window.localStorage.getItem('access_token');
}

function isUserLoggedIn() {

	var token = getAuthToken();
	var cookie = getCookie('NSAuthCookie');
	var currentDate = new Date();
	var timestampcurrent = toTimestamp(new Date());

	if (token && cookie) {
		if (token != "null" && token != "undefined" && cookie != "undefined"
				&& token != "" && token != null && cookie != ''
				&& cookie != null && cookie != "null"
				&& timestampcurrent < parseJwt(token).exp) {
			return true;
		}
	}

	localStorage.removeItem("access_token");
	eraseCookieFromAllPaths('NSAuthCookie');
	return false;

}

$(document).ready(function() {

  var exampopup = getUrlParameter('exampopup');
    if(exampopup == "true"){
     setTimeout(function(){
          if(parseJwt(getAuthToken()).exams== null || parseJwt(getAuthToken()).exams == ""){
            openExamSegmentPopup();
          }
        },1000);
}
	if (isUserLoggedIn() == true) {
		$('.login-nav-mobile').hide();
		showCart();
		if ($(window).width() < '767') {
			$('#cartDropdown>a').attr("href", "/cart");
		} else {
			$('#cartDropdown>a').removeAttr("href");
		}
		userDetailsShow();
	} else {
		userDetailsHide();
	}
})
$(window).scroll(
		function() {
			if ($(window).scrollTop() > 0) {
				$("#header").addClass("is-casting-shadow").addClass(
						"is-compact");
			} else {
				$("#header").removeClass("is-casting-shadow").removeClass(
						"is-compact");
			}
		})
$(".search-container input").keyup(function() {
	if ($('.search-container input').val().length > 0) {
		$('.search-container form button').css("pointer-events", "auto");
	} else {
		$('.search-container form button').css("pointer-events", "none");
	}
})

$(".search-container input").focus(function() {
	$(this).removeAttr("placeholder");
	$('.mat-navigation-container').hide(100);
	$('.search-container input').css("width", "800px");
	$('.clear-search').show();
	$(".search-container button").addClass("active");
});
$(".search-results").focusout(function() {
	$('.mat-navigation-container').show(500);
	$('.search-container input').css("width", "250px");
	/* $('.search-results').hide(); */
});
$('.clear-search').click(
		function() {
			$('.mat-navigation-container').show(500);
			$('.search-container input').attr("placeholder",
					"Search for Courses,Teachers,Posts....");
			$('.search-container input').css("width", "250px");
			$('.search-results').hide();
			$('.clear-search').hide();
			$(".search-container input").val("");
			$(".search-container button").removeClass("active");
		})
$(".search-container button").click(function(e) {
	e.preventDefault();
	var homeSearchInputValue = $('.search-container input').val();
	location.replace('/search?search=' + homeSearchInputValue);
})

$("#searchHeader").on('change', function(e) {
	e.preventDefault();
	$('#loader-search').show();
	$(".search-container button").hide();
	searchCommon();
	var headerSearchInputValue = $('.search-container input').val();
	window.location = '/search?search=' + headerSearchInputValue;
});

$(".search-mobile").on('submit', function(e) {
	e.preventDefault();
	var headerSearchInputValue = $(this).find('input').val();
	window.location = '/search?search=' + headerSearchInputValue;
	$('.search-mobile')[0].reset();
});

function searchCommon() {
	$('#loader-search').show();
	$(".search-container button").hide();
	$
			.ajax({
				url : apiBasePath + "/search?search="
						+ $('.search-container input').val() + "&page=1&size=5",
				type : "GET",
				success : function(response) {
					$('#loader-search').hide();
					$(".search-container button").show();
					var htmlToShowCourses = '';
					var htmlToShowInstitutes = '';
					var htmlToShowPosts = '';
					var htmlToShowteachers = '';
					var searchItemsListCourses = '<a href="/course/#slug#"> <i class="search-list-icon fa fa-search"></i>&nbsp;&nbsp;#title# <span>by #author#</span> </a>';
					var searchItemsListinstitutes = '<a href="/institute/#slug#"> <i class="search-list-icon fa fa-search"></i>&nbsp;&nbsp;#title# <span>by #author#</span> </a>';
					var searchItemsListPosts = '<a href="/#slug#"> <i class="search-list-icon fa fa-search"></i>&nbsp;&nbsp;#title#  </a>';
					var searchItemsListTeachers = '<a href="/teacher/#slug#"> <i class="search-list-icon fa fa-search"></i>&nbsp;&nbsp;#title#  </a>';
					var i = 0;
					if (response.courses != "" || response.posts != ""
							|| response.institutes != "") {
						$('#noSearchResultsCommon').hide();
						$
								.each(
										response.courses,
										function(index, element) {
											htmlToShowCourses = htmlToShowCourses += searchItemsListCourses;
											htmlToShowCourses = htmlToShowCourses
													.replace("#title#",
															element.courseTitle);
											htmlToShowCourses = htmlToShowCourses
													.replace(
															"#slug#",
															element.courseOldSlug);
											htmlToShowCourses = htmlToShowCourses
													.replace(
															"#author#",
															element.instructors[0].teacherName);
										});
						$
								.each(
										response.institutes,
										function(index, element) {
											htmlToShowInstitutes = htmlToShowInstitutes += searchItemsListinstitutes;
											htmlToShowInstitutes = htmlToShowInstitutes
													.replace("#title#",
															element.name);
											htmlToShowInstitutes = htmlToShowInstitutes
													.replace(
															"#slug#",
															element.instituteSlug);
											htmlToShowInstitutes = htmlToShowInstitutes
													.replace("#author#",
															element.ownerName);
										});
						$
								.each(
										response.posts,
										function(index, element) {
											htmlToShowPosts = htmlToShowPosts += searchItemsListPosts;
											htmlToShowPosts = htmlToShowPosts
													.replace("#title#",
															element.title);
											htmlToShowPosts = htmlToShowPosts
													.replace("#slug#",
															element.postId);
										});
						$
								.each(
										response.teachers,
										function(index, element) {
											htmlToShowteachers = htmlToShowteachers += searchItemsListTeachers;
											htmlToShowteachers = htmlToShowteachers
													.replace("#title#",
															element.teacherName);
											htmlToShowteachers = htmlToShowteachers
													.replace("#slug#",
															element.slug);
										});
						if (response.courses != "") {
							$('#courseSearchList').html(htmlToShowCourses);
							$('#searchCourses').show();
						} else {
							$('#searchCourses').hide();
						}
						if (response.teachers != "") {
							$('#teacherSearchList').html(htmlToShowteachers);
							$('#searchTeachers').show();
						} else {
							$('#searchTeachers').hide();
						}

						if (response.institutes != "") {
							$('#instituteSearchList')
									.html(htmlToShowInstitutes);
							$('#searchInstitutes').show();
						} else {
							$('#searchInstitutes').hide();
						}
						if (response.posts != "") {
							$('#postSearchList').html(htmlToShowPosts);
							$('#searchPosts').show();
						} else {
							$('#searchPosts').hide();
						}
					} else {
						$('#noSearchResultsCommon').show();
						$('#searchPosts').hide();
						$('#searchInstitutes').hide();
						$('#searchCourses').hide();
					}
					$('.search-results').show();
					$('.search-results').focus();
				},
				error : function(response) {
					$('#loader-search').hide();
					$(".search-container button").show();
				}
			});
}

// get parameter by name
function getParameterByName(name, url) {
	if (!url) {
		url = window.location.href;
	}
	name = name.replace(/[\[\]]/g, "\\$&");
	var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"), results = regex
			.exec(url);
	if (!results) {
		return null;
	}
	if (!results[2]) {
		return '';
	}
	return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function capitalise(str, force) {
	str = force ? str.toLowerCase() : str;
	return str.replace(/(\b)([a-zA-Z])/, function(firstLetter) {
		return firstLetter.toUpperCase();
	});
}

function scrollNav() {
	$(
			'.scrollTop, #scrollToSection a, .course-nav a, .teacher-nav a, .scroll-link')
			.click(function() {
				// Toggle Class
				$(".active").removeClass("active");
				$(this).addClass("active");
				// Animate
        $('html, body').animate({
					scrollTop : $($(this).attr('href')).position().top
				}, 400);
				return false;
			});
}

scrollNav();

function numberWithCommas(x) {
	if (x.toString().length > 3) {
		return x.toString().substring(0, x.toString().split('.')[0].length - 3)
				.replace(/\B(?=(\d{2})+(?!\d))/g, ",")
				+ ","
				+ x.toString().substring(x.toString().split('.')[0].length - 3);
	} else {
		return x;
	}
}

// mega menu starts here
$('.nav-tabs-to-hover > ul > li').mouseover(
		function() {
			$('.nav-tabs-to-hover > ul > li:eq(0)').removeClass("hover");
			index = $(this).index();
			$(this).parent().parent().parent().siblings().find(
					'.items-to-show:eq(' + index + ')').show();
			$(this).parent().parent().parent().siblings().find(
					'.items-to-show:eq(' + index + ')').siblings().hide();
		});
$('.items-to-show').mouseover(
		function() {
			var indexActive = $(this).index();
			$(this).parent().siblings().find('.nav-tabs-to-hover').find(
					'ul > li:eq(' + indexActive + ')').addClass("hover")
					.siblings().removeClass("hover");
			/*
			 * $('.nav-tabs-to-hover > ul > li:eq(' + indexActive +
			 * ')').addClass( "hover").siblings().removeClass("hover");
			 */
		});
$('.items-to-show').mouseleave(
		function() {
			var indexActive = $(this).index();
			$(this).parent().siblings().find('.nav-tabs-to-hover').find(
					'ul > li:eq(' + indexActive + ')').removeClass("hover")
					.siblings().removeClass("hover");
		});
$('.change-navigation-items').mouseleave(function() {
	$('.landing-darken').hide();
	if (!$('.mdl-layout__header').hasClass("is-compact")) {
		$('.mdl-layout__header').removeClass("is-casting-shadow");
	}

	$('.nav-tabs-to-hover > ul > li:eq(0)').addClass("hover");
	$(this).find('.items-to-show:eq(0)').show();
	$(this).find('.items-to-show:eq(0)').siblings().hide();
});
$('.mega-menu.mega-menu-casting > nav > ul > li').mouseover(function() {
	/*
	 * $(this).find('.items-to-show:eq(0)').show();
	 * $(this).find('.items-to-show:eq(0)').siblings().hide();
	 */
	$('#testimonialNav').mouseover(function() {
		$('.landing-darken').hide();
	});
	$('.landing-darken').show();
	$('.mdl-layout__header').addClass("is-casting-shadow");
});
$('.mega-menu.mega-menu-casting > nav > ul > li').mouseleave(function() {
	$('.landing-darken').hide();
	if (!$('.mdl-layout__header').hasClass("is-compact")) {
		$('.mdl-layout__header').removeClass("is-casting-shadow");
	}
});

/* third level starts */

$('.items-to-show .nav-category > ul > li').mouseover(
		function() {
			$('.nav-tabs-to-hover > ul > li:eq(0)').removeClass("hover");
			index = $(this).index();
			$(this).parent().parent().parent().parent().parent().parent()
					.siblings().find('.items-to-show-2:eq(' + index + ')')
					.show();
			$(this).parent().parent().parent().parent().parent().parent()
					.siblings().find('.items-to-show-2:eq(' + index + ')')
					.siblings().hide();
		});

// add to cart from course

function addToCart(buttonClicked) {

	if (buttonClicked == 'addFreeCourse') {
		$('#loader-create-order-for-free-checkout').show();
		$('#createOrderBtn').hide();
	}

	if (isUserLoggedIn() == true) {
		var cartRequest = {
			"quantity" : "1",
			"productType" : "COURSE",
			"commodityId" : $('.radio-checked:checked').val()
		};
		$
				.ajax({
					url : apiBasePath + "/user/update-cart",
					type : "POST",
					data : JSON.stringify(cartRequest),
					dataType : "json",
					contentType : "application/json",
					success : function(response) {
						if (buttonClicked == 'addFreeCourse') {
							$
									.ajax({
										url : apiBasePath
												+ "/user/submit_free_order",
										type : "POST",
										data : JSON
												.stringify({
													addressId : $(
															"#existingAddressSection input[type='radio']:checked")
															.val(),
													paymentModeType : 'free',
													couponCode : ''
												}),
										contentType : "application/json",
										success : function(response) {
											$('#loader-create-order-for-free-checkout')
													.hide();
											$('#proceedToCheckout').show();
											$('#createOrderBtn').show();
											if (response.freeOnlyOrder) {
												window.location = "/user-dashboard";
											} else {
												if (window.location.href
														.indexOf("course") > -1) {
													$
															.when(
																	updateCart(response))
															.then(
																	window.location = '/cart');
												}
											}
										},
									});
						} else if (buttonClicked == 'buyNow') {
							$.when(updateCart(response)).then(
									window.location = '/cart');
						} else {
							updateCart(response);
						}

					},
					error : function(response) {
					}
				});
	} else {
		if ($(window).width() > '767') {
			matLoginClick();
		} else {
			window.location = "/login";
		}
	}
}
function addUnitToCart(unitId) {

  if (isUserLoggedIn() == true) {
    var cartRequest = {
      "quantity" : "1",
      "productType" : "UNIT",
      "commodityId" : unitId
    };
    $("." + unitId).html('<img src="http://www.sbif.cl/recursos/sbifweb3/img/throbber_13.gif" width="18px">');
    $
    .ajax({
      url : apiBasePath + "/user/update-cart",
      type : "POST",
      data : JSON.stringify(cartRequest),
      dataType : "json",
      contentType : "application/json",
      success : function(response) {
      	if (response.items != null) {
          $("." + unitId).html('check_circle');
          $("." + unitId).css("color", "#55bf53");
          updateCart(response);
        }
        else {
          $("."+unitId).html('cancel');
          $("."+unitId).css("color", "#f90829");
          setTimeout(function() {
            $("." + unitId).html('add_shopping_cart');
            $("."+unitId).css("color", "#000000");
          }, 4000);

				}
            },
      error : function(response) {
        $("."+unitId).html('cancel');
        $("."+unitId).css("color", "#f90829");
        setTimeout(function() {
          $("." + unitId).html('add_shopping_cart');
          $("."+unitId).css("color", "#000000");
        }, 5000);
      }
    });
  } else {
    if ($(window).width() > '767') {
      matLoginClick();
    } else {
      window.location = "/login";
    }
  }
}
function announcementHeaderUpdate() {
	var slug = window.location.pathname.split("/");
	var pageUrl;
	if (slug.length > 1){
    pageUrl =  slug[slug.length-1];
    if(pageUrl == ""){
    	pageUrl = "default";
		}
	}
	else{
    pageUrl=	slug[1];
	}

	$
			.ajax({
				url : apiBasePath + "/update-header/"+ pageUrl,
				type : "GET",
				success : function(response) {
					var htmlToShow = '';
					var announcementHeader = '#headerText#<a href="#headerUrl#"  id="gtm_cta_header"class="join-now btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" style="background-image: linear-gradient(to right, #f5a623, #f5a623) !important;">#headerBtnText# </a><i class="material-icons expand-less"> expand_less </i>';

					announcementHeader = announcementHeader.replace(
							'#headerText#', response.headerText);
					announcementHeader = announcementHeader.replace(
							'#headerUrl#', response.headerUrl);
					announcementHeader = announcementHeader.replace(
							'#headerBtnText#', response.btnText);
					$('#headerText').html(announcementHeader);

				},
				error : function(response) {
				}
			});
}
$(document).ready(function() {
	announcementHeaderUpdate();
  $('#sendEnquiryButton').on('click',function () {
    dataLayer.push({
      'event': 'sendEnquiryBtn',
      'eventCategory': 'Click',
      'eventAction': 'SendEnquiryClick',
      'eventLabel': window.location.href
    });
  });
});
function changeAnnouncementHeader() {
	var d = new Date();
  if($('#defaultHeader').is(":checked")){
    var headerFormData = {
      "headerId" : 1,
      "headerText" : $('#headerText').val(),
      "headerUrl" : $('#headerUrl').val(),
      "onPageToDisplay" : $('#onPageToDisplay').val(),
      "btnText" : $('#btnText').val(),
      "createdAt" : d,
      "updatedAt" : d
    };
  }
  else {
    var headerFormData = {
      "headerText" : $('#headerText').val(),
      "headerUrl" : $('#headerUrl').val(),
      "onPageToDisplay" : $('#onPageToDisplay').val(),
      "btnText" : $('#btnText').val(),
      "createdAt" : d,
      "updatedAt" : d
    };
  }

	$.ajax({
		url : apiBasePath + "/admin_editor/change-header",
		type : "POST",
		data : JSON.stringify(headerFormData),
		dataType : "json",
		contentType : "application/json",
		success : function(response) {
			alert("Header Updated Successfully");
			/*$("#headerForm").trigger('reset')*/;
		},
		error : function(response) {
			alert("Error while updating Header");
		}
	});
}

function addToCart_old() {
	if (isUserLoggedIn() == true) {
		$.ajax({
			url : apiBasePath + "/carts",
			type : "POST",
			data : JSON.stringify({
				"cart" : {
					"items" : [ {
						"quantity" : "1",
						"productType" : "COURSE",
						"product" : {
							"commodityId" : $('.radio-checked:checked').val()
						}
					} ],
					"student" : {
						"userId" : userId
					}
				}
			}),
			dataType : "json",
			contentType : "application/json",
			success : function(response) {
				updateCart(response);
			},
			error : function(response) {
			}
		});
	} else {
		matLoginClick();
	}
}

// fetch cart items from user id
function showCart() {
	$.ajax({
		url : apiBasePath + "/user/get-cart",
		type : "GET",
		success : function(response) {
			$('#checkoutDropdown').attr("href", "/checkout?coupon="); // add
			// token
			// here instead
			// of cartID
			updateCart(response);
		},
		error : function(response) {
		}
	});
}

// update cart
function updateCart(response) {
	var htmlToShow = '';
	var cartItemsList = '<div class="mdl-grid course-in-cart"> <div class="mdl-cell mdl-cell--3-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"> <img src=#imageUrl# height="48px" width="72px"> </div> <div class="mdl-cell-cart mdl-cell mdl-cell--9-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"> <p> #courseName# #teacherName# <span class="cart-starts-on">#startsOn#</span><span class="cart-price"><i class="fa fa-inr"></i> <b>#salePrice#</b> | <span>Qty</span> <b>#quantity#</b> </span> </p> </div> </div>';
	var i = 0;
	if (response.items != null) {
		$.each(response.items, function(index, element) {
			i = index + 1;
			htmlToShow = htmlToShow += cartItemsList;
			htmlToShow = htmlToShow.replace("#salePrice#",
					numberWithCommas(element.price));
			htmlToShow = htmlToShow.replace("#courseName#", element.title);
			htmlToShow = htmlToShow.replace("#imageUrl#", element.imageUrl
					+ '=w72-h48-s');
			htmlToShow = htmlToShow.replace("#quantity#", element.quantity);
			htmlToShow = htmlToShow.replace("#startsOn#", "");
			htmlToShow = htmlToShow.replace("#teacherName#", ""); // <span><i>by</i>
			// Majid
			// Hussain</span>
		});
		$('#cartDropdownSubtotal').html(
				"Rs " + numberWithCommas(response.payAmount));
		$('#cartDropdownTotal').html(
				"Rs " + numberWithCommas(response.totalAmount));
		$('#cartNotEmpty').show();
		$('#cartEmpty').hide();
		$('#numItems').html("(" + i + " Items)");
		$('#cart_dropdown').attr("data-badge", i);
		$('#cartItemsDropdown').html(htmlToShow);
		if ($(window).width() < '767') {
			$('#cartDropdown>a').attr("href", "/cart");
		}
	} else {
		$('#cartDropdown>a').removeAttr("href");
		$('#cartEmpty').show();
		$('#cartNotEmpty').hide();
		$('#cart_dropdown').removeAttr("data-badge");
	}
}

$("#sendEnquiryForm").on('submit', function(e) {
  e.preventDefault();
  $('#sendEnquiryForm').validate({
    rules: {
      mobileNumber: {
        required: true,
        maxlength: 10,
        minlength: 10
      }
    },
    messages: {
      mobileNumber: {
        required: "Please enter your number",
        maxlength: "Please enter a valid number",
        minlength: "Please enter a valid number"
      }
    },
    submitHandler: function () {
      sendNewMessage(e);
		}
  });

});
	function sendNewMessage(el) {
  var emailId=null;
	$('.emptyMessage').hide();
	var enctype = '';
	if ($(el).siblings('textarea').val() != 0) {
		$('.send-enquiry-common').addClass('btn-disabled');
		$(el).siblings('.emptyMessage').hide();
		if ($(el).hasClass("send-message")) {
       emailId=username;
			var obj = {
				"messageTo" : $(el).siblings('textarea').attr("data-name"),
				"body" : $(el).siblings('textarea').val()
			};
		} else {
			$('.show-dialog-form').show();
			$('.message-has-been-sent').hide();
			var obj = {
				"messageTo" : $('#sendEnquiryForm #textBox').attr('data-id'),
				"body" : $('#sendEnquiryForm #textBox').val(),
				"topic" : $('#sendEnquiryForm #textBox').attr('data-title'),
				"messageUrl" : document.location.href,
        "messageFrom": $('#sendEnquiryForm #emailAddress').val()
			};
			var mobileNo= $('#sendEnquiryForm #mobileNumber').val();
			emailId=$('#sendEnquiryForm #emailAddress').val();
		}
		$.ajax({
			url : apiBasePath + '/messages?mobileNo='+mobileNo +'&emailId='+emailId,
			type : "POST",
			data : JSON.stringify(obj),
			contentType : "application/json",
			processData : false,
			dataType : "json",
			enctype : enctype,
			success : function(response) {
        dataLayer.push({
          'event': 'sendEnquirySubmit',
          'eventCategory': 'SendEnquiryForm',
          'eventAction': 'Submit',
          'eventLabel': window.location.href
        });
				$(el).removeClass('btn-disabled');
				$(el).siblings('textarea').val('');
				if ($(el).hasClass("send-message")) {
					$(el).parent().find('ul').append(
							'<li class="receiver"><div><b>'
									+ response.message.messageFrom + '</b>'
									+ response.message.body + '</div></li>');
					$(el).parent().find('ul').animate({
						scrollTop : $(el).parent().find('ul')[0].scrollHeight
					}, 100);
				} else {
					$('.show-dialog-form').hide();
					$('.message-has-been-sent').show();
				}
			},
			error : function(response) {
				if (response.status == '401') {
					$(el).siblings('.emptyMessage').html(
							"Please login to send a message").show();
				}
				$(el).removeClass('btn-disabled');
			}
		});
	} else {
		$(el).siblings('.emptyMessage').html("Please enter a message").show();
	}
}

$('#scrollToSection a').click(function() {
	$(this).addClass('active');
	$(this).siblings().removeClass('active');
});

$('.teacher-nav a, .course-nav a').click(function() {
	$(this).parent().addClass('active');
	$(this).parent().siblings().removeClass('active');
});
$('.search-mobile label').click(function() {
	$(this).parent().parent('form').siblings().hide();
	$(this).parent().parent('form').css({
		"left" : "8px",
		"width" : "100%"
	});
})
$('.search-mobile input').blur(function() {
	$(this).parent().parent('.mdl-textfield').removeClass("is-dirty");
	$(this).parent().parent().parent('form').siblings().delay("200").fadeIn();
	$(this).parent().parent().parent('form').css({
		"left" : "auto",
		"width" : "auto"
	});
	$('.search-mobile')[0].reset();
})
$('.hide-tutorial-button').click(function() {
	$('.btn-tutorial-fixed').hide();
})
