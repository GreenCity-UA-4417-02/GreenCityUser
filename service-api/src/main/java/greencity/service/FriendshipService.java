package greencity.service;

import greencity.dto.user.UserManagementDto;

import java.util.List;

public interface FriendshipService {
    List<UserManagementDto> findFriendsByUserId(Long id);
}
