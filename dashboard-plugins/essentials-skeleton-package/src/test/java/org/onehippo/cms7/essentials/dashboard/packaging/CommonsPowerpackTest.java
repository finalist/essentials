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

package org.onehippo.cms7.essentials.dashboard.packaging;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.onehippo.cms7.essentials.BaseRepositoryTest;
import org.onehippo.cms7.essentials.dashboard.instructions.InstructionSet;
import org.onehippo.cms7.essentials.dashboard.instructions.Instructions;

import static org.junit.Assert.assertEquals;

/**
 * @version "$Id$"
 */
public class CommonsPowerpackTest extends BaseRepositoryTest{


    public static final int INSTRUCTIONS_SIZE = 6;

    @Test
    public void testGroupNames() throws Exception {

        final PowerpackPackage powerpackPackage = new CommonsPowerpack();
        injector.autowireBean(powerpackPackage);
        final Instructions instructions = powerpackPackage.getInstructions();
        final Set<InstructionSet> instructionSets = instructions.getInstructionSets();
        Assert.assertTrue(instructionSets.size() == INSTRUCTIONS_SIZE);
        powerpackPackage.execute(getContext());
    }

    @Test
    public void testGetInstructionPath() throws Exception {
        PowerpackPackage powerpackPackage = new CommonsPowerpack();
        final String instructionPath = powerpackPackage.getInstructionPath();
        assertEquals(instructionPath, "/META-INF/commons_instructions.xml");

    }
}