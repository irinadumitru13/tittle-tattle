// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: history.proto

package com.example.tittle_tattle.algorithm.proto;

public final class HistoryOuterClass {
  private HistoryOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_History_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_History_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_NodeEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_NodeEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_InterestsEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_InterestsEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_Interests_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_Interests_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_EncountersEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_EncountersEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_tittle_tattle_algorithm_proto_Contact_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_tittle_tattle_algorithm_proto_Contact_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rhistory.proto\022)com.example.tittle_tatt" +
      "le.algorithm.proto\"\220\002\n\007History\022O\n\rsocial" +
      "Network\030\001 \001(\01328.com.example.tittle_tattl" +
      "e.algorithm.proto.SocialNetwork\022]\n\024encou" +
      "nteredInterests\030\002 \001(\0132?.com.example.titt" +
      "le_tattle.algorithm.proto.EncounteredInt" +
      "erests\022U\n\020encounteredNodes\030\003 \001(\0132;.com.e" +
      "xample.tittle_tattle.algorithm.proto.Enc" +
      "ounteredNodes\"\304\001\n\rSocialNetwork\022P\n\004node\030" +
      "\001 \003(\0132B.com.example.tittle_tattle.algori" +
      "thm.proto.SocialNetwork.NodeEntry\032a\n\tNod" +
      "eEntry\022\013\n\003key\030\001 \001(\003\022C\n\005value\030\002 \001(\01324.com" +
      ".example.tittle_tattle.algorithm.proto.I" +
      "nterests:\0028\001\"\253\001\n\024EncounteredInterests\022a\n" +
      "\tinterests\030\001 \003(\0132N.com.example.tittle_ta" +
      "ttle.algorithm.proto.EncounteredInterest" +
      "s.InterestsEntry\0320\n\016InterestsEntry\022\013\n\003ke" +
      "y\030\001 \001(\005\022\r\n\005value\030\002 \001(\003:\0028\001\"\035\n\tInterests\022" +
      "\020\n\010interest\030\001 \003(\005\"\332\001\n\020EncounteredNodes\022_" +
      "\n\nencounters\030\001 \003(\0132K.com.example.tittle_" +
      "tattle.algorithm.proto.EncounteredNodes." +
      "EncountersEntry\032e\n\017EncountersEntry\022\013\n\003ke" +
      "y\030\001 \001(\003\022A\n\005value\030\002 \001(\01322.com.example.tit" +
      "tle_tattle.algorithm.proto.Contact:\0028\001\"H" +
      "\n\007Contact\022\020\n\010contacts\030\001 \001(\005\022\020\n\010duration\030" +
      "\002 \001(\003\022\031\n\021lastEncounterTime\030\003 \001(\003B\004H\001P\001b\006" +
      "proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_com_example_tittle_tattle_algorithm_proto_History_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_example_tittle_tattle_algorithm_proto_History_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_History_descriptor,
        new java.lang.String[] { "SocialNetwork", "EncounteredInterests", "EncounteredNodes", });
    internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_descriptor,
        new java.lang.String[] { "Node", });
    internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_NodeEntry_descriptor =
      internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_descriptor.getNestedTypes().get(0);
    internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_NodeEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_SocialNetwork_NodeEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_descriptor,
        new java.lang.String[] { "Interests", });
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_InterestsEntry_descriptor =
      internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_descriptor.getNestedTypes().get(0);
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_InterestsEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredInterests_InterestsEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_com_example_tittle_tattle_algorithm_proto_Interests_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_com_example_tittle_tattle_algorithm_proto_Interests_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_Interests_descriptor,
        new java.lang.String[] { "Interest", });
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_descriptor,
        new java.lang.String[] { "Encounters", });
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_EncountersEntry_descriptor =
      internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_descriptor.getNestedTypes().get(0);
    internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_EncountersEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_EncounteredNodes_EncountersEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_com_example_tittle_tattle_algorithm_proto_Contact_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_com_example_tittle_tattle_algorithm_proto_Contact_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_tittle_tattle_algorithm_proto_Contact_descriptor,
        new java.lang.String[] { "Contacts", "Duration", "LastEncounterTime", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
