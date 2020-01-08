package edu.fje.dam2.abel.sopadelletres;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class web extends AppCompatActivity {

    WebView navegador;

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.activity_web);
        navegador = (WebView) findViewById(R.id.webkit);

        /*crea un directori a l'arrel al mateix nivell que res anomenat assets,
        i dins col·loca les pàgines que es vulguin colocar dins d'aplicació*/

        navegador.getSettings().setJavaScriptEnabled(true);

        //per geolocalització de HTML5
        navegador.getSettings().setAppCacheEnabled(true);
        navegador.getSettings().setDatabaseEnabled(true);
        navegador.getSettings().setDomStorageEnabled(true);
        navegador.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        //per defecte ens obrirà Chrome, cal canviar-ho
        navegador.setWebViewClient(new WebViewClient());
        //navegador.loadUrl("http://www.fje.edu/");
        //navegador.loadData("<html><body>hola, mon!</body></html>", "text/html", "UTF-8");
       /* navegador.loadData("<html><body><input type=\"button\" value=\"Hola\" onClick=\"mostrarToast('Hola Android!')\" />\n" +
                "<script type=\"text/javascript\">\n" +
                "function mostrarToast(toast) {\n" +
                "Android.mostrarToast(toast);\n" +
                "}\n" +
                "</script></body></html>", "text/html", "UTF-8");
       navegador.addJavascriptInterface (new WebAppInterface (this), "Android");*/
        navegador.loadUrl("file:///android_asset/ayuda.html");

    }

    class WebAppInterface {
        Context mContext;
        /** instancia la interfície i l'associa el context */
        WebAppInterface(Context c) {
            mContext = c;
        }
        /** mostra una torrada */
        @JavascriptInterface
        public void mostrarToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
