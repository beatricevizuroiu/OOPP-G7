# API docs
This document contains a description of every API endpoint that exists and what data that endpoint expects.

## Versions
Each API endpoint has to be prefixed with a version. The following versions exist.
- /api/v1/ [The original and current version of the API]

## Authorization
To authenticate with the server you have to attach the bearer token that was provided to you by the `/room/{id}/join`
request to every request you sent after that. Currently, you are also required to attach the header even if you don't
have a token for technical reasons.

## The `/room/` endpoints

### POST `/room/create`
Create new room with the information provided. If the action succeeds it will respond with the room.

**Examples:**
```http request
POST /api/v1/room/create
{
    "name": "Example room name",
    "studentPassword": "",
    "moderatorPassword": "M0d3r4t0rP4ssw0rd",
    "startDate": "1970-01-01 00:00:00+00:00"
}
```
```json
{
    "id": "YWieMMQQqjGNoLAwTsSUlatHzr43Z3Gt1Wvs",
    "studentPassword": "",
    "moderatorPassword": "M0d3r4t0rP4ssw0rd",
    "name": "Example room name",
    "startDate": "1970-01-01 00:00:00+00:00",
    "open": false,
    "over": false
}
```

### POST `/room/{room_id}/join`
This endpoint can be used to join a room. You provide a password and based on if that password matches the moderator or
student password are given an authentication token and a role in the response.

**Examples:**
```http request
POST /api/v1/room/YWieMMQQqjGNoLAwTsSUlatHzr43Z3Gt1Wvs/join
{
    "password": "M0d3r4t0rP4ssw0rd",
}
```
```json
{
    "roomId": "YWieMMQQqjGNoLAwTsSUlatHzr43Z3Gt1Wvs",
    "roomName": "Example room name",
    "authorization": "",
    "role": "MODERATOR"
}
```

```http request
POST /api/v1/room/YWieMMQQqjGNoLAwTsSUlatHzr43Z3Gt1Wvs/join
{
    "password": "",
}
```
```json
{
    "roomId": "YWieMMQQqjGNoLAwTsSUlatHzr43Z3Gt1Wvs",
    "roomName": "Example room name",
    "authorization": "",
    "role": "STUDENT"
}
```

### POST `/room/{room_id}/speed`
This endpoint can be used to change the speed value of a room. You provide an integer in the 
form of a SpeedAlterRequest that the server will then use to change the speed value of a room up or down.
If the user tries to access a room that does not exist the server will respond with http status code 404 (NOT_FOUND).
If the user tries to edit the speed by more than one in a request the server will respond with
http status code 400 (BAD_REQUEST).

