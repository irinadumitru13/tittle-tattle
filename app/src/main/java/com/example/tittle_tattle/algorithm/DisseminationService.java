package com.example.tittle_tattle.algorithm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.proto.Contact;
import com.example.tittle_tattle.algorithm.proto.EncounteredInterests;
import com.example.tittle_tattle.algorithm.proto.EncounteredNodes;
import com.example.tittle_tattle.algorithm.proto.History;
import com.example.tittle_tattle.algorithm.proto.Interests;
import com.example.tittle_tattle.algorithm.proto.MessageExch;
import com.example.tittle_tattle.algorithm.proto.SocialNetwork;
import com.example.tittle_tattle.data.AppDatabase;
import com.example.tittle_tattle.data.models.ContactInfo;
import com.example.tittle_tattle.data.models.EncounteredInterestsObject;
import com.example.tittle_tattle.data.models.EncounteredNodesObject;
import com.example.tittle_tattle.data.models.MessageObject;
import com.example.tittle_tattle.data.models.SocialNetworkObject;
import com.example.tittle_tattle.ui.homeScreen.HomeActivity;
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
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    public static boolean active = false;

    private int notification_id = 1;
    private ServiceHandler serviceHandler;
    /** Our handler to Nearby Connections. */
    private ConnectionsClient mConnectionsClient;
    /** The strategy we're going to use in the Nearby framework. */
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    /** The service ID of this service. */
    private static final String SERVICE_ID = "com.example.tittle_tattle.SERVICE_ID";
    /** The state of the app. As the app changes states, advertising/discovery will start/stop. */
    private State mState = State.UNKNOWN;
    /** A random UID used as this device's endpoint name. */
    private String mName;

    /** True if we are asking a discovered device to connect to us. */
    private boolean mIsConnecting = false;
    /** True if we are discovering. */
    private boolean mIsDiscovering = false;
    /** True if we are advertising. */
    private boolean mIsAdvertising = false;

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

    /** Normalization value for friendship. */
    private double maxFriendship = Double.MIN_VALUE;
    /** Normalization value for similarity. */
    private double maxSimilarity = Double.MIN_VALUE;
    /** Normalization value for contacts. */
    private double maxContacts = Double.MIN_VALUE;
    /** Aggregation weights. */
    private final double w1 = 0.25, w2 = 0.25, w3 = 0.25, w4 = 0.25;

    /** Social network threshold. */
    private final double socialNetworkThreshold = 0.5;
    /** Interest threshold. */
    private final double interestThreshold = 0.1;
    /** Contacts threshold. */
    private final int contactsThreshold = 100;
    /** Interested friends threshold - for ONSIDE algorithm */
    private final int interestedFriendsThreshold = 1;
    /** Encountered interests threshold - for ONSIDE algorithm */
    private final double encounteredInterestsThreshold = 0.7;

    /** How many contacts has this node made. */
    private long encounters = 0;

    /** List of nodes encountered by the current node during a time window. */
    private final Map<Long, ContactInfo> encounteredNodes = new HashMap<>();
    /** Social network - user id with their interests */
    private static final Map<Long, Set<Integer>> socialNetwork = new HashMap<>();
    /** Interests */
    private final Map<Integer, Long> encounteredInterests = new HashMap<>();

    /** Messages published by this node. */
    private static final LinkedList<MessageObject> ownMessages = new LinkedList<>();
    /** Messages received from other nodes. */
    private static final Set<MessageObject> messages = new LinkedHashSet<>();

    public static void addMessage(MessageObject message) {
        ownMessages.addFirst(message);
    }

    @NotNull
    public static List<MessageObject> getOwnMessages() {
        return ownMessages;
    }

    @NotNull
    @Contract(" -> new")
    public static List<MessageObject> getNotifications() {
        return new ArrayList<>(messages);
    }

    protected static void addInterest(long userId, int interest) {
        Set<Integer> interests;
        if (socialNetwork.get(userId) == null) {
            interests = new HashSet<>();
        } else {
            interests = socialNetwork.get(userId);

            if (interests == null) {
                interests = new HashSet<>();
            }
        }

        interests.add(interest);
        socialNetwork.put(userId, interests);
    }

    protected static void deleteInterest(long userId, int interest) {
        if (socialNetwork.containsKey(userId)) {
            Set<Integer> interests = socialNetwork.get(userId);

            if (interests != null) {
                interests.remove(interest);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        active = true;
        startService();

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Looper serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    private final class ServiceHandler extends Handler {
        private final AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NotNull android.os.Message msg) {
            long userId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("user_id", 0);
            mName = String.valueOf(userId);

            messages.addAll(db.messageDAO().findAllExceptUserId(userId));
            ownMessages.addAll(db.messageDAO().findAllByUserId(userId));

            socialNetwork.putAll((db.socialNetworkDAO().findAll().stream().collect(Collectors.toMap(SocialNetworkObject::getId, SocialNetworkObject::getInterests))));
            socialNetwork.put(userId, ISUser.getUser().getTopics());

            encounteredNodes.putAll((db.encounteredNodesDAO().findAll().stream().collect(Collectors.toMap(EncounteredNodesObject::getUserId, EncounteredNodesObject::getContactInfo))));
            encounteredInterests.putAll((db.encounteredInterestsDAO().findAll().stream().collect(Collectors.toMap(EncounteredInterestsObject::getId, EncounteredInterestsObject::getTimes))));

            mConnectionsClient = Nearby.getConnectionsClient(getApplicationContext());
            setState(State.SEARCHING);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        active = true;
        android.os.Message message = serviceHandler.obtainMessage();
        message.arg1 = startId;
        serviceHandler.sendMessage(message);

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
        active = false;
        // TODO put data in db or at least try to
        if (mConnectionsClient != null) {
            stopAllEndpoints();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("tittle-tattle", "tittle-tattle", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("tittle-tattle notification channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startService() {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, DisseminationService.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this, "tittle-tattle")
                        .setContentTitle("tittle-tattle")
                        .setContentText("Service is actively using Bluetooth and WiFi connections.")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentIntent(pendingIntent)
                        .build();
        startForeground(notification_id++, notification);
    }

    /** Callbacks for connections to other devices. */
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
        new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NotNull String endpointId, @NotNull ConnectionInfo connectionInfo) {
                Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
                mPendingConnections.put(endpointId, endpoint);
                DisseminationService.this.onConnectionInitiated(endpoint, connectionInfo);
            }

            @Override
            public void onConnectionResult(@NotNull String endpointId, @NotNull ConnectionResolution result) {
                mIsConnecting = false;

                if (!result.getStatus().isSuccess()) {
                    mPendingConnections.remove(endpointId);
                    return;
                }

                connectedToEndpoint(mPendingConnections.remove(endpointId));
            }

            @Override
            public void onDisconnected(@NotNull String endpointId) {
                if (!mHistoryExchangeConnections.containsKey(endpointId) ||
                        !mMessageExchangeConnections.containsKey(endpointId)) {
                    return;
                }

                disconnectedFromEndpoint(endpointId);
            }
        };

    /** Callbacks for discovering other devices. */
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback =
        new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(@NotNull String endpointId, @NotNull DiscoveredEndpointInfo info) {
                if (getServiceId().equals(info.getServiceId())) {
                    Endpoint endpoint = new Endpoint(endpointId, info.getEndpointName());
                    mDiscoveredEndpoints.put(endpointId, endpoint);

                    onEndpointDiscovered(endpoint);
                }
            }

            @Override
            public void onEndpointLost(@NotNull String endpointId) {
                mDiscoveredEndpoints.remove(endpointId);
            }
        };

    /** Callbacks for payloads (bytes of data) sent from another device to us. */
    private final PayloadCallback mPayloadCallback =
        new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NotNull String endpointId, @NotNull Payload payload) {
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
            .addOnFailureListener(this::onAdvertisingFailed);
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
        mConnectionsClient.rejectConnection(endpoint.getId())
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
            .addOnFailureListener(this::onDiscoveryFailed);
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
        Log.i("[NEARBY]", "Now endpoint " + getName() + " discovering");
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

        for (Endpoint endpoint : mMessageExchangeConnections.values()) {
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
        mMessageExchangeConnections.clear();
    }

    /**
     * Sends a connection request to the endpoint. Either {@link #onConnectionInitiated(Endpoint,
     * ConnectionInfo)} or {@link #onConnectionFailed(Endpoint)} will be called once we've found out
     * if we successfully reached the device.
     */
    protected void connectToEndpoint(@NotNull final Endpoint endpoint) {
        mIsConnecting = true;

        // Ask to connect
        mConnectionsClient.requestConnection(getName(), endpoint.getId(), mConnectionLifecycleCallback)
            .addOnSuccessListener(unusedResult -> {
                connectedToEndpoint(endpoint);
            })

            .addOnFailureListener(e -> {
                mIsConnecting = false;
            });
    }

    /** Returns {@code true} if we're currently attempting to connect to another device. */
    protected final boolean isConnecting() {
        return mIsConnecting;
    }

    private void connectedToEndpoint(Endpoint endpoint) {
        // how many connections happened until now
        encounters++;
        // just connected, first we got to send history to this node
        mHistoryExchangeConnections.put(endpoint.getId(), endpoint);
        Long encounteredId = Long.parseLong(endpoint.getName());

        // update encountered nodes
        if (encounteredNodes.containsKey(encounteredId)) {
            ContactInfo contactInfo = encounteredNodes.get(encounteredId);

            if (contactInfo != null) {
                contactInfo.incrementContacts();
                contactInfo.setLastEncounterTime(System.currentTimeMillis());
            }

        } else {
            encounteredNodes.put(encounteredId, new ContactInfo(System.currentTimeMillis()));
        }

        onEndpointConnected(endpoint);
    }

    private void disconnectedFromEndpoint(String endpoint) {
        Log.i("[NEARBY]", String.format("disconnectedFromEndpoint(endpoint=%s)", endpoint));

        mHistoryExchangeConnections.remove(endpoint);
        mMessageExchangeConnections.remove(endpoint);

        onEndpointDisconnected(Long.parseLong(endpoint));
    }

    /** Called when a connection with this endpoint has failed. */
    protected void onConnectionFailed(Endpoint endpoint) {
        if (getState() == State.SEARCHING) {
            startDiscovering();
        }
    }

    /** Called when someone has connected to us. */
    protected void onEndpointConnected(@NotNull Endpoint endpoint) {
        setState(State.CONNECTED);

        // create EncounteredNodes payload
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

        // create SocialNetwork payload
        Map<Long, Interests> network = new HashMap<>();
        for (Map.Entry<Long, Set<Integer>> node : socialNetwork.entrySet()) {
            network.put(
                    node.getKey(),
                    Interests.newBuilder()
                            .addAllInterest(node.getValue())
                            .build());
        }

        // build payload using classes compiled with protoc (ProtocolBuffers)
        History history = History.newBuilder()
                .setSocialNetwork(SocialNetwork.newBuilder().putAllNode(network).build())

                .setEncounteredInterests(EncounteredInterests.newBuilder().putAllInterests(encounteredInterests).build())

                .setEncounteredNodes(EncounteredNodes.newBuilder().putAllEncounters(encounters).build())
                .build();

        // send history to endpoint
        send(Payload.fromBytes(history.toByteArray()), endpoint.getId());
    }

    /** Called when someone has disconnected. */
    protected void onEndpointDisconnected(@NotNull Long endpoint) {
        setState(State.SEARCHING);

        updateDurationOfContact(endpoint);
    }

    private void updateDurationOfContact(Long endpoint) {
        // update duration of contact
        if (encounteredNodes.containsKey(endpoint)) {
            ContactInfo contactInfo = encounteredNodes.get(endpoint);

            if (contactInfo != null) {
                contactInfo.setDuration(System.currentTimeMillis() - contactInfo.getLastEncounterTime());
            } else {
                Log.w("[NEARBY]", "Contact info should've been previously initialized: " + endpoint);
            }

        } else {
            Log.w("[NEARBY]", "Disconnected from " + endpoint + ", but we weren't previously connected.");
        }
    }

    /**
     * Sends a {@link Payload} to all currently connected endpoints.
     *
     * @param payload The data you want to send.
     */
    protected void send(Payload payload) {
        mConnectionsClient
                .sendPayload(new ArrayList<>(mHistoryExchangeConnections.keySet()), payload)
                .addOnFailureListener(e ->
                        Log.w("[NEARBY]", "sendPayload() to all connected endpoints failed.", e));
    }

    /**
     * Sends a {@link Payload} to a connected endpoint.
     *
     * @param payload The data you want to send.
     */
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
        if (payload.getType() == Payload.Type.BYTES) {
            try {
                History history = History.parseFrom(payload.asBytes());
                // update interests encounters based on the data we received
                encounterInterests(history, Long.parseLong(endpoint.getName()));

                // compute aggregation weight <common topics, aggregation weight>
                Pair<Double, Double> utility = computeAggregationWeight(history, Long.parseLong(endpoint.getName()));

                if (utility.second != 0) {
                    // aggregate social network - if weight is 0, avoid unnecessary operations
                    aggregateSocialNetwork(history.getSocialNetwork().getNodeMap(), utility.second);
                }
                // these two might add new keys in data structures, even though 0 values inserted
                // aggregate interests
                aggregateInterests(history.getEncounteredInterests().getInterestsMap(), utility.second);
                // aggregate contacts - only one that can be modified when weight = 0
                aggregateEncounteredNodes(history.getEncounteredNodes().getEncountersMap(), utility.second);

                // if there are no common topics, no need to exchange data - first part of ONSIDE FUNCTION
                if (utility.first == 0) {
                    disconnect(endpoint);
                    return;
                }

                // send messages that the encountered node is interested in
                sendMessages(endpoint, history);

            } catch (InvalidProtocolBufferException e) {
                Log.w("[NEARBY]", "Invalid payload from endpoint " + endpoint.getId());
                disconnect(endpoint);
            }
        }
    }

    /**
     * Updates the encountered interests.
     *
     * @param history The first payload received from a node.
     * @param userId The user id of the connected endpoint.
     */
    private void encounterInterests(@NotNull History history, Long userId) {
        Map<Long, Interests> network = history.getSocialNetwork().getNodeMap();

        if (network.containsKey(userId)) {
            List<Integer> interests;
            long newValue;

            if (network.get(userId) != null) {
                interests = Objects.requireNonNull(network.get(userId)).getInterestList();

                for (int interest : interests) {
                    if (encounteredInterests.containsKey(interest)
                            && encounteredInterests.get(interest) != null) {
                        newValue = encounteredInterests.get(interest) + 1;
                        encounteredInterests.put(interest, newValue);
                    } else {
                        encounteredInterests.put(interest, (long) 1);
                    }
                }
            }
        }
    }

    @Contract(pure = true)
    private double getCommonNeighbours(@NotNull Set<Long> encounteredSocialNetwork) {
        double neighbours = 0;

        for (Long node : encounteredSocialNetwork) {
            if (socialNetwork.containsKey(node)) {
                neighbours += 1;
            }
        }

        return neighbours;
    }

    private double getCommonInterests(@NotNull List<Integer> encounteredInterests) {
        double friendship = 0;

        for (Integer interest : Objects.requireNonNull(socialNetwork.get(Long.parseLong(getName())))) {
            if (encounteredInterests.contains(interest)) {
                friendship += 1;
            }
        }

        return friendship;
    }

    /**
     * Computes the aggregation weight between the encountered node and me.
     *
     * @param history the payload signifying the history of the encountered node
     * @param endpointId the user id of the encountered node
     * @return the aggregation weight between the two nodes
     */
    @NotNull
    private Pair<Double, Double> computeAggregationWeight(@NotNull History history, Long endpointId) {
        // Because aggregation is done only after a set of contacts
        // but friendship is needed whatever the case, do only necessary computations

        // 1) friendship (common interests between the two nodes)
        double nodeFriendship = 0;
        if (history.getSocialNetwork().containsNode(endpointId)) {
            nodeFriendship = getCommonInterests(history.getSocialNetwork().getNodeOrDefault(endpointId, null).getInterestList());
        }

        if (encounters < contactsThreshold) {
            return new Pair<>(nodeFriendship, 0.0);

        } else {
            // 1.1) normalize friendship value
            if (maxFriendship < nodeFriendship) {
                maxFriendship = nodeFriendship;
            }
            double nodeFriendshipN = maxFriendship == 0 ? 0 : nodeFriendship / maxFriendship;

            // 2) similarity (number of common neighbours between individuals on social networks)
            double nodeSimilarity = getCommonNeighbours(history.getSocialNetwork().getNodeMap().keySet());
            if (maxSimilarity < nodeSimilarity) {
                maxSimilarity = nodeSimilarity;
            }
            nodeSimilarity = maxSimilarity == 0 ? 0 : nodeSimilarity / maxSimilarity;

            // 3) connectivity (whether nodes are social network friends);
            double connectivity = socialNetwork.containsKey(endpointId) ? 1.0 : 0.0;

            // 4) number of contacts between the two nodes
            double nodeContacts = Objects.requireNonNull(encounteredNodes.get(endpointId)).getContacts();
            if (maxContacts < nodeContacts) {
                maxContacts = nodeContacts;
            }
            nodeContacts = maxContacts == 0 ? 0 : nodeContacts / maxContacts;

            return new Pair<>(
                nodeFriendship,
                w1 * nodeSimilarity + w2 * nodeFriendshipN + w3 * connectivity + w4 * nodeContacts);
        }
    }

    /**
     * Aggregates the social network of this node when it comes into contact with another node.
     *
     * Adds the entries from the encountered node into the local one.
     *
     * @param encounteredSocialNetwork the social network of the encountered node
     * @param aggregationWeight aggregation weight
     */
    private void aggregateSocialNetwork(Map<Long, Interests> encounteredSocialNetwork, double aggregationWeight) {
        if (aggregationWeight > socialNetworkThreshold) {

            for (Map.Entry<Long, Interests> node : encounteredSocialNetwork.entrySet()) {
                if (socialNetwork.containsKey(node.getKey())) {
                    Set<Integer> interests = socialNetwork.get(node.getKey());

                    if (interests != null) {
                        interests.addAll(node.getValue().getInterestList());
                    } else {
                        interests = new HashSet<>(node.getValue().getInterestList());
                    }

                    socialNetwork.put(node.getKey(), interests);

                } else {
                    socialNetwork.put(node.getKey(), new HashSet<>(node.getValue().getInterestList()));
                }
            }
        }
    }

    /**
     * Aggregates the local view of interests when it comes into contact with another node.
     *
     * max(local_value, weight * their_value), if interest exists locally
     * weight * their_value                  , otherwise
     *
     * @param encounteredInterests the encountered node's view of interests
     * @param aggregationWeight aggregation weight
     */
    private void aggregateInterests(@NotNull Map<Integer, Long> encounteredInterests, double aggregationWeight) {
        for (Map.Entry<Integer, Long> interest : encounteredInterests.entrySet()) {
            if (encounteredInterests.containsKey(interest.getKey())) {
                Long timesEncountered = encounteredInterests.get(interest.getKey());

                if (timesEncountered != null) {
                    timesEncountered = (long) Math.max(timesEncountered, aggregationWeight * interest.getValue());

                // for some reason, key was added with null value
                } else {
                    timesEncountered = (long) aggregationWeight * interest.getValue();
                }

                encounteredInterests.put(interest.getKey(), timesEncountered);

                // this is a new interest
            } else {
                encounteredInterests.put(interest.getKey(), (long) aggregationWeight * interest.getValue());
            }
        }
    }

    /**
     * Aggregates the contact info regarding encountered nodes locally with the data of the
     * encountered node.
     *
     * max(local_value, weight * their_value), if interest exists locally
     * weight * their_value                  , otherwise
     *
     * @param encounteredEN encountered nodes as seen by the other node
     * @param aggregationWeight aggregation weight
     */
    private void aggregateEncounteredNodes(@NotNull Map<Long, Contact> encounteredEN, double aggregationWeight) {
        for (Map.Entry<Long, Contact> e : encounteredEN.entrySet()) {
            // we've already met this node
            if (encounteredNodes.containsKey(e.getKey())) {
                ContactInfo contactInfo = encounteredNodes.get(e.getKey());

                if (contactInfo != null) {
                    contactInfo.setContacts((int) Math.max(contactInfo.getContacts(), aggregationWeight * e.getValue().getContacts()));
                    contactInfo.setDuration((long) Math.max(contactInfo.getDuration(), aggregationWeight * e.getValue().getDuration()));
                    contactInfo.setLastEncounterTime((long) Math.max(contactInfo.getLastEncounterTime(), aggregationWeight * e.getValue().getLastEncounterTime()));

                // for some reason, key was added with null value
                } else {
                    contactInfo = new ContactInfo(
                        (int) aggregationWeight * e.getValue().getContacts(),
                        (long) aggregationWeight * e.getValue().getDuration(),
                        (long) aggregationWeight * e.getValue().getLastEncounterTime());
                }

                encounteredNodes.put(e.getKey(), contactInfo);
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
     * Performs the messages exchange between this node and the encountered one.
     *
     * This node will decide which messages to send to the other one - ONSIDE, but reversed
     * If it got here, there is at least one topic in common between the two nodes
     *
     * @param endpoint the encountered node
     * @param history their view of the network at the moment of connection
     */
    private void sendMessages(@NotNull Endpoint endpoint, History history) {
        Long userId = Long.parseLong(endpoint.getName());

        for (MessageObject message : ownMessages) {
            if (interested(message, history, userId)
                    || interestedFriends(message, history, userId)
                    || interestsEncountered(message, history)) {
                // at least one topic mandatory, so verifications must be made before building the message
                MessageExch.Builder messagePayload = MessageExch.newBuilder()
                        .setSource(message.getSource())
                        .setContent(message.getContent())
                        .setTimestamp(message.getTimestamp())
                        .setTopic1(message.getTopic1());

                if (message.getTopic2() != null) {
                    messagePayload.setTopic2(message.getTopic2());
                }
                if (message.getTopic3() != null) {
                    messagePayload.setTopic3(message.getTopic3());
                }

                // send message to the other node
                send(Payload.fromBytes(messagePayload.build().toByteArray()), endpoint.getId());
            }
        }

        for (MessageObject message : messages) {
            if (message.getSource() != Long.parseLong(endpoint.getName())) {
                if (interested(message, history, userId)
                        || interestedFriends(message, history, userId)
                        || interestsEncountered(message, history)) {
                    // at least one topic mandatory, so verifications must be made before building the message
                    MessageExch.Builder messagePayload = MessageExch.newBuilder()
                            .setSource(message.getSource())
                            .setContent(message.getContent())
                            .setTimestamp(message.getTimestamp())
                            .setTopic1(message.getTopic1());

                    if (message.getTopic2() != null) {
                        messagePayload.setTopic2(message.getTopic2());
                    }
                    if (message.getTopic3() != null) {
                        messagePayload.setTopic3(message.getTopic3());
                    }

                    // send message to the other node
                    send(Payload.fromBytes(messagePayload.build().toByteArray()), endpoint.getId());
                }
            }
        }
    }

    /**
     * Returns whether the other node is interested in at least one topic from the message.
     *
     * @param message the message to be sent
     * @param history the encountered node's view of the network at the moment of connection
     * @param userId the user id of the encountered node
     *
     * @return {@code true} if the other node is interested in the message, {@code false}
     * otherwise
     */
    private boolean interested(MessageObject message, @NotNull History history, Long userId) {
        if (history.getSocialNetwork().containsNode(userId)) {
            Interests nodeInterests = history.getSocialNetwork().getNodeOrDefault(userId, null);

            if (nodeInterests != null) {
                List<Integer> interests = nodeInterests.getInterestList();

                if (interests.contains(message.getTopic1())) {
                    return true;
                }

                if (message.getTopic2() != null) {
                    if (interests.contains(message.getTopic2())) {
                        return true;
                    }
                }

                if (message.getTopic3() != null) {
                    return interests.contains(message.getTopic3());
                }

            }
        }
        return false;
    }

    /**
     * Returns whether the other node's friends are interested in at least one topic from the message.
     *
     * @param message the message to be sent
     * @param history the encountered node's view of the network at the moment of connection
     * @param userId the user id of the encountered node
     *
     * @return {@code true} if the other node's friends are interested in the message, {@code false}
     * otherwise
     */
    private boolean interestedFriends(MessageObject message, @NotNull History history, Long userId) {
        int interestedFriends = 0;
        Interests nodeInterests;
        List<Integer> interests;

        for (Map.Entry<Long, Interests> node : history.getSocialNetwork().getNodeMap().entrySet()) {
            if (!node.getKey().equals(userId)) {
                nodeInterests = node.getValue();

                if (nodeInterests != null) {
                    interests = nodeInterests.getInterestList();

                    if (interests.contains(message.getTopic1())) {
                        interestedFriends += 1;
                    } else if (message.getTopic2() != null) {
                        if (interests.contains(message.getTopic2())) {
                            interestedFriends += 1;
                        }
                    } else if (message.getTopic3() != null) {
                        if (interests.contains(message.getTopic2())) {
                            interestedFriends += 1;
                        }
                    }

                    if (interestedFriends > interestedFriendsThreshold) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks whether this node is likely to encounter a node with given interests,
     * based on the history of encountered interests
     *
     * @param message the message to check against
     * @param history the other node's view of the network at the moment of connection
     *
     * @return {@code true} if the given node is likely to encounter the set of interests,
     * {@code false} otherwise
     */
    private boolean interestsEncountered(MessageObject message, @NotNull History history) {
        long total = 0, util = 0;

        Map<Integer, Long> eInterests = history.getEncounteredInterests().getInterestsMap();

        for (Map.Entry<Integer, Long> e : eInterests.entrySet()) {
            total += e.getValue();

            if (message.getTopic1().equals(e.getKey())) {
                util += e.getValue();
            } else if (message.getTopic2() != null) {
                if (message.getTopic2().equals(e.getKey())) {
                    util += e.getValue();
                }
            } else if (message.getTopic3() != null) {
                if (message.getTopic3().equals(e.getKey())) {
                    util += e.getValue();
                }
            }
        }

        return ((double) util / total) > encounteredInterestsThreshold;
    }

    /**
     * Someone connected to us has sent us one of their messages.
     *
     * @param endpoint The sender.
     * @param payload The data.
     */
    protected void onReceiveMessage(Endpoint endpoint, @NotNull Payload payload) {
        if (payload.getType() == Payload.Type.BYTES) {
            try {
                MessageExch message = MessageExch.parseFrom(payload.asBytes());
                MessageObject messageObject = new MessageObject(
                        message.getSource(),
                        message.getContent(),
                        message.getTopic1(),
                        message.getTopic2(),
                        message.getTopic3(),
                        message.getTimestamp());

                Set<Integer> ownInterests = socialNetwork.get(Long.parseLong(getName()));
                if (ownInterests != null) {
                    if (ownInterests.contains(messageObject.getTopic1())
                            || ownInterests.contains(messageObject.getTopic2())
                            || ownInterests.contains(messageObject.getTopic3())) {
                        messageObject.setInterested(true);

                        // send notification to UI and add in ISUser
                        if (!messages.contains(messageObject)) {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "tittle-tattle")
                                    .setSmallIcon(R.drawable.ic_stat_name)
                                    .setContentTitle("Tittle-Tattle")
                                    .setContentText(messageObject.getContent())
                                    .setOngoing(true)
                                    .setAutoCancel(true)
                                    .setContentIntent(
                                            new NavDeepLinkBuilder(getApplicationContext())
                                                    .setComponentName(HomeActivity.class)
                                                    .setGraph(R.navigation.mobile_navigation)
                                                    .setDestination(R.id.navigation_notifications)
                                                    .createPendingIntent())
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManagerCompat.from(this).notify(notification_id++, builder.build());
                        }
                    } else {
                        messageObject.setInterested(false);
                    }
                } else {
                    messageObject.setInterested(false);
                }

                AsyncTask.execute( () -> {
                    if (!messages.contains(messageObject)) {
                        AppDatabase.getInstance(getApplicationContext()).messageDAO().insert(messageObject);
                    }
                });

                messages.add(messageObject);

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
