package cwb.cmt.surface.tools;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import cwb.cmt.surface.tools.ContextWrapper;
import cwb.cmt.surface.tools.IocContext;

@SuppressWarnings("unchecked")
public class SpringIocContext extends ContextWrapper<ApplicationContext> implements IocContext, ApplicationContext {

	public SpringIocContext(ApplicationContext context) {
		super(context);
	}

	public boolean componentExists(String name) {
		return getRawContext().containsBean(name);
	}

	public <T> T getComponent(String name) {
		if (getRawContext() == null) {
			return null;
		}
		return (T) getRawContext().getBean(name);
	}

	public <T> T getComponent(String name, Object... args) {
		if (getRawContext() == null) {
			return null;
		}
		return (T) getRawContext().getBean(name, args);
	}
	
	public void publishEvent(ApplicationEvent event) {
		getRawContext().publishEvent(event);
	}

	public BeanFactory getParentBeanFactory() {
		return getRawContext().getParentBeanFactory();
	}

	public boolean containsLocalBean(String name) {
		return getRawContext().containsLocalBean(name);
	}

	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		return getRawContext().getMessage(code, args, defaultMessage, locale);
	}

	public void publishEvent(Object event) {
		getRawContext().publishEvent(event);
	}

	public Resource getResource(String location) {
		return getRawContext().getResource(location);
	}

	public Environment getEnvironment() {
		return getRawContext().getEnvironment();
	}

	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return getRawContext().getMessage(code, args, locale);
	}

	public boolean containsBeanDefinition(String beanName) {
		return getRawContext().containsBeanDefinition(beanName);
	}

	public ClassLoader getClassLoader() {
		return getRawContext().getClassLoader();
	}

	public String getId() {
		return getRawContext().getId();
	}

	public Resource[] getResources(String locationPattern) throws IOException {
		return getRawContext().getResources(locationPattern);
	}

	public String getApplicationName() {
		return getRawContext().getApplicationName();
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return getRawContext().getMessage(resolvable, locale);
	}

	public int getBeanDefinitionCount() {
		return getRawContext().getBeanDefinitionCount();
	}

	public String getDisplayName() {
		return getRawContext().getDisplayName();
	}

	public long getStartupDate() {
		return getRawContext().getStartupDate();
	}

	public String[] getBeanDefinitionNames() {
		return getRawContext().getBeanDefinitionNames();
	}

	public ApplicationContext getParent() {
		return getRawContext().getParent();
	}

	public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
		return getRawContext().getAutowireCapableBeanFactory();
	}

	public String[] getBeanNamesForType(ResolvableType type) {
		return getRawContext().getBeanNamesForType(type);
	}

	public String[] getBeanNamesForType(Class<?> type) {
		return getRawContext().getBeanNamesForType(type);
	}

	public Object getBean(String name) throws BeansException {
		return getRawContext().getBean(name);
	}

	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return getRawContext().getBean(name, requiredType);
	}

	public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
		return getRawContext().getBeanNamesForType(type, includeNonSingletons,
				allowEagerInit);
	}

	public <T> T getBean(Class<T> requiredType) throws BeansException {
		return getRawContext().getBean(requiredType);
	}

	public Object getBean(String name, Object... args) throws BeansException {
		return getRawContext().getBean(name, args);
	}

	public <T> Map<String, T> getBeansOfType(Class<T> type)
			throws BeansException {
		return getRawContext().getBeansOfType(type);
	}

	public <T> T getBean(Class<T> requiredType, Object... args)
			throws BeansException {
		return getRawContext().getBean(requiredType, args);
	}

	public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons,
			boolean allowEagerInit) throws BeansException {
		return getRawContext().getBeansOfType(type, includeNonSingletons, allowEagerInit);
	}

	public boolean containsBean(String name) {
		return getRawContext().containsBean(name);
	}

	public boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		return getRawContext().isSingleton(name);
	}

	public boolean isPrototype(String name)
			throws NoSuchBeanDefinitionException {
		return getRawContext().isPrototype(name);
	}

	public String[] getBeanNamesForAnnotation(
			Class<? extends Annotation> annotationType) {
		return getRawContext().getBeanNamesForAnnotation(annotationType);
	}

	public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType)
			throws BeansException {
		return getRawContext().getBeansWithAnnotation(annotationType);
	}

	public boolean isTypeMatch(String name, ResolvableType typeToMatch)
			throws NoSuchBeanDefinitionException {
		return getRawContext().isTypeMatch(name, typeToMatch);
	}

	public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
			throws NoSuchBeanDefinitionException {
		return getRawContext().findAnnotationOnBean(beanName, annotationType);
	}

	public boolean isTypeMatch(String name, Class<?> typeToMatch)
			throws NoSuchBeanDefinitionException {
		return getRawContext().isTypeMatch(name, typeToMatch);
	}

	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return getRawContext().getType(name);
	}

	public String[] getAliases(String name) {
		return getRawContext().getAliases(name);
	}
}
