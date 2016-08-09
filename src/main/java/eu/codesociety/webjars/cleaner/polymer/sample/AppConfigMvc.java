package eu.codesociety.webjars.cleaner.polymer.sample;

import java.util.SortedMap;
import java.util.function.Function;
import java.util.regex.Pattern;

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
		SortedMap<String, String> index = WebJarAssetLocator.getFullPathIndex(
				Pattern.compile(".*"), 
				WebJarAssetLocator.class.getClassLoader());
		Function<String, String> f = a -> (a.equals("polymer")) ? "github-com-Polymer-polymer" : a;
		WebJarAssetLocator locator = new WebJarAssetLocator(index, a -> f.apply(a));
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
