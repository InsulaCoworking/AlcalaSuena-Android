package com.triskelapps.alcalasuena.interactor;

import android.content.Context;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.api.Api;
import com.triskelapps.alcalasuena.base.BaseInteractor;
import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.Band;
import com.triskelapps.alcalasuena.model.Tag;
import com.triskelapps.alcalasuena.util.Util;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 14/02/16.
 */
public class BandInteractor extends BaseInteractor {



    public interface BandsCallback {

        void onResponse(List<Band> bands);

        void onError(String error);
    }

    public BandInteractor(Context context, BaseView baseView) {
        this.baseView = baseView;
        this.context = context;

    }

    public static void initializeBands() {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                try {
                    Tag tag1 = realm.createObject(Tag.class, 1);
                    tag1.setName("Jazz");

                    Tag tag2 = realm.createObject(Tag.class, 2);
                    tag2.setName("Blues");


                    Band band1 = realm.createObject(Band.class, 1);
                    band1.setName("Los Pepes");
                    band1.setTag(tag1);


                    Band band2 = realm.createObject(Band.class, 2);
                    band2.setName("Las Juanas");
                    band2.setTag(tag2);

                } catch (RealmPrimaryKeyConstraintException e) {

                }

            }
        });

    }


    public static List<Band> getBandsDB() {
        return Realm.getDefaultInstance().where(Band.class).findAll();
    }

    public static List<Band> getBandsDB(String nameFilter) {
        return Realm.getDefaultInstance().where(Band.class).contains(Band.NAME, nameFilter, Case.INSENSITIVE).findAll();
    }

    public static Band getBandDB(int idBand) {
        return Realm.getDefaultInstance().where(Band.class).equalTo(Band.ID, idBand).findFirst();
    }

    public void getBands(final BandsCallback callback) {

        if (!Util.isConnected(context)) {
            baseView.toast(R.string.no_connection);
            return;
        }

        baseView.setRefresing(true);

        getApi().getBands()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).doOnTerminate(actionTerminate)
                .subscribe(new Observer<List<Band>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Band> bands) {

                        baseView.setRefresing(false);

                        storeBands(bands);

                        callback.onResponse(bands);


                    }
                });


    }

    private void storeBands(List<Band> bands) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(bands);
        realm.commitTransaction();
    }


    // TAGS of bands
    public List<Tag> getTags() {
        return Realm.getDefaultInstance().where(Tag.class).findAll();
    }

//    public void initializeMockTags() {
//        Realm realm = Realm.getDefaultInstance();
//        List<Tag> tags = new ArrayList<>();
//        tags.add(new Tag(1, "Jazz", "#f67800"));
//        tags.add(new Tag(2, "Blues", "#f678bb"));
//        tags.add(new Tag(3, "Rock/Pop/Indie", "#007800"));
//        tags.add(new Tag(4, "Clasica", "#f66341"));
//
//        realm.beginTransaction();
//        realm.insertOrUpdate(tags);
//        realm.commitTransaction();
//    }

    public void toggleTagState(String idTag) {

        Realm realm = Realm.getDefaultInstance();
        Tag tag = realm.where(Tag.class).equalTo(Tag.ID, idTag).findFirst();

        realm.beginTransaction();
        tag.setActive(!tag.isActive());
        realm.commitTransaction();
    }

    public void setAllTagsActive() {

        Realm realm = Realm.getDefaultInstance();
        List<Tag> tags = realm.where(Tag.class).findAll();

        realm.beginTransaction();
        for (Tag tag : tags) {
            tag.setActive(true);
        }
        realm.commitTransaction();
    }

    public boolean areAllTagsActive() {
        return Realm.getDefaultInstance().where(Tag.class).equalTo(Tag.ACTIVE, false).findAll().isEmpty();
    }

    public void setAllTagsInactiveUnlessThisOne(String idTag) {

        List<Tag> tags = getTags();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (Tag tag : tags) {
            tag.setActive(tag.getId().equals(idTag));
        }
        realm.commitTransaction();
    }

    private Api getApi() {
        return getApi(Api.class);
    }


}
