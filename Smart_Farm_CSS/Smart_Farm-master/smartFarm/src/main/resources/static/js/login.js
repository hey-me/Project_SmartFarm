$(document).ready(function() {
	//로그인페이지 엔터 누트면 로그인버튼 누르기
	$('#loginForm').keypress(function(event) {
		if (event.which == 13) {
			actionLoginCheck();
			return false;
		}
	})
});
	//로그인 ajax
	function actionLoginCheck() {
		if ($('#user_id').val() == '') {
			Swal.fire({
				icon: 'error',
				title: '',
				text: '아이디를 입력하세요.',
			})
			return false;
		}

		if ($('#user_password').val() == '') {
			Swal.fire({
				icon: 'error',
				title: '',
				text: '비밀번호를 입력하세요.',
			})
			return false;
		}
		$("#loginForm").submit();


	}
