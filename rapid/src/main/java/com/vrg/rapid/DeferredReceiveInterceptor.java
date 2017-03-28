/*
 * Copyright © 2016 - 2017 VMware, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an “AS IS” BASIS, without warranties or conditions of any kind,
 * EITHER EXPRESS OR IMPLIED. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.vrg.rapid;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

/**
 * An interceptor that blocks server calls from being invoked until requested.
 * Used by the RpcServer to defer service method invocations until it has instantiated
 * a MembershipService object.
 */
final class DeferredReceiveInterceptor implements ServerInterceptor {
    private boolean isReady = false;
    static final Metadata.Key<String> CONFIRMATION_MSG = Metadata.Key.of("CONF", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(final ServerCall<ReqT, RespT> serverCall,
                                                                 final Metadata metadata,
                                                                 final ServerCallHandler<ReqT, RespT> next) {
        if (isReady || metadata.containsKey(CONFIRMATION_MSG)) {
            return next.startCall(serverCall, metadata);
        }
        return new ServerCall.Listener<ReqT>() { }; // Forces remote to retry
    }

    /**
     * Stops the interceptor from deferring server calls.
     */
    void unblock() {
        isReady = true;
    }
}
