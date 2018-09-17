package controllers;

import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import play.api.libs.streams.ActorFlow;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.WebSocket;

import java.util.concurrent.CompletableFuture;

public class WebSocketController extends Controller {

    public WebSocket ws() {

//        return WebSocket.Text.acceptOrResult(request -> {
//            if(request.header("upgrade").isPresent()) {
//                Sink<String, ?> in = Sink.foreach(System.out::println);

                // Send a single 'Hello!' message and then leave the socket open
//                Source<String, ?> out = Source.single("Hello!").concat(Source.maybe());

//                return F.Either.Right(CompletableFuture.completedFuture(Flow.fromSinkAndSource(in, out)))

//            } else {
//                return F.Either.Left(CompletableFuture.completedFuture(ok()));
//            }
//            // Log events to the console
//        });

        return WebSocket.Text.acceptOrResult(request -> {
            if (session().get("user") != null) {
                Sink<String, ?> in = Sink.foreach(System.out::println);

                // Send a single 'Hello!' message and then leave the socket open
                Source<String, ?> out = Source.single("Hello!").concat(Source.maybe());

                return CompletableFuture.completedFuture(F.Either.Right(Flow.fromSinkAndSource(in, out)));


//                return CompletableFuture.completedFuture(
//                        F.Either.Right(ActorFlow.actorRef(MyWebSocketActor::props,
//                                actorSystem, materializer)));
            } else {
                return CompletableFuture.completedFuture(F.Either.Left(forbidden()));
            }
        });

    }
}
