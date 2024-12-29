let studentNo; // studentNo를 외부에서도 사용

// DOMContentLoaded 이벤트로 초기화
document.addEventListener("DOMContentLoaded", () => {
  studentNo = document.querySelector('meta[name="studentNo"]').getAttribute('content');
  console.log("DOMContentLoaded에서 studentNo:", studentNo); // studentNo 값 확인
});

// sessionStorage 값 확인 (테스트용)
console.log(sessionStorage.getItem('username'));
console.log(sessionStorage.getItem('token'));

// 달력 관련 요소
const calendarDays = document.querySelector('.calendar_days');
const monthDisplay = document.getElementById('month');
let date = new Date(); // 현재 날짜

// 달력 렌더링 함수
function renderCalendar() {
  const year = date.getFullYear();
  const month = date.getMonth();
  monthDisplay.innerText = `${year}년 ${month + 1}월`; // 월 출력

  // 달력 초기화
  calendarDays.innerHTML = '';

  // 월의 첫 번째 날과 마지막 날 계산
  const firstDay = new Date(year, month, 1).getDay();
  const lastDate = new Date(year, month + 1, 0).getDate();

  // 빈칸 채우기
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

// 이전 달로 이동
function prevMonth() {
  date.setMonth(date.getMonth() - 1);
  renderCalendar();
}

// 다음 달로 이동
function nextMonth() {
  date.setMonth(date.getMonth() + 1);
  renderCalendar();
}

// 수업 정보 표시
async function showClassInfo(year, month, day) {
  const dateString = `${year}-${month}-${day}`;
  console.log("요청 날짜:", dateString);
  const response = await fetch(`/Attendance/date?date=${dateString}`);

  if (!response.ok) {
    console.error('출석 데이터를 가져오는 데 실패했습니다.');
    return;
  }

  const attendanceData = await response.json();
  console.log('Fetched Data:', attendanceData); // 데이터 확인

  const attendanceList = document.getElementById('attendance-list');
  attendanceList.innerHTML = '';

  if (!attendanceData || attendanceData.length === 0) {
    attendanceList.innerHTML = '<p>수업이 없습니다.</p>';
    document.getElementById('save-attendance').style.display = 'none'; // 저장 버튼 숨김
    return;
  }
  console.log('attendanceData:', attendanceData);
  attendanceData.forEach((item, index) => {
    console.log(`Item ${index}:`, item);
  });

  const userRole = document.querySelector('meta[name="user-role"]').getAttribute('content');
  console.log(userRole);

  attendanceData.forEach(attendance => {
   console.log('student.no:', attendance.studentNo, 'studentNo:', studentNo);
      // 로그인한 학생 번호와 일치하는 학생만 처리
      if (String(attendance.studentNo) === String(studentNo)) {  // loggedInStudentNo 대신 studentNo를 사용
        const studentRow = document.createElement('div');
        studentRow.className = 'student-row';
        studentRow.innerHTML = `
          <span>${attendance.studentName}</span>  <!-- 여기서 attendance.studentName 사용 -->
                   <select data-student-id="${attendance.studentName}" ${attendance.studentName ? 'disabled' : ''}>  <!-- attendance.student 사용 -->
                      <option value="미확인" ${attendance.attendanceStatus === '미확인' ? 'selected' : ''}>미확인</option>
                      <option value="출석" ${attendance.attendanceStatus === '출석' ? 'selected' : ''}>출석</option>
                      <option value="결석" ${attendance.attendanceStatus === '결석' ? 'selected' : ''}>결석</option>
                      <option value="지각" ${attendance.attendanceStatus === '지각' ? 'selected' : ''}>지각</option>
                   </select>
        `;
        attendanceList.appendChild(studentRow);
      }
    });
}

// 출석 상태 저장
document.getElementById('save-attendance').addEventListener('click', async () => {
  const selects = document.querySelectorAll('#attendance-list select');

  const updatedData = Array.from(selects).map(select => ({
    studentId: select.getAttribute('data-student-id'),
    attendanceStatus: select.value
  }));

  try {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const response = await fetch('/Attendance/save', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        [csrfHeader]: csrfToken
      },
      body: JSON.stringify(updatedData)
    });

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

// 초기화
renderCalendar();