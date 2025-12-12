package ttt_game_service.infrastructure;

import io.vertx.core.Vertx;
import ttt_game_service.application.*;

/**
 * 
 * Game Service including support for observability.
 * 
 */
public class GameServiceMain {

	static final int GAME_SERVICE_PORT = 9002;
    static final int PROMETHEUS_SERVER_PORT = 9400;
    
	public static void main(String[] args) {
		
		var service = new GameServiceImpl();
		service.bindGameRepository(new InMemoryGameRepository());

		/* setting up Prometheus observability server */
		PrometheusGameServiceObserver obs = null;
		try {
			obs = new PrometheusGameServiceObserver(PROMETHEUS_SERVER_PORT);
			service.addObserver(obs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		var vertx = Vertx.vertx();
		var server = new GameServiceController(service, GAME_SERVICE_PORT);
		vertx.deployVerticle(server);		
	}

}

