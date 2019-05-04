var examSegmentName;
var sortCourses;
var selectedFilters = [];
function changeFilterTabs() {
	var htmlToShow = '';

	$('.teachers-filter-card .mdl-checkbox')
			.each(
					function(i) {

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
									+ "').removeClass('is-checked').find('.mdl-checkbox__input').prop('checked',false);fetchTeachers('popular')"
									+ '" class="btn-filter" id="filtersTab_btn_'
									+ i
									+ '"><i class="fa fa-times"></i></button></div>';
						}
					});
	$('#filtersTab').html(htmlToShow);
	fetchTeachers('popular');
}
$(document).ready(function() {
	$("#header-menu nav > ul > li:nth-child(2) > a").addClass("active");
})
function fetchFilters(examSegment, sort) {
	$('#filtersTab').html('');
	$('input:checkbox').removeAttr('checked');
	$('.mdl-checkbox').removeClass("is-checked");
	$('.load-courses-progress-bar').show();
	examSegmentName = examSegment;
	sortTeachers = sort;
	$('#changeCourseCategory').val(examSegmentName);
	$('#category_' + examSegmentName).show();
	$('#course-listing-card').find('a[data-id =' + examSegmentName + ']')
			.addClass("is-active").siblings().removeClass("is-active");
	fetchAllTeachers(examSegmentName, sortTeachers, 'byFilter');
	$
			.ajax({
				url : apiBasePath + '/teachers/filters/exam/' + examSegment
						+ '/category/ALL',

				type : "GET",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				success : function(response) {
					var htmlToShow = '<div class="course-filters mdl-card__actions"><p>'
							+ ' </p>'
					var htmlToShowFilterHead = '#SubCategory#';
					var htmlToShowFilterData = '#filters#';
					$
							.each(
									response.filters,
									function(key, value) {
										key = key.replace(" ", "_");
										keyToShow = key.replace("_", " ");
										htmlToShow = htmlToShow
												+ htmlToShowFilterHead;
										if (keyToShow == examSegment) {
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

										$
												.each(
														value,
														function(index, element) {
															htmlToShow = htmlToShow
																	+ htmlToShowFilterData;
															var filterCheckboxes = '';
															var filterList = '';
															$
																	.each(
																			element,
																			function(
																					tempFilter,
																					subcategoryFilter) {
																				var key = Object
																						.keys(element)[tempFilter];
																				if (subcategoryFilter != null) {
																					filterCheckboxes = filterCheckboxes
																							+ '<span style="font-weight: bold;margin: 12px 8px 8px;display: block;" class="filter-label">'
																							+ tempFilter
																							+ '</span>';
																					$
																							.each(
																									subcategoryFilter,
																									function(
																											filterKey,
																											filterResponse) {
																										if (filterResponse != '') {
																											filterCheckboxes = filterCheckboxes
																													+ '<label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="filter'
																													+ filterResponse
																													+ filterKey
																													+ '"> <input type="checkbox" id="filter'
																													+ filterResponse
																													+ filterKey
																													+ '" class="mdl-checkbox__input" onchange="changeFilterTabs();" value="subjects'
																													+ ':'
																													+ filterResponse
																													+ '"/> <span class="mdl-checkbox__label">'
																													+ filterResponse
																													+ '</span>  </label> <br />';
																										}
																									})
																				}

																			});
															htmlToShow = htmlToShow
																	.replace(
																			'#filters#',
																			filterList
																					+ filterCheckboxes);
														});
										htmlToShow = htmlToShow + '</div>';
										$('.mdl-checkbox').not(':lt(3)').hide();
									});
					$('#teacherDynamicFilters').html(htmlToShow);
					componentHandler.upgradeDom();
				}
			})
}
$('.teachers-filter-card .mdl-checkbox__input').change(function() {
	changeFilterTabs();
})
var sort = "popular";
$('#teacherSortBy, .teacherSortBy').change(function() {
	sort = $(this).find('option').filter(':selected').val();
	var filter = [];
	filter.push({
		filterName : 'teacherExamSegment',
		value : examSegmentName
	});
	$('#teacherDynamicFilters .mdl-checkbox__input').each(function() {
		if ($(this).is(":checked")) {
			var selectedFilters = $(this).val();
			var arr = selectedFilters.split(':');
			filter.push({
				filterName : arr[0],
				value : arr[1]
			});
		}
	});
	if ($('#teacherDynamicFilters .mdl-checkbox__input:checked').length > 0) {
		fetchAllTeachers(filter, sortTeachers, 'bySort');
	} else {
		fetchAllTeachers(examSegmentName, sortTeachers, 'byFilter');
	}
})

function fetchAllTeachers(filterTeacher, sortTeachers, type) {
	$('#totalTeachers').hide();
	$('#teachersList').hide();
	$('#loader-all-teachers').show();
	if (type == 'bySort') {
		var obj = {
			filters : filterTeacher
		};
	} else {
		var obj = {
			filters : [ {
				filterName : 'teacherExamSegment',
				value : examSegmentName
			} ]
		};
	}

	$.ajax({
		url : apiBasePath + "/teachers?page=0&size=100&sort=" + sort
				+ '&search=' + encodeURIComponent(JSON.stringify(obj)),
		type : "GET",
		dataType : "json",
		contentType : "application/json",
		processData : false,

		success : function(response) {
			$('#loader-all-teachers').hide();
			showTeachersList(response, examSegmentName);
		},

		error : function(response) {
			$('#loader-all-teachers').hide();
		}
	});
}

function showTeachersList(response, examSegmentName) {
	var htmlToShow = '';
	var teachersList = '<div class="mdl-cell mdl-cell--3-col mdl-cell--4-col-tablet mdl-cell--12-col-phone"> <div class="all-teachers-card demo-card-square mdl-card mdl-shadow--2dp"> <div class=""> <a href="/teacher/#teacherUrl#"><img src="#displayPicture#" alt="#altImageTitle#"></a> </div> <h3> <a href="/teacher/#teacherUrl#">#teacherName#</a> </h3> <p> <i><a style="height: 20px; display: block;" href="/institute/#instituteUrl#">#instituteName#</a></i> </p> <p> <span class="teachers-rating">  </span> </p> '
			+ '<div class="teacher-subjects-ellipsis">#subject#</div>'
			+ '<div class="mdl-card__actions"> <span class="float-left"><i class="fa fa-file-text-o" aria-hidden="true"></i><span class="no-of-students">#noOfCourseSize# #courseText#</span></span> </div> </div> </div>';
	if (response.content.length == 1) {
		$('#totalTeachers').html(
				response.content.length + ' Teacher in ' + examSegmentName)
				.show();
	} else {
		$('#totalTeachers').html(
				response.content.length + ' Teachers in ' + examSegmentName)
				.show();
	}

	if (response != null) {
		$
				.each(
						response.content,
						function(index, element) {
							var subjects = '';
							if (element.name != null
									&& element.name != undefined) {
								htmlToShow = htmlToShow += teachersList;
								htmlToShow = htmlToShow.replace(
										"#teacherName#", element.teacherName);
								htmlToShow = htmlToShow.replace(
										"#altImageTitle#", element.teacherName);
							} else {
								htmlToShow = htmlToShow.replace(
										"#teacherName#", "");
							}
							if (element.name != null
									&& element.name != undefined) {
								htmlToShow = htmlToShow.replace(
										"#instituteName#", element.name);
							} else {
								htmlToShow = htmlToShow.replace(
										"#instituteName#", "");
							}
							if (element.instituteSlug != null
									&& element.instituteSlug != undefined) {

								htmlToShow = htmlToShow
										.replace("#instituteUrl#",
												element.instituteSlug);
							} else {

								htmlToShow = htmlToShow.replace(
										"#instituteUrl#", "");
							}
							if (element.displayPictureUrl != null) {
								htmlToShow = htmlToShow.replace(
										"#displayPicture#",
										element.displayPictureUrl
												+ '=w123-h123');
							} else {
								htmlToShow = htmlToShow
										.replace(
												"#displayPicture#",
												'https://lh3.googleusercontent.com/46iL17PdegcEAEFahtD4XKq703TlOyM1UdmSsKB4iuK4AtFnQU8SZ1TOZGZJenxP9zvQtnjz0kpVEymD0_WJBHWvZk8=s140-e7');
							}
							var subjectList = [];
							if (element.subjects != null
									&& element.subjects.length > 0) {
								subjectList = element.subjects.split(',');
							}
							if (subjectList != null && subjectList.length > 0) {

								$
										.each(
												subjectList,
												function(i) {
													subjects = subjects
															+ '<span class="mdl-chip"><span class="mdl-chip__text">'
															+ subjectList[i]
															+ '</span> </span>';

												});

								htmlToShow = htmlToShow.replace("#subject#",
										subjects);
							} else {
								htmlToShow = htmlToShow
										.replace("#subject#", "");
							}
							if (element.slug != null) {
								htmlToShow = htmlToShow.replace(
										/#teacherUrl#/g, element.slug);
							}

							if (element.courseSize == 1) {
								htmlToShow = htmlToShow.replace(
										"#noOfCourseSize#", element.courseSize);
								htmlToShow = htmlToShow.replace("#courseText#",
										"Course");
							} else {
								htmlToShow = htmlToShow.replace(
										"#noOfCourseSize#", element.courseSize);
								htmlToShow = htmlToShow.replace("#courseText#",
										"Courses");
							}

							var ratingHtml = '';
							if (element.teacherRating != 0) {
								var j = element.teacherRating;

								for (j; j > 0.5; j--) {
									ratingHtml = ratingHtml
											+ '<i class=\"material-icons\">star_rate</i>';
								}
								if (j != 0) {
									ratingHtml = ratingHtml
											+ "<i class=\"material-icons\">star_half</i>";
								}

							}
							htmlToShow = htmlToShow.replace("#teacherRating#",
									ratingHtml);
						})
	} else {
		htmlToShow = '<h4 class="text-center">No teachers to show</h4>';
	}
	$('#teachersList').html(htmlToShow).show();
}
function fetchTeachers(sort) {
	$('#teachersList').hide();
	$('#loader-all-teachers').show();
	var filter = [];
	filter.push({
		filterName : 'teacherExamSegment',
		value : examSegmentName
	});
	$('#teacherDynamicFilters .mdl-checkbox__input').each(function() {
		if ($(this).is(":checked")) {
			var selectedFilters = $(this).val();
			var arr = selectedFilters.split(':');
			filter.push({
				filterName : arr[0],
				value : arr[1]
			});
		}
	});
	var filterRequest = {};
	filterRequest["filters"] = filter;
	$
			.ajax({
				url : apiBasePath + '/teachers?page=0&size=100&sort=' + sort
						+ '&search='
						+ encodeURIComponent(JSON.stringify(filterRequest)),
				type : "GET",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				success : function(response) {
					$('#totalTeachers').html(
							response.totalElements + ' Teachers in '
									+ examSegmentName);
					showTeachersList(response, examSegmentName);
					$('#teachersList').show();
					$('#loader-all-teachers').hide();
				},

				error : function(response) {
					$('#teachersList').show();
					$('#loader-all-teachers').hide();
				}
			});

}
$('.show-filter-mobile').click(function() {
	$('.teachers-filter-card').show();
})
$('.apply-filter-mobile').click(function() {
	$('.teachers-filter-card').hide();
})
function reset() {
	$('#filtersTab').html('');
	$('input:checkbox').removeAttr('checked');
	$('.mdl-checkbox').removeClass("is-checked");
	fetchFilters('IAS', 'popular');
}