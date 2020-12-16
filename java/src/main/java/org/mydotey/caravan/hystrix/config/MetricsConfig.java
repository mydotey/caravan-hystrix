package org.mydotey.caravan.hystrix.config;

import org.mydotey.util.DataBufferConfig;
import org.mydotey.util.TimeSequenceCircularBufferConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface MetricsConfig {

    String HEALTH_SNAPSHOT_INTERVAL_PROPERTY_KEY = "execution.metrics.health-snapshot-interval";

    TimeSequenceCircularBufferConfig eventCounterConfig();

    long healthSnapshotInterval();

    DataBufferConfig executionLatencyConfig();

}
