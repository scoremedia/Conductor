package com.bluelinelabs.conductor.autodispose;

import com.uber.autodispose.OutsideLifecycleException;

import io.reactivex.functions.Function;

/**
 * Based on https://github.com/uber/AutoDispose/blob/master/lifecycle/autodispose-lifecycle/src/main/java/com/uber/autodispose/lifecycle/CorrespondingEventsFunction.java
 *
 * A corresponding events function that acts as a normal {@link Function} but ensures ControllerEvent event
 * types are used in the generic and tightens the possible exception thrown to {@link OutsideLifecycleException}.
 */
public interface CorrespondingEventsFunction extends Function<ControllerEvent, ControllerEvent> {

    /**
     * Given an event {@code event}, returns the next corresponding event that this lifecycle should
     * dispose on.
     *
     * @param event the source or start event.
     * @return the target event that should signal disposal.
     * @throws OutsideLifecycleException if the lifecycle exceeds its scope boundaries.
     */
    @Override ControllerEvent apply(ControllerEvent event) throws OutsideLifecycleException;
}