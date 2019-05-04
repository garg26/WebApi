var instituteImageUrl;
var instituteFeatureImageUrl;
var instituteHighlightList = [];
var metaInfoList = [];
var postSocialImageUrl;

$(document).ready(function() {

	$('#addMetaPanel').hide();
	$('#metaPanel').hide();
});

/**
 * Upload Institute Image
 */
$("#but_upload").click(function() {
	$('#loaderUpload').show();

	var files = $('#file')[0].files[0];
	var fd = new FormData();
	fd.append('file', files);
	fd.append('type', "institutes");
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

					instituteImageUrl = imageUrlResponse;
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

					$('#institute_row_id .row')
							.each(
									function(index, elm) {
										$(elm).find('#loaderIconUpload').show();
										file = $(elm).find(
												'#InstituteAchievementIcon')[0].files[0];
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
																						"#InstituteAchievementDescription")
																				.val();

																		var input = {
																				iconUrl : iconUrl,
																				iconName : iconName,
																				description : description

																		};
																		instituteHighlightList
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

/*
 * Delete New Row on clicking remove button on institute highlight
 */

$('.field_wrapper').on('click', '.remove-btn', function() {
	$(this).parent().parent('div').remove();

});

/*
 * Add new row on clicking add button on institute highlight
 */
$('.add-more-highlight')
		.click(
				function() {

					blank = false;
					$(".highlight-wrap input").each(function(index, elm) {
						if ($(elm).val() == "") {
							blank = true;
						}
					})

					if (!blank) {

						var fieldHTML = '<div class="row">\n'
								+ '                                <div class="col-md-4">\n'
								+ '\n'
								+ '                                    <input type="text" required name="InstituteHighlightsText[]" id="InstituteHighlightText"\n'
								+ '                                           class="form-control"\n'
								+ '                                           placeholder="Text">\n'
								+ '                                </div>\n'
								+ '\n'
								+ '                                <div class="col-md-7">\n'
								+ '                                    <input type="text" required name="InstituteHighlightDescription[]"\n'
								+ '                                           id="InstituteHighlightDescription" class="form-control"\n'
								+ '                                           placeholder="Description">\n'
								+ '                                </div>\n'
								+ '                                <div class="col-md-1">\n'
								+ '                                    <button class="btn btn-danger remove-btn pull-right" type="button"><i class="glyphicon glyphicon-remove"></i></button>\n'
								+ '                                </div>\n'
								+ '                            </div>';

						$('.field_wrapper').append(fieldHTML);
					}
				});

$('.field_wrapper1').on('click', '.remove-related-info', function() {
	$(this).parent().parent('div').remove();

});

$('.add-more-info')
		.click(
				function() {

					blank = false;
					$(".related_wrap input").each(function(index, elm) {
						if ($(elm).val() == "") {
							blank = true;
						}
					});

					if (!blank) {

						var fieldHTML = '<div class="row">\n'
								+ '                                <div class="col-md-4">\n'
								+ '\n'
								+ '                                    <input type="text" required name="InstituteRelatedText[]" id="InstituteRelatedText"\n'
								+ '                                           class="form-control"\n'
								+ '                                           placeholder="Text">\n'
								+ '                                </div>\n'
								+ '\n'
								+ '                                <div class="col-md-7">\n'
								+ '                                    <input type="text" required name="InstituteRelatedDescription[]"\n'
								+ '                                           id="InstituteRelatedDescription" class="form-control"\n'
								+ '                                           placeholder="Description">\n'
								+ '                                </div>\n'
								+ '                                <div class="col-md-1">\n'
								+ '                                    <button class="btn btn-danger remove-related-info pull-right" type="button"><i class="glyphicon glyphicon-remove"></i></button>\n'
								+ '                                </div>\n'
								+ '                            </div>'

						$('.field_wrapper1').append(fieldHTML);
					}
				});

$(document).on('click', '.remove-achievements', function() {
	$(this).parent().parent('div').remove();

});

$('.add-more-instituteAchievements')
		.click(
				function() {

					blank = false;
					$(".achievements_wrap input").each(function(index, elm) {
						if ($(elm).val() == "") {
							blank = true;
						}
					});

					if (!blank) {
						var fieldHTML = '<div class="row">\n'
								+ '                                <div class="col-md-4">\n'
								+ '\n'
								+ '                                    <label class="custom-file">\n'
								+ '                                        <input type="file" required name="multipleImageIcon[]" id="InstituteAchievementIcon"\n'
								+ '                                               class="custom-file-input">\n'
								+ '                                        <span class="custom-file-control"></span>\n'
								+ '                                    </label>\n'
								+ '\n'
								+ '\n'
								+ '\n'
								+ '                                </div>\n<div style="position: absolute; left: 300px; display: none;" id="loaderIconUpload"><img width="30px" height="30px"\n'
								+ '                                                                       src="http://www.sbif.cl/recursos/sbifweb3/img/throbber_13.gif">\n'
								+ '                </div>\n'
								+ '\n'
								+ '                                <div class="col-md-7">\n'
								+ '                                    <input type="text" required name="InstituteAchievementDescription[]"\n'
								+ '                                           id="InstituteAchievementDescription" class="form-control"\n'
								+ '                                           placeholder="Description">\n'
								+ '                                </div>\n'
								+ '\n'
								+ '                                <div class="col-md-1">\n'
								+ '                                   <button class="btn btn-danger remove-achievements pull-right" type="button"><i class="glyphicon glyphicon-remove"></i></button>\n'
								+ '                                </div>\n'
								+ '                            </div>';

						$('.field_wrapper2').append(fieldHTML);
					}

				});

$('#newInstitute').validate({

	rules : {
		instituteName : {
			required : true
		},
		instituteDescription : {
			required : true
		},
		instituteImage : {
			required : true
		},
		instituteOwner : {
			required : true
		},
		instituteSlug : {
			required : true
		}

	},
	messages : {

		instituteName : {
			required : "Please enter institute name"
		},
		instituteDescription : {
			required : "Please enter institute description"
		},
		instituteImage : {
			required : "Please enter institute image"
		},
		instituteOwner : {
			required : "Please enter institute owner name"
		},
		instituteSlug : {
			required : "Please enter institute slug"
		}

	},
	submitHandler : function() {
		submitRequest();
		$('#loader').show();
	}

});

function submitRequest() {
	var featuredImageReq = {};

	featuredImageReq["url"] = instituteFeatureImageUrl;
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

		name : $('#instituteName').val(),
		url : $('#instituteUrl').val(),
		description : $('#instituteDescription').val(),
		image : $('#file')[0].files[0].name,
		imageUrl : instituteImageUrl,
		emailId : $('#instituteEmail').val(),
		ownerName : $('#instituteOwner').val(),
		address : $('#instituteAddress').val(),
		contactNo : $('#instituteContactNumbers').val(),
    highlights : JSON.stringify(getInstituteHighlightsInfo()),
    achievements : JSON.stringify(instituteHighlightList),
    relatedInfo : JSON.stringify(getInstituteRelatedInfo()),
		facebookUrl : $('#instituteFacebook').val(),
		linkedinUrl : $('#instituteLinkedin').val(),
		twitterUrl : $('#instituteTwitter').val(),
		googlePlusUrl : $('#instituteGooglePlus').val(),
		otherInformation : $('#instituteOtherInfo').val(),
		instituteSlug : $('#slug').val(),
		status : getStatus(),
		metaList : metaInfoList,
		titleTag : $('#titleTag').val(),
		featuredImage : featuredImageReq,
		breadCrumb : {
			breadcrumbId : breadCrumbParentId
		},
		imageAltText : $('#imageAltText').val()
	};

	$.ajax({
		url : apiBasePath + "/admin_lmsadmin/institutes",
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
			alert("Institute Add successfully");
		},

		error : function(response) {
			$('#loader').hide();
			if (response.status == 403) {
				window.location.href = "/permission-error";
			} else {
				alert("Institute add failed");
			}

		}
	});

}

function getInstituteHighlightsInfo() {
	list = [];
	$(".highlight-wrap .row").each(function(index, elm) {
		text = $(elm).find("#InstituteHighlightText").val();
		description = $(elm).find("#InstituteHighlightDescription").val();

		var input = {
				text : text,
				description : description

		};

		list.push(input);
	});
	return list;
}

function getInstituteRelatedInfo() {
	list = [];
	$(".related_wrap .row").each(function(index, elm) {
		text = $(elm).find("#InstituteRelatedText").val();
		description = $(elm).find("#InstituteRelatedDescription").val();

		var input = {
				text : text,
				description : description
		};

		list.push(input);
	});
	return list;
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
					fd.append('type', "featureImage/institutes");
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
													instituteFeatureImageUrl = imageUrlResponse;
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
