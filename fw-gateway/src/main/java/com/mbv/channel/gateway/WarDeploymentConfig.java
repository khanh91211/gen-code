package com.fw.channel.gateway;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author binhph
 *
 */
@Configuration
@ConditionalOnProperty(name = "app.deployment.mode", havingValue = "web", matchIfMissing = false)
public class WarDeploymentConfig {

}
