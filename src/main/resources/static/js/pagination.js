$(function () {
    // 페이지 링크 클릭 처리
    $('.page_link, .prev_btn, .next_btn').on('click', function(e) {
        e.preventDefault();
        $('#page').val($(this).data('page'));
        $('#actionForm').submit();
    });

    $('#department-filter').on('change', function(e){
        e.preventDefault();
        $('#filter-major').val($(this).val());
        $('#filter-form').submit();
    })

    $('#department-filter option').each(function () {
        if ($(this).val() === $('#major').val()) {
            $(this).prop('selected', true);
        }
    })

    $('#person-type-filter').on('change', function(e){
        //e.preventDefault();
        $('#person').val($(this).val());
    })

    $('#person-type-filter option').each(function () {
        if ($(this).val() === $('#person').val()) {
            $(this).prop('selected', true);
            filterData();
        }
    })

    $('#search-field option').each(function () {
        if ($(this).val() === $('#field').val()) {
            $(this).prop('selected', true);
        }
    })
})