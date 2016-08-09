package eu.codesociety.webjars.cleaner.polymer.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;
import org.webjars.WebJarAssetLocator;

@Configuration
public class AppConfigMvc extends WebMvcConfigurerAdapter {

	@Bean
	public WebJarAssetLocator webJarAssetLocator() {
		WebJarAssetLocator locator = new WebJarAssetLocator();
		return locator;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		WebJarsResourceResolver resolver = new WebJarsResourceResolver(webJarAssetLocator());
		
		registry.addResourceHandler("/bower/**")
				.addResourceLocations("classpath:META-INF/resources/webjars/")
				.resourceChain(true)
				.addResolver(resolver)
				.addResolver(new PathResourceResolver());
	}
	
	
}
