$(document).ready(function () {
  let currentIndex = 0;
  const photos = $(".main-top-photo li");
  const photoCount = photos.length;

  // 첫 번째 이미지를 보이도록 설정
  photos.eq(currentIndex).fadeIn();

  // 페이드 인/아웃 반복 함수
  setInterval(function () {
    // 현재 이미지를 페이드 아웃
    photos.eq(currentIndex).fadeOut(1000);

    // 다음 이미지로 인덱스 변경
    currentIndex = (currentIndex + 1) % photoCount;

    // 다음 이미지를 페이드 인
    photos.eq(currentIndex).fadeIn(1000);
  }, 3000); // 3초마다 이미지 전환

  // 페이지 로드 시 첫 번째 이미지를 기본 이미지로 설정
  var firstImgSrc = $(".hit-controller-photo li")
    .first()
    .find("img")
    .attr("src");
  $(".hit-photo-wrap img").attr("src", firstImgSrc);

  // 첫 번째 이미지에는 'active' 클래스를 추가하여 흐리지 않도록 설정
  $(".hit-controller-photo li img:first-child").addClass("active");

  // li 클릭 시
  $(".hit-controller-photo li").on("click", function () {
    // 클릭한 li 안의 img src 값을 가져와서 .hit-photo-wrap의 img src에 설정
    var imgSrc = $(this).find("img").attr("src");
    $(".hit-photo-wrap img").attr("src", imgSrc);

    // 모든 li의 active 클래스 제거
    $(".hit-controller-photo li img").removeClass("active");

    // 클릭한 이미지에만 active 클래스 추가
    $(this).find("img").addClass("active");
  });
  // 스크롤 위치가 20% 이상일 때 버튼을 표시
  $(window).scroll(function () {
    if ($(this).scrollTop() > $(document).height() * 0.2) {
      $("#scrollToTopBtn").fadeIn(); // 버튼 표시
    } else {
      $("#scrollToTopBtn").fadeOut(); // 버튼 숨기기
    }
  });

  // 버튼 클릭 시 상단으로 스크롤
  $("#scrollToTopBtn").click(function () {
    $("html, body").animate({ scrollTop: 0 }, 500); // 0은 페이지 상단
  });
});
