var examSegmentName;
var courseCategory;
var sort = "popular";
var sortCourses;
var pageCount = 1;
var pageNumber = 1;
var selectedFilters = [];

function changeFilterTabs() {
  var filterCheckboxes = [];
  if(courseCategory!='ALL'){
    filterCheckboxes.push({
      filterName: 'courseCategory',
      value: courseCategory
    })
  }

  var htmlToShow = '';
  $('.course-filter-card .mdl-checkbox')
  .each(
      function (i) {

        $(this).attr('id', 'filter_label_' + i);

        if ($('#filter_label_' + i)
        .find('.mdl-checkbox__input').is(":checked")) {
          var x = $('#filter_label_' + i).find(
              '.mdl-checkbox__label').html();
          htmlToShow += '<div class="divFilter">'
              + x
              + '<button onclick = "$(this).parent().hide();$('
              + "'#filter_label_"
              + i
              + "').removeClass('is-checked').find('.mdl-checkbox__input').prop('checked',false);changeFilterTabs();"
              + '" class="btn-filter" id="filtersTab_btn_'
              + i
              + '"><i class="fa fa-times"></i></button></div>';
        }
      });
  $('.course-filter-card .mdl-checkbox__input').each(function () {
    if ($(this).is(":checked")) {
      var selectedFilters = $(this).val();
      var arr = selectedFilters.split(':');
      var arr_subcategory = $(this).attr("data-value").split(':');
      if(arr[0] != 'courseMedium') {
        filterCheckboxes.push({
          filterName: arr[0],
          value: arr[1]
        }, {
          filterName: arr_subcategory[0],
          value: arr_subcategory[1]
        });
      }
      else{
        filterCheckboxes.push({
          filterName: arr[0],
          value: arr[1]
        });
      }
    }
  });
  filterCheckboxes.push({
    filterName: 'courseExamSegment',
    value: examSegmentName
  });
  selectedFilters = filterCheckboxes;
  fetchCoursesList('0', selectedFilters, sort, 'checkbox');
  $('#filtersTab').html(htmlToShow);
}

