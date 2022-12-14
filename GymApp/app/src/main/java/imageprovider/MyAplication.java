package imageprovider;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;


public class MyAplication extends Application {


    public static AtomicInteger idChat=new AtomicInteger();
    public static AtomicInteger idMessage =new AtomicInteger();
    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        setUpcondig();
        Realm realm= Realm.getDefaultInstance();
        idChat=getAllid(realm,RealmChats.class);
        idMessage=getAllid(realm,modelMessages.class);
        realm.close();
    }



    public void setUpcondig(){
        Realm.init(context);
        RealmConfiguration config= new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
    }


    public <T extends RealmObject> AtomicInteger getAllid(Realm realm, Class<T> anyclas){
        RealmResults results=realm.where(anyclas).findAll();
        return (results.size()>0)?new AtomicInteger(results.max("id").intValue()):new AtomicInteger();
    }


}
