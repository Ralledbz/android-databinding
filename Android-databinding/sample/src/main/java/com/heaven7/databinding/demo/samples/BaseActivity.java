package com.heaven7.databinding.demo.samples;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.databinding.core.DataBinder;
import com.heaven7.databinding.demo.util.VolleyUtil;

import org.heaven7.core.save_state.SaveStateHelper;
import org.heaven7.core.util.Toaster;

/**
 * Created by heaven7 on 2015/11/30.
 */
public abstract  class BaseActivity extends AppCompatActivity{

    private Toaster mToaster;
    protected DataBinder mDataBinder;
    protected VolleyUtil.HttpExecutor mHttpExecute;
    private SaveStateHelper mSaveStateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToaster = new Toaster(this);
        mHttpExecute = new VolleyUtil.HttpExecutor();
        mSaveStateHelper = new SaveStateHelper(this);
        setContentView(getlayoutId());
        mDataBinder = new DataBinder(this,getBindRawId());
        onFinalInit(savedInstanceState);
        doBind();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHttpExecute.cancelAll();
    }

    @Override
    protected void onDestroy() {
        mDataBinder.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        mSaveStateHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSaveStateHelper.onRestoreInstanceState(savedInstanceState);
    }

    protected abstract void doBind();

    protected abstract void onFinalInit(Bundle savedInstanceState);

    protected abstract int getlayoutId();
    protected abstract int getBindRawId();

    protected void showToast(String msg){
        mToaster.show(msg);
    }

    protected void showToast(int resID){
        mToaster.show(resID);
    }
    protected Toaster getToaster(){
        return mToaster;
    }
}
