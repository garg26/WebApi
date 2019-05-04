var metaInfoList = [];
var postFeatureImageUrl;
var widgetList = [];
var postSocialImageUrl;

$(document)
		.ready(
				function() {
					getExamSegment();
					/*
					 * $('#summernote').summernote({
					 * 
					 * styleTags : [ 'p', 'pre', 'h2', 'h3', 'h4', 'h5', 'h6' ],
					 * height : 500, // set editor height minHeight : null, //
					 * set minimum height of editor maxHeight : null, // set
					 * maximum height of editor focus : true // set focus to
					 * editable area after initializing summernote });
					 */

					tinymce
							.init({
								selector : '#summernote',
								theme : 'modern',
								plugins : [
										'advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker',
										'searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking',
										'save table contextmenu directionality emoticons template paste textcolor' ],
								content_css : 'css/content.css',
                valid_elements: '*[*]',
								toolbar : 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | print preview media fullpage | forecolor backcolor emoticons'

							});
					/*
					 * $('#summernote').summernote('formatH2');
					 * $('#summernote').summernote('formatH6');
					 * 
					 * var markupStr = $('#summernote').summernote('code');
					 */
					$('#addMetaPanel').hide();
					$('#metaPanel').hide();

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
				$('#postExamSegment').html(htmlToShow);

			} else {
				$('#postExamSegment').html("");
			}
		},

		error : function(response) {

		}
	});
}

// submit post creation form
$("form[name='add-new-post']")
		.validate(
				{
					submitHandler : function(form, e) {
						if (document.getElementById('popular').checked) {
							$('#free').attr('checked', true);
							$('input[name="popular"]').val(true);
						} else {
							$('#free').attr('checked', false);
							$('input[name="popular"]').val(false);
						}
						if (document.getElementById('featured').checked) {
							$('#free').attr('checked', true);
							$('input[name="featured"]').val(true);
						} else {
							$('#free').attr('checked', false);
							$('input[name="featured"]').val(false);
						}
						$("#add-new-post-error").html('');
						$("#add-new-post-text-error").hide();
						$("#add-new-post-text-error").html('');
						e.preventDefault();
						if (tinyMCE.activeEditor.getContent() == '') {
							$("#add-new-post-text-error").show();
							$("#add-new-post-text-error").html(
									"This is a required field");
							return;
						}
						if (postFeatureImageUrl != undefined) {

							var featuredImageReq = {};

							featuredImageReq["url"] = postFeatureImageUrl;
							featuredImageReq["name"] = $('#featureImage')[0].files[0].name;
							featuredImageReq["title"] = $('#txtFITitle').val();
							featuredImageReq["caption"] = $('#txtFICaption')
									.val();
							featuredImageReq["altText"] = $('#txtFIAltText')
									.val();
							featuredImageReq["description"] = $(
									'#txtFIDescription').val();

							// for adding description
							// var metaDesc = {};
							//
							// metaDesc["type"] = "description";
							// metaDesc["content"] = $('#txtMetaDesc').val();
							// metaDesc["attributeType"] = "name";
							//
							// metaInfoList.push(metaDesc);
							// For adding social image
							// var metaImg = {};
							//
							// metaImg["type"] = "og:image";
							// metaImg["content"] = postSocialImageUrl;
							// metaImg["attributeType"] = "property";
							//
							// metaInfoList.push(metaImg);
							//
							// metaImg["type"] = "og:image:secure_url";
							//
							// metaInfoList.push(metaImg);
							//
							// metaImg["type"] = "twitter:image";
							// metaImg["attributeType"] = "name";
							//
							// metaInfoList.push(metaImg);
							var postPayload = {
								postSlug : $('#post-slug').val(),
								title : $('#post-title').val(),
								text : tinyMCE.activeEditor.getContent(),
								status : $('input[name=status]:checked',
										'#add-new-post').val() == undefined ? 'draft'
										: $('input[name=status]:checked',
												'#add-new-post').val(),
								featured : $('#featured').val(),
								popular : $('#popular').val(),
								readTime : $('#readTime').val(),
								noOfViews : $('#noofviews').val(),
								brief : $('#brief').val(),
								examSegment : $('#postExamSegment').val() != "" ? $(
										'#postExamSegment').val()
										: null,
								// previousPost: {
								// slug: $('#previousPost').val() != '' ? $(
								// '#previousPost').val() : 'dummyPrev'
								// },
								// nextPost: {
								// slug: $('#nextPost').val() != '' ?
								// $('#nextPost')
								// .val() : 'dummyNext'
								// },
								// relatedPosts: getRelatedPosts(),
								metaList : getUpdatedMetaList(),
								titleTag : $('#titleTag').val(),
								featuredImage : featuredImageReq,
								type : 'DEFAULT',
								widgets : widgetList,
								breadCrumb : {
									breadcrumbId : breadCrumbParentId
								},
								tags : getTags()

							};

							console.log(postPayload);
							$('#loader').show();
							$.ajax({
								url : apiBasePath + "/admin_writer/posts",
								type : "POST",
								data : JSON.stringify(postPayload),
								contentType : "application/json",
								processData : false,
								// "headers": {
								// "authorization": "Bearer "
								// +
								// window.localStorage.getItem('access_token'),
								//
								// },

								success : function(response) {
									$('#loader').hide();
									location.reload();
									alert("Post Added Successfully")

								},

								error : function(response) {

									$('#loader').hide();
									alert("Post Creation Failed");

								}
							});
						} else {
							alert("Please upload the Featured image ")
						}
					}

				});

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

