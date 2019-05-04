var fileUrl;
var fileName;
var fileType;

var global;
$('.blog-share li').on('click', function () {
  $(this).addClass("is-clicked").siblings().removeClass("is-clicked");
});

function getAllDiscussions() {
  $('#loader-discussions').show();
  $('#all-discussion-list').hide();
  $('#single-discussions').hide();
  $
  .ajax({

    url: apiBasePath + "/discussions",
    type: "get",
    contentType: "application/json",
    success: function (response) {
      $('#loader-discussions').hide();
      $('#all-discussion-list').show();
      if (response != null) {
        var discussionListToShow = '';
        $
        .each(
            response,
            function (index, value) {

              var discussionList = '<div class="mdl-grid"><div class="mdl-cell mdl-cell--2-col mdl-cell--2-col-tablet mdl-cell--2-col-phone" style="width: -moz-fit-content; width: fit-content;" >\n'
                  + '                <img src="#userimage#" >\n'
                  + '              </div>\n'
                  + '              <div class="mdl-cell mdl-cell--10-col mdl-cell--6-col-tablet mdl-cell--8-col-phone" style="margin: 0;">\n'
                  + '                <div class="mdl-grid" style="padding: 0px;">\n'
                  + '                  <div class="mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--8-col-phone" >\n'
                  + '                    <a href="#"><div class="discussion-list-heading"><a href="/discussion/#slug#"><b>#discussionTitle#</b></a></div></a>\n'
                  + '                  </div>\n'
                  + '                  <div class="mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--8-col-phone"style="margin: 0; " >\n'
                  + '                    <div class="mdl-grid" style="padding: 0;padding-left: 8px; font-size: 10px;">\n'
                  + '                      <div class="icon_container"  ><i class="material-icons comment-icon"> import_contacts </i>#examSegment#</div>\n'
                  + '                      <div class="icon_container"><i class="material-icons comment-icon"> person_outline </i>#createdBy#</div>\n'
                  + '                      <div class="icon_container"><i class="material-icons comment-icon"> favorite_border </i>#likes# Likes</div>\n'
                  // + '                      <div class="icon_container"><i class="material-icons comment-icon"> recent_actors </i>Recent by: Abhishek Goenka</div>\n'
                  + '                    </div>\n'
                  + '                  </div>\n'
                  + '                </div>\n'
                  + '              </div></div><hr>';




              discussionListToShow = discussionListToShow + discussionList;
              discussionListToShow = discussionListToShow.replace(
                  "#examSegment#", value.examSegment);
              discussionListToShow = discussionListToShow.replace("#createdBy#",
                  value.user.fullName);

              discussionListToShow = discussionListToShow.replace("#slug#",
                  value.slug);
              if (value.noOfLikes == null) {
                discussionListToShow = discussionListToShow.replace("#likes#",
                    "0");
              } else {
                discussionListToShow = discussionListToShow.replace("#likes#",
                    value.noOfLikes);
              }
              discussionListToShow = discussionListToShow.replace(
                  "#discussionTitle#", value.title);

              if(value.user==null){
                discussionListToShow = discussionListToShow.replace("#userimage#",
                    "https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw=w0-h52-cc");

              }
              else if(value.user.avatar==null){
                discussionListToShow = discussionListToShow.replace("#userimage#",
                    "https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw=w0-h52-cc");

              }
              else{
                discussionListToShow = discussionListToShow.replace("#userimage#",
                    value.user.avatar +"=w0-h52-cc");
              }

            });
      }
      $('#all-discussion-list').html(discussionListToShow);
    },


    error: function (response) {

    }
  });
}


