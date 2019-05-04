var teacherImageUrl;
var featureImageUrl;
var highlightList = [];
var certificateList = [];
var metaInfoList = [];
var postSocialImageUrl;

function initMaxLengthForEducation() {
	$('input#TeacherEducationText').maxlength({
		alwaysShow : true,
		warningClass : "label label-info",
		limitReachedClass : "label label-warning",
		placement : 'top',
		message : 'used %charsTyped% of %charsTotal% chars.',
		validate : true,
		appendToParent : true
	});

	$('input#TeacherEducationDescription').maxlength({
		alwaysShow : true,
		warningClass : "label label-info",
		limitReachedClass : "label label-warning",
		placement : 'top',
		message : 'used %charsTyped% of %charsTotal% chars.',
		validate : true,
		appendToParent : true
	});
}

function initMaxLengthForHighlight() {
	$('input#TeacherHighlightDescription').maxlength({
		alwaysShow : true,
		warningClass : "label label-info",
		limitReachedClass : "label label-warning",
		placement : 'top',
		message : 'used %charsTyped% of %charsTotal% chars.',
		validate : true
	});

}

function initMaxLengthForExperience() {
	$('input#TeacherExperienceText').maxlength({
		alwaysShow : true,
		warningClass : "label label-info",
		limitReachedClass : "label label-warning",
		placement : 'top',
		message : 'used %charsTyped% of %charsTotal% chars.',
		validate : true
	});

	$('input#TeacherExperienceDescription').maxlength({
		alwaysShow : true,
		warningClass : "label label-info",
		limitReachedClass : "label label-warning",
		placement : 'top',
		message : 'used %charsTyped% of %charsTotal% chars.',
		validate : true
	});
}

$(document).ready(function() {

	$('#addMetaPanel').hide();
	$('#metaPanel').hide();
	getExamSegment();
	initMaxLengthForEducation();
	initMaxLengthForHighlight();
	initMaxLengthForExperience();
});

/*
 * Delete New Row on clicking remove button on institute highlight
 */
$(document).on('click', '.remove-highlight-btn', function() {
	$(this).parent().parent('div').remove();

});

$('input#teacherName').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherSubjects').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherLocation').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherAddress').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherContactNo').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherEmailId').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherWebsite').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherFacebook').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherTwitter').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherLinkedin').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#teacherGooglePlus').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#txtFITitle').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#txtFICaption').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});
$('input#txtFIAltText').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

$('input#txtFIDescription').maxlength({
	alwaysShow : true,
	warningClass : "label label-info",
	limitReachedClass : "label label-warning",
	placement : 'top',
	message : 'used %charsTyped% of %charsTotal% chars.',
	validate : true
});

