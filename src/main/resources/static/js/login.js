var prevPage = document.referrer;
$('.mat-close').click(function() {
	$('form[name=signup]')[0].reset();
	$('form[name=login]')[0].reset();
	$('form[name=resetPassword]')[0].reset();
	$('form').find('label.error').remove();
	$('.common-loader').hide();
	$('.mat-card-login').show();
	$('form').find('.error-single').html('');
	$('.login-menu').removeClass('menu--on');
	$(this).hide();
	$('.landing-darken').hide();
	$('#signupMenuLogin').hide();
	$('#signupMenuSocial').hide();
	$('#signupMenuForm').hide();
	$('#resetPasswordForm').hide();
	$('#emailSentForm').hide();
	$("#errorLogin").html("");
});
function matLoginClick() {
	$('form')[0].reset();
	$('form').find('label.error').remove();
	$('form').find('.error-single').html('');
	$('.login-menu').toggleClass('menu--on');
	/* $('.signup-menu').removeClass('menu--on'); */
	if ($('.login-menu').hasClass('menu--on')) {
		$('.mat-close').show();
		$('.landing-darken').show();
		$('.mat-content').css("overflow-y", "hidden");
	} else {
		$('.mat-close').hide();
		$('.landing-darken').hide();
	}
	$('#signupMenuLogin').show();
	$('#signupMenuSocial').hide();
	$('#signupMenuForm').hide();
	$('#resetPasswordForm').hide();
	$('#emailSentForm').hide();
}

function matSignupClick() {
	$('form')[0].reset();
	$('form').find('label.error').remove();
	$('form').find('.error-single').html('');
	$('.login-menu').toggleClass('menu--on');
	/* $('.login-menu').removeClass('menu--on'); */
	if ($('.login-menu').hasClass('menu--on')
			|| $('.login-menu').hasClass('menu--on')) {
		$('.mat-close').show();
		$('.landing-darken').show();
		$('.mat-content').css("overflow-y", "hidden");
	} else {
		$('.mat-close').hide();
		$('.landing-darken').hide();
	}
	$('#signupMenuSocial').show();
	$('#signupMenuLogin').hide();
	$('#signupMenuForm').hide();
	$('#resetPasswordForm').hide();
	$('#emailSentForm').hide();
}
$('#continueWithEmail').click(function(e) {
	e.preventDefault();
	$('form')[0].reset();
	$('form').find('label.error').remove();
	$('form').find('.error-single').html('');
	$('#signupMenuSocial').hide();
	$('#signupMenuForm').show();
	$('#resetPasswordForm').hide();
	$('#resetPasswordForm').hide();
	$('#emailSentForm').hide();
	if (window.location.href.indexOf("login") != -1) {
		$(".mdl-layout__content").animate({
			scrollTop : 0
		}, "slow");
	}
})
$('.loginNow').click(function(e) {
	e.preventDefault();
	$('form')[0].reset();
	$('form').find('label.error').remove();
	$('form').find('.error-single').html('');
	$('#signupMenuSocial').hide();
	$('#signupMenuForm').hide();
	$('#signupMenuLogin').show();
	$('#resetPasswordForm').hide();
	$('#emailSentForm').hide();
	if (window.location.href.indexOf("login") != -1) {
		$(".mdl-layout__content").animate({
			scrollTop : 0
		}, "slow");
	}
})
$('#createNow').click(function(e) {
	e.preventDefault();
	$('form')[0].reset();
	$('form').find('label.error').remove();
	$('form').find('.error-single').html('');
	$('#signupMenuSocial').show();
	$('#signupMenuLogin').hide();
	$('#signupMenuForm').hide();
	$('#resetPasswordForm').hide();
	$('#emailSentForm').hide();
	if (window.location.href.indexOf("login") != -1) {
		$(".mdl-layout__content").animate({
			scrollTop : 0
		}, "slow");
	}
})
$('#forgotPassword').click(function(e) {
	e.preventDefault();
	$('form')[0].reset();
	$('form').find('label.error').remove();
	$('form').find('.error-single').html('');
	$('#signupMenuSocial').hide();
	$('#signupMenuLogin').hide();
	$('#signupMenuForm').hide();
	$('#resetPasswordForm').show();
	$('#emailSentForm').hide();
	if (window.location.href.indexOf("login") != -1) {
		$(".mdl-layout__content").animate({
			scrollTop : 0
		}, "slow");
	}

});