function fetchFilters(examSegment, category, sort) {
  $('#filtersTab').html('');
  $('#courseSortBy').val("popular");
  $('.load-courses-progress-bar').show();
  examSegmentName = examSegment;
  courseCategory = category;
  sortCourses = sort;
  $('#changeCourseCategory').val(examSegmentName);
  $('#category_' + examSegmentName).show();
  $('#category_' + examSegmentName).val(courseCategory);
  $('#course-listing-card').find(
      'a[data-id =' + examSegmentName.replace(" ", "_") + ']').addClass(
      "is-active").siblings().removeClass("is-active");
  $('.back-to-category-mobile-span').html(category);

  $
  .ajax({
    url: apiBasePath + '/courses/filters/exam/' + examSegment
        + '/category/' + category,

    type: "GET",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function (response) {
      $('#filterMedium').show();
      var htmlToShow = '<div class="course-filters mdl-card__actions"><p>'
          + ' </p>'
      var htmlToShowFilterHead = '#SubCategory#';
      var htmlToShowFilterData = '#filters#';
      $
      .each(
          response.filter,
          function (key, value) {
            key = key.replace(" ", "_");
            keyToShow = key.replace("_", " ");
            htmlToShow = htmlToShow
                + htmlToShowFilterHead;
            if (value != "") {
              if (key == examSegment.replace(" ",
                  "_")) {
                htmlToShow = htmlToShow
                .replace(
                    '#SubCategory#',
                    '<div class="course-filters mdl-card__actions mdl-card--border"><span style="font-weight: bold;" onclick="fetchFilters('
                    + "'"
                    + keyToShow
                    + "','"
                    + 'ALL'
                    + "',"
                    + "'popular'"
                    + ');" class="filter-label"> '
                    + keyToShow
                    + '</span></span><br />');
              } else {
                htmlToShow = htmlToShow
                .replace(
                    '#SubCategory#',
                    '<div class="course-filters mdl-card__actions mdl-card--border"><span onclick="fetchFilters('
                    + "'"
                    + keyToShow
                    + "','"
                    + 'ALL'
                    + "',"
                    + "'popular'"
                    + ');" class="filter-label"> '
                    + keyToShow
                    + '</span></span><br />');
              }
              if (category != "ALL") {
                if (key == examSegment.replace(
                    " ", "_")) {
                  htmlToShow = htmlToShow
                  .replace(
                      '#SubCategory#',
                      '<div class="course-filters mdl-card__actions mdl-card--border"><span onclick="fetchFilters('
                      + "'"
                      + keyToShow
                      + "','"
                      + 'ALL'
                      + "',"
                      + "'popular'"
                      + ');" style="font-weight: bold;" class="filter-label">'
                      + keyToShow
                      + '<span>('
                      + category
                      + ')</span></span><br />');
                } else {
                  htmlToShow = htmlToShow
                  .replace(
                      '#SubCategory#',
                      '<div class="course-filters mdl-card__actions mdl-card--border"><span onclick="fetchFilters('
                      + "'"
                      + keyToShow
                      + "','"
                      + 'ALL'
                      + "',"
                      + "'popular'"
                      + ');" class="filter-label">'
                      + keyToShow
                      + '<span>('
                      + category
                      + ')</span></span></div><br />');
                }

              } else {
                /*
                 * $('.btn-filter').trigger(
                 * "click");
                 */
                $('input:checkbox').removeAttr(
                    'checked');
                $('.mdl-checkbox').removeClass(
                    "is-checked");
              }
            } else {
              htmlToShow = htmlToShow.replace(
                  '#SubCategory#', "");
            }

            $
            .each(
                value,
                function (index, element) {
                  htmlToShow = htmlToShow
                      + htmlToShowFilterData;
                  if (category != "ALL") {

                    var filterCheckboxes = '';
                    var filterList = '';
                    $
                    .each(
                        Object
                        .keys(element),
                        function (
                            temp,
                            subcategory) {
                          if (subcategory == category) {
                            filterList = filterList
                                + '<div class="course-filters mdl-card__actions mdl-card--border"><span onclick="fetchFilters('
                                + "'"
                                + keyToShow
                                + "','"
                                + subcategory
                                + "',"
                                + "'popular'"
                                + ');" class="filter-label">'
                                + '<b>'
                                + subcategory
                                + '</b></span></div>';
                          } else {
                            filterList = filterList
                                + '<div class="course-filters mdl-card__actions mdl-card--border"><span onclick="fetchFilters('
                                + "'"
                                + keyToShow
                                + "','"
                                + subcategory
                                + "',"
                                + "'popular'"
                                + ');" class="filter-label">'
                                + '<span>'
                                + subcategory
                                + '</span></span></div>';
                          }

                        });
                    $
                    .each(
                        element,
                        function (
                            tempFilter,
                            subcategoryFilter) {
                          var key = Object
                          .keys(element)[tempFilter];
                          if (subcategoryFilter != null) {
                            $
                            .each(
                                subcategoryFilter,
                                function (
                                    filterKey,
                                    filterResponse) {
                                  if (filterResponse != '') {
                                    filterCheckboxes = filterCheckboxes
                                        + '<span style="font-weight: bold;margin: 8px;" class="filter-label">'
                                        + filterKey
                                        + '</span></br>';
                                  }
                                  $
                                  .each(
                                      filterResponse,
                                      function (
                                          filterIndexFinalLevel,
                                          filterResponseFinalLevel) {
                                        if (filterKey == 'Medium') {
                                          filterCheckboxes = filterCheckboxes
                                              + '<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="filter'
                                              + filterResponseFinalLevel
                                              + filterIndexFinalLevel
                                              + '"> <input type="checkbox" id="filter'
                                              + filterResponseFinalLevel
                                              + filterIndexFinalLevel
                                              + '" class="mdl-checkbox__input" onchange="changeFilterTabs();" value="courseMedium'
                                              + ':'
                                              + filterResponseFinalLevel
                                              + '"'
                                              + 'data-value="courseSubCategory:'
                                              + filterKey
                                              + '"/> <span class="mdl-checkbox__label">'
                                              + filterResponseFinalLevel
                                              + '</span>  </label> <br />';
                                        }
                                        else {
                                          filterCheckboxes = filterCheckboxes
                                              + '<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="filter'
                                              + filterResponseFinalLevel
                                              + filterIndexFinalLevel
                                              + '"> <input type="checkbox" id="filter'
                                              + filterResponseFinalLevel
                                              + filterIndexFinalLevel
                                              + '" class="mdl-checkbox__input" onchange="changeFilterTabs();" value="courseSubject'
                                              + ':'
                                              + filterResponseFinalLevel
                                              + '"'
                                              + 'data-value="courseSubCategory:'
                                              + filterKey
                                              + '"/> <span class="mdl-checkbox__label">'
                                              + filterResponseFinalLevel
                                              + '</span>  </label> <br />';
                                        }
                                      });
                                })
                          }

                        });
                    htmlToShow = htmlToShow
                    .replace(
                        '#filters#',
                        filterList
                        + filterCheckboxes);
                  } else {
                    var filterList = '';
                    $
                    .each(
                        Object
                        .keys(element),
                        function (
                            temp,
                            subcategory) {
                          filterList = filterList
                              + '<div class="course-filters mdl-card__actions mdl-card--border"><span onclick="fetchFilters('
                              + "'"
                              + keyToShow
                              + "','"
                              + subcategory
                              + "',"
                              + "'popular'"
                              + ');" class="filter-label">'
                              + '<span>'
                              + subcategory
                              + '</span></span></div>';
                        })
                    htmlToShow = htmlToShow
                    .replace(
                        '#filters#',
                        filterList);
                    $('.btn-filter')
                    .trigger(
                        "click");
                  }
                });
            htmlToShow = htmlToShow + '</div>';

          });
      $('#courseDynamicFilters').html(htmlToShow);
      componentHandler.upgradeDom();
      if (window.location.href.indexOf("courseSubject") != -1) {
        $('.mdl-checkbox').find(
            "input[value='" + "courseSubject:" + getParameterByName(
            'courseSubject') + "']").attr(
            "checked", "checked").parent("label").addClass("is-checked");
      }
      if (window.location.href.indexOf("courseMedium") != -1) {
        $('.mdl-checkbox').find(
            "input[value='" + "courseMedium:" + getParameterByName(
            'courseMedium') + "']").attr(
            "checked", "checked").parent("label").addClass("is-checked");
      }
      fetchCoursesList('0', courseCategory, sortCourses, 'category');
    }
  })

}

function fetchCoursesList(page_num, courseCategory, sortCourses, type) {
  $('#error-courses').hide();
  $('#loader-all-courses').show();
  $('.all-courses-section').hide();
  if (courseCategory == 'ALL') {
    if (examSegmentName == 'IES' || examSegmentName == 'GATE') {
      var obj = {
        filters: [{
          filterName: 'courseExamSegment',
          value: examSegmentName.replace("_", " ")
        }, {
          filterName: 'courseExamSegment',
          value: 'IES GATE'
        }]
      };
    } else {
      var obj = {
        filters: [{
          filterName: 'courseExamSegment',
          value: examSegmentName.replace("_", " ")
        }]
      };
    }

  } else if (type == 'checkbox' || $.isArray(courseCategory) == true) {
    var obj = {
      filters: courseCategory
    };
  } else if (type == 'pagination') {
    var obj = {
      filters: courseCategory
    };
  } else if (window.location.href.indexOf("courseSubject") != -1) {
      if (window.location.href.indexOf("courseMedium") != -1) {
        var obj = {
          filters: [{
            filterName: 'courseCategory',
            value: courseCategory
          }, {
            filterName: 'courseExamSegment',
            value: examSegmentName
          }, {
            filterName: 'courseSubject',
            value: $('input:checkbox:checked:first').val().split(":")[1]
          },
            {
              filterName: 'courseMedium',
              value: getParameterByName('courseMedium')
            }]
        };
      }

      else {
        var obj = {
          filters: [{
            filterName: 'courseCategory',
            value: courseCategory
          }, {
            filterName: 'courseExamSegment',
            value: examSegmentName
          }, {
            filterName: 'courseSubject',
            value: $('input:checkbox:checked:first').val().split(":")[1]
          }]
        };
      }

  }
  else if (window.location.href.indexOf("courseMedium") != -1){
    var obj = {
      filters: [{
        filterName: 'courseCategory',
        value: courseCategory
      }, {
        filterName: 'courseExamSegment',
        value: examSegmentName
      },
        {
          filterName: 'courseMedium',
          value: getParameterByName('courseMedium')
        }]
    };
  }
  else {
    var obj = {
      filters: [{
        filterName: 'courseCategory',
        value: courseCategory
      }, {
        filterName: 'courseExamSegment',
        value: examSegmentName
      }]
    };
  }
  var obj2 = JSON.stringify(obj);
  $.ajax({
    url: apiBasePath + '/courses?page=' + page_num + '&size=12&sort='
        + sortCourses + '&search='
        + encodeURIComponent(JSON.stringify(obj)),
    type: "GET",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function (response) {
      $('.load-courses-progress-bar').hide();
      $('.course-listing').css("visibility", "visible");
      $('.mat-content').css("overflow-y", "auto")

      if ($(window).width() > '767') {
        $('body,html').animate({
          scrollTop: $(".course-listing").offset().top - 100
        }, 1000);
        setTimeout(function () {
          $('.mat-all-curses-main-tab').hide();
        }, 2000);
      } else {
        $(".mat-all-curses-main-tab-mobile").hide("0", function () {
          $('body,html').animate({
            scrollTop: $(".course-listing").offset().top - 200
          }, 1000);
        })
      }
      showCourses(response);
      showTrendingCourse(response, obj);
    },

    error: function (response) {
      $('.load-courses-progress-bar').hide();
      $('.all-courses-section').hide();
      $('#loader-all-courses').hide();
      $('#error-courses').show();
    }
  });
}

