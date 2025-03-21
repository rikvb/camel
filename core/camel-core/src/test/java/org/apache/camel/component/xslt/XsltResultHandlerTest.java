/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.xslt;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.TestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XsltResultHandlerTest extends TestSupport {

    @Test
    public void testResultHandlerFactory() throws Exception {
        RouteBuilder builder = createRouteBuilder();
        CamelContext context = new DefaultCamelContext();
        ResultHandlerFactory factory = new DomResultHandlerFactory();
        context.getRegistry().bind("factory", factory);
        context.addRoutes(builder);
        context.start();

        XsltEndpoint endpoint = null;
        for (Endpoint ep : context.getEndpoints()) {
            if (ep instanceof XsltEndpoint) {
                endpoint = (XsltEndpoint) ep;
                break;
            }
        }

        assertNotNull(endpoint);
        assertEquals(DomResultHandlerFactory.class, factory.getClass());
        assertEquals(factory, endpoint.getResultHandlerFactory());
        assertEquals(factory, endpoint.getXslt().getResultHandlerFactory());
    }


    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:start").to("xslt:org/apache/camel/component/xslt/example.xsl?output=bytes&resultHandlerFactory=#factory");
            }
        };
    }

}
