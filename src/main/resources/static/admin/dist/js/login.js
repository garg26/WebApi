// submit user creation form
$("form[name='admin-signin']").validate({
    submitHandler: function (form, e) {
        e.preventDefault();
        $("#add-new-entity-error").html("");
        $('#loader').show();
        var userPayload = {
            emailId: $("form[name='admin-signin']").find("#useremail").val(),
            password: $("form[name='admin-signin']").find("#password").val()
        };
        $.ajax({
            url: apiBasePath + "/user/login",
            type: "POST",
            data: JSON.stringify(userPayload),
            dataType: "json",
            contentType: "application/json",
            processData: false,
            success: function (response) {
                $('#loader').hide();
                if (response.statusCode == '200') {
                    e.preventDefault();
                    loginUser(response.accessToken, "/admin/index");
                }
                else {
                    $('#add-new-entity-error').html(response.messages[0] ? response.messages[0] : 'Some error occured. Please try again after sometime');
                }
            },
            error: function (response) {
                e.preventDefault();
                $('#loader').hide();
            }
        });
    }

}); // end form validate

