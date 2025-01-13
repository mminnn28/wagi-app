document.addEventListener('DOMContentLoaded', function() {
    const imageFile = document.getElementById('imageFile');
    imageFile.addEventListener('change', function() {
        if (this.files && this.files[0]) {
            const fileName = this.files[0].name;
            document.getElementById('fileName').textContent = fileName;
        }
    });
});