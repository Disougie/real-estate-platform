package com.disougie.imagekit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import io.imagekit.sdk.ImageKit;

@Configuration
public class ImageKitConfig {
	
	@Bean
	ImageKit imageKit() {
		Dotenv dotenv = Dotenv.load();
		ImageKit imageKit = ImageKit.getInstance();
		io.imagekit.sdk.config.Configuration configuration = 
				new io.imagekit.sdk.config.Configuration();
		
		configuration.setUrlEndpoint(dotenv.get("IMAGEKIT_URL"));
		configuration.setPublicKey(dotenv.get("IMAGEKIT_PUBLIC_KEY"));
		configuration.setPrivateKey(dotenv.get("IMAGEKIT_PRIVATE_KEY"));
		imageKit.setConfig(configuration);
		
		return imageKit;
	}

}
