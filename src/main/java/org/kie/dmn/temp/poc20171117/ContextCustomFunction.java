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

import org.kie.dmn.feel.lang.EvaluationContext;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;

public class ContextCustomFunction extends BaseFEELFunction {

    public ContextCustomFunction() {
        super("context");
    }

    @Override
    public FEELFnResult<Object> invoke(EvaluationContext ctx, Object[] params) {
        return FEELFnResult.ofResult(ctx.getAllValues());
    }

    @Override
    protected boolean isCustomFunction() {
        return true;
    }

}
