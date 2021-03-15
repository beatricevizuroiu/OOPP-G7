# ServerCommunication Methods
Contains helper methods as well as some methods that should apply for all roles.

## Helper
- retrieveAllQuestions

## Applies for all roles
- retrieveQuestionById
- retrieveAllAnsweredQuestions
- upvoteQuestion
- editQuestion (student class has wrapper)
- deleteQuestion (student class has wrapper)

# StudentServerCommunication Methods
Contains main methods that could be used by a student.

- askQuestion
- retrieveAllQuestions (sorts by date)
- editQuestion (wraps for authorization) 
- deleteQuestion (wraps)

**P.S.** you might want to use edit/delete in ServerCommunication since 
we'll change the authorization to server at some point.
## Helper
- isOwned

# ModeratorServerCommunication Methods
Contains main methods that could be used by a TA.

- retrieveAllQuestions (sorts by upvotes)
- answerQuestion 

# LecturerServerCommunication Methods
Contains main methods that could be used by a lecturer.

- markAsAnswered (just sends an empty answer body)

# HttpMethods 
Contains helper HTTP methods.

- send
- get
- post
- put
- delete

**P.S.** Feel free to change anything, just document it.