function getUserDiscussions() {
  $('#loader-discussions').show();
  $('#all-discussion-list').hide();
  $('#single-discussions').show();
  $
  .ajax({

    url: apiBasePath + "/user/discussions",
    type: "get",
    contentType: "application/json",
    success: function (response) {
      $('#loader-discussions').hide();
      $('#all-discussion-list').show();
      if (response != null) {
        $('#noDiscussionsError').html("");
        var discussionListToShow = '';
        $
        .each(
            response,
            function (index, value) {

              var discussionList = '<div class="mdl-grid"><div class="mdl-cell mdl-cell--2-col mdl-cell--2-col-tablet mdl-cell--2-col-phone" style="width: -moz-fit-content; width: fit-content;" >\n'
                  + '                <img src="#userimage#" >\n'
                  + '              </div>\n'
                  + '              <div class="mdl-cell mdl-cell--10-col mdl-cell--6-col-tablet mdl-cell--8-col-phone" style="margin: 0;">\n'
                  + '                <div class="mdl-grid" style="padding: 0px;">\n'
                  + '                  <div class="mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--8-col-phone" >\n'
                  + '                    <a href="#"><div class="discussion-list-heading"><a href="/discussion/#slug#"><b>#discussionTitle#</b></a></div></a>\n'
                  + '                  </div>\n'
                  + '                  <div class="mdl-cell mdl-cell--12-col mdl-cell--8-col-tablet mdl-cell--8-col-phone"style="margin: 0; " >\n'
                  + '                    <div class="mdl-grid" style="padding: 0;padding-left: 8px; font-size: 10px;">\n'
                  + '                      <div class="icon_container"  ><i class="material-icons comment-icon"> import_contacts </i>#examSegment#</div>\n'
                  + '                      <div class="icon_container"><i class="material-icons comment-icon"> person_outline </i>#createdBy#</div>\n'
                  + '                      <div class="icon_container"><i class="material-icons comment-icon"> favorite_border </i>#likes# Likes</div>\n'
                  // + '                      <div class="icon_container"><i class="material-icons comment-icon"> recent_actors </i>Recent by: Abhishek Goenka</div>\n'
                  + '                    </div>\n'
                  + '                  </div>\n'
                  + '                </div>\n'
                  + '              </div></div><hr class="hr">';


              discussionListToShow = discussionListToShow + discussionList;
              discussionListToShow = discussionListToShow.replace(
                  "#examSegment#", value.examSegment);
              discussionListToShow = discussionListToShow.replace("#createdBy#",
                  value.user.fullName);

              if(value.user==null){
                discussionListToShow = discussionListToShow.replace("#userimage#",
                    "https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw=w0-h52-cc");

              }
              else if(value.user.avatar==null){
                discussionListToShow = discussionListToShow.replace("#userimage#",
                    "https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw=w0-h52-cc");

              }
              else{
                discussionListToShow = discussionListToShow.replace("#userimage#",
                    value.user.avatar +"=w0-h52-cc");
              }


              discussionListToShow = discussionListToShow.replace("#slug#",
                  value.slug);
              if (value.noOfLikes == null) {
                discussionListToShow = discussionListToShow.replace("#likes#",
                    "0");
              } else {
                discussionListToShow = discussionListToShow.replace("#likes#",
                    value.noOfLikes);
              }
              discussionListToShow = discussionListToShow.replace(
                  "#discussionTitle#", value.title);

            });
      }
      $('#user-discussion-list').html(discussionListToShow);
    },


    error: function (response) {
      $('#noDiscussionsError').show();
    }
  });
}


function loginToComment() {

  window.localStorage.setItem('comment-height',
      $('.drop-a-comment').offset().top);

  if ($(window).width() > 1024) {
    matLoginClick();
  } else {
    window.location.href = "/login";
  }

}

function createNewDiscussion() {
  $('.createBtn').hide();
  $('#loader-create-discussion').show();
  var title = $('#title').val();

  var segment = $('#examsegment').val();
  var content = $('#discussionContent').val();
  var request = {
    title: title,
    examSegment: segment,
    content: content
  };
  if (title != "" && segment != "" && content != "") {
    $
    .ajax({
      url: apiBasePath + "/user/discussions",
      type: "POST",
      data: JSON.stringify(request),
      contentType: "application/json",
      success: function (response) {
    	$('.createBtn').show();
        $('.mdl-dialog__actions').append("<div class='mdl-grid'>Discussion created successfully.</div>");
        $('#title').val("");
        $('#examsegment').val("");
        $('#discussionContent').val("");
        $('#loader-create-discussion').hide();
        setTimeout(function () {

          $('#alertMessage').hide();
          window.location.reload();
        }, 3000)
      },

      error: function (response) {
        $('.createBtn').show();
        $('#loader-create-discussion').hide();

        setTimeout(function () {
          $('#alertMessage').hide()
        }, 3000)
      }
    });
  } else {
    if (title == "") {
      $('#loader-create-discussion').hide();
      $('.createBtn').show();
      $('#errorTitle').show();

    }

    if (segment == "") {
      $('#loader-create-discussion').hide();
      $('.createBtn').show();
      $('#errorSegment').show();
    }

    if (content == "") {
      $('#loader-create-discussion').hide();
      $('.createBtn').show();
      $('#errorContent').show();
    }
  }
}

$('#title').keypress(function () {
  $('#errorTitle').hide();
});
$('#discussionContent').keypress(function () {
  $('#errorContent').hide();
});


