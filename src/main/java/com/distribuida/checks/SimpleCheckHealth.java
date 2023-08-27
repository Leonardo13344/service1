package com.distribuida.checks;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@ApplicationScoped
@Liveness
public class SimpleCheckHealth implements HealthCheck {

        @Override
        public HealthCheckResponse call() {
            return HealthCheckResponse.named("Simple health check").up().build();
        }
}
