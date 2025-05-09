package com.example.tunisialive;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RssWorker extends Worker {

    private static final String RSS_URL = "https://exemple.com/rss"; // Remplace par ton flux
    private static final String PREFS_NAME = "RSS_PREFS";
    private static final String LAST_ARTICLE_TITLE = "last_title";

    public RssWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            URL url = new URL(RSS_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(inputStream, "UTF_8");

            boolean insideItem = false;
            String title = null;
            String link = null;

            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (insideItem && xpp.getName().equalsIgnoreCase("title")) {
                        title = xpp.nextText();
                    } else if (insideItem && xpp.getName().equalsIgnoreCase("link")) {
                        link = xpp.nextText();
                    }
                } else if (xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideItem = false;

                    // Vérifier si c’est un nouvel article
                    SharedPreferences prefs = getApplicationContext()
                            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    String lastTitle = prefs.getString(LAST_ARTICLE_TITLE, "");

                    if (!title.equals(lastTitle)) {
                        prefs.edit().putString(LAST_ARTICLE_TITLE, title).apply();

                        NotificationHelper.sendNotification(getApplicationContext(), title, link);
                    }

                    break; // On ne prend que le premier article
                }
            }

            inputStream.close();
            return Result.success();

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();

        }
    }
}
