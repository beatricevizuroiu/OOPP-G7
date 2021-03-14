# API docs
This document contains a description of every API endpoint that exists and what data that endpoint expects.

## Versions
Each API endpoint has to be prefixed with a version. The following versions exist.
- /api/v1/ [The original and current version of the API]

## The `/question/` endpoints
The following endpoints are related to creating, getting, and answering questions.

### GET `/question/{id}`
Get information about the question with {id}. If such a question does not exist the server will respond with
http status code 404 (NOT_FOUND).

**Examples:**  
`GET /api/v1/question/1`
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

`GET /api/v1/question/4`
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
### GET `/question/all`
Get information about every question known to the system.

**Examples:**  
`GET /api/v1/question/all`
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

## PUT `/questions/{id}/upvote`
Upvote the question with {id}.

**Example:**
```http request
PUT /api/v1/question/1/upvote
```
`No response`

## PUT `/questions/{id}`
Edit the question with {id}.

**Example:**
```http request
PUT /api/v1/question/1
{
    "text": "This is updated question body."
}
```
`No response`

## DELETE `/questions/{id}`
Delete the question with {id}.

**Examples:**
```http request
DELETE /api/v1/question/1
```
`No response`

### POST `/question/new`
Create a new question on the server with the text provided.

**Examples:**  
```http request
POST /api/v1/question/new
{
    "text": "This is a new question"
}
```
`No response`

### POST `/question/{id}/answer`
Answer the question with {id}. If there exists no question with {id} the server will respond with
http status code 404 (NOT_FOUND).

**Examples:**  
```http request
POST /api/v1/question/1/answer
{
    "answer": ""
}
```
`No response`

```http request
POST /api/v1/question/3/answer
{
    "answer": "This is an answer to question 3"
}
```
`No response`
