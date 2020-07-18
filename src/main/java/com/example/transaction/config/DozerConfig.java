package com.example.transaction.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfig {
	@Bean
	public Mapper mapper() {
		final DozerBeanMapper mapper = new DozerBeanMapper();
		final BeanMappingBuilder builder = new BeanMappingBuilder() {
		    @Override
		    protected void configure() {
//		        mapping(Transaction.class, TransactionDto.class);
		    }
		};
		mapper.addMapping(builder);
	    return mapper;
	}

}
