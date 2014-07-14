/*
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onehippo.cms7.essentials.dashboard.restservices;

import java.util.Set;

import org.onehippo.cms7.essentials.dashboard.packaging.DefaultInstructionPackage;
import org.onehippo.cms7.essentials.dashboard.utils.EssentialConst;

import com.google.common.collect.ImmutableSet;

/**
 * @version "$Id$"
 */
public class RestServicesInstructionPackage extends DefaultInstructionPackage {

    @Override
    public String getInstructionPath() {
        return "/META-INF/rest_instructions.xml";
    }

    @Override
    public Set<String> groupNames() {
        final String restType = (String) getProperties().get(RestPluginConst.REST_TYPE);
        if (restType != null) {
            return new ImmutableSet.Builder<String>().add(restType).build();
        }
        return EssentialConst.DEFAULT_GROUPS;
    }
}
