$(document).ready(function() {
    let currentIndex = 0;
    const photos = $('.main-top-photo li');
    const photoCount = photos.length;

    // 첫 번째 이미지를 보이도록 설정
    photos.eq(currentIndex).fadeIn();

    // 페이드 인/아웃 반복 함수
    setInterval(function() {
        // 현재 이미지를 페이드 아웃
        photos.eq(currentIndex).fadeOut(1000);

        // 다음 이미지로 인덱스 변경
        currentIndex = (currentIndex + 1) % photoCount;

        // 다음 이미지를 페이드 인
        photos.eq(currentIndex).fadeIn(1000);
    }, 3000); // 3초마다 이미지 전환
});