// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: history.proto

package com.example.tittle_tattle.algorithm.proto;

/**
 * Protobuf type {@code com.example.tittle_tattle.algorithm.proto.Interests}
 */
public final class Interests extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.example.tittle_tattle.algorithm.proto.Interests)
    InterestsOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Interests.newBuilder() to construct.
  private Interests(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Interests() {
    interest_ = emptyIntList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Interests();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Interests(
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
          case 8: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              interest_ = newIntList();
              mutable_bitField0_ |= 0x00000001;
            }
            interest_.addInt(input.readInt32());
            break;
          }
          case 10: {
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            if (!((mutable_bitField0_ & 0x00000001) != 0) && input.getBytesUntilLimit() > 0) {
              interest_ = newIntList();
              mutable_bitField0_ |= 0x00000001;
            }
            while (input.getBytesUntilLimit() > 0) {
              interest_.addInt(input.readInt32());
            }
            input.popLimit(limit);
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
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        interest_.makeImmutable(); // C
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_Interests_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_Interests_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.example.tittle_tattle.algorithm.proto.Interests.class, com.example.tittle_tattle.algorithm.proto.Interests.Builder.class);
  }

  public static final int INTEREST_FIELD_NUMBER = 1;
  private com.google.protobuf.Internal.IntList interest_;
  /**
   * <code>repeated int32 interest = 1;</code>
   * @return A list containing the interest.
   */
  @java.lang.Override
  public java.util.List<java.lang.Integer>
      getInterestList() {
    return interest_;
  }
  /**
   * <code>repeated int32 interest = 1;</code>
   * @return The count of interest.
   */
  public int getInterestCount() {
    return interest_.size();
  }
  /**
   * <code>repeated int32 interest = 1;</code>
   * @param index The index of the element to return.
   * @return The interest at the given index.
   */
  public int getInterest(int index) {
    return interest_.getInt(index);
  }
  private int interestMemoizedSerializedSize = -1;

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
    getSerializedSize();
    if (getInterestList().size() > 0) {
      output.writeUInt32NoTag(10);
      output.writeUInt32NoTag(interestMemoizedSerializedSize);
    }
    for (int i = 0; i < interest_.size(); i++) {
      output.writeInt32NoTag(interest_.getInt(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    {
      int dataSize = 0;
      for (int i = 0; i < interest_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeInt32SizeNoTag(interest_.getInt(i));
      }
      size += dataSize;
      if (!getInterestList().isEmpty()) {
        size += 1;
        size += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(dataSize);
      }
      interestMemoizedSerializedSize = dataSize;
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
    if (!(obj instanceof com.example.tittle_tattle.algorithm.proto.Interests)) {
      return super.equals(obj);
    }
    com.example.tittle_tattle.algorithm.proto.Interests other = (com.example.tittle_tattle.algorithm.proto.Interests) obj;

    if (!getInterestList()
        .equals(other.getInterestList())) return false;
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
    if (getInterestCount() > 0) {
      hash = (37 * hash) + INTEREST_FIELD_NUMBER;
      hash = (53 * hash) + getInterestList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.example.tittle_tattle.algorithm.proto.Interests parseFrom(
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
  public static Builder newBuilder(com.example.tittle_tattle.algorithm.proto.Interests prototype) {
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
   * Protobuf type {@code com.example.tittle_tattle.algorithm.proto.Interests}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.example.tittle_tattle.algorithm.proto.Interests)
      com.example.tittle_tattle.algorithm.proto.InterestsOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_Interests_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_Interests_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.example.tittle_tattle.algorithm.proto.Interests.class, com.example.tittle_tattle.algorithm.proto.Interests.Builder.class);
    }

    // Construct using com.example.tittle_tattle.algorithm.proto.Interests.newBuilder()
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
      interest_ = emptyIntList();
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.example.tittle_tattle.algorithm.proto.HistoryOuterClass.internal_static_com_example_tittle_tattle_algorithm_proto_Interests_descriptor;
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.Interests getDefaultInstanceForType() {
      return com.example.tittle_tattle.algorithm.proto.Interests.getDefaultInstance();
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.Interests build() {
      com.example.tittle_tattle.algorithm.proto.Interests result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.example.tittle_tattle.algorithm.proto.Interests buildPartial() {
      com.example.tittle_tattle.algorithm.proto.Interests result = new com.example.tittle_tattle.algorithm.proto.Interests(this);
      int from_bitField0_ = bitField0_;
      if (((bitField0_ & 0x00000001) != 0)) {
        interest_.makeImmutable();
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.interest_ = interest_;
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
      if (other instanceof com.example.tittle_tattle.algorithm.proto.Interests) {
        return mergeFrom((com.example.tittle_tattle.algorithm.proto.Interests)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.example.tittle_tattle.algorithm.proto.Interests other) {
      if (other == com.example.tittle_tattle.algorithm.proto.Interests.getDefaultInstance()) return this;
      if (!other.interest_.isEmpty()) {
        if (interest_.isEmpty()) {
          interest_ = other.interest_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureInterestIsMutable();
          interest_.addAll(other.interest_);
        }
        onChanged();
      }
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
      com.example.tittle_tattle.algorithm.proto.Interests parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.example.tittle_tattle.algorithm.proto.Interests) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private com.google.protobuf.Internal.IntList interest_ = emptyIntList();
    private void ensureInterestIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        interest_ = mutableCopy(interest_);
        bitField0_ |= 0x00000001;
       }
    }
    /**
     * <code>repeated int32 interest = 1;</code>
     * @return A list containing the interest.
     */
    public java.util.List<java.lang.Integer>
        getInterestList() {
      return ((bitField0_ & 0x00000001) != 0) ?
               java.util.Collections.unmodifiableList(interest_) : interest_;
    }
    /**
     * <code>repeated int32 interest = 1;</code>
     * @return The count of interest.
     */
    public int getInterestCount() {
      return interest_.size();
    }
    /**
     * <code>repeated int32 interest = 1;</code>
     * @param index The index of the element to return.
     * @return The interest at the given index.
     */
    public int getInterest(int index) {
      return interest_.getInt(index);
    }
    /**
     * <code>repeated int32 interest = 1;</code>
     * @param index The index to set the value at.
     * @param value The interest to set.
     * @return This builder for chaining.
     */
    public Builder setInterest(
        int index, int value) {
      ensureInterestIsMutable();
      interest_.setInt(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 interest = 1;</code>
     * @param value The interest to add.
     * @return This builder for chaining.
     */
    public Builder addInterest(int value) {
      ensureInterestIsMutable();
      interest_.addInt(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 interest = 1;</code>
     * @param values The interest to add.
     * @return This builder for chaining.
     */
    public Builder addAllInterest(
        java.lang.Iterable<? extends java.lang.Integer> values) {
      ensureInterestIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, interest_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 interest = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearInterest() {
      interest_ = emptyIntList();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
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


    // @@protoc_insertion_point(builder_scope:com.example.tittle_tattle.algorithm.proto.Interests)
  }

  // @@protoc_insertion_point(class_scope:com.example.tittle_tattle.algorithm.proto.Interests)
  private static final com.example.tittle_tattle.algorithm.proto.Interests DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.example.tittle_tattle.algorithm.proto.Interests();
  }

  public static com.example.tittle_tattle.algorithm.proto.Interests getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Interests>
      PARSER = new com.google.protobuf.AbstractParser<Interests>() {
    @java.lang.Override
    public Interests parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Interests(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Interests> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Interests> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.example.tittle_tattle.algorithm.proto.Interests getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
