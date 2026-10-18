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
        getWindow().setStatusBarColor(Color.rgb(8, 10, 20));
        getWindow().setNavigationBarColor(Color.rgb(8, 10, 20));
        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(8, 10, 20));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(false);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setSupportMultipleWindows(false);
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface public void setEasterEggActive(boolean active) {
                runOnUiThread(() -> updateSecretFullscreen(active));
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

    // Deliberately not named setImmersive: Android Activity already defines that method.
    private void updateSecretFullscreen(boolean active) {
        View decor = getWindow().getDecorView();
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
        updateSecretFullscreen(false);
        if (webView != null) { webView.destroy(); webView = null; }
        super.onDestroy();
    }
}
