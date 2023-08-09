package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
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

/** This is an auto generated class representing the User type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users", type = Model.Type.USER, version = 1)
public final class User implements Model {
  public static final QueryField ID = field("User", "id");
  public static final QueryField USERNAME = field("User", "username");
  public static final QueryField VERSION = field("User", "_version");
  public static final QueryField LAST_CHANGED_AT = field("User", "_lastChangedAt");
  public static final QueryField DELETED = field("User", "_deleted");
  public static final QueryField CREATED_AT = field("User", "createdAt");
  public static final QueryField UPDATED_AT = field("User", "updatedAt");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String username;
  private final @ModelField(targetType="Conversation") @HasMany(associatedWith = "user", type = Conversation.class) List<Conversation> conversations = null;
  private final @ModelField(targetType="Int", isRequired = true) Integer _version;
  private final @ModelField(targetType="AWSTimestamp", isRequired = true) Temporal.Timestamp _lastChangedAt;
  private final @ModelField(targetType="Boolean") Boolean _deleted;
  private final @ModelField(targetType="AWSDateTime", isRequired = true) Temporal.DateTime createdAt;
  private final @ModelField(targetType="AWSDateTime", isRequired = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getUsername() {
      return username;
  }
  
  public List<Conversation> getConversations() {
      return conversations;
  }
  
  public Integer getVersion() {
      return _version;
  }
  
  public Temporal.Timestamp getLastChangedAt() {
      return _lastChangedAt;
  }
  
  public Boolean getDeleted() {
      return _deleted;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private User(String id, String username, Integer _version, Temporal.Timestamp _lastChangedAt, Boolean _deleted, Temporal.DateTime createdAt, Temporal.DateTime updatedAt) {
    this.id = id;
    this.username = username;
    this._version = _version;
    this._lastChangedAt = _lastChangedAt;
    this._deleted = _deleted;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      User user = (User) obj;
      return ObjectsCompat.equals(getId(), user.getId()) &&
              ObjectsCompat.equals(getUsername(), user.getUsername()) &&
              ObjectsCompat.equals(getVersion(), user.getVersion()) &&
              ObjectsCompat.equals(getLastChangedAt(), user.getLastChangedAt()) &&
              ObjectsCompat.equals(getDeleted(), user.getDeleted()) &&
              ObjectsCompat.equals(getCreatedAt(), user.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), user.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUsername())
      .append(getVersion())
      .append(getLastChangedAt())
      .append(getDeleted())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("User {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("username=" + String.valueOf(getUsername()) + ", ")
      .append("_version=" + String.valueOf(getVersion()) + ", ")
      .append("_lastChangedAt=" + String.valueOf(getLastChangedAt()) + ", ")
      .append("_deleted=" + String.valueOf(getDeleted()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UsernameStep builder() {
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
  public static User justId(String id) {
    return new User(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      username,
      _version,
      _lastChangedAt,
      _deleted,
      createdAt,
      updatedAt);
  }
  public interface UsernameStep {
    VersionStep username(String username);
  }
  

  public interface VersionStep {
    LastChangedAtStep version(Integer version);
  }
  

  public interface LastChangedAtStep {
    CreatedAtStep lastChangedAt(Temporal.Timestamp lastChangedAt);
  }
  

  public interface CreatedAtStep {
    UpdatedAtStep createdAt(Temporal.DateTime createdAt);
  }
  

  public interface UpdatedAtStep {
    BuildStep updatedAt(Temporal.DateTime updatedAt);
  }
  

  public interface BuildStep {
    User build();
    BuildStep id(String id);
    BuildStep deleted(Boolean deleted);
  }
  

  public static class Builder implements UsernameStep, VersionStep, LastChangedAtStep, CreatedAtStep, UpdatedAtStep, BuildStep {
    private String id;
    private String username;
    private Integer _version;
    private Temporal.Timestamp _lastChangedAt;
    private Temporal.DateTime createdAt;
    private Temporal.DateTime updatedAt;
    private Boolean _deleted;
    @Override
     public User build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new User(
          id,
          username,
          _version,
          _lastChangedAt,
          _deleted,
          createdAt,
          updatedAt);
    }
    
    @Override
     public VersionStep username(String username) {
        Objects.requireNonNull(username);
        this.username = username;
        return this;
    }
    
    @Override
     public LastChangedAtStep version(Integer version) {
        Objects.requireNonNull(version);
        this._version = version;
        return this;
    }
    
    @Override
     public CreatedAtStep lastChangedAt(Temporal.Timestamp lastChangedAt) {
        Objects.requireNonNull(lastChangedAt);
        this._lastChangedAt = lastChangedAt;
        return this;
    }
    
    @Override
     public UpdatedAtStep createdAt(Temporal.DateTime createdAt) {
        Objects.requireNonNull(createdAt);
        this.createdAt = createdAt;
        return this;
    }
    
    @Override
     public BuildStep updatedAt(Temporal.DateTime updatedAt) {
        Objects.requireNonNull(updatedAt);
        this.updatedAt = updatedAt;
        return this;
    }
    
    @Override
     public BuildStep deleted(Boolean deleted) {
        this._deleted = deleted;
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
    private CopyOfBuilder(String id, String username, Integer version, Temporal.Timestamp lastChangedAt, Boolean deleted, Temporal.DateTime createdAt, Temporal.DateTime updatedAt) {
      super.id(id);
      super.username(username)
        .version(version)
        .lastChangedAt(lastChangedAt)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .deleted(deleted);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
    }
    
    @Override
     public CopyOfBuilder version(Integer version) {
      return (CopyOfBuilder) super.version(version);
    }
    
    @Override
     public CopyOfBuilder lastChangedAt(Temporal.Timestamp lastChangedAt) {
      return (CopyOfBuilder) super.lastChangedAt(lastChangedAt);
    }
    
    @Override
     public CopyOfBuilder createdAt(Temporal.DateTime createdAt) {
      return (CopyOfBuilder) super.createdAt(createdAt);
    }
    
    @Override
     public CopyOfBuilder updatedAt(Temporal.DateTime updatedAt) {
      return (CopyOfBuilder) super.updatedAt(updatedAt);
    }
    
    @Override
     public CopyOfBuilder deleted(Boolean deleted) {
      return (CopyOfBuilder) super.deleted(deleted);
    }
  }
  
}
