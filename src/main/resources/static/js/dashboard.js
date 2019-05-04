var deviceTypeDetail;
$('#dashboard-sidenav-links a, [data-value=profile]').click(function(e) {
	e.preventDefault();
	var i = $(this).attr("data-value");
	$('.dashboard').find('#' + i).show();
	$('.dashboard').find('#' + i).siblings().hide();
});
var batchId;
$('document').ready(function() {
	if (getParameterByName('goto') != null) {
		$('a[data-value=' + getParameterByName('goto') + ']').trigger("click");
	} else {
		$('.dashboard').find('#courses').show();
		$('.dashboard').find('#courses').siblings().hide();
	}
	user_role = parseJwt(getAuthToken()).role;

	var isInstructor = false;
	if (user_role != null) {
		$.each(user_role, function(index, element) {
			if (element.authority == 'ROLE_INSTRUCTOR') {
				isInstructor = true;
			}
		});
	}

	if (window.location.href.indexOf("lms") != -1) {
		batchId = getParameterByName('batch');
		if (isInstructor) {
			getTeacherUnits();
		} else {
			getCurriculum();

		}
	} else {

		if (isInstructor) {
			getTeachersSummary();
		} else {
			getUserCourses();
		}
	}

	calculateFingerprint();
})

function getUserCourses() {
	$('#loader-dashboard-courses').show();
	$('#boughtCourses').hide();
	$
			.ajax({
				url : apiBasePath + '/user/courses',
				type : "GET",
				contentType : "application/json",
				success : function(response) {
					$('#loader-dashboard-courses').hide();
					$('#boughtCourses').show();
					var htmlToShow = '';
					var courseList = '<div class="mdl-cell mdl-cell--3-col mdl-cell--4-col-tablet mdl-cell--12-col-phone mdl-card mdl-shadow--3dp"> <div class="trending-courses-card mdl-card__media"> <!-- <i class="fav material-icons">favorite_border</i> <i style="display: none;" class="unfav material-icons">favorite</i> --> <img width="100%" src="#courseImage#=h180"> <!-- <div class="courses-label">IAS Essay</div> --> </div> <div class="ns-card">  <span class="starts-on" style="float: left;margin-bottom: 8px;">Batch starts on <span class="starts-on-date"> #startDate# </span> </span><span class="starts-on"><span class=""> #remainingDays#</span> </span> #courseTitle# <p class="ns-card-p"> <span class="p-grid-1"><i>by</i> <span class="ns-card-teacher">#instructorName#</span><br />(#institute.name#)</span> </p> <div class=""> <h3 style="float: right; color: #f26722;" class="price">  #startCourse# </h3> <span class="number-students"><i class="fa fa-user"></i>&nbsp;#studentsEnrolled# Students </span> </div> </div> </div>';

					if (response != null && response.length > 0) {
						$

								.each(
										response,
										function(index, element) {

											htmlToShow = htmlToShow += courseList;
											htmlToShow = htmlToShow.replace(
													"#instructorName#",
													element.instructorName);
											htmlToShow = htmlToShow.replace(
													"#institute.name#",
													element.instituteName);
											htmlToShow = htmlToShow.replace(
													"#studentsEnrolled#",
													element.studentsEnrolled);
											htmlToShow = htmlToShow.replace(
													/#batchId#/g,
													element.courseBatchId);
											htmlToShow = htmlToShow.replace(
													"#courseImage#",
													element.courseImage);
											if (element.remainingValidity != '0') {
												htmlToShow = htmlToShow
														.replace(
																"#remainingDays#",
																element.remainingValidity
																		+ ' days left');
											} else {
												htmlToShow = htmlToShow
														.replace(
																"#remainingDays#",
																'Validity over');
											}

											if (element.status == 'ACTIVE') {
												htmlToShow = htmlToShow
														.replace(
																"#startCourse#",
																'<a href="/lms?batch='
																		+ element.courseBatchId
																		+ '">Start Course</a>');
												htmlToShow = htmlToShow
														.replace(
																"#courseTitle#",
																'<a href="/lms?batch='
																		+ element.courseBatchId
																		+ '"> <h3 class="dashboard-title-course">'
																		+ element.courseTitle
																		+ '</h3> </a>');
											} else {
												htmlToShow = htmlToShow
														.replace(
																"#startCourse#",
																'<a style="color: #666;cursor: not-allowed;">Course Inactive</a>');
												htmlToShow = htmlToShow
														.replace(
																"#courseTitle#",
																'<a> <h3 class="dashboard-title-course">'
																		+ element.courseTitle
																		+ '</h3> </a>');
											}

											htmlToShow = htmlToShow.replace(
													"#startDate#",
													element.startDate);

										})
					} else {
						$('#boughtCourses').html("<p>No courses to show</p>");
					}
					if (htmlToShow != '') {
						$('#boughtCourses').html(htmlToShow);
					} else {
						$('#boughtCourses').html("<p>No courses to show</p>");
					}

				},

				error : function(response) {
					$('#boughtCourses').show();
					$('#loader-dashboard-courses').hide();
					// alert("No Courses to display.");
					$('#boughtCourses').html("<p>No courses to show</p>")
				}
			});
}

var singleCourseSlug = '';
var inProgress = '';
var notStarted = '';
var watchedPercent = '';

