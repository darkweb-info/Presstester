package com.presstester.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.graphics.Color;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(10, 14, 28));
        getWindow().setNavigationBarColor(Color.rgb(7, 9, 16));
        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(10, 14, 28));
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(false);
        s.setAllowFileAccess(true); // Only bundled local HTML/assets.
        s.setAllowContentAccess(false);
        s.setAllowFileAccessFromFileURLs(false);
        s.setAllowUniversalAccessFromFileURLs(false);
        s.setMediaPlaybackRequiresUserGesture(false); // Play offline sound on 67.
        s.setSupportMultipleWindows(false);
        // Only local, bundled assets are loaded. Let the 67 animation take over
        // the entire screen, including system bars, and restore them on exit.
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface public void setEasterEggActive(boolean active) {
                runOnUiThread(() -> setImmersive(active));
            }
        }, "PresstesterBridge");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !"file".equalsIgnoreCase(request.getUrl().getScheme());
            }
        });
        setContentView(webView);
        webView.loadUrl("file:///android_asset/calculator.html");
    }

public void setImmersive(boolean active) {        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(active
            ? (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
               | View.SYSTEM_UI_FLAG_FULLSCREEN
               | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
               | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
               | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
               | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            : View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override public void onBackPressed() {
        if (webView == null) { super.onBackPressed(); return; }
        webView.evaluateJavascript("(function(){var e=document.getElementById('easterEgg');if(e&&!e.hidden&&window.closeEasterEgg){window.closeEasterEgg();return true;}return false;})()", result -> {
            if (!"true".equals(result)) {
                if (webView.canGoBack()) webView.goBack();
                else finish();
            }
        });
    }

    @Override protected void onDestroy() {
        setImmersive(false);
        if (webView != null) { webView.destroy(); webView = null; }
        super.onDestroy();
    }
}
