package com.kam6512.CRN_BOT.api;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HomeTaxService {
    @POST("wqAction.do?actionId=ATTABZAA001R08&screenId=UTEABAAA13&popupYn=false&realScreenId=")
    Observable<ResponseBody> getResult(@Body RequestBody requestBody);

}