function getCurriculum() {
	$('#loader-graphs').show();
	$
			.ajax({
				url : apiBasePath + '/user/batches/' + batchId + '/units',
				type : "GET",
				success : function(response) {
					$('#loading-units').hide();
					if (response != null) {
						singleCourseSlug = response.courseSlug;
						inProgress = response.inProgressPercent;
						notStarted=response.notStartedPercent;
						watchedPercent=response.watchedPercent;
						$
								.each(
										response.response,
										function(key, value) {
											var htmlToShow = '<details><summary> <span class="mdl-navigation__link"><i style="font-size: 18px; color: #808080 !important; margin: 2px; display: block; width: 20px; float: left;" class="mdl-color-text--blue-grey-400 material-icons" role="presentation">folder</i>&nbsp;&nbsp;#key#</span> </summary>  <div class="content">';
											var curriculumList = '<a onclick="playUnit(#unitId#);" data-value="#unitId#" class="#linkClass#">'
													+ '<div style=" margin-bottom: 5px; ">#unitDesc#</div>#progressBar#</a>';
											htmlToShow = htmlToShow.replace(
													/#key#/g, key);
											$
													.each(
															value,
															function(index,
																	element) {

																htmlToShow = htmlToShow
																		+ curriculumList;

																htmlToShow = htmlToShow
																		.replace(
																				/#unitId#/g,
																				element.unitId);
																htmlToShow = htmlToShow
																		.replace(
																				'#unitDesc#',
																				element.title);

																if (element.status == 'publish') {
																	htmlToShow = htmlToShow
																			.replace(
																					"#linkClass#",
																					'mdl-navigation__link');
																	if(Math.round(element.watchPercent ) >= 100){
                                    htmlToShow = htmlToShow
                                    .replace(
                                        "#progressBar#",
                                        '<div class="progress">'
                                        + '<div class="progress-bar" role="progressbar" style="width:100%; background: linear-gradient(to top, #795305, #00ff43);'
                                        + '" aria-valuemax="100">'
                                        + '</div> </div> '
                                        + '<span class="watch-status"> '
                                        + '100%'
                                        + '</span>');
																	}
																	else{
                                    htmlToShow = htmlToShow
                                    .replace(
                                        "#progressBar#",
                                        '<div class="progress">'
                                        + '<div class="progress-bar" role="progressbar" style="width: '
                                        +  Math.round(element.watchPercent ) + '%'
                                        + '" aria-valuemax="100">'
                                        + '</div> </div> '
                                        + '<span class="watch-status"> '
                                        + Math.round(element.watchPercent )+ '%'
                                        + '</span>');
																	}


																}
															});
											htmlToShow = htmlToShow
													+ '</div></details>';
											$('#courseCurriculum').append(
													htmlToShow).show();
											htmlToShow = '';
										});

					}
          $('#loader-graphs').hide();
          google.charts.setOnLoadCallback(drawChart);
					$(".graphs").show();

				},

				error : function(response) {
					$('#loading-units').hide();
					console.log(response);
				}
			});
}

function getTeacherUnits() {
	$
			.ajax({
				url : apiBasePath + '/admin_instructor/batches/' + batchId
						+ '/units',
				type : "GET",
				success : function(response) {
					$('#loading-units').hide();
					if (response != null) {
						singleCourseSlug = response.courseSlug;
						$
								.each(
										response.response,
										function(key, value) {
											var htmlToShow = '<details><summary> <span class="mdl-navigation__link"><i style="font-size: 18px; color: #808080 !important; margin: 2px; display: block; width: 20px; float: left;" class="mdl-color-text--blue-grey-400 material-icons" role="presentation">folder</i>&nbsp;&nbsp;#key#</span> </summary>  <div class="content">';
											var curriculumList = '<a onclick="playTeacherUnit(#unitId#);" data-value="#unitId#" class="#linkClass#"><i class="mdl-color-text--blue-grey-400 material-icons" role="presentation" style="font-size: 18px;color: #colorcode# !important;">#uniticon#</i>&nbsp;&nbsp;#unitDesc#</a>';
											htmlToShow = htmlToShow.replace(
													/#key#/g, key);
											$
													.each(
															value,
															function(index,
																	element) {

																htmlToShow = htmlToShow
																		+ curriculumList;

																htmlToShow = htmlToShow
																		.replace(
																				/#unitId#/g,
																				element.unitId);
																htmlToShow = htmlToShow
																		.replace(
																				'#unitDesc#',
																				element.title);

																htmlToShow = htmlToShow
																		.replace(
																				"#uniticon#",
																				"check_circle");
																htmlToShow = htmlToShow
																		.replace(
																				"#linkClass#",
																				'mdl-navigation__link');
																htmlToShow = htmlToShow
																		.replace(
																				"#colorcode#",
																				'#3fbf53');
															});
											htmlToShow = htmlToShow
													+ '</div></details>';
											$('#courseCurriculum').append(
													htmlToShow).show();
											htmlToShow = '';
										});

					}

				},

				error : function(response) {
					$('#loading-units').hide();
					console.log(response);
				}
			});

}

function playTeacherUnit(unitId) {
	$
			.ajax({
				async : false,
				type : 'GET',
				dataType : 'json',
				url : apiBasePath + "/admin_instructor/units/" + unitId,
				success : function(response) {
					$('#loader-lms').hide();
					if (response.assignment != null) {
						var htmlbody = '<div class="mdl-shadow--2dp" style="padding: 16px 24px; background: #fff;">'
								+ response.assignment.description
								+ '</div> <div class="container file-attachment"><h6>Please attach the completed assignment below</h6><input type="file" id="attachment" name="files[]" multiple /><a onclick="uploadFile('
								+ unitId
								+ ');" id="userUploadAssignmentUpload" class="btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">Upload </a><br/><label id="attachment-error" class="error float-left text-left"></label></div>';
					} else if (response.lectureScript != null) {
						if (response.lecture.footerNotes != null) {
							var htmlbody = '<div id="'
									+ unitId
									+ '" class=""><div style="text-align:center;"></div><div class="mdl-grid"> <div class="mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"> <h4 style="margin: 0 0 8px 0;">'
									+ response.lecture.heading + '</h4>'
									+ '<div id="myplayer' + unitId + '"></div>'
									+ response.lectureScript + '<div>'
									+ response.lecture.footerNotes
									+ '</div></div> </div></div>'

						} else {
							var htmlbody = '<div id="'
									+ unitId
									+ '" class=""><div style="text-align:center;"></div><div class="mdl-grid"> <div class="mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"> '
									+ '<div class="mdl-grid"><div class ="mdl-cell mdl-cell--10-col mdl-cell--12-col-tablet mdl-cell--12-col-phone">'
									+ '<h4 style="margin: 0 0 8px 0;"> '
									+ response.lecture.heading
									+ '</h4>'
									+ '</div>'
									+ '<div class ="mdl-cell mdl-cell--2-col mdl-cell--12-col-tablet mdl-cell--12-col-phone">'
									+ '<span style="font-size: 18px;">No. of Views : &nbsp;'
									+ response.lecture.noOfViews
									+ '&nbsp;</span></div></div>'
									+ '<div id="myplayer' + unitId + '"></div>'
									+ response.lectureScript + '<div>'
									+ '</div></div> </div></div>'

						}
					} else if (response.note != null) {
						var htmlbody = '<div class="mdl-shadow--2dp" style="padding: 16px 24px; background: #fff;">'
								+ response.note.description + '</div>';
					}

					else {
						var htmlbody = '<p> Please try again after sometime</p>';
					}
					$('#unitPlay')
							.html(
									'<a class="go-back-to-dashboard" href="user-dashboard" style="margin: 4px 0 10px 0px; display: inline-block; color: #ff6600;">View my Courses</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a class="go-back-to-dashboard" href="/course/'
											+ singleCourseSlug
											+ '" style="margin: 4px 0 10px 0px; display: inline-block; color: #ff6600;">View course details</a>'
											+ htmlbody
											+ '<a class="lms-continue btn-all-courses btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent btn-View-all-teachers " onclick="'
											+ "$('.mdl-layout__drawer').addClass('is-visible');"
											+ '">View all units</a>').show();
					console.log(response);
          var intervalId = setInterval(function(){
            if ($('.jw-logo-bottom-left').length < 1 || $('.jw-logo-bottom-left').css("display") != "block"){
              jwplayer().remove();
              if($('.jwplayer').length < 1){
                clearInterval(intervalId);
              }
            }
          }, 1000);
				},

				error : function(response) {

				}
			});
}

function playUnit(unitId) {
	clearInterval(x);
	htmlToShow = '';
	$('#loader-lms').show();
	$("#unit-drawer").removeClass("is-visible");
	$('#unitPlay').html('').hide();

	var unitRequest = {
		"unitID" : unitId,
		"oldFingerprint" : getCookie('NSDeviceFP'),
		"newFingerprint" : fpValue,
		"fingerprintInfo" : fpValueDetails,
		"deviceType" : deviceTypeDetail
	};

	$
			.ajax({
				url : apiBasePath + '/admin_techops_student/units/' + unitId,
				type : "POST",
				processData : false,
				data : JSON.stringify(unitRequest),
				dataType : "json",
				contentType : "application/json",

				success : function(response) {

					$('#loader-lms').hide();
					if (response.assignment != null) {
						var htmlbody = '<div class="mdl-shadow--2dp" style="padding: 16px 24px; background: #fff;">'
								+ response.assignment.description
								+ '</div> <div class="container file-attachment"><h6>Please attach the completed assignment below</h6><input type="file" id="attachment" name="files[]" multiple /><a onclick="uploadFile('
								+ unitId
								+ ');" id="" class="btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">Upload </a><br/><label id="attachment-error" class="error float-left text-left"></label></div>';
					} else if (response.lectureScript != null) {
						if (response.lectureScript != 'generateOTP') {
								var htmlbody = '<div id="'
										+ unitId 	+ '" class=""><div style="text-align:center;"></div><div class="mdl-grid"> <div class="mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"> '
										+ '<div class="mdl-grid"><div class ="mdl-cell mdl-cell--9-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" id = "lectureSceduleDisplayed">'
										+ '<h4 style="margin: 0 0 8px 0;"> '
										+ response.lecture.heading
										+ '</h4>'
										+ '</div><div class ="mdl-cell mdl-cell--2-col mdl-cell--12-col-tablet mdl-cell--12-col-phone">'
										+ '<span style="font-size: 18px;">No. of Views : &nbsp;'
										+ response.lecture.noOfViews
										+ '&nbsp;</span></div>'
                    + '<div class ="mdl-cell mdl-cell--1-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" id = "sendEnquiryBtn" style="text-align: center; display: none;">'
                    + '<img src = "https://lh3.googleusercontent.com/_Ki1nTZT8Uefo-ANAEqazRMyLAxrBOT3eca2vs9odyNt0ZlElBh2px00SHqTbgjd1Rot7wAl42UhSBBGPeyKjeG2b1w=h30" id = "askQuestion" style="cursor: pointer;" onclick="sendTeacherQuestions('+unitId+');"/></div>'
										+ '<div class="mdl-tooltip mdl-tooltip--top mdl-tooltip--large" data-mdl-for="askQuestion">Have a question? <br/> Ask Now!!! </div>'
										+ '</div>'
										+ '<div id="myplayer' + unitId
										+ '"></div>' + response.lectureScript;

							if (response.lecture.footerNotes != null) {
								 htmlbody += '<div>' + response.lecture.footerNotes + '</div>';
							}

							htmlbody += '</div> </div></div>';
							document.cookie = 'NSDeviceFP=' + fpValue;
							count=0;
						} else {

							$.ajax({
								"async" : true,
								"url" : apiBasePath
										+ "/student_admin/generate-otp",
								"type" : "GET",
								"processData" : false,
								"dataType" : "text",
								"contentType" : "application/json",

								success : function(response) {
                  myOtpTimer();
                  $('#userPhoneNumber').text(response);
									var dialog = document
											.querySelector('#OTPDialog');
									var showDialogButton = document
											.querySelector('#show-dialog');
									if (!dialog.showModal) {
										dialogPolyfill.registerDialog(dialog);
									}
									dialog.showModal();

									dialog.querySelector('.close-dialog')
											.addEventListener('click',
													function() {
														dialog.close();
													});

								},
								error : function(response) {
									alert("Inside Error block");
								}
							});

						}

					} else if (response.note != null) {
						var htmlbody = '<div class="mdl-shadow--2dp" style="padding: 16px 24px; background: #fff;">'
								+ response.note.description + '</div>';
					} else if (response.quiz != null) {
						var quizId = response.quiz.quiz.quizId;
						var quizId = response.quiz.quiz.quizId;
						var htmlbody = '';
						if (response.quiz.questionMap != null) {
							var index = 1;
							$.each(response.quiz.questionMap, function(key,
									valueMap) {

								htmlToShow = htmlToShow
										+ '<h4 class="mcqs-topic">' + key
										+ '</h4>';
								fetchQuestionList(valueMap, key);
							})
						} else {
							fetchQuestionList(response.quiz.questionList, null);
						}
						htmlbody = "<div class='demo-ribbon-mcqs'><h3>"
								+ response.quiz.quiz.title
								+ "</h3></div><div class='mdl-shadow--2dp' style='background: #fff;max-width: 800px; margin: -80px auto 40px auto;'><div style='display: none;' class='scoreCard mdl-grid'><div class='mdl-cell mdl-cell--6-col mdl-cell--12-col-tablet mdl-cell--12-col-phone'><p id='marksObtained'></p><p id='noOfAttemptQuestion'></p><p id='noOfCorrectQuestion'></p></div><div class='mdl-cell mdl-cell--6-col mdl-cell--12-col-tablet mdl-cell--12-col-phone'><p id='noOfIncorrectQuestion'></p><p id='noOfSkippedQuestion'></p><p id='totalNoOfQuestion'></p></div><div class='mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone'><a style='display: none;' onclick='$(this).siblings("
								+ '"'
								+ ".make-table-responsive"
								+ '"'
								+ ").toggle();' id='showDetailedScore'>View Detailed Score</a><div class='make-table-responsive' style='display: none;overflow-x: auto;'><table id='detailedScore' class=''></table></div></div></div><div id=''><div id='instructions'>"
								+ response.quiz.quiz.description
								+ "</div><a onclick='$(this).parent().hide().siblings("
								+ '"'
								+ "#showQuestions"
								+ '"'
								+ ").show();countdownTimer("
								+ response.quiz.quiz.timeLimit * 60
								+ ");' class='btn-explore btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent' style='margin: 16px 0;'> Continue</a></div><div style='display: none;' id='showQuestions'>"
								+ htmlToShow
								+ '<a onclick="submitQuiz('
								+ quizId
								+ ','
								+ unitId
								+ ')" id="" class="btn-quiz btn-explore btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" style="margin: 16px 0;"> Submit</a><a class="retake-quiz btn-explore btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" style="margin: 16px 0;display: none;" onclick="playUnit('
								+ unitId
								+ ')"> Re-Take</a><div style="display: none;text-align: left; margin: 16px;" class="common-loader" id="loader-quiz"> <div class="mdl-spinner mdl-js-spinner is-active"></div></div></div></div>';
					} else {
						var htmlbody = '<p> Please try again after sometime</p>';
					}
					if (response.quiz != null) {
						$('#unitPlay')
								.html(
										'<a class="go-back-to-dashboard" href="user-dashboard" style="margin: 4px 0 10px 0px; display: inline-block; color: #ff6600;">View my Courses</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a class="go-back-to-dashboard" href="/course/'
												+ singleCourseSlug
												+ '" style="margin: 4px 0 10px 0px; display: inline-block; color: #ff6600;">View course details</a><span id="countdownTimer" class="float-right"></span>'
												+ htmlbody
												+ '<a class="lms-continue btn-all-courses btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent btn-View-all-teachers " onclick="'
												+ "$('.mdl-layout__drawer').addClass('is-visible');"
												+ '">View all units</a>')
								.show();
					} else {
						$('#unitPlay')
								.html(
										'<a class="go-back-to-dashboard" href="user-dashboard" style="margin: 4px 0 10px 0px; display: inline-block; color: #ff6600;">View my Courses</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a class="go-back-to-dashboard" href="/course/'
												+ singleCourseSlug
												+ '" style="margin: 4px 0 10px 0px; display: inline-block; color: #ff6600;">View course details</a>'
												+ htmlbody
												+ '<a class="lms-continue btn-all-courses btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent btn-View-all-teachers " onclick="'
												+ "$('.mdl-layout__drawer').addClass('is-visible');"
												+ '">View all units</a>')
								.show();
					}

					componentHandler.upgradeDom();
					console.log(response);
          if(response.enableQueryButton == true){
            $('#sendEnquiryBtn').show();
          }
          else {
          	$('#lectureSceduleDisplayed').removeClass('mdl-cell--9-col').addClass('mdl-cell--10-col');
            $('#sendEnquiryBtn').hide();
          }
          var intervalId = setInterval(function(){
            if ($('.jw-logo-bottom-left').length < 1 || $('.jw-logo-bottom-left').css("display") != "block"){
              jwplayer().remove();
              if($('.jwplayer').length < 1){
                clearInterval(intervalId);
              }
            }
          }, 1000);

				},

				error : function(response) {
					$('#loader-lms').hide();
					$('#unitPlay').html("Something went wrong").show();

				}
			});
}
function regenerateOTP() {
  $.ajax({
    "async" : true,
    "url" : apiBasePath
        + "/student_admin/generate-otp",
    "type" : "GET",
    "processData" : false,
    "dataType" : "text",
    "contentType" : "application/json",

    success : function(response) {
      document.getElementById("resendOTP").disabled = true;
      $('#resendOTP').css("color", "#00000054");
      myOtpTimer();
    },
    error : function(response) {
      alert("Inside Error block");
    }
  })
}

var htmlToShow = '';

function fetchQuestionList(mapOrList, topicName) {

	index = 1;
	var completeQuestion = '<div class="mcq-question-list">#completeQuestion##figure##completeOptionsInQuestion##options##requestObject#</div>';
	$
			.each(
					mapOrList,
					function(key, value) {
						var figure = '';
						var questionList = '';
						var optionsInQuestionList = '';
						var options = '';
						var questionNumber = index;
						htmlToShow = htmlToShow + completeQuestion;
						$.each(JSON.parse(value.questionText), function(key,
								optionValueofQuestion) {
							if (key == 0) {
								questionList = "<p style='font-size: 16px;'>"
										+ questionList + "<b>" + questionNumber
										+ "</b>" + ". " + optionValueofQuestion
										+ "</p>";
							} else {
								optionsInQuestionList = "<p>"
										+ optionsInQuestionList
										+ optionValueofQuestion + "</p>";
							}
						})
						$
								.each(
										JSON.parse(value.optionJson),
										function(key, optionValue) {
											options = options
													+ '<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="'
													+ +questionNumber
													+ '_'
													+ key
													+ '"> <input onchange="checkboxAsRadio(this);" type="checkbox" id="'
													+ questionNumber
													+ '_'
													+ key
													+ '" class="mdl-checkbox__input" name="answer_'
													+ questionNumber
													+ '" value="'
													+ optionValue
													+ '"> <span data-option-value="'
													+ optionValue
													+ '" style="font-size: 14px;" class="mdl-checkbox__label">'
													+ "<span class='question-number'>"
													+ (String
															.fromCharCode(key + 65))
													+ ")</span>&nbsp;&nbsp;"
													+ optionValue
													+ '</span> </label>';
										})
						$.each(JSON.parse(value.figureJson), function(key,
								image) {
							figure = figure + "<img src='" + image + "'>";
						})
						htmlToShow = htmlToShow.replace("#completeQuestion#",
								questionList);
						htmlToShow = htmlToShow.replace("#options#", options);
						htmlToShow = htmlToShow.replace(
								"#completeOptionsInQuestion#",
								optionsInQuestionList);
						htmlToShow = htmlToShow
								.replace("#figure#",
										"<div class='figure-mcqs'>" + figure
												+ "</div>");
						htmlToShow = htmlToShow
								.replace(
										"#requestObject#",
										'<input type="hidden" name="questionId" value="'
												+ value.questionId
												+ '"><span class="explanation"></span>');


						index++;


					})
}

function submitQuiz(quizId, unitId) {
	$('#loader-quiz').show();
	$('.btn-quiz').hide();
	$('.scoreCard').show();
	var quizSubmissionList = [];
	$('.mcq-question-list').each(function() {
		var questionId = $(this).find('input[name="questionId"]').val();
		if ($(this).find('.mdl-checkbox__input:checked').length > 0) {
			var answer = $(this).find('.mdl-checkbox__input:checked').val();
			quizSubmissionList.push({
				attempt : true,
				answer : answer,
				questionId : questionId
			})
		} else {
			quizSubmissionList.push({
				attempt : false,
				questionId : questionId
			})
		}
	})
	$
			.ajax({
				url : apiBasePath + "/user/unit/" + unitId + "/quizsubmission/"
						+ quizId + "?timetaken="+$('#countdownTimer').text(),
				type : "PUT",
				contentType : "application/json",
				data : JSON.stringify(quizSubmissionList),
				success : function(response) {
					$
							.each(
									response.solution,
									function(key, solution) {
										var questionExplanation = '';
										var questionid_solution = solution.question.questionId;
										$
												.each(
														JSON
																.parse(solution.explanation),
														function(key,
																explanation) {
															questionExplanation = questionExplanation
																	+ "<p style='color: #003362;font-weight: 600;'>"
																	+ explanation
																	+ "</p>";
														})
										$('.mcq-question-list').find(
												'input[value="'
														+ questionid_solution
														+ '"]').siblings(
												".explanation").html(
												questionExplanation);
										if (solution.userAnswer == solution.answer) {
											$('.mcq-question-list')
													.find(
															'input[value="'
																	+ questionid_solution
																	+ '"]')
													.siblings("label")
													.find(
															"span[data-option-value='"
																	+ solution.answer
																	+ "']")
													.css({"color": "green","font-weight": "bold"});
										} else {
											$('.mcq-question-list')
													.find(
															'input[value="'
																	+ questionid_solution
																	+ '"]')
													.siblings("label")
													.find(
															"span[data-option-value='"
																	+ solution.answer
																	+ "']")
													.css({"color": "green","font-weight": "bold"});
											$('.mcq-question-list')
													.find(
															'input[value="'
																	+ questionid_solution
																	+ '"]')
													.siblings("label")
													.find(
															"span[data-option-value='"
																	+ solution.userAnswer
																	+ "']")
													.css("color", "red");
										}

									});
					$('#marksObtained').html(
							'Marks Obtained: <b>'
									+ response.sectionLevel.marksObtained
									+ '</b>');
					$('#noOfAttemptQuestion').html(
							'Questions Attempted: <b>'
									+ response.sectionLevel.noOfAttemptQuestion
									+ '</b>');
					$('#noOfCorrectQuestion').html(
							'Correct Answers: <b>'
									+ response.sectionLevel.noOfCorrectQuestion
									+ '</b>');
					$('#noOfIncorrectQuestion')
							.html(
									'Incorrect Answers: <b>'
											+ response.sectionLevel.noOfIncorrectQuestion
											+ '</b>');
					$('#noOfSkippedQuestion').html(
							'Questions Skipped: <b>'
									+ response.sectionLevel.noOfSkippedQuestion
									+ '</b>');
					$('#totalNoOfQuestion').html(
							'Total Questions: <b>'
									+ response.sectionLevel.totalNoOfQuestion
									+ '</b>');
					$('.scoreCard').show();
					$('#loader-quiz').hide();
					$('.retake-quiz').show();
					$('.mcq-question-list input').attr("disabled");
					$(".mdl-layout__content").animate({
						scrollTop : 0
					}, 400);
					$('#countdownTimer').hide();
					if (response.topicLevel != null) {
						$('#showDetailedScore').show();
						var detailedScore = '<thead> <tr><th>Topic</th><th>Marks Obtained</th><th>Questions Attempted</th><th>Correct</th><th>Incorrect</th><th>Skipped</th><th>Total</th></tr></thead> <tbody> #tr# </tbody>';
						var tr = '';
						$.each(response.topicLevel, function(key,
								topicLevelScore) {
							tr = tr + '<tr><td>' + key + '</td><td>'
									+ topicLevelScore.marksObtained
									+ '</td><td>'
									+ topicLevelScore.noOfAttemptQuestion
									+ '</td><td>'
									+ topicLevelScore.noOfCorrectQuestion
									+ '</td><td>'
									+ topicLevelScore.noOfIncorrectQuestion
									+ '</td><td>'
									+ topicLevelScore.noOfSkippedQuestion
									+ '</td><td>'
									+ topicLevelScore.totalNoOfQuestion
									+ '</td></tr>';
						});
						detailedScore = detailedScore.replace("#tr#", tr);
						$('#detailedScore').html(detailedScore);
					}
				},
				error : function(response) {
					$('.scoreCard').show();
					$('#loader-quiz').hide();
					$('.btn-quiz').show();
				}
			});
}

$('#btnSubmitOTP').on("click", function() {

	// stop submit the form, we will post it manually.
	// event.preventDefault();
	var unitRequest = {
		"oldFingerprint" : getCookie('NSDeviceFP'),
		"newFingerprint" : fpValue,
		"fingerprintInfo" : fpValueDetails,
		"deviceType" : deviceTypeDetail
	};
	var validateRequest = {};
	validateRequest["otpnum"] = $('#txtOTP').val();
	validateRequest["unitRequest"] = unitRequest;
	$.ajax({
		async : true,
		url : apiBasePath + "/student_admin/validate-otp",
		type : "POST",
		contentType : "application/json",
		data : JSON.stringify(validateRequest),
		dataType : "text",
		success : function(response) {
			if (response == "SUCCESS") {
				alert("OTP is Valid.");
				location.reload();
			} else {
				alert("OTP is invalid");
			}

		},
		error : function(response) {
			alert("Some error occured while submitting OTP");
		}
	});

})

function uploadFile(unitId) {
	$('#attachment-error').html("");
	var formData = new FormData();
	formData.append('file', $('#attachment')[0].files[0]);
	formData.append('type', 'assignment');
	formData.append('contentType', $('#attachment')[0].files[0].type);
	$.ajax({
		"async" : true,
		"crossDomain" : true,
		"url" : apiBasePath + "/user/uploadfile",
		"method" : "POST",
		"processData" : false,
		"contentType" : false,
		"mimeType" : "multipart/form-data",
		"data" : formData,

		success : function(response) {
			if (response != null) {
				var input = {
					unitId : unitId,
					url : response,
					name : $('#attachment')[0].files[0].name,
					type : $('#attachment')[0].files[0].type,
				}

				saveUserAssignment(input);

			} else {
				$('#attachment-error').html("Failed while uploading image")
						.removeClass("text-success");
			}

		},
		error : function(response) {
			$('#attachment-error').html("Failed while uploading image")
					.removeClass("text-success");
		}
	});
}

function saveUserAssignment(input) {

	$.ajax({
		url : apiBasePath + "/user/assignmentsubmission",
		type : "PATCH",
		data : JSON.stringify(input),
		contentType : "application/json",
		processData : false,

		success : function(response) {
			$('#attachment-error').html("File Upload Successful").addClass(
					"text-success");

		},

		error : function(response) {
			$('#attachment-error').html("Failed while uploading image")
					.removeClass("text-success");
		}
	});
}
function sendNewMessageToStudent(el) {
  $('.emptyMessage').hide();
  var enctype = '';
  if ($(el).siblings('textarea').val() != 0) {
    $(el).addClass('btn-disabled');
    $(el).siblings('.emptyMessage').hide();
      $('.show-dialog-form').show();
      $('.message-has-been-sent').hide();
      var obj = {
        "messageTo" : $(el).siblings('textarea').attr("data-id"),
        "body" : $(el).siblings('textarea').val(),
      };

    $.ajax({
      url : apiBasePath + '/user/messages',
      type : "POST",
      data : JSON.stringify(obj),
      contentType : "application/json",
      processData : false,
      dataType : "json",
      enctype : enctype,
      success : function(response) {
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
function getTeachersCourses() {
  $('.demo-drawer').removeClass("is-visible");
  $('#loader-dashboard-courses').show();
  $('#boughtCourses').hide();
  $('#summaryContainer').hide();
  $('#students').hide();
  $('#earnings').hide();
  $('#messages').hide();

  $
  .ajax({
    url : apiBasePath + '/instructor/teachers/courses',
    type : "GET",
    contentType : "application/json",
    processData : false,
    dataType : "json",
    success : function(response) {
      $('#loader-dashboard-courses').hide();
      $('#boughtCourses').show();
      var htmlToShow = '';
      var courseList = '<div class="mdl-cell mdl-cell--3-col mdl-cell--6-col-tablet mdl-cell--12-col-phone mdl-card mdl-shadow--3dp"> <div class="trending-courses-card mdl-card__media"> <!-- <i class="fav material-icons">favorite_border</i> <i style="display: none;" class="unfav material-icons">favorite</i> --> <img src="#courseImage#" style="width: inherit;"> <!-- <div class="courses-label">IAS Essay</div> --> </div> <div class="ns-card"> <span class="course-rating"><i class="material-icons">star_rate</i> <i class="material-icons">star_rate</i> <i class="material-icons">star_rate</i> <i class="material-icons">star_rate</i></span> <span class="starts-on">Starts on <span class="starts-on-date">#startDate#</span> </span> <a href="/lms?batch=#batchId#"> <h3>#courseTitle#</h3> </a> <p class="ns-card-p"> <span class="p-grid-1"><i>by</i> <span class="ns-card-teacher">#instructorName#</span><br />(#institute.name#)</span> </p> <div class=""> <h3 style="float: right; color: #f26722;" class="price">  <a href="/lms?batch=#batchId#">Start Course</a> </h3> <span class="number-students"><i class="fa fa-user"></i>&nbsp;#studentsEnrolled# Students </span> </div> </div> </div>';

      if (response != null) {
        $

        .each(response.courseBatches, function(index, element) {

          htmlToShow = htmlToShow += courseList;
          htmlToShow = htmlToShow.replace("#courseTitle#",
              element.courseTitle);
          htmlToShow = htmlToShow.replace("#instructorName#",
              element.instructorName);
          htmlToShow = htmlToShow.replace("#institute.name#",
              element.instituteName);
          htmlToShow = htmlToShow.replace(
              "#studentsEnrolled#",
              element.studentsEnrolled);
          htmlToShow = htmlToShow.replace(/#batchId#/g,
              element.courseBatchId);
          htmlToShow = htmlToShow.replace("#courseImage#",
              element.courseImage);
          htmlToShow = htmlToShow.replace("#startDate#",
              element.startDate);

        })
      } else {
        $('#boughtCourses').html("<p>No courses to show</p>");
      }
      if (htmlToShow != '') {
        $('#boughtCourses').html(htmlToShow);
      } else {
        $('#boughtCourses').html("<p>No courses to show</p>");
      }

    },

    error : function(response) {
      $('#boughtCourses').html("<p>No courses to show</p>");
    }
  });
}

function getTeacherStudents() {
  $('.demo-drawer').removeClass("is-visible");
  $('#studentTbleToDisplay').hide();
  $(this).addClass("is-clicked").siblings().removeClass("is-clicked")
  $('#loader-dashboard-students').show();
  $('#studentstbl').html('').hide();
  $('#boughtCourses').hide();
  $('#summaryContainer').hide();
  $('#students').hide();
  $('#earnings').hide();
  $('#messages').hide();
  $.ajax({
    url: apiBasePath + '/instructor/teachers/students',
    type: "GET",
    contentType: "application/json",
    processData: false,
    dataType: "json",
    success: function (response) {
      /*Teacher students starts here*/
      $('#students').show();


      var batchRow='<div class="mdl-shadow--2dp" style="padding: 16px;margin: 32px 0;"><h5 class ="bactchName">#batchName#</h5><table class="mdl-data-table studentTbleToDisplay" cellspacing="0"width="100%" id="studentTbleToDisplay" ><thead><tr><th>Student ID</th><th>Student Name</th><td>student Address</td><th>Email ID</th><th>Enroll Date</th><th>Mobile Number</th><th>Contribution</th><th>Send Message</th></tr></thead><tbody >#tableRows#</tbody></table></div>';
      var displayStudentTable='';


      $('#loader-dashboard-students').hide();
      $.each(response.studentsMap, function(key, value) {
        var studentList = '<tr><td>#studentID#</td><td>#studentName#</td><td width="861px">#studentAddress#</td><td>#studentEmail#</td><td>#enrolledOn#</td><td align ="center"><span>#contactNo#</span></td><td>#contribution#</td><td><button id="#studentEmail#" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" style="background: #ffffff; border-radius: 3px; border: 1px solid #000000; color: #000000; box-shadow: none !important;" onclick="showSendEnquiryDialog(\'#studentEmail#\');">Message</button></td></tr>';
        var htmlToShowStudents = '';
        if (value.length > 0) {
          displayStudentTable = displayStudentTable + batchRow;
          displayStudentTable = displayStudentTable.replace("#batchName#", key);
        }
        $.each(value, function(index, element) {
          htmlToShowStudents = htmlToShowStudents + studentList;
          htmlToShowStudents=htmlToShowStudents.replace('#studentID#',element.student.userId);
          htmlToShowStudents = htmlToShowStudents.replace("#studentName#",
              element.student.fullName);
          htmlToShowStudents = htmlToShowStudents.replace("#studentAddress#",
              element.address);
          htmlToShowStudents = htmlToShowStudents.replace(/#studentEmail#/g,
              element.student.emailId);
          var contribution = element.contribution;
          var studentContibution= contribution.toString().split(".")[0];
          htmlToShowStudents = htmlToShowStudents.replace("#contribution#",studentContibution);
          if (element.student.mobileNumber != null
              && element.student.mobileNumber != '') {
            htmlToShowStudents = htmlToShowStudents.replace("#contactNo#",
                element.student.mobileNumber);
          } else {
            htmlToShowStudents = htmlToShowStudents.replace("#contactNo#",
                "Not Available");
          }
          var time = new Date(element.enrolledOn),
					enrollDate = time.getDate();
          if(enrollDate <10){
          	enrollDate = "0"+enrollDate;
					}
          var month = new Array();
          month[0] = "01";
          month[1] = "02";
          month[2] = "03";
          month[3] = "04";
          month[4] = "05";
          month[5] = "06";
          month[6] = "07";
          month[7] = "08";
          month[8] = "09";
          month[9] = "10";
          month[10] = "11";
          month[11] = "12";

          enrollMonth = month[time.getMonth()];
          enrollYeary = time.getFullYear();
          htmlToShowStudents = htmlToShowStudents.replace("#enrolledOn#", enrollDate
              + '/'+ enrollMonth + '/' + enrollYeary);
        });
        displayStudentTable = displayStudentTable.replace('#tableRows#',htmlToShowStudents);
      });

      $('.studentTbleToDisplay').show();
      $('#studentstbl').html(displayStudentTable).show();
      // var table = $('.studentTbleToDisplay').DataTable();
      // table.order([ 6, 'desc' ]).draw();
      var table = $('.studentTbleToDisplay').DataTable( {
        columnDefs: [
          {
            targets: [ 0, 1, 2 ],
            className: 'mdl-data-table__cell--non-numeric'
          }
        ]
      } );
      table.order([ 5, 'desc' ]).draw();

    },

    error: function (response) {
      $('#loader-dashboard-students').hide();
    }
  });
}

function getTeacherEarnings() {
  $('.demo-drawer').removeClass("is-visible");
  $('#boughtCourses').hide();
  $('#summaryContainer').hide();
  $('#students').hide();
  $('#earnings').hide();
  $('#messages').hide();

  $('#earningsTbl').html('').hide();
  $('#loader-dashboard-earnings').show();
  var htmlToShowTable = '';
  var displayTeacherEarnings='';
  var htmlToShowEarnings = '<div class="mdl-shadow--2dp" style="padding: 16px;"><h5>#month#</h5> <table class="mdl-data-table earningTble" style="border-collapse: collapse"><thead><tr><th>Batch ID</th><th>Course Name</th><th>Earning</th></tr></thead><tbody>#earningsRows#</tbody> </table></div>';
  $.ajax({
    url : apiBasePath + '/instructor/teachers/earnings',
    type : "GET",
    contentType : "application/json",
    processData : false,
    dataType : "json",
    success : function(response) {
      $('#earnings').show();
      $('#loader-dashboard-earnings').hide();
      console.log(response.earningsMap);
      $.each(response.earningsMap, function(key, value) {
        var htmlToShowRow = '';
        var teacherEarningsToShow = '<tr><td>#batchId#</td><td width="1000px">#course#</td><td>#earnings#</td></tr>';
        displayTeacherEarnings = displayTeacherEarnings + htmlToShowEarnings;
        displayTeacherEarnings = displayTeacherEarnings.replace("#month#", key);
        $.each(value, function(index, element) {
          htmlToShowRow = htmlToShowRow + teacherEarningsToShow;
          htmlToShowRow = htmlToShowRow.replace("#course#", element.name);
          htmlToShowRow = htmlToShowRow.replace("#batchId#",
              element.batchId);
          var commission = element.commission;
          var teacherEarning = commission.toString().split(".")[0];
          htmlToShowRow = htmlToShowRow.replace("#earnings#",
              teacherEarning);
        });
        displayTeacherEarnings = displayTeacherEarnings.replace('#earningsRows#',htmlToShowRow);
      });
      $('#earningsTbl').html(displayTeacherEarnings).show();
      $('.earningTble').DataTable( {
        columnDefs: [
          {
            targets: [ 0, 1, 2 ],
            className: 'mdl-data-table__cell--non-numeric'
          }
        ]
      } );
    },

    error : function(response) {
      $('#loader-dashboard-earnings').hide();

    }
  });
}

function getAllMessageThreads() {
  $('.demo-drawer').removeClass("is-visible");
  $('#boughtCourses').hide();
  $('#summaryContainer').hide();
  $('#students').hide();
  $('#earnings').hide();
  $('#messages > div').hide();
  $('#messages > p').hide();
  $('#loader-dashboard-messages').show();
  $('.emptyMessage').hide();
  var htmlToShow = '';
  var htmlForMessages = '<h5><a class="backToMessages" style="cursor: pointer;" onclick="backToMessages(this);">&lt;</a>&nbsp;&nbsp;#ThreadName#</h5>';
  var messageBeg = '<div class="mdl-tabs__panel #activeClass#" id="message_#id#"><ul>';
  var threadList = '<a data-name="#ThreadNameKey#" data-id="message_#id#" class="mdl-tabs__tab #activeClass#" onclick="switchTabs(this);">#ThreadName#</a>';
  var messageList = '<li class="#senderOrReceiver#"><div><b>#senderOrReceiverName#</b>#messageText#</div></li>';
  var messageEnd = '</ul><textarea onchange="$(this).siblings('
      + "'"
      + '.emptyMessage'
      + "'"
      + ').hide();" data-name="#ThreadNameKey#" data-id="message_#id#" placeholder="Write a message or attach a file.." rows="4" class="messageText"></textarea><label style="display: none;" class="emptyMessage error">Please enter a message</label>'
      + '<button onclick="sendNewMessage(this);" class="send-message mat-signup mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent">Send</button></div>';
  $.ajax({
    url : apiBasePath + '/user/messages',
    type : "GET",
    contentType : "application/json",
    processData : false,
    dataType : "json",
    success : function(response) {
      $('#messages').show();
      if (response.messagesMap != null) {
        $('#messages > div').show();
        $('#messages > p').hide();
        $('#loader-dashboard-messages').hide();
        var i = 0;
				if(response.notificationsMap["NeoStencil Notifications"] != null){
          $.each(response.notificationsMap, function(key, value) {
            htmlToShow = htmlToShow + threadList;
            htmlToShow = htmlToShow.replace(/#id#/g, i);
            if (i == 0) {
              htmlToShow = htmlToShow.replace(/#activeClass#/g,
                  'is-active');
            } else {
              htmlToShow = htmlToShow.replace(/#activeClass#/g, '');
            }
            htmlToShow = htmlToShow.replace(/#ThreadName#/g, '<img class="mat-logo-image" src="/images/logo/neostencilLogo.png" style="width: 110px; position: relative; top: -2px;">&nbsp; notification');
            htmlToShow = htmlToShow.replace(/#ThreadNameKey#/g, key);
            htmlForMessages = htmlForMessages + messageBeg;
            htmlForMessages = htmlForMessages.replace(/#id#/g, i);
            htmlForMessages = htmlForMessages.replace(/#ThreadName#/g,
                key);
            $.each(value.messages, function(index, element) {
              htmlForMessages = htmlForMessages + messageList;
              if (i == 0) {
                htmlForMessages = htmlForMessages.replace(
                    /#activeClass#/g, 'is-active');
              } else {
                htmlForMessages = htmlForMessages.replace(
                    /#activeClass#/g, '');
              }
              htmlForMessages = htmlForMessages.replace(
                  /#ThreadName#/g, key);
              htmlForMessages = htmlForMessages.replace(
                  "#messageText#", element.body);
              htmlForMessages = htmlForMessages.replace(
                  "#senderOrReceiverName#", element.messageFrom);
              if (element.messageFrom == username) {
                htmlForMessages = htmlForMessages.replace(
                    "#senderOrReceiver#", "receiver");
              } else {
                htmlForMessages = htmlForMessages.replace(
                    "#senderOrReceiver#", "sender");
              }
            })
            htmlForMessages = htmlForMessages + messageEnd;
            htmlForMessages = htmlForMessages.replace(/#ThreadName#/g,
                key);
            i++;
          });
				}






        $.each(response.messagesMap, function(key, value) {
          htmlToShow = htmlToShow + threadList;
          htmlToShow = htmlToShow.replace(/#id#/g, i);
          if (i == 0) {
            htmlToShow = htmlToShow.replace(/#activeClass#/g,
                'is-active');
          } else {
            htmlToShow = htmlToShow.replace(/#activeClass#/g, '');
          }
          htmlToShow = htmlToShow.replace(/#ThreadName#/g, key);
          htmlToShow = htmlToShow.replace(/#ThreadNameKey#/g, key);
          htmlForMessages = htmlForMessages + messageBeg;
          htmlForMessages = htmlForMessages.replace(/#id#/g, i);
          htmlForMessages = htmlForMessages.replace(/#ThreadName#/g,
              key);
          $.each(value.messages, function(index, element) {
            htmlForMessages = htmlForMessages + messageList;
            if (i == 0) {
              htmlForMessages = htmlForMessages.replace(
                  /#activeClass#/g, 'is-active');
            } else {
              htmlForMessages = htmlForMessages.replace(
                  /#activeClass#/g, '');
            }
            htmlForMessages = htmlForMessages.replace(
                /#ThreadName#/g, key);
            htmlForMessages = htmlForMessages.replace(
                "#messageText#", element.body);
            htmlForMessages = htmlForMessages.replace(
                "#senderOrReceiverName#", element.messageFrom);
            if (element.messageFrom == username) {
              htmlForMessages = htmlForMessages.replace(
                  "#senderOrReceiver#", "receiver");
            } else {
              htmlForMessages = htmlForMessages.replace(
                  "#senderOrReceiver#", "sender");
            }
          })
          htmlForMessages = htmlForMessages + messageEnd;
          htmlForMessages = htmlForMessages.replace(/#ThreadName#/g,
              key);
          i++;
        })
        $('#messageLists').html(htmlForMessages);
        $('#messageThreads').html(htmlToShow);
        componentHandler.upgradeElement(document
        .getElementById('messageThreads'));
        $("#message_0 ul").animate({
          scrollTop : $("#message_0 ul")[0].scrollHeight
        }, 100);
      } else {
        $('#messages > div').hide();
        $('#messages > p').show();
      }
      if(response.notificationsMap["NeoStencil Notifications"] != null){
        $('#message_0 textarea').prop('disabled', true);
        $('#message_0 textarea').attr("placeholder", "You Cannot Reply to Notifications");
        $('#message_0 .send-message').attr("disabled","disabled");
        $('#message_0 .send-message').css("opacity","0.5");
			}

    },

    error : function(response) {
      $('#loader-dashboard-messages').hide();

    }
  });
}
$('#messageThreads .mdl-tabs__tab').click(function () {

})


function getTeachersSummary() {
  $('.demo-drawer').removeClass("is-visible");
  $('#loader-dashboard-summary').show();
  $('#boughtCourses').hide();
  $('#students').hide();
  $('#earnings').hide();
  $('#messages').hide();

  $
  .ajax({
    url : apiBasePath + '/instructor/teacher-summary',
    type : "GET",
    contentType : "application/json",
    processData : false,
    dataType : "json",
    success : function(response) {
      /*teacher summary starts here*/

      var noOfBatches = '<div style="background: #607d8b;height: 35px !important;"><span class="summaryFigures">#noOfBatches#</span>  </div> <div class="ns-card" style="padding-top: 23px" ><span class="summaryFigures summaryCategory">Number Of Batches</span></div>';
      var noOfCourses = '<div style="background: #8bc24a;    height: 35px !important;"><span class="summaryFigures">#noOfCourses#</span> </div> <div class="ns-card" style="padding-top: 23px"><span class="summaryFigures summaryCategory">Number Of Courses</span></div>';
      var noOfStudents ='<div  style="background: #20bbd4;    height: 35px !important;"><span class="summaryFigures" >#noOfStudents#</span> </div> <div class="ns-card" style="padding-top: 23px"><span class="summaryFigures summaryCategory ">Number Of Students</span></div>';

      noOfBatches =noOfBatches.replace('#noOfBatches#',response.noOfBatches);
      noOfCourses= noOfCourses.replace('#noOfCourses#',response.noOfCourses);
      noOfStudents=noOfStudents.replace('#noOfStudents#',response.noOfStudents);

      $('#noOfBatches').html(noOfBatches);
      $('#noOfCourses').html(noOfCourses);
      $('#noOfStudents').html(noOfStudents);
      $('#loader-dashboard-summary').hide();
      $('#summaryContainer').show();

      /*Teacher summary ends here*/
    },
    error : function(response) {
    }
  });
}

$('#dashboard-sidenav-links .mdl-navigation__link').click(function () {
  $(this).addClass("is_active");
  $(this).siblings().removeClass("is_active");
});

function switchTabs(el) {
	$(el).addClass("is-active").siblings().removeClass("is-active");
	var find = $(el).attr("data-id");
  var dataName = $(el).attr("data-name");
  $('.messageText').attr("data-id",find);
  $('.messageText').attr("data-name",dataName);
	if(dataName =="NeoStencil Notifications"){
		$('#message_0 textarea').prop('disabled', true);
    $('#message_0 textarea').attr("placeholder", "You Cannot Reply to Notifications");
    $('#message_0 .send-message').attr("disabled","disabled");
    $('#message_0 .send-message').css("opacity","0.5");
	}else{
    $('#message_0 textarea').prop('disabled', false);
    $('#message_0 .send-message').removeAttr("disabled");
    $('#message_0 .send-message').css("opacity","1");
    $('#message_0 textarea').attr("placeholder", "Write a message");

  }
	$(el).parent().parent().siblings().find("#" + find).addClass("is-active")
			.siblings().removeClass("is-active");
	$(el)
			.parent()
			.parent()
			.siblings()
			.find("h5")
			.html(
					'<a class="backToMessages" style="cursor: pointer;" onclick="backToMessages(this);">&lt;</a>&nbsp;&nbsp;'
							+ $(el).attr("data-name"));
	$(el).parent().parent().siblings().find("#" + find).find('ul').animate({
		scrollTop : $(el).parent()[0].scrollHeight
	}, 100);
	if ($(window).width() < '767') {
		$(el).parent().parent().hide();
		$(el).parent().parent().siblings().show();
		$('#messageLists').show();
		$('.mdl-layout__container').css("overflow", "hidden");
	}
}

function backToMessages(el) {
	$(el).parent().parent().parent().hide();
	$(el).parent().parent().parent().siblings().show();
	$('#messageLists').hide();
	$('.mdl-layout__container').css("overflow", "auto");
}

function uploadAttachment(evt) {
	$('#messages textarea').val($("input[type=file]").get(0).files[0].name);
	$('#messages textarea').addClass("uploadFile");
}

function calculateFingerprint() {
	var fp = new Fingerprint2();
	var details = "";
	fp.get(function(result, components) {
		fpValue = result;
		for ( var index in components) {
			var obj = components[index];
			var value = obj.value;
			var line = obj.key + " = " + value.toString().substr(0, 100);
			details += line + "<br />";
		}

		fpValueDetails = details;
	});
	if (/Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent)) {
		deviceTypeDetail = "Mobile";
	} else {
		deviceTypeDetail = "PC";
	}
}
var x;
function countdownTimer(seconds) {
	var time = seconds;
	x = setInterval(
			function() {
				time = time - 1;
				var hours = Math.floor(time / (60 * 60));
				var minutes = Math.floor((time / 60) % 60);
				var seconds = Math.floor(time % 60);
				document.getElementById("countdownTimer").innerHTML = "<i class=\"fa fa-clock-o\" aria-hidden=\"true\"></i>&nbsp;&nbsp;"
						+ hours + ":" + minutes + ":" + seconds;
				if (time < 0) {
					clearInterval(x);
					document.getElementById("countdownTimer").innerHTML = "TIME OVER";
					$('.btn-quiz').trigger("click");
				}
			}, 1000);
}

function checkboxAsRadio(e) {
	$(e).parent('.mdl-checkbox').addClass("is-checked");
	$(e).attr("checked", "checked");
	$(e).parent('.mdl-checkbox').siblings('.mdl-checkbox').removeClass(
			"is-checked");
	$(e).parent('.mdl-checkbox').siblings('.mdl-checkbox').find(
			'.mdl-checkbox__input').removeAttr("checked");
}

var selectedUnitId;
function sendTeacherQuestions(unitId) {
	selectedUnitId=unitId;
  $('.show-dialog-form').show();
  $('.message-has-been-sent').hide();
  var dialog = document.querySelector('#sendEnquiryDialog');
  if (!dialog.showModal) {
    dialogPolyfill.registerDialog(dialog);
  }
  dialog.showModal();
  $('.emptyMessage').hide();
  dialog.querySelector('.show-dialog-form .close').addEventListener('click',
      function () {
        dialog.close();
        $('.show-dialog-form').show();
        $('.message-has-been-sent').hide();
      });
  dialog.querySelector('.message-has-been-sent .close').addEventListener(
      'click', function () {
        dialog.close();
        $('.show-dialog-form').show();
        $('.message-has-been-sent').hide();
      });
}

function sendQuery() {
  if ($('#queryText').val().length > 20 && $('#queryText').val().length < 250  ) {
    $('.emptyMessage').hide();
    $('#enquiryLoader').addClass("is-active");
    $('#send-enquiry-common').addClass('btn-disabled');
    var input = {
      "message": $('#queryText').val(),
      "courseSlug":singleCourseSlug,
      "unitId":selectedUnitId
    }

    $.ajax({
      async: true,
      url: apiBasePath + "/student_admin/send-query",
      type: "POST",
      processData: false,
      dataType: "text",
      data: JSON.stringify(input),
      contentType: "application/json",

      success: function (response) {
        document.getElementById('queryText').value = '';
        $('#send-enquiry-common').removeClass('btn-disabled');
        $('#enquiryLoader').removeClass("is-active");
        var dialog = document.querySelector('#sendEnquiryDialog');
        $('.show-dialog-form').hide();
        $('#enquiryResponseIcon').css("color", "#5ac119");
        $('.message-has-been-sent').show();
        setTimeout(function () {
          dialog.close();
        }, 5000);
      },
      error: function (response) {
        document.getElementById('queryText').value = '';
      	$('#send-enquiry-common').removeClass('btn-disabled');
        $('#enquiryLoader').removeClass("is-active");
        var dialog = document.querySelector('#sendEnquiryDialog');
        $('.show-dialog-form').hide();
        $('.message-has-been-sent').show();
        $('.message-has-been-sent p').text(
            'Message not sent. Please try again later.');
        $('#enquiryResponseIcon').text("cancel");
        $('#enquiryResponseIcon').css("color", "#ff3f00");
        setTimeout(function () {
          dialog.close();
        }, 5000);
      }
    });
  }
  else {
  	if($('#queryText').val().length < 20) {
      $('.emptyMessage').show();
      $('.emptyMessage').text('Please enter minimum 20 characters');
    }
    if ($('#queryText').val().length > 250){
      $('.emptyMessage').show();
      $('.emptyMessage').text('Please enter less than 250 characters');
		}
  }
}
function getUserNewCash(){
	$('#newCash').show();
	$('#loader-usageHistory').show();
  $("#newCashUsageHistory").html("");
  $.ajax({
    async: true,
    url: apiBasePath + "/user/getuserneocash",
    type: "GET",
    processData: false,
    contentType : "application/json",
    dataType : "json",
    success: function (response) {
    	$('#totalNeoCashBalance').html(response.currentNeoCashBalance);
    	var  htmlToShow= '';
      $.each(response.historyList, function(key, value) {
      	var history='<div class="new-cash">\n'
            + '                                            <div class="mdl-grid">\n'
            + '                                                <div\n'
            + '                                                    class="mdl-cell mdl-cell--1-col mdl-cell--1-col-tablet mdl-cell--1-col-phone imgRight">\n'
            + '                                                    <img\n'
            + '                                                        src="https://lh3.googleusercontent.com/yrn0zVJyixNy1g-5pSGmDNbLJbre1OHX2sDUi-Yh2aAO9jH2xO0_mOL_J7S3H0gYtsX-yg5MBwMRRoQt_ILwqocH7Mgp"\n'
            + '                                                        style="margin: 0">\n'
            + '                                                </div>\n'
            + '                                                <div\n'
            + '                                                    class="mdl-cell mdl-cell--7-col mdl-cell--5-col-tablet mdl-cell--2-col-phone">\n'
            + '                                                    <div><p>#reason#</p>'
            + '                                                        <span class="date">#createdAt#</span>'
            + '                                                    </div>\n'
            + '                                                </div>\n'
            + '<div class="mdl-cell mdl-cell--3-col mdl-cell--2-col-tablet mdl-cell--1-col-phone">#action#</div>'
            + '\t\t\t\t\t\t\t\t\t\t</div>\n'
            + '                                </div>';
        htmlToShow = htmlToShow + history;
        htmlToShow= htmlToShow.replace("#reason#",value.reason);
        var time = new Date(value.createdAt),
            enrollDate = time.getDate();
        if(enrollDate <10){
          enrollDate = "0"+enrollDate;
        }
        var month = new Array();
        month[0] = "01";
        month[1] = "02";
        month[2] = "03";
        month[3] = "04";
        month[4] = "05";
        month[5] = "06";
        month[6] = "07";
        month[7] = "08";
        month[8] = "09";
        month[9] = "10";
        month[10] = "11";
        month[11] = "12";

        enrollMonth = month[time.getMonth()];
        enrollYear = time.getFullYear();
        htmlToShow = htmlToShow.replace("#createdAt#", enrollDate
            + '/'+ enrollMonth + '/' + enrollYear);
        if(value.action == "CREDIT"){
          htmlToShow= htmlToShow.replace("#action#","<div>"
              + "                            <p style=\"color: green; font-weight: 700;\">\n"
              + "                              <i class=\"fa fa-plus\" aria-hidden=\"true\"\n"
              + "                                 style=\"padding-right: 5px;\"></i><span>"
							+ value.neoCashValue
							+ "</span>\n"
              + "                            </p>\n"
              + "                          </div>");
				}else{
          htmlToShow= htmlToShow.replace("#action#","<div>"
              + "                            <p style=\"color: red; font-weight: 700;\">\n"
              + "                              <i class=\"fa fa-minus\" aria-hidden=\"true\"\n"
              + "                                 style=\"padding-right: 5px;\"></i><span>"
							+ value.neoCashValue
							+ "</span>\n"
              + "                            </p>\n"
              + "                          </div>");
				}
			});
      $('#loader-usageHistory').hide();
			$("#newCashUsageHistory").html(htmlToShow);

    },
    error: function (response) {
      $("#newCashUsageHistory").html("");
      $('#loader-usageHistory').hide();
      $("#newCashUsageHistory").html('<div>Error While fetching usage history</div>');
    }
  });
}