$(function() {
	$("form[name='login']")
			.validate(
					{
						rules : {
							username_login : {
								required : true,
								email : true
							},
							password : {
								required : true,
								minlength : 5
							}
						},
						messages : {
							password : {
								required : "Please provide a password",
								minlength : "Your password must be at least 5 characters long"
							},
							username_login : {
								required : "Please provide an email address",
								email : "Please provide a valid email address"
							}
						},
						showErrors : function(errorMap, errorList) {
							$("form[name='login']").find("input").each(
									function() {
										$(this).removeClass("error");
									});
							$("#errorLogin").html("");
							if (errorList.length) {
								$("#errorLogin").html(errorList[0]['message']);
								$(errorList[0]['element']).addClass("error");
							}
						},
						submitHandler : function(form, e) {
							$('#loader-login').show();
							$('#btn-login').hide();
							var loginInput = {
								emailId : $("form[name='login']").find(
										"#username_login").val(),
								password : $("form[name='login']").find(
										"#password_login").val()
							};

							$
									.ajax({
										url : apiBasePath + "/login",
										type : "post",
										data : JSON.stringify(loginInput),
										dataType : "json",
										contentType : "application/json",
										processData : false,

										success : function(response) {
											$('#loader-login').hide();
											$('#btn-login').show();
											if (response.loginSucces == true) {
												(function() {
													'use strict';
													var snackbarContainer = document
															.querySelector('.mdl-js-snackbar');
													var showToastButton = document
															.querySelector('#demo-show-toast');

													var data = {
														message : "You are now logged in"
													};
													snackbarContainer.MaterialSnackbar
															.showSnackbar(data);
												}());
												e.preventDefault();
												loginUser(response.accessToken,
														"");
												userDetailsShow();
												$('.mat-close')
														.trigger("click");

                        if($('#checkIsPost').length>0){
                          if($("#checkIsPost").val() == "isPost")
                          {
                            window.location.reload();

                          }
                        }

												if (window.location.href
														.indexOf("login") != -1) {
													var passwordResetSuccess = getParameterByName('password-reset-success');
													if (passwordResetSuccess == 'true') {
														window.location = "/";
													} else {
														window.location = prevPage;
													}
												}

											} else {
												$('#errorLogin')
														.html(
																'Incorrect username or password')
														.show();
											}

										},

										error : function(response) {
											$('#loader-login').hide();
											$('#btn-login').show();
											e.preventDefault();
											console.log("Error");
											console.log(response);
											/* userDetailsShow(); */
											$('#errorLogin')
													.html(
															response.responseJSON.errorMessage)
													.show();
										}
									});
						}
					});
});

