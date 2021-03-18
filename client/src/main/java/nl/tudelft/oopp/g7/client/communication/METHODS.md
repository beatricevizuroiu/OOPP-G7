# ServerCommunication Methods
Contains helper methods as well as some methods that should apply for all roles.

## Helper
- createRoom
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

**P.S.** you might want to use edit/delete in ServerCommunication since 
we'll change the authorization to server at some point.

# ModeratorServerCommunication Methods
Contains main methods that could be used by a TA.

- retrieveAllQuestions (sorts by upvotes)
- answerQuestion 
- markAsAnswered (sends an empty answer body)

# HttpMethods 
Contains helper HTTP methods.

- send
- get
- post
- put
- delete

**P.S.** Feel free to change anything, just document it.