function getComments(page) {
  $('#loader-discussions-comments').show();
  $('#loader').show();
  $.ajax({
    async: true,
    crossDomain: true,
    type: 'GET',
    processData: false,
    url: apiBasePath + "/comments/slug/discussion/" + discussionId,
    "headers": {
      "authorization": window.localStorage.getItem('access_token') == null
          ? null : "Bearer "
          + window.localStorage.getItem('access_token'),

    },
    success: function (response) {

      $('#loader-discussions-comments').hide();
      $('#loader').hide();
      $('.no-of-answers').html(response.totalElements + " Replies");

      if (response.content.length > 0) {

        $
        .each(
            response.content,
            function (index, element) {
              addCommentHtml(element);

            });

      }

      if (response.totalElements > (response.pageable.pageNumber + 1)
          * response.pageable.pageSize) {

        var aTag = document.createElement('a');
        aTag.innerHTML = "view more";
        aTag.setAttribute('class', 'view-more-comment');
        aTag.setAttribute('value', response.pageable.pageNumber + 1);
        aTag.setAttribute('id', response.pageable.pageNumber + 1);
        aTag.setAttribute('onclick', 'viewMoreFunction()');
        $('#viewAllComments').append(aTag);
      }
    },
    error: function (element) {

      $('#loader-discussions-comments').hide();
      $('#loader').hide();
    }

  });

}

function postCommentOnDiscussion() {
  $('#post-comment').hide();
  $('#loader').show();
  $('.succesfullyUploaded').hide();
  var text = $('#commentsBox').val();
  var url = fileUrl;


  if (text != "" && text != null) {

    var input = {

      slug: discussionId,
      slugType: 'discussion',
      text: text,
      parentId: 0,
      commentedBy: {
        userId: userId
      },

      attachments: [{
        attachmentName: fileName,
        attachmentUrl: url,
        type: fileType,
      }
      ],
      status: 'publish',
      date: new Date().toJSON().slice(0, 10).replace(/-/g, '/'),
      name: user_name,
      email: username,
      type: 'COMMENT',

    };

    $.ajax({
      url: apiBasePath + "/user/comments",
      type: "post",
      data: JSON.stringify(input),
      dataType: "json",
      contentType: "application/json",
      processData: false,
      "headers": {
        "authorization": window.localStorage.getItem('access_token') == null
            ? null : "Bearer"
            + window.localStorage.getItem('access_token'),

      },

      success: function (response) {
        if (response != null) {
          var totalCommentCount = parseInt($('.comment-count span').text());
          $('.comment-count span').text(totalCommentCount + 1);

          addNewCommentHtml(response);

          $('#commentsBox').val("");
        }
        $('#loader').hide();
        $('#post-comment').show();
        $('#alertMessage').html("comment posted Successfully");

        $('#alertMessage').css("color", "green");
        $('#alertMessage').show();
        setTimeout(function () {
          $('#alertMessage').hide()
        }, 10000)
      },

      error: function (response) {
        $('#loader').hide();
        $('#alertMessage').html("failed while posting comment");

        $('#alertMessage').css("color", "red");
        $('#alertMessage').show();
        setTimeout(function () {
          $('#alertMessage').hide()
        }, 10000)
      }
    });
  } else {
    $('#post-comment').show();
    $('#loader').hide();
    $('#alertMessage').show();
    $('#alertMessage').html('Please write your comment');

    $('#alertMessage').css("color", "red");

  }
}

$("#uploadAttachment").on("change", function (e) {

  e.preventDefault();
  var formData = new FormData();
  var filetype = $('#uploadAttachment')[0].files[0].type;
  formData.append('file', $('#uploadAttachment')[0].files[0]);
  formData.append('type', 'attachments');
  formData.append('contentType', $('#uploadAttachment')[0].files[0].type);
  fileName = $('#uploadAttachment')[0].files[0].name;
  fileType = $('#uploadAttachment')[0].files[0].type;
  if (filetype == 'application/pdf') {
    $('#attachemtLoader').show();
    $.ajax({
      "async": true,
      "crossDomain": true,
      "url": apiBasePath
          + "/user/uploadfile",
      "method": "POST",
      "processData": false,
      "contentType": false,
      "mimeType": "multipart/form-data",
      "data": formData,

      success: function (response) {
        $('#attachemtLoader').hide();
        fileUrl = response;
        var Name = $('#uploadAttachment')[0].files[0].name;
        alert("Upload File " + fileName + " " + "successfully");
      },
      error: function (response) {
        $('#attachemtLoader').hide();
        alert('Failed to Upload File');

      }
    });

  } else {
    uploadFile(formData, function (response) {
      if (response != null) {

        getImageUrl(response, function (callback) {
          fileUrl = callback;
          var fileName = $('#uploadAttachment')[0].files[0].name;
          $('.succesfullyUploaded').show();
          $('#attachemtLoader').hide();

          fileName = $('#uploadAttachment')[0].files[0].name;
          fileType = $('#uploadAttachment')[0].files[0].type;

        });


      } else {
        $('#attachemtLoader').hide();

      }

    });

  }
});

