package ed.u2.experiment;

import ed.u2.stats.OperationStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsAggregator {

    public static OperationStats median(List<OperationStats> runs) {

        List<Long> tiempos = new ArrayList<>();
        List<Long> comparaciones = new ArrayList<>();
        List<Long> intercambios = new ArrayList<>();

        for (OperationStats st : runs) {
            tiempos.add(st.getTime());
            comparaciones.add(st.getComparisons());
            intercambios.add(st.getSwaps());
        }

        Collections.sort(tiempos);
        Collections.sort(comparaciones);
        Collections.sort(intercambios);

        int mid = tiempos.size() / 2;

        OperationStats median = new OperationStats();
        median.setTime(tiempos.get(mid));
        median.setComparisons(comparaciones.get(mid));
        median.setSwaps(intercambios.get(mid));

        return median;
    }
}
