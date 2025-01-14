document.addEventListener('DOMContentLoaded', function() {
    const categoryButtons = document.querySelectorAll('.category-btn');
    categoryButtons.forEach(button => {
        button.addEventListener('click', function() {
            // 기존 선택 제거
            categoryButtons.forEach(btn => {
                btn.classList.remove('selected', 'text1');
                btn.classList.add('text2');
            });
            // 새로운 선택 추가
            this.classList.remove('text2');
            this.classList.add('selected', 'text1');
            // 선택된 카테고리 저장
            document.getElementById('selectedCategory').value = this.dataset.category;
        });
    });

    // 폼 제출 전 유효성 검사
    document.getElementById('outcomeForm').addEventListener('submit', function(e) {
        const selectedCategory = document.getElementById('selectedCategory').value;
        if (!selectedCategory) {
            e.preventDefault();
            alert('카테고리를 선택해주세요.');
            return false;
        }
        return true;
    });
});