function uploadFile(fd, callBack) {
  $('#attachemtLoader').show();
  $.ajax({
    "async": true,
    "crossDomain": true,
    "url": apiBasePath + "/user/uploadfile",
    "method": "POST",
    "processData": false,
    "contentType": false,
    "mimeType": "multipart/form-data",
    "data": fd,

    success: function (response) {
      if (callBack) {
        return callBack(response);
      }

    },
    error: function (response) {
      alert("Failed in uploading image");
      if (callBack) {
        return callBack(null);
      }
    }
  });
}

function getImageUrl(image, callBack) {
  $
  .ajax({
    method: 'GET',
    url: appEngineImageUrl + "fileName=" + image,
    dataType: "text",
    success: function (response) {
      console.log(response);
      if (callBack) {
        return callBack(response);
      }

    },

    error: function (response) {
      alert("Failed while uploading image");
      if (callBack) {
        return callBack(null);
      }
    }
  });
}


function addCommentHtml(element) {
  var htmlToShow = '<div class="mdl-grid user-comments review" value="#commentId#">\n'
      + '              <div class="mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone">\n'
      + '                <div class="user-profile mdl-grid">\n'
      + '                  <div class="mdl-cell mdl-cell--1-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" style="width: -moz-fit-content;width: fit-content;">\n'
      + '                    <img src="#imagepath#=w50-h52-cc" width ="52px">\n'
      + '                  </div>\n'
      + '                  <div class=" mdl-cell mdl-cell--9-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"style="width: -moz-fit-content;width: fit-content;">\n'
      + '                    <div class="user-name">#name#</div>\n'
      + '                    <div class="last-active">#date#</div>\n'
      + '                  </div>\n'
      + '                </div>\n'
      + '                \n'
      + '                <div class=" mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone answer-descriptions">\n'

      + '                <span class="comment-text ">#text#<div #hidden#><a href="#url#" target="_blank">#attachmentName#</a></div>'
      + '                </span>\n'
      + '                </div>\n'
      + '              </div>\n'
      + '           <div class="comment-action" id="#commentAction#"> #replyToComment# &nbsp;&nbsp; #showReplies# </div>  </div><hr>';
  htmlToShow = htmlToShow
  .replace(
      /#commentId#/g,
      element.commentId);
  htmlToShow = htmlToShow
  .replace(
      /#commentAction#/g,
      'commentAction' + element.commentId);

  if (typeof element.avatar == 'undefined' || element.avatar == null) {
    htmlToShow = htmlToShow
    .replace(
        /#imagepath#/g,
        "https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw");

  } else {
    htmlToShow = htmlToShow
    .replace(
        /#imagepath#/g,
        element.avatar);
  }

  htmlToShow = htmlToShow
  .replace(
      /#id#/g,
      'commentBtn' + element.commentId);
  htmlToShow = htmlToShow
  .replace(
      /#buttonId#/g,
      'button' + element.commentId);
  htmlToShow = htmlToShow
  .replace(
      /#name#/g,
      element.name);
  htmlToShow = htmlToShow
  .replace(
      "#date#", Date.parse(element.date.substring(0, 16)).toString(
          ('MMMM dS, yyyy HH:mm')));

  htmlToShow = htmlToShow
  .replace(
      "#text#",
      element.text);

  if (element.attachments != null && element.attachments.length > 0) {
    $.each(element.attachments, function (index, element) {

      if (element.attachmentUrl != null) {
        htmlToShow = htmlToShow
        .replace(/#url#/g,
            element.attachmentUrl);
        if (element.type == "application/pdf") {

          htmlToShow = htmlToShow
          .replace(
              /#attachmentName#/g,
              "<span>" + element.attachmentName + "</span><iframe src ='"
              + element.attachmentUrl + "'></iframe>");

        } else {
          htmlToShow = htmlToShow
          .replace(
              /#attachmentName#/g,
              "<img src ='" + element.attachmentUrl + "' width ='80%'/>");
        }
        htmlToShow = htmlToShow
        .replace(
            "#hidden#", '');


      } else {
        htmlToShow = htmlToShow
        .replace(
            "#hidden#",
            'hidden');
      }
    });

  } else {
    htmlToShow = htmlToShow
    .replace(
        "#hidden#", 'hidden');
  }

  htmlToShow = htmlToShow
  .replace(
      "#replyToComment#", '<button id="commentBtn' + element.commentId
      + '" value="'
      + element.commentId + '" onclick="onClickReplyBtn(' + element.commentId
      + ',\'comment\','
      + element.commentId + ')" class="reply-to-comment">Reply</button>');
  if (element.reply_count > 1) {
    htmlToShow = htmlToShow
    .replace(
        "#showReplies#", '<button class="reply-count" id="' + element.commentId
        + '" onclick="showReply(' + element.commentId
        + ',0,\'countBtnClick\')" value="'
        + element.commentId + '">' + element.reply_count + ' Replies</button>');
  } else if (element.reply_count == 1) {
    htmlToShow = htmlToShow
    .replace(
        "#showReplies#", '<button class="reply-count" id="' + element.commentId
        + '" onclick="showReply(' + element.commentId
        + ',0,\'countBtnClick\')" value="'
        + element.commentId + '">' + element.reply_count + ' Reply</button>');
  } else {
    htmlToShow = htmlToShow
    .replace(
        "#showReplies#", '');
  }

  $('#viewAllComments').append(htmlToShow);
  var commentElement = document.getElementById(element.commentId);
  var htmlDivElement = document.createElement("div");
  htmlDivElement.id = 'reply' + element.commentId;
  htmlDivElement.className = 'reply_wrapper';
  htmlDivElement.value = element.commentId;
  $('#commentAction' + element.commentId).append(htmlDivElement);

}

