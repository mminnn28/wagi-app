document.addEventListener('DOMContentLoaded', function () {
    const hamImages1 = ['static/img/커햄1.jpg', 'static/img/커햄2.jpg', 'static/img/커햄3.jpg'];
    const hamImages2 = ['static/img/커햄1.jpg', 'static/img/커햄2.jpg', 'static/img/커햄3.jpg'];
    const hamImages3 = ['static/img/커햄1.jpg', 'static/img/커햄2.jpg', 'static/img/커햄3.jpg'];

    let imageNum1 = 0;
    let imageNum2 = 0;
    let imageNum3 = 0;

    // 첫 번째 섹션
    document.getElementById('next-button1').addEventListener('click', function () {
        imageNum1 = (imageNum1 + 1) % hamImages1.length;
        document.getElementById('ham-image1').src = hamImages1[imageNum1];
    });
    
    document.getElementById('back-button1').addEventListener('click', function () {
        imageNum1 = (imageNum1 - 1 + hamImages1.length) % hamImages1.length;
        document.getElementById('ham-image1').src = hamImages1[imageNum1];
    });

    // 두 번째 섹션
    document.getElementById('next-button2').addEventListener('click', function () {
        imageNum2 = (imageNum2 + 1) % hamImages2.length;
        document.getElementById('ham-image2').src = hamImages2[imageNum2];
    });
    
    document.getElementById('back-button2').addEventListener('click', function () {
        imageNum2 = (imageNum2 - 1 + hamImages2.length) % hamImages2.length;
        document.getElementById('ham-image2').src = hamImages2[imageNum2];
    });

    // 세 번째 섹션
    document.getElementById('next-button3').addEventListener('click', function () {
        imageNum3 = (imageNum3 + 1) % hamImages3.length;
        document.getElementById('ham-image3').src = hamImages3[imageNum3];
    });
    
    document.getElementById('back-button3').addEventListener('click', function () {
        imageNum3 = (imageNum3 - 1 + hamImages3.length) % hamImages3.length;
        document.getElementById('ham-image3').src = hamImages3[imageNum3];
    });
});