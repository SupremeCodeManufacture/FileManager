package logic.async_await;

import android.os.AsyncTask;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by vasil on 02-Nov-17.
 */

public class AsyncTaskWorker extends AsyncTask<Void, Void, Object> {

    //use paralell  execution only for server sync calls to not lock db requests OR use AsyncTask.THREAD_POOL_EXECUTOR
    public static Executor CUSTOM_HTTP_PARALEL_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    private Callable<Void> methodToRunNoRs;
    private OnAsyncDoneNoRsListener noRsListener;

    private CallableObj methodToRunWithRs;
    private OnAsyncDoneRsObjListener objRsListener;


    public AsyncTaskWorker(Callable<Void> simpleMethod, OnAsyncDoneNoRsListener listener) {
        this.methodToRunNoRs = simpleMethod;
        this.noRsListener = listener;
    }

    public AsyncTaskWorker(CallableObj heavyMethod, OnAsyncDoneRsObjListener listener) {
        this.methodToRunWithRs = heavyMethod;
        this.objRsListener = listener;
    }

    @Override
    protected Object doInBackground(Void... v) {
        try {
            if (objRsListener != null) {
                return methodToRunWithRs.call();

            } else {
                methodToRunNoRs.call();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (noRsListener != null)
            noRsListener.onDone();

        if (objRsListener != null)
            objRsListener.onDone(o);
    }
}
