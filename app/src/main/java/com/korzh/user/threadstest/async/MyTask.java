//package com.korzh.user.threadstest.async;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.korzh.user.threadstest.MainActivity;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
///**
// * Created by user on 27.06.17.
// */
//
//public class MyTask extends AsyncTask<String, Integer, Bitmap>{
//
//    private static final String TAG = "MyTask";
//    private String url;
//    MainActivity.CallBack callBack;
//
//    public MyTask(String url, MainActivity.CallBack callBack) {
//        super();
//        this.url = url;
//        this.callBack = callBack;
//        Log.d(TAG, "MyTask: ");
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        Log.d(TAG, "onPreExecute: "+Thread.currentThread().getName());
//    }
//
//    @Override
//    protected Bitmap doInBackground(String... params) {
//        Log.d(TAG, "doInBackground: "+Thread.currentThread().getName());
//        return downloadImage(url);
//    }
//
//
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
//        Log.d(TAG, "onProgressUpdate: "+Thread.currentThread().getName()+" "+values[0]);
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap bitmap) {
//        super.onPostExecute(bitmap);
//        Log.d(TAG, "onPostExecute: "+Thread.currentThread().getName());
//
//        callBack.onGetBitmap(bitmap);
//    }
//
//
//    private Bitmap downloadImage(String url) {
//
//        Bitmap bmp =null;
//        try{
//            URL ulrn = new URL(url);
//            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
//            InputStream is = con.getInputStream();
//            bmp = BitmapFactory.decodeStream(is);
//            if (null != bmp)
//                return bmp;
//
//        }catch(Exception e){}
//        return bmp;
//    }
//
//}
