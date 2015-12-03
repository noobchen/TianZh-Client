package com.tianzh.pay.service;


/**
 * Created by pig on 2015-11-13.
 */
public abstract class ConfirmDialogBuilder {
    public abstract void show();

    public abstract void dismiss();

    public abstract void confirm();

    public abstract void cancel();
}
