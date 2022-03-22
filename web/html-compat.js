function applyImg() {
    let imgs = document.getElementsByTagName('img');
    for (var i = 0; i < imgs.length; i++) {
        let img = imgs[i];
        let currentSrc = imgs[i].getAttribute('src');
        if (!currentSrc) {
            let currentDataSrc = imgs[i].getAttribute('data-src');
            img.setAttribute('src', currentDataSrc);
        }
    }
}

window.applyCompat = function applyCompat() {
    applyImg();
}