function addNewCommentHtml(element) {
  var htmlToShow = '<div class="mdl-grid user-comments review" value="#commentId#">\n'
      + '              <div class="mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone">\n'
      + '                <div class="user-profile mdl-grid">\n'
      + '                  <div class="mdl-cell mdl-cell--1-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" style="width: -moz-fit-content;width: fit-content;">\n'
      + '                    <img src="#imagepath#=w50-h52-cc" width ="52px">\n'
      + '                  </div>\n'
      + '                  <div class=" mdl-cell mdl-cell--9-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"style="width: -moz-fit-content;width: fit-content;">\n'
      + '                    <div class="user-name">#name#</div>\n'
      + '                    <div class="last-active">#date#</div>\n'
      + '                  </div>\n'
      + '                </div>\n'
      + '                \n'
      + '                <div class=" mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone answer-descriptions">\n'
      + '                <span class="comment-text ">#text#<span #hidden#><a href="#url#" target="_blank">#attachmentName#</a></span>'
      + '                </span>\n'
      + '                </div>\n'
      + '              </div>\n'

      + '           <div class="comment-action" id="#commentAction#"> #replyToComment# &nbsp;&nbsp; #showReplies# </div>  </div> <hr>';
  htmlToShow = htmlToShow
  .replace(
      /#commentId#/g,
      element.commentId);
  htmlToShow = htmlToShow
  .replace(
      /#commentAction#/g,
      'commentAction' + element.commentId);

  if (typeof element.avatar == 'undefined' || element.avatar == null) {
    htmlToShow = htmlToShow
    .replace(
        /#imagepath#/g,
        "https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw");

  } else {
    htmlToShow = htmlToShow
    .replace(
        /#imagepath#/g,
        element.avatar);
  }

  htmlToShow = htmlToShow
  .replace(
      /#id#/g,
      'commentBtn' + element.commentId);
  htmlToShow = htmlToShow
  .replace(
      /#buttonId#/g,
      'button' + element.commentId);
  htmlToShow = htmlToShow
  .replace(
      /#name#/g,
      element.name);
  htmlToShow = htmlToShow
  .replace(
      "#date#", Date.parse(element.date.substring(0, 16)).toString(
          ('MMMM dS, yyyy HH:mm')));

  htmlToShow = htmlToShow
  .replace(
      "#text#",
      element.text);

  if (element.attachments != null && element.attachments.length > 0) {
    $.each(element.attachments, function (index, element) {

      if (element.attachmentUrl != null) {
        htmlToShow = htmlToShow
        .replace(/#url#/g,
            element.attachmentUrl);
        if (element.type == "application/pdf") {

          htmlToShow = htmlToShow
          .replace(
              /#attachmentName#/g,
              "<span>" + element.attachmentName + "</span><iframe src ='"
              + element.attachmentUrl + "'></iframe>");

        } else {
          htmlToShow = htmlToShow
          .replace(
              /#attachmentName#/g,
              "<img src ='" + element.attachmentUrl + "' width ='80%'/>");
        }
        htmlToShow = htmlToShow
        .replace(
            "#hidden#", '');


      } else {
        htmlToShow = htmlToShow
        .replace(
            "#hidden#",
            'hidden');
      }
    });

  } else {
    htmlToShow = htmlToShow
    .replace(
        "#hidden#", 'hidden');
  }

  htmlToShow = htmlToShow
  .replace(
      "#replyToComment#", '<button id="commentBtn' + element.commentId
      + '" value="'
      + element.commentId + '" onclick="onClickReplyBtn(' + element.commentId
      + ',\'comment\','
      + element.commentId + ')" class="reply-to-comment">Reply</button>');
  if (element.reply_count > 1) {
    htmlToShow = htmlToShow
    .replace(
        "#showReplies#", '<button class="reply-count" id="' + element.commentId
        + '" onclick="showReply(' + element.commentId
        + ',0,\'countBtnClick\')" value="'
        + element.commentId + '">' + element.reply_count + ' Replies</button>');
  } else if (element.reply_count == 1) {
    htmlToShow = htmlToShow
    .replace(
        "#showReplies#", '<button class="reply-count" id="' + element.commentId
        + '" onclick="showReply(' + element.commentId
        + ',0,\'countBtnClick\')" value="'
        + element.commentId + '">' + element.reply_count + ' Reply</button>');
  } else {
    htmlToShow = htmlToShow
    .replace(
        "#showReplies#", '');
  }

  $('#viewAllComments').prepend(htmlToShow);
  var commentElement = document.getElementById(element.commentId);
  var htmlDivElement = document.createElement("div");
  htmlDivElement.id = 'reply' + element.commentId;
  htmlDivElement.className = 'reply_wrapper';
  htmlDivElement.value = 'element.commentId';
  $('#commentAction' + element.commentId).append(htmlDivElement);

}

