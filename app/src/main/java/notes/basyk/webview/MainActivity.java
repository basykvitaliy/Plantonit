package notes.basyk.webview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    private static final String BASE_URL = "https://plantonit.ua/";
    private ProgressBar progressBarWeb;
    private ProgressDialog progressDialog;
    private RelativeLayout relativeLayout;
    private Button buttonNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        progressBarWeb = findViewById(R.id.progressBar);
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Загрузка. Пожалуйста подождите.");

        buttonNoInternet = findViewById(R.id.bottomNoCon);
        relativeLayout = findViewById(R.id.relativeLayout);

        webView = findViewById(R.id.myWebView);
        checkConnection();

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            progressBarWeb.setVisibility(View.VISIBLE);
            progressBarWeb.setProgress(newProgress);
            setTitle("Загрузка...");
            //progressDialog.show();
            if (newProgress == 100){
                progressBarWeb.setVisibility(View.GONE);
                setTitle(view.getTitle());
                //progressDialog.dismiss();
            }

                super.onProgressChanged(view, newProgress);
            }
        });

        buttonNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.back:
                onBackPressed();
                break;
                case R.id.go:
                    if (webView.canGoForward()){
                        webView.goForward();
                    }
                    break;
            case R.id.reload:
                checkConnection();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi.isConnected()){
            webView.loadUrl(BASE_URL);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

        }else if (mobileNetwork.isConnected()){
            webView.loadUrl(BASE_URL);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }else {
            webView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }
}
