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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
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
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.filter(logResponseFilter())
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

		orders.subscribe(logNext(), logError(latch), logComplete(latch));
		latch.await();
	}

	@Test
	public void retrieveAllUsersIds() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		WebClient webClient = webClientBuilder();

		Mono<String> bannedUser = Mono.just(User.BANNED_USER.getUsername());

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

	@Test
	public void retrieveNonExistentUser() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);

		String userId = "potter";
		webClientBuilder()
				.get()
				.uri("/users/{username}", userId)
				.retrieve()
//				.onStatus(HttpStatus::is4xxClientError,
//						clientResponse -> {
//
//							String response = clientResponse.bodyToMono(String.class);
//						})
				.bodyToFlux(User.class)
				.subscribe(logNext(), logError(latch), logComplete(latch));
		latch.await();
	}

	@Test
	public void addInvalidUser() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);

		User bannedUser = User.BANNED_USER;
		webClientBuilder()
				.post()
				.uri("/users")
				.body(Mono.just(bannedUser), User.class)
				.exchange()
				.subscribe(logNext(), logError(latch), logComplete(latch));
		latch.await();
	}

	@Test
	public void addDuplicateUser() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);

		User existingUser = new User("tyrion", "Evil Tyrion");
		webClientBuilder()
				.post()
				.uri("/users")
				.body(Mono.just(existingUser), User.class)
				.exchange()
				.subscribe(logNext(), logError(latch), logComplete(latch));
		latch.await();
	}

	private ExchangeFilterFunction logResponseFilter() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			log.info("Received response statusCode={}", clientResponse.statusCode());
			return Mono.just(clientResponse);
		});
	}

}
