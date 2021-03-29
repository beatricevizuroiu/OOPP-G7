-- Drop tables if they exist.
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS rooms;

-- Create tables.
CREATE TABLE rooms
(
    id                varchar(36) PRIMARY KEY   not NULL,
    studentPassword   varchar(32) DEFAULT ''    not NULL,
    moderatorPassword varchar(32)               not NULL,
    name              text                      not NULL,
    open              boolean     DEFAULT FALSE not NULL,
    over              boolean     DEFAULT FALSE not NULL,
    startDate         timestamp with time zone  not NUlL,
    speed             int         DEFAULT 0     not NULL
);

CREATE TABLE questions
(
    id       int auto_increment PRIMARY KEY         not NULL,
    userID   int                                    not NULL,
    roomID   varchar(36)                            not NULL,
    text     text                                   not NULL,
    answer   text                     DEFAULT ''    not NULL,
    postedAt timestamp with time zone DEFAULT NOW(),
    upvotes  int                                    not NULL DEFAULT 0,
    answered boolean                  DEFAULT FALSE not NUll,
    edited   boolean                  DEFAULT FALSE not NULL,
    FOREIGN KEY (roomID) REFERENCES rooms (id)
);


-- Setup rooms.
INSERT INTO rooms (id, studentPassword, moderatorPassword, name, open, over, startDate)
VALUES ('SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', '', 'NQj7RWvT4yQKUJsE', 'Test room', false, false, '1970-01-01 00:00:00+00:00');

-- Setup questions.
INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (1, 1, 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH', 'This is a question', '', '1970-01-01 00:00:00+00:00', 0, false, false);

INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (2, 1, 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH','This is a question', 'This is an answer to the question', '2021-02-28 11:26:20+00:00', 0, true, false);

INSERT INTO questions (ID, USERID, ROOMID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (3, 1, 'SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH','This is a question', '', '1970-01-01 00:00:00+00:00', 20, true, false);