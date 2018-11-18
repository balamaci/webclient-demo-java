package com.balamaci.flux.webclientdemo;

import com.balamaci.flux.webclientdemo.order.Order;
import com.balamaci.flux.webclientdemo.user.User;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class WebclientDemoApplicationTests extends BaseWebclientTest {

	@LocalServerPort
	private Long port;

	private WebClient webClientBuilder() {
		return WebClient.builder()
				.baseUrl("http://localhost:" + port)
				.build();
	}

	@Test
	public void retrieveUsers() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		Flux<User> users = webClientBuilder()
				.get()
				.uri("/users")
				.retrieve()
				.bodyToFlux(User.class);

		users.subscribe(logNext(), logError(latch), logComplete(latch));
		latch.await();
	}

	@Test
	public void retrieveAllUsersOrders() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		WebClient webClient = webClientBuilder();

		Flux<User> users = webClient
				.get()
				.uri("/users")
				.retrieve()
				.bodyToFlux(User.class);

		Flux<Pair<User, Order>> orders = users.flatMap(user -> webClient
				.get()
				.uri("/orders/{username}", user.getUsername())
				.retrieve()
				.bodyToFlux(Order.class)
				.defaultIfEmpty(Order.NOT_FOUND)
				.map(order -> new Pair<>(user, order))
		);

		users.subscribe(logNext(), logError(latch), logComplete(latch));
		latch.await();
	}

	@Test
	public void retrieveAllUsersIds() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		WebClient webClient = webClientBuilder();

		Mono<String> bannedUser = Mono.justOrEmpty(User.BANNED_USER.getUsername());

		Flux<String> usersIds = webClient
				.get()
				.uri("/users/ids")
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<String>>(){})
				.flatMapMany(Flux::fromIterable)
				.doOnNext(userId -> log.info("Received username={}", userId))
				.take(2);

		usersIds = bannedUser.concatWith(usersIds);

		Flux<User> users = usersIds
				.flatMap(userId -> webClient
						.get()
						.uri("/users/{username}", userId)
						.retrieve()
						.bodyToFlux(User.class)
				);

		users.subscribe(logNext(), logError(latch), logComplete(latch));
		latch.await();
	}

}
