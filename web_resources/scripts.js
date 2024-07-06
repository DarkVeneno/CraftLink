function performSearch() {
    const query = document.getElementById('search-input').value;
    const engine = document.getElementById('search-engine').value;
    const url = engine + encodeURIComponent(query);
    window.open(url, '_blank');
}
