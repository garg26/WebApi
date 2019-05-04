//
//    $('.ns-question-list').each(function(){
//        var $questionList = jQuery(this);
//
//        $questionList.wrap('<form class="ns-question-list-form"></form>');
//
//        var userDetailsHtml = '<div class="ns-user-details">';
//        userDetailsHtml += '<div class="ns-row"><label>Name * : </label> <input type="text" name="user_name" value="" required></input></div>';
//        userDetailsHtml += '<div class="ns-row"><label>Email * : </label> <input type="email" name="user_email" value="" required></input></div>';
//        userDetailsHtml += '<div class="ns-row"><label>Phone : </label> <input type="tel" name="user_phone" value=""></input></div>';
//        if (window.location.href.indexOf("bpsc") != -1) {
//            userDetailsHtml += '<div class="ns-row"><label style="width: 100%;text-align: left;">Are you preparing for any of these exams as well? * : </label> <input type="checkbox" value="IAS" style="width: auto;margin: 0 2px 0 10px;">IAS&nbsp; <input type="checkbox" value="IES" style="width: auto;margin: 0 2px 0 10px;">IES&nbsp; <input type="checkbox" value="SSC" style="width: auto;margin: 0 2px 0 10px;">SSC/BANKING</div>';
//        }
//        userDetailsHtml += '<div class="ns-row"><label style="width: 100%;text-align: left;">Your Optional Subject* : </label> <select name="user_subject" required>';
//        userDetailsHtml += '<option value="">-- Select Subject --</option>';
//        userDetailsHtml += '<option value="Public Administration">Public Administration</option>';
//        userDetailsHtml += '<option value="Philosophy">Philosophy</option>';
//        userDetailsHtml += '<option value="Sociology">Sociology</option>';
//        userDetailsHtml += '<option value="Anthropology">Anthropology</option>';
//        userDetailsHtml += '<option value="History">History</option>';
//        userDetailsHtml += '<option value="Geography">Geography</option>';
//        userDetailsHtml += '<option value="Political Science">Political Science</option>';
//        userDetailsHtml += '<option value="Commerce and Accountancy">Commerce and Accountancy</option>';
//        userDetailsHtml += '<option value="Mathematics">Mathematics</option>';
//        userDetailsHtml += '<option value="Medical Science">Medical Science</option>';
//        userDetailsHtml += '<option value="Psychology">Psychology</option>';
//        userDetailsHtml += '<option value="Management">Management</option>';
//        userDetailsHtml += '<option value="Statistics">Statistics</option>';
//        userDetailsHtml += '<option value="Engineering Subject">Engineering Subject</option>';
//        userDetailsHtml += '<option value="Literature Subject">Literature Subject</option>';
//        userDetailsHtml += '<option value="Others">Other</option>';
//        if (window.location.href.indexOf("bpsc") != -1) {
//            userDetailsHtml += '<option value="Others">Undecided</option>';
//        }
//        userDetailsHtml += '</select></div>';
//        userDetailsHtml += '<div class="ns-row required-notice">Fields marked (*) are required to check result</div>';
//        userDetailsHtml += '</div>';
//        $questionList.parent().append(userDetailsHtml);
//        $questionList.parent().append('<div class="ns-submit"><input type="submit" value="Check Score"></input></div>');
//        $questionList.parent().append('<div class="ns-result">' +
//            '<h3 style="color: #000;">NeoStencil</h3>'+
//            'Final score: <span class="ns-score-obtained"></span><br>' +
//            'Total attempted: <span class="ns-attempted-questions"></span><br>' +
//            '<span style="color: green"># Correct: <span class="ns-correct-answers"></span></span><br>' +
//            '<span style="color: #ac5151"># Incorrect: <span class="ns-incorrect-answers"></span></span>' +
//            '<input type="hidden" name="ns_score_obtained" value="">' +
//            '<input type="hidden" name="ns_score_total" value="">' +
//            '<input type="hidden" name="ns_correct_answers" value="">' +
//            '<input type="hidden" name="ns_incorrect_answers" value="">' +
//            '<input type="hidden" name="ns_total_questions" value="">' +
//            '<input type="hidden" name="ns_attempted_questions" value="">' +
//            '</div>'
//        );
//
//        if (window.location.href.indexOf("upsc-prelims") != -1) {
//            $questionList.parent().append('<div class="iasbaba-result">' +
//                '<h3 style="color: #000;">IAS Baba</h3>'+
//                'Final score: <span class="iasbaba-score-obtained"></span><br>' +
//                'Total attempted: <span class="iasbaba-attempted-questions"></span><br>' +
//                '<span style="color: green"># Correct: <span class="iasbaba-correct-answers"></span></span><br>' +
//                '<span style="color: #ac5151"># Incorrect: <span class="iasbaba-incorrect-answers"></span></span>' +
//                '<input type="hidden" name="iasbaba_score_obtained" value="">' +
//                '<input type="hidden" name="iasbaba_score_total" value="">' +
//                '<input type="hidden" name="iasbaba_correct_answers" value="">' +
//                '<input type="hidden" name="iasbaba_incorrect_answers" value="">' +
//                '<input type="hidden" name="iasbaba_total_questions" value="">' +
//                '<input type="hidden" name="iasbaba_attempted_questions" value="">' +
//                '</div>'
//            );
//            $questionList.parent().append('<div class="insights-result">' +
//                '<h3 style="color: #000;">InsightsonIndia</h3>'+
//                'Final score: <span class="insights-score-obtained"></span><br>' +
//                'Total questions attempted: <span class="insights-attempted-questions"></span><br>' +
//                '<span style="color: green"># Correct: <span class="insights-correct-answers"></span></span><br>' +
//                '<span style="color: #ac5151"># Incorrect: <span class="insights-incorrect-answers"></span></span>' +
//                '<input type="hidden" name="insights_score_obtained" value="">' +
//                '<input type="hidden" name="insights_score_total" value="">' +
//                '<input type="hidden" name="insights_correct_answers" value="">' +
//                '<input type="hidden" name="insights_incorrect_answers" value="">' +
//                '<input type="hidden" name="insights_total_questions" value="">' +
//                '<input type="hidden" name="insights_attempted_questions" value="">' +
//                '</div>'
//            );
//        }
//
//
//
//
//        /*
//         * $questionList.parent().append('<div class="iasbaba-result"><h4>IAS
//         * Baba <br>Answer Key Coming Soon!</h4></div>');
//         * $questionList.parent().append('<div class="insights-result"><h4>InsightsonIndia
//         * <br>Answer Key Coming Soon!</h4></div>');
//         */
//
//
//        $(this).append('<input type="hidden" name="list_id" value="' + $('.ns-question-list').attr('id') + '">');
//
//        $('.ns-question', $questionList).each(function(){
//            var $question = jQuery(this);
//            var questionId = $question.attr('data-id');
//
//            $('.ns-answer', $question).each(function(){
//                var $answer = jQuery(this);
//                var answerId = $answer.attr('data-id');
//
//                $(this).wrapInner('<label for="ans_' + questionId + '_' + answerId + '"></label>');
//
//                $(this).prepend('<input type="radio" id="ans_' + questionId + '_' + answerId + '" name="ans[' + questionId + ']" value="' + answerId + '">');
//
//            });
//
//        });
//
//    });
//
    $('.ns-question-list-form').on('submit', function(e){
        e.preventDefault();
        var totalQuestions = 0;
        var attemptedQuestions = 0;
        var correctAnswers = 0;
        var incorrectAnswers = 0;
        var marksTotal = 0;
        var totalMarksObtained = 0;
        var totalNegativeMarks = 0;
        var correctAnswers_iasbaba = 0;
        var incorrectAnswers_iasbaba = 0;
        var marksTotal_iasbaba = 0;
        var totalMarksObtained_iasbaba = 0;
        var totalNegativeMarks_iasbaba = 0;
        var correctAnswers_insights = 0;
        var incorrectAnswers_insights = 0;
        var marksTotal_insights = 0;
        var totalMarksObtained_insights = 0;
        var totalNegativeMarks_insights = 0;

        $('.ns-question-list .ns-question', this).each(function(){
            var $question = jQuery(this);
            $('.ns-answer', $question).removeClass('ns-answered-correct').removeClass('ns-answered-incorrect');
            var marksQuestion = parseFloat($question.attr('data-marks'));
            var negativeMarksQuestion = parseFloat($question.attr('data-neg-marks'));

            marksTotal += marksQuestion;
            totalQuestions++;

            $selectedAnswer = jQuery('input:checked', $question);

            if($selectedAnswer.length > 0) {
                attemptedQuestions++;
                if($selectedAnswer.parent().hasClass('ns-correct')) {
                    totalMarksObtained += marksQuestion;
                    correctAnswers++;
                    $selectedAnswer.parent().addClass('ns-answered-correct');
                }
                else {
                    $selectedAnswer.parent().addClass('ns-answered-incorrect');
                    incorrectAnswers++;
                    totalNegativeMarks += negativeMarksQuestion;
                }
                if($selectedAnswer.parent().hasClass('iasbaba-correct')) {
                    totalMarksObtained_iasbaba += marksQuestion;
                    correctAnswers_iasbaba++;
                }
                else {
                    incorrectAnswers_iasbaba++;
                    totalNegativeMarks_iasbaba += negativeMarksQuestion;
                }
                if($selectedAnswer.parent().hasClass('insights-correct')) {
                    totalMarksObtained_insights += marksQuestion;
                    correctAnswers_insights++;
                }
                else {
                    incorrectAnswers_insights++;
                    totalNegativ.eMarks_insights += negativeMarksQuestion;
                }
            }
        });

        /*
         * if (attemptedQuestions < totalQuestions) { var submitConfirmed =
         * confirm( "Are you sure you want to check your result?\n" + "# Total
         * questions : " + totalQuestions + "\n" + "# Total questions attempted: " +
         * attemptedQuestions + "\n" + "Press [OK/Yes] to proceed or [Cancel/No]
         * to attempt more questions" );
         *
         * if (!submitConfirmed) { return; } }
         */

        totalMarksObtained = totalMarksObtained - totalNegativeMarks;
        totalMarksObtained_iasbaba = totalMarksObtained_iasbaba - totalNegativeMarks_iasbaba;
        totalMarksObtained_insights = totalMarksObtained_insights - totalNegativeMarks_insights;
        $('.ns-result .ns-score-obtained', this).text(totalMarksObtained.toFixed(2));
        $('.ns-result .ns-score-total', this).text(marksTotal);
        $('.ns-result .ns-correct-answers', this).text(correctAnswers);
        $('.ns-result .ns-incorrect-answers', this).text(incorrectAnswers);
        $('.ns-result .ns-total-questions', this).text(totalQuestions);
        $('.ns-result .ns-attempted-questions', this).text(attemptedQuestions);

        $('.ns-result [name="ns_score_obtained"]', this).val(totalMarksObtained.toFixed(2));
        $('.ns-result [name="ns_score_total"]', this).val(marksTotal);
        $('.ns-result [name="ns_correct_answers"]', this).val(correctAnswers);
        $('.ns-result [name="ns_incorrect_answers"]', this).val(incorrectAnswers);
        $('.ns-result [name="ns_total_questions"]', this).val(totalQuestions);
        $('.ns-result [name="ns_attempted_questions"]', this).val(attemptedQuestions);

        $('.iasbaba-result .iasbaba-score-obtained', this).text(totalMarksObtained_iasbaba.toFixed(2));
        $('.iasbaba-result .iasbaba-score-total', this).text(marksTotal);
        $('.iasbaba-result .iasbaba-correct-answers', this).text(correctAnswers_iasbaba);
        $('.iasbaba-result .iasbaba-incorrect-answers', this).text(incorrectAnswers_iasbaba);
        $('.iasbaba-result .iasbaba-total-questions', this).text(totalQuestions);
        $('.iasbaba-result .iasbaba-attempted-questions', this).text(attemptedQuestions);

        $('.iasbaba-result [name="iasbaba_score_obtained"]', this).val(totalMarksObtained_iasbaba.toFixed(2));
        $('.iasbaba-result [name="iasbaba_score_total"]', this).val(marksTotal);
        $('.iasbaba-result [name="iasbaba_correct_answers"]', this).val(correctAnswers_iasbaba);
        $('.iasbaba-result [name="iasbaba_incorrect_answers"]', this).val(incorrectAnswers_iasbaba);
        $('.iasbaba-result [name="iasbaba_total_questions"]', this).val(totalQuestions);
        $('.iasbaba-result [name="iasbaba_attempted_questions"]', this).val(attemptedQuestions);

        $('.insights-result .insights-score-obtained', this).text(totalMarksObtained_insights.toFixed(2));
        $('.insights-result .insights-score-total', this).text(marksTotal);
        $('.insights-result .insights-correct-answers', this).text(correctAnswers_insights);
        $('.insights-result .insights-incorrect-answers', this).text(incorrectAnswers_insights);
        $('.insights-result .insights-total-questions', this).text(totalQuestions);
        $('.insights-result .insights-attempted-questions', this).text(attemptedQuestions);

        $('.insights-result [name="insights_score_obtained"]', this).val(totalMarksObtained_insights.toFixed(2));
        $('.insights-result [name="insights_score_total"]', this).val(marksTotal);
        $('.insights-result [name="insights_correct_answers"]', this).val(correctAnswers_insights);
        $('.insights-result [name="insights_incorrect_answers"]', this).val(incorrectAnswers_insights);
        $('.insights-result [name="insights_total_questions"]', this).val(totalQuestions);
        $('.insights-result [name="insights_attempted_questions"]', this).val(attemptedQuestions);

        $('.ns-result', this).slideDown();

        $(this).addClass('submitted');

        var formData = $(this).serializeArray();
        if ($('.ns-user-details input:checked').length > 0){
            formData.forEach(function (item) {
                if (item.name === 'user_subject'){
                    item.value = item.value + ',' + $('.ns-user-details input:checked').map(function () { return $(this).val(); }).get().join();
                }
            });
        }
        $.post('/wp-json/ns-api/v1/questions/save-entries', formData, function(resp) {
        });
        if (window.location.href.indexOf("upsc-prelims") != -1) {
            var htmlToShowDifference = '';
            $(".ns-question-list .ns-question").each(function() {
                if ($(this).find(".ns-answer input[type='radio']").is(":checked") == true){
                    var nsCorrect ='';
                    var iasbabaCorrect ='';
                    var insightsCorrect ='';
                    var questionNumber ='';
                    var myAnswer ='';
                    $(this).find(".ns-answer").each(function() {

                        if ($(this).parent().find('.ns-correct.iasbaba-correct.insights-correct').length > 0){
                        }else{

                            nsCorrect = $(this).parent().find('.ns-correct').attr('data-id');
                            iasbabaCorrect = $(this).parent().find('.iasbaba-correct').attr('data-id');
                            insightsCorrect = $(this).parent().find('.insights-correct').attr('data-id');
                            questionNumber = $(this).parent().parent().attr('data-id');
                            if ($(this).parent().find('.ns-answer').hasClass('ns-answered-incorrect')){
                                myAnswer = $(this).parent().find('.ns-answered-incorrect').attr('data-id');
                            }else if ($(this).parent().find('.ns-answer').hasClass('ns-answered-correct')){
                                myAnswer = $(this).parent().find('.ns-answered-correct').attr('data-id');
                            }

                        }
                    });
                    if(nsCorrect != ''){
                        htmlToShowDifference = htmlToShowDifference += '<tr><td>'+questionNumber+'</td><td>'+nsCorrect+'</td><td>'+iasbabaCorrect+'</td><td>'+insightsCorrect+'</td><td>'+myAnswer+'</td></tr>';
                    }
                }
            });

            $('#differences').html('<h4>Attempted questions in which answer keys differ:</h4><table><thead><tr><th>Question no</th><th>NeoStencil</th><th>IAS Baba</th><th>Insights on India</th><th>Your Answer</th></tr></thead><tbody>'+htmlToShowDifference+'</tbody></table>');
        }
        /* $('#differences').html(''); */

    });

