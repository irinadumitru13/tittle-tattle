package com.example.tittle_tattle.algorithm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tittle_tattle.algorithm.proto.Contact;
import com.example.tittle_tattle.algorithm.proto.EncounteredNodes;
import com.example.tittle_tattle.algorithm.proto.History;
import com.example.tittle_tattle.algorithm.proto.Interests;
import com.example.tittle_tattle.algorithm.proto.MessageExch;
import com.example.tittle_tattle.algorithm.proto.SocialNetwork;
import com.example.tittle_tattle.data.models.Message;
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
import com.google.protobuf.InvalidProtocolBufferException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * DisseminationService. This Service has 3 {@link State}s.
 *
 * <p>{@link State#UNKNOWN}: We cannot do anything while we're in this state. The app is likely in
 * the background.
 *
 * <p>{@link State#SEARCHING}: Our default state (after we've connected). We constantly listen for a
 * device to advertise near us, while simultaneously advertising ourselves.
 *
 * <p>{@link State#CONNECTED}: We've connected to another device and can now talk to them by holding
 * down the volume keys and speaking into the phone. Advertising and discovery have both stopped.
 */
public class DisseminationService extends Service {
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    private static final String SERVICE_ID = "com.example.tittle_tattle.SERVICE_ID";

    /** The state of the app. As the app changes states, advertising/discovery will start/stop. */
    private State mState = State.UNKNOWN;

    /** A random UID used as this device's endpoint name. */
    private String mName;

    /** Our handler to Nearby Connections. */
    private ConnectionsClient mConnectionsClient;

    /**
     * True if we are asking a discovered device to connect to us. While we ask, we cannot ask another
     * device.
     */
    private boolean mIsConnecting = false;

    /** True if we are discovering. */
    private boolean mIsDiscovering = false;

    /** True if we are advertising. */
    private boolean mIsAdvertising = false;

    /** Normalization value for friendship. */
    private double maxFriendship = Double.MIN_VALUE;

    /** Normalization value for similarity. */
    private double maxSimilarity = Double.MIN_VALUE;

    /** Normalization value for contacts. */
    private double maxContacts = Double.MIN_VALUE;

    /** Aggregation weights. */
    private final double w1 = 0.25, w2 = 0.25, w3 = 0.25, w4 = 0.25;

    /** Social network threshold. */
    private double socialNetworkThreshold;

    /** Interest threshold. */
    private double interestThreshold;

    /** Contacts threshold. */
    private int contactsThreshold;

    /** How many contacts has this node made. */
    private long encounters = 0;

    /** The devices we've discovered near us. */
    private final Map<String, Endpoint> mDiscoveredEndpoints = new HashMap<>();

    /**
     * The devices we have pending connections to. They will stay pending until we call {@link
     * #acceptConnection(Endpoint)} or {@link #rejectConnection(Endpoint)}.
     */
    private final Map<String, Endpoint> mPendingConnections = new HashMap<>();

    /**
     * The devices we are currently connected to. First interaction means history exchange.
     * For advertisers, this may be large. For discoverers, there will only be one entry in this map.
     */
    private final Map<String, Endpoint> mHistoryExchangeConnections = new HashMap<>();

    /**
     * The devices we are currently connected to. After history exchange, messages can be exchanged.
     * For advertisers, this may be large. For discoverers, there will only be one entry in this map.
     */
    private final Map<String, Endpoint> mMessageExchangeConnections = new HashMap<>();

    /** List of nodes encountered by the current node during a time window. */
    private final Map<Long, ContactInfo> encounteredNodes = new HashMap<>();

    /** Social network */
    private final Set<Long> socialNetwork = new HashSet<>();

    /** Interests */
    private final HashSet<Integer> interests = new HashSet<>();

    /** Messages to be transmitted */
    private final List<Message> messages = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO extract data from db
        Log.i("[NEARBY]", "Service onCreate()");

        mName = String.valueOf(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("user_id", 0));

        mConnectionsClient = Nearby.getConnectionsClient(this);
        setState(State.SEARCHING);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "tittle-tattle service starting", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "tittle-tattle service done", Toast.LENGTH_SHORT).show();
        // TODO put data in db
    }

    /** Callbacks for connections to other devices. */
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
        new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NotNull String endpointId, @NotNull ConnectionInfo connectionInfo) {
                Log.d("[NEARBY]", String.format("onConnectionInitiated(endpointId=%s, endpointName=%s)",
                                                    endpointId, connectionInfo.getEndpointName()));

                Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
                mPendingConnections.put(endpointId, endpoint);

                DisseminationService.this.onConnectionInitiated(endpoint, connectionInfo);
            }

            @Override
            public void onConnectionResult(@NotNull String endpointId, @NotNull ConnectionResolution result) {
                Log.d("[NEARBY]", String.format("onConnectionResponse(endpointId=%s, result=%s)", endpointId, result));

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
                if (!mHistoryExchangeConnections.containsKey(endpointId) ||
                        !mMessageExchangeConnections.containsKey(endpointId)) {
                    Log.i("[NEARBY]", "Unexpected disconnection from endpoint " + endpointId);
                    return;
                }

                // TODO calculate duration with endpoint
                // TODO see from where to delete
                disconnectedFromEndpoint(mHistoryExchangeConnections.get(endpointId));
            }
        };

    /** Callbacks for discovering other devices. */
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback =
        new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(@NotNull String endpointId, @NotNull DiscoveredEndpointInfo info) {
                Log.d("[NEARBY]", String.format("onEndpointFound(endpointId=%s, serviceId=%s, endpointName=%s)",
                        endpointId, info.getServiceId(), info.getEndpointName()));

                if (getServiceId().equals(info.getServiceId())) {
                    Endpoint endpoint = new Endpoint(endpointId, info.getEndpointName());
                    mDiscoveredEndpoints.put(endpointId, endpoint);

                    onEndpointDiscovered(endpoint);
                }
            }

            @Override
            public void onEndpointLost(@NotNull String endpointId) {
                Log.d("[NEARBY]", String.format("onEndpointLost(endpointId=%s)", endpointId));
                mDiscoveredEndpoints.remove(endpointId);
            }
        };

    /** Callbacks for payloads (bytes of data) sent from another device to us. */
    private final PayloadCallback mPayloadCallback =
        new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NotNull String endpointId, @NotNull Payload payload) {
                Log.d("[NEARBY]", String.format("onPayloadReceived(endpointId=%s, payload=%s)", endpointId, payload));

                // received their history
                if (mHistoryExchangeConnections.containsKey(endpointId)) {
                    mMessageExchangeConnections.put(endpointId, mHistoryExchangeConnections.remove(endpointId));

                    onReceiveHistory(Objects.requireNonNull(mMessageExchangeConnections.get(endpointId)), payload);
                } else if (mMessageExchangeConnections.containsKey(endpointId)) {
                    onReceiveMessage(mMessageExchangeConnections.get(endpointId), payload);
                } else {
                    Log.w("[NEARBY]",
                            String.format("onPayloadReceived(endpointId=%s, payload=%s) from unknown endpoint",
                                    endpointId, payload));
                }
            }

            @Override
            public void onPayloadTransferUpdate(@NotNull String endpointId, @NotNull PayloadTransferUpdate update) {
                Log.d("[NEARBY]", String.format("onPayloadTransferUpdate(endpointId=%s, update=%s)", endpointId, update));
            }
        };

    /**
     * Sets the device to advertising mode. It will broadcast to other devices in discovery mode.
     * Either {@link #onAdvertisingStarted()} or {@link #onAdvertisingFailed(Exception e)} will be
     * called once we've found out if we successfully entered this mode.
     */
    protected void startAdvertising() {
        mIsAdvertising = true;
        final String localEndpointName = getName();

        AdvertisingOptions.Builder advertisingOptions = new AdvertisingOptions.Builder();
        advertisingOptions.setStrategy(getStrategy());

        mConnectionsClient
            .startAdvertising(localEndpointName, getServiceId(), mConnectionLifecycleCallback, advertisingOptions.build())

            .addOnSuccessListener(unusedResult -> onAdvertisingStarted())

            .addOnFailureListener(e -> {
                if (!Objects.equals(e.getMessage(), "8001: STATUS_ALREADY_ADVERTISING")) {
                    onAdvertisingFailed(e);
                }
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
        Log.d("[NEARBY]","Now advertising endpoint " + getName());
    }

    /** Called when advertising fails to start. */
    protected void onAdvertisingFailed(Exception e) {
        mIsAdvertising = false;
        Log.w("[NEARBY]", "startAdvertising() failed.", e);
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
        mConnectionsClient.acceptConnection(endpoint.getId(), mPayloadCallback)
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
     * {@link #onDiscoveryStarted()} or {@link #onDiscoveryFailed(Exception e)} will be called once we've found
     * out if we successfully entered this mode.
     */
    protected void startDiscovering() {
        mIsDiscovering = true;
        mDiscoveredEndpoints.clear();

        DiscoveryOptions.Builder discoveryOptions = new DiscoveryOptions.Builder();
        discoveryOptions.setStrategy(getStrategy());

        mConnectionsClient.startDiscovery(getServiceId(), mEndpointDiscoveryCallback, discoveryOptions.build())
            .addOnSuccessListener(unusedResult -> onDiscoveryStarted())

            .addOnFailureListener(e -> {
                if (!Objects.equals(e.getMessage(), "8002: STATUS_ALREADY_DISCOVERING")) {
                    onDiscoveryFailed(e);
                }
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
        Log.i("[NEARBY]", "Now endpoint " + getName() + "discovering");
    }

    /** Called when discovery fails to start. Override this method to act on the event. */
    protected void onDiscoveryFailed(Exception e) {
        Log.w("[NEARBY]", "startDiscovering() failed.", e);
        mIsDiscovering = false;
    }

    /**
     * Called when a remote endpoint is discovered. To connect to the device, call {@link
     * #connectToEndpoint(Endpoint)}.
     */
    protected void onEndpointDiscovered(Endpoint endpoint) {
        stopDiscovering();
        connectToEndpoint(endpoint);
    }

    /** Disconnects from the given endpoint. */
    protected void disconnect(@NotNull Endpoint endpoint) {
        mConnectionsClient.disconnectFromEndpoint(endpoint.getId());

        mHistoryExchangeConnections.remove(endpoint.getId());
        mMessageExchangeConnections.remove(endpoint.getId());
    }

    /** Disconnects from all currently connected endpoints. */
    protected void disconnectFromAllEndpoints() {
        for (Endpoint endpoint : mHistoryExchangeConnections.values()) {
            mConnectionsClient.disconnectFromEndpoint(endpoint.getId());
        }

        mHistoryExchangeConnections.clear();
        mMessageExchangeConnections.clear();
    }

    /** Resets and clears all state in Nearby Connections. */
    protected void stopAllEndpoints() {
        mConnectionsClient.stopAllEndpoints();
        mIsAdvertising = false;
        mIsDiscovering = false;
        mIsConnecting = false;
        mDiscoveredEndpoints.clear();
        mPendingConnections.clear();
        mHistoryExchangeConnections.clear();
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
        mConnectionsClient.requestConnection(getName(), endpoint.getId(), mConnectionLifecycleCallback)
            .addOnSuccessListener(unusedResult -> {
                Log.i("[NEARBY]","Connected to endpoint: " + endpoint.getId());

                connectedToEndpoint(endpoint);
            })

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

        encounters++;
        mHistoryExchangeConnections.put(endpoint.getId(), endpoint);
        Long encounteredId = Long.parseLong(endpoint.getId());

        if (encounteredNodes.containsKey(encounteredId)) {
            ContactInfo contactInfo = encounteredNodes.get(encounteredId);
            contactInfo.incrementContacts();
            contactInfo.setLastEncounterTime(System.currentTimeMillis());
        } else {
            encounteredNodes.put(encounteredId, new ContactInfo(System.currentTimeMillis()));
        }

        onEndpointConnected(endpoint);
    }

    private void disconnectedFromEndpoint(Endpoint endpoint) {
        Log.i("[NEARBY]", String.format("disconnectedFromEndpoint(endpoint=%s)", endpoint));

        mHistoryExchangeConnections.remove(endpoint.getId());
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

        // this is the first message when someone connected to us
        // compose HistoryPayload
        Map<Long, Contact> encounters = new HashMap<>();

        for (Map.Entry<Long, ContactInfo> entry : encounteredNodes.entrySet()) {
            encounters.put(
                entry.getKey(),
                Contact.newBuilder()
                    .setContacts(entry.getValue().getContacts())
                    .setDuration(entry.getValue().getDuration())
                    .setLastEncounterTime(entry.getValue().getLastEncounterTime())
                    .build());
        }

        History history = History.newBuilder()
                .setSocialNetwork(SocialNetwork.newBuilder().addAllUserId(socialNetwork).build())
                .setInterests(Interests.newBuilder().addAllInterest(interests).build())
                .setEncounteredNodes(EncounteredNodes.newBuilder().putAllEncounters(encounters).build())
                .build();

        send(Payload.fromBytes(history.toByteArray()), endpoint.getId());


        // send messages
        for (Message message : messages) {
            send(Payload.fromBytes(MessageExch.newBuilder()
                    .setSource(message.getSource())
                    .setTimestamp(message.getTimestamp())
                    .setTopic1(message.getTopic1())
                    .setTopic2(message.getTopic2())
                    .setTopic3(message.getTopic3())
                    .build().toByteArray()), endpoint.getId());
        }
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
        send(payload, mHistoryExchangeConnections.keySet());
    }

    private void send(Payload payload, Set<String> endpoints) {
        mConnectionsClient
            .sendPayload(new ArrayList<>(endpoints), payload)
            .addOnFailureListener(e -> Log.w("[NEARBY]", "sendPayload() failed.", e));
    }

    private void send(Payload payload, String endpoint) {
        mConnectionsClient.sendPayload(endpoint, payload)
                .addOnFailureListener(e -> Log.w("[NEARBY]", "sendPayload() failed.", e));
    }

    /**
     * Someone connected to us has sent us their history.
     *
     * @param endpoint The sender.
     * @param payload The data.
     */
    protected void onReceiveHistory(@NotNull Endpoint endpoint, @NotNull Payload payload) {
        Log.d("[NEARBY]", "Endpoint " + endpoint.getId() + " sent us their history.");
        if (payload.getType() == Payload.Type.BYTES) {
            try {
                History history = History.parseFrom(payload.asBytes());
                // compute aggregation weight
                Pair<Double, Double> utility = computeAggregationWeight(
                                                    history,
                                                    Long.parseLong(endpoint.getId()));

                double aggregationWeight;
                if (utility == null) {
                    aggregationWeight = 0;
                } else {
                    aggregationWeight = utility.second;

                    // aggregate social network
                    aggregateSocialNetwork(history.getSocialNetwork().getUserIdList(), aggregationWeight);
                    // aggregate interests
                    aggregateInterests(history.getInterests().getInterestList(), aggregationWeight);
                }

                // aggregate contacts - only one that can be modified when weight = 0
                aggregateEncounteredNodes(history.getEncounteredNodes().getEncountersMap(), aggregationWeight);

                // if there are no common topics, no need to exchange data
                if (utility != null && utility.first == 0) {
                    disconnect(endpoint);
                }

            } catch (InvalidProtocolBufferException e) {
                Log.w("[NEARBY]", "Invalid payload from endpoint " + endpoint.getId());
                disconnect(endpoint);
            }
        }
    }

    @Contract(pure = true)
    private double getCommonNeighbours(@NotNull List<Long> encounteredSocialNetwork) {
        double neighbours = 0;

        for (Long node : encounteredSocialNetwork) {
            if (socialNetwork.contains(node)) {
                neighbours += 1;
            }
        }

        return neighbours;
    }

    @Contract(pure = true)
    private double getCommonInterest(@NotNull List<Integer> encounteredInterests) {
        double friendship = 0;

        for (Integer interest : encounteredInterests) {
            if (interests.contains(interest)) {
                friendship += 1;
            }
        }

        return friendship;
    }

    // TODO add javadoc :)
    @org.jetbrains.annotations.Nullable
    private Pair<Double, Double> computeAggregationWeight(@NotNull History history, Long endpointId) {
        if (encounters < contactsThreshold) {
            return null;
        }

        // 1) similarity (number of common neighbours between individuals on social networks)
        double nodeSimilarity = getCommonNeighbours(history.getSocialNetwork().getUserIdList());
        if (maxSimilarity < nodeSimilarity) {
            maxSimilarity = nodeSimilarity;
        }
        nodeSimilarity = maxSimilarity == 0 ? 0 : nodeSimilarity / maxSimilarity;

        // 2) friendship (common interests)
        double nodeFriendship = getCommonInterest(history.getInterests().getInterestList());
        if(maxFriendship < nodeFriendship) {
            maxFriendship = nodeFriendship;
        }
        nodeFriendship = maxFriendship == 0 ? 0 : nodeFriendship / maxFriendship;

        // 3) connectivity (whether nodes are social network friends);
        double connectivity = socialNetwork.contains(endpointId) ? 1.0 : 0.0;

        // 4) number of contacts between the two nodes
        double nodeContacts = Objects.requireNonNull(encounteredNodes.get(endpointId)).getContacts();
        if (maxContacts < nodeContacts) {
            maxContacts = nodeContacts;
        }
        nodeContacts = maxContacts == 0 ? 0 : nodeContacts / maxContacts;

        return new Pair<>(
                nodeFriendship, // so that if there aren't enough common interests, disconnectFromEndpoint
                w1 * nodeSimilarity + w2 * nodeFriendship + w3 * connectivity + w4 * nodeContacts);
    }

    // TODO add javadoc :)
    private void aggregateSocialNetwork(List<Long> encounteredSocialNetwork, double aggregationWeight) {
        if (aggregationWeight > socialNetworkThreshold) {
            socialNetwork.addAll(encounteredSocialNetwork);
        }
    }

    // TODO add javadoc :)
    private void aggregateInterests(List<Integer> encounteredInterests, double aggregationWeight) {
        if (aggregationWeight > interestThreshold) {
            interests.addAll(encounteredInterests);
        }
    }

    // TODO add javadoc :)
    private void aggregateEncounteredNodes(@NotNull Map<Long, Contact> encounteredEN, double aggregationWeight) {
        for (Map.Entry<Long, Contact> e : encounteredEN.entrySet()) {
            // we've already met this node
            if (encounteredNodes.containsKey(e.getKey())) {
                ContactInfo contactInfo = encounteredNodes.get(e.getKey());

                if (contactInfo != null) {
                    contactInfo.setContacts(
                        (int) Math.max(
                                        contactInfo.getContacts(),
                                        aggregationWeight * e.getValue().getContacts()));

                    contactInfo.setDuration(
                            (long) Math.max(
                                    contactInfo.getDuration(),
                                    aggregationWeight * e.getValue().getDuration()));

                    contactInfo.setLastEncounterTime(
                        (long) Math.max(
                                        contactInfo.getLastEncounterTime(),
                                        aggregationWeight * e.getValue().getLastEncounterTime()));

                    encounteredNodes.put(e.getKey(), contactInfo);
                }

            // this is a new node
            } else {
                ContactInfo contactInfo = new ContactInfo(
                        (int) aggregationWeight * e.getValue().getContacts(),
                        (long) aggregationWeight * e.getValue().getDuration(),
                        (long) aggregationWeight * e.getValue().getLastEncounterTime()
                );

                encounteredNodes.put(e.getKey(), contactInfo);
            }
        }
    }

    /**
     * Someone connected to us has sent us one of their messages.
     *
     * @param endpoint The sender.
     * @param payload The data.
     */
    protected void onReceiveMessage(Endpoint endpoint, @NotNull Payload payload) {
        // TODO aici adaug mesajul in baza de date si in lista utilizatorului

        if (payload.getType() == Payload.Type.BYTES) {
            try {
                MessageExch message = MessageExch.parseFrom(payload.asBytes());

                // TODO aici adaug mesajul in baza de date si in lista utilizatorului

            } catch (InvalidProtocolBufferException e) {
                Log.w("[NEARBY]", "Invalid payload from endpoint " + endpoint.getId());
                disconnect(endpoint);
            }
        }
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
     * @param newState The new state we're now in.
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
        return new HashSet<>(mHistoryExchangeConnections.values());
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