$(function() {
	$("form[name='signup']")
			.validate(
					{
						rules : {
							name : {
								required : true
							},
							email : {
								required : true,
								email : true
							},
							password : {
								required : true,
								minlength : 5
							}
						},
						messages : {
							name : {
								required : "Please provide your name"
							},
							email : {
								required : "Please provide an email address",
								email : "Please provide a valid email address"
							},
							password : {
								required : "Please provide a password",
								minlength : "Your password must be at least 5 characters long"
							}
						},
						showErrors : function(errorMap, errorList) {
							$("form[name='signup']").find("input").each(
									function() {
										$(this).removeClass("error");
									});
							$("#errorSignup").html("");
							if (errorList.length) {
								$("#errorSignup").html(errorList[0]['message']);
								$(errorList[0]['element']).addClass("error");
							}
						},
						submitHandler : function(form, e) {
							$('#loader-signup').show();
							$('#btn-signup').hide();
							e.preventDefault();

							var signUpInput = {
								fullName : $("form[name='signup']").find(
										"#name").val(),
								emailId : $("form[name='signup']").find(
										"#email").val(),
								password : $("form[name='signup']").find(
										"#password").val()
							};

							$
									.ajax({
										url : apiBasePath + "/signup",
										type : "post",
										data : JSON.stringify(signUpInput),
										dataType : "json",
										contentType : "application/json",
										processData : false,

										success : function(response) {
											//pushSignupDataToHubspot(response.accessToken)
											$('#loader-signup').hide();
											$('#btn-signup').show();
											if (response.loginSucces) {
												(function() {
													'use strict';
													var snackbarContainer = document
															.querySelector('.mdl-js-snackbar');
													var showToastButton = document
															.querySelector('#demo-show-toast');

													var data = {
														message : "You have now signed up"
													};
													snackbarContainer.MaterialSnackbar
															.showSnackbar(data);
													
												}());
												$('#errorSignup').html('')
														.hide();
												e.preventDefault();
												console.log("Success");
												console.log(response);
												loginUser(response.accessToken,
														"");
												userDetailsShow();
												$('.mat-close')
														.trigger("click");
												creditNeoCash();

                        if($('#checkIsPost').length>0){
                          if($("#checkIsPost").val() == "isPost")
                          {
                            window.location.reload();
                            $("html, body").animate({ scrollTop: $('.drop-a-comment').offset().top }, 1000);

                          }
                        }


											} else {
												$('#errorSignup').html(
														response.errorMessage)
														.show();
											}

											/* document.cookie='access_token='+response.accessToken; */
										},

										error : function(response) {
											$('#loader-signup').hide();
											$('#btn-signup').show();
											e.preventDefault();
											console.log("Error");
											console.log(response);
											/* userDetailsShow(); */
											$('#errorSignup')
													.html(
															response.responseJSON.errorMessage)
													.show();
										}
									});
						}
					});
});

$(function() {
	$("form[name='resetPassword']").validate(
			{
				rules : {
					email : {
						required : true,
						email : true
					}
				},
				messages : {
					email : {
						required : "Please provide an email address",
						email : "Please provide a valid email address"
					}
				},
				showErrors : function(errorMap, errorList) {
					$("form[name='signup']").find("input").each(function() {
						$(this).removeClass("error");
					});
					$("#errorPassword").html("");
					if (errorList.length) {
						$("#errorPassword").html(errorList[0]['message']);
						$(errorList[0]['element']).addClass("error");
					}
				},
				submitHandler : function(form, e) {
					$('#loader-reset-password').show();
					$('#sendResetLink').hide();
					e.preventDefault();
					$('form')[0].reset();
					$('form').find('label.error').remove();
					$('form').find('.error-single').html('');
					var resetInput = $("form[name='resetPassword']").find(
							"#email-id").val();

					$.ajax({
						url : apiBasePath + "/forgot-password",
						type : "post",
						data : resetInput,
						dataType : "json",
						contentType : "application/json",
						processData : false,

						success : function(response) {
							console.log(response);
							$('#loader-reset-password').hide();
							$('#sendResetLink').show();
							if (response.success) {
								console.log("Success");
								$('#signupMenuSocial').hide();
								$('#signupMenuForm').hide();
								$('#resetPasswordForm').hide();
								$('#emailSentForm').show();
								$('#email-id-reset').val($('#email-id').val());
								$('#reset-password-email').html(
										$('#email-id').val());
							} else {
								$('#errorPassword').html(response.errorMessage)
										.show();
							}
						},

						error : function(response) {
							console.log("Error");
							console.log(response);
							$('#loader-reset-password').hide();
							$('#sendResetLink').show();
							alert(response.errorMessage);
						}
					});
				}
			});
});
$(function() {
	$("form[name='resetPasswordResendLink']").validate(
			{
				rules : {
					email : {
						required : true,
						email : true
					}
				},
				messages : {
					email : {
						required : "Please provide an email address",
						email : "Please provide a valid email address"
					}
				},
				showErrors : function(errorMap, errorList) {
					$("form[name='signup']").find("input").each(function() {
						$(this).removeClass("error");
					});
					$("#errorPassword").html("");
					if (errorList.length) {
						$("#errorPassword").html(errorList[0]['message']);
						$(errorList[0]['element']).addClass("error");
					}
				},
				submitHandler : function(form, e) {
					$('#linkSentAgain').hide();
					$('#loader-reset-password-resend').show();
					$('#sendLinkAgain').hide();
					e.preventDefault();
					$('form')[0].reset();
					$('form').find('label.error').remove();
					$('form').find('.error-single').html('');
					var resetInput = $("form[name='resetPasswordResendLink']")
							.find("#email-id-reset").val();

					$.ajax({
						url : apiBasePath + "/forgot-password",
						type : "post",
						data : resetInput,
						dataType : "json",
						contentType : "application/json",
						processData : false,

						success : function(response) {
							console.log(response);

							$('#loader-reset-password-resend').hide();
							$('#sendLinkAgain').show();
							if (response.success) {
								$('#linkSentAgain').show();
								console.log("Success");
								$('#signupMenuSocial').hide();
								$('#signupMenuForm').hide();
								$('#resetPasswordForm').hide();
								$('#emailSentForm').show();
								$('#reset-password-email-again').html(
										$('#email-id-reset').val());
							} else {
								$('#errorPasswordReset').html(
										response.errorMessage).show();
							}
						},

						error : function(response) {
							console.log("Error");
							console.log(response);
							$('#loader-reset-password-resend').hide();
							$('#sendResetLink').show();
							alert(response.errorMessage);
						}
					});
				}
			});
});
/*
 * $( "input" ).keyup(function() { $('form').find('.error-single').html(''); });
 */

