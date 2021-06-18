// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: history.proto

package com.example.tittle_tattle.algorithm.proto;

/**
 * Protobuf type {@code com.example.tittle_tattle.algorithm.proto.SocialNetwork}
 */
public final class SocialNetwork extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.example.tittle_tattle.algorithm.proto.SocialNetwork)
    SocialNetworkOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SocialNetwork.newBuilder() to construct.
  private SocialNetwork(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SocialNetwork() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new SocialNetwork();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private SocialNetwork(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              node_ = com.google.protobuf.MapField.newMapField(
                  NodeDefaultEntryHolder.defaultEntry);
              mutable_bitField0_ |= 0x00000001;
            }
            com.google.protobuf.MapEntry<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests>
            node__ = input.readMessage(
                NodeDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
            node_.getMutableMap().put(
                node__.getKey(), node__.getValue());
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  @java.lang.Override
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 1:
        return internalGetNode();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.example.tittle_tattle.algorithm.proto.SocialNetwork.class, com.example.tittle_tattle.algorithm.proto.SocialNetwork.Builder.class);
  }

  public static final int NODE_FIELD_NUMBER = 1;
  private static final class NodeDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> defaultEntry =
            com.google.protobuf.MapEntry
            .<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests>newDefaultInstance(
                com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_NodeEntry_descriptor, 
                com.google.protobuf.WireFormat.FieldType.INT64,
                0L,
                com.google.protobuf.WireFormat.FieldType.MESSAGE,
                com.example.tittle_tattle.algorithm.proto.Interests.getDefaultInstance());
  }
  private com.google.protobuf.MapField<
      java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> node_;
  private com.google.protobuf.MapField<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests>
  internalGetNode() {
    if (node_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          NodeDefaultEntryHolder.defaultEntry);
    }
    return node_;
  }

  public int getNodeCount() {
    return internalGetNode().getMap().size();
  }
  /**
   * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
   */

  @java.lang.Override
  public boolean containsNode(
      long key) {
    
    return internalGetNode().getMap().containsKey(key);
  }
  /**
   * Use {@link #getNodeMap()} instead.
   */
  @java.lang.Override
  @java.lang.Deprecated
  public java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> getNode() {
    return getNodeMap();
  }
  /**
   * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
   */
  @java.lang.Override

  public java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> getNodeMap() {
    return internalGetNode().getMap();
  }
  /**
   * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
   */
  @java.lang.Override

  public com.example.tittle_tattle.algorithm.proto.Interests getNodeOrDefault(
      long key,
      com.example.tittle_tattle.algorithm.proto.Interests defaultValue) {
    
    java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> map =
        internalGetNode().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
   */
  @java.lang.Override

  public com.example.tittle_tattle.algorithm.proto.Interests getNodeOrThrow(
      long key) {
    
    java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> map =
        internalGetNode().getMap();
    if (!map.containsKey(key)) {
      throw new java.lang.IllegalArgumentException();
    }
    return map.get(key);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    com.google.protobuf.GeneratedMessageV3
      .serializeLongMapTo(
        output,
        internalGetNode(),
        NodeDefaultEntryHolder.defaultEntry,
        1);
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (java.util.Map.Entry<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> entry
         : internalGetNode().getMap().entrySet()) {
      com.google.protobuf.MapEntry<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests>
      node__ = NodeDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, node__);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.example.tittle_tattle.algorithm.proto.SocialNetwork)) {
      return super.equals(obj);
    }
    com.example.tittle_tattle.algorithm.proto.SocialNetwork other = (com.example.tittle_tattle.algorithm.proto.SocialNetwork) obj;

    if (!internalGetNode().equals(
        other.internalGetNode())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (!internalGetNode().getMap().isEmpty()) {
      hash = (37 * hash) + NODE_FIELD_NUMBER;
      hash = (53 * hash) + internalGetNode().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.example.tittle_tattle.algorithm.proto.SocialNetwork prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.example.tittle_tattle.algorithm.proto.SocialNetwork}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.example.tittle_tattle.algorithm.proto.SocialNetwork)
      com.example.tittle_tattle.algorithm.proto.SocialNetworkOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 1:
          return internalGetNode();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMutableMapField(
        int number) {
      switch (number) {
        case 1:
          return internalGetMutableNode();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.example.tittle_tattle.algorithm.proto.SocialNetwork.class, com.example.tittle_tattle.algorithm.proto.SocialNetwork.Builder.class);
    }

    // Construct using com.example.tittle_tattle.algorithm.proto.SocialNetwork.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      internalGetMutableNode().clear();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_descriptor;
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.SocialNetwork getDefaultInstanceForType() {
      return com.example.tittle_tattle.algorithm.proto.SocialNetwork.getDefaultInstance();
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.SocialNetwork build() {
      com.example.tittle_tattle.algorithm.proto.SocialNetwork result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.SocialNetwork buildPartial() {
      com.example.tittle_tattle.algorithm.proto.SocialNetwork result = new com.example.tittle_tattle.algorithm.proto.SocialNetwork(this);
      int from_bitField0_ = bitField0_;
      result.node_ = internalGetNode();
      result.node_.makeImmutable();
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.example.tittle_tattle.algorithm.proto.SocialNetwork) {
        return mergeFrom((com.example.tittle_tattle.algorithm.proto.SocialNetwork)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.example.tittle_tattle.algorithm.proto.SocialNetwork other) {
      if (other == com.example.tittle_tattle.algorithm.proto.SocialNetwork.getDefaultInstance()) return this;
      internalGetMutableNode().mergeFrom(
          other.internalGetNode());
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.example.tittle_tattle.algorithm.proto.SocialNetwork parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.example.tittle_tattle.algorithm.proto.SocialNetwork) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private com.google.protobuf.MapField<
        java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> node_;
    private com.google.protobuf.MapField<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests>
    internalGetNode() {
      if (node_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            NodeDefaultEntryHolder.defaultEntry);
      }
      return node_;
    }
    private com.google.protobuf.MapField<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests>
    internalGetMutableNode() {
      onChanged();;
      if (node_ == null) {
        node_ = com.google.protobuf.MapField.newMapField(
            NodeDefaultEntryHolder.defaultEntry);
      }
      if (!node_.isMutable()) {
        node_ = node_.copy();
      }
      return node_;
    }

    public int getNodeCount() {
      return internalGetNode().getMap().size();
    }
    /**
     * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
     */

    @java.lang.Override
    public boolean containsNode(
        long key) {
      
      return internalGetNode().getMap().containsKey(key);
    }
    /**
     * Use {@link #getNodeMap()} instead.
     */
    @java.lang.Override
    @java.lang.Deprecated
    public java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> getNode() {
      return getNodeMap();
    }
    /**
     * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
     */
    @java.lang.Override

    public java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> getNodeMap() {
      return internalGetNode().getMap();
    }
    /**
     * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
     */
    @java.lang.Override

    public com.example.tittle_tattle.algorithm.proto.Interests getNodeOrDefault(
        long key,
        com.example.tittle_tattle.algorithm.proto.Interests defaultValue) {
      
      java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> map =
          internalGetNode().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
     */
    @java.lang.Override

    public com.example.tittle_tattle.algorithm.proto.Interests getNodeOrThrow(
        long key) {
      
      java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> map =
          internalGetNode().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }

    public Builder clearNode() {
      internalGetMutableNode().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
     */

    public Builder removeNode(
        long key) {
      
      internalGetMutableNode().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests>
    getMutableNode() {
      return internalGetMutableNode().getMutableMap();
    }
    /**
     * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
     */
    public Builder putNode(
        long key,
        com.example.tittle_tattle.algorithm.proto.Interests value) {
      
      if (value == null) { throw new java.lang.NullPointerException(); }
      internalGetMutableNode().getMutableMap()
          .put(key, value);
      return this;
    }
    /**
     * <code>map&lt;int64, .com.example.tittle_tattle.algorithm.proto.Interests&gt; node = 1;</code>
     */

    public Builder putAllNode(
        java.util.Map<java.lang.Long, com.example.tittle_tattle.algorithm.proto.Interests> values) {
      internalGetMutableNode().getMutableMap()
          .putAll(values);
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.example.tittle_tattle.algorithm.proto.SocialNetwork)
  }

  // @@protoc_insertion_point(class_scope:com.example.tittle_tattle.algorithm.proto.SocialNetwork)
  private static final com.example.tittle_tattle.algorithm.proto.SocialNetwork DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.example.tittle_tattle.algorithm.proto.SocialNetwork();
  }

  public static com.example.tittle_tattle.algorithm.proto.SocialNetwork getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SocialNetwork>
      PARSER = new com.google.protobuf.AbstractParser<SocialNetwork>() {
    @java.lang.Override
    public SocialNetwork parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new SocialNetwork(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<SocialNetwork> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SocialNetwork> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.example.tittle_tattle.algorithm.proto.SocialNetwork getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

