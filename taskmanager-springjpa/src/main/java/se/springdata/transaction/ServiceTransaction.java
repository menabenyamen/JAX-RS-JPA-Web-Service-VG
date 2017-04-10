package se.springdata.transaction;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class ServiceTransaction {

	@Transactional
	public <T> T execute(Action<T> action) {
		return action.execute();
	}

	@FunctionalInterface
	public static interface Action<T> {
		T execute();
	}
}
