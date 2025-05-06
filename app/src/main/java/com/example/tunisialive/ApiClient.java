package com.example.tunisialive;

import java.security.cert.CertificateException;
import java.util.Collections;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://dummy.com/"; // Base fictive pour Retrofit

    public static Retrofit getRetrofitInstance() {
        OkHttpClient client = getSecureOkHttpClient();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL) // Retrofit exige une base URL même si on utilise @Url
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create()) // Version classique
                .build();
    }

    private static OkHttpClient getSecureOkHttpClient() {
        try {
            // TrustManager qui ignore toutes les erreurs SSL
            final TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Création du contexte SSL pour ignorer les erreurs de certificats
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Activation des protocoles TLS 1.2 et TLS 1.3 pour éviter "Handshake failed"
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
                    .build();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCertificates[0])
                    .hostnameVerifier((hostname, session) -> true) // Ignore les erreurs de hostname SSL
                    .connectionSpecs(Collections.singletonList(spec)) // Active TLS 1.2 et 1.3
                    .addInterceptor(chain -> {
                        Request request = chain.request();
                        Response response = chain.proceed(request);

                        // Lire la réponse en texte brut pour éviter les erreurs XML mal formées
                        String rawXml = response.body().string();
                        rawXml = rawXml.trim().replaceFirst("^([\\W]+)<", "<"); // Supprime les caractères non valides avant <?xml
                        ResponseBody newBody = ResponseBody.create(response.body().contentType(), rawXml);

                        return response.newBuilder().body(newBody).build();
                    })
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Safari/537.36")
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
