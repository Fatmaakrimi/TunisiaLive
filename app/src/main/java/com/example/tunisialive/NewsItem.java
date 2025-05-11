package com.example.tunisialive;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Root(name = "item", strict = false)
public class NewsItem {

    @Element(name = "title")
    private String title = "";

    @Element(name = "link")
    private String link;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "pubDate", required = false)
    private String pubDate;

    @Element(name = "category", required = false)
    private String category;

    @Element(name = "enclosure", required = false)
    private Enclosure enclosure;

    @ElementList(entry = "content", inline = true, required = false)
    private List<MediaContent> mediaContentList;

    @Element(name = "thumbnail", required = false)
    private MediaThumbnail mediaThumbnail;

    @Namespace(reference = "http://purl.org/rss/1.0/modules/content/")
    @Element(name = "encoded", required = false)
    private String contentEncoded;

    // --- GETTERS ---

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Date getPubDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        try {
            return format.parse(pubDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }

    public String getCategory() {
        return category;
    }

    public String getContentEncoded() {
        return contentEncoded;
    }

    public String getImageUrlFromDescription() {
        if (description == null) return null;

        Pattern pattern = Pattern.compile("<img[^>]+src=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public String getImageUrl() {
        if (enclosure != null) {
            return enclosure.getUrl();
        } else if (mediaThumbnail != null) {
            return mediaThumbnail.getUrl();
        } else if (mediaContentList != null && !mediaContentList.isEmpty()) {
            return mediaContentList.get(0).getUrl();
        }
        return null;
    }

    public boolean isInCategory(String cat) {
        if (category == null || cat == null) return false;
        return category.equalsIgnoreCase(cat);
    }

    // --- SETTERS (used when needed manually) ---

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public void setMediaContentList(List<MediaContent> mediaContentList) {
        this.mediaContentList = mediaContentList;
    }

    public void setMediaThumbnail(MediaThumbnail mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }

    public void setContentEncoded(String contentEncoded) {
        this.contentEncoded = contentEncoded;
    }
}