function publishPost(postId) {
	$('#loader').show();
	$.ajax({
		url : apiBasePath + "/admin_editor/post/" + postId + "/publish",
		type : "PATCH",
		dataType : "json",
		contentType : "application/json",
		processData : false,
		// "headers": {
		// "authorization": "Bearer "
		// + window.localStorage.getItem('access_token'),
		//
		// },

		success : function(response) {
			$('#loader').hide();
			location.reload();
			alert("Post Published Successfully")

		},

		error : function(response) {

			$('#loader').hide();
			if (response.status == 403) {
				window.location.href = "/permission-error";
			} else {
				alert("Post Publishing Failed");
			}

		}
	});
}

// function getRelatedPosts() {
// list = [];
//
// $("#related-posts").val().forEach(function(value, index) {
// list.push({
// postSummaryId : value
// });
// });
//
// return list;
// }

function showAddMetaPanel() {
	$('#addMetaPanel').show();
}

function addMetaInfo() {
	// var meta = {};
	//
	// meta["type"] = $('#ddMetaType').val();
	// meta["content"] = $('#txtMetaContent').val();
	// switch ($('#ddMetaType').val()) {
	// case "og:locale":
	// case "og:site_name":
	// case "og:type":
	// case "og:description":
	// case "fb:app_id":
	// case "og:url":
	// meta["attributeType"] = 'property';
	// break;
	// default:
	// meta["attributeType"] = 'name';
	// break;
	// }
	//
	// metaInfoList.push(meta);
	//
	// $('#ddMetaType').prop('selectedIndex', 0);
	// $('#txtMetaContent').prop('value', '');
	//
	// $('#addMetaPanel').hide();
	// showMetaDetails();

	var htmlToShow = '<div class="metaPanelData">\n'
			+ '                  <div class="col-md-4">\n'
			+ '                    <label style="font-weight: bold;">Meta Type:</label> <input type="text" readonly="readonly" id="metaType" value="#type#">\n'
			+ '                  </div>\n'
			+ '                  <div class="col-md-8">\n'
			+ '                    <label style="font-weight: bold;">Meta Content:</label> <input style="width: 70%;" type="text" value="#content#" id="metaContent">\n'
			+ '                  </div>\n' + '                </div>';

	htmlToShow = htmlToShow.replace("#type#", $('#ddMetaType').val());
	htmlToShow = htmlToShow.replace("#content#", $('#txtMetaContent').val());

	$('.metaPanel').append(htmlToShow);
	$('#addMetaPanel').hide();

	$('#ddMetaType').prop('selectedIndex', 0);
	$('#txtMetaContent').prop('value', '');
}

