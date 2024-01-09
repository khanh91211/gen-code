package com.fw.core.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

@Configuration
public class ValidatorConfig {
  /**
   * Configure the validator to support spring context
   * 
   * @param autowireCapableBeanFactory
   * @return
   */
  @Bean
  Validator validator(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
    ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
        .constraintValidatorFactory(
            new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
        .buildValidatorFactory();
    return validatorFactory.getValidator();
  }
}
