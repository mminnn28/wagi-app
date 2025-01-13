function toggleEdit() {
    const editForm = document.querySelector('.edit-form');
    const editBtn = document.querySelector('.edit-btn');
    const saveBtn = document.querySelector('.save-btn');
    const titleInput = document.querySelector('.text2');
    const textbox = document.querySelector('.textbox');

    if (editForm.style.display === 'none') {
        editForm.style.display = 'block';
        editBtn.style.display = 'none';
        saveBtn.style.display = 'inline-block';
        titleInput.removeAttribute('readonly');
        textbox.removeAttribute('readonly');
    } else {
        editForm.style.display = 'none';
        editBtn.style.display = 'inline-block';
        saveBtn.style.display = 'none';
        titleInput.setAttribute('readonly', true);
        textbox.setAttribute('readonly', true);
    }
}

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