$('#commentsBox').keypress(function () {
  $('#alertMessage').hide();
});

function onClickReplyBtn(newCommentId, type, oldCommentId) {

  if (isUserLoggedIn() == false) {
    if ($(window).width() > 1024) {
      matLoginClick();
    } else {
      window.location.href = "/login";
    }
  } else {
    global = oldCommentId;

    if ($('#post-reply').length) {
      $('#post-reply').remove();
    }

    if (type == 'comment') {

      if ($('#' + oldCommentId).length > 0) {
        showReply(newCommentId, 0, 'reply-btn-click');

      } else {
        $('#reply'
            + newCommentId).append(
            '<form name="post-reply" id="post-reply" class="post-reply">\n'
            +
            '\n' +
            '                                <div class="mdl-grid" style="padding: 0px">\n'
            +
            '                                    <div\n' +
            '                                            class="mdl-cell mdl-cell--5-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"\n'
            +
            '                                            style="width: 100%; margin: 0px">\n'
            +
            '                                        <label for="username_login">Reply</label><br/><textarea rows="7" type="text" name="replyBox" id="replyBox" style="width: 100%"\n'
            +
            '                                                  /><span id="reply-parent-id" hidden="hidden"></span>\n'
            +
            '                                    </div>\n' +
            '                                </div><button id="post-reply-btn" class="post-reply-btn">POST REPLY</button>\n'
            +
            '\n' +
            '                            </form>');
      }

      $('#commentAction' + newCommentId + ' .reply-to-comment').remove();

      if ($('#commentAction' + newCommentId + ' .reply-count').length > 0) {
        $('#commentAction' + newCommentId + ' .reply-count').remove();
      }

    } else {
      if ($('#replyBtn' + newCommentId).length > 0) {

        $('#replyBtn'
            + newCommentId).replaceWith(
            '<form name="post-reply" id="post-reply">\n'
            +
            '\n' +
            '                                <div class="mdl-grid" style="padding: 0px">\n'
            +
            '                                    <div\n' +
            '                                            class="mdl-cell mdl-cell--5-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"\n'
            +
            '                                            style="width: 100%; margin: 0px">\n'
            +
            '                                        <label for="username_login">Reply</label><br/><textarea rows="7" type="text" name="replyBox" id="replyBox" style="width: 100%"\n'
            +
            '                                                 /><span id="reply-parent-id" hidden="hidden"></span>\n'
            +
            '                                    </div>\n' +
            '                                </div><button id="post-reply-btn" class="post-reply-btn">POST REPLY</button>\n'
            +
            '\n' +
            '                            </form>');

        $('#reply-parent-id').text(oldCommentId);
      }
    }
  }
}

