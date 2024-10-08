INSERT INTO users (id, name, nickname, profile_url, birth, gender, auth_type, is_delete, sns_id)
VALUES ('1', 'User1', 'sender1', 'default_profile.PNG', '1990-01-01', 'MALE', 'KAKAO', false, 'snsId1'),
       ('2', 'User2', 'receiver1', 'default_profile.PNG', '1991-01-01', 'MALE', 'KAKAO', false, 'snsId2'),
       ('3', 'User3', 'receiver2', 'default_profile.PNG', '1992-01-01', 'MALE', 'KAKAO', false, 'snsId3'),
       ('4', 'User4', 'receiver3', 'default_profile.PNG', '1993-01-01', 'MALE', 'KAKAO', false, 'snsId4'),
       ('5', 'User5', 'receiver4', 'default_profile.PNG', '1994-01-01', 'MALE', 'KAKAO', false, 'snsId5'),
       ('6', 'User6', 'receiver5', 'default_profile.PNG', '1995-01-01', 'MALE', 'KAKAO', false, 'snsId6'),
       ('7', 'User7', 'receiver6', 'default_profile.PNG', '1996-01-01', 'MALE', 'KAKAO', false, 'snsId7'),
       ('8', 'User8', 'receiver7', 'default_profile.PNG', '1997-01-01', 'MALE', 'KAKAO', false, 'snsId8'),
       ('9', 'User9', 'receiver8', 'default_profile.PNG', '1998-01-01', 'MALE', 'KAKAO', false, 'snsId9'),
       ('10', 'User10', 'receiver9', 'default_profile.PNG', '1999-01-01', 'MALE', 'KAKAO', false,
        'snsId10'),
       ('11', 'User11', 'receiver10', 'default_profile.PNG', '2000-01-01', 'MALE', 'KAKAO', false,
        'snsId11');

-- 유저 관계 생성 (User1이 나머지 9명의 유저에게 친구 요청을 보냄) + User2, User3이 User1에게 친구 요청을 보냄
-- User1 -> User2, User3, User4, User5, User6, User7, User8, User9, User10
INSERT INTO user_relationship (id, relation_type, is_delete, relation_status, giver_id, receiver_id)
VALUES (1, 'FRIEND', false, 'REQUEST', '1', '2'),
       (2, 'FRIEND', false, 'REQUEST', '1', '3'),
       (3, 'FRIEND', false, 'REQUEST', '1', '4'),
       (4, 'FRIEND', false, 'REQUEST', '1', '5'),
       (5, 'FRIEND', false, 'REQUEST', '1', '6'),
       (6, 'FRIEND', false, 'REQUEST', '1', '7'),
       (7, 'FRIEND', false, 'REQUEST', '1', '8'),
       (8, 'FRIEND', false, 'REQUEST', '1', '9'),
       (9, 'FRIEND', false, 'REQUEST', '1', '10'),
       (10, 'FRIEND', false, 'REQUEST', '2', '1'),
       (11, 'FRIEND', false, 'REQUEST', '3', '1');