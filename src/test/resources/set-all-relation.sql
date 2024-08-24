-- 1. Insert 20 users into the 'users' table.
INSERT INTO users (id, name, nickname, profile_url, birth, gender, auth_type, is_delete, sns_id,
                   one_signal_id)
VALUES ('1', 'User1', 'nick1', 'profile1.png', '1990-01-01', 'MALE', 'KAKAO', false, 'sns1', 'signal1'),
       ('2', 'User2', 'nick2', 'profile2.png', '1991-01-01', 'FEMALE', 'KAKAO', false, 'sns2',
        'signal2'),
       ('3', 'User3', 'nick3', 'profile3.png', '1992-01-01', 'MALE', 'KAKAO', false, 'sns3', 'signal3'),
       ('4', 'User4', 'nick4', 'profile4.png', '1993-01-01', 'MALE', 'KAKAO', false, 'sns4', 'signal4'),
       ('5', 'User5', 'nick5', 'profile5.png', '1994-01-01', 'MALE', 'KAKAO', false, 'sns5', 'signal5'),
       ('6', 'User6', 'nick6', 'profile6.png', '1995-01-01', 'FEMALE', 'KAKAO', false, 'sns6',
        'signal6'),
       ('7', 'User7', 'nick7', 'profile7.png', '1996-01-01', 'FEMALE', 'KAKAO', false, 'sns7',
        'signal7'),
       ('8', 'User8', 'nick8', 'profile8.png', '1997-01-01', 'MALE', 'KAKAO', false, 'sns8', 'signal8'),
       ('9', 'User9', 'nick9', 'profile9.png', '1998-01-01', 'MALE', 'KAKAO', false, 'sns9', 'signal9'),
       ('10', 'User10', 'nick10', 'profile10.png', '1999-01-01', 'FEMALE', 'KAKAO', false, 'sns10',
        'signal10'),
       ('11', 'User11', 'nick11', 'profile11.png', '2000-01-01', 'FEMALE', 'KAKAO', false, 'sns11',
        'signal11'),
       ('12', 'User12', 'nick12', 'profile12.png', '2001-01-01', 'MALE', 'KAKAO', false, 'sns12',
        'signal12'),
       ('13', 'User13', 'nick13', 'profile13.png', '2002-01-01', 'MALE', 'KAKAO', false, 'sns13',
        'signal13'),
       ('14', 'User14', 'nick14', 'profile14.png', '2003-01-01', 'FEMALE', 'KAKAO', false, 'sns14',
        'signal14'),
       ('15', 'User15', 'nick15', 'profile15.png', '2004-01-01', 'FEMALE', 'KAKAO', false, 'sns15',
        'signal15'),
       ('16', 'User16', 'nick16', 'profile16.png', '2005-01-01', 'MALE', 'KAKAO', false, 'sns16',
        'signal16'),
       ('17', 'User17', 'nick17', 'profile17.png', '2006-01-01', 'MALE', 'KAKAO', false, 'sns17',
        'signal17'),
       ('18', 'User18', 'nick18', 'profile18.png', '2007-01-01', 'FEMALE', 'KAKAO', false, 'sns18',
        'signal18'),
       ('19', 'User19', 'nick19', 'profile19.png', '2008-01-01', 'FEMALE', 'KAKAO', false, 'sns19',
        'signal19'),
       ('20', 'User20', 'nick20', 'profile20.png', '2009-01-01', 'MALE', 'KAKAO', false, 'sns20',
        'signal20');



-- 2. Insert relationships with different statuses into the 'user_relationship' table.

-- Pending Requests
INSERT INTO user_relationship (id, relation_type, is_delete, relation_status, giver_id, receiver_id)
VALUES (1, 'FRIEND', false, 'REQUEST', '1', '2'),    -- User1 sent a friend request to User2
       (2, 'FRIEND', false, 'REQUEST', '2', '3'),    -- User2 sent a friend request to User3
       (9, 'FRIEND', false, 'REQUEST', '9', '10'),   -- User9 sent a friend request to User10
       (13, 'FRIEND', false, 'REQUEST', '13', '14'), -- User13 sent a friend request to User14
       (17, 'FRIEND', false, 'REQUEST', '17', '18');
-- User17 sent a friend request to User18

-- Accepted Requests
INSERT INTO user_relationship (id, relation_type, is_delete, relation_status, giver_id, receiver_id)
VALUES (3, 'FRIEND', false, 'ACCEPT', '3', '4'),    -- User3 sent a friend request to User4, and it was accepted
       (4, 'FRIEND', false, 'ACCEPT', '4', '5'),    -- User4 sent a friend request to User5, and it was accepted
       (10, 'FRIEND', false, 'ACCEPT', '10', '11'), -- User10 sent a friend request to User11, and it was accepted
       (14, 'FRIEND', false, 'ACCEPT', '14', '15'), -- User14 sent a friend request to User15, and it was accepted
       (18, 'FRIEND', false, 'ACCEPT', '18', '19');
-- User18 sent a friend request to User19, and it was accepted

-- Rejected Requests
INSERT INTO user_relationship (id, relation_type, is_delete, relation_status, giver_id, receiver_id)
VALUES (5, 'FRIEND', false, 'REJECT', '5', '6'),    -- User5 sent a friend request to User6, and it was rejected
       (6, 'FRIEND', false, 'REJECT', '6', '7'),    -- User6 sent a friend request to User7, and it was rejected
       (11, 'FRIEND', false, 'REJECT', '11', '12'), -- User11 sent a friend request to User12, and it was rejected
       (15, 'FRIEND', false, 'REJECT', '15', '16'), -- User15 sent a friend request to User16, and it was rejected
       (19, 'FRIEND', false, 'REJECT', '19', '20');
-- User19 sent a friend request to User20, and it was rejected

-- Logically Deleted Requests
INSERT INTO user_relationship (id, relation_type, is_delete, relation_status, giver_id, receiver_id)
VALUES (7, 'FRIEND', true, 'REQUEST', '7', '8'),    -- User7 sent a friend request to User8, but it was deleted
       (8, 'FRIEND', true, 'REQUEST', '8', '9'),    -- User8 sent a friend request to User9, but it was deleted
       (12, 'FRIEND', true, 'REQUEST', '12', '13'), -- User12 sent a friend request to User13, but it was deleted
       (16, 'FRIEND', true, 'REQUEST', '16', '17'), -- User16 sent a friend request to User17, but it was deleted
       (20, 'FRIEND', true, 'REQUEST', '20', '1'); -- User20 sent a friend request to User1, but it was deleted
