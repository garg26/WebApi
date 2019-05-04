var metaInfoList = [];

$(document).ready(function() {

	$('#addMetaPanel').hide();
	$('#metaPanel').hide();
});

$('form[name=newBatch]').validate({
	rules : {
		startDate : {
			required : true
		},
		startTime : {
			required : true
		},
		endTime : {
			required : true
		},
		regularPrice : {
			required : true
		},
		salePrice : {
			required : true
		},
		displaySize : {
			required : true
		},
		validity : {
			required : true
		},
		duration : {
			required : true
		}
	},
	messages : {

		startDate : {
			required : "Please enter start date"
		},
		batchName : {
			required : "Please enter the batch name"
		},
		regularPrice : {
			required : "Please enter regular price"
		},
		salePrice : {
			required : "Please enter sale price"
		},
		displaySize : {
			required : "Please enter display size"
		},
		validity : {
			required : "Please enter batch validity"
		},
		duration : {
			required : "Please enter batch duration"
		}
	},
	submitHandler : function() {
		submitRequest();
		$('#loader').show();
	}

});

function submitRequest() {

	var courseBatch = [];
	var input = {
		startDate : $('#startDate').val(),
		endDate : $('#endDate').val(),
		duration : $('#duration').val(),
		batchName : $('#batchName').val(),
		classTiming : $('#classTiming').val(),
		discount : $('#instituteName').val(),
		regularPrice : $('#regularPrice').val(),
		salePrice : $('#salePrice').val(),
		displaySize : $('#displaySize').val(),
		noOfAvailableSeats:$('#noOfSeats').val(),
		status : getStatus(),
		metaList : metaInfoList,
		validity : $('#validity').val(),
		course : {
			id : $('#courseId').val(),
		},
    noOfSession: $('#noOfSession').val(),
    validityDisplay : $('#validityDisplay').val(),

	};
	$.ajax({
		url : apiBasePath + "/admin_lmsadmin/coursebatches",
		type : "POST",
		data : JSON.stringify(input),
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
			alert("Course batch Add successfully");

		},

		error : function(response) {
			$('#loader').hide();
			if (response.status == 403) {
				window.location.href = "/permission-error";
			} else {
				alert("Course batch add failed");
			}

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
