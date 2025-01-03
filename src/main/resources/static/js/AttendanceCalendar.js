console.log(sessionStorage.getItem('username')); // 저장된 사용자명 확인
console.log(sessionStorage.getItem('token'));    // 저장된 토큰 확인
const calendarDays = document.querySelector('.calendar_days'); // calendar_day 클래스를 가진 요소
const monthDisplay = document.getElementById('month'); // 달력 상단 월 표시
let date = new Date(); // 현재 날짜

// 달력 렌더링 함수
function renderCalendar() {
  const year = date.getFullYear(); // 현재 연도
  const month = date.getMonth(); // 현재 월
  monthDisplay.innerText = `${year}년 ${month + 1}월`; // 월 출력

  // 달력 초기화
  calendarDays.innerHTML = '';

  // 월의 첫 번째 날과 마지막 날 계산
  const firstDay = new Date(year, month, 1).getDay(); // 해당 월의 첫날 요일
  const lastDate = new Date(year, month + 1, 0).getDate(); // 해당 월의 마지막 날

  // 빈칸 채우기 (첫날이 일요일이면 첫 번째 칸에 빈칸이 없고, 월요일이면 첫 번째 칸에 빈칸 하나 추가)
  for (let i = 0; i < firstDay; i++) {
    calendarDays.appendChild(document.createElement('div'));
  }

  // 날짜 생성
  for (let day = 1; day <= lastDate; day++) {
    const dayElement = document.createElement('div');
    dayElement.innerText = day;
    dayElement.onclick = () => showClassInfo(year, month + 1, day); // 날짜 클릭 시 수업 정보 출력
    calendarDays.appendChild(dayElement);
  }
}

// 이전 달로 이동하는 함수
function prevMonth() {
  date.setMonth(date.getMonth() - 1); // 한 달 빼기
  renderCalendar(); // 달력 다시 렌더링
}

// 다음 달로 이동하는 함수
function nextMonth() {
  date.setMonth(date.getMonth() + 1); // 한 달 더하기
  renderCalendar(); // 달력 다시 렌더링
}

// 수업 정보 표시하는 함수
async function showClassInfo(year, month, day) {
  const dateString = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
  const response = await fetch(`/Attendance/date?date=${dateString}`);

  if (!response.ok) {
    console.error('출석 데이터를 가져오는 데 실패했습니다.');
    return;
  }

  const attendanceData = await response.json();
  console.log('Fetched Data:', attendanceData); // 데이터가 제대로 반환되는지 확인

  const attendanceList = document.getElementById('attendance-list');
  attendanceList.innerHTML = '';

  if (!attendanceData || attendanceData.length === 0) {
    attendanceList.innerHTML = '<p>수업이 없습니다.</p>';
    document.getElementById('save-attendance').style.display = 'none'; // 저장 버튼 숨김
    return;
  }

  const userRole = document.querySelector('meta[name="user-role"]').getAttribute('content');
  console.log(userRole);


  // 출석 데이터 처리
  attendanceData.forEach(student => {
    // student 객체의 데이터가 제대로 전달되는지 확인하는 로그


    const studentRow = document.createElement('div');
    studentRow.className = 'student-row';
    studentRow.innerHTML = `
      <span>${student.studentName}</span> <!-- studentName 확인 -->
      <select data-student-id="${student.id}"
        <option value="미확인" ${student.attendanceStatus === '미확인' ? 'selected' : ''}>미확인</option>
        <option value="출석" ${student.attendanceStatus === '출석' ? 'selected' : ''}>출석</option>
        <option value="결석" ${student.attendanceStatus === '결석' ? 'selected' : ''}>결석</option>
        <option value="지각" ${student.attendanceStatus === '지각' ? 'selected' : ''}>지각</option>
      </select>
    `;
    attendanceList.appendChild(studentRow);
  });



}

document.getElementById('save-attendance').addEventListener('click', async () => {
  const selects = document.querySelectorAll('#attendance-list select');

  // 출석 상태 데이터 생성
  const updatedData = Array.from(selects).map(select => ({
    studentId: select.getAttribute('data-student-id'),
    attendanceStatus: select.value
  }));

  try {
    // CSRF 토큰 가져오기
    // CSRF 토큰 및 헤더 이름 읽기
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // POST 요청
    const response = await fetch('/Attendance/save', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        [csrfHeader]: csrfToken // 동적으로 읽은 CSRF 헤더와 토큰 사용
      },
      body: JSON.stringify(updatedData)
    });

    // 응답 처리
    if (response.ok) {
      alert('출석 상태가 저장되었습니다.');
    } else if (response.status === 403) {
      alert('접근 권한이 없습니다. 다시 로그인하세요.');
    } else {
      alert('저장에 실패했습니다. 오류 코드: ' + response.status);
    }
  } catch (error) {
    console.error('출석 상태 저장 중 오류 발생:', error);
    alert('저장 중 오류가 발생했습니다. 관리자에게 문의하세요.');
  }
});

// 달력 렌더링 초기화
renderCalendar();