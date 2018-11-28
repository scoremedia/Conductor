package com.bluelinelabs.conductor.autodispose;

import android.support.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.uber.autodispose.LifecycleScopeProvider;
import com.uber.autodispose.OutsideLifecycleException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class ControllerScopeProvider implements LifecycleScopeProvider<ControllerEvent> {
    private static final CorrespondingEventsFunction CORRESPONDING_EVENTS =
            new CorrespondingEventsFunction() {
                @Override
                public ControllerEvent apply(ControllerEvent lastEvent) throws OutsideLifecycleException {
                    switch (lastEvent) {
                        case CREATE:
                            return ControllerEvent.DESTROY;
                        case CONTEXT_AVAILABLE:
                            return ControllerEvent.CONTEXT_UNAVAILABLE;
                        case CREATE_VIEW:
                            return ControllerEvent.DESTROY_VIEW;
                        case ATTACH:
                            return ControllerEvent.DETACH;
                        case DETACH:
                            return ControllerEvent.DESTROY;
                        default:
                            throw new OutsideLifecycleException("Cannot bind to Controller lifecycle when outside of it.");
                    }
                }
            };

    @NonNull private final BehaviorSubject<ControllerEvent> lifecycleSubject;
    @NonNull private final Function<ControllerEvent, ControllerEvent> correspondingEventsFunction;

    public static ControllerScopeProvider from(@NonNull Controller controller) {
        return new ControllerScopeProvider(controller, CORRESPONDING_EVENTS);
    }

    public static ControllerScopeProvider from(@NonNull Controller controller, @NonNull final ControllerEvent untilEvent) {
        return new ControllerScopeProvider(controller, new CorrespondingEventsFunction() {
            @Override
            public ControllerEvent apply(ControllerEvent controllerEvent) {
                return untilEvent;
            }
        });
    }

    public static ControllerScopeProvider from(@NonNull Controller controller, @NonNull final CorrespondingEventsFunction correspondingEventsFunction) {
        return new ControllerScopeProvider(controller, correspondingEventsFunction);
    }

    private ControllerScopeProvider(@NonNull Controller controller, @NonNull CorrespondingEventsFunction correspondingEventsFunction) {
        lifecycleSubject = ControllerLifecycleSubjectHelper.create(controller);
        this.correspondingEventsFunction = correspondingEventsFunction;
    }

    @Override
    public Observable<ControllerEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    public Function<ControllerEvent, ControllerEvent> correspondingEvents() {
        return correspondingEventsFunction;
    }

    @Override
    public ControllerEvent peekLifecycle() {
        return lifecycleSubject.getValue();
    }
}
