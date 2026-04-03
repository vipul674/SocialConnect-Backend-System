CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(120) NOT NULL,
    bio VARCHAR(500) NULL,
    profile_picture_url VARCHAR(512) NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE user_preferences (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    target_min_age INT NULL,
    target_max_age INT NULL,
    preferred_location VARCHAR(120) NULL,
    user_interests VARCHAR(500) NULL,
    preferred_interests VARCHAR(500) NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_user_preferences PRIMARY KEY (id),
    CONSTRAINT uk_user_preferences_user_id UNIQUE (user_id),
    CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_user_preferences_target_age CHECK (
        target_min_age IS NULL OR target_max_age IS NULL OR target_min_age <= target_max_age
    )
) ENGINE=InnoDB;

CREATE TABLE interactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    actor_user_id BIGINT NOT NULL,
    target_user_id BIGINT NOT NULL,
    type VARCHAR(16) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_interactions PRIMARY KEY (id),
    CONSTRAINT fk_interactions_actor_user FOREIGN KEY (actor_user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_interactions_target_user FOREIGN KEY (target_user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_interactions_actor_target CHECK (actor_user_id <> target_user_id),
    CONSTRAINT chk_interactions_type CHECK (type IN ('LIKE', 'SKIP', 'MATCH'))
) ENGINE=InnoDB;

CREATE INDEX idx_user_preferences_location ON user_preferences (preferred_location);
CREATE INDEX idx_interactions_actor_user_id ON interactions (actor_user_id);
CREATE INDEX idx_interactions_target_user_id ON interactions (target_user_id);
CREATE INDEX idx_interactions_created_at ON interactions (created_at);
CREATE INDEX idx_interactions_actor_target_type ON interactions (actor_user_id, target_user_id, type);
