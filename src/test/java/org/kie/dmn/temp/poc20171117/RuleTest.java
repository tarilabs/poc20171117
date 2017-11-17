/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.kie.dmn.temp.poc20171117;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleTest {
    static final Logger LOG = LoggerFactory.getLogger(RuleTest.class);
    private static DMNRuntime dmnRuntime;
    private static DMNModel dmnModel;

    @BeforeClass
    public static void init() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kContainer = kieServices.getKieClasspathContainer();
        Results verifyResults = kContainer.verify();
        for (Message m : verifyResults.getMessages()) {
            LOG.info("{}", m);
        }

        assertFalse(verifyResults.getMessages().toString(), verifyResults.hasMessages(Level.ERROR));

        dmnRuntime = kContainer.newKieSession().getKieRuntime(DMNRuntime.class);

        dmnModel = dmnRuntime.getModel("http://www.trisotech.com/definitions/_337d15f6-09db-4d20-b49e-2ce272ac0f8b", "caller PN");
        assertTrue(dmnModel != null);
    }

    private DMNContext makeContext(int aNumber) {
        DMNContext dmnContext = dmnRuntime.newContext();
        dmnContext.set("a number", aNumber);
        dmnContext.set("abc", "value-of-abc");
        dmnContext.set("xyz", "this-is-xyz");
        return dmnContext;
    }

    @Test
    public void testPositive() {
        DMNContext dmnContext = makeContext(47);

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        System.out.println(dmnResult);
        assertEquals("I am a model that depends on abc: value-of-abc and xyz: this-is-xyz and I take a decision.",
                     dmnResult.getDecisionResultByName("invoke external model").getResult());
    }

    @Test
    public void testNegative() {
        DMNContext dmnContext = makeContext(-999);

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        System.out.println(dmnResult);
        assertEquals("I am a decision that depends on a number: -999 and xyz: this-is-xyz to conclude for another decision.",
                     dmnResult.getDecisionResultByName("invoke external model").getResult());
    }
}