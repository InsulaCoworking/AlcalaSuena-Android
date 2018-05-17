package com.triskelapps.alcalasuena.model;

public class FirebasePush {

    public static final String KEY_NEWS_ID = "id_news";

    private String to;
    private FirebaseNotification notification;
    private FirebaseData data;

    public static class FirebaseNotification{
        private String title;
        private String body;

        public FirebaseNotification(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }

    public class FirebaseData{
        private String id_news;

        public String getId_news() {
            return id_news;
        }

        public void setId_news(String id_news) {
            this.id_news = id_news;
        }
    }

    public void setIdNewsData(String idNews) {
        FirebaseData firebaseData = new FirebaseData();
        firebaseData.setId_news(idNews);
        setData(firebaseData);
    }
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public FirebaseNotification getNotification() {
        return notification;
    }

    public void setNotification(FirebaseNotification notification) {
        this.notification = notification;
    }

    public FirebaseData getData() {
        return data;
    }

    public void setData(FirebaseData data) {
        this.data = data;
    }
}
