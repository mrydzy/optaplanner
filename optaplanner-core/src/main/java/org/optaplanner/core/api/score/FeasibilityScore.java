/*
 * Copyright 2013 JBoss Inc
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

package org.optaplanner.core.api.score;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;

/**
 * A {@link Score} that supports {@link #isFeasible()}.
 * Most {@link Score} implementations implement this interface (including {@link HardSoftScore}),
 * except for {@link SimpleScore} variants.
 * @see Score
 */
public interface FeasibilityScore<S extends FeasibilityScore> extends Score<S> {

    /**
     * A {@link Solution} is feasible if it has no broken hard constraints.
     * @return true if the hard score is 0 or higher
     */
    boolean isFeasible();

}
