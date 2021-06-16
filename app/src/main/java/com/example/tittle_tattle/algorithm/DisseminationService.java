package com.example.tittle_tattle.algorithm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * DisseminationService. This Service has 3 {@link State}s.
 *
 * <p>{@link State#UNKNOWN}: We cannot do anything while we're in this state. The app is likely in
 * the background. TODO most probably not useful
 *
 * <p>{@link State#SEARCHING}: Our default state (after we've connected). We constantly listen for a
 * device to advertise near us, while simultaneously advertising ourselves.
 *
 * <p>{@link State#CONNECTED}: We've connected to another device and can now talk to them by holding
 * down the volume keys and speaking into the phone. Advertising and discovery have both stopped.
 */
public class DisseminationService extends Service {
//    private Looper serviceLooper;
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    private static final String SERVICE_ID = "com.example.tittle_tattle.SERVICE_ID";

    /** The state of the app. As the app changes states, advertising/discovery will start/stop. */
    private State mState = State.UNKNOWN;

    /** A random UID used as this device's endpoint name. */
    private String mName;

    /** Our handler to Nearby Connections. */
    private ConnectionsClient mConnectionsClient;

    /** The devices we've discovered near us. */
    private final Map<String, Endpoint> mDiscoveredEndpoints = new HashMap<>();

    /**
     * The devices we have pending connections to. They will stay pending until we call {@link
     * #acceptConnection(Endpoint)} or {@link #rejectConnection(Endpoint)}.
     */
    private final Map<String, Endpoint> mPendingConnections = new HashMap<>();

    /**
     * The devices we are currently connected to. For advertisers, this may be large. For discoverers,
     * there will only be one entry in this map.
     */
    private final Map<String, Endpoint> mEstablishedConnections = new HashMap<>();

    /**
     * True if we are asking a discovered device to connect to us. While we ask, we cannot ask another
     * device.
     */
    private boolean mIsConnecting = false;

    /** True if we are discovering. */
    private boolean mIsDiscovering = false;

    /** True if we are advertising. */
    private boolean mIsAdvertising = false;

    @Override
    public void onCreate() {
//        HandlerThread thread = new HandlerThread("DisseminationService",
//                Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//
//        serviceLooper = thread.getLooper();
        // TODO nu stiu daca mai trebuie facut ceva
        mConnectionsClient = Nearby.getConnectionsClient(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // TODO sau START_REDELIVER_INTENT
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        // TODO maybe sth done here?
    }

    /** Callbacks for connections to other devices. */
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
        new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NotNull String endpointId, ConnectionInfo connectionInfo) {
                Log.i("[NEARBY]", String.format("onConnectionInitiated(endpointId=%s, endpointName=%s)",
                                                    endpointId, connectionInfo.getEndpointName()));

                Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
                mPendingConnections.put(endpointId, endpoint);
                DisseminationService.this.onConnectionInitiated(endpoint, connectionInfo);
            }

            @Override
            public void onConnectionResult(@NotNull String endpointId, @NotNull ConnectionResolution result) {
                Log.i("[NEARBY]", String.format("onConnectionResponse(endpointId=%s, result=%s)", endpointId, result));

                // We're no longer connecting
                mIsConnecting = false;

                if (!result.getStatus().isSuccess()) {
                    Log.w("[NEARBY]", String.format("Connection failed. Received status %s.",
                                                DisseminationService.toString(result.getStatus())));

                    onConnectionFailed(mPendingConnections.remove(endpointId));
                    return;
                }

                connectedToEndpoint(mPendingConnections.remove(endpointId));
            }

            @Override
            public void onDisconnected(@NotNull String endpointId) {
                if (!mEstablishedConnections.containsKey(endpointId)) {
                    Log.i("[NEARBY]", "Unexpected disconnection from endpoint " + endpointId);
                    return;
                }

                disconnectedFromEndpoint(mEstablishedConnections.get(endpointId));
            }
        };

    /** Callbacks for payloads (bytes of data) sent from another device to us. */
    private final PayloadCallback mPayloadCallback =
        new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NotNull String endpointId, @NotNull Payload payload) {
                Log.i("[NEARBY]", String.format("onPayloadReceived(endpointId=%s, payload=%s)", endpointId, payload));

                onReceive(mEstablishedConnections.get(endpointId), payload);
            }

            @Override
            public void onPayloadTransferUpdate(@NotNull String endpointId, @NotNull PayloadTransferUpdate update) {
                Log.i("[NEARBY]", String.format("onPayloadTransferUpdate(endpointId=%s, update=%s)", endpointId, update));
            }
        };

    /**
     * Sets the device to advertising mode. It will broadcast to other devices in discovery mode.
     * Either {@link #onAdvertisingStarted()} or {@link #onAdvertisingFailed()} will be called once
     * we've found out if we successfully entered this mode.
     */
    protected void startAdvertising() {
        mIsAdvertising = true;
        final String localEndpointName = getName();

        AdvertisingOptions.Builder advertisingOptions = new AdvertisingOptions.Builder();
        advertisingOptions.setStrategy(getStrategy());

        mConnectionsClient
            .startAdvertising(localEndpointName, getServiceId(), mConnectionLifecycleCallback, advertisingOptions.build())

            .addOnSuccessListener(unusedResult -> {
                Log.i("[NEARBY]","Now advertising endpoint " + localEndpointName);
                onAdvertisingStarted();
            })

            .addOnFailureListener(e -> {
                mIsAdvertising = false;
                Log.w("[NEARBY]", "startAdvertising() failed.", e);
                onAdvertisingFailed();
            });
    }

    /** Stops advertising. */
    protected void stopAdvertising() {
        mIsAdvertising = false;
        mConnectionsClient.stopAdvertising();
    }

    /** Returns {@code true} if currently advertising. */
    protected boolean isAdvertising() {
        return mIsAdvertising;
    }

    /** Called when advertising successfully starts. */
    protected void onAdvertisingStarted() {
        // TODO
    }

    /** Called when advertising fails to start. */
    protected void onAdvertisingFailed() {
        // TODO
    }

    /**
     * Called when a pending connection with a remote endpoint is created. Use {@link ConnectionInfo}
     * for metadata about the connection (like incoming vs outgoing, or the authentication token). If
     * we want to continue with the connection, call {@link #acceptConnection(Endpoint)}. Otherwise,
     * call {@link #rejectConnection(Endpoint)}.
     */
    protected void onConnectionInitiated(Endpoint endpoint, ConnectionInfo connectionInfo) {
        acceptConnection(endpoint);
    }

    /** Accepts a connection request. */
    protected void acceptConnection(@NotNull final Endpoint endpoint) {
        mConnectionsClient
            .acceptConnection(endpoint.getId(), mPayloadCallback)
            .addOnFailureListener(e -> Log.w("[NEARBY]", "acceptConnection() failed.", e));
    }

    /** Rejects a connection request. */
    protected void rejectConnection(@NotNull Endpoint endpoint) {
        mConnectionsClient
            .rejectConnection(endpoint.getId())
            .addOnFailureListener(e -> Log.w("[NEARBY]", "rejectConnection() failed.", e));
    }

    /**
     * Sets the device to discovery mode. It will now listen for devices in advertising mode. Either
     * {@link #onDiscoveryStarted()} or {@link #onDiscoveryFailed()} will be called once we've found
     * out if we successfully entered this mode.
     */
    protected void startDiscovering() {
        mIsDiscovering = true;
        mDiscoveredEndpoints.clear();

        DiscoveryOptions.Builder discoveryOptions = new DiscoveryOptions.Builder();
        discoveryOptions.setStrategy(getStrategy());

        mConnectionsClient
            .startDiscovery(getServiceId(), new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(@NotNull String endpointId, @NotNull DiscoveredEndpointInfo info) {
                    Log.i("[NEARBY]", String.format("onEndpointFound(endpointId=%s, serviceId=%s, endpointName=%s)",
                                                    endpointId, info.getServiceId(), info.getEndpointName()));

                    if (getServiceId().equals(info.getServiceId())) {
                        Endpoint endpoint = new Endpoint(endpointId, info.getEndpointName());
                        mDiscoveredEndpoints.put(endpointId, endpoint);

                        onEndpointDiscovered(endpoint);
                    }
                }

                @Override
                public void onEndpointLost(@NotNull String endpointId) {
                    Log.i("[NEARBY]", String.format("onEndpointLost(endpointId=%s)", endpointId));
                }
            }, discoveryOptions.build())

            .addOnSuccessListener(unusedResult -> onDiscoveryStarted())

            .addOnFailureListener(e -> {
                Log.w("[NEARBY]", "startDiscovering() failed.", e);

                mIsDiscovering = false;
                onDiscoveryFailed();
            });
    }

    /** Stops discovery. */
    protected void stopDiscovering() {
        mIsDiscovering = false;
        mConnectionsClient.stopDiscovery();
    }

    /** Returns {@code true} if currently discovering. */
    protected boolean isDiscovering() {
        return mIsDiscovering;
    }

    /** Called when discovery successfully starts. Override this method to act on the event. */
    protected void onDiscoveryStarted() {
        // TODO
    }

    /** Called when discovery fails to start. Override this method to act on the event. */
    protected void onDiscoveryFailed() {
        // TODO
    }

    /**
     * Called when a remote endpoint is discovered. To connect to the device, call {@link
     * #connectToEndpoint(Endpoint)}.
     */
    protected void onEndpointDiscovered(Endpoint endpoint) {
        stopDiscovering();
        connectedToEndpoint(endpoint);
    }

    /** Disconnects from the given endpoint. */
    protected void disconnect(@NotNull Endpoint endpoint) {
        mConnectionsClient.disconnectFromEndpoint(endpoint.getId());
        mEstablishedConnections.remove(endpoint.getId());
    }

    /** Disconnects from all currently connected endpoints. */
    protected void disconnectFromAllEndpoints() {
        for (Endpoint endpoint : mEstablishedConnections.values()) {
            mConnectionsClient.disconnectFromEndpoint(endpoint.getId());
        }

        mEstablishedConnections.clear();
    }

    /** Resets and clears all state in Nearby Connections. */
    protected void stopAllEndpoints() {
        mConnectionsClient.stopAllEndpoints();
        mIsAdvertising = false;
        mIsDiscovering = false;
        mIsConnecting = false;
        mDiscoveredEndpoints.clear();
        mPendingConnections.clear();
        mEstablishedConnections.clear();
    }

    /**
     * Sends a connection request to the endpoint. Either {@link #onConnectionInitiated(Endpoint,
     * ConnectionInfo)} or {@link #onConnectionFailed(Endpoint)} will be called once we've found out
     * if we successfully reached the device.
     */
    protected void connectToEndpoint(@NotNull final Endpoint endpoint) {
        Log.v("[NEARBY]", "Sending a connection request to endpoint " + endpoint);
        // Mark ourselves as connecting so we don't connect multiple times
        mIsConnecting = true;

        // Ask to connect
        mConnectionsClient
            .requestConnection(getName(), endpoint.getId(), mConnectionLifecycleCallback)

            .addOnFailureListener(e -> {
                Log.w("[NEARBY]", "requestConnection() failed. ", e);

                mIsConnecting = false;
                onConnectionFailed(endpoint);
            });
    }

    /** Returns {@code true} if we're currently attempting to connect to another device. */
    protected final boolean isConnecting() {
        return mIsConnecting;
    }

    private void connectedToEndpoint(Endpoint endpoint) {
        Log.i("[NEARBY]", String.format("connectedToEndpoint(endpoint=%s)", endpoint));

        mEstablishedConnections.put(endpoint.getId(), endpoint);
        onEndpointConnected(endpoint);
    }

    private void disconnectedFromEndpoint(Endpoint endpoint) {
        Log.i("[NEARBY]", String.format("disconnectedFromEndpoint(endpoint=%s)", endpoint));

        mEstablishedConnections.remove(endpoint.getId());
        onEndpointDisconnected(endpoint);
    }

    /** Called when a connection with this endpoint has failed. */
    protected void onConnectionFailed(Endpoint endpoint) {
        // Let's try someone else.
        if (getState() == State.SEARCHING) {
            startDiscovering();
        }
    }

    /** Called when someone has connected to us. */
    protected void onEndpointConnected(@NotNull Endpoint endpoint) {
        Toast.makeText(this, "Connected: " + endpoint.getName(), Toast.LENGTH_SHORT).show();
        setState(State.CONNECTED);
    }

    /** Called when someone has disconnected. */
    protected void onEndpointDisconnected(@NotNull Endpoint endpoint) {
        Toast.makeText(this, "Disconnected: " +endpoint.getName(), Toast.LENGTH_SHORT).show();
        setState(State.SEARCHING);
    }

    /**
     * Sends a {@link Payload} to all currently connected endpoints.
     *
     * @param payload The data you want to send.
     */
    protected void send(Payload payload) {
        send(payload, mEstablishedConnections.keySet());
    }

    private void send(Payload payload, Set<String> endpoints) {
        mConnectionsClient
            .sendPayload(new ArrayList<>(endpoints), payload)
            .addOnFailureListener(e -> Log.w("[NEARBY]", "sendPayload() failed.", e));
    }

    /**
     * Someone connected to us has sent us data.
     *
     * @param endpoint The sender.
     * @param payload The data.
     */
    protected void onReceive(Endpoint endpoint, Payload payload) {
        // TODO aici adaug mesajul in baza de date si in lista utilizatorului
//        if (payload.getType() == Payload.Type.STREAM) {
//            if (mAudioPlayer != null) {
//                mAudioPlayer.stop();
//                mAudioPlayer = null;
//            }
//
//            AudioPlayer player =
//                    new AudioPlayer(payload.asStream().asInputStream()) {
//                        @WorkerThread
//                        @Override
//                        protected void onFinish() {
//                            runOnUiThread(
//                                    new Runnable() {
//                                        @UiThread
//                                        @Override
//                                        public void run() {
//                                            mAudioPlayer = null;
//                                        }
//                                    });
//                        }
//                    };
//            mAudioPlayer = player;
//            player.start();
//        }
    }

    /**
     * The state has changed. I wonder what we'll be doing now.
     *
     * @param state The new state.
     */
    private void setState(State state) {
        if (mState == state) {
            Log.w("NEARBY", "State set to " + state + " but already in that state");
            return;
        }

        Log.i("NEARBY", "State set to " + state);
        State oldState = mState;
        mState = state;
        onStateChanged(oldState, state);
    }

    /** @return The current state. */
    private State getState() {
        return mState;
    }

    /**
     * State has changed.
     *
     * @param oldState The previous state we were in. Clean up anything related to this state.
     * @param newState The new state we're now in. Prepare the UI for this state.
     */
    private void onStateChanged(State oldState, @NotNull State newState) {
        // Update Nearby Connections to the new state.
        switch (newState) {
            case SEARCHING:
                disconnectFromAllEndpoints();
                startDiscovering();
                startAdvertising();
                break;
            case CONNECTED:
                stopDiscovering();
                stopAdvertising();
                break;
            case UNKNOWN:
                stopAllEndpoints();
                break;
            default:
                // no-op
                break;
        }
    }

    /** Returns the client's name. Visible to others when connecting. */
    protected String getName() {
        return mName;
    }

    /**
     * Returns the service id. This represents the action this connection is for. When discovering,
     * we'll verify that the advertiser has the same service id before we consider connecting to them.
     */
    protected String getServiceId() {
        return SERVICE_ID;
    }

    /**
     * Returns the strategy we use to connect to other devices. Only devices using the same strategy
     * and service id will appear when discovering. Stragies determine how many incoming and outgoing
     * connections are possible at the same time, as well as how much bandwidth is available for use.
     */
    protected Strategy getStrategy() {
        return STRATEGY;
    }

    /** Returns a list of currently connected endpoints. */
    protected Set<Endpoint> getDiscoveredEndpoints() {
        return new HashSet<>(mDiscoveredEndpoints.values());
    }

    /** Returns a list of currently connected endpoints. */
    protected Set<Endpoint> getConnectedEndpoints() {
        return new HashSet<>(mEstablishedConnections.values());
    }

    /**
     * Transforms a {@link Status} into a English-readable message for logging.
     *
     * @param status The current status
     * @return A readable String. eg. [404]File not found.
     */
    @NotNull
    private static String toString(@NotNull Status status) {
        return String.format(Locale.US, "[%d]%s", status.getStatusCode(),
            status.getStatusMessage() != null ? status.getStatusMessage()
                                              : ConnectionsStatusCodes.getStatusCodeString(status.getStatusCode()));
    }

    /** Represents a device we can talk to. */
    protected static class Endpoint {
        @NonNull
        private final String id;
        @NonNull
        private final String name;

        private Endpoint(@NonNull String id, @NonNull String name) {
            this.id = id;
            this.name = name;
        }

        @NonNull
        public String getId() {
            return id;
        }

        @NonNull
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Endpoint) {
                Endpoint other = (Endpoint) obj;
                return id.equals(other.id);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @NotNull
        @Override
        public String toString() {
            return String.format("Endpoint{id=%s, name=%s}", id, name);
        }
    }

    /** States that the UI goes through. */
    public enum State {
        UNKNOWN,
        SEARCHING,
        CONNECTED
    }
}
