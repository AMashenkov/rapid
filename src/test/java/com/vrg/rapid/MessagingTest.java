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

import com.google.common.net.HostAndPort;
import com.vrg.rapid.pb.Remoting.Response;
import com.vrg.rapid.pb.Remoting.Status;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * Tests without changing incarnations for a watermark-buffer
 */
public class MessagingTest {
    private static final int K = 10;
    private static final int H = 8;
    private static final int L = 3;

    @Test
    public void oneWayPing() throws InterruptedException, IOException {
        final int serverPort = 1234;
        final HostAndPort serverAddr = HostAndPort.fromParts("127.0.0.1", serverPort);
        final MembershipService service = new MembershipService(serverAddr, K, H, L);
        service.startServer();

        final HostAndPort clientAddr = HostAndPort.fromParts("127.0.0.1", serverPort);
        final long configId = 10;
        final MessagingClient client = new MessagingClient();
        final Response result = client.sendLinkUpdateMessage(clientAddr, serverAddr,
                                                            Status.DOWN, configId);
        assertNotNull(result);
    }

}