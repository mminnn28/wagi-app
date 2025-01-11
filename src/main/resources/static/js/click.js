const buttons = document.querySelectorAll(".text1");

function handleClick(event) {
  // 모든 버튼에서 'selected' 클래스를 제거
  buttons.forEach(button => {
    button.classList.remove("selected");
  });
  
  // 클릭된 버튼에 'selected' 클래스 추가
  event.target.classList.add("selected");
}

// 각 버튼에 클릭 이벤트 리스너 추가
buttons.forEach(button => {
  button.addEventListener("click", handleClick);
});
