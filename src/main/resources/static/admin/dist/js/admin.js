var breadCrumbParentId = null;
var latestBreadCrumbId = null;
var breadCrumbLevel = 1;
var slugTypeVar;
function generateSlug(name, slugType) {
  slugTypeVar =slugType;
  if (name != null) {
    var nameLower = name.toLowerCase();
    if (document.getElementById('slug') == null) {
      var fieldHTML = '<label>Slug(URL)</label>\n' +
          '            <div class="input-group">\n' +
          '\n' +
          '              <span class="input-group-addon" id="slug-name"></span>\n'
          +
          '              <input id="slug" type="text" class="form-control" name="slug"\n'
          +
          '                     placeholder="slug..." onkeypress="slugExistence(event,this.value,slugTypeVar)">\n'
          +
          '            </div><div id="errMsg"></div>';

      $('.slug_wrapper').append(fieldHTML);

      $('#slug-name').text('https://neostencil.com/' + slugType + '/');

    }
    document.getElementById('slug').value = nameLower.replace(/\s+/g,
        '-').replace(/[^\w-]+/g, '');
  }

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

function uploadFile(fd, callBack) {
  $.ajax({
    "async": true,
    "crossDomain": true,
    "url": apiBasePath + "/user/uploadfile",
    "method": "POST",
//    "headers": {
//      "authorization": "Bearer " + window.localStorage.getItem('access_token'),
//
//    },
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

$('#uploadAttachmentFile').click(function (e) {
  e.preventDefault();
  $('#attachemtLoader').show();
  $('#uploadAttachmentFile').hide();
  var formData = new FormData();
  var filetype = $('#attachment')[0].files[0].type;
  formData.append('file', $('#attachment')[0].files[0]);
  formData.append('type', 'attachments');
  formData.append('contentType', $('#attachment')[0].files[0].type);
  fileName = $('#attachment')[0].files[0].name;
  fileType = $('#attachment')[0].files[0].type;
  if (filetype == 'application/pdf') {

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
        $('#uploadAttachmentFile').show();
        fileUrl = response;
        var  Name = $('#attachment')[0].files[0].name;
        alert("Upload File " + fileName + " " + "successfully");
        $('#copyLinkUrl').attr("value",fileUrl);
      },
      error: function (response) {
        $('#attachemtLoader').hide();
        alert('Failed to Upload File');
        $('#uploadAttachmentFile').show();
      }
    });
  }
  else {
    uploadFile(formData, function (response) {
      if (response != null) {


        getImageUrl(response, function (callback) {
          fileUrl = callback;
          var  fileName = $('#attachment')[0].files[0].name;
          alert("Upload File " + fileName + " " + "successfully");
          $('#copyLinkUrl').attr("value",fileUrl);
          $('#attachemtLoader').hide();
          $('#uploadAttachmentFile').show();

          fileName = $('#attachment')[0].files[0].name;
          fileType = $('#attachment')[0].files[0].type;

        });

      }

      else {
        $('#attachemtLoader').hide();
        alert('Failed to Upload File');
        $('#uploadAttachmentFile').show();

      }

    });

  }
});
function copyAttachmentUrl() {
  var copyText = document.getElementById("copyLinkUrl");
  copyText.select();
  document.execCommand("copy");
}

/**
 *  BreadCrumb creation code from admin dashboard
 */

function createBreadCrumb() {

  if ($('#panel-body .create-breadcrumb').length > 0) {
    $('#panel-body .create-breadcrumb').remove();
  }

  var htmlToAdd = '<div id="new-breadcrumb" style="margin-bottom: 15px"><label>#level#</label><div class="row"><div class="col-md-2">'
      + '<input type="text" required name="beadCrumbText" id="beadCrumbText" class="form-control" placeholder="Text"></div>'
      + '<div class="col-md-6"><input type="text" name="beadCrumbLink" id="beadCrumbLink" class="form-control" placeholder="Link">'
      + '</div><div class="col-md-1">'
      + '<button type="button" class="btn btn-default btn-save-breadcrumb pull-right" id="btn-save-breadcrumb" onclick="postBreadCrumb()">\n'
      + '                     Save BreadCrumb\n'
      + '                  </button>'
      + '</div></div></div>';

  breadCrumbLevel = breadCrumbLevel + 1;

  htmlToAdd = htmlToAdd.replace(
      /#level#/g, 'BreadCrumb Level ' + breadCrumbLevel);

  $('#panel-body').append(htmlToAdd);

}

function postBreadCrumb() {

  blank = false;
  var input;

  $("#new-breadcrumb .row").each(function (index, elm) {
    if ($(elm).find("#beadCrumbText").val() == "") {
      blank = true;
    }
    else {
      text = $(elm).find("#beadCrumbText").val();
      link = $(elm).find("#beadCrumbLink").val();

      input = {
        text: text,
        link: link,
        parentId: (breadCrumbParentId == null) ? 0 : breadCrumbParentId
      };

    }
  });

  if (!blank) {
    $.ajax({
      url: apiBasePath + "/admin_lmsadmin_writer/breadcrumbs",
      type: "post",
      data: JSON.stringify(input),
      dataType: "json",
      contentType: "application/json",
      processData: false,
//      "headers": {
//        "authorization": "Bearer "
//        + window.localStorage.getItem('access_token'),
//
//      },

      success: function (response) {
        breadCrumbParentId = response.breadcrumbId;
        if ($('#panel-body .btn-save-breadcrumb').length > 0) {
          $('#panel-body .btn-save-breadcrumb').remove();
        }
        htmlToAdd = '<button type="button" class="btn btn-default create-breadcrumb"\n'
            + '                        style="margin-top: 8px" onclick="createBreadCrumb()">\n'
            + '                  <span class="glyphicon glyphicon-plus-sign"></span> Create BreadCrumb\n'
            + '                </button>';
        $('#panel-body').append(htmlToAdd);
      },

      error: function (response) {
        alert("Failed in creating breadcrumb")

      }
    });
  }

}

function getBreadCrumb(id) {
  $
  .ajax({
    type: 'GET',
    async: false,
    dataType: 'json',
    url: apiBasePath + "/breadcrumbs/" + id + "/child",
    success: function (response) {
      if (response.length > 0) {
        breadCrumbLevel = breadCrumbLevel + 1;
        var htmlToShow = '<div class="form-group"><label>#level#</label><select name="show-breadCrumb" class="form-control show-breadCrumb" title="Select BreadCrumb"><option value="0" selected disabled>Select BreadCrumb</option>';
        var breadCrumbList = '<option value="#breadcrumbId#">#breadcrumbText#</option>';
        $.each(response, function (index, element) {
          i = index + 1;

          htmlToShow = htmlToShow += breadCrumbList;
          htmlToShow = htmlToShow.replace(
              /#level#/g, 'BreadCrumb Level ' + breadCrumbLevel);
          htmlToShow = htmlToShow.replace(
              /#breadcrumbId#/g, element.breadcrumbId);
          htmlToShow = htmlToShow.replace(
              "#breadcrumbText#",
              element.text);
        });
        htmlToShow = htmlToShow + '</select></div>';
        $('#saved-breadCrumb').append(htmlToShow);
      }
    },

    error: function (response) {
      alert("No BreadCrumb found");
    }
  });

}

$(document).on("change", ".show-breadCrumb", function (event) {
  es = $(this).val();
  breadCrumbParentId = es;
  getBreadCrumb(es)
});

function slugExistence(event, value, slugType) {
  event.preventDefault();
  if (event.keyCode == 13) {
    $
    .ajax({
      async: true,
      type: 'GET',
      url: apiBasePath + "/slug/" + value + "/type/" + slugType + "/exist",
      processData: false,
      success: function (response) {

        if(!response){

          $("div#errMsg").css("color", "green");
          $("div#errMsg").html("Slug is available");
        }

        else{
          $("div#errMsg").css("color", "red");
          $("div#errMsg").html("Slug is not available");
        }
      },

      error: function (response) {
        $("div#errMsg").css("color", "red");
        $("div#errMsg").html("Slug is not available");
      }
    });
  }

}






