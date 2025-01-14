document.querySelectorAll('.authority').forEach(button => {
    button.addEventListener('click', () => {
        const studentId = button.getAttribute('data-id');
        const currentRole = button.getAttribute('data-role');

        // 순환적 권한 변경: USER -> MANAGER -> ADMIN -> USER
        const newRole = currentRole === 'USER'
            ? 'MANAGER'
            : (currentRole === 'MANAGER' ? 'ADMIN' : 'USER');

        if (newRole !== currentRole) {
            // 서버로 데이터 전송
            fetch('/admin/updateRole', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    studentId: studentId,
                    role: newRole
                })
            })
            .then(response => {
                if (response.ok) {
                    button.setAttribute('data-role', newRole);
                    button.textContent = newRole === 'USER'
                        ? '일반'
                        : (newRole === 'MANAGER' ? '운영진' : '회장단');
                } else {
                    alert('권한 변경 실패.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버 요청 중 오류가 발생했습니다.');
            });
        }
    });
});