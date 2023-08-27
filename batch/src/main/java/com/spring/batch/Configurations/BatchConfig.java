package com.spring.batch.Configurations;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.spring.batch.models.User;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public FlatFileItemReader<User> reader(){
		FlatFileItemReader<User> reader=new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("records.csv"));
		 reader.setLinesToSkip(1);
		reader.setLineMapper(getLineMapper());
		return reader;
	}

	private LineMapper<User> getLineMapper() {
		DefaultLineMapper<User> lineMapper=new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] {"userId","firstName","lastName","country"});
		lineTokenizer.setIncludedFields(new int[] {0,1,2,4});
		lineMapper.setLineTokenizer(lineTokenizer);
		
		BeanWrapperFieldSetMapper<User> fieldSetter=new BeanWrapperFieldSetMapper<>();
		fieldSetter.setTargetType(User.class);
		lineMapper.setFieldSetMapper(fieldSetter);
		return lineMapper;
	}
	
	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<User> writer(){
		JdbcBatchItemWriter<User> writer=new JdbcBatchItemWriter<User>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setSql("insert into user(userId,firstName,lastName,country) values (:userId,:firstName,:lastName,:country)");
		writer.setDataSource(dataSource);
		return writer;
	}
	
	@Bean
	public Job importJob() {
		return this.jobBuilderFactory.get("User-Import-Job")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("step1")
				.<User,User>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	
}
