package com.phantom.seckill.kaptcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
public class KaptchaConfig {

		@Bean
	    public DefaultKaptcha getKaptchaBean()
	    {
	        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
	        Properties properties = new Properties();
	        properties.setProperty("kaptcha.border", "yes");
	        properties.setProperty("kaptcha.border.color", "105,179,90");
	        properties.setProperty("kaptcha.textproducer.font.color", "blue");
	        properties.setProperty("kaptcha.image.width", "160");
	        properties.setProperty("kaptcha.image.height", "60");
	        properties.setProperty("kaptcha.textproducer.font.size", "28");
	        properties.setProperty("kaptcha.session.key", "kaptchaCode");
	        properties.setProperty("kaptcha.textproducer.char.spac", "35");
	        properties.setProperty("kaptcha.textproducer.char.length", "5");
	        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
	        properties.setProperty("kaptcha.noise.color", "white");
	        Config config = new Config(properties);
	        defaultKaptcha.setConfig(config);
	        return defaultKaptcha;
	    }


}
