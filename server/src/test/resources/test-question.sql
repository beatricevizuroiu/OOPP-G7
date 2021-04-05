-- Drop tables if they exist.
DROP TABLE IF EXISTS upvotes;
DROP TABLE IF EXISTS speeds;
DROP TABLE IF EXISTS pollResults;
DROP TABLE IF EXISTS pollOptions;
DROP TABLE IF EXISTS polls;
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
    over boolean DEFAULT FALSE not NULL,
    startDate timestamp with time zone not NULL,
    speed int DEFAULT 0 not NULL
);

CREATE TABLE IF NOT EXISTS bannedUsers (
    ip varchar(39) not NULL,
    roomID varchar(36) not NULL,
    reason text DEFAULT '' not NULL,
    FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE,
    PRIMARY KEY (ip, roomID)
);

CREATE TABLE IF NOT EXISTS users (
    id varchar(36) PRIMARY KEY not NULL,
    roomID varchar(36) not NULL,
    nickname text DEFAULT '' not NULL,
    ip varchar(39) not NULL,
    userRole varchar(32) not NULL,
    token varchar(128) not NULL,
    FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS questions (
    id serial PRIMARY KEY not NULL,
    userID varchar(36) not NULL,
    roomID varchar(36) not NULL,
    text text not NULL,
    answer text DEFAULT '' not NULL,
    postedAt timestamp with time zone DEFAULT NOW(),
    answered boolean DEFAULT FALSE not NUll,
    edited boolean DEFAULT FALSE not NULL,
    FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE,
    FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS polls(
    id serial PRIMARY KEY not NULL,
    roomID varchar(36) not NULL,
    question text not NULL,
    createdAt timestamp with time zone DEFAULT NOW(),
    publicResults boolean DEFAULT FALSE not NUll,
    isOver boolean DEFAULT FALSE not NULL,
    FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pollOptions(
    id serial PRIMARY KEY not NULL,
    pollID int not NULL,
    roomID varchar(36) not NULL,
    text text not NULL,
    FOREIGN KEY (pollID) REFERENCES polls(id) ON DELETE CASCADE,
    FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pollResults(
    pollID int not NULL,
    roomID varchar(36) not NULL,
    userID varchar(36) not NULL,
    optionID int not NULL,
    PRIMARY KEY (pollID, roomID, optionID),
    FOREIGN KEY (pollID) REFERENCES polls(id) ON DELETE CASCADE,
    FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE,
    FOREIGN KEY (optionID) REFERENCES pollOptions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS upvotes(
    userID varchar(36) not NULL,
    roomID varchar(36) not NULL,
    questionID int not NULL,
    PRIMARY KEY (userID, roomID, questionID),
    FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (questionID) REFERENCES questions(id) ON DELETE CASCADE,
    FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS speeds(
    userID varchar(36) not NULL,
    roomID varchar(36) not NULL,
    speed int not NULL,
    PRIMARY KEY (userID, roomID),
    FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Setup rooms.
INSERT INTO rooms (id, studentPassword, moderatorPassword, name, over, startDate)
VALUES ('SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', '', 'NQj7RWvT4yQKUJsE', 'Test room', false, '1970-01-01 00:00:00+00:00');

-- Setup users.
INSERT INTO users (ID, ROOMID, NICKNAME, IP, USERROLE, TOKEN)
VALUES ('RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'Student name', '127.10.0.1', 'STUDENT', 'Ftqp8J5Ub8PcUO0qJDXGuAooXZZfzZrZbfb51pCeYWDchzf6wyuwtFNzYeEeacE7k82Xn7y6ue9KWxPmP0eENubnz3PMelle4i9NLKb0RiQiVCDK8xdDjuu1uacyHdTC');

INSERT INTO users (ID, ROOMID, NICKNAME, IP, USERROLE, TOKEN)
VALUES ('eWMuhJfj41ShR1BdPGbLg2NKZ7FIEMb4VE6R', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'Moderator name', '127.11.0.1', 'MODERATOR', 'Dm1J7ZsghOtyvFnbMEMWrJDlWHteOGx3rr60stqn405f4sdgPqsj8wO9lWcGkrNGCYf5yH9Y1efaMgnD32hUwaSi3Jsi1mdtXUBK2U7C2HdqdAPdnuUqih2ihmjMk5lG');

-- Setup questions.
INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, ANSWERED, EDITED)
VALUES (1, 'RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'This is a question', '', '1970-01-01 00:00:00+00:00', false, false);

INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, ANSWERED, EDITED)
VALUES (2, 'RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH','This is a question', 'This is an answer to the question', '2021-02-28 11:26:20+00:00', true, false);

INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, ANSWERED, EDITED)
VALUES (3, 'RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH','This is a question', '', '1970-01-01 00:00:00+00:00', true, false);

-- Setup polls.
INSERT INTO polls (ID, roomID, question, createdAt, publicResults, isOver)
VALUES (1, 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'Poll question', '1970-01-01 00:00:00+00:00', false, false);

INSERT INTO pollOptions (ID, pollId, roomId, text)
VALUES (1, 1, 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'Option 1');

INSERT INTO pollOptions (ID, pollId, roomId, text)
VALUES (2, 1, 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'Option 2');

INSERT INTO pollOptions (ID, pollId, roomId, text)
VALUES (3, 1, 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'Option 3');

-- Setup upvotes.
INSERT INTO upvotes (userID, roomID, questionID)
VALUES ('RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 2);
INSERT INTO upvotes (userID, roomID, questionID)
VALUES ('eWMuhJfj41ShR1BdPGbLg2NKZ7FIEMb4VE6R', 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 3);