$(document).on('submit', "#post-reply", function (event) {
  event.preventDefault();

  var input = {
    slug: discussionId,
    slugType: 'discussion',
    name: user_name,
    email: username,
    text: $('#replyBox').val(),
    parentId: global,
    commentedBy: {
      userId: userId
    },
    status: 'publish',
    date: new Date().toJSON().slice(0, 10).replace(/-/g, '/'),
  };

  $.ajax({
    url: apiBasePath + "/user/comments",
    type: "post",
    data: JSON.stringify(input),
    dataType: "json",
    contentType: "application/json",
    processData: false,
    "headers": {
      "authorization": window.localStorage.getItem('access_token') == null
          ? null : "Bearer "
          + window.localStorage.getItem('access_token'),

    },

    success: function (response) {

      if ($('#post-reply').length) {
        $('#post-reply').remove();
      }

      var viewMoreBtnExit = $('#' + response.parentId).length;
      var pageNum = $('#' + response.parentId + '.view-more-reply').attr(
          'value');
      var id = $('#' + response.parentId + '.view-more-reply').attr('id');

      if ($('#replyAction' + response.parentId).length > 0) {
        $('#replyAction' + response.parentId).remove();
      }

      var htmlToShow = '<div class="mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" id="#replyId#">\n'
          + '                <div class="user-profile mdl-grid">\n'
          + '                  <div class="mdl-cell mdl-cell--1-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" style="width: -moz-fit-content;width: fit-content;">\n'
          + '                    <img src="#imagepath#" width="52px">\n'
          + '                  </div>\n'
          + '                  <div class=" mdl-cell mdl-cell--9-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" style="width: -moz-fit-content;width: fit-content;">\n'
          + '                    <div class="user-name">#name#</div>\n'
          + '                    <div class="last-active">#date#</div>\n'
          + '                  </div>\n'
          + '                </div>\n'
          + '                \n'
          + '                <div class=" mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone answer-descriptions">\n'
          + '                <span class="comment-text ">#text#</span>\n'
          + '                </div>\n'
          + '              </div>';

      if (response.avatar == null || typeof response.avatar == 'undefined') {
        htmlToShow = htmlToShow
        .replace(
            /#imagepath#/g,
            "https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw=cc");
      } else {
        htmlToShow = htmlToShow
        .replace(
            /#imagepath#/g,
            response.avatar + "=cc-s64");
      }

      htmlToShow = htmlToShow
      .replace(
          /#replyId#/g,
          'reply' + response.commentId);
      htmlToShow = htmlToShow
      .replace(
          /#name#/g,
          response.name);

      htmlToShow = htmlToShow
      .replace(
          "#date#", Date.parse(response.date.substring(0, 16)).toString(
              ('MMMM dS, yyyy HH:mm')));

      htmlToShow = htmlToShow
      .replace(
          "#text#",
          response.text);

      $('#reply' + response.parentId).append(htmlToShow);

      var html = '<div class="comment-action" id="#replyAction#"><button class="reply-to" onclick="onClickReplyBtn(this.value,\'reply\',$(\'#\'+ this.id).parent().parent().parent().parent().attr(\'value\'))" value="#commentId#"  id="#Id#">Reply</button></div>';
      html = html
      .replace(
          /#Id#/g,
          'replyBtn' + response.commentId);

      html = html
      .replace(
          /#commentId#/g,
          response.commentId);
      html = html
      .replace(
          /#replyAction#/g,
          'replyAction' + response.parentId);

      $('#reply' + response.parentId).append(html);

      if (viewMoreBtnExit > 0) {

        var br = document.createElement("br");
        $('#replyAction' + response.parentId).append(br);
        var aTag = document.createElement('a');
        aTag.innerHTML = "view more";
        aTag.setAttribute('class', 'view-more-reply');
        aTag.setAttribute('value', pageNum);
        aTag.setAttribute('id', id);
        aTag.setAttribute('onclick', 'viewMoreReplyFunction()');
        $('#replyAction' + response.parentId).append(aTag);

      }

    },

    error: function (response) {
      alert('error');
    }
  });

});

