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