var getUrlParameter = function getUrlParameter(sParam) {
	var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL
			.split('&'), sParameterName, i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : sParameterName[1];
		}
	}
};
$(function() {
	$("form[name='reset-password-form']")
			.validate(
					{
						rules : {
							passwordReset : {
								required : true,
								minlength : 5
							},
							confirmPasswordReset : {
								required : true,
								minlength : 5,
								equalTo : '[name="passwordReset"]'
							},
						},
						messages : {
							passwordReset : {
								required : "Please provide a password",
								minlength : "Your password must be at least 5 characters long",
								passwordMatch : true
							},
							confirmPasswordReset : {
								required : "Please re-enter the password",
								minlength : "Your password must be at least 5 characters long",
								equalTo : "Your Passwords Must Match"
							},
						},
						showErrors : function(errorMap, errorList) {
							$("form[name='reset-password-form']").find("input")
									.each(function() {
										$(this).removeClass("error");
									});
							$("#resetPasswordError").html("");
							if (errorList.length) {
								$("#resetPasswordError").html(
										errorList[0]['message']);
								$(errorList[0]['element']).addClass("error");
							}
						},
						submitHandler : function(form, e) {
							var token = getUrlParameter('token'); // in
							// main.js
							var passwordOnReset = {
								resetToken : token,
								password : $("input[name='passwordReset']")
										.val()
							};
							$('#loader-reset-password-page').show();
							$('#btn-password-reset').hide();
							e.preventDefault();

							$
									.ajax({
										url : apiBasePath + "/update-password",
										type : "POST",
										data : JSON.stringify(passwordOnReset),
										dataType : "json",
										contentType : "application/json",
										processData : false,

										success : function(response) {
											$('#loader-reset-password-page')
													.hide();
											$('#btn-password-reset').show();
											console.log(response);

											if (response.updateSuccess == true) {
												$('#resetPasswordError').html(
														'').hide();
												e.preventDefault();
												window.location = "/login?password-reset-success=true";
											} else {
												$('#resetPasswordError')
														.html(
																'Some error occured. Please try again after sometime')
														.show();
											}
										},

										error : function(response) {
											$('#loader-reset-password-page')
													.hide();
											$('#btn-password-reset').show();
											e.preventDefault();
											alert('Some error occured');
										}
									});
						}
					});
});

$(document).ready(function() {
	getFBLoginButtonUri();
	getGoogleLoginButtonUri();
	$('#userDetails').hide();
	$('#userDetailsGoogle').hide();
});

// facebook login
function getFBLoginButtonUri() {
	$.ajax({
		url : "/facebook/getloginuri",
		contentType : "application/json",
		dataType : "text",
		success : function(response) {
			console.log(response);
			if (window.location.href.indexOf("login") != -1) {
				$(".loginUsingFacebook").attr("href",
						response + "&state=" + encodeURIComponent(prevPage));
			} else {
				$(".loginUsingFacebook").attr(
						"href",
						response + "&state="
								+ encodeURIComponent(window.location.href));
			}

		},
		error : function(response) {
			console.log(response);
		}
	});
}
// google login
function getGoogleLoginButtonUri() {
	$.ajax({
		url : "/google/getloginuri",
		contentType : "application/json",
		dataType : "text",
		success : function(response) {
			console.log(response);
			if (window.location.href.indexOf("login") != -1) {
				$(".loginUsingGoogle").attr("href",
						response + "&state=" + encodeURIComponent(prevPage));
			} else {
				$(".loginUsingGoogle").attr(
						"href",
						response + "&state="
								+ encodeURIComponent(window.location.href));
			}

		},
		error : function(response) {
			console.log(response);
		}
	});
}

