package liquid.config.service;

import liquid.config.PropertyPlaceholderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 4/9/15.
 */
@Service
public class ConfigurableServiceFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurableServiceFactoryPostProcessor.class);

    @Autowired
    private PropertyPlaceholderConfig propertyPlaceholderConfig;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            logger.debug("Generic bean: {}", beanName);
        }

//        beanNames = beanFactory.getBeanNamesForType(UserService.class);
//        for (String beanName : beanNames) {
//            logger.debug("UserService: {}", beanName);
//        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        logger.debug("User Service Class Name: {}");
        beanDefinition.setBeanClassName(PropertiesConfigurator.getInstance().getUserServiceClassName());
        registry.registerBeanDefinition("userService", beanDefinition);
    }
}
