$(document).ready(function(){
    $(".portal-admin-course-open-button").on("click",function(){
        $.ajax({
            url: '/a/course/status',
            method: 'GET',
            success: function(data){
                if(data === 'OPEN'){
                    if (confirm("수강신청 서버를 닫으시겠습니까?")) {
                        $.ajax({
                            url: '/a/course/close',
                            method: 'GET',
                            success: function(status){
                                if(status === 'ok'){
                                    alert("SERVER CLOSED!!")
                                    location.reload();
                                } else {
                                    alert(status.responseText);
                                    location.reload();
                                }
                            },
                            error: function(xhr){
                                alert(xhr.responseText);
                            }
                        })
                    }
                } else {
                    if (confirm("수강신청 서버를 여시겠습니까?")) {
                        $.ajax({
                            url: '/a/course/open',
                            method: 'GET',
                            success: function(status){
                                if(status === 'ok'){
                                    alert("SERVER OPEN!!")
                                    location.reload();
                                } else {
                                    alert(status.responseText);
                                    location.reload();
                                }
                            },
                            error: function(xhr){
                                alert(xhr.responseText);
                            }
                        })
                    }
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText + error);
            }
        })
    })

    $(".portal-student-course-button").on("click", function(){
        $.ajax({
            url: '/s/course/status',
            method: 'GET',
            success: function(data){
                if(data === 'OPEN'){
                    openNewWindow('/s/course', 1920, 1080);
                } else {
                    alert("수강신청 기간이 아닙니다!!")
                    location.reload();
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        })
    })


})

// 새 창 띄우는 스크립트
function openNewWindow(url, width, height) {
    var screenWidth = window.innerWidth;
    var screenHeight = window.innerHeight;

    var left = (screenWidth - width) / 2 + window.screenX;
    var top = (screenHeight - height) / 2 + window.screenY;

    window.open(url, '_blank', `width=${width},height=${height},top=${top},left=${left}`);
}

 document.addEventListener("DOMContentLoaded", () => {
        const portalNav = document.querySelector(".portal-nav");
        const ul = portalNav.querySelector("ul");

        // 초기 상태 설정
        ul.style.display = "none"; // 처음에는 보이지 않음
        ul.style.marginRight = "-340px"; // 화면 밖으로 위치
        ul.style.opacity = "0"; // 투명하게 설정

        // 페이지 로드 후 애니메이션 실행
        setTimeout(() => {
          ul.style.display = "block"; // 보이게 설정
          setTimeout(() => {
            ul.style.transition = "margin-right 1s ease, opacity 1s ease"; // 애니메이션 설정
            ul.style.marginRight = "0px"; // 제자리로 이동
            ul.style.opacity = "1"; // 보이도록 변경
          }, 300); // 조금 느리게 실행
        }, 100); // 초기 딜레이
      });