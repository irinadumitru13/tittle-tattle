// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: history.proto

package com.example.tittle_tattle.algorithm.proto;

/**
 * Protobuf type {@code com.example.tittle_tattle.algorithm.proto.EncounteredInterests}
 */
public final class EncounteredInterests extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.example.tittle_tattle.algorithm.proto.EncounteredInterests)
    EncounteredInterestsOrBuilder {
private static final long serialVersionUID = 0L;
  // Use EncounteredInterests.newBuilder() to construct.
  private EncounteredInterests(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private EncounteredInterests() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new EncounteredInterests();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private EncounteredInterests(
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
              interests_ = com.google.protobuf.MapField.newMapField(
                  InterestsDefaultEntryHolder.defaultEntry);
              mutable_bitField0_ |= 0x00000001;
            }
            com.google.protobuf.MapEntry<java.lang.Integer, java.lang.Long>
            interests__ = input.readMessage(
                InterestsDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
            interests_.getMutableMap().put(
                interests__.getKey(), interests__.getValue());
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
    return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  @java.lang.Override
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 1:
        return internalGetInterests();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.example.tittle_tattle.algorithm.proto.EncounteredInterests.class, com.example.tittle_tattle.algorithm.proto.EncounteredInterests.Builder.class);
  }

  public static final int INTERESTS_FIELD_NUMBER = 1;
  private static final class InterestsDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        java.lang.Integer, java.lang.Long> defaultEntry =
            com.google.protobuf.MapEntry
            .<java.lang.Integer, java.lang.Long>newDefaultInstance(
                com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_InterestsEntry_descriptor, 
                com.google.protobuf.WireFormat.FieldType.INT32,
                0,
                com.google.protobuf.WireFormat.FieldType.INT64,
                0L);
  }
  private com.google.protobuf.MapField<
      java.lang.Integer, java.lang.Long> interests_;
  private com.google.protobuf.MapField<java.lang.Integer, java.lang.Long>
  internalGetInterests() {
    if (interests_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          InterestsDefaultEntryHolder.defaultEntry);
    }
    return interests_;
  }

  public int getInterestsCount() {
    return internalGetInterests().getMap().size();
  }
  /**
   * <code>map&lt;int32, int64&gt; interests = 1;</code>
   */

  @java.lang.Override
  public boolean containsInterests(
      int key) {
    
    return internalGetInterests().getMap().containsKey(key);
  }
  /**
   * Use {@link #getInterestsMap()} instead.
   */
  @java.lang.Override
  @java.lang.Deprecated
  public java.util.Map<java.lang.Integer, java.lang.Long> getInterests() {
    return getInterestsMap();
  }
  /**
   * <code>map&lt;int32, int64&gt; interests = 1;</code>
   */
  @java.lang.Override

  public java.util.Map<java.lang.Integer, java.lang.Long> getInterestsMap() {
    return internalGetInterests().getMap();
  }
  /**
   * <code>map&lt;int32, int64&gt; interests = 1;</code>
   */
  @java.lang.Override

  public long getInterestsOrDefault(
      int key,
      long defaultValue) {
    
    java.util.Map<java.lang.Integer, java.lang.Long> map =
        internalGetInterests().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <code>map&lt;int32, int64&gt; interests = 1;</code>
   */
  @java.lang.Override

  public long getInterestsOrThrow(
      int key) {
    
    java.util.Map<java.lang.Integer, java.lang.Long> map =
        internalGetInterests().getMap();
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
      .serializeIntegerMapTo(
        output,
        internalGetInterests(),
        InterestsDefaultEntryHolder.defaultEntry,
        1);
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (java.util.Map.Entry<java.lang.Integer, java.lang.Long> entry
         : internalGetInterests().getMap().entrySet()) {
      com.google.protobuf.MapEntry<java.lang.Integer, java.lang.Long>
      interests__ = InterestsDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, interests__);
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
    if (!(obj instanceof com.example.tittle_tattle.algorithm.proto.EncounteredInterests)) {
      return super.equals(obj);
    }
    com.example.tittle_tattle.algorithm.proto.EncounteredInterests other = (com.example.tittle_tattle.algorithm.proto.EncounteredInterests) obj;

    if (!internalGetInterests().equals(
        other.internalGetInterests())) return false;
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
    if (!internalGetInterests().getMap().isEmpty()) {
      hash = (37 * hash) + INTERESTS_FIELD_NUMBER;
      hash = (53 * hash) + internalGetInterests().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests parseFrom(
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
  public static Builder newBuilder(com.example.tittle_tattle.algorithm.proto.EncounteredInterests prototype) {
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
   * Protobuf type {@code com.example.tittle_tattle.algorithm.proto.EncounteredInterests}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.example.tittle_tattle.algorithm.proto.EncounteredInterests)
      com.example.tittle_tattle.algorithm.proto.EncounteredInterestsOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 1:
          return internalGetInterests();
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
          return internalGetMutableInterests();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.example.tittle_tattle.algorithm.proto.EncounteredInterests.class, com.example.tittle_tattle.algorithm.proto.EncounteredInterests.Builder.class);
    }

    // Construct using com.example.tittle_tattle.algorithm.proto.EncounteredInterests.newBuilder()
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
      internalGetMutableInterests().clear();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_descriptor;
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.EncounteredInterests getDefaultInstanceForType() {
      return com.example.tittle_tattle.algorithm.proto.EncounteredInterests.getDefaultInstance();
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.EncounteredInterests build() {
      com.example.tittle_tattle.algorithm.proto.EncounteredInterests result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.EncounteredInterests buildPartial() {
      com.example.tittle_tattle.algorithm.proto.EncounteredInterests result = new com.example.tittle_tattle.algorithm.proto.EncounteredInterests(this);
      int from_bitField0_ = bitField0_;
      result.interests_ = internalGetInterests();
      result.interests_.makeImmutable();
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
      if (other instanceof com.example.tittle_tattle.algorithm.proto.EncounteredInterests) {
        return mergeFrom((com.example.tittle_tattle.algorithm.proto.EncounteredInterests)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.example.tittle_tattle.algorithm.proto.EncounteredInterests other) {
      if (other == com.example.tittle_tattle.algorithm.proto.EncounteredInterests.getDefaultInstance()) return this;
      internalGetMutableInterests().mergeFrom(
          other.internalGetInterests());
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
      com.example.tittle_tattle.algorithm.proto.EncounteredInterests parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.example.tittle_tattle.algorithm.proto.EncounteredInterests) e.getUnfinishedMessage();
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
        java.lang.Integer, java.lang.Long> interests_;
    private com.google.protobuf.MapField<java.lang.Integer, java.lang.Long>
    internalGetInterests() {
      if (interests_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            InterestsDefaultEntryHolder.defaultEntry);
      }
      return interests_;
    }
    private com.google.protobuf.MapField<java.lang.Integer, java.lang.Long>
    internalGetMutableInterests() {
      onChanged();;
      if (interests_ == null) {
        interests_ = com.google.protobuf.MapField.newMapField(
            InterestsDefaultEntryHolder.defaultEntry);
      }
      if (!interests_.isMutable()) {
        interests_ = interests_.copy();
      }
      return interests_;
    }

    public int getInterestsCount() {
      return internalGetInterests().getMap().size();
    }
    /**
     * <code>map&lt;int32, int64&gt; interests = 1;</code>
     */

    @java.lang.Override
    public boolean containsInterests(
        int key) {
      
      return internalGetInterests().getMap().containsKey(key);
    }
    /**
     * Use {@link #getInterestsMap()} instead.
     */
    @java.lang.Override
    @java.lang.Deprecated
    public java.util.Map<java.lang.Integer, java.lang.Long> getInterests() {
      return getInterestsMap();
    }
    /**
     * <code>map&lt;int32, int64&gt; interests = 1;</code>
     */
    @java.lang.Override

    public java.util.Map<java.lang.Integer, java.lang.Long> getInterestsMap() {
      return internalGetInterests().getMap();
    }
    /**
     * <code>map&lt;int32, int64&gt; interests = 1;</code>
     */
    @java.lang.Override

    public long getInterestsOrDefault(
        int key,
        long defaultValue) {
      
      java.util.Map<java.lang.Integer, java.lang.Long> map =
          internalGetInterests().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <code>map&lt;int32, int64&gt; interests = 1;</code>
     */
    @java.lang.Override

    public long getInterestsOrThrow(
        int key) {
      
      java.util.Map<java.lang.Integer, java.lang.Long> map =
          internalGetInterests().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }

    public Builder clearInterests() {
      internalGetMutableInterests().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <code>map&lt;int32, int64&gt; interests = 1;</code>
     */

    public Builder removeInterests(
        int key) {
      
      internalGetMutableInterests().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.Integer, java.lang.Long>
    getMutableInterests() {
      return internalGetMutableInterests().getMutableMap();
    }
    /**
     * <code>map&lt;int32, int64&gt; interests = 1;</code>
     */
    public Builder putInterests(
        int key,
        long value) {
      
      
      internalGetMutableInterests().getMutableMap()
          .put(key, value);
      return this;
    }
    /**
     * <code>map&lt;int32, int64&gt; interests = 1;</code>
     */

    public Builder putAllInterests(
        java.util.Map<java.lang.Integer, java.lang.Long> values) {
      internalGetMutableInterests().getMutableMap()
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


    // @@protoc_insertion_point(builder_scope:com.example.tittle_tattle.algorithm.proto.EncounteredInterests)
  }

  // @@protoc_insertion_point(class_scope:com.example.tittle_tattle.algorithm.proto.EncounteredInterests)
  private static final com.example.tittle_tattle.algorithm.proto.EncounteredInterests DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.example.tittle_tattle.algorithm.proto.EncounteredInterests();
  }

  public static com.example.tittle_tattle.algorithm.proto.EncounteredInterests getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<EncounteredInterests>
      PARSER = new com.google.protobuf.AbstractParser<EncounteredInterests>() {
    @java.lang.Override
    public EncounteredInterests parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new EncounteredInterests(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<EncounteredInterests> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<EncounteredInterests> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.example.tittle_tattle.algorithm.proto.EncounteredInterests getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

