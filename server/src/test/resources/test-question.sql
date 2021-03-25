-- Drop tables if they exist.
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS bannedUsers;
DROP TABLE IF EXISTS rooms;

-- Create tables.
CREATE TABLE IF NOT EXISTS rooms (
    id varchar(36) PRIMARY KEY not NULL,
    studentPassword varchar(32) DEFAULT '' not NULL,
    moderatorPassword varchar(32) not NULL,
    name text not NULL,
    open boolean DEFAULT FALSE not NULL,
    over boolean DEFAULT FALSE not NULL,
    startDate timestamp with time zone not NULL,
    speed int DEFAULT 0 not NULL
);

CREATE TABLE IF NOT EXISTS bannedUsers (
    ip varchar(39) not NULL,
    roomID varchar(36) not NULL,
    reason text DEFAULT '' not NULL,
    FOREIGN KEY (roomID) REFERENCES rooms(id),
    PRIMARY KEY (ip, roomID)
);

CREATE TABLE IF NOT EXISTS users (
    id varchar(36) PRIMARY KEY not NULL,
    roomID varchar(36) not NULL,
    nickname text DEFAULT '' not NULL,
    ip varchar(39) not NULL,
    userRole varchar(32) not NULL,
    token varchar(128) not NULL,
    FOREIGN KEY (roomID) REFERENCES rooms(id)
);

CREATE TABLE IF NOT EXISTS questions (
    id serial PRIMARY KEY not NULL,
    userID varchar(36) not NULL,
    roomID varchar(36) not NULL,
    text text not NULL,
    answer text DEFAULT '' not NULL,
    postedAt timestamp with time zone DEFAULT NOW(),
    upvotes int not NULL DEFAULT 0,
    answered boolean DEFAULT FALSE not NUll,
    edited boolean DEFAULT FALSE not NULL,
    FOREIGN KEY (roomID) REFERENCES rooms(id),
    FOREIGN KEY (userID) REFERENCES users(id)
);


-- Setup rooms.
INSERT INTO rooms (id, studentPassword, moderatorPassword, name, open, over, startDate)
VALUES ('SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', '', 'NQj7RWvT4yQKUJsE', 'Test room', false, false, '1970-01-01 00:00:00+00:00');

-- Setup users.
INSERT INTO users (ID, ROOMID, NICKNAME, IP, USERROLE, TOKEN)
VALUES ('dummy', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'dummy', '127.10.0.1', 'STUDENT', 'Ftqp8J5Ub8PcUO0qJDXGuAooXZZfzZrZbfb51pCeYWDchzf6wyuwtFNzYeEeacE7k82Xn7y6ue9KWxPmP0eENubnz3PMelle4i9NLKb0RiQiVCDK8xdDjuu1uacyHdTC');

-- Setup questions.
INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (1, 'dummy', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'This is a question', '', '1970-01-01 00:00:00+00:00', 0, false, false);

INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (2, 'dummy', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH','This is a question', 'This is an answer to the question', '2021-02-28 11:26:20+00:00', 0, true, false);

INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (3, 'dummy', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH','This is a question', '', '1970-01-01 00:00:00+00:00', 20, true, false);