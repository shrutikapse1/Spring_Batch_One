package com.spring.batch.Configurations;

import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.models.User;

public class UserItemProcessor implements ItemProcessor<User, User> {

	@Override
	public User process(User user) throws Exception {
		
		return user;
	}

}
