package com.interview_master.service;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import com.interview_master.infrastructure.CollectionRepository;
import com.interview_master.infrastructure.QuizRepository;
import com.interview_master.infrastructure.UserRepository;
import com.interview_master.ui.request.CreateQuizInput;
import com.interview_master.ui.request.EditQuizInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpsertQuizService {

  private final QuizRepository quizRepository;
  private final CollectionRepository collectionRepository;
  private final UserRepository userRepository;

  @Transactional
  public Quiz saveQuiz(CreateQuizInput createQuizInput, Long userId) {
    // collection 검증
    Collection collection = collectionRepository.findByIdAndIsDeletedFalse(
            createQuizInput.getCollectionId())
        .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));
    collection.isOwner(userId);

    // 유저 존재 여부 검증
    User user = userRepository.findByIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    // 새 퀴즈 생성
    Quiz newQuiz = new Quiz(
        createQuizInput.getQuestion(),
        createQuizInput.getAnswer(),
        collection,
        user,
        collection.getAccess()
    );

    // 저장
    return quizRepository.save(newQuiz);
  }

  @Transactional
  public Quiz editQuiz(Long quizId, EditQuizInput editQuizInput, Long userId) {
    Collection collection = null;
    if (editQuizInput.getCollectionId() != null) {
      collection = collectionRepository.findByIdAndIsDeletedFalse(editQuizInput.getCollectionId())
          .orElseThrow(() -> new ApiException(ErrorCode.COLLECTION_NOT_FOUND));
      collection.isOwner(userId);
    }

    Quiz quiz = quizRepository.findByIdAndIsDeletedFalse(quizId)
        .orElseThrow(() -> new ApiException(ErrorCode.QUIZ_NOT_FOUND));

    quiz.isOwner(userId);

    quiz.edit(editQuizInput.getQuestion(), editQuizInput.getAnswer(), collection);

    return quiz;
  }

  @Transactional
  public void deleteQuiz(Long quizId, Long userId) {
    Quiz quiz = quizRepository.findByIdAndIsDeletedFalse(quizId)
        .orElseThrow(() -> new ApiException(ErrorCode.QUIZ_NOT_FOUND));

    quiz.isOwner(userId);

    quiz.markDeleted();
  }
}