function showMetaDetails() {
	htmlToShow = '';
	htmlBeg = '<div class="row">';
	htmlRow = '<div style="margin-top: 20px;" class="col-md-4">	<label id="lblType" style="font-weight:  bold;"> Meta Type:&nbsp;&nbsp;</label ><input type="text" value="#metaType#" readonly="readonly" style="border-bottom:  0; border-top: 0; border-left:  0; border-right:  0;"></div><div style="margin-top: 20px;" class="col-md-4"><label id="lblContent" style="font-weight:  bold;"> Meta Content:&nbsp;&nbsp;</label> <input type="text" value="#metaContent#" readonly="readonly" style="border-bottom:  0; border-top: 0; border-left:  0; border-right:  0;"></div>';
	htmlEnd = '</div><br/>';

	if (metaInfoList != null && metaInfoList.length > 0) {
		htmlToShow += htmlBeg;

		$.each(metaInfoList, function(index, element) {
			htmlToShow += htmlRow;

			htmlToShow = htmlToShow.replace("#metaType#", element.type);
			htmlToShow = htmlToShow.replace("#metaContent#", element.content);
		});
		if ($('#txtMetaDesc').val() != null && $('#txtMetaDesc').val() != '') {
			htmlToShow += htmlRow;

			htmlToShow = htmlToShow.replace("#metaType#", "description");
			htmlToShow = htmlToShow.replace("#metaContent#", $('#txtMetaDesc')
					.val());
		}
		htmlToShow += htmlEnd;

		$('#metaPanel').html(htmlToShow).show();
	}

}

$("#but_feature_upload")
		.click(
				function() {
					$('#loaderFeatureUpload').show();
					$('#but_feature_upload').hide();

					var files = $('#featureImage')[0].files[0];
					var fd = new FormData();
					fd.append('file', files);
					fd.append('type', "featureImage/post");
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
													postFeatureImageUrl = imageUrlResponse;
												} else {
													$('#loaderFeatureUpload')
															.hide();
													$('#but_feature_upload')
															.show();
												}
											});

								}

								else {
									$('#loaderFeatureUpload').hide();
									$('#but_feature_upload').show();
								}

							});

				});

$("#but_social_upload")
		.click(
				function() {
					$('#loaderSocialUpload').show();
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
													if ($("#social-image-url").length > 0) {

														$('#social-image-url')
																.remove();
														var mydiv = document
																.getElementById("social-image");
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

													} else {
														var mydiv = document
																.getElementById("social-image");
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
												}
											});

								}

								else {
									$('#loaderSocialUpload').hide();
								}

							});

				});

/**
 * Add post widget
 */
$('.add-more-widget')
		.click(
				function() {

					var fieldHTML = '<div class="row">\n'
							+ '                                <div class="col-md-4">\n'
							+ '                                    <label class="custom-file">\n'
							+ '                                        <input type="file" required name="postWidgetImage[]" id="postWidgetImage"\n'
							+ '                                               class="custom-file-input">\n'
							+ '                                        <span class="custom-file-control"></span>\n'
							+ '                                    </label>\n'
							+ '                                </div><div style="position: absolute; left: 300px; display: none;" id="loaderWidgetUpload">\n'
							+ '                  <img width="30px" height="30px"\n'
							+ '                       src="http://www.sbif.cl/recursos/sbifweb3/img/throbber_13.gif">\n'
							+ '                </div>'
							+ '\n'
							+ '\n'
							+ '                                <div class="col-md-1">\n'
							+ '                                    <button class="btn btn-danger remove-widget-btn pull-right" type="button"><i class="glyphicon glyphicon-remove"></i></button>     \n'
							+ '                                </div>\n' + '\n'
							+ '                            </div>';

					$('.field_wrapper').append(fieldHTML);

				});

