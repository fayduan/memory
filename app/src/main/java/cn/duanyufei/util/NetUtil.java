package cn.duanyufei.util;

/**
 * Created by fayduan on 2018/1/4.
 */
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class NetUtil {

    public static final String TAG = NetUtil.class.getName();

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json");
    private static NetUtil mInstance = null;
    //获取UI线程
    private Handler mHandler = new Handler(Looper.getMainLooper());

    //回调接口
    public interface MOkCallBack {
        void onSuccess(String rep);
        void onError();
    }

    //获取实例
    public static NetUtil getInstance() {
        if (mInstance == null) {
            synchronized (NetUtil.class) {
                if (mInstance == null) {
                    mInstance = new NetUtil();
/*                    try {
                        mInstance.setCertificates(MApplication.getContext().getAssets().open("server.crt"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mInstance.mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                    mInstance.mOkHttpClient.setCookieHandler(new CookieManager(
                            new PersistentCookieStore(ISeeYouApplication.getContext()), CookiePolicy.ACCEPT_ALL
                    ));*/
                }
            }
        }
        return mInstance;
    }

    public void get(String url, final MOkCallBack mCallBack) {
        get(mOkHttpClient, url, mCallBack);
    }

    // Get 请求
    private void get(OkHttpClient ohc, String url, final MOkCallBack mCallBack) {
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "*/*")
                .build();
        Call call = ohc.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onError();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String re = response.body().string();
                mHandler.post(new Runnable() {
                    public void run() {
                        mCallBack.onSuccess(re);
                    }
                });
            }
        });
    }

    public void post(String url, MOkCallBack mCallBack) {
        post(url, mCallBack);
    }

    public void post(String url, String json, final MOkCallBack mCallBack) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (json != null && json.length() > 0) {
            builder.post(RequestBody.create(JSON, json));
        }
        Request request = builder.build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onError();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String re = response.body().string();
                mHandler.post(new Runnable() {
                    public void run() {
                        mCallBack.onSuccess(re);
                    }
                });
            }
        });
    }

    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}