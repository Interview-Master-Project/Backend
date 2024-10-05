package com.interview_master.infrastructure;

import com.interview_master.domain.collection.Collection;
import com.interview_master.domain.usercollectionattempt.UserCollectionAttempt;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface UserCollectionAttemptRepository extends Repository<UserCollectionAttempt, Long> {

  List<UserCollectionAttempt> findByCollectionIn(List<Collection> collections);
}
