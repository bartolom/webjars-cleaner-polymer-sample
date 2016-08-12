package eu.codesociety.webjars.cleaner.polymer.sample;

import java.util.SortedMap;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;
import org.webjars.WebJarAssetLocator;

import eu.codesociety.webjars.cleaner.polymer.PackForPolymer;
import eu.codesociety.webjars.cleaner.polymer.PackForSaulis;

@Configuration
public class AppConfigMvc extends WebMvcConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AppConfigMvc.class);
	
	@Bean
	public WebJarAssetLocator webJarAssetLocator() {
		SortedMap<String, String> index = WebJarAssetLocator.getFullPathIndex(
				Pattern.compile(".*"), 
				AppConfigMvc.class.getClassLoader());
		
		logger.info("Full index size {}", index.size());
		Function<String, String> f = a -> (a.equals("polymer")) ? "github-com-Polymer-polymer" : a;
		
		SortedMap<String, String> cleanedIndex = 
				(new PackForPolymer())
						.andThen(new PackForSaulis())
						.apply(index);
		
		logger.info("Reduced from {} to {}", index.size(), cleanedIndex.size());
		cleanedIndex.forEach((k, v) -> logger.info("  - {}", k));
		
		logger.info("Cleaned index size {}", cleanedIndex.size());
		
		WebJarAssetLocator locator = new WebJarAssetLocator(cleanedIndex, a -> f.apply(a));
		return locator;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		WebJarsResourceResolver resolver = new WebJarsResourceResolver(webJarAssetLocator());
		
		registry.addResourceHandler("/bower_components/**")
				.addResourceLocations("classpath:META-INF/resources/webjars/")
				.resourceChain(true)
				.addResolver(resolver)
				.addResolver(new PathResourceResolver());
	}
	
	
}
