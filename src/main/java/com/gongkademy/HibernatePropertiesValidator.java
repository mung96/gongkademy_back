package com.gongkademy;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

final class HibernatePropertiesValidator implements EnvironmentPostProcessor {

    private static final String PRODUCTION_PROFILE_NAME = "prod";
    private static final String DEVELOP_PROFILE_NAME = "dev";

    private static final String SPRING_JPA_HIBERNATE_DDL_AUTO_PROPERTY = "spring.jpa.hibernate.ddl-auto";
    private static final String SPRING_JPA_PROPERTIES_HIBERNATE_HBM2DDL_AUTO_PROPERTY = "spring.jpa.properties.hibernate.hbm2ddl.auto";

    private static final List<String> VALIDATION_PROPERTY_NAMES = Arrays.asList(
            SPRING_JPA_HIBERNATE_DDL_AUTO_PROPERTY,
            SPRING_JPA_PROPERTIES_HIBERNATE_HBM2DDL_AUTO_PROPERTY
    );

    @Override
    public void postProcessEnvironment(
            final ConfigurableEnvironment environment,
            final SpringApplication application
    ) {
        boolean isProdProfileActive = Arrays
                .asList(environment.getActiveProfiles())
                .contains(PRODUCTION_PROFILE_NAME);

        boolean isDevProfileActive = Arrays
                .asList(environment.getActiveProfiles())
                .contains(DEVELOP_PROFILE_NAME);

        if (isProdProfileActive||isDevProfileActive) {
            validate(environment);
        }
    }

    private void validate(
            final ConfigurableEnvironment environment
    ) {
        for (final String propertyName : VALIDATION_PROPERTY_NAMES) {
            final String propertyValue = environment.getProperty(propertyName);
            if (checkInvalidPropertyValue(propertyValue)) {
                throw new IllegalArgumentException(
                        String.format("In profile, '%s': 'none' 만 가능합니다. 현재 '%s'로 설정되어 있습니다."
                                , propertyName
                                , propertyValue
                        )
                );
            }
        }
    }

    private boolean checkInvalidPropertyValue(
            final String propertyValue
    ) {
        // none or validate
        return propertyValue != null && (!"none".equals(propertyValue));
    }
}
