package com.ncgtelevision.net.playback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ncgtelevision.net.R;
import com.ncgtelevision.net.playback.model.Datum;
import com.ncgtelevision.net.signin.SignInActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;

public class IframePlaybackActivity extends FragmentActivity {
    WebView mWebView;
    String TAG="IframePlaybackActivity";
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iframe_playback);
        mWebView = (WebView) findViewById(R.id.webview);
        dialog = custom_loader(IframePlaybackActivity.this);
        Log.i(TAG, "onCreate: IframePlaybackActivity");
        loadIframe();
    }

    private void loadIframe() {
        String video;

//            Bundle bundle = getIntent().getExtras();
//              video = bundle.getString("VIDEO_ID");
//                Log.d("TAG", "loadIframe: " + video);


//        Bundle bundle = getIntent().getExtras();
//
//        String video;
        Intent intent = getIntent();
        video=   intent.getStringExtra("VIDEO_ID");
        Log.d("TAG", "loadIframe: " + video);
        Log.d("TAG", "loadIframe: " + video.matches("source"));

        if(video.startsWith("<script"))
        {
            Log.i(TAG, "loadIframe: script true");
            video=video.replace("false","true");
//          Datum datum = (com.ncgtelevision.net.playback.model.Datum) bundle.getParcelable("playbackData");
//            video=datum.getIframe();

            mWebView.setInitialScale(1);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
//        video = video.replace(" allowfullscreen", " allowfullscreen allow=\"autoplay; fullscreen; picture-in-picture; xr-spatial-tracking; encrypted-media\""); //"<iframe style=\"border:none;\" style=\"width:1332px; height:680px;\" allowfullscreen allow=\"autoplay; fullscreen; picture-in-picture; xr-spatial-tracking; encrypted-media\" src=\"https://framtetv.online/frametv/nova/embed.html\"></iframe>";
//<iframe style=\"background:black;\" width=' "+width+"' height='"+height+"' src=\""+VIDEO_URL+"\" frameborder=\"0\"></iframe>


//        String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, height=device-height, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> " + video+ " </body> </html> ";


//        mWebView.loadData(data_html, "text/html; charset=UTF-8", "UTF-8"     );
            dialog.show();
            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.i(TAG, "Processing webview url click...");
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    Log.i(TAG, "Finished loading URL: " + url);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e(TAG, "Error: " + description);

                }
            });

            Log.i(TAG, "loadIframe: change element "+video);

            mWebView.loadDataWithBaseURL(null, video , "text/html",  "UTF-8", null);

        }
        else if(video.startsWith("<iframe") || video.startsWith("<div") )
        {
            Log.i(TAG, "loadIframe: iframe true");
            dialog.show();
            String str = video.toString();

            Pattern pattern = Pattern.compile("src=\"(.*?)\"", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                System.out.println(matcher.group(1));
                video=matcher.group(1);
            }

//          Datum datum = (com.ncgtelevision.net.playback.model.Datum) bundle.getParcelable("playbackData");
//            video=datum.getIframe();

            mWebView.setInitialScale(1);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            Log.i(TAG, "loadIframe: height "+height);
            Log.i(TAG, "loadIframe:  width "+width);


//        video = video.replace(" allowfullscreen", "allowfullscreen allow=\"autoplay; fullscreen; picture-in-picture; xr-spatial-tracking; encrypted-media\""); //"<iframe style=\"border:none;\" style=\"width:1332px; height:680px;\" allowfullscreen allow=\"autoplay; fullscreen; picture-in-picture; xr-spatial-tracking; encrypted-media\" src=\"https://framtetv.online/frametv/nova/embed.html\"></iframe>";
          String mVideo="<iframe allow=\"autoplay\" width=\"100%\" height=\"100%\" src=\""+video+"\"></iframe>";


//        String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, height=device-height, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> " + video+ " </body> </html> ";


//        mWebView.loadData(data_html, "text/html; charset=UTF-8", "UTF-8"     );
//            video="<iframe src=\"https://www.tvdream.net/embed/telemia/\" width=' \"+width+\"' height='\"+height+\"' title=\"TeleMia player video\" allowfullscreen=\"\" __idm_frm__=\"1179\" __idm_id__=\"1023288321\"></iframe>"

            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.i(TAG, "Processing webview url click...");
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    Log.i(TAG, "Finished loading URL: " + url);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e(TAG, "Error: " + description);

                }
            });

            Log.i(TAG, "loadIframe: change element "+video);

            mWebView.loadDataWithBaseURL(null, mVideo , "text/html",  "UTF-8", null);

        }
        else
        {
            Log.i(TAG, "loadIframe: script else");
            video=video.replace("false","true");
//          Datum datum = (com.ncgtelevision.net.playback.model.Datum) bundle.getParcelable("playbackData");
//            video=datum.getIframe();

            mWebView.setInitialScale(1);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
//        video = video.replace(" allowfullscreen", " allowfullscreen allow=\"autoplay; fullscreen; picture-in-picture; xr-spatial-tracking; encrypted-media\""); //"<iframe style=\"border:none;\" style=\"width:1332px; height:680px;\" allowfullscreen allow=\"autoplay; fullscreen; picture-in-picture; xr-spatial-tracking; encrypted-media\" src=\"https://framtetv.online/frametv/nova/embed.html\"></iframe>";
//<iframe style=\"background:black;\" width=' "+width+"' height='"+height+"' src=\""+VIDEO_URL+"\" frameborder=\"0\"></iframe>


//        String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, height=device-height, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> " + video+ " </body> </html> ";


//        mWebView.loadData(data_html, "text/html; charset=UTF-8", "UTF-8"     );
            dialog.show();
            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.i(TAG, "Processing webview url click...");
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    Log.i(TAG, "Finished loading URL: " + url);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e(TAG, "Error: " + description);

                }
            });

            Log.i(TAG, "loadIframe: change element "+video);

            mWebView.loadDataWithBaseURL(null, video , "text/html",  "UTF-8", null);
        }



    }

}