package com.fw.channel.gateway;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author binhph
 *
 */
@Configuration
@ConditionalOnMissingBean(WarDeploymentConfig.class)
public class JarDeploymentConfig {

}
