function renderReadabilityToggle() {
    initReadabilityPage();
    var cn = document.body.className;
    if (!cn) {
        cn = "";
    }
    if (cn.indexOf('reader-enabled') == -1) {
        document.body.className += " reader-enabled";
    } else {
        document.body.className = cn.replace(' reader-enabled', '');
    }
};

function removeAllNodes(dc) {
    /* style="widh:100px" */
    dc.querySelectorAll('[style]')
        .forEach(el => el.removeAttribute('style'));

    /*  <style></style> */
    dc.querySelectorAll('style')
      .forEach(el => el.parentNode.removeChild(el));

    /*  <link */
    dc.querySelectorAll('link')
      .forEach(el => el.parentNode.removeChild(el));

    /*  <script */
    dc.querySelectorAll('script')
      .forEach(el => { if(el.id != 'readability-script') {el.parentNode.removeChild(el)} });
    /*  meta */
    dc.querySelectorAll('meta')
        .forEach(el => el.parentNode.removeChild(el));
};

function removeBlackElements(dc){
    var host = document.location.host;
    if(host) {
        if(host.search("36kr") > 0){
            var tipsElement = dc.getElementById("tips-content");
            if(tipsElement){
                tipsElement.parentNode.removeChild(tipsElement);
            }
        }
    }
};
