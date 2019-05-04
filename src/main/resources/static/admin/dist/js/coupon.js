// add a new coupon
$("form[name=coupon-creation]").validate({
    submitHandler: function(form, e) {
        e.preventDefault();
        var couponPayload = {
            code : $("form[name='coupon-creation']").find("#coupon-code").val(),
            description: $("form[name='coupon-creation']").find("#coupon-description").val(),
            discountType : $("form[name='coupon-creation']").find("#coupon-discount-type :selected").val(),
            value : $("form[name='coupon-creation']").find("#coupon-amount").val(),
            expiryDate : $("form[name='coupon-creation']").find("#expiry-date").val(),
            minimumSpend : $("form[name='coupon-creation']").find("#minimum-spend").val(),
            maximumSpend : $("form[name='coupon-creation']").find("#maximum-spend").val(),
            isIndividualUseOnly : $("form[name='coupon-creation']").find("#individual-use:checked").val(),
            isExcludeSaleItems : $("form[name='coupon-creation']").find("#exclude-sale-items:checked").val(),
            isIncludeLmsProducts : true,
            isExcludeLmsProducts : true,
            allowedEmails : $("form[name='coupon-creation']").find("#allowed-emails").val(),
            maximumNumberOfUsageLimit : $("form[name='coupon-creation']").find("#usage-limit-per-coupon").val(),
            maximumNumberofAllowedItems : $("form[name='coupon-creation']").find("#limit-usage-to-x-items").val(),
            maximumNumberOfUsageLimitPerUser : $("form[name='coupon-creation']").find("#usage-limit-per-user").val()
        };
        $('#loader-add-new-coupon').show();
        $.ajax({
        	url : apiBasePath + "/admin/ecommerce/coupons",
            type: "POST",
            data: JSON.stringify(couponPayload),
            dataType: "json",
            contentType: "application/json",
            processData: false,
            success: function(response) {
                $('#loader-add-new-coupon').hide();
                console.log(response);
                if (response.statusCode == '201') {
                    e.preventDefault();
                    $("#add-new-coupon-error").html(response.messages[0]);
                }
                else {
                    $('#add-new-coupon-error').html(response.messages[0] ? response.messages[0]: 'Some error occured. Please try again after sometime');
                }
            },
            error: function(response) {
                e.preventDefault();
                $('#loader-add-new-coupon').hide();
                console.log(response);
            }
        });
    }

}); // end form validate

// select or deselect all checkboxes
$("#select-all-coupons").click(function () {
    $(".select-coupon").prop('checked', $(this).prop('checked'));
});

// Edit coupon event handler
$("#edit-coupon").on('click', function(e) {
    $("#coupons-error").html("");
    var nChecked = $('input[name=select-coupon]:checked').length;
    if (nChecked == 1) {
        var couponId = $('input[name=select-coupon]:checked').val();
        window.location.href = '/admin/ecommerce/coupons/update-coupon/' + couponId;
    } else {
        $("#coupons-error").html("Select exactly one coupon to edit!");
    }
});

// Trash coupon(s) event handler
$("#trash-coupon").on('click', function(e) {
    $("#coupons-error").html("");
    // get coupon ids from all checked checkboxes
    // make ajax call to bulk coupon update API, update returned status at front end
    var nChecked = $('input[name=select-coupon]:checked').length;
    if (nChecked == 0) {
        $("#coupons-error").html("Select exactly one coupon to edit!");
    }
    if (nChecked > 20) {
        $("#coupons-error").html("Maximum 20 coupons can be trashed!");
    }
    if (nChecked >= 1 && nChecked <= 20) {
        var couponId = $('input[name=select-coupon]:checked').val();
        window.location.href = '/admin/ecommerce/coupons/trash/';
    }
});

// Clone coupon(s) event handler
$("#clone-coupon").on('click', function(e) {
    $("#coupons-error").html("");
    var nChecked = $('input[name=select-coupon]:checked').length;
    if (nChecked == 1) {
        var couponId = $('input[name=select-coupon]:checked').val();
        // insert a new row with source coupon data
        var newCoupon = '<tr>' +
                '<td><input type="checkbox" name="select-coupon" class="select-coupon" th:value="${coupon.id}"></td>' +
                '<td><a th:href="@{'/admin/ecommerce/coupons/' + ${coupon.id}}" th:text="${coupon.id}"></a></td>' +
                '<td th:text="${coupon.code}"></td>' +
                '<td th:text="${coupon.type}"></td>' +
                '<td th:text="${coupon.amount}"></td>' +
                '<td id="description" th:text="${coupon.description}"></td>' +
                '<td th:text="${coupon.includeProducts}"></td>' +
                '<td th:text="${coupon.usageLimit}"></td>' +
                '<td th:text="${coupon.expiryDate}"></td>' +
            '</tr>';

    } else {
        $("#coupons-error").html("Select exactly one coupon to clone!");
    }
});

$(document).ready(function() {
    $('#all-coupons').DataTable();
} );