function pushSignupDataToHubspot(accessToken) {
	var _hsq = window._hsq = window._hsq || [];
	_hsq.push([ "identify", {
		email : parseJwt(accessToken).emailId,
		ns_source : 'Website Registration/ First Login'
	} ]);
	_hsq.push([ 'trackPageView' ]);
}

function loginUser(accessToken, redirectPage) {
	window.localStorage.setItem('access_token', accessToken);
	document.cookie = 'NSAuthCookie=' + accessToken + "; path=/";

	if (redirectPage) {
		window.location.href = redirectPage+"?exampopup=true";
	} else {
	  setTimeout(function(){
            if(parseJwt(getAuthToken()).exams== null || parseJwt(getAuthToken()).exams == ""){
              openExamSegmentPopup();
            }
          },1000);
    showCart();
	}
}

function creditNeoCash() {
	request = {'amount':3000,
			'reason':'Signup'}
	
	
	$.ajax({
		url : apiBasePath + "/user/credit-neo-cash",
		type:"put",
		contentType : "application/json",
		data : JSON.stringify(request),
		success : function(response) {

		},
		error : function(response) {
			console.log(response);
		}
	});
}

$('.or-signup').click(function() {
	$('.mat-signup').trigger("click");
})

$('.show-password').click(function() {
	if ($(this).siblings('input[name=password]').attr("type") == "password") {
		$(this).siblings('input[name=password]').attr("type", "text");
	} else {
		$(this).siblings('input[name=password]').attr("type", "password");
	}
})

function userDetailsShow() {
	$('#loginBtn').hide();
	$('#signup').hide();
	if (isUserLoggedIn() == true) {
		var userName = parseJwt(getAuthToken()).name;
	}
	$('#user').html(userName.slice(0, 1).toUpperCase()).show();
}
function userDetailsHide() {
	$('#loginBtn').show();
	$('#signup').show();
	$('#user').hide();
}
function signOut_common() {
	window.localStorage.removeItem('access_token');
	eraseCookieFromAllPaths('NSAuthCookie');
	userDetailsHide();
	(function() {
		'use strict';
		var snackbarContainer = document.querySelector('.mdl-js-snackbar');
		var showToastButton = document.querySelector('#demo-show-toast');

		var data = {
			message : "You have successfully logged out"
		};
		snackbarContainer.MaterialSnackbar.showSnackbar(data);
	}());
	location.reload();
}

// subscribe to newsletter
$(function() {
	$("form[name='subscribe']").validate(
			{
				rules : {
					input_subscribe : {
						required : true,
						email : true
					}
				},
				messages : {
					input_subscribe : {
						required : "Please provide an email address",
						email : "Please provide a valid email address"
					}
				},
				showErrors : function(errorMap, errorList) {
					$("form[name='subscribe']").find("input").each(function() {
						$(this).removeClass("error");
					});
					$("#subscribeError").html("");
					if (errorList.length) {
						$("#subscribeError").html(errorList[0]['message']);
						$(errorList[0]['element']).addClass("error");
					}
				},
				submitHandler : function(form, e) {
					var subscribeInput = {
						"emailId" : $("form[name='subscribe']").find(
								".input_subscribe").val(),
						"subscriberList" : [ {
							name : "UPSC"
						} ]

					};
					$('#loader-subscribe').show();
					$.ajax({
						url : apiBasePath + "/newsletter/subscribers",
						type : "POST",
						data : JSON.stringify(subscribeInput),
						dataType : "json",
						contentType : "application/json",
						processData : false,
						success : function(response) {
							$('#loader-subscribe').hide();
							if (response.subscribe) {
								e.preventDefault();
								$("#subscribeError").css("color", "white");
								$("#subscribeError")
										.html(response.errorMessage);
							} else if (response != null) {
								$("#subscribeError").css("color", "white");
								$("#subscribeError")
										.html(response.errorMessage);
							} else {
								$('#errorSignup').html(response.errorMessage)
										.show();
							}
						},
						error : function(response) {
							e.preventDefault();
							$('#loader-subscribe').hide();
						}
					});
				}
			});
});

