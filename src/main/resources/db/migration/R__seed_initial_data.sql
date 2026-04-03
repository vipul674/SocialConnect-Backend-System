-- Seeding is disabled by default and controlled by: DB_SEED_ENABLED=true
SET @seed_enabled = '${seed_data_enabled}';

INSERT INTO users (username, email, bio, profile_picture_url, created_at, updated_at)
SELECT
    'alex',
    'alex@socialconnect.local',
    'Product-minded runner who enjoys weekend hikes and indie music.',
    'https://cdn.socialconnect.local/profiles/alex.jpg',
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
FROM dual
WHERE @seed_enabled = 'true'
ON DUPLICATE KEY UPDATE
    bio = VALUES(bio),
    profile_picture_url = VALUES(profile_picture_url),
    updated_at = CURRENT_TIMESTAMP(6);

INSERT INTO users (username, email, bio, profile_picture_url, created_at, updated_at)
SELECT
    'maya',
    'maya@socialconnect.local',
    'Coffee enthusiast, UX designer, and city explorer.',
    'https://cdn.socialconnect.local/profiles/maya.jpg',
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
FROM dual
WHERE @seed_enabled = 'true'
ON DUPLICATE KEY UPDATE
    bio = VALUES(bio),
    profile_picture_url = VALUES(profile_picture_url),
    updated_at = CURRENT_TIMESTAMP(6);

INSERT INTO users (username, email, bio, profile_picture_url, created_at, updated_at)
SELECT
    'liam',
    'liam@socialconnect.local',
    'Backend engineer, cyclist, and amateur photographer.',
    'https://cdn.socialconnect.local/profiles/liam.jpg',
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
FROM dual
WHERE @seed_enabled = 'true'
ON DUPLICATE KEY UPDATE
    bio = VALUES(bio),
    profile_picture_url = VALUES(profile_picture_url),
    updated_at = CURRENT_TIMESTAMP(6);

INSERT INTO user_preferences (
    user_id,
    target_min_age,
    target_max_age,
    preferred_location,
    user_interests,
    preferred_interests,
    created_at,
    updated_at
)
SELECT
    u.id,
    24,
    34,
    'Seattle',
    'running,hiking,music',
    'coffee,travel,music',
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
FROM users u
WHERE u.username = 'alex' AND @seed_enabled = 'true'
ON DUPLICATE KEY UPDATE
    target_min_age = VALUES(target_min_age),
    target_max_age = VALUES(target_max_age),
    preferred_location = VALUES(preferred_location),
    user_interests = VALUES(user_interests),
    preferred_interests = VALUES(preferred_interests),
    updated_at = CURRENT_TIMESTAMP(6);

INSERT INTO user_preferences (
    user_id,
    target_min_age,
    target_max_age,
    preferred_location,
    user_interests,
    preferred_interests,
    created_at,
    updated_at
)
SELECT
    u.id,
    25,
    36,
    'Seattle',
    'coffee,design,travel',
    'hiking,photography,music',
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
FROM users u
WHERE u.username = 'maya' AND @seed_enabled = 'true'
ON DUPLICATE KEY UPDATE
    target_min_age = VALUES(target_min_age),
    target_max_age = VALUES(target_max_age),
    preferred_location = VALUES(preferred_location),
    user_interests = VALUES(user_interests),
    preferred_interests = VALUES(preferred_interests),
    updated_at = CURRENT_TIMESTAMP(6);

INSERT INTO user_preferences (
    user_id,
    target_min_age,
    target_max_age,
    preferred_location,
    user_interests,
    preferred_interests,
    created_at,
    updated_at
)
SELECT
    u.id,
    23,
    33,
    'Seattle',
    'cycling,photography,tech',
    'running,coffee,travel',
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
FROM users u
WHERE u.username = 'liam' AND @seed_enabled = 'true'
ON DUPLICATE KEY UPDATE
    target_min_age = VALUES(target_min_age),
    target_max_age = VALUES(target_max_age),
    preferred_location = VALUES(preferred_location),
    user_interests = VALUES(user_interests),
    preferred_interests = VALUES(preferred_interests),
    updated_at = CURRENT_TIMESTAMP(6);

INSERT INTO interactions (actor_user_id, target_user_id, type, created_at)
SELECT
    actor.id,
    target.id,
    'LIKE',
    CURRENT_TIMESTAMP(6)
FROM users actor
JOIN users target ON target.username = 'maya'
WHERE actor.username = 'alex'
  AND @seed_enabled = 'true'
  AND NOT EXISTS (
    SELECT 1
    FROM interactions i
    WHERE i.actor_user_id = actor.id
      AND i.target_user_id = target.id
      AND i.type = 'LIKE'
  );

INSERT INTO interactions (actor_user_id, target_user_id, type, created_at)
SELECT
    actor.id,
    target.id,
    'MATCH',
    CURRENT_TIMESTAMP(6)
FROM users actor
JOIN users target ON target.username = 'alex'
WHERE actor.username = 'maya'
  AND @seed_enabled = 'true'
  AND NOT EXISTS (
    SELECT 1
    FROM interactions i
    WHERE i.actor_user_id = actor.id
      AND i.target_user_id = target.id
      AND i.type = 'MATCH'
  );