/**
 * Upload Mutiple Post Widget
 */
$("#but_widget_upload").click(function() {
	$('#but_widget_upload').hide();

	$('#post_widget_row_id .row').each(function(index, elm) {
		$(elm).find('#loaderWidgetUpload').show();
		file = $(elm).find('#postWidgetImage')[0].files[0];
		var form = new FormData();
		form.append("file", file);
		form.append('type', 'post/widget');
		form.append('contentType', file.type);

		uploadFile(form, function(response) {
			if (response != null) {
				getImageUrl(response, function(imageUrlResponse) {

					if (imageUrlResponse != null) {
						$(elm).find('#loaderWidgetUpload').hide();
						var input = {

							postWidget : {
								widgetUrl : imageUrlResponse,
								widgetName : response
							}
						}
						widgetList.push(input);
					} else {
						$(elm).find('#loaderWidgetUpload').hide();
						alert("Failed in uploading widgets");
					}

				});

			}

			else {
				$(elm).find('#loaderWidgetUpload').hide();
				alert("Failed in uploading widgets");
			}

		});
	});

});

$('.add-more-tag')
		.click(
				function() {
					blank = false;
					$(".tag_wrap input").each(function(index, elm) {
						if ($(elm).val() == "") {
							blank = true;
						}
					})
					if (!blank) {
						var fieldHTML = '<div class="input-group control-group add-tag">\n'
								+ '              <div class="row">\n'
								+ '                <div class="col-md-10">\n'
								+ '                  <input type="text" required name="tags[]"\n'
								+ '                         id="tags" class="form-control"\n'
								+ '                         placeholder="Tag name....">\n'
								+ '                </div>\n'
								+ '\n'
								+ '                <div class="col-md-2">\n'
								+ '                  <button class="btn btn-danger remove-tag-btn pull-right"\n'
								+ '                          type="button">\n'
								+ '                    <i class="fa fa-times" aria-hidden="true"></i>\n'
								+ '                  </button>\n'
								+ '                </div>\n'
								+ '              </div>\n'
								+ '\n'
								+ '\n'
								+ '            </div>';
						$('.field_wrapper1').append(fieldHTML);
					}

				});

$('.field_wrapper1').on('click', '.remove-tag-btn', function() {
	$(this).parent().parent('div').remove();

});

function getTags() {
	var stringTag = "";
	$(".tag_wrap input").each(function(index, elm) {
		if (index == 0) {
			stringTag += $(elm).val();
		} else {
			stringTag += "," + $(elm).val();
		}
	});
	return stringTag;
}

function getUpdatedMetaList() {
	list = [];
	$(".metaPanel .metaPanelData").each(function(index, elm) {

		metaType = $(elm).find("#metaType").val();
		metaContent = $(elm).find("#metaContent").val();

		if (metaType.includes("og")) {
			attributeType = 'property';
		} else {
			attributeType = 'name';
		}

		var input = {

			metaInformation : {
				type : metaType,
				content : metaContent,
				attributeType : attributeType
			}
		};

		list.push(input);
	});

	return list;
}

function editPost(postId) {

	var lockRequest = {};
	lockRequest["entityId"] = postId;
	lockRequest["entityType"] = 'POST';

	$.ajax({
		url : apiBasePath + "/acquire-lock",
		type : "POST",
		dataType : "json",
		data : JSON.stringify(lockRequest),
		contentType : "application/json",
		processData : false,

		success : function(response) {

			if (response.success == true) {
				window.location.href = '/admin_writer/posts/' + postId
						+ '/update/';
			} else {
				alert("Entity is being edited by " + response.acquiredBy);
				alertOnEditPost(postId);
			}

		},

		error : function(response) {

			$('#loader').hide();
			alert("Some error occured");
		}
	});
}