/* Update FullName - User Profile */

$(".fullNameChanges")
		.click(
				function(e) {
					e.preventDefault();
					var inputUpd = {
						fullName : $('#fullNameUpd').val(),
						emailId : $('#emailId').val(),
					}
					if (($('#fullNameUpd').val().length >= 4)) {
						$
								.ajax({
									method : "POST",
									url : apiBasePath
											+ "/user/updateuserfullname",
									data : JSON.stringify(inputUpd),
									dataType : "json",
									contentType : "application/json",
									processData : false,

									success : function(response) {
										$('.fullNameUpdate')
												.html(
														'<font color="green">Name Updated Successfully</font>')
												.delay(5000).hide(1);
										location.reload();
									},

									error : function(response) {
										alert("Name Update Failed");
									}
								});
					} else {
						$('.errorShowFullName')
								.html(
										'<font color="red">Your Full Name must be at least 4 characters long</font>');
					}
				});

/* Update Password - User Profile */

function validatePasswordMatch() {
	var password = document.getElementById("passwordUpd").value;
	var confirmPassword = document.getElementById("confirmPasswordUpd").value;
	if (password != confirmPassword) {
		return false;
	}
	return true;
}

$(".passwordChanges")
		.click(
				function(e) {
					e.preventDefault();
					var inputUpd = {
						password : $('#passwordUpd').val(),
						confirmPassword : $('#confirmPassword').val(),
						emailId : $('#emailId').val(),
					}
					if (validatePasswordMatch()
							&& ($('#passwordUpd').val().length >= 5)) {
						$
								.ajax({
									method : "POST",
									url : apiBasePath
											+ "/user/updateuserpassword",
									data : JSON.stringify(inputUpd),
									dataType : "json",
									contentType : "application/json",
									processData : false,

									success : function(response) {
										$('.passwordUpdate')
												.html(
														'<font color="green">Password Updated Successfully</font>')
												.delay(5000).hide(1);
										location.reload();
									},

									error : function(response) {
										alert("Password Update Failed");
									}
								});
					} else {
						if (!validatePasswordMatch())
							$('.errorShow')
									.html(
											'<font color="red">Your Passwords Must Match</font>');
						else
							$('.errorShowPassword')
									.html(
											'<font color="red">Your password must be at least 5 characters long</font>');
					}
				});

/* Add New Address Details - User Profile */
$("form[name=addressDetailsCreateForm]")
		.validate(
				{

					rules : {
						firstName : {
							required : true
						},
						lastname : {
							required : true
						},
						addressText : {
							required : true,
						},
						mobileNumber : {
							required : true,
						},
						city : {
							required : true
						},
						state : {
							required : true
						},
						pincode : {
							required : true
						}
					},
					messages : {
						firstName : {
							required : "Please provide your First Name"
						},
						lastname : {
							required : "Please provide your Last Name"
						},
						addressText : {
							required : "Please provide Your Address detail",
						},
						mobileNumber : {
							required : "Please provide your Mobile Number",
						},
						city : {
							required : "Please provide your City Name"
						},
						state : {
							required : "Please Enter Your State"
						},
						pincode : {
							required : "Please Enter Your Pincode"
						}
					},
					submitHandler : function(form, e) {
						e.preventDefault();
						var inputCreate = {
							emailId : $('#emailId').val(),
							firstName : $('#firstNameAdd').val(),
							lastname : $('#lastnameAdd').val(),
							addressText : $('#addressTextAdd').val(),
							pincode : $('#pincodeAdd').val(),
							city : $('#cityAdd').val(),
							state : $('#stateAdd').val(),
							mobileNumber : $('#mobileNumberAdd').val(),
						}

						$
								.ajax({
									method : "POST",
									url : apiBasePath
											+ "/user/createuseraddressdetail",
									data : JSON.stringify(inputCreate),
									dataType : "json",
									contentType : "application/json",
									processData : false,

									success : function(response) {
										$('.addressDetailsCreate')
												.html(
														'<font color="green">New Address Created Successfully</font>')
												.delay(5000).hide(1);
										location.reload();
									},

									error : function(response) {
										alert("New Address Create Failed");
									}
								});
					}
				});

