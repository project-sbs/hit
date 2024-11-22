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
  const dateString = `${year}-${month}-${day}`; // 날짜 형식
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

  // 출석 데이터 처리
  attendanceData.forEach(student => {
    // student 객체의 데이터가 제대로 전달되는지 확인하는 로그
    console.log('Student Data:', student);

    const studentRow = document.createElement('div');
    studentRow.className = 'student-row';
    studentRow.innerHTML = `
      <span>${student.studentName}</span> <!-- studentName 확인 -->
      <select data-student-id="${student.id}"> <!-- id 확인 -->
        <option value="미확인" ${student.attendanceStatus === '미확인' ? 'selected' : ''}>미확인</option>
        <option value="출석" ${student.attendanceStatus === '출석' ? 'selected' : ''}>출석</option>
        <option value="결석" ${student.attendanceStatus === '결석' ? 'selected' : ''}>결석</option>
        <option value="지각" ${student.attendanceStatus === '지각' ? 'selected' : ''}>지각</option>
      </select>
    `;
    attendanceList.appendChild(studentRow);
  });

  // 출석 상태 저장 버튼을 보이도록 설정
  document.getElementById('save-attendance').style.display = 'block';
}

// 출석 상태 저장
document.getElementById('save-attendance').addEventListener('click', async () => {
  const selects = document.querySelectorAll('#attendance-list select');
  const updatedData = Array.from(selects).map(select => ({
    studentId: select.getAttribute('data-student-id'),
    attendanceStatus: select.value
  }));

  const response = await fetch('/Attendance/save', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(updatedData)
  });

  if (response.ok) {
    alert('출석 상태가 저장되었습니다.');
  } else {
    alert('저장에 실패했습니다.');
  }
});

// 달력 렌더링 초기화
renderCalendar();