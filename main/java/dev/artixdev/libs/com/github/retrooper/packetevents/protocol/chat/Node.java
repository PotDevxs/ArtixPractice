package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat;

import java.util.List;
import java.util.Optional;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class Node {
   private byte flags;
   private List<Integer> children;
   private Optional<Integer> redirectNodeIndex;
   private Optional<String> name;
   private Optional<Integer> parserID;
   private Optional<List<Object>> properties;
   private Optional<ResourceLocation> suggestionsType;

   public Node(byte flags, List<Integer> children, @Nullable Integer redirectNodeIndex, @Nullable String name, @Nullable Integer parserID, @Nullable List<Object> properties, @Nullable ResourceLocation suggestionsType) {
      this.flags = flags;
      this.children = children;
      this.redirectNodeIndex = Optional.ofNullable(redirectNodeIndex);
      this.name = Optional.ofNullable(name);
      this.parserID = Optional.ofNullable(parserID);
      this.properties = Optional.ofNullable(properties);
      this.suggestionsType = Optional.ofNullable(suggestionsType);
   }

   public byte getFlags() {
      return this.flags;
   }

   public void setFlags(byte flags) {
      this.flags = flags;
   }

   public List<Integer> getChildren() {
      return this.children;
   }

   public void setChildren(List<Integer> children) {
      this.children = children;
   }

   public Optional<Integer> getRedirectNodeIndex() {
      return this.redirectNodeIndex;
   }

   public void setRedirectNodeIndex(Optional<Integer> redirectNodeIndex) {
      this.redirectNodeIndex = redirectNodeIndex;
   }

   public Optional<String> getName() {
      return this.name;
   }

   public void setName(Optional<String> name) {
      this.name = name;
   }

   public Optional<Integer> getParserID() {
      return this.parserID;
   }

   public void setParserID(Optional<Integer> parserID) {
      this.parserID = parserID;
   }

   public Optional<List<Object>> getProperties() {
      return this.properties;
   }

   public void setProperties(Optional<List<Object>> properties) {
      this.properties = properties;
   }

   public Optional<ResourceLocation> getSuggestionsType() {
      return this.suggestionsType;
   }

   public void setSuggestionsType(Optional<ResourceLocation> suggestionsType) {
      this.suggestionsType = suggestionsType;
   }
}
