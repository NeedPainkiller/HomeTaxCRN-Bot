package com.kam6512.CRN_BOT.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.Retrofit;


public class HomeTaxApi {

    private final HomeTaxService homeTaxService;

    private HomeTaxApi() {
        homeTaxService = createHomeTaxService();
    }

    public static HomeTaxApi getInstance() {
        return HomeTaxApiHolder.INSTANCE;
    }

    private HomeTaxService createHomeTaxService() {
        return new Retrofit.Builder()
                .baseUrl("https://teht.hometax.go.kr/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(HomeTaxService.class);
    }

    private static class HomeTaxApiHolder {
        private static final HomeTaxApi INSTANCE = new HomeTaxApi();
    }

    public HomeTaxService getHomeTaxService() {
        return homeTaxService;
    }
}