var segmentList= '';
var userExamSegment=[];
var userMobileNumber='';
var userCity = '';

function openExamSegmentPopup() {
  var segmentDialog = document.querySelector('#examSegmentDialog');
  if(segmentDialog != null){
    if (!segmentDialog.showModal) {
      dialogPolyfill.registerDialog(segmentDialog);
    }
    segmentDialog.showModal();
    // segmentDialog.querySelector('#closeDialogButton').addEventListener('click', function () {
    //   segmentDialog.close();
    // });
  }
}
$('#saveExamSegments').on('click',function () {
  userCity=$('#searchCity').val();
  userMobileNumber=$('#userMobileNumber').val();
  $('.dialog-content').hide();
  $('#examSegmentDialog #saving').show();
  $('#loader-dialog').show();
  userExamSegment=[];
  $('.mdl-chip').each(function () {
    if($(this).hasClass('chip-selected') ==true){
      var exam =$(this).attr('data-id');
      userExamSegment.push(exam);
    }
  });
  segmentList = '';
  for (var i in userExamSegment) {
    segmentList =segmentList + userExamSegment[i] + ',';
  }
  updateExamSegments();
});
function updateExamSegments(){
  var input = {
    city : userCity,
    mobileNo : userMobileNumber,
    examSegment :segmentList
  }
  if(segmentList != "" && userCity != "" && userMobileNumber != ""){
    $
    .ajax({
      url : apiBasePath + "/user/examsegment",
      data : JSON.stringify(input),
      type : "PUT",
      contentType : "application/json",
      success : function(response) {

        $('.dialog-content').show();
        $('.dialog-content').html('<div style="width: fit-content; width: -moz-fit-content;margin: 10px auto;"><i class="material-icons" style="color:#55bf53; font-size: 100px">check_circle</i></div><div style="width: fit-content; width: -moz-fit-content;margin: 10px auto; "><h5 style="font-size: 15px;color: #494b4c;">Thank you for saving your Exam Details.It will help us serve you better.</h5></div>');
        $('#examSegmentDialog #saving').hide();
        $('#loader-dialog').hide();
        setTimeout(function(){
          var segmentDialog = document.querySelector('#examSegmentDialog');
          segmentDialog.close();
          if (window.location.href.indexOf("login") != -1) {
            window.location.href = prevPage;
          }
        },2000);

      },
      error : function(response) {
        $('#errorMessage').show();
        $('.dialog-content').show();
        $('#examSegmentDialog #saving').hide();
        $('#loader-dialog').hide();
      }
    });
  }
  else{
    if(segmentList == ""){
      $('.dialog-content').show();
      $('#examSegmentDialog #saving').hide();
      $('#loader-dialog').hide();
      $('#errorMessage').show();
      $('#errorMessage').text('Please select atleast 1 exam segment');
    }
    if(userCity ==""){
      $('.dialog-content').show();
      $('#examSegmentDialog #saving').hide();
      $('#loader-dialog').hide();
      $('#errorSearchCity').show();
    }
    if(userMobileNumber ==""){
      $('.dialog-content').show();
      $('#examSegmentDialog #saving').hide();
      $('#loader-dialog').hide();
      $('#errorUserMobileNumber').show();
    }
  }
}
$('#examSegmentDialog .mdl-chip').on('click',function () {
  $('#errorMessage').hide();
  if($(this).hasClass('chip-selected') ==true){
    $(this).removeClass('chip-selected');
  }
  else{
    $(this).addClass('chip-selected');
  }
});
$('#examSegmentDialog #searchCity').keypress(function () {
  $('#errorSearchCity').hide();
});
$('#examSegmentDialog #userMobileNumber').keypress(function () {
  $('#errorUserMobileNumber').hide();
});
$('#closeDialogButton').on('click',function () {
  if (window.location.href.indexOf("login") != -1) {
    window.location.href = prevPage;
  }
});