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

package org.optaplanner.core.impl.score.buildin.bendable;

import org.junit.Test;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.config.score.trend.InitializingScoreTrendLevel;
import org.optaplanner.core.impl.score.trend.InitializingScoreTrend;

import static org.junit.Assert.assertEquals;

public class BendableScoreDefinitionTest {

    @Test
    public void getLevelsSize() {
        assertEquals(2, new BendableScoreDefinition(1, 1).getLevelsSize());
        assertEquals(7, new BendableScoreDefinition(3, 4).getLevelsSize());
        assertEquals(7, new BendableScoreDefinition(4, 3).getLevelsSize());
        assertEquals(5, new BendableScoreDefinition(0, 5).getLevelsSize());
        assertEquals(5, new BendableScoreDefinition(5, 0).getLevelsSize());
    }

    @Test
    public void getFeasibleLevelsSize() {
        assertEquals(1, new BendableScoreDefinition(1, 1).getFeasibleLevelsSize());
        assertEquals(3, new BendableScoreDefinition(3, 4).getFeasibleLevelsSize());
        assertEquals(4, new BendableScoreDefinition(4, 3).getFeasibleLevelsSize());
        assertEquals(0, new BendableScoreDefinition(0, 5).getFeasibleLevelsSize());
        assertEquals(5, new BendableScoreDefinition(5, 0).getFeasibleLevelsSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createScoreWithIllegalArgument() {
        BendableScoreDefinition bendableScoreDefinition = new BendableScoreDefinition(2, 3);
        bendableScoreDefinition.createScore(1, 2, 3);
    }

    @Test
    public void createScore() {
        int hardLevelSize = 3;
        int softLevelSize = 2;
        int levelSize = hardLevelSize + softLevelSize;
        int[] scores = new int [levelSize];
        for (int i = 0; i < levelSize; i++) {
            scores[i] = i;
        }
        BendableScoreDefinition bendableScoreDefinition = new BendableScoreDefinition(hardLevelSize, softLevelSize);
        BendableScore bendableScore = bendableScoreDefinition.createScore(scores);
        assertEquals(hardLevelSize, bendableScore.getHardLevelsSize());
        assertEquals(softLevelSize, bendableScore.getSoftLevelsSize());
        for (int i = 0; i < levelSize; i++) {
            if (i < hardLevelSize) {
                assertEquals(scores[i], bendableScore.getHardScore(i));
            } else {
                assertEquals(scores[i], bendableScore.getSoftScore(i - hardLevelSize));
            }
        }
    }

    @Test
    public void buildOptimisticBoundOnlyUp() {
        BendableScoreDefinition scoreDefinition = new BendableScoreDefinition(2, 3);
        BendableScore optimisticBound = scoreDefinition.buildOptimisticBound(
                InitializingScoreTrend.buildUniformTrend(InitializingScoreTrendLevel.ONLY_UP, 5),
                scoreDefinition.createScore(-1, -2, -3, -4, -5));
        assertEquals(Integer.MAX_VALUE, optimisticBound.getHardScore(0));
        assertEquals(Integer.MAX_VALUE, optimisticBound.getHardScore(1));
        assertEquals(Integer.MAX_VALUE, optimisticBound.getSoftScore(0));
        assertEquals(Integer.MAX_VALUE, optimisticBound.getSoftScore(1));
        assertEquals(Integer.MAX_VALUE, optimisticBound.getSoftScore(2));
    }

    @Test
    public void buildOptimisticBoundOnlyDown() {
        BendableScoreDefinition scoreDefinition = new BendableScoreDefinition(2, 3);
        BendableScore optimisticBound = scoreDefinition.buildOptimisticBound(
                InitializingScoreTrend.buildUniformTrend(InitializingScoreTrendLevel.ONLY_DOWN, 5),
                scoreDefinition.createScore(-1, -2, -3, -4, -5));
        assertEquals(-1, optimisticBound.getHardScore(0));
        assertEquals(-2, optimisticBound.getHardScore(1));
        assertEquals(-3, optimisticBound.getSoftScore(0));
        assertEquals(-4, optimisticBound.getSoftScore(1));
        assertEquals(-5, optimisticBound.getSoftScore(2));
    }

    @Test
    public void buildPessimisticBoundOnlyUp() {
        BendableScoreDefinition scoreDefinition = new BendableScoreDefinition(2, 3);
        BendableScore pessimisticBound = scoreDefinition.buildPessimisticBound(
                InitializingScoreTrend.buildUniformTrend(InitializingScoreTrendLevel.ONLY_UP, 5),
                scoreDefinition.createScore(-1, -2, -3, -4, -5));
        assertEquals(-1, pessimisticBound.getHardScore(0));
        assertEquals(-2, pessimisticBound.getHardScore(1));
        assertEquals(-3, pessimisticBound.getSoftScore(0));
        assertEquals(-4, pessimisticBound.getSoftScore(1));
        assertEquals(-5, pessimisticBound.getSoftScore(2));
    }

    @Test
    public void buildPessimisticBoundOnlyDown() {
        BendableScoreDefinition scoreDefinition = new BendableScoreDefinition(2, 3);
        BendableScore pessimisticBound = scoreDefinition.buildPessimisticBound(
                InitializingScoreTrend.buildUniformTrend(InitializingScoreTrendLevel.ONLY_DOWN, 5),
                scoreDefinition.createScore(-1, -2, -3, -4, -5));
        assertEquals(Integer.MIN_VALUE, pessimisticBound.getHardScore(0));
        assertEquals(Integer.MIN_VALUE, pessimisticBound.getHardScore(1));
        assertEquals(Integer.MIN_VALUE, pessimisticBound.getSoftScore(0));
        assertEquals(Integer.MIN_VALUE, pessimisticBound.getSoftScore(1));
        assertEquals(Integer.MIN_VALUE, pessimisticBound.getSoftScore(2));
    }

}
