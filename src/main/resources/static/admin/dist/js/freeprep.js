var metaInfoList = [];
var bookletList = [];
var postFeatureImageUrl;


$(document).ready(function() {
	$('#summernote').summernote({
		height : 500, // set editor height
		minHeight : null, // set minimum height of editor
		maxHeight : null, // set maximum height of editor
		focus : true
	// set focus to editable area after initializing summernote
	});
	var markupStr = $('#summernote').summernote('code');

	$('#addMetaPanel').hide();
	$('#metaPanel').hide();
	$('#pnlBooklet').hide();
});
// submit post creation form
$("form[name='add-new-post']").validate(
		{
			submitHandler : function(form, e) {
				/*
				 * if (document.getElementById('popular').checked) {
				 * $('#free').attr('checked', true);
				 * $('input[name="popular"]').val(true); } else {
				 * $('#free').attr('checked', false);
				 * $('input[name="popular"]').val(false); } if
				 * (document.getElementById('featured').checked) {
				 * $('#free').attr('checked', true);
				 * $('input[name="featured"]').val(true); } else {
				 * $('#free').attr('checked', false);
				 * $('input[name="featured"]').val(false); }
				 */
				$("#add-new-post-error").html('');
				$("#add-new-post-text-error").hide();
				$("#add-new-post-text-error").html('');
				e.preventDefault();
				if ($('#summernote').summernote('isEmpty')) {
					$("#add-new-post-text-error").show();
					$("#add-new-post-text-error").html(
							"This is a required field");
					return;
				}

        var featuredImageReq = {};

        featuredImageReq["url"] = postFeatureImageUrl;
        featuredImageReq["name"] = $('#featureImage')[0].files[0].name;
        featuredImageReq["title"] = $('#txtFITitle').val();
        featuredImageReq["caption"] = $('#txtFICaption').val();
        featuredImageReq["altText"] = $('#txtFIAltText').val();
        featuredImageReq["description"] = $('#txtFIDescription').val();


				var postPayload = {
					postId : $('#post-id').val(),
					title : $('#post-title').val(),
					text : $('#summernote').summernote('code'),
					status : $('input[name=status]:checked', '#add-new-post').val() == undefined ? 'draft': $('input[name=status]:checked', '#add-new-post').val(),
					category : $("form[name='add-new-post']").find(
							"#post-category :selected").val(),
					postSubCategory : $("form[name='add-new-post']").find(
							"#post-sub-category :selected").val(),
					featured : $('#featured').val(),
					popular : $('#popular').val(),
					// previousPost : {
					// 	slug : 'dummyPrev'
					// },
					// nextPost : {
					// 	slug : 'dummyNext'
					// },
					tags:getTags(),
					relatedPosts : getRelatedPosts(),
          metaList: [{
            type: 'keyword',
            content: $('#titleKeyWord').val(),

          },
            {
              type: 'description',
              content: $('#txtMetaDescription').val()
            }
          ],
          titleTag: $('#titleTag').val(),
          featuredImage: featuredImageReq,
					type : 'FREE_PREP'

				};

				console.log(postPayload);
				$('#loader').show();
				$.ajax({
					url : apiBasePath + "/admin_writer/posts",
					type : "POST",
					data : JSON.stringify(postPayload),
					contentType : "application/json",
					processData : false,
//					"headers" : {
//						"authorization" : "Bearer "
//								+ window.localStorage.getItem('access_token'),
//
//					},

					success : function(response) {
						alert("Post Added Successfully");
						location.reload();

					},

					error : function(response) {

						if (response.status == 403) {
							window.location.href = "/permission-error";
						} else {
							alert("Post Creation Failed");
						}

					}
				});
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
//		"headers" : {
//			"authorization" : "Bearer "
//					+ window.localStorage.getItem('access_token'),
//
//		},

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
function getRelatedPosts() {
	list = [];

	$("#related-posts").val().forEach(function(value, index) {
		list.push({
			postSummaryId : value
		});
	});

	return list;
}

function showAddMetaPanel() {
	$('#addMetaPanel').show();
}

function addMetaInfo() {
	var meta = {};

	meta["type"] = $('#ddMetaType').val();
	meta["content"] = $('#txtMetaContent').val();

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

function showBookletPanel() {
	$('#pnlBooklet').show();
}

function onSaveBooklet() {
	var booklet = {};

	booklet["bookletTitle"] = $('#txtBookletTitle').val();
	booklet["contentUrl"] = $('#txtBookletContent').val();

	bookletList.push(booklet);

	$('#txtBookletTitle').prop('value', '');
	$('#txtBookletContent').prop('value', '');

	$('#pnlBooklet').hide();

	showBooklets();

}
function showBooklets() {
	htmlToShow = '';
	htmlBeg = '<div class="row">';
	htmlRow = '<div style="margin-top: 20px;" class="col-md-9"><label>Booklet Title:</label> <input type="text" id="txtTitle"	class="form-control" value="#bookTitle#" readonly="readonly"></div>	<div style="margin-top: 20px;" class="col-md-9"><label> Upload Booklet:</label> <input type="text"	placeholder="Enter Booklet URL" id="txtContent"	class="form-control" value="#bookContent#" readonly="readonly"></div>'
	htmlEnd = '</div><br/>';
	if (bookletList != null && bookletList.length > 0) {
		htmlToShow += htmlBeg;

		$.each(bookletList, function(index, element) {
			htmlToShow += htmlRow;
			htmlToShow = htmlToShow
					.replace("#bookTitle#", element.bookletTitle);
			htmlToShow = htmlToShow
					.replace("#bookContent#", element.contentUrl);

		});
		htmlToShow += htmlEnd;

		$('#booklets').html(htmlToShow).show();
	}

}

function onSubmit() {
	var answerSheet = {};

	answerSheet["title"] = $('#sheet-title').val();
	answerSheet["category"] = $('#sheet-category').val();
	answerSheet["booklets"] = bookletList;

	$.ajax({
		url : apiBasePath + "/admin_writer/freeprep/answersheets",
		type : "POST",
		data : JSON.stringify(answerSheet),
		dataType : "json",
		contentType : "application/json",
		processData : false,
//		"headers" : {
//			"authorization" : "Bearer "
//					+ window.localStorage.getItem('access_token'),
//
//		},

		success : function(response) {

			alert("AnswerSheet added successfully");
			location.reload();

		},

		error : function(response) {

			if (response.status == 403) {
				window.location.href = "/permission-error";
			} else {
				alert("AnswerSheet Creation Failed");
			}

		}
	});

}

$("#but_feature_upload").click(function () {
  $('#loaderFeatureUpload').show();
  $('#but_feature_upload').hide();

  var files = $('#featureImage')[0].files[0];
  var fd = new FormData();
  fd.append('file', files);
  fd.append('type', "featureImage/post");
  fd.append('contentType', files.type);

  uploadFile(fd, function (response) {
    if (response != null) {
      getImageUrl(response, function (imageUrlResponse) {

        if (imageUrlResponse != null) {
          $('#loaderFeatureUpload').hide();
          var reader = new FileReader();
          reader.onload = function (e) {
            $('#upload-feature-img-tag').attr('src', e.target.result);
          };
          reader.readAsDataURL($('#featureImage')[0].files[0]);
          postFeatureImageUrl = imageUrlResponse;
        }
        else {
          $('#loaderFeatureUpload').hide();
          $('#but_feature_upload').show();
        }
      });

    }

    else {
      $('#loaderFeatureUpload').hide();
      $('#but_feature_upload').show();
    }

  });

});

$('.add-more-tag')
.click(
    function () {
      blank = false;
      $(".tag_wrap input").each(
          function (index, elm) {
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

$('.field_wrapper1').on('click', '.remove-tag-btn', function () {
  $(this).parent().parent('div').remove();

});

function getTags() {
  list = [];
  $(".tag_wrap input").each(function (index, elm) {

    list.push($(elm).val());
  });
  return list;
}
