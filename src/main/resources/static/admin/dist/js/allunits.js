$(document).ready(



    function () {

      var table = $('#unit-table').DataTable({
        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "columnDefs": [ {
          "targets": [1],
          "orderable": false,
        } ],
        "rowReorder": true
      });

       table.order([1, 'desc']).draw();

      $('#divLinkUnit').hide();
      $('#dropCourses').hide();
      $('#btnLink').hide();
      $('#divCourses').hide();
      // $('#divCourseBatches').hide();
      // getCourseBatch();
      $('#price').hide();
      $('#lblPrice').hide();
      $('#releaseDate').datepicker({
        format: 'dd-mm-yyyy'
      });

      $('#uploadDate').datepicker({
        format: 'dd-mm-yyyy'
      });
      $('#txtTopic').hide();
      $('select[name=unitType]').change(function () {

        if ($(this).val() == 'LECTURE') {
          $('#lectureModel').modal("show");
          fetchAllMacro();

        }

      });

      $('#addLectureBtn').on('click', function () {
        var lectureInput = {
          url: $('#url').val(),
          releaseDate: $('#releaseDate').val(),
          duration: $('#duration').val(),
          heading: $('#heading').val(),
          footerNotes: $('#footerNotes').summernote('code'),
          jwMacro: {name: $('#jwMacro').val()}
        };


        $.ajax({
          url: apiBasePath + "/admin_techops/lectures",
          type: "post",
          data: JSON.stringify(lectureInput),
          dataType: "json",
          contentType: "application/json",
          processData: false,
          success: function (response) {
            $('#lectureModel').modal("hide");
            $('#lectureForm')[0].reset();
            lms = response;
            alert("Successfully create lecture");

          },
          error: function (response) {

            alert("Failed creating lecture");

          }
        });

      });
    });

var selectedUnits = [];
var selectedBatches= [];



  // $('#select-all-units').onchange(function() {
  // $('input[name=select-unit]:checked').each(function(i) { selectedUnits[i] =
  // $(this).val(); });  selectCourse(selectedCourses); });
$('body').on('change', '#select-all-units', function() {
  var rows, checked;
  rows = $('#unit-table').find('tbody tr');
  checked = $(this).prop('checked');
  $.each(rows, function() {
    var selectAllCheckbox = $($(this).find('td').eq(1)).find('input').prop('checked', checked);
  });
  var selectedUnitsCheckbox = [];
  $('input[name=select-unit]:checked').each(function (i) {
    selectedUnitsCheckbox[i] = $(this).val();

  });
  selectedUnits = selectedUnitsCheckbox;

  if (selectedUnitsCheckbox != null && selectedUnitsCheckbox.length > 0) {
    $('#divLinkUnit').show();
  } else {
    $('#divLinkUnit').hide();
  }

});


function linkUnit() {
  $('#divCourses').show();
  $('#dropCourses').show();
}

$('.select-unit').change(function () {
  // $('#divLinkUnit').show();
  var selectedUnitsCheckbox = [];
  $('input[name=select-unit]:checked').each(function (i) {
    selectedUnitsCheckbox[i] = $(this).val();

  });
  selectedUnits = selectedUnitsCheckbox;

  if (selectedUnitsCheckbox != null && selectedUnitsCheckbox.length > 0) {
    $('#divLinkUnit').show();
  } else {
    $('#divLinkUnit').hide();
  }

});

$('#dropCourses').change(function () {
  var selectedCourses = [];

  $('#dropCourses').val().forEach(function (value, index) {
    selectedCourses[index] = value;
  });
  selectCourse(selectedCourses);
});