**Examples:**
`GET /api/v1/room/{room_id}/speed
```http request
POST /api/v1/room/YWieMMQQqjGNoLAwTsSUlatHzr43Z3Gt1Wvs/speed
{
    "speed": 1,
}
```
`No response`

### GET `/room/{room_id}/speed`
Get the speed value of the room with {room_id}. If such a room does not exist the server will respond with 
http status code 404 (NOT_FOUND).

**Examples:**  
`GET /api/v1/room/{room_id}/speed`
```json
{
  "speed": 5
}
```

### DELETE `/room/{room_id}/speed`
Reset the current lecturer speed in the room with {room_id}. This method will respond with http status code 200 (OK).

**Examples:**   
`DELETE /api/v1/room/8fH0vBsgAiQxQ4yiTq5uFI76kEhUv7fK9rKQ/speed`  
`No response`

## The `/room/{room_id}/user/` endpoints
The following endpoints are related to getting userdata and moderating users.

### GET /room/{room_id}/user/{user_id}
Get information about the user with {user_id}. If such a user does not exist the server will respond with
http status code 404 (NOT_FOUND).

**Examples:**  
`GET /api/v1/room/8fH0vBsgAiQxQ4yiTq5uFI76kEhUv7fK9rKQ/user/zvbC3z2ySGKV9G7wrQ1DPsUvYR3uc1UhNgb9`
```json
{
    "id": "zvbC3z2ySGKV9G7wrQ1DPsUvYR3uc1UhNgb9",
    "roomId": "8fH0vBsgAiQxQ4yiTq5uFI76kEhUv7fK9rKQ",
    "nickname": "Nickname",
    "userRole": "STUDENT"
}
```


### GET /room/{room_id}/user/all
Get information about all users. If there are no users the server will respond with http status code 
404 (NOT_FOUND).

**Examples:**  
`GET /api/v1/room/8fH0vBsgAiQxQ4yiTq5uFI76kEhUv7fK9rKQ/user/all`
```json
[
    {
        "id": "zvbC3z2ySGKV9G7wrQ1DPsUvYR3uc1UhNgb9",
        "roomId": "8fH0vBsgAiQxQ4yiTq5uFI76kEhUv7fK9rKQ",
        "nickname": "Nickname",
        "userRole": "STUDENT"
    },
    {
        "id": "v3pNWbkAQOQCtS7VVJomjZVTVoTc5IpI3bj1",
        "roomId": "8fH0vBsgAiQxQ4yiTq5uFI76kEhUv7fK9rKQ",
        "nickname": "Nickname",
        "userRole": "MODERATOR"
    }
]
```

### DELETE /room/{room_id}/user/{user_id}
Ban a user, making them unable to send questions, upvote them and give feedback to lecturer about his/her speed.

**Examples:**  
`DELETE /api/v1/room/8fH0vBsgAiQxQ4yiTq5uFI76kEhUv7fK9rKQ/user/zvbC3z2ySGKV9G7wrQ1DPsUvYR3uc1UhNgb9`
`No response`

## The `/room/{room_id}/question/` endpoints
The following endpoints are related to creating, getting, and answering questions.

### GET `/room/{room_id}/question/{id}`
Get information about the question with {id}. If such a question does not exist the server will respond with
http status code 404 (NOT_FOUND).

**Examples:**  
`GET /api/v1/room/{room_id}/question/1`
```json
{
    "answer": "",
    "answered": true,
    "edited": false,
    "id": 1,
    "postedAt": null,
    "text": "This is a new question",
    "upvotes": 0
}
```

`GET /api/v1/room/{room_id}/question/4`
```json
{
    "answer": "",
    "answered": false,
    "edited": false,
    "id": 4,
    "postedAt": null,
    "text": "This is a new question",
    "upvotes": 0
}
```
### GET `/room/{room_id}/question/all`
Get information about every question known to the system.

**Examples:**  
`GET /api/v1/room/{room_id}/question/all`
```json
[
    {
        "answer": "",
        "answered": true,
        "edited": false,
        "id": 1,
        "postedAt": null,
        "text": "This is a new question",
        "upvotes": 0
    },
    {
        "answer": "",
        "answered": false,
        "edited": false,
        "id": 2,
        "postedAt": null,
        "text": "This is a new question",
        "upvotes": 0
    },
    {
        "answer": "This is an answer to question 3",
        "answered": true,
        "edited": false,
        "id": 3,
        "postedAt": null,
        "text": "This is a new question",
        "upvotes": 0
    },
    {
        "answer": "",
        "answered": false,
        "edited": false,
        "id": 4,
        "postedAt": null,
        "text": "This is a new question",
        "upvotes": 0
    }
]
```

## POST `/questions/{id}`
Edit the question with {id}.

**Example:**
```http request
PUT /api/v1/question/1
{
    "text": "This is updated question body."
}
```
`No response`

## DELETE `/room/{room_id}/questions/{id}`
Delete the question with {id}.

**Examples:**
```http request
DELETE /api/v1/room/{room_id}/question/1
```
`No response`

### POST `/room/{room_id}/question/new`
Create a new question on the server with the text provided.

**Examples:**  
```http request
POST /api/v1/room/{room_id}/question/new
{
    "text": "This is a new question"
}
```
`No response`

### POST `/room/{room_id}/question/{id}/answer`
Answer the question with {id}. If there exists no question with {id} the server will respond with
http status code 404 (NOT_FOUND).

**Examples:**  
```http request
POST /api/v1/room/{room_id}/question/1/answer
{
    "text": ""
}
```
`No response`

```http request
POST /api/v1/room/{room_id}/question/3/answer
{
    "text": "This is an answer to question 3"
}
```
`No response`

## POST `/room/{room_id}/questions/{id}/upvote`
Upvote the question with {id}.

**Example:**
```http request
PUT /api/v1/room/{room_id}/question/1/upvote
```
`No response`  

## DELETE `/room/{room_id}/questions/{id}/upvote`   
Remove an upvote to the question with {id}.
  
  **Example:**  

  `DELETE /api/vi/room/8fH0vBsgAiQxQ4yiTq5uFI76kEhUv7fK9rKQ/question/1/upvote`  
  `No response.`