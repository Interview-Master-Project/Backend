package com.interview_master.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.interview_master.common.exception.ApiException;
import com.interview_master.domain.Access;
import com.interview_master.dto.CollectionWithAttempts;
import com.interview_master.dto.DataPage;
import com.interview_master.dto.SortOrder;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SearchCollectionServiceTest {

  @Autowired
  private SearchCollectionService searchCollectionService;

  @Test
  void testSearchCollectionsWithUserId_CategoryIds_Keywords_MaxCorrectRate() {
    // Given
    Long userId = 1L;
    List<Long> categoryIds = Arrays.asList(1L, 2L);
    List<String> keywords = Arrays.asList("Java", "Spring");
    Integer maxCorrectRate = 80;
    DataPage dataPage = new DataPage(0, 10);
    SortOrder sortOrder = SortOrder.LATEST;

    Page<CollectionWithAttempts> result = searchCollectionService.searchCollections(categoryIds,
        keywords, maxCorrectRate, dataPage, sortOrder, userId);

    assertNotNull(result);
    assertEquals(3, result.getContent().size());

    List<CollectionWithAttempts> content = result.getContent();

    // 첫 번째 항목 검증 (가장 최근에 생성된 항목)
    assertEquals("20-24 네이버 면접 기출 모음집 (java backend)",
        content.get(0).getCollection().getDescription());
    assertEquals(0, content.get(0).getTotalAttempts());
    assertEquals(0, content.get(0).getCorrectAttempts());
    assertEquals(3, content.get(0).getQuizCount());

    // 두 번째 항목 검증
    assertEquals("Core Java concepts and advanced topics",
        content.get(1).getCollection().getDescription());
    assertEquals(53, content.get(1).getTotalAttempts());
    assertEquals(39, content.get(1).getCorrectAttempts());
    assertEquals(53, content.get(1).getQuizCount());

    // 세 번째 항목 검증
    assertEquals("자바스크립트", content.get(2).getCollection().getDescription());
    assertEquals(0, content.get(2).getTotalAttempts());
    assertEquals(0, content.get(2).getCorrectAttempts());
    assertEquals(0, content.get(2).getQuizCount());

    // 정렬 순서 검증 (최신순)
    assertTrue(content.get(0).getCollection().getCreatedAt()
        .isAfter(content.get(1).getCollection().getCreatedAt()));
    assertTrue(content.get(1).getCollection().getCreatedAt()
        .isAfter(content.get(2).getCollection().getCreatedAt()));

    // 모든 항목의 정확도가 80% 이하인지 검증
    for (CollectionWithAttempts c : content) {
      double accuracyRate = c.getTotalAttempts() == 0 ? 0 :
          (double) c.getCorrectAttempts() / c.getTotalAttempts() * 100;
      assertTrue(accuracyRate <= 80, "Accuracy rate should be <= 80%");
    }
  }

  @Test
  void testSearchCollectionsWithoutUserId() {
    // Given
    Long userId = null;
    List<Long> categoryIds = List.of(1L, 2L);
    List<String> keywords = List.of();
    Integer maxCorrectRate = null;
    DataPage dataPage = new DataPage(0, 10);
    SortOrder sortOrder = SortOrder.LATEST;

    Page<CollectionWithAttempts> result = searchCollectionService.searchCollections(categoryIds,
        keywords, maxCorrectRate, dataPage, sortOrder, userId);

    assertNotNull(result);
    assertEquals(10, result.getContent().size());

    for (CollectionWithAttempts c : result.getContent()) {
      assertNotEquals(Access.PRIVATE, c.getCollection().getAccess());
      assertEquals(0, c.getTotalAttempts());
      assertEquals(0, c.getCorrectAttempts());
    }
  }

  // categoryId가 없는 경우
  @Test
  void testSearchCollectionsWithNonExistentCategoryId() {
    // Given
    Long userId = 1L;
    List<Long> categoryIds = List.of(9999L); // Assuming this category ID doesn't exist
    List<String> keywords = Arrays.asList("Java", "Spring");
    Integer maxCorrectRate = 80;
    DataPage dataPage = new DataPage(0, 10);
    SortOrder sortOrder = SortOrder.LATEST;

    assertThrows(ApiException.class, () -> searchCollectionService.searchCollections(categoryIds, keywords, maxCorrectRate, dataPage,
        sortOrder, userId), "Should throw ApiException for non-existent category");
  }

  // 존재하는 categoryId와 categoryId가 섞여 있는 경우
  @Test
  void testSearchCollectionsWithMixedExistingAndNonExistingCategoryIds() {
    // Given
    Long userId = 1L;
    List<Long> categoryIds = Arrays.asList(1L, 9999L); // Assuming 1L exists and 9999L doesn't
    List<String> keywords = Arrays.asList("Java", "Spring");
    Integer maxCorrectRate = 80;
    DataPage dataPage = new DataPage(0, 10);
    SortOrder sortOrder = SortOrder.LATEST;

    assertThrows(ApiException.class, () -> searchCollectionService.searchCollections(categoryIds, keywords, maxCorrectRate, dataPage,
        sortOrder, userId), "Should throw ApiException when at least one category doesn't exist");
  }

  // 정답률이 음수인 경우 -> 시도하지 않은 경우들로 반환
  @Test
  void testSearchCollectionsWithNegativeMaxCorrectRate() {
    // Given
    Long userId = 1L;
    List<Long> categoryIds = Arrays.asList(1L, 2L);
    List<String> keywords = null;
    Integer maxCorrectRate = -10; // Negative correct rate
    DataPage dataPage = new DataPage(0, 10);
    SortOrder sortOrder = SortOrder.LATEST;

    Page<CollectionWithAttempts> result = searchCollectionService.searchCollections(categoryIds,
        keywords, maxCorrectRate, dataPage, sortOrder, userId);

    for (CollectionWithAttempts c : result.getContent()) {
      assertEquals(0, c.getTotalAttempts());
      assertEquals(0, c.getCorrectAttempts());
    }
  }

  @Test
  void testSearchCollectionsWithLowestAccuracySorting() {
    // Given
    Long userId = 1L;
    List<Long> categoryIds = Arrays.asList(1L, 2L);
    List<String> keywords = null;
    Integer maxCorrectRate = 100;
    DataPage dataPage = new DataPage(0, 10);
    SortOrder sortOrder = SortOrder.LOWEST_ACCURACY;

    Page<CollectionWithAttempts> result = searchCollectionService.searchCollections(categoryIds,
        keywords, maxCorrectRate, dataPage, sortOrder, userId);

    assertNotNull(result);
    assertFalse(result.getContent().isEmpty(), "Should return results sorted by lowest accuracy");

    // Verify sorting
    double previousAccuracy = -1;
    for (CollectionWithAttempts c : result.getContent()) {
      if (c.getTotalAttempts() == 0) continue;
      double currentAccuracy = (double) c.getCorrectAttempts() / c.getTotalAttempts() * 100;
      assertTrue(currentAccuracy >= previousAccuracy,
          "Results should be sorted by lowest accuracy");
      previousAccuracy = currentAccuracy;
    }
  }
}