package com.atguigu.retrofitandrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RetrofitRxjavaActivity extends AppCompatActivity {
    @Bind(R.id.et1)
    EditText et1;
    @Bind(R.id.et2)
    EditText et2;
    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.tv_content)
    TextView tvContent;
    private String baseUrl = "http://47.93.118.241:8081/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_rxjava);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn1)
    public void onClick() {
        login();
    }


    /**
     * 账号 123 密码 123 电话123123123 可以登录
     */
    public void login() {
        String username = et1.getText().toString().trim();
        String password = et2.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)//baseUrl 请求根连接 此处为 "http://47.93.118.241:8081/"
                .addConverterFactory(GsonConverterFactory.create()) //Gson解析工厂 自动解析Bean
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//RxJava回调工厂 表示支持返回Observable
                .build();

        RequestServes requestServes = retrofit.create(RequestServes.class);

        requestServes.login(username, password, "123123123")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        tvContent.setText(user.toString());
                    }
                });
    }


    //业务接口
    public interface RequestServes {
        //使用@Query("字段名")   Retrofit会自动拼接字段 发送请求
        //拼接后续连接
        @POST("android/user/login")
        Observable<User> login(@Query("username") String username,
                               @Query("password") String password,
                               @Query("phone") String phone);
    }
}
