package com.example.tunisialive;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends ViewModel {
    public static final String[] RSS_SOURCES = {
            "https://kapitalis.com/tunisie/feed/",
            "https://brands.com.tn/feed/",
            "https://www.mosaiquefm.net/ar/rss",
            "https://lapresse.tn/feed/",
            "https://ar.africanmanager.com/feed/",
            "https://ar.leaders.com.tn/rss",
            "https://www.tuniscope.com/feed",
            "https://www.jawharafm.net/ar/rss/showRss/88/1/1",
            "https://www.alchourouk.com/rss",
            "https://hakaekonline.com/feed/",
            "https://www.businessnews.com.tn/rss.xml",
            "https://www.babnet.net/feed.php",
            "https://www.arrakmia.com/feed-actualites-tunisie.xml",
            "https://www.arabesque.tn/ar/rss",
            "https://radioexpressfm.com/ar/feed/",
            "http://ar.webmanagercenter.com/feed/",
            "https://rassdtunisia.net/feed/"
    };


    private final MutableLiveData<List<NewsItem>> newsLiveData = new MutableLiveData<>();
    private final List<NewsItem> allNewsList = new ArrayList<>();
    private int sourcesProcessed = 0;

    private ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public LiveData<List<NewsItem>> getNews() {
        for (String source : RSS_SOURCES) {
            fetchNewsFromSource(source);
        }
        return newsLiveData;
    }

    private void fetchNewsFromSource(String baseUrl) {
        apiService.getNews(baseUrl).enqueue(new Callback<RSSFeed>() {
            @Override
            public void onResponse(Call<RSSFeed> call, Response<RSSFeed> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (NewsItem item : response.body().getChannel().getNewsItems()) {
                        String category = guessCategory(item.getTitle(), item.getDescription());
                        item.setCategory(category);
                        allNewsList.add(item);
                    }
                }

                sourcesProcessed++;
                if (sourcesProcessed == RSS_SOURCES.length) {
                    Collections.sort(allNewsList, (a, b) -> b.getPubDate().compareTo(a.getPubDate()));
                    newsLiveData.setValue(allNewsList);
                }
            }

            @Override
            public void onFailure(Call<RSSFeed> call, Throwable t) {
                Log.e("DEBUG_RSS", "Erreur sur " + baseUrl + ": " + t.getMessage());
                sourcesProcessed++;
                if (sourcesProcessed == RSS_SOURCES.length) {
                    newsLiveData.setValue(allNewsList);
                }
            }
        });
    }

    private String guessCategory(String title, String description) {
        String text = (title + " " + description).toLowerCase();

        if (text.contains("sport")) return "Sport";
        if (text.contains("politique") || text.contains("gouvernement")) return "Politique";
        if (text.contains("économie") || text.contains("financ")) return "Économie";
        if (text.contains("culture") || text.contains("festival")) return "Culture";
        if (text.contains("santé") || text.contains("covid") || text.contains("hôpital")) return "Santé";

        return "Autres";
    }

    public LiveData<List<NewsItem>> getNewsByCategory(String category) {
        MutableLiveData<List<NewsItem>> filteredLiveData = new MutableLiveData<>();
        List<NewsItem> filtered = new ArrayList<>();

        for (NewsItem item : allNewsList) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                filtered.add(item);
            }
        }

        filteredLiveData.setValue(filtered);
        return filteredLiveData;
    }

    public void loadNews() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RSS_PREFS", Context.MODE_PRIVATE);
        Set<String> selectedSources = sharedPreferences.getStringSet("SELECTED_RSS", new HashSet<>());

        allNewsList.clear();
        sourcesProcessed = 0;

        for (String url : selectedSources) {
            apiService.getNews(url).enqueue(new Callback<RSSFeed>() {
                @Override
                public void onResponse(Call<RSSFeed> call, Response<RSSFeed> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (NewsItem item : response.body().getChannel().getNewsItems()) {
                            String category = guessCategory(item.getTitle(), item.getDescription());
                            item.setCategory(category);
                            allNewsList.add(item);
                        }
                    }

                    sourcesProcessed++;
                    if (sourcesProcessed == selectedSources.size()) {
                        Collections.sort(allNewsList, (a, b) -> b.getPubDate().compareTo(a.getPubDate()));
                        newsLiveData.setValue(allNewsList);
                    }
                }

                @Override
                public void onFailure(Call<RSSFeed> call, Throwable t) {
                    sourcesProcessed++;
                }
            });
        }
    }
}