function showTrendingCourse(response, obj) {

  if (examSegmentName == obj.filters[0].value) {
    $('#trending-course').text('Trending Courses in ' + examSegmentName);
  } else {
    $('#trending-course').text(
        'Trending Courses in ' + examSegmentName + ' '
        + obj.filters[0].value);
  }

  if (response != null && response.content.length > 0) {
    var htmlToShow = '';
    var trendingCourseList = '<div><div class="trending-courses-card mdl-card__media"><!-- <a class="close-ribbon"><span>BEST SELLER</span></a> -->  <a href="/course/#courseLink#"><img src="#courseImage#" alt="#altImageTitle#"></a> <!-- <div class="courses-label">IAS Optional</div> --> </div> <div class="ns-card"><span class="course-rating" >#courseRating#</span><span class="starts-on">Start Date: <span class="starts-on-date">#startDate#</span></span> <a href="/course/#courseLink#"><h3 class="course-title-ellipis">#courseTitle#</h3></a> <p class="ns-card-p"><span class="p-grid-1"><i>by</i> <span class="ns-card-teacher"><a href="/teacher/#teacherSlug#">#teacherName#</a></span><br/><a href="/institute/#instructorSlug#">(#instituteName#)</a></span> </p> <div class=""> <h3 class="price">#coursePrice# </h3> <span class="number-students float-right"><i class="fa fa-user"></i>&nbsp;#noOfStudents# </span> </div></div></div>';
    $.each(response.content, function (index, element) {

      if (element.popular) {
        $('#single-course-slider-3').slick('unslick');

        htmlToShow = htmlToShow += trendingCourseList;
        htmlToShow = htmlToShow.replace("#courseImage#",
            element.courseImageUrl + '=w296-h184-s');

        htmlToShow = htmlToShow.replace("#courseTitle#",
            element.courseTitle);
        htmlToShow = htmlToShow.replace("#altImageTitle#",
            element.courseTitle);

        var ratingHtml = '';
        if (element.courseRating != 0) {
          var j = element.courseRating;
          for (j; j >= 1; j--) {
            ratingHtml = ratingHtml
                + '<i class=\"material-icons\">star_rate</i>';
          }
          if (j < 1 && j > 0) {
            ratingHtml = ratingHtml
                + "<i class=\"material-icons\">star_half</i>";
          }
          htmlToShow = htmlToShow.replace("#courseRating#",
              ratingHtml);
        } else {
          htmlToShow = htmlToShow.replace("#courseRating#", '');
        }

        htmlToShow = htmlToShow.replace("#startDate#",
            element.startDate);

        if (element.price == 0) {
          htmlToShow = htmlToShow.replace("#coursePrice#",
              '<span style="color: green;">FREE</span>');
        } else {
          htmlToShow = htmlToShow.replace("#coursePrice#",
              '<i class="fa fa-inr">&nbsp;</i>'
              + numberWithCommas(element.price));
        }

        if (element.instructors[0] != null) {
          htmlToShow = htmlToShow.replace("#teacherName#",
              element.instructors[0].teacherName);
          htmlToShow = htmlToShow.replace("#instructorSlug#",
              element.instituteSlug);
          htmlToShow = htmlToShow.replace("#teacherSlug#",
              element.instructors[0].slug);
        }
        htmlToShow = htmlToShow
        .replace("#instituteName#", element.name);

        if (element.courseOldSlug != null) {
          htmlToShow = htmlToShow.replace(/#courseLink#/g,
              element.courseOldSlug);
        }

        htmlToShow = htmlToShow.replace("#noOfStudents#",
            element.studentsEnrolled + ' Students');
        if ($(window).width() > '767') {
          $('#single-course-slider-3').html(htmlToShow).slick({
            dots: false,
            infinite: true,
            slidesToShow: 4,
            slidesToScroll: 4
          });
        } else {
          $('#single-course-slider-3').html(htmlToShow).slick({
            responsive: [{
              breakpoint: 768,
              settings: {
                arrows: false,
                centerMode: true,
                centerPadding: '40px',
                slidesToShow: 3

              }
            }, {
              breakpoint: 480,
              settings: {
                arrows: false,
                centerMode: true,
                centerPadding: '40px',
                slidesToShow: 1
              }
            }]
          });
        }

      }

    })
  }

}