function showReply(commentId, page) {

  if ($('#' + commentId + '.view-more-reply').length > 0) {
    $('#' + commentId + '.view-more-reply').remove();
  }
  $('#reply' + commentId).append(
      '<i class="fa fa-spinner fa-spin loader"></i>');
  $.ajax({
    async: true,
    crossDomain: true,
    type: 'GET',
    processData: false,
    url: apiBasePath + "/comments/" + commentId + "?page=" + page + "&&size=4",
    "headers": {
      "authorization": window.localStorage.getItem('access_token') == null
          ? null : "Bearer "
          + window.localStorage.getItem('access_token'),

    },
    success: function (response) {
      $('#reply' + commentId + ' .loader').remove();

      if ($('#commentAction' + commentId + ' .reply-count').length > 0) {
        $('#commentAction' + commentId + ' .reply-count').remove();
        $('#commentAction' + commentId + ' .reply-to-comment').remove();
      }

      if (response.content.length > 0) {
        $
        .each(
            response.content,
            function (index, element) {

              if ($('#reply' + element.commentId).length == 0) {
                var htmlToShow = '<div class="mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" id="#replyId#">\n'
                    + '                <div class="user-profile mdl-grid">\n'
                    + '                  <div class="mdl-cell mdl-cell--1-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" style="width: -moz-fit-content;width: fit-content;">\n'
                    + '                    <img src="#imagepath#" width="52px">\n'
                    + '                  </div>\n'
                    + '                  <div class=" mdl-cell mdl-cell--9-col mdl-cell--12-col-tablet mdl-cell--12-col-phone" style="width: -moz-fit-content;width: fit-content;">\n'
                    + '                    <div class="user-name">#name#</div>\n'
                    + '                    <div class="last-active">#date#</div>\n'
                    + '                  </div>\n'
                    + '                </div>\n'
                    + '                \n'
                    + '                <div class=" mdl-cell mdl-cell--12-col mdl-cell--12-col-tablet mdl-cell--12-col-phone answer-descriptions">\n'
                    + '                <span class="comment-text ">#text#</span>\n'
                    + '                </div>\n'
                    + '              </div>';

                htmlToShow = htmlToShow
                .replace(
                    /#replyId#/g,
                    'reply' + element.commentId);

                if (typeof element.avatar == 'undefined' || element.avatar
                    == null) {
                  htmlToShow = htmlToShow
                  .replace(
                      /#imagepath#/g,
                      "https://lh3.googleusercontent.com/Z4uHbjIjELCl1WaD_fGK7VnkE9sXzT_b4SLAmmapf7JbmtR9pNF17JW58z3vA7JsRuCGl0BiovQc7zuHiuOp6PZi7Cw=cc");
                } else {
                  htmlToShow = htmlToShow
                  .replace(
                      /#imagepath#/g,
                      element.avatar + "=cc-s64");
                }

                htmlToShow = htmlToShow
                .replace(
                    /#name#/g,
                    element.name);
                htmlToShow = htmlToShow
                .replace(
                    "#date#",
                    Date.parse(element.date.substring(0, 16)).toString(
                        ('MMMM dS, yyyy HH:mm')));

                htmlToShow = htmlToShow
                .replace(
                    "#text#",
                    element.text);

                $('#reply' + commentId).append(htmlToShow);
              }
            });
      }

      if (response.content.length > 0) {

        var html = '<div class="comment-action" id="#replyAction#"><button class="reply-to" onclick="onClickReplyBtn(this.value,\'reply\',this.value)" value="#commentId#"  id="#Id#">Reply</button></div>';
        html = html
        .replace(
            /#Id#/g,
            'replyBtn' + commentId);
        html = html
        .replace(
            /#commentId#/g,
            commentId);
        html = html
        .replace(
            /#replyAction#/g,
            'replyAction' + commentId);
        $('#reply' + commentId).append(html);
      }

      if (response.totalElements > (response.pageable.pageNumber + 1)
          * response.pageable.pageSize) {

        var br = document.createElement("br");
        $('#replyAction' + commentId).append(br);
        var aTag = document.createElement('a');
        aTag.innerHTML = "view more";
        aTag.setAttribute('class', 'view-more-reply');
        aTag.setAttribute('value', response.pageable.pageNumber + 1);
        aTag.setAttribute('id', commentId);
        aTag.setAttribute('onclick', 'viewMoreReplyFunction()');
        $('#replyAction' + commentId).append(aTag);

      }

      // if (flag == 'reply-btn-click') {
      //     $('#replyBtn' + commentId).replaceWith('<form name="post-reply" id="post-reply">\n' +
      //         '\n' +
      //         '                                <div class="mdl-grid" style="padding: 0px">\n' +
      //         '                                    <div\n' +
      //         '                                            class="mdl-cell mdl-cell--5-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"\n'
      //         +
      //         '                                            style="width: 100%; margin: 0px">\n' +
      //         '                                        <label for="username_login">Reply</label><br/><textarea rows="7" type="text" name="replyBox" id="replyBox" style="width: 100%"\n'
      //         +
      //         '                                                  /><span id="reply-parent-id" hidden="hidden"></span>\n'
      //         +
      //         '                                    </div>\n' +
      //         '                                </div><button id="post-reply-btn" class="post-reply-btn">POST REPLY</button>\n'
      //         +
      //         '\n' +
      //         '                            </form>');
      //
      //     $('#reply-parent-id').text(commentId);
      //     var latestCommentElement = document.getElementById('post-reply');
      //     var offsetTop = latestCommentElement.offsetTop;
      //     document.getElementById('mat-content').scrollTop = offsetTop - 50;
      // }

    },

    error: function (response) {
      $('#reply' + commentId + ' .loader').hide();
    }
  });

  if ($('#post-reply').length) {
    $('#post-reply').remove();
  }

}

function viewMoreReplyFunction() {
  var val = $('.view-more-reply').attr('value');
  var commentId = $('.view-more-reply').attr('id');
  $('#' + commentId).remove();

  $('#reply' + commentId).find('.comment-action').remove();

  showReply(commentId, parseInt(val), 'a');
};

$('.postCommentBtn').on('click', function () {
  if (isUserLoggedIn() == true) {
    $('.drop-a-comment').toggle();
  } else {
    if ($(window).width() > 1024) {
      matLoginClick();
    } else {
      window.location.href = "/login";
    }
  }

});

function openDiscussionDialog() {
  if (isUserLoggedIn() == true) {
    var dialog = document.querySelector('#createNewDiscussion');
    if (!dialog.showModal) {
      dialogPolyfill.registerDialog(dialog);
    }
    dialog.showModal();
    dialog.querySelector('#closeBtn')
    .addEventListener('click', function () {
      dialog.close();
    });
  } else {
    if ($(window).width() > 1024) {
      matLoginClick();
    } else {
      window.location.href = "/login";
    }
  }

}
