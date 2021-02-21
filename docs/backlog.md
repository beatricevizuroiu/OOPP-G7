# Backlog
## Goal of the application
Replace the zoom/teams chat with a more streamlined and less cluttered system for asking questions to the lecturer.

## Workflow
Simplified workflow of the application.

1. Lecturer creates a room and is given two links (Student and TA).
2. Students and TAs join via link.
3. Students can ask questions and up/downvote others’ questions.
4. TAs can moderate current questions.
5. Lecturer sees the most upvoted/relevant questions and answers them verbally. The question is deleted.
6. Lecture ends and the remaining questions are deleted.

## Technical details
- Hit refresh get updated list of questions (ugly)
- Server gives data to the client (trickier)

## Must have
- Lecturers can press done or something similar after answering questions.
- Questions marked as done should disappear.
- TAs (moderators) can moderate (delete / edit) questions.
- Allow students to say if the lecturer is going too fast/slow (not useful 35% too fast, find a better alternative) : Trinary system, where lecturer gets a notification if there is x amount (percentage) of students that think the lecturer is going too fast.
- Students can ask questions
- Other students see the questions and upvote
- Avoid interaction between students – it will become distracting
- Simplistic design is really important.
- Sorted questions
- Supports multiple lectures at the same time
- After the lecture is done, the questions disappear (like zoom, jitsi)
- Login with a code


## Should have
- Lecturer sees questions sorted by most important (naive: #of upvotes, other: questions asked now relevant than those from 30 mins ago)
- Lecturer should also be able to moderate
- Lecturer sees the important questions at the top
- Students sorted by new by default (Maybe add sorting options later)
- Students can send as many questions they want
- Scheduling for lectures (calendar and time interactions). Allows for recurring meetings.


## Could have (extras if we have time)
### Moderation (P1)
- Merge questions that are very similar (add more text maybe and sent it – by TAs) (the parent of the question has edit & delete button)
- How many upvotes for merged? (each person is counted once)
- Lecturer creates a set of questions that may show up, students selects the one they want to be answered and the system shows them to the lecturer
- TA can remove questions that have already been answered.


### Polling (P2)
- Polling system (if you have time)

### Tags/Categories (P2)
- Lecturer can create tags/categories
- Students can tag their questions with these tags.

### Accessibility  (P3)
- Color blindness, accessibility (go over accessible software from google) – write in report

### Interactions (P4)
- Allow TAs to give short answers to questions ( extra)
- React with emoji (extra not necessary only if you have 2 days left and are done)

### Will Not Have
- Don’t worry about security. (No need for authentication) – not enough time and resources
- No need for SSO. (too much bureaucracy) – not enough time and resources
- No need for a star rating or other feedback about the lecturer after lecture.
- No private or group chatting – not the scope of this project
- Students cannot answer questions. – not the scope of this project

