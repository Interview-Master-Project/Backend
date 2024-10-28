package com.interview_master.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.interview_master.domain.collection.Collection;
import com.interview_master.infrastructure.CollectionRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UpsertCollectionServiceTest {

  @Autowired
  private UpsertCollectionService upsertCollectionService;

  @Autowired
  private CollectionRepository collectionRepository;

  @Test
  public void like() throws InterruptedException {
    int numberOfUsers = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    Long targetCollectionId = 10L;
    CountDownLatch latch = new CountDownLatch(numberOfUsers);

    // 테스트용 사용자 ID 리스트 생성 (1부터 100까지)
    List<Long> userIds = IntStream.rangeClosed(1, numberOfUsers)
        .mapToObj(Long::valueOf)
        .toList();

    for (Long userId : userIds) {
      executorService.submit(() -> {
        try {
          boolean success = false;
          while (!success) {
            try {
              upsertCollectionService.likeCollection(targetCollectionId, userId);
              success = true;
            } catch (Exception e) {
              try {
                Thread.sleep(50); // 잠깐 대기 후 재시도
              } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
              }
            }
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    Collection collection = collectionRepository.findByIdAndIsDeletedFalse(targetCollectionId)
        .orElseThrow(() -> new RuntimeException("Collection not found"));

    assertEquals(numberOfUsers, collection.getLikes());
  }

  @Test
  public void unlike() throws InterruptedException {
    int numberOfUsers = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    Long targetCollectionId = 10L;
    CountDownLatch latch = new CountDownLatch(numberOfUsers);

    // 테스트용 사용자 ID 리스트 생성 (1부터 100까지)
    List<Long> userIds = IntStream.rangeClosed(1, numberOfUsers)
        .mapToObj(Long::valueOf)
        .toList();

    for (Long userId : userIds) {
      executorService.submit(() -> {
        try {
          boolean success = false;
          while (!success) {
            try {
              upsertCollectionService.unlikeCollection(targetCollectionId, userId);
              success = true;
            } catch (Exception e) {
              try {
                Thread.sleep(50); // 잠깐 대기 후 재시도
              } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
              }
            }
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    Collection collection = collectionRepository.findByIdAndIsDeletedFalse(targetCollectionId)
        .orElseThrow(() -> new RuntimeException("Collection not found"));

    assertEquals(0, collection.getLikes());
  }

}