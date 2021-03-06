/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.domain.variable.inverserelation;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.optaplanner.core.impl.domain.variable.descriptor.GenuineVariableDescriptor;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.testdata.domain.TestdataEntity;
import org.optaplanner.core.impl.testdata.domain.TestdataSolution;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;
import org.optaplanner.core.impl.testdata.domain.shadow.inverserelation.TestdataInverseRelationEntity;
import org.optaplanner.core.impl.testdata.domain.shadow.inverserelation.TestdataInverseRelationSolution;
import org.optaplanner.core.impl.testdata.domain.shadow.inverserelation.TestdataInverseRelationValue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.optaplanner.core.impl.testdata.util.PlannerAssert.assertCollectionContainsExactly;

public class ExternalizedCollectionInverseVariableSupplyTest {

    @Test
    public void normal() {
        GenuineVariableDescriptor variableDescriptor = TestdataEntity.buildVariableDescriptorForValue();
        ScoreDirector scoreDirector = mock(ScoreDirector.class);
        ExternalizedCollectionInverseVariableSupply supply = new ExternalizedCollectionInverseVariableSupply(variableDescriptor);

        TestdataValue val1 = new TestdataValue("1");
        TestdataValue val2 = new TestdataValue("2");
        TestdataValue val3 = new TestdataValue("3");
        TestdataEntity a = new TestdataEntity("a", val1);
        TestdataEntity b = new TestdataEntity("b", val1);
        TestdataEntity c = new TestdataEntity("c", val3);
        TestdataEntity d = new TestdataEntity("d", val3);

        TestdataSolution solution = new TestdataSolution("solution");
        solution.setEntityList(Arrays.asList(a, b, c, d));
        solution.setValueList(Arrays.asList(val1, val2, val3));

        when(scoreDirector.getWorkingSolution()).thenReturn(solution);
        supply.resetWorkingSolution(scoreDirector);

        assertCollectionContainsExactly((Collection<Object>) supply.getInverseCollection(val1), a, b);
        assertCollectionContainsExactly((Collection<Object>) supply.getInverseCollection(val2));
        assertCollectionContainsExactly((Collection<Object>) supply.getInverseCollection(val3), c, d);

        supply.beforeVariableChanged(scoreDirector, c);
        c.setValue(val2);
        supply.afterVariableChanged(scoreDirector, c);

        assertCollectionContainsExactly((Collection<Object>) supply.getInverseCollection(val1), a, b);
        assertCollectionContainsExactly((Collection<Object>) supply.getInverseCollection(val2), c);
        assertCollectionContainsExactly((Collection<Object>) supply.getInverseCollection(val3), d);

        supply.clearWorkingSolution(scoreDirector);
    }

}
