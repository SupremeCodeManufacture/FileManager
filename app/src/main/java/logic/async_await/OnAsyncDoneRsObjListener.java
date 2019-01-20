package logic.async_await;

/**
 * Created by vasil on 01-Nov-17.
 */

public interface OnAsyncDoneRsObjListener {

    <T> void onDone(T obj);
}