function showCourses(response) {

  $('.all-courses-section').show();
  $('#loader-all-courses').hide();
  $('.course-listing').css("visibility", "visible");
  $('.mat-content').css("overflow-y", "auto")
  var htmlToShowList = '';
  var htmlToShowGrid = '';
  var courseList = '<div class="all-courses-card"> <span class="best-seller">#discount# discount</span> <div class="mdl-grid"> <div class="mdl-cell mdl-cell--3-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"> <a href="/course/#courseLink#"> <img src="#courseImage#" alt="#altImageTitle#"></a> </div> <div class="mdl-cell mdl-cell--7-col mdl-cell--12-col-tablet mdl-cell--12-col-phone"> <div class="course-single-card-list"> <a href="/course/#courseLink#"> <h3>#courseTitle#</h3> </a> <p class="by-teacher"> <span><i>by</i> </span><span><span class="moreTeacher">#TEACHER# </span><a href="institute/#instructorSlug#"><i><span style="color: #666;font-family: crimson;">#instituteName#</span></i></a> </span> <br /> </p> <p class="course-rating"> <span>#courseRating#</span> <span>&nbsp;&nbsp;<small style="vertical-align: top;">(#noOfReviewers# Ratings)</small></span> </p> <p class="starts-on"> Start Date: <span>#courseStartDate#</span>&nbsp;&nbsp;|&nbsp;&nbsp;Enrolled Students: <span>#noOfStudents#</span>&nbsp;</p> </div> </div> <div class="fav-card mdl-cell mdl-cell--2-col mdl-cell--12-col-tablet mdl-cell--12-col-phone">   <h3 class="price">#fees# </h3> <br /> <a class="view-details btn-explore btn-cta-secondary mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" href="/course/#courseLink#">View Details</a> </div> </div></div>';
  var courseGrid = '<div class="mdl-cell mdl-cell--4-col mdl-cell--4-col-tablet mdl-cell--12-col-phone mdl-card mdl-shadow--3dp"> <div class="trending-courses-card mdl-card__media"> <a href="/course/#courseLink#"> <img src="#courseImage#" alt="#altImageTitle#" width="100%"></a> <div class="mdl-tooltip mdl-tooltip--top" data-mdl-for="Layer_2"> <strong>Add to cart</strong> </div> </div> <div class="ns-card"> <span class="course-rating">#courseRating#</span> <span class="starts-on">Start Date: <span class="starts-on-date">#courseStartDate#</span> </span> <a href="/course/#courseLink#"> <h3 class="course-heading-grid">#courseTitle#</h3> </a> <p class="ns-card-p"> <span class="p-grid-1"><i>by</i>#TEACHERNAME#<br /> (<a href="institute/#instructorSlug#"><i><span>#instituteName#</span></i></a>) </span> <span class="p-grid-2"><i class="material-icons">person_outline</i> <span class="num-teacher">#noOfStudents#</span> </span> </p> <div class=""> <h3 class="price">#fees#</h3> <a class="mat-link" href="/course/#courseLink#"> View Details <i class="material-icons">chevron_right</i> </a> </div> </div> </div>';

  if (response != null) {
    var pageNumberOfCurrent = response.pageable.pageNumber;
    var showingCourseNumber = pageNumberOfCurrent * response.size + 1;
    var totalShowing = showingCourseNumber + response.numberOfElements - 1;
    if (courseCategory != 'ALL') {
      $('#totalCourses')
      .html(
          response.totalElements
          + ' Courses in '
          + examSegmentName.replace("_", " ")
          + ' '
          + courseCategory
          + '&nbsp;&nbsp;<span style="font-sizeL 14px;">Showing '
          + showingCourseNumber + ' - '
          + totalShowing + ' of '
          + response.totalElements + '</span>');
    } else {
      $('#totalCourses')
      .html(
          response.totalElements
          + ' Courses in '
          + examSegmentName.replace("_", " ")
          + '&nbsp;&nbsp;<span style="font-sizeL 14px;">Showing '
          + showingCourseNumber + ' - '
          + totalShowing + ' of '
          + response.totalElements + '</span>');
    }

    $
    .each(
        response.content,
        function (index, element) {
          htmlToShowList = htmlToShowList += courseList;
          htmlToShowList = htmlToShowList.replace(
              "#courseTitle#", element.courseTitle);
          htmlToShowList = htmlToShowList.replace(
              "#altImageTitle#", element.courseTitle);
          htmlToShowList = htmlToShowList.replace(
              "#courseName#", element.courseName);

          if (element.instructorName != null && element.instructorName != ""
              && element.instructors.length > 1) {
            htmlToShowTeacher = '';
            $.each(element.instructors, function (index, elm) {
              if (element.instructorName == elm.teacherName) {

                htmlToShowTeacher = htmlToShowTeacher
                    + "<span> <a style='color: #000;' href='teacher/" + elm.slug
                    + "'><span>" + elm.teacherName + ",</span></a></span>";

              }
            });

            $.each(element.instructors, function (index, elm) {
              if (element.instructorName != elm.teacherName) {

                htmlToShowTeacher = htmlToShowTeacher
                    + "<span> <a style='color: #000;' href='teacher/" + elm.slug
                    + "'><span>" + elm.teacherName + "</span></a></span>";

              }
            });

            htmlToShowList = htmlToShowList.replace(
                "#TEACHER#", htmlToShowTeacher);
          }

          else {
            htmlToShowTeacher = '';
            $.each(element.instructors, function (index, elm) {

              htmlToShowTeacher = htmlToShowTeacher
                  + "<span> <a style='color: #000;' href='teacher/" + elm.slug
                  + "'><span>" + elm.teacherName + "</span></a></span> ";

              htmlToShowList = htmlToShowList.replace(
                  "#teacherName#", elm.teacherName);
              htmlToShowList = htmlToShowList.replace(
                  "#teacherSlug#", elm.slug);

            });
            htmlToShowList = htmlToShowList.replace(
                "#TEACHER#", htmlToShowTeacher);
          }

          // htmlToShowList = htmlToShowList.replace(
          // 		"#teacherName#", element.instructors[0].teacherName);
          // htmlToShowList = htmlToShowList.replace(
          //     "#teacherSlug#", element.instructors[0].slug);

          htmlToShowList = htmlToShowList.replace(
              "#instructorSlug#", element.instituteSlug);

          htmlToShowList = htmlToShowList.replace(
              "#instituteName#", element.name);
          htmlToShowList = htmlToShowList.replace(
              /#courseLink#/g, element.courseOldSlug);
          htmlToShowList = htmlToShowList.replace(
              "#courseImage#", element.courseImageUrl
              + '=w208-h130-s');
          if (element.price != 0) {
            htmlToShowList = htmlToShowList
            .replace(
                "#fees#",
                '<i class="fa fa-inr"></i>&nbsp;'
                + numberWithCommas(element.price));
          } else {
            htmlToShowList = htmlToShowList
            .replace(
                "#fees#",
                '<span style="color: green; font-size: 18px; font-family: open sans;">FREE</span>');
          }
          if (element.noOfReviewers != null) {
            htmlToShowList = htmlToShowList.replace(
                "#noOfReviewers#",
                element.noOfReviewers);
          }
          if (element.discount != 0) {
            htmlToShowList = htmlToShowList.replace(
                "#discount#", element.discount);
          } else {
            htmlToShowList = htmlToShowList
            .replace(
                "<span class=\"best-seller\">#discount# discount</span>",
                "");
          }
          htmlToShowList = htmlToShowList.replace(
              "#courseStartDate#", element.startDate);
          htmlToShowList = htmlToShowList.replace(
              "#totalLectures#", element.noOfSessions);

          var ratingHtml = '';
          if (element.courseRating != 0) {
            var j = element.courseRating;

            for (j; j >= 1; j--) {
              ratingHtml = ratingHtml
                  + '<i class=\"material-icons\">star_rate</i>';
            }
            if (j < 1 && j > 0) {
              ratingHtml = ratingHtml
                  + "<i class=\"material-icons\">star_half</i>";
            }
            htmlToShowList = htmlToShowList.replace(
                "#courseRating#", ratingHtml);
          }
          if (element.studentsEnrolled != null) {
            htmlToShowList = htmlToShowList.replace(
                "#noOfStudents#",
                element.studentsEnrolled);
          }
          htmlToShowGrid = htmlToShowGrid += courseGrid;
          htmlToShowGrid = htmlToShowGrid.replace(
              "#courseTitle#", element.courseTitle);
          htmlToShowGrid = htmlToShowGrid.replace(
              "#altImageTitle#", element.courseTitle);
          htmlToShowGrid = htmlToShowGrid.replace(
              "#courseName#", element.courseName);
          htmlToShowGrid = htmlToShowGrid.replace(
              "#noOfStudents#", element.studentsEnrolled);
          htmlToShowGrid = htmlToShowGrid.replace(
              /#courseLink#/g, element.courseOldSlug);

          if (element.instructorName != null && element.instructorName != ""
              && element.instructors.length > 1) {
            htmlToShowTeacherGrid = '';
            $.each(element.instructors, function (index, elm) {
              if (element.instructorName == elm.teacherName) {

                htmlToShowTeacherGrid = htmlToShowTeacherGrid
                    + "<span> <a style='color: #000;' href='teacher/" + elm.slug
                    + "'><span>" + elm.teacherName + ",</span></a></span>";

              }
            });

            $.each(element.instructors, function (index, elm) {
              if (element.instructorName != elm.teacherName) {

                htmlToShowTeacherGrid = htmlToShowTeacherGrid
                    + "<span> <a style='color: #000;' href='teacher/" + elm.slug
                    + "'><span>" + elm.teacherName + "</span></a></span>";

              }
            });

            htmlToShowGrid = htmlToShowGrid.replace(
                "#TEACHERNAME#", htmlToShowTeacherGrid);
          }

          else {
            htmlToShowTeacherGrid = '';
            $.each(element.instructors, function (index, elm) {

              htmlToShowTeacherGrid = htmlToShowTeacherGrid
                  + "<span> <a style='color: #000;' href='teacher/" + elm.slug
                  + "'><span>" + elm.teacherName + "</span></a></span>";

              htmlToShowGrid = htmlToShowGrid.replace(
                  "#teacherName#", elm.teacherName);
              htmlToShowGrid = htmlToShowGrid.replace(
                  "#teacherSlug#", elm.slug);

            });
            htmlToShowGrid = htmlToShowGrid.replace(
                "#TEACHERNAME#", htmlToShowTeacherGrid);
          }

          htmlToShowGrid = htmlToShowGrid.replace(
              "#instructorSlug#", element.instituteSlug);

          htmlToShowGrid = htmlToShowGrid.replace(
              "#instituteName#", element.name);
          htmlToShowGrid = htmlToShowGrid.replace(
              "#courseImage#", element.courseImageUrl
              + '=w306-h190-s');
          if (element.price != 0) {
            htmlToShowGrid = htmlToShowGrid
            .replace(
                "#fees#",
                '<i class="fa fa-inr"></i>&nbsp;'
                + numberWithCommas(element.price));
          } else {
            htmlToShowGrid = htmlToShowGrid
            .replace(
                "#fees#",
                '<span style="color: green; font-size: 18px; font-family: open sans;">FREE</span>');
          }

          htmlToShowGrid = htmlToShowGrid.replace(
              "#courseStartDate#", element.startDate);
          var ratingHtml = '';
          if (element.courseRating != 0) {
            var j = element.courseRating;

            for (j; j >= 1; j--) {
              ratingHtml = ratingHtml
                  + '<i class=\"material-icons\">star_rate</i>';
            }
            if (j < 1 && j > 0) {
              ratingHtml = ratingHtml
                  + "<i class=\"material-icons\">star_half</i>";
            }
            htmlToShowGrid = htmlToShowGrid.replace(
                "#courseRating#", ratingHtml);
          }
        })
    $('#listStyle').html(htmlToShowList);
    $('.all-course-hubspotForm').show();
    $('#gridStyle > .mdl-grid').html(htmlToShowGrid);
    var paginationToShow = '';
    pageCount = response.totalPages;
    pageNumber = response.pageable.pageNumber;
    paginationToShow += "<ul class='pagination'>";
    if (pageNumber > 0 && pageCount > 1) {
      paginationToShow += "<li id='NA'></li>";
      paginationToShow += "<li id='first'><a href='#'>First</a></li>";
      paginationToShow += "<li id='previous'><a href='#'>Prev</a></li>";
    } else {
      paginationToShow += "<li id='NA'></li>";
      paginationToShow += "<li id='first'></li>";
      paginationToShow += "<li id='previous'></li>";
    }

    var paginationValues = [];
    var activePosition = 0;
    var i = 0;
    if (pageNumber > 4) {
      var leftCount = 8 - ((pageCount - pageNumber) >= 4 ? 4
          : (pageCount - pageNumber));
      leftCount = leftCount < pageNumber ? leftCount : (pageNumber - 1);
      while (i <= leftCount) {
        paginationValues[i] = pageNumber + i - leftCount;
        i++;
      }
    } else {
      while (i < pageNumber) {
        paginationValues[i] = i + 1;
        i++;
      }
    }
    activePosition = i + 3;
    if (pageNumber < pageCount) {
      var value = pageNumber + 1;
      var rightCount = 9 - paginationValues.length;
      var j = 0;
      while ((value <= pageCount) && (j < rightCount)) {
        paginationValues[i++] = value++;
        j++;
      }
    }
    for (var i = 0; i < paginationValues.length; i++) {
      paginationToShow += "<li><a href='#' rel='" + paginationValues[i]
          + "'>" + paginationValues[i] + "</a></li>";
    }
    if (pageNumber < pageCount && pageCount > 1) {
      paginationToShow += "<li id='next'><a href='#'>Next</a></li>";
      paginationToShow += "<li id='last'><a href='#'>Last</a></li>";
      paginationToShow += "<li id='NA'></li>";
    } else {
      paginationToShow += "<li id='next'></li>";
      paginationToShow += "<li id='last'></li>";
      paginationToShow += "<li id='NA'></li>";
    }
    paginationToShow += "</ul>";
    $('#pageNavigation').html(paginationToShow);
    $("#pageNavigation li:nth-child(" + (activePosition + 1) + ")")
    .addClass('active');
    if ($(window).width() < '767') {
      $("#pageNavigation .active").prevUntil('#previous').hide();
      $("#pageNavigation .active").nextUntil('#next').hide();
      $('#pageNavigation #NA').hide();
    }
  } else {
    $('#listStyle').html('<p>No Courses To Show</p>');
    $('#gridStyle > .mdl-grid').html('<p>No Courses To Show</p>');
  }
}
$('#pageNavigation').on('click', 'a', function () {

  pageTemp = $(this).text();
  if (pageTemp == 'First') {
    pageTemp = 1;
  }
  if (pageTemp == 'Last') {
    pageTemp = pageCount;
  }
  if (pageTemp == 'Prev') {
    pageTemp = pageNumber;
  }
  if (pageTemp == 'Next') {
    pageTemp = pageNumber + 2;
  }
  if (courseCategory == 'ALL') {
    if (examSegmentName == 'IES' || examSegmentName == 'GATE') {

      var filterCheckboxes = [];
      filterCheckboxes.push({
        filterName: 'courseExamSegment',
        value: examSegmentName
      }, {
        filterName: 'courseExamSegment',
        value: 'IES GATE'
      })
    } else {
      var filterCheckboxes = [];
      filterCheckboxes.push({
        filterName: 'courseExamSegment',
        value: examSegmentName
      })
    }
  } else {
    var filterCheckboxes = [];
    filterCheckboxes.push({
      filterName: 'courseCategory',
      value: courseCategory
    }, {
      filterName: 'courseExamSegment',
      value: examSegmentName
    })
  }
  $('.course-filter-card .mdl-checkbox__input').each(function () {
    if ($(this).is(":checked")) {
      var selectedFilters = $(this).val();
      var arr = selectedFilters.split(':');
      var arr_subcategory = $(this).attr("data-value").split(':');
      filterCheckboxes.push({
        filterName: arr[0],
        value: arr[1]
      }, {
        filterName: arr_subcategory[0],
        value: arr_subcategory[1]
      });
    }
  });
  selectedFilters = filterCheckboxes;
  fetchCoursesList(pageTemp - 1, selectedFilters, sort, 'pagination');
  $('body,html').animate({
    scrollTop: 0
  }, 200);
});

function selectBatch(price, selectedBatchId, type) {
  if ($('.batch-index input[type="radio"]:checked').length > 0) {
    $('.buy-course, .add-to-cart, .pay-later').css("pointer-events", "all");
    $('.buy-course, .add-to-cart, .pay-later').css("opacity", "1");
    if ($(window).width() < '840') {
      $('#buy-now-mobile').show();
    }
  }
  if (price < 1) {
    $('.add-to-cart, .pay-later, .small-redcarpet').hide();
  } else {
    $('.add-to-cart, .pay-later, .small-redcarpet').show();
  }

  $('#accordion').hide();
  $('#loader-accordian').show();
  /*
   * if (type == 'change') { $('.mat-content').animate({ scrollTop :
   * $('#session').position().top }, 1000); }
   */

  $
  .ajax({
    url: apiBasePath + '/batch/' + selectedBatchId + '/units',
    type: "GET",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function (response) {
      $('#loader-accordian').hide();
      var htmlToShow = '';
      var unitsToShow = '<h3>#unit#</h3> <div> <div class="dots-list"> <ol id="unitId#unitId#">#unitDescription#</ol>#seemore# </div> </div>';
      $
      .each(
          response.response,
          function (key, value) {
            var unitDescription = '';
            var unitId = key.replace(" ", "");
            htmlToShow = htmlToShow += unitsToShow;
            htmlToShow = htmlToShow.replace(
                "#unit#", key);
            htmlToShow = htmlToShow.replace(
                "#unitId#", unitId);
            $
            .each(
                value,
                function (index, element) {
                  if (index < 4) {
                    if (element.type == "ASSIGNMENT") {
                      if (element.free == true) {
                        unitDescription = unitDescription += '<li class="assignment-pdf-icon clickable" id="playFreeBtn'
                            + element.unitId
                            + '" onclick="playFreeUnit('
                            + element.unitId
                            + ');" >'
                            + element.title
                            + "&nbsp;&nbsp;"
                            // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                            // + element.noOfViews
                            + "<i class='free-lectures'>Free Session</i></li>";
                      } else {
                        if (element.publiclyBuyable == true && element.product == true) {
                          unitDescription = unitDescription += "<li class=\"assignment-pdf-icon \">"
                              + element.title
                              // + "&nbsp;&nbsp;"
                              // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                              // + element.noOfViews
                              +"<span class='singleBuyableUnits' onclick='addUnitToCart("
                              + element.unitId
                              + ")'><label><i class=\"fa fa-inr\" style='color:#8a8a8a;'></i>&nbsp;"
                              + element.price
                              + "</label><i class='material-icons  \ "+element.unitId+"\'>add_shopping_cart</i></span>\n"
                              + "</li>";
                        }
                        else{
                          unitDescription = unitDescription += "<li class=\"assignment-pdf-icon \">"
                              + element.title
                              // + "&nbsp;&nbsp;"
                              // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                              // + element.noOfViews
                              + "</li>";
                        }
                      }
                    }
                    else if (element.type == "NOTES") {
                      if (element.free == true) {
                        unitDescription = unitDescription += '<li class="notes-icon clickable" id="playFreeBtn'
                            + element.unitId
                            + '" onclick="playFreeUnit('
                            + element.unitId
                            + ');" >'
                            + element.title
                            + "</span>&nbsp;&nbsp;<i class='free-lectures'>Free Session</i></li>";
                      } else {
                        if (element.publiclyBuyable == true && element.product == true) {
                          unitDescription = unitDescription += "<li class=\"notes-icon \">"
                              + element.title
                              +"<span class='singleBuyableUnits' onclick='addUnitToCart("
                              + element.unitId
                              + ")'><label><i class=\"fa fa-inr\" style='color:#8a8a8a;'></i>&nbsp;"
                              + element.price
                              + "</label><i class='material-icons  \ "+element.unitId+"\'>add_shopping_cart</i></span>\n"
                              + "</li>";
                        }
                        else{
                          unitDescription = unitDescription += "<li class=\"notes-icon \">"
                              + element.title
                              + "</li>";
                        }

                      }
                    }
                    else {
                      if (element.free == true) {
                        unitDescription = unitDescription += '<li class="unit-play-icon clickable" id="playFreeBtn'
                            + element.unitId
                            + '" onclick="playFreeUnit('
                            + element.unitId
                            + ');" >'
                            + element.title
                            // + "&nbsp;&nbsp;"
                            // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                            // + element.noOfViews
                            + "&nbsp;&nbsp;<i class='free-lectures'>Free Session</i></li>";
                      } else {
                        if (element.publiclyBuyable == true && element.product == true) {
                          unitDescription = unitDescription += "<li class=\"unit-play-icon \">"
                              + element.title
                              // + "&nbsp;&nbsp;"
                              // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                              // + element.noOfViews
                              +"<span class='singleBuyableUnits' onclick='addUnitToCart("
                              + element.unitId
                              + ")'><label><i class=\"fa fa-inr\" style='color:#8a8a8a;'></i>&nbsp;"
                              + element.price
                              + "</label><i class='material-icons  \ "+element.unitId+"\'>add_shopping_cart</i></span>\n"
                              + "</li>";
                        }
                        else{
                          unitDescription = unitDescription += "<li class=\"unit-play-icon \">"
                              + element.title
                              // + "&nbsp;&nbsp;"
                              // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                              // + element.noOfViews
                              + "</li>";
                        }

                      }
                    }
                  } else {
                    if (element.type == "ASSIGNMENT") {
                      if (element.free == true) {
                        unitDescription = unitDescription += '<li data-display="none" style="display: none;" class="assignment-pdf-icon clickable" id="playFreeBtn'
                            + element.unitId
                            + '" onclick="playFreeUnit('
                            + element.unitId
                            + ');" >'
                            + element.title
                            // + "&nbsp;&nbsp;"
                            // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                            // + element.noOfViews
                            + "&nbsp;&nbsp;<i class='free-lectures'>Free Session</i></li>";
                      } else {
                        if (element.publiclyBuyable ==  true && element.product == true) {
                          unitDescription = unitDescription += "<li data-display='none'  style='display: none;' class=\"assignment-pdf-icon \">"
                              + element.title
                              // + "&nbsp;&nbsp;"
                              // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                              // + element.noOfViews
                              +"<span class='singleBuyableUnits' onclick='addUnitToCart("
                              + element.unitId
                              + ")'><label><i class=\"fa fa-inr\" style='color:#8a8a8a;'></i>&nbsp;"
                              + element.price
                              + "</label><i class='material-icons  \ "+element.unitId+"\'>add_shopping_cart</i></span>\n"
                              + "</li>";
                        }
                        else{
                          unitDescription = unitDescription += "<li data-display='none'  style='display: none;' class=\"assignment-pdf-icon \">"
                              + element.title
                              // + "&nbsp;&nbsp;"
                              // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                              // + element.noOfViews
                              + "</li>";
                        }
                      }
                    }
                    else if (element.type == "NOTES") {
                      if (element.free == true) {
                        unitDescription = unitDescription += '<li data-display="none" style="display: none;" class="notes-icon  clickable" id="playFreeBtn'
                            + element.unitId
                            + '" onclick="playFreeUnit('
                            + element.unitId
                            + ');" >'
                            + element.title
                            + "</span>&nbsp;&nbsp;<i class='free-lectures'>Free Session</i></li>";
                      } else {
                        if (element.publiclyBuyable == true && element.product == true) {
                          unitDescription = unitDescription += "<li data-display='none'  style='display: none;' class=\"notes-icon \">"
                              + element.title
                              +"<span class='singleBuyableUnits' onclick='addUnitToCart("
                              + element.unitId
                              + ")'><label><i class=\"fa fa-inr\" style='color:#8a8a8a;'></i>&nbsp;"
                              + element.price
                              + "</label><i class='material-icons  \ "+element.unitId+"\'>add_shopping_cart</i></span>\n"
                              + "</li>";
                        }
                        else{
                          unitDescription = unitDescription += "<li data-display='none'  style='display: none;' class=\"notes-icon \">"
                              + element.title
                              + "</li>";
                        }

                      }
                    }
                    else {
                      if (element.free == true) {
                        unitDescription = unitDescription += '<li data-display="none"  style="display: none;" class="unit-play-icon clickable" id="playFreeBtn'
                            + element.unitId
                            + '" onclick="playFreeUnit('
                            + element.unitId
                            + ');" >'
                            + element.title
                            // + "&nbsp;&nbsp;"
                            // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                            // + element.noOfViews
                            + "&nbsp;&nbsp;<i class='free-lectures'>Free Session</i></li>";
                      } else {
                        if (element.publiclyBuyable == true && element.product == true) {
                          unitDescription = unitDescription += "<li data-display='none'  style='display: none;' class=\"unit-play-icon \">"
                              + element.title
                              // + "&nbsp;&nbsp;"
                              // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                              // + element.noOfViews
                              +"<span class='singleBuyableUnits' onclick='addUnitToCart("
                              + element.unitId
                              + ")'><label><i class=\"fa fa-inr\" style='color:#8a8a8a;'></i>&nbsp;"
                              + element.price
                              + "</label><i class='material-icons  \ "+element.unitId+"\'>add_shopping_cart</i></span>\n"
                              + "</li>";
                        }
                        else{
                          unitDescription = unitDescription += "<li data-display='none'  style='display: none;' class=\"unit-play-icon \">"
                              + element.title
                              // + "&nbsp;&nbsp;"
                              // + "| <i class='fa fa-eye' aria-hidden='true' style='font-size: 16px;margin: 4px;'></i>"
                              // + element.noOfViews
                              + "</li>";
                        }
                      }
                    }
                  }
                });
            htmlToShow = htmlToShow.replace(
                "#unitDescription#",
                unitDescription);
            htmlToShow = htmlToShow
            .replace(
                "#seemore#",
                '<a class="see-more-units" onclick="seeMore(this)"><span>View More</span><i style="margin-left: 4px;" class="see-more-units fa fa-angle-down" aria-hidden="true"></i></a>');
          });
      $('#accordion').html(htmlToShow).show();
      $("#accordion").accordion({
        active: 0
      });
      $("#accordion").accordion("refresh");
      stopPosition = $('#session').offset().top
          - navWrap.offset().top + $("#accordion").height()
          + $('.mat-content').scrollTop();
    },
    error: function (response) {
      $('#loader-accordian').hide();
    }
  })
}

function startCourse(batchId) {
  $.ajax({
    url: '/lms?batch=' + batchId,
    type: "GET",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function (response) {

    },
    error: function (response) {
      $('#loader-accordian').hide();
    }
  });

}

function seeMore(el) {
  $(el).siblings('ol').find('li[data-display=none]').toggle("50");
  if ($(el).find('span').html() == 'View More') {
    $(el).find('span').html("View Less");
    $(el).find('span').siblings('i').removeClass("fa-angle-down").addClass(
        "fa-angle-up");
  } else {
    $(el).find('span').html("View More");
    $(el).find('span').siblings('i').removeClass("fa-angle-up").addClass(
        "fa-angle-down");
  }
}

/*
 * $('.course-filter-card .mdl-checkbox__input').change(function() {
 * changeFilterTabs(); })
 */
$('document').ready(function () {
  $("#header-menu nav > ul > li:nth-child(1) > a").addClass("active");

})

var category = "";
$('#courseSortBy, .courseSortBy').change(function () {
  sort = $(this).find('option').filter(':selected').val();
  if (selectedFilters.length > 0) {
    fetchCoursesList('0', selectedFilters, sort, 'sort');
  } else {
    fetchCoursesList('0', courseCategory, sort, 'sort');
  }

})

$('.show-filter-mobile').click(function () {
  $('.course-filter-card').show();
})
$('.apply-filter-mobile').click(function () {
  $('.course-filter-card').hide();
})

function changeCourseCategory(el) {
  var selectedCategory = $(el).find('option:selected').val();
  $('#category_' + selectedCategory).show().siblings(
      '.select-category-filters').hide();
  $('.select-category-filters').val("Select");
}

var dialogPayLater = document.querySelector('#thankyou-paylater');
$("form[name=pay-later-form]").validate({

  rules: {
    firstName: {
      required: true
    },
    lastName: {
      required: true
    },
    email: {
      required: true,
      email: true
    },
    mobile: {
      required: true,
      minlength: 10
    },
    collegeName: {
      required: true
    },
    passyear: {
      required: true
    },
    city: {
      required: true
    }
  },
  messages: {
    firstName: {
      required: "Please provide your First Name"
    },
    lastName: {
      required: "Please provide your Last Name"
    },
    email: {
      required: "Please provide an Email Address",
      email: "Email Address is Not Valid"
    },
    mobile: {
      required: "Please provide your Mobile Number",
      minlength: "Mobile Number must be 10 Digits long"
    },
    collegeName: {
      required: "Please provide your College Name"
    },
    passyear: {
      required: "Please Select your Current/Pass Year"
    },
    city: {
      required: "Please Select Your City"
    }
  },
  submitHandler: function (form, e) {
    $('#modal-submit-btn').hide();
    $('#loader-pay-later').show();
    e.preventDefault();
    var input = {
      email: $('#emailId').val(),
      firstName: $('#first-name').val(),
      lastName: $('#last-name').val(),
      mobile: $('#mobile').val(),
      collegeName: $('#college-name').val(),
      city: $('#city').val(),
      passyear: $('#college-year').val(),
      courseName: $('#course-name').val(),
      coursePrice: $('#course-price').val(),
      coursePage: $('#course-page').val(),
    }

    $.ajax({
      method: "POST",
      url: apiBasePath + "/paylater",
      data: JSON.stringify(input),
      dataType: "text",
      contentType: "application/json",
      processData: false,

      success: function (response) {
        (function () {
          'use strict';

          if (!dialogPayLater.showModal) {
            dialogPolyfill.registerDialog(dialogPayLater);
          }
          dialogPayLater.showModal();

        }());
        $('#modal-submit-btn').show();
        $('#loader-pay-later').hide();
        dialogPayLaterOne.close();

      },

      error: function (response) {
        alert("Email Sending failed");
        $('#modal-submit-btn').show();
        $('#loader-pay-later').hide();
        dialogPayLaterOne.close();
      }
    });
  }

});

function reset() {
  $('#filtersTab').html('');
  $('input:checkbox').removeAttr('checked');
  $('.mdl-checkbox').removeClass("is-checked");
  fetchFilters('IAS', 'ALL', 'popular');
}