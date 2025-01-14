document.addEventListener('DOMContentLoaded', function() {
    // 카테고리 버튼 처리
    const categoryButtons = document.querySelectorAll('.category-btn');
    categoryButtons.forEach(button => {
        button.addEventListener('click', function() {
            // 기존 선택 제거
            categoryButtons.forEach(btn => btn.classList.remove('selected'));
            // 새로운 선택 추가
            this.classList.add('selected');
            // 선택된 카테고리 저장
            document.getElementById('selectedCategory').value = this.dataset.category;
        });
    });

    // 이미지 파일 처리
    const imageFile = document.getElementById('imageFile');
    imageFile.addEventListener('change', function() {
        if (this.files && this.files[0]) {
            const fileName = this.files[0].name;
            document.getElementById('fileName').textContent = fileName;
        }
    });

    // 폼 제출 전 유효성 검사
    document.getElementById('outcomeForm').addEventListener('submit', function(e) {
        e.preventDefault();
        if (!document.getElementById('selectedCategory').value) {
            alert('카테고리를 선택해주세요.');
            return;
        }
        this.submit();
    });
});