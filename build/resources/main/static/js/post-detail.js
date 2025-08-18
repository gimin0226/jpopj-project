async function deletePost(id) {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    const token  = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

    const res = await fetch(`/posts/${id}`, {
        method: 'DELETE',
        headers: token ? { [header]: token, 'X-Requested-With': 'XMLHttpRequest' } : { 'X-Requested-With': 'XMLHttpRequest' }
    });

    if (res.ok) {
        window.location.assign('/');            // ← 여기서 직접 이동
    } else {
        alert('삭제에 실패했습니다.');
    }
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function () {
            const commentId = this.getAttribute("data-comment-id");
            const postId = this.getAttribute("data-post-id");
            if (!confirm("정말 이 댓글을 삭제하시겠습니까?")) return;

            fetch(`/posts/${postId}/comments/${commentId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert("삭제되었습니다.");
                        // 댓글 DOM 삭제
                        this.closest(".comment-item").remove();
                    } else {
                        alert("삭제 실패: " + response.status);
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    alert("오류가 발생했습니다.");
                });
        });
    });
});