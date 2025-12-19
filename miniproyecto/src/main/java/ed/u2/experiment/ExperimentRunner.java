package ed.u2.experiment;
import ed.u2.stats.OperationStats;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ExperimentRunner {

    public static OperationStats run(Supplier<OperationStats> algorithmRun) {

        List<OperationStats> validRuns = new ArrayList<>();

        for (int i = 0; i < ExperimentConfig.TOTAL_RUNS; i++) {

            OperationStats st = algorithmRun.get();

            // descartar warm-up
            if (i >= ExperimentConfig.WARMUP_DISCARD) {
                validRuns.add(st);
            }
        }

        return StatsAggregator.median(validRuns);
    }
}