function selectCourse(selectedCourses) {
  var batchNameRequest = {};
  batchNameRequest["courseIds"] = selectedCourses;

  $
  .ajax({
    type: 'GET',
    async: false,
    dataType: 'json',
    url: apiBasePath + "/coursebatches/names?batchNameRequest="
    + encodeURIComponent(JSON.stringify(batchNameRequest)),
    success: function (response) {

      if($('#dropCourseBatches').length>0){
        $('#dropCourseBatches').remove();
      }
      var htmlBeg = '<label> Course Batches</label> <select class="form-control selectpicker" multiple id="dropCourseBatches" onchange="selectCourseBatch();" data-live-search="true">';
      var htmlToShow = '';
      var batchesList = '<option value="#batchId#">#batchName# | #courseName# | #id#</option>';
      var htmlEnd = '</select> <div id="linkBtnDiv"><button type="button" class="btn btn-primary" id="btnLinkUnit" onclick="linkToBatches();">Link</button></div>';
      htmlToShow += htmlBeg;
      if (response.length > 0) {
        $.each(response, function (index, element) {
          i = index + 1;
          htmlToShow = htmlToShow += batchesList;
          htmlToShow = htmlToShow.replace("#batchId#",
              element.batchId);
          htmlToShow = htmlToShow.replace("#id#",
              element.batchId);
          htmlToShow = htmlToShow.replace("#batchName#",
              element.batchName);
          htmlToShow = htmlToShow.replace("#courseName#",
              element.courseName);

        });
        htmlToShow = htmlToShow += htmlEnd;
        // $('#dropCourseBatches').html(htmlToShow);
        $('#divCourseBatches').html(htmlToShow).show();
        $('#dropCourseBatches').selectpicker();
        // $('#dropCourseBatches').show();

      }
    },
    error: function (response) {
      alert("Error occured while fetching batchnames");
    }
  });
}



  // $('#btnLink').hide();
  var selectBatches =  [];
  $("#dropCourseBatches").change(function () {
    selectBatches.push($(this).val());
  });
  selectedBatches = selectBatches;


function linkToBatches() {
  var selectedBatches = [];
  $("#dropCourseBatches").val().forEach(function (value, index) {
    selectedBatches[index] = value;
  });

  var request = {};
  /*
   * var units=[]; if(selectedUnits!=null && selectedUnits.length>0) { var
   * unit={}; unit["unitId"]= }
   */
  request["units"] = selectedUnits;
  request["batches"] = selectedBatches;
  $.ajax({
    type: 'PUT',
    async: false,
    dataType: 'json',
    contentType: 'application/json',
    crossDomain: true,

    data: JSON.stringify(request),
    url: apiBasePath + "/coursebatches/units/link",
    success: function (response) {

      alert("Units linked successfully");
      location.reload();

    },
    error: function (response) {
      alert("Error occured while linking units");
    }
  });

}


$('#lectureModel').on('shown.bs.modal', function () {
  $('.input-group.date').datepicker({
    format: "dd-mm-yyyy",
    autoclose: true,
    todayHighlight: true,
    container: '#lectureModel modal-body'
  });
});


$('#newUnit').validate({

  rules: {
    getBatchData: {
      required: true
    },

    unitType: {
      required: true
    }
  },

  messages: {
    getBatchData: {
      required: "Please select course batch"
    },

    unitType: {
      required: "Please select unit type"
    }
  },

  submitHandler: function () {
    submitRequest();
    $('#loader').show();
  }

});

