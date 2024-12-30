$(function () {
    $('.major-pagination .page_link').on('click', function(e) {
        e.preventDefault();
        var page = $(this).data('page');
        $('#major-page').val(page);
        $('#major-actionForm').submit();
    });

    $('.major-pagination .prev_btn, .major-pagination .next_btn').on('click', function(e) {
        e.preventDefault();
        $('#major-page').val($(this).data('page'));
        $('#major-actionForm').submit();
    });

    $('.general-pagination .page_link').on('click', function(e) {
        e.preventDefault();
        var page = $(this).data('page');
        $('#general-page').val(page);
        $('#general-actionForm').submit();
    });

    $('.general-pagination .prev_btn, .general-pagination .next_btn').on('click', function(e) {
        e.preventDefault();
        $('#general-page').val($(this).data('page'));
        $('#general-actionForm').submit();
    });

    $('.page_link, .prev_btn, .next_btn').on('click', function(e) {
        e.preventDefault();
        var page = $(this).data('page');
        $('#page').val(page);
        $('#actionForm').submit();
    });

    $('#department-filter').on('change', function(e) {
        e.preventDefault();
        $('#filter-major').val($(this).val());
        $('#filter-form').submit();
    });

    $('#department-filter option').each(function () {
        if ($(this).val() === $('#major').val()) {
            $(this).prop('selected', true);
        }
    });

    $('#person-type-filter').on('change', function(e) {
        $('#person').val($(this).val());
    });

    $('#person-type-filter option').each(function () {
        if ($(this).val() === $('#person').val()) {
            $(this).prop('selected', true);
            filterData();
        }
    });

    $('#search-field option').each(function () {
        if ($(this).val() === $('#field').val()) {
            $(this).prop('selected', true);
        }
    });
});