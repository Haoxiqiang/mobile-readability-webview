//
//  ViewController.swift
//  ios-swift
//
//  Created by haoxiqiang on 2022/3/30.
//

import UIKit
import WebKit

class ViewController: UIViewController {
    
    var webView: WKWebView!

    override func loadView() {
        let config = WKWebViewConfiguration()
        config.preferences.setValue(true, forKey: "allowFileAccessFromFileURLs")
        webView = WKWebView(frame: .zero, configuration: config)
        webView.navigationDelegate = self
        webView.configuration.defaultWebpagePreferences.allowsContentJavaScript = true
        view = webView
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        title = "WKWebView"

        // Add addScriptMessageHandler in javascript: window.webkit.messageHandlers.MyObserver.postMessage()
        webView.configuration.userContentController.add(self, name: "MyObserver")

        if let filePath = Bundle.main.path(forResource: "readerview", ofType: "html", inDirectory: "dist/readerview") {
            let filePathURL = URL.init(fileURLWithPath: filePath)
            let fileDirectoryURL = filePathURL.deletingLastPathComponent()

            var urlComponents = URLComponents(string: filePathURL.absoluteString)!
            urlComponents.queryItems = [
                URLQueryItem(name: "ref", value: "https://www.zhihu.com/question/47819047/answer/108130984")
            ]
            print(urlComponents.url!)

            webView.loadFileURL(urlComponents.url!, allowingReadAccessTo: fileDirectoryURL)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

extension ViewController: WKScriptMessageHandler {
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        // Callback from javascript: window.webkit.messageHandlers.MyObserver.postMessage(message)
        let text = message.body as! String
        let alertController = UIAlertController(title: "Javascript said:", message: text, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "OK", style: .default) { (_) in
            print("OK")
        }
        alertController.addAction(okAction)
        present(alertController, animated: true, completion: nil)
    }
}

extension ViewController: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        print("didFinish navigation:")
    }
}

