package com.interview_master.dto;

import com.interview_master.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPage {
    private User user;
    private CollectionPage collectionPage;
}