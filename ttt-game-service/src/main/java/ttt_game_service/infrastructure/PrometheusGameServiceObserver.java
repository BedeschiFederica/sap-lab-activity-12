package ttt_game_service.infrastructure;

import common.exagonal.Adapter;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import ttt_game_service.application.GameServiceEvent;
import ttt_game_service.application.GameServiceEventObserver;
import ttt_game_service.application.NewGameCreatedEvent;
import ttt_game_service.domain.GameEnded;
import ttt_game_service.domain.GameEvent;
import ttt_game_service.domain.GameObserver;
import ttt_game_service.domain.GameStarted;
import ttt_game_service.domain.NewMove;

@Adapter
public class PrometheusGameServiceObserver implements GameServiceEventObserver, GameObserver {

	private Counter nTotalGamesCreated;
	private Gauge	nCurrentNumberOfGames;
	private HTTPServer promServer;
	
	public PrometheusGameServiceObserver(int port) throws ObsMetricServerException {
		JvmMetrics.builder().register(); 
		
		/* total number of games created so far - monotonic increasing  */
		
		nTotalGamesCreated = Counter.builder()
	    		    .name("gs_num_game_total")
	    		    .help("Total number of games created")
	    		    .register();

		/* number of ongoing/active games - can decrease */
		
		nCurrentNumberOfGames = Gauge.builder()
					.name("gs_num_ongoing_games")
					.help("gs_num_ongoing_games")
					.register();
	    try {
	    	promServer = HTTPServer.builder()
		    .port(port)
		    .buildAndStart();
	    } catch (Exception ex) {
	    	throw new ObsMetricServerException();
	    }
	}
	

	@Override
	public synchronized void notifyNewGameCreated(String gameId) {
		nTotalGamesCreated.inc();
		log("new game created - " + nTotalGamesCreated.get());

	}

	@Override
	public synchronized void notifyGameEvent(GameEvent ev) {
		if (ev instanceof GameStarted) {
			
			var num = nCurrentNumberOfGames.get();
			nCurrentNumberOfGames.set(num + 1);
			log("new game started event - " + nCurrentNumberOfGames.get());
		} else if (ev instanceof GameEnded) {
			var num = nCurrentNumberOfGames.get();
			nCurrentNumberOfGames.set(num - 1);
			log("new game completed event - " + nCurrentNumberOfGames.get());
		}	
	}
	
	private void log(String msg) {
		System.out.println("[PROM] " + msg);
	}
}