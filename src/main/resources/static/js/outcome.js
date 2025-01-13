// outcome.js
document.addEventListener('DOMContentLoaded', function() {
    // 각 카테고리별 슬라이더 초기화
    initializeSlider('web');
    initializeSlider('app');
    initializeSlider('game');
});

function initializeSlider(category) {
    const images = document.querySelectorAll(`.outcome-images[data-category="${category}"] > div`);
    if (images.length === 0) return;

    let currentIndex = 0;

    // 첫 번째 이미지만 보이도록 설정
    updateImageVisibility();

    // 이전 버튼 이벤트
    document.getElementById(`back-button${category === 'web' ? '1' : category === 'app' ? '2' : '3'}`).addEventListener('click', () => {
        currentIndex = (currentIndex - 1 + images.length) % images.length;
        updateImageVisibility();
    });

    // 다음 버튼 이벤트
    document.getElementById(`next-button${category === 'web' ? '1' : category === 'app' ? '2' : '3'}`).addEventListener('click', () => {
        currentIndex = (currentIndex + 1) % images.length;
        updateImageVisibility();
    });

    function updateImageVisibility() {
        images.forEach((image, index) => {
            image.style.display = index === currentIndex ? 'block' : 'none';
        });
    }
}