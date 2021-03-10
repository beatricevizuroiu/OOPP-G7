DROP TABLE IF EXISTS questions;

CREATE TABLE questions
(
    ID       INT auto_increment primary key,
    TEXT     TEXT                                   not null,
    ANSWER   TEXT                     default ''    not null,
    POSTEDAT TIMESTAMP WITH TIME ZONE default NOW(),
    UPVOTES  INT                      default 0     not null,
    ANSWERED BOOLEAN                  default FALSE not null,
    EDITED   BOOLEAN                  default FALSE not null
);

INSERT INTO questions (ID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (1, 'This is a question', '', '1970-01-01 00:00:00+00:00', 0, false, false);

INSERT INTO questions (ID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (2, 'This is a question', 'This is an answer to the question', '2021-02-28 11:26:20+00:00', 0, true, false);

INSERT INTO questions (ID, TEXT, ANSWER, POSTEDAT, UPVOTES, ANSWERED, EDITED)
VALUES (3, 'This is a question', '', '1970-01-01 00:00:00+00:00', 20, true, false);