package com.interview_master.domain.collection;

import com.interview_master.common.exception.ApiException;
import com.interview_master.common.exception.ErrorCode;
import com.interview_master.domain.Access;
import com.interview_master.domain.BaseEntity;
import com.interview_master.domain.category.Category;
import com.interview_master.domain.quiz.Quiz;
import com.interview_master.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.interview_master.common.exception.ErrorCode.FORBIDDEN_ACCESS;
import static com.interview_master.domain.Access.PUBLIC;

@Entity
@Table(name = "collections")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

  private String imgUrl;

  private Integer likes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id")
  private User creator;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @Enumerated(EnumType.STRING)
  private Access access;

  @OneToMany(mappedBy = "collection", fetch = FetchType.LAZY)
  private List<Quiz> quizzes;

  // domain logic

  /**
   * 수정된 것만 반영
   */
  public void edit(String newName, String newDescription, String newImgUrl, Category newCategory,
      Access newAccess) {

    boolean nameChanged = newName != null && !newName.equals(this.name);
    boolean descriptionChanged = newDescription != null && !newDescription.equals(this.description);
    boolean imgUrlChanged = newImgUrl != null && !newImgUrl.equals(this.imgUrl);
    boolean categoryChanged =
        newCategory.getId() != null && !newCategory.getId().equals(this.category.getId());
    boolean accessChanged = newAccess != null && newAccess != this.access;

    if (nameChanged) {
      setName(newName);
    }
    if (descriptionChanged) {
      setDescription(newDescription);
    }
    if (imgUrlChanged) {
      setImgUrl(newImgUrl);
    }
    if (categoryChanged) {
      setCategory(newCategory);
    }
    if (accessChanged) {
      setAccess(newAccess);
    }
  }

  public void canAccess(Long userId) {
    boolean isCreator = this.creator.getId().equals(userId);
    boolean isPublic = this.getAccess().equals(PUBLIC);

    if (!isCreator && !isPublic) {
      throw new ApiException(FORBIDDEN_ACCESS, "접근 불가능함");
    }
  }

  /**
   * collection 주인인지 검증하는 로직
   */
  public void isOwner(Long userId) {
    boolean isCreator = this.creator.getId().equals(userId);
    if (!isCreator) {
      throw new ApiException(ErrorCode.FORBIDDEN_MODIFICATION);
    }
  }

  /**
   * 좋아요  로직
   */
  public void like() {
    this.likes++;
  }

  /**
   * 좋아요 취소 로직
   */
  public void unlike() {
    this.likes--;
  }

  // setter
  private void setName(String name) {
    this.name = name;
  }

  private void setAccess(Access access) {
    this.access = access;
  }

  private void setCreator(User creator) {
    this.creator = creator;
  }

  private void setCategory(Category category) {
    this.category = category;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  private void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }
}
