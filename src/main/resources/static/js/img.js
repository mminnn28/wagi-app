document.getElementById('imageFile').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const previewDiv = document.getElementById('imagePreview');
            // 기존 이미지가 있다면 제거
            previewDiv.innerHTML = '';
            // 새 이미지 추가
            const img = document.createElement('img');
            img.src = e.target.result;
            img.style.maxWidth = '200px';
            img.alt = 'Preview Image';
            previewDiv.appendChild(img);
        }
        reader.readAsDataURL(file);
    }
});
