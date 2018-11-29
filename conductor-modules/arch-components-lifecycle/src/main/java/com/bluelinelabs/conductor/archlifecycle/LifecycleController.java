package com.bluelinelabs.conductor.archlifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bluelinelabs.conductor.Controller;

public abstract class LifecycleController extends Controller implements LifecycleOwner {

    private final ControllerLifecycleOwner lifecycleOwner = new ControllerLifecycleOwner(this);

    public LifecycleController() {
        super();
    }

    public LifecycleController(@Nullable Bundle args) {
        super(args);
    }

    @Override @NonNull
    public Lifecycle getLifecycle() {
        return lifecycleOwner.getLifecycle();
    }

}