function getExamSegment() {

	$.ajax({
		type : 'GET',
		dataType : 'json',
		url : apiBasePath + "/teacherexamsegments",
		success : function(response) {
			var htmlToShow = '<option value="0">Select Exam Segment</option>';
			var examSegmentList = '<option value="#name#">#name#</option>';
			if (response.length > 0) {
				$.each(response, function(index, element) {
					i = index + 1;
					htmlToShow = htmlToShow += examSegmentList;
					htmlToShow = htmlToShow.replace(/#name#/g, element);

				});
				$('#teacherExamSegment').html(htmlToShow);

			} else {
				$('#teacherExamSegment').html("");
			}
		},

		error : function(response) {

		}
	});

}

$("#teacherExamSegment").change(function(event) {
	es = $(this).val();
	getTeacherCategoryByExamSegment(es);
});

function getTeacherCategoryByExamSegment(category) {

	$
			.ajax({
				type : 'GET',
				dataType : 'json',
				url : apiBasePath + "/teacherexamsegments/teachercategory/" + category,
				success : function(response) {
					var htmlToShow = '<option value="0">Select Teacher Category</option>';
					var categoryList = '<option value="#name#">#name#</option>';
					if (response.length > 0) {
						$.each(response,
								function(index, element) {
									i = index + 1;
									htmlToShow = htmlToShow += categoryList;
									htmlToShow = htmlToShow.replace(/#name#/g,
											element);

								});
						$('#teacherCategory').html(htmlToShow).removeAttr(
								'disabled');

					} else {
						$('#teacherCategory').html("");
					}
				},

				error : function(response) {

				}
			});

}

$("#teacherCategory").change(function(event) {
	es = $(this).val();
	ss = $("#teacherExamSegment").val();
	getTeacherSubjectByTeacherCategory(ss, es);
});

function getTeacherSubjectByTeacherCategory(examsegment, teacherCategory){

    $
        .ajax({
            type : 'GET',
            dataType : 'json',
            url : apiBasePath + "/teacherexamsegments/" + examsegment + "/teachercategory/" + teacherCategory + "/teachersubject",

            success : function(response){
                var htmlToShow = '<option value="0">Select Teacher Subject</option>';
                var subjectList = '<option value="#subject#">#subject#</option>';
                if (response.length > 0) {
                	$.each(response,
               		function(index, element) {
               			i = index + 1;
                		htmlToShow = htmlToShow += subjectList;
           				htmlToShow = htmlToShow.replace(/#subject#/g,
                					element);

               			});
                		$('#teacherSubject').html(htmlToShow).removeAttr(
                				'disabled');

                } else {
               			$('#teacherSubject').html("");
              	}
            },

            error : function(response){

            }
        });
}

/*
 * Add new row on clicking add button on institute highlight
 */
$('.add-more-highlight')
		.click(
				function() {

					blank = false;
					$(".highlight_wrap input").each(function(index, elm) {
						if ($(elm).val() == "") {
							blank = true;
						}
					})

					if (!blank) {
						var fieldHTML = '<div class="row">\n'
								+ '                                <div class="col-md-4">\n'
								+ '\n'
								+ '                                    <label class="custom-file">\n'
								+ '                                        <input type="file" required name="multipleImageIcon[]" id="TeacherHighlightIcon"\n'
								+ '                                               class="custom-file-input">\n'
								+ '                                        <span class="custom-file-control"></span>\n'
								+ '                                    </label>\n'
								+ '\n'
								+ '\n'
								+ '                                </div><div style="position: absolute; left: 300px; display: none;" id="loaderIconUpload">\n'
								+ '                <img width="30px" height="30px"\n'
								+ '                     src="http://www.sbif.cl/recursos/sbifweb3/img/throbber_13.gif">\n'
								+ '              </div>\n'
								+ '\n'
								+ '                                <div class="col-md-7">\n'
								+ '                                    <input type="text" required name="TeacherHighlightDescription[]"\n'
								+ '                                           id="TeacherHighlightDescription" class="form-control"\n'
								+ '                                           placeholder="Description" maxlength="150" minlength="2">\n'
								+ '                                </div>\n'
								+ '\n'
								+ '                                <div class="col-md-1">\n'
								+ '                                    <button class="btn btn-danger remove-highlight-btn pull-right" type="button"><i class="glyphicon glyphicon-remove"></i></button>\n'
								+ '                                </div>\n'
								+ '                            </div>';

						$('.field_wrapper').append(fieldHTML);
						initMaxLengthForHighlight();
					}

				});

/*
 * Delete New Row on clicking remove button on institute highlight
 */
$(document).on('click', '.remove-certificate-btn', function() {
	$(this).parent().parent('div').remove();

});

/**
 * Add certificate row
 */
$('.add-more-certificate')
		.click(
				function() {

					var fieldHTML = '<div class="row">\n'
							+ '                                <div class="col-md-4">\n'
							+ '                                    <label class="custom-file">\n'
							+ '                                        <input type="file" required name="teacherCertificate[]" id="teacherCertificate"\n'
							+ '                                               class="custom-file-input">\n'
							+ '                                        <span class="custom-file-control"></span>\n'
							+ '                                    </label>\n'
							+ '                                </div><div style="position: absolute; left: 300px; display: none;" id="loaderCertificateUpload">\n'
							+ '                  <img width="30px" height="30px"\n'
							+ '                       src="http://www.sbif.cl/recursos/sbifweb3/img/throbber_13.gif">\n'
							+ '                </div>'
							+ '\n'
							+ '\n'
							+ '                                <div class="col-md-1">\n'
							+ '                                    <button class="btn btn-danger remove-certificate-btn pull-right" type="button"><i class="glyphicon glyphicon-remove"></i></button>     \n'
							+ '                                </div>\n' + '\n'
							+ '                            </div>';

					$('.field_wrapper3').append(fieldHTML);

				});

$('.field_wrapper2').on('click', '.remove-experience-btn', function() {
	$(this).parent().parent('div').remove();

});

$('.add-more-experience')
		.click(
				function() {

					blank = false;
					$(".experience_wrap input").each(function(index, elm) {
						if ($(elm).val() == "") {
							blank = true;
						}
					})

					if (!blank) {

						var fieldHTML = '<div class="row">\n'
								+ '                                <div class="col-md-4">\n'
								+ '\n'
								+ '                                    <input type="text" required name="TeacherExperienceText[]" id="TeacherExperienceText"\n'
								+ '                                           class="form-control"\n'
								+ '                                           placeholder="Text" maxlength="150" minlength="2">\n'
								+ '                                </div>\n'
								+ '\n'
								+ '                                <div class="col-md-7">\n'
								+ '                                    <input type="text" required name="TeacherExperienceDescription[]"\n'
								+ '                                           id="TeacherExperienceDescription" class="form-control"\n'
								+ '                                           placeholder="Description" maxlength="150" minlength="2">\n'
								+ '                                </div>\n'
								+ '                                <div class="col-md-1">\n'
								+ '                                    <button class="btn btn-danger remove-experience-btn pull-right" type="button"><i class="glyphicon glyphicon-remove"></i></button>\n'
								+ '                                </div>\n'
								+ '                            </div>'

						$('.field_wrapper2').append(fieldHTML);
						initMaxLengthForExperience();
					}
				});

$('.field_wrapper1').on('click', '.remove-education-btn', function() {
	$(this).parent().parent('div').remove();

});

$('.add-education-more')
		.click(
				function() {

					blank = false;
					$(".education_wrap input").each(function(index, elm) {
						if ($(elm).val() == "") {
							blank = true;
						}
					})

					if (!blank) {

						var fieldHTML = '<div class="row">\n'
								+ '                                <div class="col-md-4">\n'
								+ '\n'
								+ '                                    <input type="text" required name="TeacherEducationText[]" id="TeacherEducationText"\n'
								+ '                                           class="form-control"\n'
								+ '                                           placeholder="Text" maxlength="150" minlength="2">\n'
								+ '                                </div>\n'
								+ '\n'
								+ '                                <div class="col-md-7">\n'
								+ '                                    <input type="text" required name="TeacherEducationDescription[]"\n'
								+ '                                           id="TeacherEducationDescription" class="form-control"\n'
								+ '                                           placeholder="Description" maxlength="150" minlength="2">\n'
								+ '                                </div>\n'
								+ '                                <div class="col-md-1">\n'
								+ '                                    <button class="btn btn-danger remove-education-btn pull-right" type="button"><i class="glyphicon glyphicon-remove"></i></button>\n'
								+ '                                </div>\n'
								+ '                            </div>';

						$('.field_wrapper1').append(fieldHTML);
						initMaxLengthForEducation();
					}
				});

/**
 * Upload Teacher Image
 */
$("#but_upload").click(function() {
	$('#loaderUpload').show();

	var files = $('#file')[0].files[0];
	var fd = new FormData();
	fd.append('file', files);
	fd.append('type', "teachers");
	fd.append('contentType', files.type);

	uploadFile(fd, function(response) {
		if (response != null) {
			alert("Upload File " + response + " " + "successfully");
			getImageUrl(response, function(imageUrlResponse) {

				if (imageUrlResponse != null) {
					$('#loaderUpload').hide();
					var reader = new FileReader();
					reader.onload = function(e) {
						$('#upload-img-tag').attr('src', e.target.result);
					};
					reader.readAsDataURL($('#file')[0].files[0]);

					teacherImageUrl = imageUrlResponse;
				} else {
					$('#loaderUpload').hide();

				}
			});

		}

		else {
			$('#loaderUpload').hide();

		}

	});

});

/**
 * Upload Mutiple Icons
 */
$("#but_icon_upload")
		.click(
				function() {
					$('#but_icon_upload').hide();

					$('#highlight_row_id .row')
							.each(
									function(index, elm) {
										$(elm).find('#loaderIconUpload').show()
										file = $(elm).find(
												'#TeacherHighlightIcon')[0].files[0];
										var form = new FormData();
										form.append("file", file);
										form.append('type', 'icons');
										form.append('contentType', file.type);

										uploadFile(
												form,
												function(response) {
													if (response != null) {

														getImageUrl(
																response,
																function(
																		imageUrlResponse) {

																	if (imageUrlResponse != null) {
																		$(elm)
																				.find(
																						'#loaderIconUpload')
																				.hide();
																		iconUrl = imageUrlResponse;
																		iconName = response;
																		description = $(
																				elm)
																				.find(
																						"#TeacherHighlightDescription")
																				.val();

																		var input = {
																				iconUrl : iconUrl,
																				iconName : iconName,
																				description : description
																		};
																		highlightList
																				.push(input);

																	} else {
																		alert("Failed in uploading certificate");
																		$(elm)
																				.find(
																						'#loaderIconUpload')
																				.hide();
																	}

																});

													}

													else {
														alert("Failed in uploading certificate");
														$(elm)
																.find(
																		'#loaderIconUpload')
																.hide();
													}

												});
									});

				});

/**
 * Upload Mutiple Teacher Certificate
 */
$("#but_certificate_upload").click(function() {
	$('#but_certificate_upload').hide();

	$('#certificate_row_id .row').each(function(index, elm) {
		$(elm).find('#loaderCertificateUpload').show();
		file = $(elm).find('#teacherCertificate')[0].files[0];
		var form = new FormData();
		form.append("file", file);
		form.append('type', 'certificate');
		form.append('contentType', file.type);

		uploadFile(form, function(response) {
			if (response != null) {
				getImageUrl(response, function(imageUrlResponse) {

					if (imageUrlResponse != null) {
						$(elm).find('#loaderCertificateUpload').hide();
						icon = imageUrlResponse;
						certificateList.push(icon);
					} else {
						$(elm).find('#loaderCertificateUpload').hide();
						alert("Failed in uploading certificate");
					}

				});

			}

			else {
				$(elm).find('#loaderCertificateUpload').hide();
				alert("Failed in uploading certificate");
			}

		});
	});

});

$('#newTeacher').validate({

	rules : {
		teacherName : {
			required : true
		},
		teacherDescription : {
			required : true
		},
		teacherSubjects : {
			required : true
		},
		teacherInstitute : {
			required : true
		},
		teacherSlug : {
			required : true
		},
		teacherImage : {
			required : true
		},

	},
	messages : {

		teacherName : {
			required : "Please enter Teacher name"
		},
		teacherDescription : {
			required : "Please enter Teacher description"
		},
		teacherSubjects : {
			required : "Please enter Teacher image"
		},
		teacherInstitute : {
			required : "Please Select institute"
		},
		teacherSlug : {
			required : "Please enter teacher slug"
		},
		teacherImage : {
			required : "Please enter teacher image"
		},
	},
	submitHandler : function() {
		submitRequest();
		$('#loader').show();
	}

});

function submitRequest() {
	

		if (document.getElementById('receivesQuery').checked) {
		$('#receivesQuery').attr('checked', true);
		$('input[name="product"]').val(true);
	} else {
		$('#receivesQuery').attr('checked', false);
		$('input[name="product"]').val(false);
	}
	
	var featuredImageReq = {};

	featuredImageReq["url"] = featureImageUrl;
	featuredImageReq["name"] = $('#featureImage')[0].files[0].name;
	featuredImageReq["title"] = $('#txtFITitle').val();
	featuredImageReq["caption"] = $('#txtFICaption').val();
	featuredImageReq["altText"] = $('#txtFIAltText').val();
	featuredImageReq["description"] = $('#txtFIDescription').val();

	// for adding description

	var metaDesc = {};

	metaDesc["type"] = "description";
	metaDesc["content"] = $('#txtMetaDesc').val();
	metaDesc["attributeType"] = "name";

	metaInfoList.push(metaDesc);

	// For adding social image

	var metaImg = {};

	metaImg["type"] = "og:image";
	metaImg["content"] = postSocialImageUrl;
	metaImg["attributeType"] = "property";

	metaInfoList.push(metaImg);

	metaImg["type"] = "og:image:secure_url";

	metaInfoList.push(metaImg);

	metaImg["type"] = "twitter:image";
	metaImg["attributeType"] = "name";

	metaInfoList.push(metaImg);

	var input = {

		teacherName : $('#teacherName').val(),
		teacherDescription : $('#teacherDescription').val(),
		displayPicture : $('#file')[0].files[0].name,
		displayPictureUrl : teacherImageUrl,
		subjects : getTeacherSubjects(),
		institute : {
			id : $('#teacherInstitute').val()
		},
		teacherBio : $('#teacherBio').val(),
		slug : $('#slug').val(),
		totalExperience : $('#teacherExperience').val(),
		teacherLocation : $('#teacherLocation').val(),
    achievements : JSON.stringify(highlightList),
    educations : JSON.stringify(getTeacherEducation()),
    experiences : JSON.stringify(getTeacherExperience()),
		address : $('#teacherAddress').val(),
		contactNo : $('#teacherContactNo').val(),
		emailId : $('#teacherEmailId').val(),
		website : $('#teacherWebsite').val(),
		facebookUrl : $('#teacherFacebook').val(),
		linkedinUrl : $('#teacherLinkedin').val(),
		twitterUrl : $('#teacherTwitter').val(),
		googlePlusUrl : $('#teacherGooglePlus').val(),
		currentStatus : $('#teacherCurrentStatus').val(),
		certificates : certificateList,
		status : getStatus(),
		teacherRating : $('#teacherRating').val(),
		metaList : metaInfoList,
		titleTag : $('#titleTag').val(),
		featuredImage : featuredImageReq,
		teacherExamSegment : $('#teacherExamSegment').val() != "" ? $(
				'#teacherExamSegment').val() : null,
		teacherCategory : $('#teacherCategory').val() != "" ? $(
				'#teacherCategory').val() : null,

		breadCrumb : {
			breadcrumbId : breadCrumbParentId
		},
		commissionPercentage : $('#commissionPercentage').val(),
		imageAltText : $('#imageAltText').val(),
		receiveQueries:$('#receivesQuery').val()
	};

	$.ajax({
		url : apiBasePath + "/admin_lmsadmin/teachers",
		type : "post",
		data : JSON.stringify(input),
		contentType : "application/json",
		processData : false,
		/*
		 * "headers" : { "authorization" : "Bearer " +
		 * window.localStorage.getItem('access_token'), },
		 */

		success : function(response) {
			$('#loader').hide();
			location.reload();
			alert("Teacher Add successfully");
		},

		error : function(response) {
			$('#loader').hide();
			if (response.status == 403) {
				window.location.href = "/permission-error";
			} else {
				alert("Teacher add failed");
			}

		}
	});
}

function getTeacherExperience() {
	list = [];
    $(".experience_wrap .row").each(function(index, elm) {
    	text = $(elm).find("#TeacherExperienceText").val();
    	description = $(elm).find("#TeacherExperienceDescription").val();
    	var input = {
    			text : text,
    			description : description
    	};

    	list.push(input);
    });
    return list;

}

function getTeacherEducation() {
	list = [];
	$(".education_wrap .row").each(function(index, elm) {
		text = $(elm).find("#TeacherEducationText").val();
		description = $(elm).find("#TeacherEducationDescription").val();
		var input = {
				text : text,
				description : description
		};

		list.push(input);
	});
	return list;
}

function getTeacherSubjects() {
    var stringTeacherSubjects = "";
    $(".teacherSubjects_wrap select").each(function (index, elm) {
      if(index == 0){
        stringTeacherSubjects += $(elm).val();
      } else {
        stringTeacherSubjects += "," + $(elm).val();
      }
    });
    return stringTeacherSubjects;
  }

function getStatus() {
	var status;
	if (document.getElementById('publish').checked) {
		status = document.getElementById('publish').value;
	} else if (document.getElementById('draft').checked) {
		status = document.getElementById('draft').value;
	} else {
		status = document.getElementById('draft').value;
	}

	return status;
}

$("#but_feature_upload")
		.click(
				function() {
					$('#loaderFeatureUpload').show();

					var files = $('#featureImage')[0].files[0];
					var fd = new FormData();
					fd.append('file', files);
					fd.append('type', "featureImage/teachers");
					fd.append('contentType', files.type);

					uploadFile(
							fd,
							function(response) {
								if (response != null) {
									getImageUrl(
											response,
											function(imageUrlResponse) {

												if (imageUrlResponse != null) {
													$('#loaderFeatureUpload')
															.hide();
													var reader = new FileReader();
													reader.onload = function(e) {
														$(
																'#upload-feature-img-tag')
																.attr(
																		'src',
																		e.target.result);
													};
													reader
															.readAsDataURL($('#featureImage')[0].files[0]);
													featureImageUrl = imageUrlResponse;
												} else {
													$('#loaderFeatureUpload')
															.hide();

												}
											});

								}

								else {
									$('#loaderFeatureUpload').hide();

								}

							});

				});

function showAddMetaPanel() {
	$('#addMetaPanel').show();
}

function addMetaInfo() {
	var meta = {};

	meta["type"] = $('#ddMetaType').val();
	meta["content"] = $('#txtMetaContent').val();
	switch ($('#ddMetaType').val()) {
	case "og:locale":
	case "og:site_name":
	case "og:type":
	case "og:description":
	case "fb:app_id":
	case "og:url":
		meta["attributeType"] = 'property';
		break;
	default:
		meta["attributeType"] = 'name';
		break;
	}

	metaInfoList.push(meta);

	$('#ddMetaType').prop('selectedIndex', 0);
	$('#txtMetaContent').prop('value', '');

	$('#addMetaPanel').hide();
	showMetaDetails();
}

function showMetaDetails() {
	htmlToShow = '';
	htmlBeg = '<div class="row">';
	htmlRow = '<div style="margin-top: 20px;" class="col-md-9">	<label id="lblType" style="font-weight:  bold;"> Meta Type:&nbsp;&nbsp;</label ><input type="text" value="#metaType#" readonly="readonly" style="border-bottom:  0; border-top: 0; border-left:  0; border-right:  0;"></div><div style="margin-top: 20px;" class="col-md-9"><label id="lblContent" style="font-weight:  bold;"> Meta Content:&nbsp;&nbsp;</label> <input type="text" value="#metaContent#" readonly="readonly" style="border-bottom:  0; border-top: 0; border-left:  0; border-right:  0;"></div>';
	htmlEnd = '</div><br/>';

	if (metaInfoList != null && metaInfoList.length > 0) {
		htmlToShow += htmlBeg;

		$.each(metaInfoList, function(index, element) {
			htmlToShow += htmlRow;

			htmlToShow = htmlToShow.replace("#metaType#", element.type);
			htmlToShow = htmlToShow.replace("#metaContent#", element.content);
		});
		htmlToShow += htmlEnd;

		$('#metaPanel').html(htmlToShow).show();
	}

}

$("#but_social_upload")
		.click(
				function() {
					$('#loaderSocialUpload').show();
					$('#but_social_upload').hide();

					var files = $('#socialImage')[0].files[0];
					var fd = new FormData();
					fd.append('file', files);
					fd.append('type', "socialImage/post");
					fd.append('contentType', files.type);

					uploadFile(
							fd,
							function(response) {
								if (response != null) {
									getImageUrl(
											response,
											function(imageUrlResponse) {

												if (imageUrlResponse != null) {
													$('#loaderSocialUpload')
															.hide();
													var reader = new FileReader();
													reader.onload = function(e) {
														$(
																'#upload-social-img-tag')
																.attr(
																		'src',
																		e.target.result);
													};
													reader
															.readAsDataURL($('#socialImage')[0].files[0]);
													postSocialImageUrl = imageUrlResponse;
													if ($("#upload-social-img-tag").length > 0) {

														var mydiv = document
																.getElementById("socialImageContainer");
														var aTag = document
																.createElement('a');
														aTag
																.setAttribute(
																		'href',
																		postSocialImageUrl);
														aTag
																.setAttribute(
																		'id',
																		"social-image-url");
														aTag.innerHTML = postSocialImageUrl;
														mydiv.appendChild(aTag);

													} else {
														var mydiv = document
																.getElementById("socialImageContainer");
														var aTag = document
																.createElement('a');
														aTag
																.setAttribute(
																		'href',
																		imageUrlResponse);
														aTag
																.setAttribute(
																		'id',
																		"social-image-url");
														aTag.innerHTML = imageUrlResponse;
														mydiv.appendChild(aTag);
													}

												} else {
													$('#loaderSocialUpload')
															.hide();
													$('#but_social_upload')
															.show();
												}
											});

								}

								else {
									$('#loaderSocialUpload').hide();
									$('#but_social_upload').show();
								}

							});

				});