function saveUnit() {
  if (document.getElementById('product').checked) {
    $('#product').attr('checked', true);
    $('input[name="product"]').val(true);
  } else {
    $('#product').attr('checked', false);
    $('input[name="product"]').val(false);
  }

  if (document.getElementById('free').checked) {
    $('#free').attr('checked', true);
    $('input[name="free"]').val(true);
  } else {
    $('#free').attr('checked', false);
    $('input[name="free"]').val(false);
  }
  if (document.getElementById('publiclyBuyable').checked) {
    $('#publiclyBuyable').attr('checked', true);
    $('input[name="publiclyBuyable"]').val(true);
  } else {
    $('#publiclyBuyable').attr('checked', false);
    $('input[name="publiclyBuyable"]').val(false);
  }

  var batches = [];
  $('#loader').show();
  var input = {};
  if ($('#unitType').val() == "LECTURE") {
    input = {
      description: $('#unitDescription').summernote('code'),
      type: $('#unitType').val(),
      title: $('#txtTitle').val(),
      typeId: lms.id,
      product: $('#product').val(),
      publiclyBuyable : $('#publiclyBuyable').val(),
      price: ($('#price').val() != null) ? $('#price').val() : 0,
      free: $('#free').val(),
      status: getStatus()

    };
  }
  else {
    input = {
      description: $('#unitDescription').summernote('code'),
      type: $('#unitType').val(),
      title: $('#txtTitle').val(),
      product: $('#product').val(),
      publiclyBuyable : $('#publiclyBuyable').val(),
      price: ($('#price').val() != null) ? $('#price').val() : 0,
      free: $('#free').val(),
      status: getStatus()
    };
  }
  if ($('#unitTopic').val() != null) {
    input["topic"] = $('#unitTopic').val();
  } else {
    input["topic"] = $('#txtTopic').val();
  }
  $.ajax({
    url: apiBasePath + "/admin_techops_lmsadmin/units/create",
    type: "POST",
    data: JSON.stringify(input),
    dataType: "json",
    contentType: "application/json",
    processData: false,

    success: function (response) {
      $('#loader').hide();
      selectedUnits[0] = response.unitId;
      alert("Unit Created Successfully");
      $('#divLinkUnit').show();

    },

    error: function (response) {
      $('#loader').hide();
      alert("Unit creation failed");

    }
  });

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

function getCourseBatch() {

  /*
   * list = []; $("#courseBatch").val().forEach(function(value, index) {
   * list.push({ id : value }); }); return list;
   */
  id = $('#courseId').val();
  if (id != null) {
    $.ajax({
      async: true,
      crossDomain: true,
      type: 'GET',
      processData: false,
      url: apiBasePath + "/coursebatches/" + id,

      success: function (response) {
        courseBatch = response;
      },

      error: function (response) {
        alert("Error occured while fetching batch details");
      }
    });
  }

}

function showPrice() {
  if (document.getElementById('product').checked) {
    $('#price').show();
    $('#lblPrice').show();
  } else {
    $('#price').hide();
    $('#lblPrice').hide();
  }
}

function fetchAllMacro() {
  $
  .ajax({
    async: true,
    crossDomain: true,
    type: 'GET',
    processData: false,
    url: apiBasePath + "/admin_techops/jwmacros",

    success: function (response) {
      var htmlToShow = '<option value="0" disabled selected>Select JWMacro</option>';
      var macroList = '<option value="#name#">#name#</option>';
      if (response.length > 0) {
        $.each(response, function (index, element) {
          i = index + 1;
          htmlToShow = htmlToShow += macroList;
          htmlToShow = htmlToShow.replace(/#name#/g,
              element.name);

        });
        $('#jwMacro').html(htmlToShow);
      } else {
        $('#jwMacro').html(htmlToShow);
      }
    },

    error: function (response) {

    }
  });
}


function addTopic() {
  $('#txtTopic').show();
}

function addTopic() {
  $('#txtTopic').show();
}

$("#but_upload").click(function (e) {
  e.preventDefault();
  var formData = new FormData();

  fileName = $('#uploadAttachment')[0].files[0].name;
  fileType = $('#uploadAttachment')[0].files[0].type;

  formData.append('file', $('#uploadAttachment')[0].files[0]);
  formData.append('type', 'assignments');
  formData.append('contentType', fileType);

  if (fileType == 'application/pdf') {
    $('#loaderUploadAttachment').show();
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
        $('#loaderUploadAttachment').hide();
        alert("Upload File " + fileName + " " + "successfully");

        if (response != null) {
          $('#upload-attachment-url').remove();
          var mydiv = document.getElementById("upload-attachment");
          var aTag = document.createElement('a');
          aTag.setAttribute('href', response);
          aTag.setAttribute('id', "upload-attachment-url");
          aTag.innerHTML = response;
          mydiv.appendChild(aTag);
        }
        else {
          var mydiv = document.getElementById("upload-attachment");
          var aTag = document.createElement('a');
          aTag.setAttribute('href', response);
          aTag.setAttribute('id', "upload-attachment-url");
          aTag.innerHTML = response;
          mydiv.appendChild(aTag);
        }

      },
      error: function (response) {
        $('#loaderUploadAttachment').hide();
        alert('Failed to Upload File');

      }
    });
  }
  else {
    uploadFile(formData, function (response) {
      if (response != null) {

        getImageUrl(response, function (callback) {
          fileUrl = callback;
          alert("Upload File " + fileName + " " + "successfully");
          $('#loaderUploadAttachment').hide();

          if (fileUrl != null) {
            $('#upload-attachment-url').remove();
            var mydiv = document.getElementById("upload-attachment");
            var aTag = document.createElement('a');
            aTag.setAttribute('href', fileUrl);
            aTag.setAttribute('id', "upload-attachment-url");
            aTag.innerHTML = fileUrl;
            mydiv.appendChild(aTag);
          }
          else {
            var mydiv = document.getElementById("upload-attachment");
            var aTag = document.createElement('a');
            aTag.setAttribute('href', fileUrl);
            aTag.setAttribute('id', "upload-attachment-url");
            aTag.innerHTML = fileUrl;
            mydiv.appendChild(aTag);
          }

        });

      }

      else {
        $('#loaderUploadAttachment').hide();

      }

    });

  }
});
