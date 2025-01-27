document.addEventListener("DOMContentLoaded", () => {
  const opener = document.querySelector(".nav-opener");
  const responsibleNav = document.querySelector(".responsible-nav");
  const body = document.querySelector("body");
  const responsibleClose = document.querySelector(".responsible-nav-close");

  opener.addEventListener("click", () => {
    // 메뉴 활성화 클래스 토글
    responsibleNav.classList.toggle("active");
    body.style.overflow = "hidden";
  });
  responsibleClose.addEventListener("click", () => {
    responsibleNav.classList.remove("active");
    body.style.overflow = "scroll";
  });
  document.querySelectorAll(".rmenu-title").forEach(function (rmenuTitle) {
    rmenuTitle.addEventListener("click", function (r) {
      const rmenuItem = r.target.closest(".rmenu-item");

      document.querySelectorAll(".rmenu-item").forEach(function (item) {
        if (item !== rmenuItem) {
          item.classList.remove("active");
        }
      });

      // 현재 메뉴 항목을 활성화/비활성화
      rmenuItem.classList.toggle("active");
    });
  });
  // 페이지 로딩 시 첫 번째 메뉴 항목 활성화
  window.addEventListener("DOMContentLoaded", function () {
    const firstMenuItem = document.querySelector(
      ".responsible-nav .rmenu-item"
    );
    if (firstMenuItem) {
      firstMenuItem.classList.add("active");
    }
  });
});
