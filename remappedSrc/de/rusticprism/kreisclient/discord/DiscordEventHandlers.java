package de.rusticprism.kreisclient.discord;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DiscordEventHandlers extends Structure {
        /**
         * Handler function for the ready event
         */
        public interface OnReady extends Callback
        {
            void accept(DiscordUser user);
        }

        /**
         * Handler function for the exceptional events (error, disconnect)
         */
        public interface OnStatus extends Callback
        {
            void accept(int errorCode, String message);
        }

        /**
         * Handler function for game update events (joinGame, spectateGame)
         */
        public interface OnGameUpdate extends Callback
        {
            void accept(String secret);
        }

        /**
         * Handler function for user join requests
         */
        public interface OnJoinRequest extends Callback
        {
            void accept(DiscordUser request);
        }

        private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
                "ready",
                "disconnected",
                "errored",
                "joinGame",
                "spectateGame",
                "joinRequest"
        ));

        /**
         * Called when the RPC connection has been established
         */
        public DiscordEventHandlers.OnReady ready;
        /**
         * Called when the RPC connection has been severed
         */
        public DiscordEventHandlers.OnStatus disconnected;
        /**
         * Called when an internal error is caught within the SDK
         */
        public OnStatus errored;
        /**
         * Called when the logged in user joined a game
         */
        public DiscordEventHandlers.OnGameUpdate joinGame;
        /**
         * Called when the logged in user joined to spectate a game
         */
        public DiscordEventHandlers.OnGameUpdate spectateGame;
        /**
         * Called when another discord user wants to join the game of the logged in user
         */
        public DiscordEventHandlers.OnJoinRequest joinRequest;

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (!(o instanceof DiscordEventHandlers))
                return false;
            DiscordEventHandlers that = (DiscordEventHandlers) o;
            return Objects.equals(ready, that.ready)
                    && Objects.equals(disconnected, that.disconnected)
                    && Objects.equals(errored, that.errored)
                    && Objects.equals(joinGame, that.joinGame)
                    && Objects.equals(spectateGame, that.spectateGame)
                    && Objects.equals(joinRequest, that.joinRequest);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(ready, disconnected, errored, joinGame, spectateGame, joinRequest);
        }

        @Override
        protected List<String> getFieldOrder()
        {
            return FIELD_ORDER;
        }
}
