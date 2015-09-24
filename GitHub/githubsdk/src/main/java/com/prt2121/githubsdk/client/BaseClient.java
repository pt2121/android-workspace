package com.prt2121.githubsdk.client;

import android.content.Context;
import com.squareup.okhttp.OkHttpClient;
import retrofit.Converter;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pt2121 on 9/23/15.
 */
public abstract class BaseClient<T> {

  private static final String BASE_URL = "https://api.github.com";
  protected Context context;
  private CredentialStorage credentialStorage;

  public BaseClient(Context context) {
    this.context = context.getApplicationContext();
  }

  protected Retrofit getRetrofit() {
    Retrofit.Builder builder =
        new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(BASE_URL);

    if (customConverter() != null) {
      builder.addConverterFactory(customConverter());
    }

    if (getClient() != null) {
      builder.client(getClient());
    }

    return builder.build();
  }

  protected Converter.Factory customConverter() {
    return null;
  }

  protected OkHttpClient getClient() {
    return null;
  }

  protected abstract Observable<T> execute();

  protected <T1> Observable.Transformer<T1, T1> applySchedulers() {
    return observable -> observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}