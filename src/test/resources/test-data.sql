-- 유저 데이터 삽입
INSERT INTO users (id, name, nickname, sns_id, auth_type, is_delete)
VALUES ('user1', 'Alice', 'alice_nick', 'sns1', 'CUSTOM', false),
       ('user2', 'Bob', 'bob_nick', 'sns2', 'KAKAO', false),
       ('user3', 'Charlie', 'charlie_nick', 'sns3', 'CUSTOM', false);

-- 친구 관계 데이터 삽입
INSERT INTO user_relationship (id, giver_id, receiver_id, relation_type, relation_status, is_delete)
VALUES (1, 'user1', 'user2', 'FRIEND', 'REQUEST', false),
       (2, 'user2', 'user1', 'FRIEND', 'ACCEPT', false),
       (3, 'user3', 'user1', 'FRIEND', 'REQUEST', false);