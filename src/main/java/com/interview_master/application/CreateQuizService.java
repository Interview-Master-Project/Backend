package com.interview_master.application;

import com.interview_master.domain.quiz.Answer;
import com.interview_master.domain.quiz.CollectionInfo;
import com.interview_master.domain.quiz.Question;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.quiz.QuizCreator;
import com.interview_master.domain.quiz.QuizRepository;
import com.interview_master.domain.user.User;
import com.interview_master.ui.QuizRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateQuizService {

    private final QuizRepository quizRepository;

    @Transactional
    public void createQuiz(QuizRequest quizRequest, User user) {
        CollectionInfo collectionInfo = new CollectionInfo(
            quizRequest.getCollectionId(), quizRequest.getCollectionName());

        Quiz newQuiz = new Quiz(collectionInfo, new Question(quizRequest.getQuestion()),
            new Answer(quizRequest.getAnswer()), new QuizCreator(user.getId(), user.getNickname()),
            quizRequest.getAccess());

        quizRepository.save(newQuiz);
    }

}
