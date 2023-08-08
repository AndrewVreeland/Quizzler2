package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Message type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Messages", type = Model.Type.USER, version = 1)
@Index(name = "byMessage", fields = {"conversationID","content"})
public final class Message implements Model {
  public static final QueryField ID = field("Message", "id");
  public static final QueryField CONTENT = field("Message", "content");
  public static final QueryField CONVERSATION = field("Message", "conversationID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String content;
  private final @ModelField(targetType="Conversation") @BelongsTo(targetName = "conversationID", targetNames = {"conversationID"}, type = Conversation.class) Conversation conversation;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getContent() {
      return content;
  }
  
  public Conversation getConversation() {
      return conversation;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Message(String id, String content, Conversation conversation) {
    this.id = id;
    this.content = content;
    this.conversation = conversation;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Message message = (Message) obj;
      return ObjectsCompat.equals(getId(), message.getId()) &&
              ObjectsCompat.equals(getContent(), message.getContent()) &&
              ObjectsCompat.equals(getConversation(), message.getConversation()) &&
              ObjectsCompat.equals(getCreatedAt(), message.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), message.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getContent())
      .append(getConversation())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Message {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("content=" + String.valueOf(getContent()) + ", ")
      .append("conversation=" + String.valueOf(getConversation()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static ContentStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Message justId(String id) {
    return new Message(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      content,
      conversation);
  }
  public interface ContentStep {
    BuildStep content(String content);
  }
  

  public interface BuildStep {
    Message build();
    BuildStep id(String id);
    BuildStep conversation(Conversation conversation);
  }
  

  public static class Builder implements ContentStep, BuildStep {
    private String id;
    private String content;
    private Conversation conversation;
    @Override
     public Message build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Message(
          id,
          content,
          conversation);
    }
    
    @Override
     public BuildStep content(String content) {
        Objects.requireNonNull(content);
        this.content = content;
        return this;
    }
    
    @Override
     public BuildStep conversation(Conversation conversation) {
        this.conversation = conversation;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String content, Conversation conversation) {
      super.id(id);
      super.content(content)
        .conversation(conversation);
    }
    
    @Override
     public CopyOfBuilder content(String content) {
      return (CopyOfBuilder) super.content(content);
    }
    
    @Override
     public CopyOfBuilder conversation(Conversation conversation) {
      return (CopyOfBuilder) super.conversation(conversation);
    }
  }
  
}
