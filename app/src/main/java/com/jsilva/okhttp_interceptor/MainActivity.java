package com.jsilva.okhttp_interceptor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * OkHttp client to perform HTTP requests
     */
    private OkHttpClient okHttpClient;

    /**
     * WebView to present the result
     */
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setOnClickListeners();
    }

    /**
     * Defines the click listeners and respective behaviour.
     * "Go" button will inflate the UI WebView with the result of the Http response.
     * Interceptor switch will redefine the OkHttp client to enable/disable the interceptor.
     */
    private void setOnClickListeners() {
        Switch aSwitch = (Switch) findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new
                            InterceptRequest()).build();
                } else {
                    okHttpClient = new OkHttpClient().newBuilder().build();
                }
            }
        });

        Button aButton = (Button) findViewById(R.id.button);
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView = (WebView) findViewById(R.id.webView);
                webView.setWebViewClient(createOkhttpClient());
                System.out.println("LOADING ");
                webView.loadUrl("https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_UY268_CR1,0,182,268_AL_.jpg");
            }
        });
    }

    /**
     * Creates a WebViewClient that uses OkhttpClient to perform requests.
     *
     * @return WebViewClient with an OkhttpClient
     */
    private WebViewClient createOkhttpClient() {
        System.out.println("CREATE CLIENT");
        return new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                System.out.println("NEW RESOURCE RESPONSE");
                Request okHttpRequest = new Request.Builder().url(url).cacheControl(
                        CacheControl.FORCE_NETWORK).build();
                try {
                    Response response = okHttpClient.newCall(okHttpRequest).execute();
                    WebResourceResponse webResource = new WebResourceResponse("", "", response.body().byteStream());
                    return webResource;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
